package jp.co.ojt.model;

import java.io.Serializable;
import java.util.Date;

public class Work implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;

	private String userName;

	private Date startTime;

	private Date endTime;

	private Date workingTime;

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

	public void setUserName(String user_name) {
		this.userName = user_name;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Date getWorkingTime() {
		return workingTime;
	}

	public void setWorkingTime(Date workingTime) {
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
