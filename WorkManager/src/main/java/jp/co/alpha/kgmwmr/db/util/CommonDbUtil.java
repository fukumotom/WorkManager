package jp.co.alpha.kgmwmr.db.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jp.co.alpha.kgmwmr.common.exception.SystemException;
import jp.co.alpha.kgmwmr.common.util.ConstantDef;
import jp.co.alpha.kgmwmr.common.util.PropertyUtils;
import jp.co.alpha.kgmwmr.common.util.ThreadIdentifier;

/**
 * DB汎用ユーティリティ
 * 
 * @author kigami
 *
 */
public class CommonDbUtil {

	/**
	 * ロガー
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(CommonDbUtil.class);

	/**
	 * クラスローダー
	 */
	private static ClassLoader classLoader = CommonDbUtil.class
			.getClassLoader();

	/**
	 * コネクションマップ
	 */
	private static Map<String, Connection> connectionMap = new HashMap<>();

	/**
	 * プライベートコンストラクタ
	 */
	private CommonDbUtil() {
	}

	/**
	 * JNDIからコネクションを取得し、コネクションmapにスレッドと紐づけて格納する<br>
	 * オートコミット：OFF
	 * 
	 * @param isAutoCommit
	 *            自動コミット判定
	 */
	public static void openConnection(boolean isAutoCommit) {

		String connectionId = getThreadId();

		DataSource ds = null;
		Connection con = null;
		try {
			Context context = new InitialContext();

			// JNDI経由でコネクションを取得
			ds = (DataSource) context
					.lookup(PropertyUtils.getValue("db.look.up.name"));
			con = ds.getConnection();
			con.setAutoCommit(isAutoCommit);
			connectionMap.put(connectionId, con);
		} catch (NamingException | SQLException e) {
			logger.error("JNDI接続エラー:{}", e);
			throw new SystemException(e);
		}
	}

	/**
	 * JNDIからコネクションを取得し、コネクションmapにスレッドと紐づけて格納する<br>
	 * オートコミット：ON
	 * 
	 */
	public static void openConnection() {
		openConnection(true);
	}

	/**
	 * コネクションに紐づくスレッドIDを生成
	 * 
	 * @return スレッドID
	 */
	private static String getThreadId() {

		return String.valueOf(ThreadIdentifier.getThreadId()) + ":";
	}

	/**
	 * コミット
	 */
	public static void commit() {
		try {
			connectionMap.get(getThreadId()).commit();
		} catch (SQLException e) {
			throw new SystemException(e);
		}
	}

	/**
	 * コネクションをコネクションMapから削除してクローズする
	 */
	public static void closeConnection() {

		// コネクションマップからスレッドIDの開放
		Connection con = connectionMap.remove(getThreadId());

		// メモリリーク防止のためのスレッド破棄
		ThreadIdentifier.remove();

		if (con != null) {
			try {
				con.close();
			} catch (SQLException e) {
				logger.warn("DBコネクションクローズ時に失敗:{}", e);
			}
		}
	}

	/**
	 * SQLファイル読み込み
	 * 
	 * @param sqlName
	 *            SQLファイル名
	 * @return 読みこんだSQL文
	 */
	public static StringBuilder readSql(String sqlName) {

		// sqlファイル読みこみ
		InputStream iStream = classLoader
				.getResourceAsStream("/sql/" + sqlName);

		StringBuilder builder = new StringBuilder();
		try (InputStreamReader reader = new InputStreamReader(iStream);
				BufferedReader bufReader = new BufferedReader(reader);) {

			while (true) {
				String line = bufReader.readLine();
				if (line == null) {
					break;
				}
				builder.append(line);
				builder.append("\n");
			}

		} catch (IOException e) {
			logger.error("SQLファイル読み込み失敗", e);
		}
		logger.info("読み込みSQL：{}", builder.toString());
		return builder;
	}

	/**
	 * JNDIによりデータソースを取得
	 * 
	 * @return データソース
	 */
	public static DataSource lookup() {

		DataSource ds = null;
		try {
			Context context = new InitialContext();
			ds = (DataSource) context
					.lookup(PropertyUtils.getValue(ConstantDef.DB_LOOK_UP));
		} catch (NamingException e) {
			logger.error("JNDI接続エラー:{}", e);
			throw new SystemException(e);
		}
		return ds;
	}

