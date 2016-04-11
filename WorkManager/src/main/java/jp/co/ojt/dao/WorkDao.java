package jp.co.ojt.dao;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jp.co.ojt.dao.dto.WorkDto;
import jp.co.ojt.db.util.CommonDbUtil;
import jp.co.ojt.model.Work;

public class WorkDao {

	private static final Logger logger = LoggerFactory.getLogger(Work.class);

	public List<Work> findAllWork(Work work) {

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

	private HashMap<Integer, Object> createParamMap(StringBuilder sql, WorkDto dto) {

		Map<String, Object> dtoMap = createDtoMap(dto);

		Map<Integer, String> sqlParamMap = CommonDbUtil.createSqlMap(sql);

		HashMap<Integer, Object> paramMap = new HashMap<>();

		for (Entry<String, Object> fieldEntry : dtoMap.entrySet()) {

			for (Entry<Integer, String> sqlEntry : sqlParamMap.entrySet()) {
				if ((fieldEntry.getKey()).equals(sqlEntry.getValue())) {

					paramMap.put(sqlEntry.getKey(), fieldEntry.getValue());
				}
			}
		}
		return paramMap;

	}

	private HashMap<String, Object> createDtoMap(WorkDto dto) {
		// Dtoのフィールド名と値のMapを作成
		String regex = "get(([A-Z][a-zA-Z\\d]*))";
		Pattern ptm = Pattern.compile(regex);

		// Dtoのgetterからフィールド名を取得
		Method[] methods = dto.getClass().getMethods();
		HashMap<String, Object> dtoMap = new HashMap<>();

		for (Method method : methods) {
			// getterを抽出
			Matcher mat = ptm.matcher(method.getName());
			if (mat.find()) {
				String getter = method.getName();
				if (!getter.contains("Class")) {
					String fieldName = mat.group(1);
					fieldName = fieldName.substring(0, 1).toLowerCase() + fieldName.substring(1);

					Object value = null;
					try {
						value = method.invoke(dto, null);
					} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
						logger.info("リフレクション失敗", e);
					}

					dtoMap.put(fieldName, value);
				}
			}
		}

		for (String key : dtoMap.keySet()) {
			logger.info("Map内容[{}]:{}", key, dtoMap.get(key));
		}
		return dtoMap;
	}

	private static Work mappingDtoToModel(WorkDto dto) {

		Work work = new Work();

		work.setId(dto.getId());
		work.setUserName(dto.getUserName());
		work.setStartTime(dto.getStartTime());
		work.setEndTime(dto.getEndTime());
		work.setWorkingTime(dto.getWorkingTime());
		work.setContents(dto.getContents());
		work.setNote(dto.getNote());
		return work;
	}

	private static WorkDto mappingModelToDto(Work work) {

		WorkDto dto = new WorkDto();
		dto.setId(work.getId());
		dto.setUserName(work.getUserName());
		dto.setStartTime((Time) work.getStartTime());
		dto.setEndTime((Time) work.getEndTime());
		dto.setWorkingTime((Time) work.getWorkingTime());
		dto.setContents(work.getContents());
		dto.setNote(work.getNote());
		return dto;
	}

}
