package com.tools.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * 日期模块 --- 已弃用，请使用com.tools.util.DateUtil.java
 * 
 * yyyy-MM-dd HH:mm:ss yyyy-MM-dd HH:mm:ss.SSS
 * 
 * "yyyy-MM-dd", "yyyy-MM-dd HH:mm", "yyyy-MM-dd HH:mmZ",
 * "yyyy-MM-dd HH:mm:ss.SSSZ", "yyyy-MM-dd'T'HH:mm:ss.SSSZ",
 * 
 * @author lmc 版权声明：版权归作者所有，未经协议授权不得使用、转载、抄袭等，否则依法追究法律责任和经济责任。
 */

public class DatetimeUtil {

	private static final String TAG = DatetimeUtil.class.getSimpleName();

	/**
	 * 判断字符串是否为空，等于null或者长度不大于零都视为空字符串
	 * 
	 * @param src
	 * @return
	 */
	protected static boolean isEmptyString(String src) {
		if (src == null) {
			return true;
		}

		if (src.length() <= 0) {
			return true;
		}
		return false;
	}

	/**
	 * @param format
	 * @param locale
	 *            系统语言
	 * @param timeZone
	 *            时区
	 * @return
	 */
	public static String getCurrent(String format, Locale locale,
			TimeZone timeZone) {
		if (isEmptyString(format) || locale == null || timeZone == null) {
			return null;
		}
		// SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
		// Locale.CHINESE);
		// sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
		// String dateTime = sdf.format(new Date(System.currentTimeMillis()));
		// return dateTime;
		SimpleDateFormat sdf = new SimpleDateFormat(format, locale);
		sdf.setTimeZone(timeZone);
		String dateTime = sdf.format(new Date(getCurrentMilliseconds()));
		return dateTime;
	}

	public static String getCurrent(String format, TimeZone timeZone) {
		return getCurrent(format, Locale.getDefault(), timeZone);
	}

	public static String getCurrent(String format) {
		return getCurrent(format, Locale.getDefault(), TimeZone.getDefault());
	}

	public static String getCurrentDatetime() {
		return getCurrent("yyyy-MM-dd HH:mm:ss");
	}

	public static String getCurrentDate() {
		return getCurrent("yyyy-MM-dd");
	}

	public static String getCurrentTime() {
		return getCurrent("HH:mm:ss");
	}

	/**
	 * 得到当前时间的毫秒数量
	 * 
	 * @return
	 */
	public static long getCurrentMilliseconds() {
		// System.currentTimeMillis()
		// Calendar.getInstance().getTimeInMillis()
		return Calendar.getInstance().getTimeInMillis();
	}

	/**
	 * 日期格式和日期字符串转成日期对象 yyyy-MM-dd HH:mm:ss.SSS
	 * 
	 * @param format
	 * @param datetime
	 * @return
	 */
	public static java.util.Date parseFormatDate(String format, String datetime) {
		if (isEmptyString(format) || isEmptyString(datetime)) {
			return null;
		}

		java.util.Date date = null;
		java.text.SimpleDateFormat sdf = new SimpleDateFormat(format);
		try {
			date = sdf.parse(datetime);
			// TODO 给年代加上1900（不要加上1900，其它的方法使用后，数值会变大）
			// date.setYear(date.getYear()+1900);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return date;
	}

	public static java.util.Date parseDatetime(String datetime) {
		return parseFormatDate("yyyy-MM-dd HH:mm:ss", datetime);
	}

	public static java.util.Date parseDatetimeS(String datetime) {
		return parseFormatDate("yyyy-MM-dd HH:mm", datetime);
	}

	public static java.util.Date parseDate(String date) {
		return parseFormatDate("yyyy-MM-dd", date);
	}

	public static java.util.Date parseTime(String time) {
		return parseFormatDate("HH:mm:ss", time);
	}

	/**
	 * 将日期对象格式化为字符串
	 * 
	 * @param format
	 * @param date
	 * @return
	 */
	public static String toFormatString(String format, java.util.Date date) {
		if (isEmptyString(format) || date == null) {
			return null;
		}

		SimpleDateFormat sdf = new SimpleDateFormat(format);
		String dateTime = sdf.format(date);
		return dateTime;
	}

	public static String toDatetimeString(java.util.Date date) {
		return toFormatString("yyyy-MM-dd HH:mm:ss", date);
	}

	public static String toDatetimeStringS(String datetime) {
		return toFormatString("yyyy-MM-dd HH:mm", parseDatetime(datetime));
	}

	public static String toDateString(java.util.Date date) {
		return toFormatString("yyyy-MM-dd", date);
	}

	public static String toTimeString(java.util.Date date) {
		return toFormatString("HH:mm:ss", date);
	}

	/**
	 * 日期转换毫秒数
	 * 
	 * @param date
	 *            日期
	 * @return
	 */
	public static long Date2Milliseconds(java.util.Date date) {
		return date.getTime();
	}

	/**
	 * 毫秒数转换日期
	 * 
	 * @param milliseconds
	 *            毫秒数
	 * @return
	 */
	public static java.util.Date Milliseconds2Date(long milliseconds) {
		return new Date(milliseconds);
	}

	/**
	 * 日期转换毫秒数
	 * 
	 * @param dateStr
	 *            日期yyyy-MM-dd HH:mm:ss
	 * @return 毫秒数
	 */
	public static long String2Milliseconds(String dateStr) {
		Date date = parseDatetime(dateStr);
		return date.getTime();
	}

	/**
	 * 日期转换毫秒数
	 * 
	 * @param dateStr
	 *            日期yyyy-MM-dd HH:mm:ss
	 * @return 毫秒数
	 */
	public static long String2MillisecondsS(String dateStr) {
		if (isEmptyString(dateStr)) {
			return 0;
		}
		Date date = parseDatetime(dateStr + ":00");
		return date.getTime();
	}

	/**
	 * 毫秒数转换日期
	 * 
	 * @param milliseconds
	 *            毫秒数
	 * @return 日期yyyy-MM-dd HH:mm:ss
	 */
	public static String Milliseconds2String(long milliseconds) {
		Date date = new Date(milliseconds);
		return toDatetimeString(date);
	}

	/**
	 * 秒数转换成 mm:ss格式
	 * 
	 * @param seconds
	 *            秒数
	 * @return 时间 mm:ss
	 */
	public static String Seconds2MMString(long seconds) {
		long min = seconds / 60;
		long sec = seconds % 60;
		String minStr = min < 10 ? ("0" + min) : ("" + min);
		String secStr = sec < 10 ? ("0" + sec) : ("" + sec);
		return minStr + ":" + secStr;
	}

	/**
	 * 秒数转换成 hh:mm:ss格式
	 * 
	 * @param seconds
	 *            秒数
	 * @return 时间 hh:mm:ss
	 */
	public static String Seconds2String(long seconds) {
		long hour = seconds / 3600;
		long min = seconds / 60;
		long sec = seconds % 60;
		String hourStr = hour < 10 ? ("0" + hour) : ("" + hour);
		String minStr = min < 10 ? ("0" + min) : ("" + min);
		String secStr = sec < 10 ? ("0" + sec) : ("" + sec);
		return hourStr + ":" + minStr + ":" + secStr;
	}

	/**
	 * 判断时间是否过去时间
	 * 
	 * @param date
	 *            判断的时间
	 * @return 是否过去时间
	 */
	public static boolean isPassDate(java.util.Date date) {
		long passTime = getCurrentMilliseconds() - date.getTime();
		if (passTime > 0) {
			return true;
		} else {
			return false;
		}
	}

}
