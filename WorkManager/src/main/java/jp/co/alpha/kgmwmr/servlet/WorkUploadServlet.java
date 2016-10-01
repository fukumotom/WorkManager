package jp.co.alpha.kgmwmr.servlet;

import java.io.IOException;
import java.time.LocalDate;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jp.co.alpha.kgmwmr.common.exception.BusinessException;
import jp.co.alpha.kgmwmr.common.exception.SystemException;
import jp.co.alpha.kgmwmr.common.util.ConstantDef;
import jp.co.alpha.kgmwmr.common.util.DateUtils;
import jp.co.alpha.kgmwmr.common.util.MsgCodeDef;
import jp.co.alpha.kgmwmr.common.util.ValidationResult;
import jp.co.alpha.kgmwmr.form.WorkListForm;
import jp.co.alpha.kgmwmr.form.WorkListViewForm;
import jp.co.alpha.kgmwmr.logic.WorkLogic;

/**
 * 作業リスト表示サーブレット
 * 
 * @author kigami
 *
 */
@WebServlet("/WorkList/Upload")
@MultipartConfig(location = "C:/apache-tomcat-8.0.32/temp", fileSizeThreshold = 1048576)
public class WorkUploadServlet extends HttpServlet {

	/**
	 * シリアルバージョン
	 */
	private static final long serialVersionUID = 6145241402666462470L;

	/**
	 * ロガー
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(WorkUploadServlet.class);

	/**
	 * 作業リスト画面への遷移パス
	 */
	private static final String WORKLIST_JSP_PATH = "/WEB-INF/jsp/work/workList.jsp";

	/**
	 * 作業リストアップロード処理
	 * 
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public void doPost(HttpServletRequest request,
			HttpServletResponse response) {

		WorkListViewForm viewForm = new WorkListViewForm();
		try {
			logger.info("csvアップロード処理開始:");

			// エラー結果格納用
			ValidationResult result;

			Part part;
			try {
				part = request.getPart("csvFile");
				String fileName = getFileName(part);
				if (fileName.isEmpty()) {
					throw new BusinessException(MsgCodeDef.NOT_SELECT_FILE);
				}
				// 検索条件を取得
				WorkListForm initCreteria = (WorkListForm) request.getSession()
						.getAttribute(ConstantDef.CRITERIA);

				WorkLogic logic = new WorkLogic();
				result = logic.upload(part.getInputStream(),
						initCreteria.getUserName(), initCreteria.getWorkDate());
				if (!result.isCheckResult()) {
					// CSVファイルチェックエラーを設定
					throw new BusinessException(result);
				}

			} catch (IOException | ServletException e) {
				// アップロード失敗
				throw new BusinessException(e, MsgCodeDef.FAILURE_FILE_UPLOAD);
			}

			// 作業リストの再表示
			viewForm = getWorkListViaSession(request);

		} catch (BusinessException e) {
			// 作業リストの再表示
			viewForm = getWorkListViaSession(request);
			// エラーメッセージ設定
			viewForm.setErrMsgs(e.getMessage());
		}
		request.setAttribute(ConstantDef.ATTR_FORM, viewForm);

		RequestDispatcher dispatcher = request
				.getRequestDispatcher(WORKLIST_JSP_PATH);

		try {
			dispatcher.forward(request, response);
		} catch (ServletException | IOException e) {
			throw new SystemException(e, MsgCodeDef.ERR_FORWARD);
		}
	}

	/**
	 * セッションにある検索条件を取得
	 * 
	 * @param request
	 *            リクエスト情報
	 * @return 作業リスト
	 */
	private WorkListViewForm getWorkListViaSession(HttpServletRequest request) {

		WorkListForm criteria = (WorkListForm) request.getSession()
				.getAttribute(ConstantDef.CRITERIA);
		LocalDate date = DateUtils.getParseDate(criteria.getWorkDate());
		boolean delete = criteria.getDeleteCechk()
				.equals(ConstantDef.DELETE_CHECK_ON);
		WorkLogic logic = new WorkLogic();
		WorkListViewForm viewForm = logic
				.getWorkListViewForm(criteria.getUserName(), date, delete);
		return viewForm;
	}

	/**
	 * アップロードファイル名取得
	 * 
	 * @param part
	 *            パート情報
	 * @return アップロードファイル名
	 */
	private String getFileName(Part part) {

		String fileName = null;
		String dispotions = part.getHeader("Content-Disposition");
		for (String dispotion : dispotions.split(";")) {
			// ヘッダー内のファイル名を抽出
			if (dispotion.trim().startsWith("filename")) {
				// アップロードファイルパスを取得
				fileName = dispotion.substring(dispotion.indexOf("=") + 2,
						dispotion.length() - 1);
				// ファイルパスの区切りを統一
				fileName = fileName.replace("\\", "/");
				// ファイル名と拡張子を取得
				int pos = fileName.lastIndexOf("/");
				if (pos >= 0) {
					fileName = fileName.substring(pos + 1);
				}
				break;
			}
		}
		return fileName;
	}
}
