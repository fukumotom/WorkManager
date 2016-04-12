package jp.co.ojt.logic;

import java.util.Date;
import java.util.List;

import jp.co.ojt.dao.WorkDao;
import jp.co.ojt.model.Work;

public class WorkListLogic {

	public List<Work> findAllWork(Work work) {

		WorkDao dao = new WorkDao();

		List<Work> workList = dao.findAllWork(work);

		return workList;

	}

	public Date getStartTime(Work inputWork) {
		WorkDao dao = new WorkDao();
		
		Work work = dao.getStartTime(inputWork);
		
		return work.getStartTime();
	}

	public void insertWork(Work inputWork) {
		// TODO Auto-generated method stub
		
	}
}
