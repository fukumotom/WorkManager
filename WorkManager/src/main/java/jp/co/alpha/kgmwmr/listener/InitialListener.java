package jp.co.alpha.kgmwmr.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import jp.co.alpha.kgmwmr.common.util.PropertyUtils;

/**
 * 起動時呼び出しリスナー
 * 
 * @author kigami
 *
 */
@WebListener
public class InitialListener implements ServletContextListener {

	/**
	 * 起動時にプロパティファイルをロード
	 * 
	 * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
	 */
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		// プロパティファイルのロード
		PropertyUtils.load();
	}

	/**
	 * contextDestroyed
	 * 
	 * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
	 */
	@Override
	public void contextDestroyed(ServletContextEvent sce) {
	}

}