	/**
	 * SQL発行パラメータ作成
	 * 
	 * @param sql
	 *            SQL文
	 * @param dtoClass
	 *            パラメータ作成用DTOクラス
	 * @return PreparedStatementにバインドするパラメータMap
	 */
	public static <T> HashMap<Integer, Object> createParamMap(StringBuilder sql,
			T dtoClass) {

		HashMap<String, Object> dtoMap = CommonDbUtil
				.createBeanValueMap(dtoClass);

		Map<Integer, String> sqlParamMap = CommonDbUtil.createSqlMap(sql);

		HashMap<Integer, Object> paramMap = new HashMap<>();

		for (Entry<String, Object> dtoEntry : dtoMap.entrySet()) {

			for (Entry<Integer, String> sqlEntry : sqlParamMap.entrySet()) {
				if ((dtoEntry.getKey()).equals(sqlEntry.getValue())) {
					paramMap.put(sqlEntry.getKey(), dtoEntry.getValue());
				}
			}
		}
		return paramMap;
	}

	/**
	 * sql文からパラメータ用Map作成<br>
	 * SQL文のバインド変数をMapに変換
	 * 
	 * @param sql
	 *            SQL文
	 * @return SQLMap
	 */
	public static Map<Integer, String> createSqlMap(StringBuilder sql) {
		// sql文の動的パラメータとパラメータの順番のMapを作成
		String regex = "\\$\\{([a-zA-Z\\d]*)\\}";
		Pattern ptm = Pattern.compile(regex);

		// SQL文からパラメータ代入箇所を取得
		HashMap<Integer, String> sqlParamMap = new HashMap<>();
		Matcher mat = ptm.matcher(sql);
		int index = 0;
		while (mat.find()) {
			index++;
			String sqlParam = mat.group(1);

			sqlParamMap.put(index, sqlParam);
		}
		String convertQuery = mat.replaceAll("?");
		// クエリに置換
		sql.replace(0, sql.length(), convertQuery);

		for (Entry<Integer, String> entry : sqlParamMap.entrySet()) {
			logger.debug("sqlMap内容[{}]:{}", entry.getKey(), entry.getValue());
		}
		return sqlParamMap;
	}

	/**
	 * dtoに変換したパラメータをSQL文に補完する。
	 * 
	 * @param pstm
	 *            クエリーSQL文
	 * @param paramMap
	 *            バインドパラメータ
	 * @throws SQLException
	 *             SQL例外
	 */
	public static void bindParam(PreparedStatement pstm,
			Map<Integer, Object> paramMap) throws SQLException {

		for (Entry<Integer, Object> entry : paramMap.entrySet()) {

			Object value = entry.getValue();

			if (value instanceof String) {
				pstm.setString(entry.getKey(), (String) entry.getValue());
			} else if (value instanceof Integer) {
				pstm.setInt(entry.getKey(),
						((Integer) entry.getValue()).intValue());
			} else if (value instanceof Time) {
				pstm.setTime(entry.getKey(), (Time) entry.getValue());
			} else if (value instanceof Date) {
				pstm.setDate(entry.getKey(), (Date) entry.getValue());
			} else if (value == null) {
				pstm.setObject(entry.getKey(), null);
			} else {
				// 想定外の型は一律String型に置換
				pstm.setString(entry.getKey(), entry.getValue().toString());
			}
		}
	}

	/**
	 * SQL実行結果をDtoに詰め替える
	 * 
	 * @param result
	 *            SQL発行結果
	 * @param dtoClass
	 *            SQL発行結果を変換するDTOクラス
	 * @return DTOリスト
	 * @throws SQLException
	 *             SQL例外
	 */
	public static <T> List<T> resultSetToWorkDtoList(ResultSet result,
			Class<T> dtoClass) throws SQLException {

		HashMap<String, String> clmNameMap = new HashMap<String, String>();
		ResultSetMetaData meta = result.getMetaData();
		for (int i = 1; i < meta.getColumnCount() + 1; i++) {

			meta.getColumnType(i);
			clmNameMap.put(meta.getColumnLabel(i), meta.getColumnClassName(i));
		}

		ArrayList<T> dtoList = new ArrayList<>();

		while (result.next()) {
			T dto;
			try {
				dto = dtoClass.newInstance();
			} catch (InstantiationException | IllegalAccessException e1) {
				logger.error("DTOのリフレクションに失敗");
				throw new SystemException(e1);
			}
			for (Entry<String, String> entry : clmNameMap.entrySet()) {
				String label = entry.getKey();
				String typeStr = entry.getValue();
				logger.info("clmNameMap内容[ラベル]:型     [{}]:{}", label, typeStr);

				// ラベルからDB結果を取得
				Object obj;
				if (typeStr.contains("Integer")) {
					obj = result.getInt(label);
				} else if (typeStr.contains("Time")) {
					obj = result.getTime(label);
				} else {
					obj = result.getString(label);
				}

				// setterを取得
				Method setter = createSetter(label, dtoClass);

				try {
					setter.invoke(dto, obj);

				} catch (IllegalAccessException | IllegalArgumentException
						| InvocationTargetException e) {
					logger.error("DBのバインドに失敗");
					throw new SystemException(e);
				}
			}
			dtoList.add(dto);
		}
		return dtoList;
	}

