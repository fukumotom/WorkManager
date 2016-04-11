package jp.co.ojt.dao;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jp.co.ojt.dao.dto.UsersDto;
import jp.co.ojt.db.util.CommonDbUtil;
import jp.co.ojt.model.User;

public class UserRegisterDao {

	Logger logger = LoggerFactory.getLogger(UserRegisterDao.class);

	public void insertUsers(User user) {

		// load SQLsentence
		StringBuilder sql1 = CommonDbUtil.readSql("registUser.sql");
		StringBuilder sql2 = CommonDbUtil.readSql("i_role.sql");

		UsersDto dto = mappingModelToDto(user);

		// create sql parameter
		HashMap<Integer, Object> paramMap = createPramMap(sql1, dto);
		HashMap<Integer, Object> paramMap2 = createPramMap(sql2, dto);

		// DB更新
		CommonDbUtil.insertUsers(sql1.toString(), paramMap);
		CommonDbUtil.insertUsers(sql2.toString(), paramMap2);

	}

	private HashMap<Integer, Object> createPramMap(StringBuilder sql, UsersDto dto) {

		HashMap<String, Object> dtoMap = createUserDtoMap(dto);

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

	private HashMap<String, Object> createUserDtoMap(UsersDto dto) {

		HashMap<String, Object> userDtoMap = new HashMap<>();

		String regex = "get([A-Z][a-zA-Z\\d]*)";
		Pattern ptn = Pattern.compile(regex);
		Class<?> c = dto.getClass();
		Method[] methods = c.getMethods();
		for (Method method : methods) {
			// メソッド一覧からgetterを取得
			Matcher mat = ptn.matcher(method.getName());
			while (mat.find()) {
				String getter = mat.group(1);
				if (!"getClass".equals(getter)) {
					String fieldName = mat.group(1);
					// 頭文字を小文字化
					fieldName = fieldName.substring(0, 1).toLowerCase() + fieldName.substring(1);
					try {
						userDtoMap.put(fieldName, method.invoke(dto, null));
					} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
						logger.error("リフレクション失敗", e);
					}
				}
			}
		}
		return userDtoMap;

	}

	private UsersDto mappingModelToDto(User user) {

		// 登録情報を設定
		UsersDto dto = new UsersDto();
		dto.setUserName(user.getUserName());
		dto.setPassword(user.getPassword());

		return dto;

	}

}
