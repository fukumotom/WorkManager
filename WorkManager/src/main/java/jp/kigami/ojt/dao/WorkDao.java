package jp.kigami.ojt.dao;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalTime;
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

		Map<String, Object> dtoMap = CommonDbUtil.createDtoMap(dto,
				WorkDto.class);

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

	private static Work mappingDtoToModel(WorkDto dto) {

		Work work = new Work();

		work.setId(dto.getId());
		work.setUserName(dto.getUserName());
		work.setStartTime(dto.getStartTime().toLocalTime());
		if (dto.getEndTime() != null) {
			work.setEndTime(dto.getEndTime().toLocalTime());
		}
		if (dto.getWorkingTime() != null) {
			work.setWorkingTime(dto.getWorkingTime().toLocalTime());
		}
		work.setContents(dto.getContents());
		work.setNote(dto.getNote());
		return work;
	}

	private static WorkDto mappingModelToDto(Work work) {

		WorkDto dto = new WorkDto();
		dto.setId(work.getId());
		dto.setUserName(work.getUserName());
		if (work.getStartTime() != null) {
			dto.setStartTime(Time.valueOf(work.getStartTime()));
		}
		if (work.getEndTime() != null) {
			dto.setEndTime(Time.valueOf(work.getEndTime()));
		}
		if (work.getWorkingTime() != null) {
			dto.setWorkingTime(Time.valueOf(work.getWorkingTime()));
		}
		dto.setContents(work.getContents());
		dto.setNote(work.getNote());
		if (work.getDeleteFlg()) {
			dto.setDeleteFlg(1);
		} else {
			dto.setDeleteFlg(0);
		}
		if (work.getWorkDate() != null) {
			dto.setWorkDate(Date.valueOf(work.getWorkDate()));
		}
		return dto;
	}

	/**
	 * 編集するWork情報を取得
	 * 
	 * @param inputWork
	 * @return
	 */
	public Work getEditWork(Work work) {

		// load SQLfile
		StringBuilder sql = CommonDbUtil.readSql("getEditWork.sql");

		WorkDto dto = mappingModelToDto(work);
		HashMap<Integer, Object> paramMap = createParamMap(sql, dto);

		WorkDto resultDto = CommonDbUtil.findEditWork(sql.toString(), paramMap);

		return mappingDtoToModel(resultDto);
	}

	public List<Work> findWorking(Work work) {

		// load SQLfile
		StringBuilder sql = CommonDbUtil.readSql("getWorking.sql");

		WorkDto dto = mappingModelToDto(work);
		HashMap<Integer, Object> paramMap = createParamMap(sql, dto);

		List<WorkDto> dtoList = CommonDbUtil.findWorking(sql.toString(),
				paramMap);

		ArrayList<Work> workList = new ArrayList<>();
		for (WorkDto dtoElm : dtoList) {
			Work elm = mappingDtoToModel(dtoElm);
			workList.add(elm);
		}

		return workList;
	}

	public void finishWork(Work work) throws BusinessException {

		StringBuilder sql = CommonDbUtil.readSql("finishWork.sql");

		WorkDto dto = mappingModelToDto(work);

		HashMap<Integer, Object> paramMap = createParamMap(sql, dto);

		int resultCnt = CommonDbUtil.finishWork(sql.toString(), paramMap);
		if (resultCnt == 0) {
			throw new BusinessException("すでに完了した作業です。");
		} else if (resultCnt == 1) {
			logger.info("正常に作業が終了");
		} else {
			throw new SystemException("作業終了が正常に行われませんでした。");
		}
	}

	public void startWork(Work work) throws BusinessException {

		StringBuilder sql = CommonDbUtil.readSql("startWork.sql");

		WorkDto dto = mappingModelToDto(work);

		HashMap<Integer, Object> paramMap = createParamMap(sql, dto);
		int resultCnt = CommonDbUtil.startWork(sql.toString(), paramMap);
		if (resultCnt > 0) {
			logger.info("正常に作業を開始");
			logger.info("作業開始件数:{}件", resultCnt);
		} else {
			throw new SystemException("作業開始が正常に行われませんでした。");
		}
	}

	public List<Work> findAllWork(Work work) {

		// load sqlFile
		String sqlName;
		if (work.getDeleteFlg()) {
			sqlName = "getWorkDelList.sql";
		} else if (work.getWorkDate() != null) {
			sqlName = "getWorkPastList.sql";
		} else {
			sqlName = "getWorkList.sql";
		}
		StringBuilder sql = CommonDbUtil.readSql(sqlName);

		WorkDto dto = mappingModelToDto(work);

		HashMap<Integer, Object> paramMap = createParamMap(sql, dto);

		List<WorkDto> dtoList = CommonDbUtil.findAllWork(sql.toString(),
				paramMap);

		ArrayList<Work> workList = new ArrayList<>();
		for (WorkDto dtoElm : dtoList) {
			Work elm = mappingDtoToModel(dtoElm);
			workList.add(elm);
		}

		return workList;
	}

	public Work getStartTime(Work inputWork) throws BusinessException {

		// load SQLfile
		StringBuilder sql = CommonDbUtil.readSql("getStartTime.sql");

		// DTOに詰め替え
		WorkDto dto = mappingModelToDto(inputWork);

		// パラメータ設定
		HashMap<Integer, Object> paramMap = createParamMap(sql, dto);

		// 実行
		LocalTime startTime = CommonDbUtil.findTime(sql.toString(), paramMap,
				"start_time");

		Work resultWork = new Work();
		resultWork.setStartTime(startTime);

		return resultWork;
	}

	public Work getEndTime(Work inputWork) throws BusinessException {

		// load SQLfile
		StringBuilder sql = CommonDbUtil.readSql("getEndTime.sql");

		// DTOに詰め替え
		WorkDto dto = mappingModelToDto(inputWork);

		// パラメータ設定
		HashMap<Integer, Object> paramMap = createParamMap(sql, dto);

		// 実行
		LocalTime startTime = CommonDbUtil.findTime(sql.toString(), paramMap,
				"end_time");

		Work resultWork = new Work();
		resultWork.setStartTime(startTime);

		return resultWork;
	}

	public void insert(Work inputWork) {

		// SQL読み込み
		StringBuilder sql = CommonDbUtil.readSql("insertWork.sql");

		// DTOに詰め替え
		WorkDto dto = mappingModelToDto(inputWork);

		// パラメータ設定
		HashMap<Integer, Object> paramMap = createParamMap(sql, dto);

		CommonDbUtil.insertWork(sql.toString(), paramMap);

	}

	public void delete(Work inputWork) throws BusinessException {

		// SQL読み込み
		StringBuilder sql = CommonDbUtil.readSql("updateDeleteFlg.sql");

		WorkDto dto = mappingModelToDto(inputWork);

		HashMap<Integer, Object> paramMap = createParamMap(sql, dto);

		CommonDbUtil.deleteWork(sql.toString(), paramMap);

	}

	public void updateWork(Work inputWork) {

		// SQL読み込み
		StringBuilder sql = CommonDbUtil.readSql("updataWork.sql");

		WorkDto dto = mappingModelToDto(inputWork);

		HashMap<Integer, Object> paramMap = createParamMap(sql, dto);

		CommonDbUtil.updataWork(sql.toString(), paramMap);
	}

	public void saveWork(Work inputWork) {
		// SQL読み込み
		StringBuilder sql = CommonDbUtil.readSql("saveWork.sql");

		WorkDto dto = mappingModelToDto(inputWork);

		HashMap<Integer, Object> paramMap = createParamMap(sql, dto);

		CommonDbUtil.saveWork(sql.toString(), paramMap);
	}

	public void deleteUnSaveWork(Work inputWork) {
		// SQL読み込み
		StringBuilder sql = CommonDbUtil.readSql("deleteUnSaveWork.sql");

		WorkDto dto = mappingModelToDto(inputWork);

		HashMap<Integer, Object> paramMap = createParamMap(sql, dto);

		CommonDbUtil.deleteUnSaveWork(sql.toString(), paramMap);

	}

}
