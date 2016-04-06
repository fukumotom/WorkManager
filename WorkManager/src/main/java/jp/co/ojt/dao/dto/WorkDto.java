package jp.co.ojt.dao.dto;

import java.io.Serializable;
import java.sql.Time;

public class WorkDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;

	private String userName;

	private Time startTime;

	private Time endTime;

	private Time workingTime;

	private String contents;

	private String note;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Time getStartTime() {
		return startTime;
	}

	public void setStartTime(Time startTime) {
		this.startTime = startTime;
	}

	public Time getEndTime() {
		return endTime;
	}

	public void setEndTime(Time endTime) {
		this.endTime = endTime;
	}

	public Time getWorkingTime() {
		return workingTime;
	}

	public void setWorkingTime(Time workingTime) {
		this.workingTime = workingTime;
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

}
