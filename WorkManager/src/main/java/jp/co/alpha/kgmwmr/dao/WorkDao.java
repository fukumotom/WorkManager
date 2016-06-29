package jp.co.alpha.kgmwmr.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jp.co.alpha.kgmwmr.common.exception.BusinessException;
import jp.co.alpha.kgmwmr.common.exception.SystemException;
import jp.co.alpha.kgmwmr.dao.dto.WorkDto;
import jp.co.alpha.kgmwmr.db.util.CommonDbUtil;
import jp.co.alpha.kgmwmr.model.Work;

public class WorkDao {

	private static final Logger logger = LoggerFactory.getLogger(Work.class);

	/**
	 * 編集作業取得SQL発行
	 * 
	 * @param inputWork
	 * @return
	 */
	public Work getEditWork(Work work) {

		// load SQLfile
		StringBuilder sql = CommonDbUtil.readSql("getEditWork.sql");

		// DTOに詰め替え
		WorkDto dto = new WorkDto();
		CommonDbUtil.beanMaping(work, dto);

		HashMap<Integer, Object> paramMap = CommonDbUtil.createParamMap(sql,
				dto);

		WorkDto resultDto = CommonDbUtil.findOne(sql.toString(), paramMap,
				WorkDto.class);

		// modelに詰め替え
		Work outputWork = new Work();
		CommonDbUtil.beanMaping(resultDto, outputWork);

		return outputWork;
	}

	/**
	 * 画面から取得したIDから作業を取得するSQL発行
	 * 
	 * @param inputWork
	 * @return
	 */
	public Work getSelectWork(Work work) {

		// load SQLfile
		StringBuilder sql = CommonDbUtil.readSql("getSelectWork.sql");

		// DTOに詰め替え
		WorkDto dto = new WorkDto();
		CommonDbUtil.beanMaping(work, dto);

		HashMap<Integer, Object> paramMap = CommonDbUtil.createParamMap(sql,
				dto);

		WorkDto resultDto = CommonDbUtil.findOne(sql.toString(), paramMap,
				WorkDto.class);

		// modelに詰め替え
		Work outputWork = new Work();
		CommonDbUtil.beanMaping(resultDto, outputWork);

		return outputWork;
	}

	/**
	 * 作業中の作業取得SQL発行
	 * 
	 * @param work
	 * @return
	 */
	public List<Work> findWorking(Work work) {

		// load SQLfile
		StringBuilder sql = CommonDbUtil.readSql("getWorking.sql");

		// DTOに詰め替え
		WorkDto dto = new WorkDto();
		CommonDbUtil.beanMaping(work, dto);

		HashMap<Integer, Object> paramMap = CommonDbUtil.createParamMap(sql,
				dto);

		List<WorkDto> dtoList = CommonDbUtil.getDtoList(sql.toString(),
				paramMap, WorkDto.class);

		ArrayList<Work> workList = new ArrayList<>();
		for (WorkDto dtoElm : dtoList) {

			// modelに詰め替え
			Work elm = new Work();
			CommonDbUtil.beanMaping(dtoElm, elm);

			workList.add(elm);
		}

		return workList;
	}

	/**
	 * 作業終了SQL発行
	 * 
	 * @param work
	 * @throws BusinessException
	 */
	public void finishWork(Work work) throws BusinessException {

		StringBuilder sql = CommonDbUtil.readSql("finishWork.sql");

		// DTOに詰め替え
		WorkDto dto = new WorkDto();
		CommonDbUtil.beanMaping(work, dto);

		HashMap<Integer, Object> paramMap = CommonDbUtil.createParamMap(sql,
				dto);

		int resultCnt = CommonDbUtil.getDbResultCnt(sql.toString(), paramMap);
		if (resultCnt == 0) {
			throw new BusinessException("すでに完了した作業です。");
		} else if (resultCnt == 1) {
			logger.info("正常に作業が終了");
		} else {
			throw new SystemException("作業終了が正常に行われませんでした。");
		}
	}

	/**
	 * 作業開始SQL発行
	 * 
	 * @param work
	 */
	public void startWork(Work work) {

		StringBuilder sql = CommonDbUtil.readSql("startWork.sql");

		// DTOに詰め替え
		WorkDto dto = new WorkDto();
		CommonDbUtil.beanMaping(work, dto);

		HashMap<Integer, Object> paramMap = CommonDbUtil.createParamMap(sql,
				dto);
		int resultCnt = CommonDbUtil.getDbResultCnt(sql.toString(), paramMap);
		if (resultCnt > 0) {
			logger.info("正常に作業を開始");
			logger.info("作業開始件数:{}件", resultCnt);
		} else {
			throw new SystemException("作業開始が正常に行われませんでした。");
		}
	}

	/**
	 * 作業リスト取得SQL発行
	 * 
	 * @param work
	 * @return
	 */
	public List<Work> findAllWork(Work work) {

		// load sqlFile
		String sqlName;
		if (work.isDelete()) {
			sqlName = "getWorkDelList.sql";
		} else if (work.getWorkDate() != null) {
			sqlName = "getWorkPastList.sql";
		} else {
			sqlName = "getWorkList.sql";
		}
		StringBuilder sql = CommonDbUtil.readSql(sqlName);

		// DTOに詰め替え
		WorkDto dto = new WorkDto();
		CommonDbUtil.beanMaping(work, dto);

		HashMap<Integer, Object> paramMap = CommonDbUtil.createParamMap(sql,
				dto);

		List<WorkDto> dtoList = CommonDbUtil.getDtoList(sql.toString(),
				paramMap, WorkDto.class);

		ArrayList<Work> workList = new ArrayList<>();
		for (WorkDto dtoElm : dtoList) {

			// modelに詰め替え
			Work elm = new Work();
			CommonDbUtil.beanMaping(dtoElm, elm);

			workList.add(elm);
		}

		return workList;
	}

