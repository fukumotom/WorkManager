package jp.kigami.ojt.logic;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jp.kigami.ojt.common.util.InputValidation;
import jp.kigami.ojt.common.util.MsgCodeDef;
import jp.kigami.ojt.common.util.PropertyUtils;
import jp.kigami.ojt.common.util.ValidationResult;
import jp.kigami.ojt.dao.UserRegisterDao;
import jp.kigami.ojt.dao.dto.UserDto;
import jp.kigami.ojt.form.UserForm;
import jp.kigami.ojt.model.User;

public class UserRegistLogic {

	private static final Logger logger = LoggerFactory
			.getLogger(UserRegistLogic.class);

	/**
	 * ユーザ情報登録
	 * 
	 * @param userForm
	 */
	public void register(UserForm userForm) {

		// TODO DB更新前のチェック
		inputvalidation(userForm);

		User user = new User();
		user.setUserName(userForm.getUserName());
		user.setPassword(userForm.getPassword());

		// DB更新
		UserRegisterDao dao = new UserRegisterDao();
		dao.insertUsers(user);
	}

	/**
	 * ユーザ登録入力チェック
	 * 
	 * @param userForm
	 * @return
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
			result.addErrorMsg(PropertyUtils.getValue(MsgCodeDef.SIZE_ERROR,
					"ユーザ名", "5", "20"));
			result.setCheckResult(false);
		}
		// ユーザ重複チェック
		validationChek = multiUserCheck(userForm.getUserName());
		if (!validationChek) {
			result.addErrorMsg(PropertyUtils.getValue(MsgCodeDef.EXIT_USER));
			result.setCheckResult(false);
		}

		// パスワードサイズチェック
		validationChek = InputValidation.inputSize(userForm.getPassword(), 5,
				20);
		if (!validationChek) {
			result.addErrorMsg(PropertyUtils.getValue(MsgCodeDef.SIZE_ERROR,
					"パスワード", "5", "64"));
			result.setCheckResult(false);
		}

		// パスワード同一チェック
		validationChek = InputValidation.confilm(userForm.getPassword(),
				userForm.getConfirmPassword());
		if (!validationChek) {
			result.addErrorMsg(
					PropertyUtils.getValue(MsgCodeDef.CONFIRM_ERROR));
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
	 * @return
	 */
	private boolean multiUserCheck(String userName) {

		User user = new User();
		user.setUserName(userName);

		UserRegisterDao dao = new UserRegisterDao();
		// 入力ユーザの存在チェック
		List<UserDto> userDtoList = dao.findUser(user);

		return userDtoList.size() == 0;
	}
}
