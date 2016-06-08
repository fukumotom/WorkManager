package jp.kigami.ojt.form;

/**
 * 作業登録処理のフォーム
 * 
 * @author kigami
 *
 */
public class WorkRegisterForm {

	/**
	 * 
	 * 作業ID
	 */
	private String id;

	/**
	 * 開始時間
	 */
	private String startTime;

	/**
	 * 作業内容
	 */
	private String contents;

	/**
	 * 備考
	 */
	private String note;

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

}
