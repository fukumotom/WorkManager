package jp.kigami.ojt.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

public class Work implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;

	private String userName;

	private LocalTime startTime;

	private LocalTime endTime;

	private LocalTime workingTime;

	private String contents;

	private String note;

	private boolean deleteFlg;

	private LocalDate workDate;

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

	public LocalTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalTime startTime) {
		this.startTime = startTime;
	}

	public LocalTime getEndTime() {
		return endTime;
	}

	public void setEndTime(LocalTime endTime) {
		this.endTime = endTime;
	}

	public LocalTime getWorkingTime() {
		return workingTime;
	}

	public void setWorkingTime(LocalTime workingTime) {
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

	public boolean getDeleteFlg() {
		return deleteFlg;
	}

	public void setDeleteFlg(boolean b) {
		this.deleteFlg = b;
	}

	public LocalDate getWorkDate() {
		return workDate;
	}

	public void setWorkDate(LocalDate workDate) {
		this.workDate = workDate;
	}

}
