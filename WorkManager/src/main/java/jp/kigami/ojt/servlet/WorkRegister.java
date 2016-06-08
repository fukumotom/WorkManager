package jp.kigami.ojt.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.kigami.ojt.common.exception.BusinessException;
import jp.kigami.ojt.common.exception.SystemException;
import jp.kigami.ojt.common.util.ConstantDef;
import jp.kigami.ojt.common.util.DateUtils;
import jp.kigami.ojt.common.util.InputValidation;
import jp.kigami.ojt.common.util.ValidationResult;
import jp.kigami.ojt.form.WorkFinishForm;
import jp.kigami.ojt.form.WorkRegisterForm;
import jp.kigami.ojt.form.WorkRegisterViewForm;
import jp.kigami.ojt.logic.WorkLogic;
import jp.kigami.ojt.model.Work;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 作業登録/完了処理
 *
 */
@WebServlet("/WorkRegister")
public class WorkRegister extends HttpServlet {

	/**
	 * シリアルバージョン
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * ロガー
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(WorkRegister.class);

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
		getWorking(request);

		RequestDispatcher dispatcher = request
				.getRequestDispatcher("/WEB-INF/jsp/work/workRegistForm.jsp");

		try {
			dispatcher.forward(request, response);
		} catch (ServletException | IOException e) {
			throw new SystemException("フォワード失敗", e);
		}
	}

	/**
	 * 仕掛処理取得
	 * 
	 * @param request
	 */
	private void getWorking(HttpServletRequest request) {

		String userName = request.getUserPrincipal().getName();
		logger.info("ユーザ名:{}", userName);

		// 画面表示データ取得
		WorkLogic logic = new WorkLogic();
		WorkRegisterViewForm form = logic.getWorkRegisterViewForm(userName);

		request.setAttribute("viewForm", form);

	}

	/*
	 * 作業登録画面更新用
	 * 
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.
	 * HttpServletRequest , javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// ログインユーザ名を設定
		Work inputWork = new Work();
		String userName = request.getUserPrincipal().getName();
		inputWork.setUserName(userName);

		// 作業終了処理
		if (request.getParameter("finishBtn") != null) {
			WorkFinishForm finshForm = setfinishForm(request);

			// 入力(id)チェック
			String id = finshForm.getId();
			if (!InputValidation.idCheck(id)) {
				throw new SystemException("不正な入力");
			}

			// IDを設定
			inputWork.setId(Integer.valueOf(id));
			try {
				WorkLogic logic = new WorkLogic();
				logic.finishWork(inputWork);
			} catch (BusinessException e) {
				request.setAttribute(ConstantDef.ERROR_MSG, e.getMessage());
			}

			// 作業開始処理
		} else if (request.getParameter("startBtn") != null) {

			WorkRegisterForm registerForm = setRegisterForm(request);

			// 入力チェック
			ValidationResult result = inputCheckWhenStart(registerForm,
					inputWork);
			if (!result.isCheckResult()) {
				request.setAttribute(ConstantDef.ERROR_MSG,
						result.getErrorMsg());
			} else {

				// 入力エラーなしの場合
				logger.info("入力値：開始時間[{}] 作業内容[{}] 備考[{}]",
						registerForm.getStartTime(), registerForm.getContents(),
						registerForm.getNote());
				try {
					register(inputWork);
				} catch (BusinessException e) {
					request.setAttribute(ConstantDef.ERROR_MSG, e.getMessage());
				}
			}
		}

		// 登録画面の再表示
		getWorking(request);
		RequestDispatcher dispatcher = request
				.getRequestDispatcher("/WEB-INF/jsp/work/workRegistForm.jsp");
		try {
			dispatcher.forward(request, response);
		} catch (ServletException | IOException e) {
			throw new SystemException(e);
		}

	}

	/**
	 * 作業開始処理（同期処理） 仕掛作業がある場合は、仕掛作業を終了して 作業を開始する
	 * 
	 * @param inputWork
	 * @throws BusinessException
	 */
	private synchronized void register(Work inputWork)
			throws BusinessException {

		WorkLogic logic = new WorkLogic();

		// 仕掛処理確認
		List<Work> workList = logic.findWorking(inputWork);
		if (workList.size() == 1) {
			// 仕掛処理がある場合、終了
			logic.findWorking(inputWork);
		}

		// 作業開始
		logic.startWork(inputWork);

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
	 * 作業開始ボタン押下時の入力チェック
	 * 
	 * @param form
	 * @param inputWork
	 * @return
	 */
	private ValidationResult inputCheckWhenStart(WorkRegisterForm form,
			Work inputWork) {

		ValidationResult result = new ValidationResult();
		result.setCheckResult(true);

		// idチェック
		String id = form.getId();
		if (!InputValidation.idCheck(id)) {
			throw new SystemException("不正な入力");
		} else {
			if (!id.isEmpty()) {
				inputWork.setId(Integer.valueOf(id));
			}
		}

		// 開始時間チェック
		String startTime = form.getStartTime();
		if (startTime == null) {
			throw new SystemException("不正な入力");
		}
		if (!startTime.isEmpty()) {
			// 入力check
			result = InputValidation.isTime(startTime);
			if (result.isCheckResult()) {
				inputWork.setStartTime(DateUtils.getFomatTime(startTime));
			}
		} else {
			// 未入力の場合、再入力
			result.setCheckResult(false);
			result.setErrorMsg("入力してください。");
		}

		if (result.isCheckResult()) {
			// 作業内容
			String contents = form.getContents();
			if (contents == null) {
				throw new SystemException("不正な入力");
			} else {
				result = InputValidation.inputSize(contents, 0, 40);
				if (result.isCheckResult()) {
					inputWork.setContents(contents);
				}
			}
		}

		if (result.isCheckResult()) {
			// 備考チェック
			String note = form.getNote();
			if (note == null) {
				throw new SystemException("不正な入力");
			} else {
				result = InputValidation.inputSize(note, 0, 40);
				if (result.isCheckResult()) {
					inputWork.setNote(note);
				}
			}
		}

		return result;
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
