package jp.co.alpha.kgmwmr.dao.dto;

import java.io.Serializable;

/**
 * ユーザ登録処理のDTOクラス
 * 
 * @author kigami
 *
 */
public class UserDto implements Serializable {

	/**
	 * シリアルバージョン
	 */
	private static final long serialVersionUID = -1611859676683377943L;

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
