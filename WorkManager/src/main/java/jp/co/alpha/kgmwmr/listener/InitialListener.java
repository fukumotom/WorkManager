package jp.co.alpha.kgmwmr.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jp.co.alpha.kgmwmr.common.util.PropertyUtils;

@WebListener
public class InitialListener implements ServletContextListener {

	private Logger logger = LoggerFactory.getLogger(InitialListener.class);

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		// プロパティファイルのロード
		PropertyUtils.load();
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		logger.debug("処理なし");

	}

}
