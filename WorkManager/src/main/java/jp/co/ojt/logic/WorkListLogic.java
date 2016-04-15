package jp.co.ojt.logic;

import java.time.LocalTime;
import java.util.List;

import jp.co.ojt.common.exception.SystemException;
import jp.co.ojt.dao.WorkDao;
import jp.co.ojt.model.Work;

public class WorkListLogic {

	public List<Work> findAllWork(Work work) throws SystemException {

		WorkDao dao = new WorkDao();

		List<Work> workList = dao.findAllWork(work);

		return workList;

	}

	public LocalTime getStartTime(Work inputWork) {
		WorkDao dao = new WorkDao();
		
		Work work = dao.getStartTime(inputWork);
		
		return work.getStartTime();
	}
	
	public LocalTime getEndTime(Work inputWork) {
		WorkDao dao = new WorkDao();
		
		Work work = dao.getEndTime(inputWork);
		
		return work.getStartTime();
	}

	public void insertWork(Work inputWork) {
		
		WorkDao dao = new WorkDao();
		dao.insert(inputWork);
		
	}

	public void delete(Work inputWork) {
		WorkDao dao = new WorkDao();
		dao.delete(inputWork);
	}
}
