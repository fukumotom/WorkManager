package jp.co.ojt.logic;

import java.util.ArrayList;

import jp.co.ojt.dao.WorkDao;
import jp.co.ojt.model.Work;

public class WorkListLogic {

	public ArrayList<Work> findAllWork(Work work) {

		WorkDao dao = new WorkDao();

		ArrayList<Work> workList = dao.findAllWork(work);

		return workList;

	}
}
