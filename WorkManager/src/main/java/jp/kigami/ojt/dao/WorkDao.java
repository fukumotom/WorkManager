package jp.kigami.ojt.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jp.kigami.ojt.common.exception.BusinessException;
import jp.kigami.ojt.common.exception.SystemException;
import jp.kigami.ojt.dao.dto.WorkDto;
import jp.kigami.ojt.db.util.CommonDbUtil;
import jp.kigami.ojt.model.Work;

public class WorkDao {

	private static final Logger logger = LoggerFactory.getLogger(Work.class);

	private HashMap<Integer, Object> createParamMap(StringBuilder sql,
			WorkDto dto) {

		Map<String, Object> dtoMap = CommonDbUtil.createBeanValueMap(dto);

		Map<Integer, String> sqlParamMap = CommonDbUtil.createSqlMap(sql);

		HashMap<Integer, Object> paramMap = new HashMap<>();

		for (Entry<String, Object> fieldEntry : dtoMap.entrySet()) {

			for (Entry<Integer, String> sqlEntry : sqlParamMap.entrySet()) {
				if ((fieldEntry.getKey()).equals(sqlEntry.getValue())) {

					paramMap.put(sqlEntry.getKey(), fieldEntry.getValue());
					logger.info("paramMap内容[{}]:{}", sqlEntry.getKey(),
							fieldEntry.getValue());
				}
			}
		}
		return paramMap;

	}

	// private static WorkDto mappingModelToDto(Work work) {
	//
	// WorkDto dto = new WorkDto();
	// dto.setId(work.getId());
	// dto.setUserName(work.getUserName());
	// if (work.getStartTime() != null) {
	// dto.setStartTime(Time.valueOf(work.getStartTime()));
	// }
	// if (work.getEndTime() != null) {
	// dto.setEndTime(Time.valueOf(work.getEndTime()));
	// }
	// if (work.getWorkingTime() != null) {
	// dto.setWorkingTime(Time.valueOf(work.getWorkingTime()));
	// }
	// dto.setContents(work.getContents());
	// dto.setNote(work.getNote());
	// if (work.getDeleteFlg()) {
	// dto.setDeleteFlg(1);
	// } else {
	// dto.setDeleteFlg(0);
	// }
	// if (work.getWorkDate() != null) {
	// dto.setWorkDate(Date.valueOf(work.getWorkDate()));
	// }
	// return dto;
	// }

	/**
	 * 編集作業取得SQL発行
	 * 
	 * @param inputWork
	 * @return
	 */
	public Work getEditWork(Work work) {

		// load SQLfile
		StringBuilder sql = CommonDbUtil.readSql("getEditWork.sql");

		WorkDto dto = CommonDbUtil.beanMaping(work, new WorkDto());
		HashMap<Integer, Object> paramMap = createParamMap(sql, dto);

		WorkDto resultDto = CommonDbUtil.findOne(sql.toString(), paramMap,
				WorkDto.class);

		return CommonDbUtil.beanMaping(resultDto, new Work());
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

		WorkDto dto = CommonDbUtil.beanMaping(work, new WorkDto());
		HashMap<Integer, Object> paramMap = createParamMap(sql, dto);

		List<WorkDto> dtoList = CommonDbUtil.getDtoList(sql.toString(),
				paramMap, WorkDto.class);

		ArrayList<Work> workList = new ArrayList<>();
		for (WorkDto dtoElm : dtoList) {
			Work elm = CommonDbUtil.beanMaping(dtoElm, new Work());
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

		WorkDto dto = CommonDbUtil.beanMaping(work, new WorkDto());

		HashMap<Integer, Object> paramMap = createParamMap(sql, dto);

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

		WorkDto dto = CommonDbUtil.beanMaping(work, new WorkDto());

		HashMap<Integer, Object> paramMap = createParamMap(sql, dto);
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
		if (work.isDeleteFlg()) {
			sqlName = "getWorkDelList.sql";
		} else if (work.getWorkDate() != null) {
			sqlName = "getWorkPastList.sql";
		} else {
			sqlName = "getWorkList.sql";
		}
		StringBuilder sql = CommonDbUtil.readSql(sqlName);

		WorkDto dto = CommonDbUtil.beanMaping(work, new WorkDto());

		HashMap<Integer, Object> paramMap = createParamMap(sql, dto);

		List<WorkDto> dtoList = CommonDbUtil.getDtoList(sql.toString(),
				paramMap, WorkDto.class);

		ArrayList<Work> workList = new ArrayList<>();
		for (WorkDto dtoElm : dtoList) {
			Work elm = CommonDbUtil.beanMaping(dtoElm, new Work());
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
		WorkDto dto = CommonDbUtil.beanMaping(work, new WorkDto());

		// パラメータ設定
		HashMap<Integer, Object> paramMap = createParamMap(sql, dto);

		// 実行
		WorkDto resultDto = CommonDbUtil.findOne(sql.toString(), paramMap,
				WorkDto.class);

		return CommonDbUtil.beanMaping(resultDto, new Work());
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
		WorkDto dto = CommonDbUtil.beanMaping(work, new WorkDto());

		// パラメータ設定
		HashMap<Integer, Object> paramMap = createParamMap(sql, dto);

		// 実行
		WorkDto resultDto = CommonDbUtil.findOne(sql.toString(), paramMap,
				WorkDto.class);

		if (resultDto.getEndTime() == null) {
			throw new BusinessException("作業中の下に追加はできません。");
		}

		return CommonDbUtil.beanMaping(resultDto, new Work());
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
		WorkDto dto = CommonDbUtil.beanMaping(work, new WorkDto());

		// パラメータ設定
		HashMap<Integer, Object> paramMap = createParamMap(sql, dto);

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

		WorkDto dto = CommonDbUtil.beanMaping(work, new WorkDto());

		HashMap<Integer, Object> paramMap = createParamMap(sql, dto);

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

		WorkDto dto = CommonDbUtil.beanMaping(work, new WorkDto());

		HashMap<Integer, Object> paramMap = createParamMap(sql, dto);

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

		WorkDto dto = CommonDbUtil.beanMaping(work, new WorkDto());

		HashMap<Integer, Object> paramMap = createParamMap(sql, dto);

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

		WorkDto dto = CommonDbUtil.beanMaping(work, new WorkDto());

		HashMap<Integer, Object> paramMap = createParamMap(sql, dto);

		int resultCnt = CommonDbUtil.updata(sql.toString(), paramMap);
		logger.debug("{}件未保存データ削除しました", resultCnt);

	}
	
	/**
	 * 今日の作業を未保存状態で複製するSQL発行
	 * 
	 * @param inputWork
	 */
	public void copyTodayWork(Work inputWork) {

		// SQL読み込み
		StringBuilder sql = CommonDbUtil.readSql("copyTodayWork.sql");

		WorkDto dto = mappingModelToDto(inputWork);

		HashMap<Integer, Object> paramMap = createParamMap(sql, dto);

		CommonDbUtil.copyWork(sql.toString(), paramMap);
	}
}
