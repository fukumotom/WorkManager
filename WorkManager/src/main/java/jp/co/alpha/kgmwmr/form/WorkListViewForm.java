/**
 * 
 */
package jp.co.alpha.kgmwmr.form;

import java.util.List;

import jp.co.alpha.kgmwmr.model.Work;

/**
 * 作業リスト表示用フォーム
 * 
 * @author kigami
 *
 */
public class WorkListViewForm {

	/**
	 * 削除済みフラグ
	 */
	private String deletedFlg;

	/**
	 * 作業リスト
	 */
	private List<Work> workList;

	/**
	 * エラーメッセージ
	 */
	private String errMsgs;

	/**
	 * 表示作業の日付
	 */
	private String listDate;

	/**
	 * @return the deletedFlg
	 */
	public String getDeletedFlg() {
		return deletedFlg;
	}

	/**
	 * @param deletedFlg
	 *            the deletedFlg to set
	 */
	public void setDeletedFlg(String deletedFlg) {
		this.deletedFlg = deletedFlg;
	}

	/**
	 * @return the workList
	 */
	public List<Work> getWorkList() {
		return workList;
	}

	/**
	 * @param workList
	 *            the workList to set
	 */
	public void setWorkList(List<Work> workList) {
		this.workList = workList;
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
	 * @return the listDate
	 */
	public String getListDate() {
		return listDate;
	}

	/**
	 * @param listDate
	 *            the listDate to set
	 */
	public void setListDate(String listDate) {
		this.listDate = listDate;
	}

}
