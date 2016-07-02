package jp.co.alpha.kgmwmr.common.util;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
<<<<<<< 4d34a3ce0c44d12073067ca79ff47abda3ebb911
 * DateUtils
 * @author kigami
 *
 */
/**
 * ======= 日時操作ユーティリティ
 * 
 * >>>>>>> add javadoc and format method in dateutils #103
 * 
 * @author kigami
 *
 */
public class DateUtils {

	/**
	 * 日付フォーマット
	 */
	public static final DateTimeFormatter DATEFORMATTER = DateTimeFormatter
			.ofPattern("yyyy/M/d");

	/**
	 * 時間フォーマット
	 */
	public static final DateTimeFormatter TIMEFORMATTER = DateTimeFormatter
			.ofPattern("HH:mm");

	/**
	 * CSVファイル名用日付フォーマット
	 */
	private static final DateTimeFormatter csvFileNameFormatter = DateTimeFormatter
			.ofPattern("yyyyMMdd");

	/**
	 * プライベートコンストラクタ
	 */
	private DateUtils() {
	}

	/**
	 * 現在日付を取得
	 * 
	 * @return 現在の日付文字列
	 */
	public static String getTodayStr() {
		return LocalDate.now().format(DATEFORMATTER);
	}

	/**
	 * <<<<<<< 4d34a3ce0c44d12073067ca79ff47abda3ebb911 現在時間（文字列）取得
	 * 
	 * @return 現在時間 ======= 現在時間を文字列で取得
	 * 
	 * @return >>>>>>> add javadoc and format method in dateutils #103
	 */
	public static String getNowTimeStr() {
		return LocalTime.now().truncatedTo(ChronoUnit.SECONDS)
				.format(TIMEFORMATTER);
	}

	/**
	 * <<<<<<< 4d34a3ce0c44d12073067ca79ff47abda3ebb911 現在時間取得
	 * 
	 * @return 現在時間 ======= 現在時間を秒切り捨てで取得
	 * 
	 * @return >>>>>>> add javadoc and format method in dateutils #103
	 */
	public static LocalTime getNowTime() {
		String now = LocalTime.now().format(TIMEFORMATTER);
		return LocalTime.parse(now).truncatedTo(ChronoUnit.SECONDS);
	}

	/**
	 * 時間を秒切り捨てで取得
	 * 
	 * @param time
	 *            時間
	 * @return HH:ｍｍ形式の時間
	 */
	public static LocalTime truncatedTime(LocalTime time) {

		String timeStr = time.format(TIMEFORMATTER);
		return LocalTime.parse(timeStr).truncatedTo(ChronoUnit.SECONDS);
	}

	/**
	 * 文字列をLocalTime型に変換
	 * 
	 * @param time
	 *            時間文字列
	 * @return HH:mm 時間
	 */
	public static LocalTime getParseTime(String time) {

		return LocalTime.parse(time, TIMEFORMATTER);
	}

	/**
	 * 日付フォーマット
	 * 
	 * @param localDate
	 *            <<<<<<< 4d34a3ce0c44d12073067ca79ff47abda3ebb911 フォーマット対象
	 * @return yyyy/M/d形式の日付 =======
	 * @return >>>>>>> add javadoc and format method in dateutils #103
	 */
	public static String formatDate(LocalDate localDate) {

		return localDate.format(DATEFORMATTER);
	}

	/**
	 * 時間フォーマット
	 * 
	 * @param localTime
	 *            フォーマット対象
	 * @return HH:mm形式の時間
	 */
	public static String formatTime(LocalTime localTime) {

		return localTime.format(TIMEFORMATTER);
	}

	/**
	 * 文字列をLocalDate型に変換
	 * 
	 * @param date
	 *            日付文字列
	 * @return yyyy/M/d形式の日付
	 */
	public static LocalDate getParseDate(String date) {
		return LocalDate.parse(date, DATEFORMATTER);
	}

	/**
	 * csvファイル名用時間フォーマット
	 * 
	 * @param time
	 * @return
	 */
	public static String csvFormatTime(LocalTime time) {

		return time.format(csvFileNameFormatter);
	}

}
