package jp.kigami.ojt.common.util;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class DateUtils {

	public static final DateTimeFormatter dateFormatter = DateTimeFormatter
			.ofPattern("yyyy/M/d");

	public static final DateTimeFormatter timeFormatter = DateTimeFormatter
			.ofPattern("HH:mm");

	private DateUtils() {
	}

	public static String getTodayStr() {
		return LocalDate.now().format(dateFormatter);
	}

	public static String getNowTimeStr() {
		return LocalTime.now().truncatedTo(ChronoUnit.SECONDS)
				.format(timeFormatter);
	}

	public static LocalTime getNowTime() {
		String now = LocalTime.now().format(timeFormatter);
		return LocalTime.parse(now).truncatedTo(ChronoUnit.SECONDS);
	}

	public static LocalTime getParseTime(LocalTime time) {

		String timeStr = time.format(timeFormatter);
		return LocalTime.parse(timeStr).truncatedTo(ChronoUnit.SECONDS);
	}

	public static LocalTime getFomatTime(String time) {

		return LocalTime.parse(time, timeFormatter);
	}

	public static String formatDate(LocalDate localDate) {

		return localDate.format(dateFormatter);
	}

}
