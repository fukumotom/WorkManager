package jp.co.alpha.kgmwmr.logic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jp.co.alpha.kgmwmr.common.exception.BusinessException;
import jp.co.alpha.kgmwmr.common.exception.SystemException;
import jp.co.alpha.kgmwmr.common.util.ConstantDef;
import jp.co.alpha.kgmwmr.common.util.ConvertToModelUtils;
import jp.co.alpha.kgmwmr.common.util.DateUtils;
import jp.co.alpha.kgmwmr.common.util.InputValidation;
import jp.co.alpha.kgmwmr.common.util.MsgCodeDef;
import jp.co.alpha.kgmwmr.common.util.PropertyUtils;
import jp.co.alpha.kgmwmr.common.util.ValidationResult;
import jp.co.alpha.kgmwmr.dao.WorkDao;
import jp.co.alpha.kgmwmr.db.util.CommonDbUtil;
import jp.co.alpha.kgmwmr.form.WorkEditForm;
import jp.co.alpha.kgmwmr.form.WorkListForm;
import jp.co.alpha.kgmwmr.form.WorkListViewForm;
import jp.co.alpha.kgmwmr.form.WorkRegisterForm;
import jp.co.alpha.kgmwmr.form.WorkRegisterViewForm;
import jp.co.alpha.kgmwmr.model.Work;

/**
 * 作業管理ロジッククラス
 * 
 * @author kigami
 *
 */
public class WorkLogic {

	/**
	 * ロガー
	 */
	private static Logger logger = LoggerFactory.getLogger(WorkLogic.class);

	/**
	 * CSV出力タイトル
	 */
	private static final String[] CSV_TITLES = {"開始時間", "終了時間", "作業時間", "作業内容",
			"備考"};

	/**
	 * CSV区切り文字
	 */
	private static final String CSV_DELIMITER = ",";

	/**
	 * CSV出力文字コード
	 */
	private static final String CSV_CHARSET = "Shift_JIS";

	/**
	 * CSV改行
	 */
	private static final String NEW_LINE = "\n";

	/**
	 * 作業内容/備考の入力最小サイズ
	 */
	private static final int MIN_SIZE = 0;

	/**
	 * 作業内容/備考の入力最大サイズ
	 */
	private static final int MAX_SIZE = 40;

	/**
	 * 作業挿入処理
	 * 
	 * @param inputForm
	 *            入力情報
	 * @return 画面表示情報
	 */
	public WorkListViewForm insertWork(WorkListForm inputForm) {

		// formから処理に必要な情報を取得
		Work inputWork = new Work();
		String userName = inputForm.getUserName();
		inputWork.setUserName(userName);
		inputWork.setId(ConvertToModelUtils.convertInt(inputForm.getId()));
		inputWork.setWorkDate(DateUtils.getParseDate(inputForm.getWorkDate()));

		try {
			// トランザクション管理設定
			CommonDbUtil.openConnection(false);

			WorkDao dao = new WorkDao();

			// 挿入する作業の開始、終了時間を取得
			LocalTime time = dao.findStartTime(inputWork);
			inputWork.setStartTime(DateUtils.truncatedTime(time));
			inputWork.setEndTime(DateUtils.truncatedTime(time));

			// 挿入処理実行
			dao.insert(inputWork);

			// コミット
			CommonDbUtil.commit();

		} finally {
			// 処理完了後、コネクションMapからコネクションを削除
			CommonDbUtil.closeConnection();
		}

		// 画面表示データ再取得(削除済みリストに追加はなし)
		WorkListViewForm viewForm = getWorkListViewForm(inputWork.getUserName(),
				inputWork.getWorkDate(), false);
		return viewForm;
	}

	/**
	 * 作業論理削除処理
	 * 
	 * @param inputForm
	 *            入力情報
	 * @return 画面表示情報
	 * @throws BusinessException
	 *             業務例外
	 */
	public WorkListViewForm delete(WorkListForm inputForm)
			throws BusinessException {

		// 作業削除データ設定
		Work inputWork = new Work();
		String userName = inputForm.getUserName();
		inputWork.setUserName(userName);
		inputWork.setId(ConvertToModelUtils.convertInt(inputForm.getId()));
		inputWork.setWorkDate(DateUtils.getParseDate(inputForm.getWorkDate()));

		try {
			// トランザクション管理設定
			CommonDbUtil.openConnection(false);

			WorkDao dao = new WorkDao();
			dao.delete(inputWork);

			// コミット
			CommonDbUtil.commit();

		} finally {
			// 処理完了後、コネクションMapからコネクションを削除
			CommonDbUtil.closeConnection();
		}

		// 画面表示データ再取得(削除済みリストはもう削除しない)
		WorkListViewForm viewForm = getWorkListViewForm(inputWork.getUserName(),
				inputWork.getWorkDate(), false);
		return viewForm;
	}

