package jp.co.ojt.logic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jp.co.ojt.common.util.PropertyUtils;
import jp.co.ojt.model.User;

public class UserRegistLogic {

	private static Logger logger = LoggerFactory.getLogger(UserRegistLogic.class);

	public void register(User user) {

		logger.info("DB登録処理");

		String url = PropertyUtils.getValue("url");
		logger.info("プロパティからの取得:" + url);

		// try(DriverManager.getConnection(url, user, password))

	}

}
