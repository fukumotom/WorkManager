package jp.co.ojt.logic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jp.co.ojt.dao.UserRegisterDao;
import jp.co.ojt.dao.dto.UsersDto;
import jp.co.ojt.model.User;

public class UserRegistLogic {

	private static Logger logger = LoggerFactory.getLogger(UserRegistLogic.class);

	public void register(User user) {

		logger.info("DB登録処理");

		// 登録情報を設定
		UsersDto dto = new UsersDto();
		dto.setUserName(user.getUserName());
		dto.setPassword(user.getPassword());

		// DB更新
		UserRegisterDao dao = new UserRegisterDao();
		dao.insert(dto);
	}

}
