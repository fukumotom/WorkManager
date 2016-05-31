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

	public LocalTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalTime startTime) {
		this.startTime = startTime;
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	public ArrayList<String> getContentList() {
		return contentList;
	}

	public void setContentList(ArrayList<String> contentList) {
		this.contentList = contentList;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public ArrayList<String> getNoteList() {
		return noteList;
	}

	public void setNoteList(ArrayList<String> noteList) {
		this.noteList = noteList;
	}

	public String getWorkingStates() {
		return workingStates;
	}

	public void setWorkingStates(String workingStates) {
		this.workingStates = workingStates;
	}

	public Work getWork() {
		return work;
	}

	public void setWork(Work work) {
		this.work = work;
	}

	public String getNowTime() {
		return nowTime;
	}

	public void setNowTime(String nowTime) {
		this.nowTime = nowTime;
	}
}