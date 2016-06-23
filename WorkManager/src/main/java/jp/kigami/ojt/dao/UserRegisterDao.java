package jp.kigami.ojt.dao;

import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jp.kigami.ojt.dao.dto.UserDto;
import jp.kigami.ojt.db.util.CommonDbUtil;
import jp.kigami.ojt.model.User;

public class UserRegisterDao {

	private static Logger logger = LoggerFactory
			.getLogger(UserRegisterDao.class);

	/**
	 * ユーザ登録SQL発行
	 * 
	 * @param user
	 */
	public void insertUsers(User user) {

		// load SQLsentence
		StringBuilder sql1 = CommonDbUtil.readSql("registUser.sql");
		StringBuilder sql2 = CommonDbUtil.readSql("registRole.sql");

		// dtoに詰め替え
		UserDto dto = new UserDto();
		CommonDbUtil.beanMaping(user, dto);

		// create sql parameter
		HashMap<Integer, Object> paramMap = CommonDbUtil.createParamMap(sql1,
				dto);
		HashMap<Integer, Object> paramMap2 = CommonDbUtil.createParamMap(sql2,
				dto);

		// DB更新
		int cnt = CommonDbUtil.updata(sql1.toString(), paramMap);
		logger.info("{}件登録", cnt);
		int cnt2 = CommonDbUtil.updata(sql2.toString(), paramMap2);
		logger.info("{}件登録", cnt2);
	}

	/**
	 * ユーザ検索SQL発行 TODO トランザクション
	 * 
	 * @param user
	 * @return
	 */
	public List<UserDto> findUser(User user) {

		// sql読み込み
		StringBuilder sql = CommonDbUtil.readSql("findUser.sql");

		// dtoにmodelを詰め替え
		UserDto userDto = new UserDto();
		CommonDbUtil.beanMaping(user, userDto);

		// SQL発行パラメータ取得
		HashMap<Integer, Object> paramMap = CommonDbUtil.createParamMap(sql,
				userDto);

		// SQL発行
		List<UserDto> dtoList = CommonDbUtil.getDtoList(sql.toString(),
				paramMap, UserDto.class);

		return dtoList;
	}
}