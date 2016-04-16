package jp.co.ojt.dao;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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

import jp.co.ojt.common.exception.BusinessException;
import jp.co.ojt.common.exception.SystemException;
import jp.co.ojt.dao.dto.WorkDto;
import jp.co.ojt.db.util.CommonDbUtil;
import jp.co.ojt.model.Work;

public class WorkDao {

	private static final Logger logger = LoggerFactory.getLogger(Work.class);

	public List<Work> findAllWork(Work work) throws SystemException {

		// load sqlFile
		StringBuilder sql = CommonDbUtil.readSql("getWorkList.sql");

		WorkDto dto = mappingModelToDto(work);

		HashMap<Integer, Object> paramMap = createParamMap(sql, dto);

		List<WorkDto> dtoList = CommonDbUtil.findAllWork(sql.toString(), paramMap);

		ArrayList<Work> workList = new ArrayList<>();
		for (WorkDto dtoElm : dtoList) {
			Work elm = mappingDtoToModel(dtoElm);
			workList.add(elm);
		}

		return workList;
	}

	public Work getStartTime(Work inputWork) {

		// load SQLfile
		StringBuilder sql = CommonDbUtil.readSql("getStartTime.sql");

		// DTOに詰め替え
		WorkDto dto = mappingModelToDto(inputWork);

		// パラメータ設定
		HashMap<Integer, Object> paramMap = createParamMap(sql, dto);

		// 実行
		LocalTime startTime = CommonDbUtil.findTime(sql.toString(), paramMap, "start_time");

		Work resultWork = new Work();
		resultWork.setStartTime(startTime);

		return resultWork;
	}

	public Work getEndTime(Work inputWork) {

		// load SQLfile
		StringBuilder sql = CommonDbUtil.readSql("getEndTime.sql");

		// DTOに詰め替え
		WorkDto dto = mappingModelToDto(inputWork);

		// パラメータ設定
		HashMap<Integer, Object> paramMap = createParamMap(sql, dto);

		// 実行
		LocalTime startTime = CommonDbUtil.findTime(sql.toString(), paramMap, "end_time");

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

	public void delete(Work inputWork) {

		// SQL読み込み
		StringBuilder sql = CommonDbUtil.readSql("deleteWork.sql");

		WorkDto dto = mappingModelToDto(inputWork);

		HashMap<Integer, Object> paramMap = createParamMap(sql, dto);

		CommonDbUtil.deleteWork(sql.toString(), paramMap);

	}

	private HashMap<Integer, Object> createParamMap(StringBuilder sql, WorkDto dto) {

		Map<String, Object> dtoMap = createDtoMap(dto);

		Map<Integer, String> sqlParamMap = CommonDbUtil.createSqlMap(sql);

		HashMap<Integer, Object> paramMap = new HashMap<>();

		for (Entry<String, Object> fieldEntry : dtoMap.entrySet()) {

			for (Entry<Integer, String> sqlEntry : sqlParamMap.entrySet()) {
				if ((fieldEntry.getKey()).equals(sqlEntry.getValue())) {

					paramMap.put(sqlEntry.getKey(), fieldEntry.getValue());
					logger.info("paramMap内容[{}]:{}", sqlEntry.getKey(), fieldEntry.getValue());
				}
			}
		}
		return paramMap;

	}

	private HashMap<String, Object> createDtoMap(WorkDto dto) {

		Field[] fields = dto.getClass().getFields();
		HashMap<String, Object> dtoMap = new HashMap<>();

		try {
			for (Field field : fields) {

				// フィールド名からgetterを取得
				String fName = field.getName();
				if (fName == "serialVersionUID") {
					continue;
				}
				String getter = "get" + fName.substring(0, 1).toUpperCase() + fName.substring(1);

				// getterから値を取得し、Mapにつめる
				Method method = dto.getClass().getMethod(getter, null);
				Object value = method.invoke(dto, null);
				dtoMap.put(fName, value);
			}

		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			throw new BusinessException(e);
		}

		for (Entry<String, Object> entry : dtoMap.entrySet()) {
			logger.info("DtoMap内容[{}]:{}", entry.getKey(), entry.getValue());
		}
		return dtoMap;
	}

	private static Work mappingDtoToModel(WorkDto dto) {

		Work work = new Work();

		work.setId(dto.getId());
		work.setUserName(dto.getUserName());
		work.setStartTime(dto.getStartTime().toLocalTime());
		work.setEndTime(dto.getEndTime().toLocalTime());
		work.setWorkingTime(dto.getWorkingTime().toLocalTime());
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
		dto.setDeleteFlg(work.getDeleteFlg());
		if (work.getWorkDate() != null) {
			dto.setWorkDate(Date.valueOf(work.getWorkDate()));
		}
		return dto;
	}

}
