package jp.co.alpha.kgmwmr.servlet;

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

import jp.co.alpha.kgmwmr.common.exception.SystemException;
import jp.co.alpha.kgmwmr.common.util.ConstantDef;
import jp.co.alpha.kgmwmr.logic.WorkLogic;

/**
 * MenuServletクラス
 * 
 * @author kigami
 *
 */
@WebServlet("/Menu")
public class MenuServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory
			.getLogger(MenuServlet.class);

	/**
	 * メニュー画面表示<br>
	 * 未保存処理を削除、セッション情報から検索条件を削除
	 * 
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public void doGet(HttpServletRequest request,
			HttpServletResponse response) {

		// 検索条件削除
		HttpSession session = request.getSession();
		session.removeAttribute(ConstantDef.CRITERIA);
		logger.info("検索条件を削除。");

		// 未保存作業削除
		String userName = request.getUserPrincipal().getName();
		WorkLogic logic = new WorkLogic();
		logic.deleteUnSaveWork(userName);

		RequestDispatcher dispatcher = request
				.getRequestDispatcher("/WEB-INF/jsp/menu.jsp");
		try {
			dispatcher.forward(request, response);
		} catch (ServletException | IOException e) {
			throw new SystemException("フォワード失敗", e);
		}
	}
}