	/**
	 * 編集作業検索処理
	 * 
	 * @param inputForm
	 *            入力情報
	 * @return 作業編集用情報
	 */
	public WorkEditForm getEditWork(WorkListForm inputForm) {

		// 編集画面引継ぎデータ取得
		Work inputWork = new Work();
		String userName = inputForm.getUserName();
		inputWork.setUserName(userName);
		inputWork.setId(ConvertToModelUtils.convertInt(inputForm.getId()));
		Work output;
		WorkEditForm editForm;
		try {
			// 接続開始
			CommonDbUtil.openConnection();

			WorkDao dao = new WorkDao();
			output = dao.getEditWork(inputWork);

			// 編集するデータ（画面初期表示用）取得
			editForm = setWorkEditForm(output);

			// コンボボックス用作業内容取得
			ArrayList<String> contentsList = getConbbox("contents", inputWork);
			editForm.setContentsList(contentsList);
			ArrayList<String> noteList = getConbbox("note", inputWork);
			editForm.setNoteList(noteList);

		} finally {
			// 処理完了後、コネクションMapからコネクションを削除
			CommonDbUtil.closeConnection();
		}

		return editForm;
	}

	/**
	 * 作業更新処理
	 * 
	 * @param editForm
	 *            更新情報
	 * @throws BusinessException
	 */
	public void updateWork(WorkEditForm editForm) throws BusinessException {

		try {
			// トランザクション管理設定
			CommonDbUtil.openConnection(false);

			// 作業中のチェック用に、更新する作業データを取得
			Work inputWork = new Work();
			inputWork.setUserName(editForm.getUserName());
			inputWork.setWorkDate(
					DateUtils.getParseDate(editForm.getWorkDate()));

			WorkDao dao = new WorkDao();
			List<Work> working = dao.findWorking(inputWork);

			// 作業中の作業がある場合に終了時間が入力されなければエラー
			if (working.size() == 1 && editForm.getEndTime().isEmpty()) {
				throw new BusinessException(MsgCodeDef.ALREADY_EXIT_WORKING);
			}

			// 入力チェック
			ValidationResult result = editValidationCheck(editForm);
			if (!result.isCheckResult()) {
				throw new BusinessException(result.getErrorMsgs());
			}

			// 更新のためのデータを設定
			inputWork.setId(ConvertToModelUtils.convertInt(editForm.getId()));
			String startTime = editForm.getStartTime();
			inputWork.setStartTime(DateUtils.getParseTime(startTime));
			String endTime = editForm.getEndTime();
			// 終了時間が入力されていない場合、nullを設定
			if (endTime.isEmpty()) {
				inputWork.setEndTime(null);
				// 終了時間が空の場合、作業時間も空を設定
				inputWork.setWorkingTime(null);
			} else {
				inputWork.setEndTime(DateUtils.getParseTime(endTime));
			}
			// 作業終了時間が入力された場合、作業時間の再計算
			if (!endTime.isEmpty() && !startTime.isEmpty()) {
				calcWorkTime(inputWork);
			}
			inputWork.setContents(editForm.getContents());
			inputWork.setNote(editForm.getNote());

			// 更新処理実行
			dao.updateWork(inputWork);

			// コミット
			CommonDbUtil.commit();

		} finally {
			// 処理完了後、コネクションMapからコネクションを削除
			CommonDbUtil.closeConnection();
		}
	}

	/**
	 * 作業編集フォームの入力チェック
	 * 
	 * @param editForm
	 *            入力情報
	 * @return チェック結果
	 */
	private ValidationResult editValidationCheck(WorkEditForm editForm) {

		ValidationResult result = new ValidationResult();
		result.setCheckResult(true);

		// 開始時間の入力チェック
		if (editForm.getStartTime().isEmpty()) {
			result.setCheckResult(false);
			result.addErrorMsg(MsgCodeDef.EMPTY_INPUT, "開始時間");
		} else if (!InputValidation.isTime(editForm.getStartTime())) {
			result.setCheckResult(false);
			result.addErrorMsg(PropertyUtils
					.getValue(MsgCodeDef.INPUT_FORMAT_ERROR, "開始時間"));
		}

		// 終了時間の入力チェック
		if (!editForm.getEndTime().isEmpty()
				&& !InputValidation.isTime(editForm.getEndTime())) {
			// 終了時間は空を許容
			result.setCheckResult(false);
			result.addErrorMsg(PropertyUtils
					.getValue(MsgCodeDef.INPUT_FORMAT_ERROR, "終了時間"));
		}

		// 作業内容と備考のチェック
		validationSizeCheck(editForm.getContents(), "作業内容", result);
		validationSizeCheck(editForm.getNote(), "備考", result);

		return result;
	}