	/**
	 * 開始時間取得SQL発行
	 * 
	 * @param inputWork
	 * @return
	 */
	public Work findStartTime(Work work) {

		// load SQLfile
		StringBuilder sql = CommonDbUtil.readSql("getStartTime.sql");

		// DTOに詰め替え
		WorkDto dto = new WorkDto();
		CommonDbUtil.beanMaping(work, dto);

		// パラメータ設定
		HashMap<Integer, Object> paramMap = CommonDbUtil.createParamMap(sql,
				dto);

		// 実行
		WorkDto resultDto = CommonDbUtil.findOne(sql.toString(), paramMap,
				WorkDto.class);

		// modelに詰め替え
		Work outputWork = new Work();
		CommonDbUtil.beanMaping(resultDto, outputWork);

		return outputWork;
	}

	/**
	 * 終了時間取得SQL発行
	 * 
	 * @param inputWork
	 * @return
	 * @throws BusinessException
	 */
	public Work getEndTime(Work work) throws BusinessException {

		// load SQLfile
		StringBuilder sql = CommonDbUtil.readSql("getEndTime.sql");

		// DTOに詰め替え
		WorkDto dto = new WorkDto();
		CommonDbUtil.beanMaping(work, dto);

		// パラメータ設定
		HashMap<Integer, Object> paramMap = CommonDbUtil.createParamMap(sql,
				dto);

		// 実行
		WorkDto resultDto = CommonDbUtil.findOne(sql.toString(), paramMap,
				WorkDto.class);

		if (resultDto.getEndTime() == null) {
			throw new BusinessException("作業中の下に追加はできません。");
		}

		// modelに詰め替え
		Work outputWork = new Work();
		CommonDbUtil.beanMaping(resultDto, outputWork);

		return outputWork;
	}

	/**
	 * 作業挿入処理SQL発行
	 * 
	 * @param inputWork
	 */
	public void insert(Work work) {

		// SQL読み込み
		StringBuilder sql = CommonDbUtil.readSql("insertWork.sql");

		// DTOに詰め替え
		WorkDto dto = new WorkDto();
		CommonDbUtil.beanMaping(work, dto);

		// パラメータ設定
		HashMap<Integer, Object> paramMap = CommonDbUtil.createParamMap(sql,
				dto);

		int resultCnt = CommonDbUtil.updata(sql.toString(), paramMap);
		logger.info("{}件挿入しました", resultCnt);

	}

	/**
	 * 作業終了処理SQL発行
	 * 
	 * @param inputWork
	 * @throws BusinessException
	 */
	public void delete(Work work) throws BusinessException {

		// SQL読み込み
		StringBuilder sql = CommonDbUtil.readSql("updateDeleteFlg.sql");

		// DTOに詰め替え
		WorkDto dto = new WorkDto();
		CommonDbUtil.beanMaping(work, dto);

		HashMap<Integer, Object> paramMap = CommonDbUtil.createParamMap(sql,
				dto);

		int resultCnt = CommonDbUtil.updata(sql.toString(), paramMap);

		if (resultCnt == 0) {
			throw new BusinessException("データは削除されています。");
		} else if (resultCnt > 1) {
			throw new SystemException("削除が正常に行われませんでした。");
		}
	}

	/**
	 * 作業更新保存SQL発行
	 * 
	 * @param inputWork
	 */
	public void updateWork(Work work) {

		// SQL読み込み
		StringBuilder sql = CommonDbUtil.readSql("updataWork.sql");

		// DTOに詰め替え
		WorkDto dto = new WorkDto();
		CommonDbUtil.beanMaping(work, dto);

		HashMap<Integer, Object> paramMap = CommonDbUtil.createParamMap(sql,
				dto);

		int resultCnt = CommonDbUtil.updata(sql.toString(), paramMap);
		logger.info("{}件更新しました", resultCnt);
	}

	/**
	 * 作業保存SQL発行
	 * 
	 * @param inputWork
	 */
	public void saveWork(Work work) {
		// SQL読み込み
		StringBuilder sql = CommonDbUtil.readSql("saveWork.sql");

		// DTOに詰め替え
		WorkDto dto = new WorkDto();
		CommonDbUtil.beanMaping(work, dto);

		HashMap<Integer, Object> paramMap = CommonDbUtil.createParamMap(sql,
				dto);

		int resultCnt = CommonDbUtil.updata(sql.toString(), paramMap);
		logger.info("{}件保存しました", resultCnt);
	}

	/**
	 * 未保存データの削除SQL発行
	 * 
	 * @param inputWork
	 */
	public void deleteUnSaveWork(Work work) {
		// SQL読み込み
		StringBuilder sql = CommonDbUtil.readSql("deleteUnSaveWork.sql");

		// DTOに詰め替え
		WorkDto dto = new WorkDto();
		CommonDbUtil.beanMaping(work, dto);

		HashMap<Integer, Object> paramMap = CommonDbUtil.createParamMap(sql,
				dto);

		int resultCnt = CommonDbUtil.updata(sql.toString(), paramMap);
		logger.debug("{}件未保存データ削除しました", resultCnt);

	}
}