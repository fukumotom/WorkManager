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

@WebServlet("/WorkRegister")
public class WorkRegister extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(WorkRegister.class);

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) {

		RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/work/workRegistForm.jsp");
		try {
			dispatcher.forward(request, response);
		} catch (ServletException | IOException e) {
			logger.error("画面遷移失敗", e);
		}
	}
}
