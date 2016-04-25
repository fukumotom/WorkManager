package test.test.ojt.logic;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import test.test.ojt.common.util.PropertyUtils;

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
