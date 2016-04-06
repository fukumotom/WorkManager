package jp.co.ojt.dao;

import java.sql.Time;
import java.util.ArrayList;

import jp.co.ojt.dao.dto.WorkDto;
import jp.co.ojt.db.util.CommonDbUtil;
import jp.co.ojt.model.Work;

public class WorkDao {

	public ArrayList<Work> findAllWork(Work work) {

		// load sqlFile
		String sql = CommonDbUtil.readSql("getWorkList");

		WorkDto dto = mappingModelToDto(work);

		ArrayList<WorkDto> dtoList = CommonDbUtil.findAllWork(sql, dto);

		ArrayList<Work> workList = new ArrayList<>();
		for (WorkDto dtoElm : dtoList) {
			Work elm = mappingDtoToModel(dtoElm);
			workList.add(elm);
		}

		return workList;
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
