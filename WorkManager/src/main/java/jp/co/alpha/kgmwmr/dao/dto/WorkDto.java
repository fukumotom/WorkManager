package jp.co.alpha.kgmwmr.dao.dto;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;

public class WorkDto implements Serializable {

	/**
	 * シリアルバージョン
	 */
	private static final long serialVersionUID = 4728983518352414937L;

	/**
	 * 作業ID
	 */
	private Integer id;

	/**
	 * ユーザ名
	 */
	private String userName;

	/**
	 * 作業開始時間
	 */
	private Time startTime;

	/**
	 * 作業終了時間
	 */
	private Time endTime;

	/**
	 * 作業時間
	 */
	private Time workingTime;

	/**
	 * 作業内容
	 */
	private String contents;

	/**
	 * 備考
	 */
	private String note;

	/**
	 * 削除フラグ
	 */
	private boolean deleteFlg;

	/**
	 * 作業日付
	 */
	private Date workDate;

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName
	 *            the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return the startTime
	 */
	public Time getStartTime() {
		return startTime;
	}

	/**
	 * @param startTime
	 *            the startTime to set
	 */
	public void setStartTime(Time startTime) {
		this.startTime = startTime;
	}

	/**
	 * @return the endTime
	 */
	public Time getEndTime() {
		return endTime;
	}

	/**
	 * @param endTime
	 *            the endTime to set
	 */
	public void setEndTime(Time endTime) {
		this.endTime = endTime;
	}

	/**
	 * @return the workingTime
	 */
	public Time getWorkingTime() {
		return workingTime;
	}

	/**
	 * @param workingTime
	 *            the workingTime to set
	 */
	public void setWorkingTime(Time workingTime) {
		this.workingTime = workingTime;
	}

	/**
	 * @return the contents
	 */
	public String getContents() {
		return contents;
	}

	/**
	 * @param contents
	 *            the contents to set
	 */
	public void setContents(String contents) {
		this.contents = contents;
	}

	/**
	 * @return the note
	 */
	public String getNote() {
		return note;
	}

	/**
	 * @param note
	 *            the note to set
	 */
	public void setNote(String note) {
		this.note = note;
	}

	/**
	 * @return the deleteFlg
	 */
	public boolean isDeleteFlg() {
		return deleteFlg;
	}

	/**
	 * @param deleteFlg
	 *            the deleteFlg to set
	 */
	public void setDeleteFlg(boolean deleteFlg) {
		this.deleteFlg = deleteFlg;
	}

	/**
	 * @return the workDate
	 */
	public Date getWorkDate() {
		return workDate;
	}

	/**
	 * @param workDate
	 *            the workDate to set
	 */
	public void setWorkDate(Date workDate) {
		this.workDate = workDate;
	}

}
