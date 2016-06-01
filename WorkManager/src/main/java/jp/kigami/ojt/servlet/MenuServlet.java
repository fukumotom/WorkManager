package jp.kigami.ojt.servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jp.kigami.ojt.common.exception.SystemException;
import jp.kigami.ojt.model.Work;

@WebServlet("/Menu")
public class MenuServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory
			.getLogger(MenuServlet.class);

	@Override
	public void doGet(HttpServletRequest request,
			HttpServletResponse response) {

		// 検索条件削除
		HttpSession session = request.getSession();
		session.removeAttribute("criteria");
		logger.info("検索条件を削除。");

		// 未保存作業削除
		Work work = new Work();
		String userName = request.getUserPrincipal().getName();
		work.setUserName(userName);
		WorkHelper helper = new WorkHelper();
		helper.deleteUnSaveWork(work);

		RequestDispatcher dispatcher = request
				.getRequestDispatcher("/WEB-INF/jsp/menu.jsp");
		try {
			dispatcher.forward(request, response);
		} catch (ServletException | IOException e) {
			throw new SystemException("フォワード失敗:", e);
		}
	}
}
