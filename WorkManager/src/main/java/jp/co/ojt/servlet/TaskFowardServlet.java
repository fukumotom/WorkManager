package jp.co.ojt.servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TaskFowardServlet extends HttpServlet {

	protected static Logger logger = LoggerFactory.getLogger(TaskFowardServlet.class);

	private static final long serialVersionUID = -1971958283532858214L;

	public void doGet(HttpServletRequest request, HttpServletResponse response) {

		// フォワード(jsp <-> servlet)
		RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/forward.jsp");
		try {
			dispatcher.forward(request, response);
		} catch (ServletException | IOException e) {
			logger.error("フォワード失敗", e);
		}
	}

}
