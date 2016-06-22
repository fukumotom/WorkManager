package jp.kigami.ojt.logic;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jp.kigami.ojt.common.exception.BusinessException;
import jp.kigami.ojt.common.exception.SystemException;
import jp.kigami.ojt.common.util.DateUtils;
import jp.kigami.ojt.common.util.InputValidation;
import jp.kigami.ojt.common.util.MsgCodeDef;
import jp.kigami.ojt.common.util.PropertyUtils;
import jp.kigami.ojt.common.util.ValidationResult;
import jp.kigami.ojt.dao.WorkDao;
import jp.kigami.ojt.form.WorkRegisterForm;
import jp.kigami.ojt.form.WorkRegisterViewForm;
import jp.kigami.ojt.model.Work;

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

	public List<Work> findAllWork(Work work) {
		WorkDao dao = new WorkDao();
		List<Work> workList = dao.findAllWork(work);
		return workList;

	}

	/**
	 * 引数の情報を条件にDBから開始時間を取得
	 * 
	 * @param inputWork
	 * @return
	 * @throws BusinessException
	 */
	public LocalTime getStartTime(Work inputWork) {
		WorkDao dao = new WorkDao();
		Work work = dao.findStartTime(inputWork);
		return work.getStartTime();
	}

	/**
	 * 作業挿入処理
	 * 
	 * @param inputWork
	 */
	public void insertWork(Work inputWork) {

		LocalTime time = getStartTime(inputWork);

		inputWork.setStartTime(DateUtils.getParseTime(time));
		inputWork.setEndTime(DateUtils.getParseTime(time));

		WorkDao dao = new WorkDao();
		dao.insert(inputWork);

	}

	public void delete(Work inputWork) throws BusinessException {
		WorkDao dao = new WorkDao();
		dao.delete(inputWork);
	}

	public void finishWork(Work inputWork) throws BusinessException {

		WorkDao dao = new WorkDao();
		dao.finishWork(inputWork);
	}

	/**
	 * 編集作業検索処理 TODO実装途中
	 * 
	 * @param inputWork
	 * @return
	 */
	public Work getEditWork(Work inputWork) {

		WorkDao dao = new WorkDao();
		// DBから取得
		return dao.getEditWork(inputWork);
	}

	/**
	 * 作業更新処理 TODO実装途中
	 * 
	 * @param inputWork
	 */
	public void updateWork(Work inputWork) {

		WorkDao dao = new WorkDao();
		dao.updateWork(inputWork);

	}

	public void saveWork(Work inputWork) {
		WorkDao dao = new WorkDao();
		dao.saveWork(inputWork);
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

		WorkDao dao = new WorkDao();
		dao.deleteUnSaveWork(inputWork);
	}

	/**
	 * 画面表示用フォームの取得
	 * 
	 * @param userName
	 * @return
	 */
	public WorkRegisterViewForm getWorkRegisterViewForm(String userName) {

		WorkRegisterViewForm form = new WorkRegisterViewForm();

		Work inputWork = new Work();
		inputWork.setUserName(userName);

		// 現在時間を設定
		inputWork.setWorkDate(LocalDate.now());

		// 仕掛作業取得
		List<Work> workList = findWorking(inputWork);

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
	 * 仕掛作業取得
	 * 
	 * @param inputWork
	 * @return
	 */
	public List<Work> findWorking(Work inputWork) {
		WorkDao dao = new WorkDao();
		List<Work> workList = dao.findWorking(inputWork);
		return workList;
	}

	/**
	 * 作業完了処理 TODO実装途中
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

		Work inputWork = new Work();
		// ユーザ名を設定
		inputWork.setUserName(userName);
		// IDを設定
		inputWork.setId(Integer.valueOf(deleteId));

		// 終了時間を設定
		inputWork.setEndTime(DateUtils.getNowTime());

		// 開始時間を設定
		LocalTime startTime = getStartTime(inputWork);
		inputWork.setStartTime(DateUtils.getParseTime(startTime));

		// 作業時間を計算
		calcWorkTime(inputWork);

		WorkDao dao = new WorkDao();
		dao.finishWork(inputWork);

		return getWorkRegisterViewForm(userName);
	}

	/**
	 * <<<<<<< 0586a435c5935cdd93587548b120d24c230c8829 ======= 作業時間の計算処理
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
	 * >>>>>>> modify workhelper #83 作業開始処理 仕掛作業がある場合は、仕掛作業を終了して 作業を開始する
	 * 
	 * @param userName
	 * 
	 * @param registerForm
	 * @return
	 * @throws BusinessException
	 */
	public WorkRegisterViewForm register(String userName,
			WorkRegisterForm registerForm) throws BusinessException {

		WorkRegisterViewForm form = getWorkRegisterViewForm(userName);

		// 入力チェック
		ValidationResult result = inputCheckWhenStart(registerForm);
		if (!result.isCheckResult()) {

			// 入力チェックエラーの場合、エラーメッセージを設定
			form.setErrMsgs(result.getErrorMsgs());
		} else {

			// 入力チェックOKの場合
			Work inputWork = new Work();

			// 登録情報を設定
			inputWork.setUserName(userName);
			if (!registerForm.getId().isEmpty()) {
				inputWork.setId(Integer.valueOf(registerForm.getId()));
			}
			inputWork.setStartTime(
					DateUtils.getFomatTime(registerForm.getStartTime()));
			inputWork.setContents(registerForm.getContents());
			inputWork.setNote(registerForm.getNote());

			// 作業開始の 同期処理
			workRegiste(inputWork);
		}

		return getWorkRegisterViewForm(userName);
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

		if (workList.size() == 1) {
			// 仕掛処理がある場合、終了
			finishWork(inputWork.getUserName(),
					workList.get(0).getId().toString());
		}

		// 作業開始
		WorkDao dao = new WorkDao();
		dao.startWork(inputWork);
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
	 * 作業追加処理 TODO実装途中
	 * 
	 * @param inputWork
	 * @throws BusinessException
	 */
	public void addWork(Work inputWork) throws BusinessException {

		// 作業中の作業の場合、追加不可。
		WorkDao dao = new WorkDao();
		Work work = dao.getEndTime(inputWork);
		LocalTime time = work.getEndTime();

		inputWork.setStartTime(DateUtils.getParseTime(time));
		inputWork.setEndTime(DateUtils.getParseTime(time));

		dao.insert(inputWork);
	}
}