	/**
	 * 作業保存処理
	 * 
	 * @param inputForm
	 *            入力情報
	 * @return 画面表示情報
	 */
	public WorkListViewForm saveWork(WorkListForm inputForm) {

		Work inputWork = new Work();
		inputWork.setUserName(inputForm.getUserName());
		inputWork.setId(ConvertToModelUtils.convertInt(inputForm.getId()));
		inputWork.setWorkDate(DateUtils.getParseDate(inputForm.getWorkDate()));

		try {
			// トランザクション管理設定
			CommonDbUtil.openConnection(false);

			// 保存処理実行
			WorkDao dao = new WorkDao();
			// 編集用複製データ削除
			dao.deleteCopyBase(inputWork);
			dao.saveWork(inputWork);
			// コミット
			CommonDbUtil.commit();

			// 画面表示用にデータを複製
			dao.copyWork(inputWork);
			// コミット
			CommonDbUtil.commit();

		} finally {
			// 処理完了後、コネクションMapからコネクションを削除
			CommonDbUtil.closeConnection();
		}

		// 画面表示データ取得
		WorkListViewForm viewForm = getWorkListViewForm(inputWork.getUserName(),
				inputWork.getWorkDate(), false);
		return viewForm;
	}

	/**
	 * 未保存作業削除処理
	 * 
	 * @param userName
	 *            ログインユーザ名
	 */
	public void deleteUnSaveWork(String userName) {

		Work inputWork = new Work();
		inputWork.setUserName(userName);

		try {
			// トランザクション管理設定
			CommonDbUtil.openConnection(false);

			WorkDao dao = new WorkDao();
			dao.deleteUnSaveWork(inputWork);

			// コミット
			CommonDbUtil.commit();

		} finally {
			// 処理完了後、コネクションMapからコネクションを削除
			CommonDbUtil.closeConnection();
		}
	}

	/**
	 * 画面表示用データを取得
	 * 
	 * @param userName
	 *            ログインユーザ名
	 * @return 画面表示情報
	 */
	public WorkRegisterViewForm getViewdata(String userName) {

		WorkRegisterViewForm form;
		try {

			// コネクション開始
			CommonDbUtil.openConnection();

			// 画面表示用データを取得
			form = getWorkRegisterViewForm(userName);
		} finally {

			// 処理完了後、コネクションMapからコネクションを削除
			CommonDbUtil.closeConnection();
		}

		return form;
	}

	/**
	 * 画面表示用フォームの取得
	 * 
	 * @param userName
	 *            ログインユーザ名
	 * @return 画面表示情報
	 */
	private WorkRegisterViewForm getWorkRegisterViewForm(String userName) {

		WorkRegisterViewForm form = new WorkRegisterViewForm();

		Work inputWork = new Work();
		inputWork.setUserName(userName);

		// 現在時間を設定
		inputWork.setWorkDate(LocalDate.now());

		List<Work> workList;

		// 仕掛作業取得
		workList = findWorking(inputWork);

		// formに仕掛作業と作業状態フラグを設定
		if (workList.size() == 0) {
			logger.info(PropertyUtils.getValue(MsgCodeDef.WORKING));
			form.setWork(null);
			form.setWorkingFlg(false);
		} else {
			logger.info(PropertyUtils.getValue(MsgCodeDef.NOT_WORKING));
			form.setWork(workList.get(0));
			form.setWorkingFlg(true);
		}

		// 作業開始時間(初期表示用)を設定
		form.setNowTime(DateUtils.getNowTimeStr());

		// コンボボックス用作業内容取得
		ArrayList<String> contentsList = getConbbox("contents", inputWork);
		form.setContentsList(contentsList);
		ArrayList<String> noteList = getConbbox("note", inputWork);
		form.setNoteList(noteList);

		return form;
	}

	/**
	 * 仕掛作業取得（コネクションは呼び元でオープン）
	 * 
	 * @param inputWork
	 * @return
	 */
	private List<Work> findWorking(Work inputWork) {

		List<Work> workList;

		WorkDao dao = new WorkDao();
		workList = dao.findWorking(inputWork);

		return workList;
	}

