package jp.co.alpha.kgmwmr.form;

/**
 * 作業登録処理のフォーム
 * 
 * @author kigami
 *
 */
public class WorkEditForm {

	/**
	 * 作業ID
	 */
	private String id;

	/**
	 * ログインユーザ名
	 */
	private String userName;

	/**
	 * 開始時間
	 */
	private String startTime;

	/**
	 * 終了時間
	 */
	private String endTime;

	/**
	 * 作業内容
	 */
	private String contents;

	/**
	 * 備考
	 */
	private String note;

	/**
	 * 作業日付
	 */
	private String workDate;

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return the startTime
	 */
	public String getStartTime() {
		return startTime;
	}

	/**
	 * @param startTime
	 *            the startTime to set
	 */
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
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
	 * @return the workDate
	 */
	public String getWorkDate() {
		return workDate;
	}

	/**
	 * @param workDate
	 *            the workDate to set
	 */
	public void setWorkDate(String workDate) {
		this.workDate = workDate;
	}

}
