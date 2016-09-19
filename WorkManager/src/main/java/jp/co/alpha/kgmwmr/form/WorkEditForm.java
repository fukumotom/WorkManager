package jp.co.alpha.kgmwmr.form;

import java.util.ArrayList;

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
	 * 選択用作業内容リスト
	 */
	private ArrayList<String> contentsList;

	/**
	 * 備考
	 */
	private String note;

	/**
	 * エラーメッセージ
	 */
	private String errMsgs;

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
	 * @return the endTime
	 */
	public String getEndTime() {
		return endTime;
	}

	/**
	 * @param endTime
	 *            the endTime to set
	 */
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
	 * @return the contentsList
	 */
	public ArrayList<String> getContentsList() {
		return contentsList;
	}

	/**
	 * @param contentsList
	 *            the contentsList to set
	 */
	public void setContentsList(ArrayList<String> contentsList) {
		this.contentsList = contentsList;
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
	 * @return the errMsgs
	 */
	public String getErrMsgs() {
		return errMsgs;
	}

	/**
	 * @param errMsgs
	 *            the errMsgs to set
	 */
	public void setErrMsgs(String errMsgs) {
		this.errMsgs = errMsgs;
	}

	/**
	 * @return the noteList
	 */
	public ArrayList<String> getNoteList() {
		return noteList;
	}

	/**
	 * @param noteList
	 *            the noteList to set
	 */
	public void setNoteList(ArrayList<String> noteList) {
		this.noteList = noteList;
	}

	/**
	 * 選択用備考リスト
	 */
	private ArrayList<String> noteList;

}
