package jp.co.ojt.servlet;

import java.time.LocalDate;
import java.time.LocalTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jp.co.ojt.common.exception.BusinessException;
import jp.co.ojt.logic.WorkListLogic;
import jp.co.ojt.model.Work;

public class WorkHelper {
	private static final Logger logger = LoggerFactory.getLogger(WorkHelper.class);

	void idCheck(Integer id) throws BusinessException {
		if (id == null) {
			throw new BusinessException("行を選択して下さい。");
		}
	}

	void action(Work work, String actionName) throws BusinessException {

		WorkListLogic logic = new WorkListLogic();
		LocalTime time;
		switch (actionName) {

		case "insert":
			time = logic.getStartTime(work);
			work.setStartTime(time);
			work.setEndTime(time);

			logic.insertWork(work);

			break;

		case "add":
			time = logic.getEndTime(work);
			work.setStartTime(time);
			work.setEndTime(time);

			logic.insertWork(work);

			break;

		case "delete":
			logic.delete(work);
			break;
		}
	}

	/**
	 * 履歴取得用日付の入力チェック
	 * 
	 * @param historyDate
	 * @param request
	 * @return
	 * @throws BusinessException
	 */
	void dateCheck(Work inputWork) throws BusinessException {

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

}
