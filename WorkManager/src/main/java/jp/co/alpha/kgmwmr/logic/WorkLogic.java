package jp.co.alpha.kgmwmr.logic;

import java.time.LocalDate;
import java.time.LocalTime;
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
	 * 作業挿入処理
	 * 
	 * @param inputWork
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
	 * @return
	 * @throws BusinessException
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
	 * @return
	 */
	public WorkEditForm getEditWork(WorkListForm inputForm) {

		// 編集画面引継ぎデータ取得
		Work inputWork = new Work();
		String userName = inputForm.getUserName();
		inputWork.setUserName(userName);
		inputWork.setId(ConvertToModelUtils.convertInt(inputForm.getId()));
		Work output;
		try {
			// 接続開始
			CommonDbUtil.openConnection();

			WorkDao dao = new WorkDao();
			output = dao.getEditWork(inputWork);

		} finally {
			// 処理完了後、コネクションMapからコネクションを削除
			CommonDbUtil.closeConnection();
		}

		// 編集するデータ（画面初期表示用）取得
		WorkEditForm editForm = setWorkEditForm(output);
		return editForm;
	}

	/**
	 * 作業更新処理
	 * 
	 * @param editForm
	 * @throws BusinessException
	 */
	public void updateWork(WorkEditForm editForm) throws BusinessException {

		try {
			// トランザクション管理設定
			CommonDbUtil.openConnection(false);

			// 作業中のチェック用に、更新する作業データを取得
			Work inputWork = new Work();
			inputWork.setUserName(editForm.getUserName());
			WorkDao dao = new WorkDao();
			List<Work> working = dao.findWorking(inputWork);

			// 作業中の作業がある場合に終了時間が入力されなければエラー
			if (working.size() == 1 && editForm.getEndTime().isEmpty()) {
				throw new BusinessException(PropertyUtils
						.getValue(MsgCodeDef.ALREADY_EXIT_WORKING));
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
			result.addErrorMsg(
					PropertyUtils.getValue(MsgCodeDef.EMPTY_INPUT, "開始時間"));
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
	 * @return
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
			dao.copyTodayWork(inputWork);
			// コミット
			CommonDbUtil.commit();

		} finally {
			// 処理完了後、コネクションMapからコネクションを削除
			CommonDbUtil.closeConnection();
		}

		// 画面表示データ取得(保存後は当日のデータを表示)
		WorkListViewForm viewForm = getWorkListViewForm(inputWork.getUserName(),
				LocalDate.now(), false);
		return viewForm;
	}

	/**
	 * 未保存作業削除処理
	 * 
	 * @param userName
	 */
	public void deleteUnSaveWork(String userName) {

		logger.debug("未保存作業削除処理開始");

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
	 * @return
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
	 * @return
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
			logger.info("仕掛処理なし");
			form.setWork(null);
			form.setWorkingFlg(false);
		} else {
			logger.info("仕掛処理あり");
			form.setWork(workList.get(0));
			form.setWorkingFlg(true);
		}

		// 作業開始時間(初期表示用)を設定
		form.setNowTime(DateUtils.getNowTimeStr());

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
	 * @param finshForm
	 * 
	 * @param inputWork
	 * @return
	 * @throws BusinessException
	 */
	public WorkRegisterViewForm finishWork(String userName, String deleteId)
			throws BusinessException {

		// 入力(id)チェック
		if (!InputValidation.idCheck(deleteId)) {
			throw new SystemException("不正な入力");
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
	 * @param deleteId
	 * @return
	 * @throws BusinessException
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
			throw new BusinessException("すでに削除されています");
		} else if (finishWork.getEndTime() != null) {
			throw new BusinessException("すでに終了しています");
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
		if(endTime.isBefore(startTime)){
			throw new BusinessException(PropertyUtils.getValue(MsgCodeDef.START_END_ERROR));
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
	 * @param registerForm
	 * @return
	 * @throws BusinessException
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
	 * （同期処理）
	 * 
	 * @param inputWork
	 * @throws BusinessException
	 */
	private synchronized void workRegiste(Work inputWork)
			throws BusinessException {

		// 仕掛処理確認
		List<Work> workList = findWorking(inputWork);
		WorkDao dao = new WorkDao();

		if (workList.size() == 1) {
			if (inputWork.getId() == 0) {
				// 別の操作で作業が追加されていた場合
				throw new BusinessException("作業が開始されています");
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
				throw new BusinessException("別の作業が開始されています");
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
	 * @param inputWork
	 * @return
	 */
	private ValidationResult inputCheckWhenStart(WorkRegisterForm form) {

		ValidationResult result = new ValidationResult();
		result.setCheckResult(true);

		boolean validationChek = false;

		logger.info("入力値：開始時間[{}] 作業内容[{}] 備考[{}]", form.getStartTime(),
				form.getContents(), form.getNote());

		// idチェック
		String id = form.getId();
		if (!InputValidation.idCheck(id)) {
			throw new SystemException("不正な入力");
		}

		// 開始時間チェック
		String startTime = form.getStartTime();
		if (startTime == null) {
			throw new SystemException(
					PropertyUtils.getValue(MsgCodeDef.BAD_INPUT));
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
			result.addErrorMsg(
					PropertyUtils.getValue(MsgCodeDef.EMPTY_INPUT, "開始時間"));
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
			throw new SystemException(
					PropertyUtils.getValue(MsgCodeDef.BAD_INPUT));
		} else {
			// サイズチェック
			validationChek = InputValidation.inputSize(target, 0, 40);
			if (!validationChek) {
				result.addErrorMsg(PropertyUtils.getValue(MsgCodeDef.SIZE_ERROR,
						targetName, "0", "40"));
				result.setCheckResult(validationChek);
			}
		}
	}

	/**
	 * 履歴表示ロジック TODO実装途中
	 * 
	 * @param inputForm
	 * @return
	 * @throws BusinessException
	 */
	public WorkListViewForm history(WorkListForm inputForm)
			throws BusinessException {

		ValidationResult result = new ValidationResult();
		result.setCheckResult(true);

		String workDateStr = inputForm.getWorkDate();
		// 入力チェック
		if (workDateStr == null || !InputValidation.isDate(workDateStr)) {

			throw new BusinessException(PropertyUtils
					.getValue(MsgCodeDef.INPUT_FORMAT_ERROR, "日付"));
		}

		logger.info("入力日付:{}", workDateStr);
		String userName = inputForm.getUserName();

		LocalDate workDate = DateUtils.getParseDate(workDateStr);

		// 過去日チェック
		logger.info("今日の日付:{}", LocalDate.now());
		if (workDate.isAfter(LocalDate.now())) {
			throw new BusinessException("過去日を選択してください。");
		}

		// 未保存データ削除
		deleteUnSaveWork(userName);

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
	 * @return
	 * @throws BusinessException
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
	 * @param inputWork
	 */
	public void copyTodayWork(String userName) {

		Work inputWork = new Work();
		inputWork.setUserName(userName);

		try {
			// コネクション開始
			CommonDbUtil.openConnection();

			WorkDao dao = new WorkDao();
			dao.copyTodayWork(inputWork);

		} finally {

			// 処理完了後、コネクションMapからコネクションを削除
			CommonDbUtil.closeConnection();
		}
	}

	/**
	 * 作業リスト表示用フォームを取得
	 * 
	 * @param userName
	 * @param listDate
	 * @param delete
	 * @return
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
		logger.info("作業リストの日付:{}", DateUtils.getTodayStr());

		return form;
	}

	/**
	 * 作業編集用Formへの詰め替え
	 * 
	 * @param work
	 * @return
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

		return editForm;
	}
}
