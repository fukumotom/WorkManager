package jp.co.alpha.kgmwmr.form;

/**
 * ユーザ登録処理のFormクラス
 * 
 * @author kigami
 *
 */
public class UserForm {

	/**
	 * ユーザ名
	 */
	private String userName;

	/**
	 * パスワード
	 */
	private String password;

	/**
	 * 確認用パスワード
	 */
	private String confirmPassword;

	/**
	 * エラーメッセージ
	 */
	private String errMsgs;

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
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the confirmPassword
	 */
	public String getConfirmPassword() {
		return confirmPassword;
	}

	/**
	 * @param confirmPassword
	 *            the confirmPassword to set
	 */
	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
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
