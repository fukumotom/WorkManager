package test.test.ojt.logic;

import java.time.LocalTime;
import java.util.List;

import test.test.ojt.common.exception.BusinessException;
import test.test.ojt.common.exception.SystemException;
import test.test.ojt.dao.WorkDao;
import test.test.ojt.model.Work;

public class WorkListLogic {

	public List<Work> findAllWork(Work work) throws SystemException {
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
}
