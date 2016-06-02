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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jp.kigami.ojt.common.exception.BusinessException;
import jp.kigami.ojt.common.exception.SystemException;
import jp.kigami.ojt.common.util.ConstantDef;
import jp.kigami.ojt.common.util.ConvertToModelUtils;
import jp.kigami.ojt.common.util.DateUtils;
import jp.kigami.ojt.form.WorkRegisterForm;
import jp.kigami.ojt.logic.WorkLogic;
import jp.kigami.ojt.model.Work;

/**
 * 作業登録/完了処理
 *
 */
@WebServlet("/WorkRegister")
public class WorkRegister extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory
			.getLogger(WorkRegister.class);

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
			form.setWorkingStates(ConstantDef.WOKING_STATE_NONWORKING);
		} else if (workList.size() == 1) {
			logger.info("仕掛処理あり");
			form.setWork(workList.get(0));
			form.setWorkingStates(ConstantDef.WOKING_STATE_WORKING);
		} else {
			logger.warn("仕掛処理が複数あります。");
			request.setAttribute("errMsg", "仕掛処理が複数あります。");
			form.setWork(workList.get(0));
			form.setWorkingStates(ConstantDef.WOKING_STATE_WORKING);
		}

		// 作業開始時間に初期表示用
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

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String userName = request.getUserPrincipal().getName();
		String id = request.getParameter("id");
		Work inputWork = new Work();
		inputWork.setUserName(userName);
		if (id != "") {
			// ID取得時に型変換
			inputWork.setId(ConvertToModelUtils.convertInt(id));
		}

		String action = request.getParameter("action");
		// 作業終了処理
		if ("作業終了".equals(action)) {

			try {

				workFinish(inputWork);

			} catch (BusinessException e) {
				request.setAttribute("errMsg", e.getMessage());
			}
		} else if ("作業開始".equals(action)) {

			String state = request.getParameter("state");
			logger.info("作業状況を取得:{}", state);

			String startTime = request.getParameter("startTime");
			String contents = (String) request.getParameter("contents");
			String note = (String) request.getParameter("note");
			logger.info("入力値：開始時間[{}] 作業内容[{}] 備考[{}]", startTime, contents,
					note);

			if ("作業中".equals(state)) {

				try {
					workFinish(inputWork);
				} catch (BusinessException e) {
					request.setAttribute("errMsg", e.getMessage());
				}
			}

			if (startTime != "") {
				inputWork.setStartTime(DateUtils.getFomatTime(startTime));
			} else {
				// 未入力の場合、現在時間で開始
				inputWork.setStartTime(DateUtils.getNowTime());
			}
			inputWork.setContents(contents);
			inputWork.setNote(note);
			WorkLogic logic = new WorkLogic();

			try {
				logic.startWork(inputWork);
			} catch (BusinessException e) {
				request.setAttribute("errMsg", e.getMessage());
			}
		}
		// 登録画面の再表示
		try {
			response.sendRedirect("/WorkManager/WorkRegister");
		} catch (IOException e) {
			throw new SystemException(e);
		}
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
