package jp.co.alpha.kgmwmr.logic;

import java.util.List;

import jp.co.alpha.kgmwmr.common.exception.IllegalOperationException;
import jp.co.alpha.kgmwmr.common.util.InputValidation;
import jp.co.alpha.kgmwmr.common.util.MsgCodeDef;
import jp.co.alpha.kgmwmr.common.util.ValidationResult;
import jp.co.alpha.kgmwmr.dao.UserRegisterDao;
import jp.co.alpha.kgmwmr.dao.dto.UserDto;
import jp.co.alpha.kgmwmr.db.util.CommonDbUtil;
import jp.co.alpha.kgmwmr.form.UserForm;
import jp.co.alpha.kgmwmr.model.User;

/**
 * ユーザ新規登録Logicクラス
 * 
 * @author kigami
 *
 */
public class UserRegistLogic {

	/**
	 * ユーザ情報登録
	 * 
	 * @param userForm
	 *            ユーザ情報
	 */
	public void register(UserForm userForm) {

		// 更新前セッションチェック
		if (userForm == null) {
			// セッションから情報が取得できなかった場合、不正操作
			throw new IllegalOperationException(MsgCodeDef.BAD_OPERATION);
		}

		// formをmodelに詰め替え
		User user = new User();
		user.setUserName(userForm.getUserName());
		user.setPassword(userForm.getPassword());

		// トランザクション境界
		try {
			// トランザクション管理設定
			CommonDbUtil.openConnection(false);
			// DB更新
			UserRegisterDao dao = new UserRegisterDao();
			dao.insertUsers(user);

			// コミット
			CommonDbUtil.commit();

		} finally {
			// 処理完了後、コネクションMapからコネクションを削除
			CommonDbUtil.closeConnection();
		}
	}

	/**
	 * ユーザ登録入力チェック
	 * 
	 * @param userForm
	 *            チェック対象のユーザ情報
	 * @return 表示用ユーザ情報
	 */
	public UserForm inputvalidation(UserForm userForm) {

		// 入力チェック
		ValidationResult result = new ValidationResult();

		// 入力チェック
		boolean validationChek = true;

		// ユーザ名サイズチェック
		validationChek = InputValidation.inputSize(userForm.getUserName(), 5,
				20);
		if (!validationChek) {
			result.addErrorMsg(MsgCodeDef.SIZE_ERROR, "ユーザ名", "5", "20");
			result.setCheckResult(false);
		}
		// ユーザ重複チェック
		validationChek = multiUserCheck(userForm.getUserName());
		if (!validationChek) {
			result.addErrorMsg(MsgCodeDef.EXIT_USER);
			result.setCheckResult(false);
		}

		// パスワードサイズチェック
		validationChek = InputValidation.inputSize(userForm.getPassword(), 5,
				20);
		if (!validationChek) {
			result.addErrorMsg(MsgCodeDef.SIZE_ERROR, "パスワード", "5", "64");
			result.setCheckResult(false);
		}

		// パスワード同一チェック
		validationChek = InputValidation.confilm(userForm.getPassword(),
				userForm.getConfirmPassword());
		if (!validationChek) {
			result.addErrorMsg(MsgCodeDef.CONFIRM_ERROR);
			result.setCheckResult(false);
		}

		// 画面表示データ設定
		UserForm viewForm = new UserForm();
		viewForm.setUserName(userForm.getUserName());
		viewForm.setErrMsgs(result.getErrorMsgs());

		return viewForm;
	}

	/**
	 * ユーザ重複チェック
	 * 
	 * @param userName
	 *            ユーザ名
	 * @return チェック結果
	 */
	private boolean multiUserCheck(String userName) {

		User user = new User();
		user.setUserName(userName);
		boolean result = false;

		// トランザクション境界
		try {
			// 接続開始
			CommonDbUtil.openConnection();

			UserRegisterDao dao = new UserRegisterDao();
			// 入力ユーザの存在チェック
			List<UserDto> userDtoList = dao.findUser(user);
			result = (userDtoList.size() == 0);

		} finally {
			// 処理完了後、コネクションMapからコネクションを削除
			CommonDbUtil.closeConnection();
		}

		return result;
	}
}