package jp.co.ojt.servlet;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jp.co.ojt.common.exception.BusinessException;
import jp.co.ojt.logic.WorkListLogic;
import jp.co.ojt.model.Work;

@WebServlet("/WorkList")
public class WorkListServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(WorkListServlet.class);

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// 作業リスト取得
		String userName = request.getUserPrincipal().getName();
		Work work = new Work();
		work.setUserName(userName);
		WorkListLogic logic = new WorkListLogic();
		List<Work> workList = null;
		workList = logic.findAllWork(work);
		request.setAttribute("workList", workList);

		RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/work/workList.jsp");
		dispatcher.forward(request, response);

	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		Work inputWork = (Work) request.getSession().getAttribute("criteria");
		if (inputWork == null) {
			inputWork = new Work();
			request.getSession().setAttribute("criteria", inputWork);
		}

		inputWork.setUserName(request.getUserPrincipal().getName());

		Map<String, String[]> requestParamMap = request.getParameterMap();

		// submitボタン判定
		String actionName = null;
		if (requestParamMap.containsKey("insertBtn")) {
			actionName = "insert";
			logger.info("挿入処理開始:");
		} else if (requestParamMap.containsKey("addBtn")) {
			actionName = "add";
			logger.info("追加処理開始:");
		} else if (requestParamMap.containsKey("deleteBtn")) {
			actionName = "delete";
			logger.info("削除処理開始:");
		}

		String id = request.getParameter("radio");
		WorkListLogic logic = new WorkListLogic();

		if ((id == null) && (!requestParamMap.containsKey("logBtn"))) {
			request.setAttribute("errMsg", "行を選択して下さい。");
		} else {
			inputWork.setId(Integer.valueOf(id));
			logger.info("選択した行ID：{}", id);

			switch (actionName) {

			case "insert":
			case "add":
				try {
					insertWork(inputWork, actionName);
				} catch (BusinessException e) {
					logger.error("挿入失敗", e);
				}
				break;

			case "delete":
				try {
					logic.delete(inputWork);
				} catch (BusinessException e) {
					logger.error("削除失敗", e);
				}
				break;
			}
		}

		List<Work> workList = getWorkList(inputWork);
		request.setAttribute("workList", workList);

		if (requestParamMap.containsKey("logBtn")) {
			logger.info("履歴表示処理開始:");

			// 選択日付取得
			String inputLogDate = request.getParameter("workDate");

			try {
				LocalDate workDate = dateCheck(inputLogDate, request);

				if (request.getParameter("delFlg") != null) {
					logger.info("削除含むラジオボタン押下：{}", request.getParameter("delFlg"));
					inputWork.setDeleteFlg(1);
				} else {
					inputWork.setDeleteFlg(null);
				}

				inputWork.setWorkDate(workDate);
				workList = logic.findAllWork(inputWork);
			} catch (BusinessException e) {
				logger.error("履歴取得に失敗", e);
				workList = getWorkList(inputWork);
			}
			request.setAttribute("workList", workList);

		}
		RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/work/workList.jsp");
		dispatcher.forward(request, response);

	}

	private LocalDate dateCheck(String inputLogDate, HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	private void insertWork(Work inputWork, String actionName) {

		WorkListLogic logic = new WorkListLogic();

		LocalTime time = null;
		// 挿入の場合
		if ("insert".equals(actionName)) {
			time = logic.getStartTime(inputWork);

		} else if ("add".equals(actionName)) {
			time = logic.getEndTime(inputWork);
		}
		inputWork.setStartTime(time);
		inputWork.setEndTime(time);

		logic.insertWork(inputWork);

	}

	private List<Work> getWorkList(Work inputWork) {

		WorkListLogic logic = new WorkListLogic();
		List<Work> workList = logic.findAllWork(inputWork);

		return workList;
	}

}
