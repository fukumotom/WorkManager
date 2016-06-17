package jp.kigami.ojt.servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jp.kigami.ojt.common.exception.BusinessException;
import jp.kigami.ojt.common.exception.SystemException;
import jp.kigami.ojt.common.util.ConstantDef;
import jp.kigami.ojt.form.WorkFinishForm;
import jp.kigami.ojt.form.WorkRegisterForm;
import jp.kigami.ojt.form.WorkRegisterViewForm;
import jp.kigami.ojt.logic.WorkLogic;

/**
 * 作業登録/完了処理
 *
 */
@WebServlet("/WorkRegister")
public class WorkRegistServlet extends HttpServlet {

	/**
	 * シリアルバージョン
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * ロガー
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(WorkRegistServlet.class);

	/*
	 * 作業登録画面表示用
	 * 
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.
	 * HttpServletRequest , javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// 仕掛処理取得
		String userName = request.getUserPrincipal().getName();
		logger.info("ユーザ名:{}", userName);

		// 画面表示データ取得
		WorkLogic logic = new WorkLogic();
		WorkRegisterViewForm form = logic.getWorkRegisterViewForm(userName);

		request.setAttribute(ConstantDef.ATTR_FORM, form);

		RequestDispatcher dispatcher = request
				.getRequestDispatcher("/WEB-INF/jsp/work/workRegistForm.jsp");

		try {
			dispatcher.forward(request, response);
		} catch (ServletException | IOException e) {
			throw new SystemException("フォワード失敗", e);
		}
	}

	/*
	 * 作業登録画面更新用
	 * 
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.
	 * HttpServletRequest , javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public void doPost(HttpServletRequest request,
			HttpServletResponse response) {

		// ログインユーザ名を取得
		String userName = request.getUserPrincipal().getName();

		WorkLogic logic = new WorkLogic();

		// 画面表示用フォーム
		WorkRegisterViewForm form = new WorkRegisterViewForm();

		// 作業終了処理
		if (request.getParameter("finishBtn") != null) {

			WorkFinishForm finshForm = setfinishForm(request);

			try {
				form = logic.finishWork(userName, finshForm.getId());
			} catch (BusinessException e) {
				form.setErrMsgs(e.getMessage());
			}

			// 作業開始処理
		} else if (request.getParameter("startBtn") != null) {

			WorkRegisterForm registerForm = setRegisterForm(request);

			try {
				form = logic.register(userName, registerForm);
			} catch (BusinessException e) {
				form.setErrMsgs(e.getMessage());
			}
		}

		request.setAttribute(ConstantDef.ATTR_FORM, form);

		RequestDispatcher dispatcher = request
				.getRequestDispatcher("/WEB-INF/jsp/work/workRegistForm.jsp");

		try {
			dispatcher.forward(request, response);
		} catch (ServletException | IOException e) {
			throw new SystemException("フォワード失敗", e);
		}
	}

	/**
	 * 作業終了時のリクエスト情報をフォームに設定
	 * 
	 * @param request
	 * @return
	 */
	private WorkFinishForm setfinishForm(HttpServletRequest request) {

		WorkFinishForm form = new WorkFinishForm();
		form.setId(request.getParameter("id"));

		return form;
	}

	/**
	 * 作業開始時のリクエスト情報をフォームに設定
	 * 
	 * @param request
	 * @return
	 */
	private WorkRegisterForm setRegisterForm(HttpServletRequest request) {

		WorkRegisterForm form = new WorkRegisterForm();
		form.setId(request.getParameter("id"));
		form.setStartTime(request.getParameter("startTime"));
		form.setContents(request.getParameter("contents"));
		form.setNote(request.getParameter("note"));

		return form;
	}

}
