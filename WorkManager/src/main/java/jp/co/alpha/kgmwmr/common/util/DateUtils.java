package jp.co.alpha.kgmwmr.common.util;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * DateUtils
 * @author kigami
 *
 */
/**
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
	 * プライベートコンストラクタ
	 */
	private DateUtils() {
	}

	/**
	 * 今日の日付取得
	 * 
	 * @return 今日の日付文字列
	 */
	public static String getTodayStr() {
		return LocalDate.now().format(DATEFORMATTER);
	}

	/**
	 * 現在時間（文字列）取得
	 * 
	 * @return 現在時間
	 */
	public static String getNowTimeStr() {
		return LocalTime.now().truncatedTo(ChronoUnit.SECONDS)
				.format(TIMEFORMATTER);
	}

	/**
	 * 現在時間取得
	 * 
	 * @return 現在時間
	 */
	public static LocalTime getNowTime() {
		String now = LocalTime.now().format(TIMEFORMATTER);
		return LocalTime.parse(now).truncatedTo(ChronoUnit.SECONDS);
	}

	/**
	 * 秒切り捨て時間の取得
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
	 *            フォーマット対象
	 * @return yyyy/M/d形式の日付
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

}
