package jp.kigami.ojt.form;

import java.time.LocalTime;
import java.util.ArrayList;

import jp.kigami.ojt.model.Work;

/**
 * 作業登録画面のフォーム
 * 
 * @author btkigamim
 *
 */
public class WorkRegisterForm {

	/**
	 * 開始時間
	 */
	private LocalTime startTime;

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
	 * 作業状況
	 */
	private String workingStates;

	/**
	 * 仕掛り作業
	 */
	private Work work;

	/**
	 * 作業開始時間に初期表示用
	 */
	private String nowTime;

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
	 * @return the workingStates
	 */
	public String getWorkingStates() {
		return workingStates;
	}

	/**
	 * @param workingStates
	 *            the workingStates to set
	 */
	public void setWorkingStates(String workingStates) {
		this.workingStates = workingStates;
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
}
