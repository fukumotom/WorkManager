package jp.co.ojt.dao;

import java.util.ArrayList;

import jp.co.ojt.dao.dto.UsersDto;
import jp.co.ojt.db.util.CommonDbUtil;

public class UserRegisterDao {

	public void insert(UsersDto dto) {

		// load SQLsentence
		String[] sqls = new String[2];
		sqls[0] = CommonDbUtil.readSql("registUser.sql");
		sqls[1] = CommonDbUtil.readSql("i_role.sql");

		// create sql parameter TODO

		ArrayList<String> usersParamList = new ArrayList<>();
		usersParamList.add(dto.getUserName());
		usersParamList.add(dto.getPassword());

		ArrayList<String> roleParamList = new ArrayList<>();
		roleParamList.add(dto.getUserName());

		// DB更新
		CommonDbUtil.insertDB(sqls[0], usersParamList);
		CommonDbUtil.insertDB(sqls[1], roleParamList);

	}

}
