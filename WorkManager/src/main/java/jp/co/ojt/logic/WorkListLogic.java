package jp.co.ojt.logic;

import java.util.List;

import jp.co.ojt.dao.WorkDao;
import jp.co.ojt.model.Work;

public class WorkListLogic {

	public List<Work> findAllWork(Work work) {

		WorkDao dao = new WorkDao();

		List<Work> workList = dao.findAllWork(work);

		return workList;

	}
}
