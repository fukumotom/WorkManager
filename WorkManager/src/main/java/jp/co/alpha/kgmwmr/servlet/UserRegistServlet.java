package jp.co.alpha.kgmwmr.servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import jp.co.alpha.kgmwmr.common.exception.SystemException;
import jp.co.alpha.kgmwmr.common.util.ConstantDef;
import jp.co.alpha.kgmwmr.common.util.EncryptionUtils;
import jp.co.alpha.kgmwmr.common.util.MsgCodeDef;
import jp.co.alpha.kgmwmr.form.UserForm;
import jp.co.alpha.kgmwmr.logic.UserRegistLogic;

/**
 * ユーザ登録処理のServletクラス
 * 
 * @author kigami
 *
 */
@WebServlet("/RegisterForm")
public class UserRegistServlet extends HttpServlet {

	/**
	 * シリアルバージョン
	 */
	private static final long serialVersionUID = 421709107098888902L;

	/**
	 * ユーザ登録フォームへの遷移パス
	 */
	private static final String USER_REGIST_FORM_JSP_PATH = "/WEB-INF/jsp/user/userRegistForm.jsp";

	/**
	 * ユーザ登録確認画面への遷移パス
	 */
	private static final String USER_REGIST_CONFIRM_JSP_PATH = "/WEB-INF/jsp/user/userRegistConfirm.jsp";
	
	
	/**
	 * ユーザ登録完了画面への遷移パス
	 */
	private static final String USER_REGIST_COMPLETE_JSP_PATH = "/WEB-INF/jsp/user/userRegistComplete.jsp";

	/**
	 * ユーザ登録フォーム表示、ユーザ登録完了画面表示
	 * 
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException {

		String forwardPath = null;

		if (request.getParameter("userRegit") != null) {
			// ユーザ新規登録ボタン押下時
			forwardPath = USER_REGIST_FORM_JSP_PATH;

		} else if (request.getParameter("registBtn") != null) {

			// 登録確認画面から登録ボタン押下時
			HttpSession session = request.getSession();

			// 登録データ取得
			UserForm userForm = (UserForm) session
					.getAttribute(ConstantDef.ATTR_FORM);

			// 登録ロジック呼び出し
			UserRegistLogic logic = new UserRegistLogic();
			logic.register(userForm);

			// 不要なスコープ削除
			session.removeAttribute(ConstantDef.ATTR_FORM);

			forwardPath = USER_REGIST_COMPLETE_JSP_PATH;

		}

		RequestDispatcher dispacher = request.getRequestDispatcher(forwardPath);
		try {
			dispacher.forward(request, response);
		} catch (ServletException | IOException e) {
			throw new SystemException(e, MsgCodeDef.ERR_FORWARD);
		}
	}

	/**
	 * ユーザ登録処理
	 * 
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public void doPost(HttpServletRequest request,
			HttpServletResponse response) {

		UserForm userForm = getUserForm(request);

		try {

			if (request.getParameter("returnBtn") != null) {
				// 戻るボタン押下時
				response.sendRedirect("/WorkManager/Menu");

			} else if (request.getParameter("confirmBtn") != null) {

				// 確認ボタン押下時
				String forwardPath = USER_REGIST_CONFIRM_JSP_PATH;

				UserRegistLogic logic = new UserRegistLogic();
				// 画面表示情報設定
				UserForm viewForm = logic.inputvalidation(userForm);
				viewForm.setUserName(userForm.getUserName());
				if (!viewForm.getErrMsgs().isEmpty()) {
					// 入力チェックエラーの場合、ユーザ登録フォームへ遷移
					forwardPath = USER_REGIST_FORM_JSP_PATH;
				}

				// セッションにユーザ情報を保存
				// パスワード暗号化
				String plainPassword = userForm.getPassword();
				String encPassword = EncryptionUtils
						.getEncPassword(plainPassword);
				userForm.setPassword(encPassword);
				userForm.setConfirmPassword(encPassword);

				HttpSession session = request.getSession();
				session.setAttribute(ConstantDef.ATTR_FORM, userForm);

				request.setAttribute(ConstantDef.ATTR_FORM, viewForm);

				// 確認画面へフォワード
				RequestDispatcher dispatcher = request
						.getRequestDispatcher(forwardPath);
				dispatcher.forward(request, response);

			}
		} catch (IOException | ServletException e) {
			throw new SystemException(e, MsgCodeDef.ERR_REDIRECT);
		}
	}

	/**
	 * 画面情報をユーザフォームに詰める
	 * 
	 * @param request
	 * @return
	 */
	private UserForm getUserForm(HttpServletRequest request) {

		UserForm form = new UserForm();
		form.setUserName(request.getParameter("j_username"));
		form.setPassword(request.getParameter("password"));
		form.setConfirmPassword(request.getParameter("confirmPassword"));

		return form;
	}
}