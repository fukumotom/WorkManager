package jp.co.alpha.kgmwmr.servlet;

import java.io.IOException;
import java.time.LocalDate;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jp.co.alpha.kgmwmr.common.exception.BusinessException;
import jp.co.alpha.kgmwmr.common.exception.SystemException;
import jp.co.alpha.kgmwmr.common.util.ConstantDef;
import jp.co.alpha.kgmwmr.common.util.DateUtils;
import jp.co.alpha.kgmwmr.form.WorkEditForm;
import jp.co.alpha.kgmwmr.form.WorkListForm;
import jp.co.alpha.kgmwmr.form.WorkListViewForm;
import jp.co.alpha.kgmwmr.logic.WorkLogic;

/**
 * 作業リスト表示サーブレット
 * 
 * @author kigami
 *
 */
@WebServlet("/WorkList")
public class WorkListServlet extends HttpServlet {

	/**
	 * シリアルバージョン
	 */
	private static final long serialVersionUID = 3175864924537021769L;

	/**
	 * ロガー
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(WorkListServlet.class);

	/**
	 * 作業リスト画面への遷移パス
	 */
	private static final String WORKLIST_JSP_PATH = "/WEB-INF/jsp/work/workList.jsp";

	/**
	 * 作業リスト表示
	 * 
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public void doGet(HttpServletRequest request,
			HttpServletResponse response) {

		// ログインユーザ取得
		String userName = request.getUserPrincipal().getName();

		// 現在日時分の登録作業を編集用に複製
		WorkLogic logic = new WorkLogic();
		logic.copyWork(userName, LocalDate.now());
		WorkListViewForm form = logic.getWorkListViewForm(userName,
				LocalDate.now(), false);

		request.setAttribute(ConstantDef.ATTR_FORM, form);

		RequestDispatcher dispatcher = request
				.getRequestDispatcher(WORKLIST_JSP_PATH);
		try {
			dispatcher.forward(request, response);
		} catch (ServletException | IOException e) {
			throw new SystemException("フォワード失敗", e);
		}
	}

	/**
	 * 作業リスト処理
	 * 
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public void doPost(HttpServletRequest request,
			HttpServletResponse response) {

		// 画面情報をFormに詰める
		WorkListForm inputForm = setForm(request);

		String requestPath = WORKLIST_JSP_PATH;
		WorkLogic logic = new WorkLogic();
		WorkListViewForm viewForm = new WorkListViewForm();
		try {

			if (request.getParameter("insertBtn") != null) {
				logger.info("挿入処理開始:");
				viewForm = logic.insertWork(inputForm);
			} else if (request.getParameter("addBtn") != null) {
				logger.info("追加処理開始:");
				viewForm = logic.addWork(inputForm);
			} else if (request.getParameter("deleteBtn") != null) {
				logger.info("削除処理開始:");
				viewForm = logic.delete(inputForm);
			} else if (request.getParameter("historyBtn") != null) {
				logger.info("履歴表示処理開始:");
				viewForm = logic.history(inputForm);
			} else if (request.getParameter("saveBtn") != null) {
				logger.info("保存処理開始:");
				viewForm = logic.saveWork(inputForm);
			} else {
				// 編集
				logger.info("編集処理開始:");
				requestPath = "/WEB-INF/jsp/work/workEditForm.jsp";
				WorkEditForm editForm = logic.getEditWork(inputForm);
				request.setAttribute(ConstantDef.ATTR_EDIT_FORM, editForm);
			}

			// 作業リスト表示条件をセッションに保持
			WorkListForm creteriaForm = (WorkListForm) request.getSession()
					.getAttribute(ConstantDef.CRITERIA);
			if (creteriaForm == null) {
				// 検索条件を設定
				request.getSession().setAttribute(ConstantDef.CRITERIA,
						inputForm);
			}

		} catch (BusinessException e) {
			logger.warn("入力チェックエラー");
			// 作業リストの再表示
			WorkListForm sessionForm = (WorkListForm) request.getSession()
					.getAttribute(ConstantDef.CRITERIA);
			String listDate = sessionForm.getWorkDate();
			if (listDate == null) {
				listDate = DateUtils.getTodayStr();
			}
			LocalDate date = DateUtils.getParseDate(listDate);
			boolean delete = sessionForm.getDeleteCechk()
					.equals(ConstantDef.DELETE_CHECK_ON);
			viewForm = logic.getWorkListViewForm(sessionForm.getUserName(),
					date, delete);

			// エラーメッセージ設定
			viewForm.setErrMsgs(e.getMessage());
		}

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
	 * リクエスト情報をformに詰め替え
	 * 
	 * @param request
	 *            リクエスト情報
	 * @return 作業リストフォーム
	 */
	private WorkListForm setForm(HttpServletRequest request) {

		WorkListForm form = new WorkListForm();

		String id = request.getParameter("id");
		form.setId(id);
		String userName = request.getUserPrincipal().getName();
		form.setUserName(userName);
		String workDate = request.getParameter("workDate");
		if (workDate.isEmpty()) {
			if (request.getParameter("historyBtn") != null) {
				// 履歴処理の日付未入力はnullを設定
				form.setWorkDate(null);
			} else {
				// 履歴処理以外の日付未入力は表示日付を設定
				String listDate = request.getParameter("listDate");
				form.setWorkDate(listDate);
			}
		} else {
			form.setWorkDate(workDate);
		}

		String deleteCheck = request.getParameter("deleteFlg");
		if (deleteCheck == null) {
			form.setDeleteCechk(ConstantDef.DELETE_CHECK_OFF);
		} else {
			form.setDeleteCechk(deleteCheck);
		}
		return form;
	}
}
