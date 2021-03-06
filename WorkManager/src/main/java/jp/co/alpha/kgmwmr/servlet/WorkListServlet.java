package jp.co.alpha.kgmwmr.servlet;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
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
import jp.co.alpha.kgmwmr.common.util.MsgCodeDef;
import jp.co.alpha.kgmwmr.common.util.PropertyUtils;
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
	 * 作業編集フォームへの遷移パス
	 */
	private static final String WORKEDITFORM_JSP_PATH = "/WEB-INF/jsp/work/workEditForm.jsp";

	/**
	 * 作業リスト画面表示
	 * 
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public void doGet(HttpServletRequest request,
			HttpServletResponse response) {

		// ログインユーザ取得
		String userName = request.getUserPrincipal().getName();

		WorkListForm criteria = setForm(request);

		// 初期表示の作業リスト検索条件をセッションに設定
		request.getSession().setAttribute(ConstantDef.CRITERIA, criteria);

		WorkLogic logic = new WorkLogic();
		logic.copyTodayWork(userName);
		WorkListViewForm form = logic.getWorkListViewForm(userName,
				LocalDate.now(), false);

		// 作業リスト表示条件をセッションに保持
		WorkListForm creteriaForm = (WorkListForm) request.getSession()
				.getAttribute(ConstantDef.CRITERIA);
		if (creteriaForm == null) {
			// 検索条件を設定
			request.getSession().setAttribute(ConstantDef.CRITERIA,
					setForm(request));
		}

		request.setAttribute(ConstantDef.ATTR_FORM, form);

		RequestDispatcher dispatcher = request
				.getRequestDispatcher(WORKLIST_JSP_PATH);
		try {
			dispatcher.forward(request, response);
		} catch (ServletException | IOException e) {
			throw new SystemException(e, MsgCodeDef.ERR_FORWARD);
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
				logger.debug("挿入処理開始:");
				viewForm = logic.insertWork(inputForm);
			} else if (request.getParameter("addBtn") != null) {
				logger.debug("追加処理開始:");
				viewForm = logic.addWork(inputForm);
			} else if (request.getParameter("deleteBtn") != null) {
				logger.debug("削除処理開始:");
				viewForm = logic.delete(inputForm);
			} else if (request.getParameter("historyBtn") != null) {
				logger.debug("履歴表示処理開始:");
				viewForm = logic.history(inputForm);
			} else if (request.getParameter("saveBtn") != null) {
				logger.debug("保存処理開始:");
				viewForm = logic.saveWork(inputForm);
			} else if (request.getParameter("csvDownloadBtn") != null) {
				logger.info("CSVダウンロード処理開始:");
				File csvFile = logic.csvDownload(inputForm);
				download(response, csvFile);
			} else {
				// 編集
				logger.info("編集処理開始:");
				requestPath = WORKEDITFORM_JSP_PATH;
				WorkEditForm editForm = logic.getEditWork(inputForm);
				request.setAttribute(ConstantDef.ATTR_EDIT_FORM, editForm);
			}

		} catch (BusinessException e) {
			logger.warn(PropertyUtils.getValue(MsgCodeDef.INPUT_ERROR));
			// 作業リストの再表示
			// セッションにある検索条件を取得
			WorkListForm criteria = (WorkListForm) request.getSession()
					.getAttribute(ConstantDef.CRITERIA);
			LocalDate date = DateUtils.getParseDate(criteria.getWorkDate());
			boolean delete = criteria.getDeleteCechk()
					.equals(ConstantDef.DELETE_CHECK_ON);
			viewForm = logic.getWorkListViewForm(criteria.getUserName(), date,
					delete);

			// エラーメッセージ設定
			viewForm.setErrMsgs(e.getMessage());
		} finally {

		}

		request.setAttribute(ConstantDef.ATTR_FORM, viewForm);

		RequestDispatcher dispatcher = request
				.getRequestDispatcher(requestPath);

		try {
			dispatcher.forward(request, response);
		} catch (ServletException | IOException e) {
			throw new SystemException(e, MsgCodeDef.ERR_FORWARD);
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
		if (workDate == null || workDate.isEmpty()) {
			if (request.getParameter("historyBtn") != null) {
				// 履歴処理の日付未入力はnullを設定
				form.setWorkDate(null);
			}
			form.setWorkDate(DateUtils.getTodayStr());
		} else {
			form.setWorkDate(workDate);
		}
		String deleteCheck = request.getParameter("deleteFlg");
		if (deleteCheck == null) {
			form.setDeleteCechk(ConstantDef.DELETE_CHECK_OFF);
		}
		return form;
	}

	/**
	 * CSVファイルダウンロード処理
	 * 
	 * @param response
	 *            レスポンス情報
	 * 
	 * @param csvFile
	 *            CSVファイル
	 */
	private void download(HttpServletResponse response, File csvFile) {

		response.setContentType("application/octet-stream");
		response.setContentLength((int) csvFile.length());
		response.setHeader("Content-disposition",
				"attachment; filename=\"" + csvFile.getName() + "\"");// csvFile.getName()

		// キャッシュの無効化
		response.setHeader("Cache-Control",
				"must-revalidate, post-check=0,pre-check=0");
		// プロキシサーバーでのキャッシュ無効化
		response.setHeader("Pragma", "private");

		try (BufferedInputStream bufIn = new BufferedInputStream(
				new FileInputStream(csvFile));
				BufferedOutputStream bufOut = new BufferedOutputStream(
						response.getOutputStream())) {

			byte[] buf = new byte[1024];
			int len;
			// 1Kbyteずつファイルを読み込み
			while ((len = bufIn.read(buf)) != -1) {
				bufOut.write(buf, 0, len);
			}
			// 出力
			bufOut.flush();

		} catch (IOException e) {
			// ヘッダーのリセット
			response.reset();
			throw new SystemException(e);
		} finally {
			// ダウンロード失敗時、ファイルを削除
			csvFile.delete();
		}
	}
}
