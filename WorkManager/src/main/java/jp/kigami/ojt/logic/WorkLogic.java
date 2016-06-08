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
	
	/**
	 * 画面表示用フォームの取得
	 * 
	 * @param userName
	 * @return
	 */
	public WorkRegisterViewForm getWorkRegisterViewForm(String userName) {

		WorkRegisterViewForm form = new WorkRegisterViewForm();

		Work inputWork = new Work();
		inputWork.setUserName(userName);

		// 現在時間を設定
		inputWork.setWorkDate(LocalDate.now());

		// 仕掛作業取得
		List<Work> workList = findWorking(inputWork);

		// formに仕掛作業と作業状態フラグを設定
		if (workList.size() == 0) {
			logger.info("仕掛処理なし");
			form.setWork(null);
			form.setWorkingFlg(false);
		} else {
			logger.info("仕掛処理あり");
			form.setWork(workList.get(0));
			form.setWorkingFlg(true);
		}

		// 作業開始時間(初期表示用)を設定
		form.setNowTime(DateUtils.getNowTimeStr());

		return form;
	}

	/**
	 * 仕掛作業取得
	 * 
	 * @param inputWork
	 * @return
	 */
	public List<Work> findWorking(Work inputWork) {
		WorkDao dao = new WorkDao();
		List<Work> workList = dao.findWorking(inputWork);
		return workList;
	}

	/**
	 * 作業完了処理
	 * 
	 * @param inputWork
	 * @throws BusinessException
	 */
	public void finishWork(Work inputWork) throws BusinessException {

		// 終了時間を設定
		inputWork.setEndTime(DateUtils.getNowTime());

		// 開始時間を設定
		LocalTime startTime = getStartTime(inputWork);
		inputWork.setStartTime(DateUtils.getParseTime(startTime));

		// 作業時間を計算
		WorkHelper helper = new WorkHelper();
		helper.calcWorkTime(inputWork);

		WorkDao dao = new WorkDao();
		dao.finishWork(inputWork);
	}
	
	
}
