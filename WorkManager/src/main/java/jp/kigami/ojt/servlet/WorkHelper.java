package jp.kigami.ojt.servlet;

import java.time.LocalDate;
import java.time.LocalTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jp.kigami.ojt.common.exception.BusinessException;
import jp.kigami.ojt.common.util.DateUtils;
import jp.kigami.ojt.logic.WorkLogic;
import jp.kigami.ojt.model.Work;

public class WorkHelper {
	private static final Logger logger = LoggerFactory
			.getLogger(WorkHelper.class);

	/**
	 * 作業追加/挿入処理
	 * 
	 * @param work
	 * @param actionName
	 * @throws BusinessException
	 */
	public void insert(Work work, String actionName) throws BusinessException {

		WorkLogic logic = new WorkLogic();
		LocalTime time;
		if ("insert".equals(actionName)) {
			time = logic.getStartTime(work);
		} else {
			time = logic.getEndTime(work);
		}
		work.setStartTime(DateUtils.getParseTime(time));
		work.setEndTime(DateUtils.getParseTime(time));
		logic.insertWork(work);
	}

	/**
	 * 作業削除処理
	 * 
	 * @param work
	 * @throws BusinessException
	 */
	public void delete(Work work) throws BusinessException {

		WorkLogic logic = new WorkLogic();
		logic.delete(work);
	}

	/**
	 * 編集する作業の呼び出し
	 * 
	 * @param inputWork
	 *            編集する作業の検索条件
	 * @return
	 */
	public Work getEditWork(Work inputWork) {

		WorkLogic logic = new WorkLogic();
		return logic.getEditWork(inputWork);

	}

	/**
	 * 履歴取得用日付の入力チェック
	 * 
	 * @param historyDate
	 * @param request
	 * @return
	 * @throws BusinessException
	 */
	public void dateCheck(Work inputWork) throws BusinessException {

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
	}

	public void save(Work inputWork) {
		WorkLogic logic = new WorkLogic();
		logic.saveWork(inputWork);
	}

	public void deleteUnSaveWork(Work inputWork) {
		WorkLogic logic = new WorkLogic();
		logic.deleteUnSaveWork(inputWork);
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
		LocalTime calcTime1 = endTime.minusHours(startTime.getHour());
		LocalTime workingTime = calcTime1.minusMinutes(startTime.getMinute());
		inputWork.setWorkingTime(DateUtils.getParseTime(workingTime));
		logger.info("作業時間:{}", inputWork.getWorkingTime());
	}

}