	/**
	 * 作業完了処理
	 * 
	 * @param userName
	 *            ログインユーザ名
	 * @param deleteId
	 *            削除する作業ID
	 * @return 画面表示情報
	 * @throws BusinessException
	 *             業務例外
	 */
	public WorkRegisterViewForm finishWork(String userName, String deleteId)
			throws BusinessException {

		// 入力(id)チェック
		if (!InputValidation.idCheck(deleteId)) {
			throw new SystemException(MsgCodeDef.BAD_INPUT);
		}

		// 画面表示用情報
		WorkRegisterViewForm viewForm;

		try {
			// トランザクション管理設定
			CommonDbUtil.openConnection(false);

			Work finishWork = getFinishWork(userName,
					Integer.valueOf(deleteId));

			// 作業終了処理
			WorkDao dao = new WorkDao();
			dao.finishWork(finishWork);

			viewForm = getWorkRegisterViewForm(userName);

			// コミット
			CommonDbUtil.commit();

		} finally {
			// 処理完了後、コネクションMapからコネクションを削除
			CommonDbUtil.closeConnection();
		}

		return viewForm;
	}

	/**
	 * 完了する作業情報を取得
	 *
	 * @param userName
	 *            ログインユーザ名
	 * @param deleteId
	 * @return
	 * @throws BusinessException
	 *             業務例外
	 */
	private Work getFinishWork(String userName, Integer deleteId)
			throws BusinessException {

		// 取得する作業条件を設定
		Work inputWork = new Work();
		inputWork.setId(deleteId);
		inputWork.setUserName(userName);

		WorkDao dao = new WorkDao();
		// 画面から取得したIDに一致する作業を取得
		Work finishWork = dao.getSelectWork(inputWork);
		// 取得した作業チェック
		if (finishWork.isDelete()) {
			throw new BusinessException(MsgCodeDef.ALREADY_FINISHED);
		} else if (finishWork.getEndTime() != null) {
			throw new BusinessException(MsgCodeDef.ALREADY_DELETE);
		}

		// 終了時間を設定
		finishWork.setEndTime(DateUtils.getNowTime());
		// 作業時間計算
		calcWorkTime(finishWork);

		return finishWork;
	}

	/**
	 * 作業時間の計算処理
	 * 
	 * @param inputWork
	 * @throws BusinessException
	 */
	private void calcWorkTime(Work inputWork) throws BusinessException {

		LocalTime startTime = DateUtils.truncatedTime(inputWork.getStartTime());
		logger.info("開始時間:{}", startTime);

		LocalTime endTime = DateUtils.truncatedTime(inputWork.getEndTime());

		// 開始時間<終了時間チェック
		if (endTime.isBefore(startTime)) {
			throw new BusinessException(MsgCodeDef.START_END_ERROR);
		}

		LocalTime calcTime = endTime.minusHours(startTime.getHour());
		LocalTime workingTime = calcTime.minusMinutes(startTime.getMinute());
		inputWork.setWorkingTime(DateUtils.truncatedTime(workingTime));
		logger.info("作業時間:{}", inputWork.getWorkingTime());
	}

	/**
	 * 作業開始処理<br>
	 * 仕掛作業がある場合は、仕掛作業を終了して 作業を開始する
	 * 
	 * @param userName
	 *            ログインユーザ名
	 * @param registerForm
	 *            入力情報
	 * @return 画面表示情報
	 * @throws BusinessException
	 *             業務例外
	 */
	public WorkRegisterViewForm register(String userName,
			WorkRegisterForm registerForm) throws BusinessException {

		WorkRegisterViewForm form;

		try {

			// 入力チェック
			ValidationResult result = inputCheckWhenStart(registerForm);
			if (!result.isCheckResult()) {

				CommonDbUtil.openConnection();

				// 画面表示用情報
				form = getWorkRegisterViewForm(userName);
				// 入力チェックエラーの場合、エラーメッセージを設定
				form.setErrMsgs(result.getErrorMsgs());

			} else {

				// 入力チェックOKの場合
				Work inputWork = new Work();

				// 登録情報を設定
				inputWork.setUserName(userName);
				if (!registerForm.getId().isEmpty()) {
					// 仕掛り作業がある場合、作業IDを設定
					inputWork.setId(Integer.valueOf(registerForm.getId()));
				}
				inputWork.setStartTime(
						DateUtils.getParseTime(registerForm.getStartTime()));

				inputWork.setContents(registerForm.getContents());
				inputWork.setNote(registerForm.getNote());

				// トランザクション管理設定
				CommonDbUtil.openConnection(false);

				// 作業開始の 同期処理
				workRegiste(inputWork);

				// 画面表示用情報
				form = getWorkRegisterViewForm(userName);

				// コミット
				CommonDbUtil.commit();

			}

		} finally {
			// 処理完了後、コネクションMapからコネクションを削除
			CommonDbUtil.closeConnection();
		}

		return form;
	}

