package jp.co.alpha.kgmwmr.dao;

import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jp.co.alpha.kgmwmr.common.util.MsgCodeDef;
import jp.co.alpha.kgmwmr.common.util.PropertyUtils;
import jp.co.alpha.kgmwmr.dao.dto.UserDto;
import jp.co.alpha.kgmwmr.db.util.CommonDbUtil;
import jp.co.alpha.kgmwmr.model.User;

/**
 * ユーザ登録処理DAOクラス
 * 
 * @author kigami
 *
 */
public class UserRegisterDao {

	/**
	 * ロガー
	 */
	private static Logger logger = LoggerFactory
			.getLogger(UserRegisterDao.class);

	/**
	 * ユーザ登録SQL発行
	 * 
	 * @param user
	 *            登録するユーザ情報
	 */
	public void insertUsers(User user) {

		// load SQLsentence
		StringBuilder sql1 = CommonDbUtil.readSql("registUser.sql");
		StringBuilder sql2 = CommonDbUtil.readSql("registRole.sql");

		// dtoに詰め替え
		UserDto dto = new UserDto();
		CommonDbUtil.beanMaping(user, dto);

		// create sql parameter
		HashMap<Integer, Object> paramMap = CommonDbUtil.createParamMap(sql1,
				dto);
		HashMap<Integer, Object> paramMap2 = CommonDbUtil.createParamMap(sql2,
				dto);

		// DB更新
		int cnt = CommonDbUtil.updata(sql1.toString(), paramMap);
		logger.info(PropertyUtils.getValue(MsgCodeDef.DB_REGISTER), cnt);
		int cnt2 = CommonDbUtil.updata(sql2.toString(), paramMap2);
		logger.info(PropertyUtils.getValue(MsgCodeDef.DB_REGISTER), cnt2);
	}

	/**
	 * ユーザ検索SQL発行 TODO トランザクション
	 * 
	 * @param user
	 *            検索対象のユーザ情報
	 * @return ユーザ情報リスト
	 */
	public List<UserDto> findUser(User user) {

		// sql読み込み
		StringBuilder sql = CommonDbUtil.readSql("findUser.sql");

		// dtoにmodelを詰め替え
		UserDto userDto = new UserDto();
		CommonDbUtil.beanMaping(user, userDto);

		// SQL発行パラメータ取得
		HashMap<Integer, Object> paramMap = CommonDbUtil.createParamMap(sql,
				userDto);

		// SQL発行
		List<UserDto> dtoList = CommonDbUtil.getDtoList(sql.toString(),
				paramMap, UserDto.class);

		return dtoList;
	}
}