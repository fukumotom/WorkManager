package jp.kigami.ojt.servlet;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.kigami.ojt.common.exception.BindFormatException;
import jp.kigami.ojt.common.exception.BusinessException;
import jp.kigami.ojt.common.exception.SystemException;
import jp.kigami.ojt.common.util.ConstantDef;
import jp.kigami.ojt.common.util.ConvertToModelUtils;
import jp.kigami.ojt.common.util.DateUtils;
import jp.kigami.ojt.common.util.InputValidation;
import jp.kigami.ojt.common.util.MsgCodeDef;
import jp.kigami.ojt.common.util.PropertyUtils;
import jp.kigami.ojt.form.WorkRegisterForm;
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
	 * @see
	 * javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest
	 * , javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// 初期表示情報取得
		String userName = request.getUserPrincipal().getName();
		Work inputWork = new Work();
		inputWork.setUserName(userName);
		inputWork.setWorkDate(LocalDate.now());
		logger.info("ユーザ名:{}", userName);

		WorkLogic logic = new WorkLogic();

		List<Work> workList = logic.findWorking(inputWork);

		WorkRegisterForm form = new WorkRegisterForm();
		if (workList.size() == 0) {
			logger.info("仕掛処理なし");
			form.setWork(null);
			form.setWorkingFlg(false);
		} else if (workList.size() == 1) {
			logger.info("仕掛処理あり");
			form.setWork(workList.get(0));
			form.setWorkingFlg(true);
		} else {
			logger.warn("仕掛処理が複数あります。");
			request.setAttribute(ConstantDef.ERROR_MSG,
					PropertyUtils.getValue(MsgCodeDef.MULTI_DATE_EXIT));
			form.setWork(workList.get(0));
			form.setWorkingFlg(true);
		}

		// 作業開始時間(初期表示用)を設定
		form.setNowTime(DateUtils.getNowTimeStr());
		request.setAttribute("form", form);
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
	 * @see
	 * javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest
	 * , javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		WorkRegisterForm form = setForm(request);

		Work inputWork = new Work();
		String userName = request.getUserPrincipal().getName();
		inputWork.setUserName(userName);

		// idチェック
		if (!InputValidation.idCheck(form.getId())) {
			throw new SystemException("不正な入力値です。");
		} else {
			// id正常取得時に設定
			inputWork.setId(Integer.valueOf(form.getId()));
		}

		if (request.getParameter("finishBtn") != null) {
			// 作業終了処理
			try {
				workFinish(inputWork);
			} catch (BusinessException e) {
				request.setAttribute(ConstantDef.ERROR_MSG, e.getMessage());
			}

		} else if (request.getParameter("startBtn") != null) {

			String workingState = request.getParameter("workingFlg");
			boolean workingFlg = false;
			try {
				workingFlg = ConvertToModelUtils.convertBoolean(workingState);
			} catch (BindFormatException e) {
				logger.warn("入力値のバインドに失敗");
				request.setAttribute(ConstantDef.ERROR_MSG, e.getErrMsg());
			}

			String startTime = request.getParameter("startTime");

			if (!startTime.isEmpty() & startTime != null) {
				inputWork.setStartTime(DateUtils.getFomatTime(startTime));
			} else if (startTime.isEmpty()) {
				// 未入力の場合、現在時間で開始
				inputWork.setStartTime(DateUtils.getNowTime());
			} else if (startTime != null) {
				// 入力check
				if (!InputValidation.isTime(startTime)) {
					// ("不正な入力値です。");
				}
			}

			String contents = (String) request.getParameter("contents");
			String note = (String) request.getParameter("note");
			logger.info("入力値：開始時間[{}] 作業内容[{}] 備考[{}]", startTime, contents,
					note);

			if (workingFlg) {

				try {
					workFinish(inputWork);
				} catch (BusinessException e) {
					request.setAttribute(ConstantDef.ERROR_MSG, e.getMessage());
				}
			}

			inputWork.setContents(contents);
			inputWork.setNote(note);
			WorkLogic logic = new WorkLogic();

			try {
				logic.startWork(inputWork);
			} catch (BusinessException e) {
				request.setAttribute(ConstantDef.ERROR_MSG, e.getMessage());
			}
		}
		doGet(request, response);
	}

	private WorkRegisterForm setForm(HttpServletRequest request) {

		WorkRegisterForm form = new WorkRegisterForm();
		form.setId(request.getParameter("id"));

//		boolean workFlg = ConvertToModelUtils.convertBoolean(request
//				.getParameter("workingFlg"));
//		form.setWorkingFlg(workFlg);

		form.setContents(request.getParameter("contents"));
		form.setNote(request.getParameter("note"));

		return form;
	}

	/**
	 * 作業完了処理
	 * 
	 * @param inputWork
	 * @throws BusinessException
	 */
	private void workFinish(Work inputWork) throws BusinessException {
		WorkLogic logic = new WorkLogic();
		inputWork.setEndTime(DateUtils.getNowTime());
		LocalTime startTime = logic.getStartTime(inputWork);
		inputWork.setStartTime(DateUtils.getParseTime(startTime));

		WorkHelper helper = new WorkHelper();
		helper.calcWorkTime(inputWork);

		logic.finishWork(inputWork);
	}
}
