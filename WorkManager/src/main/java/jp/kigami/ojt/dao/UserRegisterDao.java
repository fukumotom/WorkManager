package jp.kigami.ojt.dao;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import jp.kigami.ojt.dao.dto.UsersDto;
import jp.kigami.ojt.db.util.CommonDbUtil;
import jp.kigami.ojt.model.User;

public class UserRegisterDao {

	public void insertUsers(User user) {

		// load SQLsentence
		StringBuilder sql1 = CommonDbUtil.readSql("registUser.sql");
		StringBuilder sql2 = CommonDbUtil.readSql("registRole.sql");

		UsersDto dto = mappingModelToDto(user);

		// create sql parameter
		HashMap<Integer, Object> paramMap = createPramMap(sql1, dto);
		HashMap<Integer, Object> paramMap2 = createPramMap(sql2, dto);

		// DB更新
		CommonDbUtil.insertUsers(sql1.toString(), paramMap);
		CommonDbUtil.insertUsers(sql2.toString(), paramMap2);

	}

	private HashMap<Integer, Object> createPramMap(StringBuilder sql,
			UsersDto dto) {

		HashMap<String, Object> dtoMap = CommonDbUtil.createDtoMap(dto,
				UsersDto.class);

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

}
