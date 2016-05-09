package test.test.ojt.common.util;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import test.test.ojt.common.exception.BindFormatException;

public class ConvertToModelUtils {

	private ConvertToModelUtils() {
	}

	public static String convertStr(Object target) {

		if (target instanceof String) {
			return (String) target;
		} else {
			return null;
		}
	}

	public static Integer convertInt(String target) {

		if (target != null) {
			return Integer.valueOf(target);
		} else {
			return null;
		}

	}

	public static boolean convertBoolean(String target)
			throws BindFormatException {
		return "true".equals(target);

	}

	public static LocalDate convertLocalDate(String target, String targetName)
			throws BindFormatException {

		if (target == "") {
			return null;
		}

		// format
		DateTimeFormatter dateFormatter = DateTimeFormatter
				.ofPattern("yyyy/M/d");

		LocalDate result;
		try {
			result = LocalDate.parse(target, dateFormatter);
		} catch (DateTimeParseException e) {
			throw new BindFormatException(e, targetName, target);
		}
		return result;
	}

	public static LocalTime convertLocalTime(String target, String targetName)
			throws BindFormatException {

		if (target == "") {
			return null;
		}

		// format
		DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:dd");

		LocalTime result;
		try {
			result = LocalTime.parse(target, timeFormatter);
		} catch (DateTimeParseException e) {
			throw new BindFormatException(e, targetName, target);
		}
		return result;
	}
}