	/**
	 * 登録（同期処理）
	 * 
	 * @param inputWork
	 *            作業情報
	 * @throws BusinessException
	 *             業務例外
	 */
	private synchronized void workRegiste(Work inputWork)
			throws BusinessException {

		// 仕掛処理確認
		List<Work> workList = findWorking(inputWork);
		WorkDao dao = new WorkDao();

		if (workList.size() == 1) {
			if (inputWork.getId() == 0) {
				// 別の操作で作業が追加されていた場合
				throw new BusinessException(MsgCodeDef.ALREADY_START, "作業");
			}

			// DBに登録されている仕掛処理を取得
			Work dbWorking = workList.get(0);

			// 画面表示の仕掛作業を取得
			Work viewWorking = getFinishWork(inputWork.getUserName(),
					inputWork.getId());

			if (dbWorking.getId().equals(viewWorking.getId())) {
				// 画面情報とDB情報が一致。処理を終了する

				// 終了処理
				dao.finishWork(viewWorking);
				// 作業開始
				dao.startWork(inputWork);

			} else {
				throw new BusinessException(MsgCodeDef.ALREADY_START, "別の作業");
			}
		} else {
			// 仕掛り処理なしの場合
			// 作業開始
			dao.startWork(inputWork);
		}
	}

	/**
	 * 作業開始ボタン押下時の入力チェック
	 * 
	 * @param form
	 *            入力情報
	 * @return 入力チェック結果
	 */
	private ValidationResult inputCheckWhenStart(WorkRegisterForm form) {

		ValidationResult result = new ValidationResult();
		result.setCheckResult(true);

		boolean validationChek = false;

		logger.debug("入力値：開始時間[{}] 作業内容[{}] 備考[{}]", form.getStartTime(),
				form.getContents(), form.getNote());

		// idチェック
		String id = form.getId();
		if (!InputValidation.idCheck(id)) {
			throw new SystemException(MsgCodeDef.BAD_INPUT);
		}

		// 開始時間チェック
		String startTime = form.getStartTime();
		if (startTime == null) {
			throw new SystemException(MsgCodeDef.BAD_INPUT);
		}
		if (!startTime.isEmpty()) {
			// フォーマットチェック
			validationChek = InputValidation.isTime(startTime);
			if (!validationChek) {
				result.addErrorMsg(PropertyUtils
						.getValue(MsgCodeDef.INPUT_FORMAT_ERROR, "開始時間"));
				result.setCheckResult(false);
			}
		} else {
			// 入力チェック
			result.setCheckResult(false);
			result.addErrorMsg(MsgCodeDef.EMPTY_INPUT, "開始時間");
		}

		// 作業内容と備考のチェック
		validationSizeCheck(form.getContents(), "作業内容", result);
		validationSizeCheck(form.getNote(), "備考", result);

		return result;
	}

	/**
	 * 文字サイズチェック
	 * 
	 * @param target
	 *            チェック対象
	 * @param targetName
	 *            チェック対象の項目名(作業内容/備考)
	 * @param result
	 *            チェック結果
	 */
	private void validationSizeCheck(String target, String targetName,
			ValidationResult result) {

		boolean validationChek = false;

		// 作業内容
		if (target == null) {
			throw new SystemException(MsgCodeDef.BAD_INPUT);
		} else {
			// サイズチェック
			validationChek = InputValidation.inputSize(target, MIN_SIZE,
					MAX_SIZE);
			if (!validationChek) {
				result.addErrorMsg(MsgCodeDef.SIZE_ERROR, targetName,
						String.valueOf(MIN_SIZE), String.valueOf(MAX_SIZE));
				result.setCheckResult(validationChek);
			}
		}
	}

	/**
	 * 履歴表示ロジック
	 * 
	 * @param inputForm
	 *            入力所法
	 * @return 画面表示情報
	 * @throws BusinessException
	 *             業務例外
	 */
	public WorkListViewForm history(WorkListForm inputForm)
			throws BusinessException {

		ValidationResult result = new ValidationResult();
		result.setCheckResult(true);

		String workDateStr = inputForm.getWorkDate();
		// 入力チェック
		if (workDateStr == null) {

			throw new BusinessException(MsgCodeDef.EMPTY_INPUT, "履歴");
		}
		if (!InputValidation.isDate(workDateStr)) {

			throw new BusinessException(MsgCodeDef.INPUT_FORMAT_ERROR, "履歴");
		}

		logger.info("入力日付:{}", workDateStr);
		String userName = inputForm.getUserName();

		LocalDate workDate = DateUtils.getParseDate(workDateStr);

		// 過去日チェック
		if (logger.isDebugEnabled()) {
			logger.debug("今日の日付:{}", LocalDate.now());
		}
		if (workDate.isAfter(LocalDate.now())) {
			throw new BusinessException(MsgCodeDef.EMPTY_INPUT, "過去日");
		}

		// 未保存データ削除
		deleteUnSaveWork(userName);

		// 編集用に指定された日付分の登録作業を複製
		copyWork(userName, workDate);

		// 削除反映
		boolean delete = ConstantDef.DELETE_CHECK_ON
				.equals(inputForm.getDeleteCechk());

		// 画面表示データ再取得
		WorkListViewForm viewForm = getWorkListViewForm(userName, workDate,
				delete);

		return viewForm;
	}