	/**
	 * DB結果取得用のsetterをラベル文字列から作成
	 * 
	 * @param label
	 *            ラベル
	 * @param dtoClass
	 *            DTOクラス
	 * @return DTO
	 */
	private static <T> Method createSetter(String label, Class<T> dtoClass) {

		// ラベルからsetter文字列を作成
		String[] pieces = label.split("_");
		StringBuilder setterStr = new StringBuilder();

		for (int i = 0; i < pieces.length; i++) {
			if (i == 0) {
				setterStr.append("set");
			}
			setterStr.append(pieces[i].substring(0, 1).toUpperCase());
			setterStr.append(pieces[i].substring(1));
		}

		Method[] methods = dtoClass.getDeclaredMethods();
		Method setter = null;
		for (Method tmpSetter : methods) {
			if (tmpSetter.getName().equals(setterStr.toString())) {
				setter = tmpSetter;
				break;
			}
		}
		if (setter == null) {
			logger.error("{}内に{}のsetterが見つかりませんでした。", dtoClass.getName(),
					label);
			throw new SystemException("バインドに失敗しました。");
		}
		return setter;
	}

	/**
	 * ORマッパー用<br>
	 * ビーンからフィールド名とフィールドの値をmapで取得
	 * 
	 * @param dto
	 *            DTO
	 * @return DTOの項目と値のMap
	 */
	public static <T> HashMap<String, Object> createBeanValueMap(T dto) {

		// Dtoのフィールド名と値のMapを作成
		String regex = "get(([A-Z][a-zA-Z\\d]*))";
		Pattern ptm = Pattern.compile(regex);

		// Dtoのgetterからフィールド名を取得
		Method[] methods = dto.getClass().getDeclaredMethods();
		HashMap<String, Object> dtoMap = new HashMap<>();

		for (Method method : methods) {
			// getterを抽出
			Matcher mat = ptm.matcher(method.getName());
			if (mat.find()) {
				String fieldName = mat.group(1);
				fieldName = fieldName.substring(0, 1).toLowerCase()
						+ fieldName.substring(1);

				Object value = null;
				try {
					value = method.invoke(dto);
				} catch (IllegalAccessException | IllegalArgumentException
						| InvocationTargetException e) {
					logger.info("リフレクション失敗", e);
				}
				dtoMap.put(fieldName, value);
			}
		}

		for (Entry<String, Object> entry : dtoMap.entrySet()) {
			logger.info("DtoMap内容[{}]:{}", entry.getKey(), entry.getValue());
		}
		return dtoMap;
	}

	/**
	 * 件数取得
	 * 
	 * @param sql
	 *            発行SQL
	 * @param paramMap
	 *            バインドパラメータ
	 * @return SQL結果件数
	 */
	public static int getDbResultCnt(String sql,
			Map<Integer, Object> paramMap) {

		int resultCnt = 0;
		Connection con = connectionMap.get(getThreadId());
		try (PreparedStatement pstm = con.prepareStatement(sql);) {

			bindParam(pstm, paramMap);
			resultCnt = pstm.executeUpdate();

		} catch (SQLException e) {
			logger.error("DB接続失敗", e);
			throw new SystemException(e);
		}

		return resultCnt;
	}

