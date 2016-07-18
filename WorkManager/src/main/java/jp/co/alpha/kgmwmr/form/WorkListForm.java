/**
 * 
 */
package jp.co.alpha.kgmwmr.form;

/**
 * 作業リスト処理用フォーム
 * 
 * @author kigami
 *
 */
public class WorkListForm {

	/**
	 * 作業ID
	 */
	private String id;

	/**
	 * ログインユーザ名
	 */
	private String userName;

	/**
	 * 作業日付
	 */
	private String workDate;

	/**
	 * 履歴削除含check
	 */
	private String deleteCechk;

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

	/**
	 * @return the deleteCechk
	 */
	public String getDeleteCechk() {
		return deleteCechk;
	}

	/**
	 * @param deleteCechk the deleteCechk to set
	 */
	public void setDeleteCechk(String deleteCechk) {
		this.deleteCechk = deleteCechk;
	}

}