	/**
	 * 作業追加処理
	 * 
	 * @param inputForm
	 *            入力情報
	 * @return 画面表示情報
	 * @throws BusinessException
	 *             業務例外
	 */
	public WorkListViewForm addWork(WorkListForm inputForm)
			throws BusinessException {

		// form情報を処理用モデルに設定
		Work inputWork = new Work();
		String userName = inputForm.getUserName();
		inputWork.setUserName(userName);
		inputWork.setId(ConvertToModelUtils.convertInt(inputForm.getId()));
		inputWork.setWorkDate(DateUtils.getParseDate(inputForm.getWorkDate()));

		try {
			// トランザクション管理設定
			CommonDbUtil.openConnection(false);

			// 作業中の作業の場合、追加不可。
			WorkDao dao = new WorkDao();

			// 追加する作業の開始、終了時間を取得
			LocalTime time = dao.findEndTime(inputWork);
			inputWork.setStartTime(DateUtils.truncatedTime(time));
			inputWork.setEndTime(DateUtils.truncatedTime(time));

			dao.insert(inputWork);

			// コミット
			CommonDbUtil.commit();

		} finally {
			// 処理完了後、コネクションMapからコネクションを削除
			CommonDbUtil.closeConnection();
		}

		// 画面表示データ再取得(削除済みリストに追加はなし)
		WorkListViewForm viewForm = getWorkListViewForm(inputWork.getUserName(),
				inputWork.getWorkDate(), false);
		return viewForm;
	}

	/**
	 * 編集用作業リスト複製処理
	 * 
	 * @param workDate
	 * 
	 * @param inputWork
	 */
	public void copyWork(String userName, LocalDate workDate) {

		Work inputWork = new Work();
		inputWork.setUserName(userName);
		inputWork.setWorkDate(workDate);

		try {
			// コネクション開始
			CommonDbUtil.openConnection();

			WorkDao dao = new WorkDao();
			dao.copyWork(inputWork);

		} finally {

			// 処理完了後、コネクションMapからコネクションを削除
			CommonDbUtil.closeConnection();
		}
	}

	/**
	 * 作業情報リストCSVファイルのアップロード
	 * 
	 * @param fileName
	 *            アップロードファイル名
	 * @param loginUserName
	 *            ログインユーザ名
	 * @param workDate
	 *            作業日付
	 * @throws BusinessException
	 *             業務例外
	 */
	public void upload(String fileName, String loginUserName, String workDate)
			throws BusinessException {

		// アップロードする作業リスト格納用
		List<Work> workList = new ArrayList<>();

		// ファイルの内容チェック
		try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
			String line;
			int cnt = 0;
			while ((line = br.readLine()) != null) {
				// カンマ区切りで分解
				if (cnt == 0) {
					// タイトル部分確認
					String[] titles = line.split(CSV_DELIMITER);
					if (!Arrays.equals(titles, CSV_TITLES)) {
						throw new BusinessException(
								MsgCodeDef.INPUT_FORMAT_ERROR,
								"アップロードファイルのタイトル");
					}
					cnt++;
				} else {
					// データ部分確認
					String[] datas = line.split(CSV_DELIMITER);

					ValidationResult result = csvDataFormatCheck(datas);
					if (!result.isCheckResult()) {
						throw new BusinessException(result);
					}

					// 開始時間
					LocalTime startTime = DateUtils.getParseTime(datas[0]);
					// 終了時間
					LocalTime endTime = DateUtils.getParseTime(datas[1]);

					// データ詰め替え
					Work inputWork = new Work();
					inputWork.setUserName(loginUserName);
					inputWork.setStartTime(startTime);
					inputWork.setEndTime(endTime);
					// 作業時間計算
					calcWorkTime(inputWork);
					inputWork.setContents(datas[3]);
					inputWork.setNote(datas[4]);

					workList.add(inputWork);
				}
			}
		} catch (IOException e) {

			throw new BusinessException(e);
		}

