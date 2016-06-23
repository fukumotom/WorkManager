package jp.kigami.ojt.model;

import java.io.Serializable;

/**
 * ユーザ登録処理のmodelクラス
 * 
 * @author kigami
 *
 */
public class User implements Serializable {

	/**
	 * シリアルバージョン
	 */
	private static final long serialVersionUID = -2761102579685994856L;

	/**
	 * ユーザ名
	 */
	private String userName;

	/**
	 * パスワード
	 */
	private String password;

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

}
