package jp.co.ojt.servlet;

import java.io.IOException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet("/Logout")
public class LogoutServlet extends HttpServlet {

	private static Logger logger = LoggerFactory.getLogger(LogoutServlet.class);

	private static final long serialVersionUID = -1971958283532858214L;

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) {

		// ログイン情報削除
		HttpSession session = request.getSession();
		session.invalidate();
		logger.info("session削除しました。" + request.getUserPrincipal().getName());

		try {
			response.sendRedirect("/WorkManager/Menu");
		} catch (IOException e) {
			logger.error("redirect失敗:", e);
		}
	}

}
