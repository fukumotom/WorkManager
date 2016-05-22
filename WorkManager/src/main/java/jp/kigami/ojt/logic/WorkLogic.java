package jp.kigami.ojt.logic;

import java.time.LocalTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jp.kigami.ojt.common.exception.BusinessException;
import jp.kigami.ojt.common.util.DateUtils;
import jp.kigami.ojt.dao.WorkDao;
import jp.kigami.ojt.model.Work;

public class WorkLogic {

	private static final Logger logger = LoggerFactory
			.getLogger(WorkLogic.class);

	public List<Work> findAllWork(Work work) {
		WorkDao dao = new WorkDao();
		List<Work> workList = dao.findAllWork(work);
		return workList;

	}

	public LocalTime getStartTime(Work inputWork) throws BusinessException {
		WorkDao dao = new WorkDao();
		Work work = dao.getStartTime(inputWork);
		return work.getStartTime();
	}

	public LocalTime getEndTime(Work inputWork) throws BusinessException {
		WorkDao dao = new WorkDao();
		Work work = dao.getEndTime(inputWork);
		return work.getStartTime();
	}

	public void insertWork(Work inputWork) {
		WorkDao dao = new WorkDao();
		dao.insert(inputWork);

	}

	public void delete(Work inputWork) throws BusinessException {
		WorkDao dao = new WorkDao();
		dao.delete(inputWork);
	}

	public List<Work> findWorking(Work inputWork) {

		WorkDao dao = new WorkDao();
		List<Work> workList = dao.findWorking(inputWork);
		return workList;
	}

	public void finishWork(Work inputWork) throws BusinessException {

		// 作業時間計算
		LocalTime startTime = DateUtils.getParseTime(inputWork.getStartTime());
		logger.info("開始時間:{}", startTime);
		LocalTime endTime = DateUtils.getParseTime(inputWork.getEndTime());
		LocalTime calcTime1 = endTime.minusHours(startTime.getHour());
		LocalTime workingTime = calcTime1.minusMinutes(startTime.getMinute());
		// LocalTime workingTime = LocalTime.of(hour.getHour(),
		// minute.getMinute());
		inputWork.setWorkingTime(DateUtils.getParseTime(workingTime));
		logger.info("作業時間:{}", inputWork.getWorkingTime());

		WorkDao dao = new WorkDao();
		dao.finishWork(inputWork);
	}

	public void startWork(Work inputWork) throws BusinessException {
		WorkDao dao = new WorkDao();
		dao.startWork(inputWork);
	}

	/**
	 * 編集作業検索処理
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
	 * 作業更新処理
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
}
