package jp.co.alpha.kgmwmr.logic;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jp.co.alpha.kgmwmr.common.exception.BusinessException;
import jp.co.alpha.kgmwmr.common.exception.SystemException;
import jp.co.alpha.kgmwmr.common.util.DateUtils;
import jp.co.alpha.kgmwmr.common.util.InputValidation;
import jp.co.alpha.kgmwmr.common.util.MsgCodeDef;
import jp.co.alpha.kgmwmr.common.util.PropertyUtils;
import jp.co.alpha.kgmwmr.common.util.ValidationResult;
import jp.co.alpha.kgmwmr.dao.WorkDao;
import jp.co.alpha.kgmwmr.db.util.CommonDbUtil;
import jp.co.alpha.kgmwmr.form.WorkRegisterForm;
import jp.co.alpha.kgmwmr.form.WorkRegisterViewForm;
import jp.co.alpha.kgmwmr.model.Work;

/**
 * 作業管理ロジッククラス TODO form対応未
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
	 * 作業リスト表示データ取得
	 * 
	 * @param inputWork
	 * @return
	 */
	public List<Work> findAllWork(Work inputWork) {

		try {
			// 接続開始
			CommonDbUtil.openConnection();

			WorkDao dao = new WorkDao();
			List<Work> workList = dao.findAllWork(inputWork);
			return workList;

		} finally {
			// 処理完了後、コネクションMapからコネクションを削除
			CommonDbUtil.closeConnection();
		}

	}

	/**
	 * 作業挿入処理
	 * 
	 * @param inputWork
	 */
	public void insertWork(Work inputWork) {

		try {
			// トランザクション管理設定
			CommonDbUtil.openConnection(false);

			WorkDao dao = new WorkDao();

			// 挿入する作業の開始、終了時間を取得
			LocalTime time = dao.findStartTime(inputWork).getStartTime();
			inputWork.setStartTime(DateUtils.getParseTime(time));
			inputWork.setEndTime(DateUtils.getParseTime(time));

			// 挿入処理実行
			dao.insert(inputWork);

			// コミット
			CommonDbUtil.commit();

		} finally {
			// 処理完了後、コネクションMapからコネクションを削除
			CommonDbUtil.closeConnection();
		}

	}

	/**
	 * 作業論理削除処理
	 * 
	 * @param inputWork
	 * @throws BusinessException
	 */
	public void delete(Work inputWork) throws BusinessException {

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
	}

	/**
	 * 編集作業検索処理
	 * 
	 * @param inputWork
	 * @return
	 */
	public Work getEditWork(Work inputWork) {

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

		// DBから取得
		return output;
	}

	/**
	 * 作業更新処理
	 * 
	 * @param inputWork
	 */
	public void updateWork(Work inputWork) {

		try {
			// トランザクション管理設定
			CommonDbUtil.openConnection(false);

			// 更新処理実行
			WorkDao dao = new WorkDao();
			dao.updateWork(inputWork);

			// コミット
			CommonDbUtil.commit();

		} finally {
			// 処理完了後、コネクションMapからコネクションを削除
			CommonDbUtil.closeConnection();
		}
	}

	/**
	 * 作業保存処理 TODO実装途中
	 * 
	 * @param inputWork
	 */
	public void saveWork(Work inputWork) {

		try {
			// トランザクション管理設定
			CommonDbUtil.openConnection(false);

			// 保存処理実行
			WorkDao dao = new WorkDao();
			dao.saveWork(inputWork);

			// コミット
			CommonDbUtil.commit();

		} finally {
			// 処理完了後、コネクションMapからコネクションを削除
			CommonDbUtil.closeConnection();
		}
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
	public List<Work> findWorking(Work inputWork) {

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
	 */
	public void calcWorkTime(Work inputWork) {

		LocalTime startTime = DateUtils.getParseTime(inputWork.getStartTime());
		logger.info("開始時間:{}", startTime);

		LocalTime endTime = DateUtils.getParseTime(inputWork.getEndTime());
		LocalTime calcTime = endTime.minusHours(startTime.getHour());
		LocalTime workingTime = calcTime.minusMinutes(startTime.getMinute());
		inputWork.setWorkingTime(DateUtils.getParseTime(workingTime));
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
						DateUtils.getFomatTime(registerForm.getStartTime()));
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
	public ValidationResult inputCheckWhenStart(WorkRegisterForm form) {

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

		// 作業内容
		String contents = form.getContents();
		if (contents == null) {
			throw new SystemException(
					PropertyUtils.getValue(MsgCodeDef.BAD_INPUT));
		} else {
			// サイズチェック
			validationChek = InputValidation.inputSize(contents, 0, 40);
			if (!validationChek) {
				result.addErrorMsg(PropertyUtils.getValue(MsgCodeDef.SIZE_ERROR,
						"作業内容", "0", "40"));
				result.setCheckResult(false);
			}
		}

		// 備考チェック
		String note = form.getNote();
		if (note == null) {
			throw new SystemException("不正な入力");
		} else {
			// サイズチェック
			validationChek = InputValidation.inputSize(note, 0, 40);
			if (!validationChek) {
				result.addErrorMsg(PropertyUtils.getValue(MsgCodeDef.SIZE_ERROR,
						"備考", "0", "40"));
				result.setCheckResult(validationChek);
			}
		}
		return result;
	}

	/**
	 * 履歴表示ロジック TODO実装途中
	 * 
	 * @param inputWork
	 * @throws BusinessException
	 */
	public void history(Work inputWork) throws BusinessException {

		LocalDate workDate = inputWork.getWorkDate();
		logger.info("入力日付:{}", workDate);

		if (workDate == null) {
			throw new BusinessException("日付を入力してください。");
		}

		// 過去日チェック
		logger.info("今日の日付:{}", LocalDate.now());
		if (workDate.isAfter(LocalDate.now())) {
			throw new BusinessException("過去日を選択してください。");
		}

		// 未保存データ削除
		deleteUnSaveWork(inputWork.getUserName());

	}

	/**
	 * 作業追加処理
	 * 
	 * @param inputWork
	 * @throws BusinessException
	 */
	public void addWork(Work inputWork) throws BusinessException {

		try {
			// トランザクション管理設定
			CommonDbUtil.openConnection(false);

			// 作業中の作業の場合、追加不可。
			WorkDao dao = new WorkDao();

			Work work = dao.getEndTime(inputWork);
			// 追加する作業の開始、終了時間を取得
			LocalTime time = work.getEndTime();
			inputWork.setStartTime(DateUtils.getParseTime(time));
			inputWork.setEndTime(DateUtils.getParseTime(time));

			dao.insert(inputWork);

			// コミット
			CommonDbUtil.commit();

		} finally {
			// 処理完了後、コネクションMapからコネクションを削除
			CommonDbUtil.closeConnection();
		}
	}
}