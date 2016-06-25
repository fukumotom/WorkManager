package jp.co.alpha.kgmwmr.form;

import java.util.ArrayList;

import jp.co.alpha.kgmwmr.model.Work;

/**
 * 作業登録画面表示用のフォーム
 * 
 * @author kigami
 *
 */
public class WorkRegisterViewForm {

	/**
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
	 * 選択用作業内容リスト
	 */
	private ArrayList<String> contentList;

	/**
	 * 備考
	 */
	private String note;

	/**
	 * 選択用備考リスト
	 */
	private ArrayList<String> noteList;

	/**
	 * 作業中フラグ
	 */
	private boolean workingFlg;

	/**
	 * 作業中フラグの文字列(true/false)
	 */
	private String workingFlgStr;

	/**
	 * 作業開始時間に初期表示用
	 */
	private String nowTime;

	/**
	 * 仕掛り作業
	 */
	private Work work;

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
	 * @return the contentList
	 */
	public ArrayList<String> getContentList() {
		return contentList;
	}

	/**
	 * @param contentList
	 *            the contentList to set
	 */
	public void setContentList(ArrayList<String> contentList) {
		this.contentList = contentList;
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
	 * @return the workingFlg
	 */
	public boolean isWorkingFlg() {
		return workingFlg;
	}

	/**
	 * @param workingFlg
	 *            the workingFlg to set
	 */
	public void setWorkingFlg(boolean workingFlg) {
		this.workingFlg = workingFlg;
	}

	/**
	 * @return the workingFlgStr
	 */
	public String getWorkingFlgStr() {
		return workingFlgStr;
	}

	/**
	 * @param workingFlgStr
	 *            the workingFlgStr to set
	 */
	public void setWorkingFlgStr(String workingFlgStr) {
		this.workingFlgStr = workingFlgStr;
	}

	/**
	 * @return the nowTime
	 */
	public String getNowTime() {
		return nowTime;
	}

	/**
	 * @param nowTime
	 *            the nowTime to set
	 */
	public void setNowTime(String nowTime) {
		this.nowTime = nowTime;
	}

	/**
	 * @return the work
	 */
	public Work getWork() {
		return work;
	}

	/**
	 * @param work
	 *            the work to set
	 */
	public void setWork(Work work) {
		this.work = work;
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
}
