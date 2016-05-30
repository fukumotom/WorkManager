package jp.kigami.ojt.logic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jp.kigami.ojt.dao.UserRegisterDao;
import jp.kigami.ojt.model.User;

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
