package jp.kigami.ojt.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

public class Work implements Serializable {

	/**
	 * シリアルバージョン
	 */
	private static final long serialVersionUID = -5682236353241371166L;

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
	private LocalTime startTime;

	/**
	 * 作業終了時間
	 */
	private LocalTime endTime;

	/**
	 * 作業時間
	 */
	private LocalTime workingTime;

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
	private LocalDate workDate;

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
	public LocalTime getStartTime() {
		return startTime;
	}

	/**
	 * @param startTime
	 *            the startTime to set
	 */
	public void setStartTime(LocalTime startTime) {
		this.startTime = startTime;
	}

	/**
	 * @return the endTime
	 */
	public LocalTime getEndTime() {
		return endTime;
	}

	/**
	 * @param endTime
	 *            the endTime to set
	 */
	public void setEndTime(LocalTime endTime) {
		this.endTime = endTime;
	}

	/**
	 * @return the workingTime
	 */
	public LocalTime getWorkingTime() {
		return workingTime;
	}

	/**
	 * @param workingTime
	 *            the workingTime to set
	 */
	public void setWorkingTime(LocalTime workingTime) {
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
	public LocalDate getWorkDate() {
		return workDate;
	}

	/**
	 * @param workDate
	 *            the workDate to set
	 */
	public void setWorkDate(LocalDate workDate) {
		this.workDate = workDate;
	}

}
