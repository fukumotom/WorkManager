package test.test.ojt.logic;

import java.util.List;

import test.test.ojt.dao.WorkDao;
import test.test.ojt.model.Work;

public class WorkRegisterLogic {

	public List<Work> findWorking(Work inputWork) {

		WorkDao dao = new WorkDao();
		List<Work> workList = dao.findWorking(inputWork);
		return workList;
	}

}
