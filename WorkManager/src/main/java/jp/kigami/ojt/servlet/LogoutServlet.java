package jp.kigami.ojt.servlet;

import java.io.IOException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jp.kigami.ojt.common.exception.SystemException;
import jp.kigami.ojt.model.Work;

@WebServlet("/Logout")
public class LogoutServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory
			.getLogger(LogoutServlet.class);

	@Override
	public void doGet(HttpServletRequest request,
			HttpServletResponse response) {

		// 未保存作業削除
		Work work = new Work();
		String userName = request.getUserPrincipal().getName();
		work.setUserName(userName);
		WorkHelper helper = new WorkHelper();
		helper.deleteUnSaveWork(work);

		// ログイン情報削除
		HttpSession session = request.getSession();
		session.invalidate();
		logger.info("session削除しました。" + userName);

		try {
			response.sendRedirect("/WorkManager/Menu");
		} catch (IOException e) {
			throw new SystemException("リダイレクト失敗", e);
		}
	}

}
