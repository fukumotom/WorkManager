package jp.co.ojt.servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet("/Menu")
public class MenuServlet extends HttpServlet {

	private static final Logger logger = LoggerFactory.getLogger(MenuServlet.class);

	private static final long serialVersionUID = -1971958283532858214L;

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) {

		logger.info("parameter: " + request.getParameter("submit"));

		RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/menu.jsp");
		try {
			dispatcher.forward(request, response);
		} catch (ServletException | IOException e) {
			logger.error("フォワード失敗", e);
		}
	}
}
