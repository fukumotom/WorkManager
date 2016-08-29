package jp.co.alpha.kgmwmr.servlet;

import java.io.IOException;
import java.time.LocalDate;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.co.alpha.kgmwmr.common.exception.BusinessException;
import jp.co.alpha.kgmwmr.common.exception.SystemException;
import jp.co.alpha.kgmwmr.common.util.ConstantDef;
import jp.co.alpha.kgmwmr.common.util.DateUtils;
import jp.co.alpha.kgmwmr.form.WorkEditForm;
import jp.co.alpha.kgmwmr.form.WorkListViewForm;
import jp.co.alpha.kgmwmr.logic.WorkLogic;

@WebServlet("/WorkEdit")
public class WorkEditServlet extends HttpServlet {

	/**
	 * シリアルバージョン
	 */
	private static final long serialVersionUID = 1013971984714290550L;

	/**
	 * 作業リスト画面への遷移パス
	 */
	private static final String WORKLIST_JSP_PATH = "/WEB-INF/jsp/work/workList.jsp";

	/**
	 * 作業編集フォームへの遷移パス
	 */
	private static final String WORKDEIT_JSP_PATH = "/WEB-INF/jsp/work/workEditForm.jsp";

	/**
	 * 作業編集処理
	 * 
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public void doPost(HttpServletRequest request,
			HttpServletResponse response) {

		String requestPath = WORKLIST_JSP_PATH;

		WorkEditForm editForm = setForm(request);

		WorkLogic logic = new WorkLogic();

		try {
			logic.updateWork(editForm);

			// 作業リストへ戻る
			WorkListViewForm viewForm = logic.getWorkListViewForm(
					editForm.getUserName(), LocalDate.now(), false);
			request.setAttribute(ConstantDef.ATTR_FORM, viewForm);

		} catch (BusinessException e1) {
			// 入力チェックエラーの場合、作業編集フォーム再表示
			requestPath = WORKDEIT_JSP_PATH;
			// エラーメッセージ設定
			editForm.setErrMsgs(e1.getMessage());
			request.setAttribute(ConstantDef.ATTR_EDIT_FORM, editForm);
		}

		// 作業リストへ戻る
		WorkListViewForm viewForm = logic.getWorkListViewForm(
				editForm.getUserName(),
				DateUtils.getParseDate(editForm.getWorkDate()), false);
		request.setAttribute(ConstantDef.ATTR_FORM, viewForm);
		RequestDispatcher dispatcher = request
				.getRequestDispatcher(requestPath);
		try {
			dispatcher.forward(request, response);
		} catch (ServletException | IOException e) {
			throw new SystemException("フォワード失敗", e);
		}
	}

	/**
	 * リクエスト情報をformに詰め替え<br>
	 * 入力チェック
	 * 
	 * @param request
	 *            リクエスト情報
	 * @return 作業編集フォーム
	 */
	private WorkEditForm setForm(HttpServletRequest request) {

		WorkEditForm editForm = new WorkEditForm();

		String id = request.getParameter("id");
		editForm.setId(id);
		String userName = request.getUserPrincipal().getName();
		editForm.setUserName(userName);

		String startTime = request.getParameter("startTime");
		editForm.setStartTime(startTime);
		String endTime = request.getParameter("endTime");
		editForm.setEndTime(endTime);

		String contents = (String) request.getParameter("contents");
		editForm.setContents(contents);
		String note = (String) request.getParameter("note");
		editForm.setNote(note);

		String workDate = request.getParameter("workDate");
		editForm.setWorkDate(workDate);

		return editForm;
	}
}