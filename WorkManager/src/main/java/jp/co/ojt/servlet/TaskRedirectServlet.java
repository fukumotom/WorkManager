package jp.co.ojt.servlet;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TaskRedirectServlet extends HttpServlet {

	protected static Logger logger = LoggerFactory.getLogger(TaskRedirectServlet.class);

	private static final long serialVersionUID = -1971958283532858214L;

	public void doGet(HttpServletRequest request, HttpServletResponse response) {

		// リダイレクト(jsp <-> servlet)
		// response.sendRedirect("/WorkManager/SampleServlet");
		try {
			response.sendRedirect("http://localhost:8080/WorkManager/SampleServlet");
		} catch (IOException e) {
			logger.error("redirect失敗:", e);
		}
	}

}
