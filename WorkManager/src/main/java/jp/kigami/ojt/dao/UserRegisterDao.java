package jp.kigami.ojt.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jp.kigami.ojt.common.exception.SystemException;
import jp.kigami.ojt.dao.dto.UsersDto;
import jp.kigami.ojt.db.util.CommonDbUtil;
import jp.kigami.ojt.model.User;

public class UserRegisterDao {

	private static Logger logger = LoggerFactory
			.getLogger(UserRegisterDao.class);

	public void insertUsers(User user) {

		// load SQLsentence
		StringBuilder sql1 = CommonDbUtil.readSql("registUser.sql");
		StringBuilder sql2 = CommonDbUtil.readSql("registRole.sql");

		UsersDto dto = mappingModelToDto(user);

		// create sql parameter
		HashMap<Integer, Object> paramMap = createPramMap(sql1, dto);
		HashMap<Integer, Object> paramMap2 = createPramMap(sql2, dto);

		// DB更新
		insertUsers(sql1.toString(), paramMap);
		insertUsers(sql2.toString(), paramMap2);

	}

	private HashMap<Integer, Object> createPramMap(StringBuilder sql,
			UsersDto dto) {

		HashMap<String, Object> dtoMap = CommonDbUtil.createBeanValueMap(dto);

		Map<Integer, String> sqlParamMap = CommonDbUtil.createSqlMap(sql);

		HashMap<Integer, Object> paramMap = new HashMap<>();

		for (Entry<String, Object> dtoEntry : dtoMap.entrySet()) {

			for (Entry<Integer, String> sqlEntry : sqlParamMap.entrySet()) {
				if ((dtoEntry.getKey()).equals(sqlEntry.getValue())) {
					paramMap.put(sqlEntry.getKey(), dtoEntry.getValue());
				}
			}
		}

		return paramMap;

	}

	private UsersDto mappingModelToDto(User user) {

		// 登録情報を設定
		UsersDto dto = new UsersDto();
		dto.setUserName(user.getUserName());
		dto.setPassword(user.getPassword());

		return dto;

	}

	/**
	 * ユーザ登録処理実行
	 * 
	 * @param sql
	 * @param paramMap
	 */
	private void insertUsers(String sql, Map<Integer, Object> paramMap) {

		DataSource ds = CommonDbUtil.lookup();
		try (Connection con = ds.getConnection();
				PreparedStatement pstm = con.prepareStatement(sql);) {

			logger.info("発行SQL:{}", sql);

			// parameter join
			CommonDbUtil.bindParam(pstm, paramMap);

			int resultCnt = pstm.executeUpdate();
			logger.info("{}件登録", resultCnt);

		} catch (SQLException e) {
			logger.error("DB接続失敗", e);
			throw new SystemException(e);
		}
	}

}