package jp.co.ojt.logic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jp.co.ojt.model.User;

public class UserRegistLogic {

	protected static Logger logger = LoggerFactory.getLogger(UserRegistLogic.class);

	public void register(User user) {

		logger.info("DB登録処理");
	}

	public int createUserId() {
		
		logger.info("ユーザIDを生成");

		return 0;
	}

}
