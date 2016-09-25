package jp.co.alpha.kgmwmr.servlet;

import java.io.IOException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jp.co.alpha.kgmwmr.common.exception.SystemException;
import jp.co.alpha.kgmwmr.common.util.MsgCodeDef;
import jp.co.alpha.kgmwmr.logic.WorkLogic;

@WebServlet("/Logout")
public class LogoutServlet extends HttpServlet {

	/**
	 * シリアルバージョン
	 */
	private static final long serialVersionUID = -6810456018218534289L;

	/**
	 * ロガー
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(LogoutServlet.class);

	/**
	 * ログアウト処理
	 * 
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public void doGet(HttpServletRequest request,
			HttpServletResponse response) {

		// 未保存作業削除
		String userName = request.getUserPrincipal().getName();
		WorkLogic logic = new WorkLogic();
		logic.deleteUnSaveWork(userName);

		// ログイン情報削除
		HttpSession session = request.getSession();
		session.invalidate();
		logger.debug("sessionを削除しました。 :userName:{}" + userName);

		try {
			response.sendRedirect("/WorkManager/Menu");
		} catch (IOException e) {
			throw new SystemException(e, MsgCodeDef.ERR_REDIRECT);
		}
	}

}
