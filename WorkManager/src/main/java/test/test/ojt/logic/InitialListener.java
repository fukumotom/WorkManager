package jp.co.ojt.logic;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import jp.co.ojt.common.util.PropertyUtils;

@WebListener
public class InitialListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		// プロパティファイルのロード
		PropertyUtils.load();
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// TODO Auto-generated method stub

	}

}
