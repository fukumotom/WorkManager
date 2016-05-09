package test.test.ojt.servlet;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import test.test.ojt.common.exception.BindFormatException;
import test.test.ojt.common.exception.BusinessException;
import test.test.ojt.common.exception.SystemException;
import test.test.ojt.common.util.ConvertToModelUtils;
import test.test.ojt.common.util.DateUtils;
import test.test.ojt.logic.WorkLogic;
import test.test.ojt.model.Work;

@WebServlet("/WorkList")
public class WorkListServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory
			.getLogger(WorkListServlet.class);

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// 作業リスト取得
		String userName = request.getUserPrincipal().getName();
		Work work = new Work();
		work.setUserName(userName);
		work.setWorkDate(LocalDate.now());

		WorkLogic logic = new WorkLogic();
		List<Work> workList = null;
		workList = logic.findAllWork(work);
		request.setAttribute("workList", workList);

		request.setAttribute("listDate", DateUtils.getTodayStr());
		logger.info("作業リストの日付:{}", DateUtils.getTodayStr());

		RequestDispatcher dispatcher = request
				.getRequestDispatcher("/WEB-INF/jsp/work/workList.jsp");
		try {
			dispatcher.forward(request, response);
		} catch (ServletException | IOException e) {
			throw new SystemException(e);
		}
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");

		// 作業リスト表示条件をセッションに保持
		Work inputWork = (Work) request.getSession().getAttribute("criteria");
		if (inputWork == null) {
			inputWork = new Work();
			request.getSession().setAttribute("criteria", inputWork);
		}

		// パラメータをモデルに設定
		try {
			// ログインユーザ
			inputWork.setUserName(request.getUserPrincipal().getName());
			// 作業ID
			String id = request.getParameter("id");
			inputWork.setId(ConvertToModelUtils.convertInt(id));
			// 履歴の日付と削除対象チェック
			String workDate = request.getParameter("workDate");

			inputWork.setWorkDate(
					ConvertToModelUtils.convertLocalDate(workDate, "workDate"));

			String deleteFlg = request.getParameter("deleteFlg");
			inputWork.setDeleteFlg(
					ConvertToModelUtils.convertBoolean(deleteFlg));
		} catch (BindFormatException e) {
			request.setAttribute("errMsg", e.getErrMsg());
		}

		String btnAction = request.getParameter("action");
		WorkLogic logic = new WorkLogic();

		// submitボタン判定
		String actionName = "";
		WorkHelper helper = new WorkHelper();
		String listDate = DateUtils.getTodayStr();

		try {

			if ("挿入".equals(btnAction)) {
				actionName = "insert";
				logger.info("挿入処理開始:");
				helper.idCheck(inputWork.getId());
				helper.action(inputWork, actionName);
			} else if ("追加".equals(btnAction)) {
				actionName = "add";
				logger.info("追加処理開始:");
				helper.idCheck(inputWork.getId());
				helper.action(inputWork, actionName);
			} else if ("削除".equals(btnAction)) {
				actionName = "delete";
				logger.info("削除処理開始:");
				helper.idCheck(inputWork.getId());
				helper.action(inputWork, actionName);
			} else if ("履歴".equals(btnAction)) {
				actionName = "history";
				logger.info("履歴表示処理開始:");
				// 履歴日付入力チェック
				helper.dateCheck(inputWork);
			}
		} catch (BusinessException e) {
			if (request.getAttribute("errMsg") == null) {
				request.setAttribute("errMsg", e.getMessage());
			}
		}

		// 作業リストの再表示
		List<Work> workList = logic.findAllWork(inputWork);
		request.setAttribute("workList", workList);
		request.setAttribute("listDate", listDate);

		RequestDispatcher dispatcher = request
				.getRequestDispatcher("/WEB-INF/jsp/work/workList.jsp");

		try {
			dispatcher.forward(request, response);
		} catch (ServletException | IOException e) {
			throw new SystemException(e);
		}

	}

}
