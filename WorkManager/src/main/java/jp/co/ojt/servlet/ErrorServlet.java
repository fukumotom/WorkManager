package jp.co.ojt.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet("/Error")
public class ErrorServlet extends HttpServlet {

	private static final Logger logger = LoggerFactory.getLogger(ErrorServlet.class);

	private static final long serialVersionUID = -1971958283532858214L;

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		
		logger.info("サーブレット例外発生");
		throw new ServletException();

	}
}
