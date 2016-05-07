package test.test.ojt.common.util;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class DateUtils {

	public static final DateTimeFormatter dateFormatter = DateTimeFormatter
			.ofPattern("yyyy/M/d");

	public static final DateTimeFormatter timeFormatter = DateTimeFormatter
			.ofPattern("HH:mm");

	// private static final Logger logger = LoggerFactory
	// .getLogger(DateUtils.class);

	public static String getTodayStr() {
		return LocalDate.now().format(dateFormatter);
	}

	public static LocalTime getNowTime() {
		String now = LocalTime.now().format(timeFormatter);
		return LocalTime.parse(now).truncatedTo(ChronoUnit.SECONDS);
	}

	public static LocalTime getParseTime(LocalTime time) {

		String timeStr = time.format(timeFormatter);
		return LocalTime.parse(timeStr).truncatedTo(ChronoUnit.SECONDS);
	}

	public static LocalTime getFomatTime(String startTime) {

		return LocalTime.parse(startTime, timeFormatter);
	}

}