		try {
			// トランザクション管理設定
			CommonDbUtil.openConnection(false);

			WorkDao dao = new WorkDao();
			// ファイル読み込みデータをDBに保存
			dao.upload(workList);

			// コミット
			CommonDbUtil.commit();
		} finally {
			// 処理完了後、コネクションMapからコネクションを削除
			CommonDbUtil.closeConnection();
			// アップロード処理後の後片付け
			File uploadFile = new File(fileName);
			if (uploadFile.exists()) {
				uploadFile.delete();
			}
		}
	}

	/**
	 * CSVアップロード時のフォーマットチェック
	 * 
	 * @param datas
	 *            csv情報
	 * @return チェック結果
	 */
	private ValidationResult csvDataFormatCheck(String[] datas) {

		ValidationResult result = new ValidationResult();
		result.setCheckResult(true);

		boolean validationChek = false;

		// 開始時間
		if (!datas[0].isEmpty()) {
			// フォーマットチェック
			validationChek = InputValidation.isTime(datas[0]);
			if (!validationChek) {
				result.addErrorMsg(MsgCodeDef.INPUT_FORMAT_ERROR,
						"アップロードファイルの開始時間");
				result.setCheckResult(false);
			}
		} else {
			// 入力チェック
			result.setCheckResult(false);
			result.addErrorMsg(MsgCodeDef.EMPTY_INPUT, "アップロードファイルの開始時間");
		}

		// 終了時間
		if (!datas[1].isEmpty()) {
			// フォーマットチェック
			validationChek = InputValidation.isTime(datas[1]);
			if (!validationChek) {
				result.addErrorMsg(MsgCodeDef.INPUT_FORMAT_ERROR,
						"アップロードファイルの終了時間");
				result.setCheckResult(false);
			}
		} else {
			// 入力チェック
			result.setCheckResult(false);
			result.addErrorMsg(MsgCodeDef.EMPTY_INPUT, "アップロードファイルの終了時間");
		}

		if (result.isCheckResult()) {
			// 開始時間<終了時間チェック
			if (DateUtils.getParseTime(datas[1])
					.isBefore(DateUtils.getParseTime(datas[0]))) {
				result.addErrorMsg(MsgCodeDef.START_END_ERROR);
				result.setCheckResult(false);
			}
		}

		// 作業内容サイズチェック
		validationChek = InputValidation.inputSize(datas[3], 0, 40);
		if (!validationChek) {
			result.addErrorMsg(MsgCodeDef.SIZE_ERROR, "作業内容", "0", "40");
			result.setCheckResult(false);
		}

		// 備考 サイズチェック
		validationChek = InputValidation.inputSize(datas[4], 0, 40);
		if (!validationChek) {
			result.addErrorMsg(MsgCodeDef.SIZE_ERROR, "備考", "0", "40");
			result.setCheckResult(validationChek);
		}

		return result;
	}

	/**
	 * 作業リスト表示用フォームを取得
	 * 
	 * @param userName
	 *            ログインユーザ名
	 * @param listDate
	 *            表示する作業日付
	 * @param delete
	 *            検索条件（削除済みを含むかの判定用）
	 * @return 画面表示情報
	 */
	public WorkListViewForm getWorkListViewForm(String userName,
			LocalDate listDate, boolean delete) {

		WorkListViewForm form = new WorkListViewForm();

		Work inputWork = new Work();
		inputWork.setUserName(userName);
		inputWork.setWorkDate(listDate);
		inputWork.setDelete(delete);

		List<Work> workList;
		try {
			// 接続開始
			CommonDbUtil.openConnection();

			WorkDao dao = new WorkDao();
			workList = dao.findAllWork(inputWork);

		} finally {
			// 処理完了後、コネクションMapからコネクションを削除
			CommonDbUtil.closeConnection();
		}

		form.setWorkList(workList);

		// 表示用日付
		form.setListDate(DateUtils.formatDate(listDate));
		logger.debug("作業リストの日付:{}", listDate);

		return form;
	}

	/**
	 * 作業編集用Formへの詰め替え
	 * 
	 * @param work
	 *            作業情報
	 * @return 作業編集用Form
	 */
	private WorkEditForm setWorkEditForm(Work work) {

		WorkEditForm editForm = new WorkEditForm();
		editForm.setId(String.valueOf(work.getId()));
		editForm.setStartTime(DateUtils.formatTime(work.getStartTime()));
		if (work.getEndTime() != null) {
			editForm.setEndTime(DateUtils.formatTime(work.getEndTime()));
		}
		editForm.setContents(work.getContents());
		editForm.setNote(work.getNote());
		editForm.setWorkDate(DateUtils.formatDate(work.getWorkDate()));

		return editForm;
	}

	/**
	 * コンボボックスのデータを取得
	 * 
	 * @param taget
	 *            取得対象
	 * @param inputWork
	 *            検索条件
	 * @return コンボボックス用データ
	 */
	private ArrayList<String> getConbbox(String taget, Work inputWork) {

		WorkDao dao = new WorkDao();
		ArrayList<String> targetList;
		if ("contents".equals(taget)) {
			targetList = dao.findAllContets(inputWork);
		} else {
			targetList = dao.findAllNote(inputWork);
		}
		return targetList;
	}

	/**
	 * CSVエクスポート処理
	 * 
	 * @param inputForm
	 *            入力情報
	 * @return 出力するCSVファイル
	 * @throws BusinessException
	 *             業務例外
	 */
	public File csvDownload(WorkListForm inputForm) throws BusinessException {

		// form情報を処理用モデルに設定
		Work inputWork = new Work();
		String userName = inputForm.getUserName();
		inputWork.setUserName(userName);

		LocalDate workDate = DateUtils.getParseDate(inputForm.getWorkDate());
		inputWork.setWorkDate(workDate);

		List<Work> dataList;
		try {

			// トランザクション管理設定
			CommonDbUtil.openConnection();

			// 出力内容取得
			WorkDao dao = new WorkDao();
			dataList = dao.findAllWork(inputWork);

		} finally {
			// 処理完了後、コネクションMapからコネクションを削除
			CommonDbUtil.closeConnection();
		}

		StringBuilder sb = new StringBuilder();
		// タイトル列を作成
		sb.append(String.join(CSV_DELIMITER, CSV_TITLES));
		sb.append(NEW_LINE);

		// 内容を出力
		for (Work data : dataList) {
			// 時間は""で囲む
			String startTime = DateUtils.formatTime(data.getStartTime());
			sb.append(addDoublequotes(startTime)).append(CSV_DELIMITER);

			if (data.getEndTime() != null) {
				// 時間は""で囲む
				String endTime = DateUtils.formatTime(data.getEndTime());
				sb.append(addDoublequotes(endTime)).append(CSV_DELIMITER);
			} else {
				// 未終了作業の終了時間は空白
				sb.append(CSV_DELIMITER);
			}

			if (data.getWorkingTime() != null) {
				// 時間は""で囲む
				String WorkingTime = DateUtils
						.formatTime(data.getWorkingTime());
				sb.append(addDoublequotes(WorkingTime)).append(CSV_DELIMITER);
			} else {
				// 未終了の作業時間は空白
				sb.append(CSV_DELIMITER);
			}

			// ""（引用符）がある場合、CSV出力用にエスケープする。
			String Contents = csvEscape(data.getContents());
			sb.append(addDoublequotes(Contents)).append(CSV_DELIMITER);

			// ""（引用符）がある場合、CSV出力用にエスケープする。
			String note = csvEscape(data.getNote());
			sb.append(addDoublequotes(note)).append(CSV_DELIMITER);
			// 行の改行を追加
			sb.append(NEW_LINE);
		}

		// 一時ファイル作成
		File tmpFile = createTmpFile(sb);
		logger.debug("作成ファイルのパス:{}", tmpFile.getAbsolutePath());

		return tmpFile;
	}

	/**
	 * 一時ファイル作成
	 * 
	 * @param sb
	 * 
	 * @return 一時ファイル
	 * @throws BusinessException
	 * @throws IOException
	 *             例外情報
	 */
	private File createTmpFile(StringBuilder sb) throws BusinessException {

		File tmpFile = null;
		FileOutputStream fw = null;
		OutputStreamWriter outsw = null;
		try {
			tmpFile = File.createTempFile("worklist", ".csv");

			fw = new FileOutputStream(tmpFile);
			outsw = new OutputStreamWriter(fw, Charset.forName(CSV_CHARSET));

			outsw.write(sb.toString());
			outsw.flush();

		} catch (IOException e) {
			throw new BusinessException(e, "CSV作成に失敗");
		} finally {
			// 処理完了後、コネクションMapからコネクションを削除
			CommonDbUtil.closeConnection();

			if (fw != null) {
				try {
					fw.close();
				} catch (IOException e) {
					logger.warn(PropertyUtils.getValue(MsgCodeDef.MISS_CLOSE));
				}
			}
			if (outsw != null) {
				try {
					outsw.close();
				} catch (IOException e) {
					logger.warn(PropertyUtils.getValue(MsgCodeDef.MISS_CLOSE));
				}
			}
		}
		return tmpFile;
	}

	/**
	 * 対象文字をcsv出力用にダブルクォートでエスケープ
	 * 
	 * @param target
	 * @return エスケープした文字列
	 */
	private String csvEscape(String target) {

		String escape = target.replaceAll("\"", "\"\"");
		return escape;
	}

	/**
	 * 対象文字をダブルクォートで囲む
	 * 
	 * @param target
	 * @return ダブルクォートで囲んだ文字列
	 */
	private String addDoublequotes(String target) {
		return "\"" + target + "\"";
	}
}