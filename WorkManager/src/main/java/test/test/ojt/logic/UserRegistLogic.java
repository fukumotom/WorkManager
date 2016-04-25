package test.test.ojt.logic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import test.test.ojt.dao.UserRegisterDao;
import test.test.ojt.model.User;

public class UserRegistLogic {

	private static final Logger logger = LoggerFactory
			.getLogger(UserRegistLogic.class);

	public void register(User user) {

		logger.info("DB登録処理");

		// DB更新
		UserRegisterDao dao = new UserRegisterDao();
		dao.insertUsers(user);
	}

}
