package jp.kigami.ojt.servlet;

import java.io.IOException;
import java.util.ArrayList;

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
import jp.kigami.ojt.common.util.EncryptionUtils;
import jp.kigami.ojt.common.util.InputValidation;
import jp.kigami.ojt.common.util.ValidationResult;
import jp.kigami.ojt.logic.UserRegistLogic;
import jp.kigami.ojt.model.User;

@WebServlet("/RegisterForm")
public class UserRegister extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory
			.getLogger(UserRegister.class);

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException {

		String forwardPath = null;

		String param = request.getParameter("action");

		if (param == null) {
			// ユーザ新規登録ボタン押下時
			forwardPath = "/WEB-INF/jsp/user/userRegistForm.jsp";

		} else if ("confirm".equals(param)) {

			// 更新前入力チェック TODO
			// ArrayList<ValidationResult> checkList = validation(request);
			// for (ValidationResult check : checkList) {
			// if (!check.isCheckResult()) {
			// throw new BusinessException("");
			// }
			// }

			// 登録確認画面から登録ボタン押下時
			HttpSession session = request.getSession();

			// 登録データ取得
			User user = (User) session.getAttribute("registUser");

			// 登録ロジック呼び出し
			UserRegistLogic logic = new UserRegistLogic();
			logic.register(user);

			// 不要なスコープ削除
			session.removeAttribute("registUser");

			forwardPath = "/WEB-INF/jsp/user/userRegistComplete.jsp";

		}

		RequestDispatcher dispacher = request.getRequestDispatcher(forwardPath);
		try {
			dispacher.forward(request, response);
		} catch (ServletException | IOException e) {
			logger.error("foward失敗：", e);
		}
	}

	@Override
	public void doPost(HttpServletRequest request,
			HttpServletResponse response) {

		String actionBtn = request.getParameter("actionBtn");

		try {

			if ("戻る".equals(actionBtn)) {
				response.sendRedirect("/WorkManager/Menu");
			} else if ("確認".equals(actionBtn)) {
				confilm(request, response);
			}
		} catch (IOException | ServletException e) {
			throw new SystemException("画面遷移失敗:", e);
		}
	}

	/**
	 * 確認画面遷移
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	private void confilm(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String forwardPath = "/WEB-INF/jsp/user/userRegistConfirm.jsp";

		// 入力チェック
		ArrayList<ValidationResult> checkList = validation(request);
		for (ValidationResult check : checkList) {
			if (!check.isCheckResult()) {
				// 入力チェックがある場合、入力フォームを再表示
				forwardPath = "/WEB-INF/jsp/user/userRegistForm.jsp";
				break;
			}
		}

		// 登録情報設定
		User user = new User();
		user.setUserName(request.getParameter("j_username"));

		// 暗号化
		String plainPassword = request.getParameter("password");
		String encPassword = EncryptionUtils.getEncPassword(plainPassword);
		user.setPassword(encPassword);

		// セッションに入力情報を保存
		HttpSession session = request.getSession();
		session.setAttribute("registUser", user);

		// 確認画面へフォワード
		RequestDispatcher dispatcher = request
				.getRequestDispatcher(forwardPath);

		dispatcher.forward(request, response);
	}

	private ArrayList<ValidationResult> validation(HttpServletRequest request) {

		ArrayList<ValidationResult> resultList = new ArrayList<>();
		String userName = request.getParameter("j_username");
		String password = request.getParameter("password");
		String passConfilm = request.getParameter("passwordConfirm");

		ValidationResult result = InputValidation.inputSize(userName, 5, 20);
		ValidationResult result2 = InputValidation.inputSize(password, 5, 64);
		ValidationResult result3 = InputValidation.confilm(password,
				passConfilm);
		resultList.add(result);
		resultList.add(result2);
		resultList.add(result3);

		// 入力情報の長さチェック
		if (!result.isCheckResult()) {
			// 画面表示用エラーメッセージ作成
			logger.info("★★入力エラー:{}", result.getErrorMsg());
			request.setAttribute("syze_user", result.getErrorMsg());
		}

		// 入力情報の長さチェック
		if (!result2.isCheckResult()) {
			// 画面表示用エラーメッセージ作成
			logger.info("★★入力エラー:{}", result2.getErrorMsg());
			request.setAttribute("syze_pass", result2.getErrorMsg());
		}

		// パスワード同一確認
		if (!result3.isCheckResult()) {
			// 画面表示用エラーメッセージ作成
			logger.info("★★入力エラー:{}", result3.getErrorMsg());
			request.setAttribute("confirm_pass", result3.getErrorMsg());
		}
		return resultList;
	}
}
