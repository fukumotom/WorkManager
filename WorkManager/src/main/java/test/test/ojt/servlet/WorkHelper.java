package test.test.ojt.servlet;

import java.time.LocalDate;
import java.time.LocalTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import test.test.ojt.common.exception.BusinessException;
import test.test.ojt.common.util.DateUtils;
import test.test.ojt.logic.WorkLogic;
import test.test.ojt.model.Work;

public class WorkHelper {
	private static final Logger logger = LoggerFactory
			.getLogger(WorkHelper.class);

	private void insert(Work work, String actionName) throws BusinessException {

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

	private void delete(Work work) throws BusinessException {

		WorkLogic logic = new WorkLogic();
		logic.delete(work);
	}

	/**
	 * 履歴取得用日付の入力チェック
	 * 
	 * @param historyDate
	 * @param request
	 * @return
	 * @throws BusinessException
	 */
	private void dateCheck(Work inputWork) throws BusinessException {

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

	public void check(String actionName, Work inputWork)
			throws BusinessException {

		switch (actionName) {
		case "insert":
		case "add":
			insert(inputWork, actionName);
			break;

		case "delete":
			delete(inputWork);
			break;

		case "history":
			dateCheck(inputWork);
			break;

		default:
			logger.warn("想定外の処理");
			break;
		}
	}
}
