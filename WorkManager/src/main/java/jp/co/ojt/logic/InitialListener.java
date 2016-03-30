package jp.co.ojt.logic;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jp.co.ojt.common.util.PropertyUtils;

public class InitialListener implements ServletContextListener {
	private static Logger logger = LoggerFactory.getLogger(InitialListener.class);

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		// プロパティファイルのロード
		PropertyUtils.load();
		logger.info("★★★初期処理終了");
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// TODO Auto-generated method stub

	}

}