	/**
	 * 検索結果を取得
	 * 
	 * @param sql
	 *            SQL文
	 * @param paramMap
	 *            バインドパラメータ
	 * @param dtoClass
	 *            結果作成用DTOクラス
	 * @return DTOリスト
	 */
	public static <T> List<T> getDtoList(String sql,
			Map<Integer, Object> paramMap, Class<T> dtoClass) {

		List<T> dtoList = new ArrayList<>();
		Connection con = connectionMap.get(getThreadId());
		try (PreparedStatement pstm = con.prepareStatement(sql);) {

			logger.info("発行SQL:{}", sql);

			// parameter join
			bindParam(pstm, paramMap);
			ResultSet result = pstm.executeQuery();

			// マッピング
			dtoList = resultSetToWorkDtoList(result, dtoClass);

		} catch (SQLException e) {
			logger.error("DB接続失敗", e);
			throw new SystemException(e);
		}

		return dtoList;
	}

	/**
	 * １件取得
	 * 
	 * @param sql
	 *            SQL文
	 * @param paramMap
	 *            バインドパラメータ
	 * @param dtoClass
	 *            結果作成用DTOクラス
	 * @return DTO
	 */
	public static <T> T findOne(String sql, HashMap<Integer, Object> paramMap,
			Class<T> dtoClass) {

		List<T> dtoList = getDtoList(sql, paramMap, dtoClass);
		if (dtoList.size() != 1) {
			throw new SystemException("複数件存在します");
		}

		return dtoList.get(0);
	}

	/**
	 * DB更新
	 * 
	 * @param sql
	 *            SQL文
	 * @param paramMap
	 *            バインドパラメータ
	 * @return 更新件数
	 */
	public static int updata(String sql, HashMap<Integer, Object> paramMap) {

		int resultCnt = 0;
		Connection con = connectionMap.get(getThreadId());
		try (PreparedStatement pstm = con.prepareStatement(sql);) {

			logger.info("発行SQL:{}", sql);

			bindParam(pstm, paramMap);

			resultCnt = pstm.executeUpdate();

		} catch (SQLException e) {
			throw new SystemException("DB更新失敗", e);
		}

		return resultCnt;
	}

	/**
	 * ビーンマッパー
	 * 
	 * @param before
	 *            変換前ビーンインスタンス
	 * @param after
	 *            変換後ビーンインスタンス
	 */
	public static <T, U> void beanMaping(U before, T after) {

		try {
			HashMap<String, Object> dtoMap = createBeanValueMap(before);
			for (Entry<String, Object> entry : dtoMap.entrySet()) {
				Object value = entry.getValue();
				// 値があるフィールドを詰め替え
				if (value != null) {

					// 型変換
					value = convertDate(value);

					// 返却するクラスのsetter取得
					StringBuilder setterStr = new StringBuilder();
					setterStr.append("set")
							.append(entry.getKey().substring(0, 1)
									.toUpperCase())
							.append(entry.getKey().substring(1));

					Method[] methods = after.getClass().getDeclaredMethods();
					Method setter = null;
					for (Method tmpSetter : methods) {
						if (tmpSetter.getName().equals(setterStr.toString())) {
							setter = tmpSetter;
							break;
						}
					}
					setter.invoke(after, value);
				}

			}

		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			logger.error("DBのバインドに失敗");
			throw new SystemException(e);
		}
	}

	/**
	 * SQL型とjava型の日付相互変換
	 * 
	 * @param value
	 *            変換対象
	 * @return 変換したオブジェクト
	 */
	private static Object convertDate(Object value) {

		// sqlとjavaの日付型変換
		if (value instanceof Date) {
			value = ((Date) value).toLocalDate();
		} else if (value instanceof Time) {
			value = ((Time) value).toLocalTime();
		} else if (value instanceof LocalDate) {
			value = Date.valueOf((LocalDate) value);
		} else if (value instanceof LocalTime) {
			value = Time.valueOf((LocalTime) value);
		}
		return value;
	}

	/**
	 * 編集時の今日のデータを未保存状態で複製
	 * 
	 * @param sql
	 *            SQL文
	 * @param paramMap
	 *            バインドパラメータ
	 */
	public static void copyWork(String sql, HashMap<Integer, Object> paramMap) {

		DataSource ds = lookup();
		try (Connection con = ds.getConnection();
				PreparedStatement pstm = con.prepareStatement(sql)) {

			logger.info("発行SQL：{}", sql);

			bindParam(pstm, paramMap);

			pstm.execute();

		} catch (SQLException e) {
			logger.error("複製処理失敗", e);
			throw new SystemException(e);
		}
	}
}