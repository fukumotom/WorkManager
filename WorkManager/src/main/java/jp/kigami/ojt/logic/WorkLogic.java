package jp.kigami.ojt.logic;

import java.time.LocalTime;
import java.util.List;

import jp.kigami.ojt.common.exception.BusinessException;
import jp.kigami.ojt.dao.WorkDao;
import jp.kigami.ojt.model.Work;

public class WorkLogic {

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

	public void deleteUnSaveWork(Work inputWork) {
		WorkDao dao = new WorkDao();
		dao.deleteUnSaveWork(inputWork);

	}
}
