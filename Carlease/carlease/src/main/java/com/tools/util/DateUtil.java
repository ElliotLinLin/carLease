package com.tools.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * 日期模块
 *
 * 相关类：
 * java.util.Date
 * android.text.format.DateFormat
 * android.text.format.DateUtils
 * java.text.DateFormat
 * java.text.SimpleDateFormat
 * 
 * abstract java.util.Calendar
 * java.util.GregorianCalendar
 * 
 * android.text.format.Time
 * android.util.TimeUtils
 * 
 * 功能:
 * 对象java.util.Date、日期字符串、毫秒数三者之间的转换。
 * 
 * Date表示yyyy-MM-dd HH:mm:ss
 * Time表示HH:mm:ss
 * 没有Datetime
 *
 * yyyy-MM-dd HH:mm:ss
 * yyyy-MM-dd HH:mm:ss.SSS
 *
 * "yyyy-MM-dd",
   "yyyy-MM-dd HH:mm",
   "yyyy-MM-dd HH:mmZ",
   "yyyy-MM-dd HH:mm:ss.SSSZ",
   "yyyy-MM-dd'T'HH:mm:ss.SSSZ",
 *
 * @author lmc
 * 版权声明：版权归作者所有，未经协议授权不得使用、转载、抄袭等，否则依法追究法律责任和经济责任。
 */
public class DateUtil {

	private static final String TAG = DateUtil.class.getSimpleName();

	public static final String default_format_date_time_all = "yyyy-MM-dd HH:mm:ss.SSS";

	public static final String default_format_date_time = "yyyy-MM-dd HH:mm:ss";
	public static final String default_format_date = "yyyy-MM-dd";
	public static final String default_format_time = "HH:mm:ss";

	public static final String default_format_time_all = "HH:mm:ss.SSS";

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
	 * 得到当前日期字符串
	 * 
	 * @param format
	 * @param locale 系统语言
	 * @param timeZone 时区
	 * @return
	 */
	public static String getCurrentDateString(String format, Locale locale, TimeZone timeZone) {
		if (isEmptyString(format) || locale == null || timeZone == null) {
			return null;
		}
		//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINESE);
		//		sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
		//		String dateTime = sdf.format(new Date(System.currentTimeMillis()));
		//		return dateTime;
		SimpleDateFormat sdf = new SimpleDateFormat(format, locale);
		sdf.setTimeZone(timeZone);
		String dateTime = sdf.format(new Date(getCurrentMilliseconds()));
		return dateTime;
	}

	/**
	 * 得到当前日期字符串
	 * 
	 * @param format
	 * @return
	 */
	public static String getCurrentDateString(String format) {
		return getCurrentDateString(format, Locale.getDefault(), TimeZone.getDefault());
	}

	/**
	 * 得到当前日期字符串
	 * 
	 * @return
	 */
	public static String getCurrentDateString() {
		return getCurrentDateString( default_format_date );
	}

	/**
	 * 得到当前时间字符串
	 * 
	 * @return
	 */
	public static String getCurrentTimeString() {
		return getCurrentDateString( default_format_time );
	}

	/**
	 * 得到当前时间毫秒数
	 *
	 * @return
	 */
	public static long getCurrentMilliseconds() {
		// System.currentTimeMillis()
		// Calendar.getInstance().getTimeInMillis()
		return Calendar.getInstance().getTimeInMillis();
	}

	/**
	 * 得到当前日期对象java.util.Date
	 * 
	 * @return
	 */
	public static java.util.Date getCurrentDate() {
		return Milliseconds2Date( getCurrentMilliseconds() );
	}

	/**
	 * 得到当前时间对象android.text.format.Time (不要此方法，因没有Time对象)
	 * 
	 * android.text.format.Time
	 * android.util.TimeUtils
	 * 
	 * @return
	 */
	//	public static android.text.format.Time getCurrentTime() {
	//		return null;
	//	}

	/**
	 * 格式日期字符串成日期对象java.util.Date
	 *
	 * @param format
	 * @param date
	 * @return
	 */
	public static java.util.Date formatDateString2Date(String format, String date) {
		if (isEmptyString(format) || isEmptyString(date)) {
			return null;
		}

		java.util.Date __date = null;
		java.text.SimpleDateFormat sdf = new SimpleDateFormat(format);
		try {
			__date = sdf.parse(date);
			// TODO 给年代加上1900（不要加上1900，其它的方法使用后，数值会变大）
			//			date.setYear(date.getYear()+1900);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return __date;
	}

	/**
	 * 格式日期字符串成毫秒数
	 * 
	 * @param format
	 * @param date
	 * @return
	 */
	public static long formatDateString2Milliseconds(String format, String date) {
		java.util.Date __date = formatDateString2Date(format, date);
		if (__date == null) {
			return 0L;
		}
		return __date.getTime();
	}

	/**
	 * 格式化日期对象成日期字符串
	 *
	 * @param format
	 * @param date
	 * @return
	 */
	public static String formatDate2DateString(String format, java.util.Date date) {
		if (isEmptyString(format) || date == null) {
			return null;
		}

		SimpleDateFormat sdf = new SimpleDateFormat(format);
		String dateString = sdf.format(date);
		return dateString;
	}

	/**
	 * 格式化毫秒数成日期字符串
	 *
	 * @param format
	 * @param date
	 * @return
	 */
	public static String formatMilliseconds2DateString(String format, long milliseconds) {
		java.util.Date date = Milliseconds2Date(milliseconds);
		return formatDate2DateString(format, date);
	}

	/**
	 * 日期对象转成毫秒数
	 * 
	 * @param date 日期
	 * @return
	 */
	public static long Date2Milliseconds(java.util.Date date) {
		if (date == null) {
			return 0L;
		}
		return date.getTime();
	}

	/**
	 * 日期对象java.util.Date转成日期字符串
	 * 
	 * @param date
	 * @return
	 */
	public static String Date2DateString(java.util.Date date) {
		return formatDate2DateString(default_format_date_time, date);
	}

	/**
	 * 日期对象java.util.Date转成时间字符串
	 * 
	 * @param date
	 * @return
	 */
	public static String Date2TimeString(java.util.Date date) {
		return formatDate2DateString(default_format_time, date);
	}

	/**
	 * 毫秒数转成日期对象java.util.Date
	 * 
	 * @param milliseconds 毫秒数
	 * @return
	 */
	public static java.util.Date Milliseconds2Date(long milliseconds) {
		return new Date(milliseconds);
	}

	/**
	 * 毫秒数转成日期字符串
	 * 
	 * @param milliseconds 毫秒数
	 * @return 日期yyyy-MM-dd HH:mm:ss
	 */
	public static String Milliseconds2DateString(long milliseconds) {
		Date date = new Date(milliseconds);
		return Date2DateString(date);
	}

	/**
	 * 日期字符串转成日期对象java.util.Date
	 * 
	 * @param date
	 * @return
	 */
	public static java.util.Date DateString2Date(String date) {
		return formatDateString2Date(default_format_date_time, date);
	}

	/**
	 * 日期字符串转成毫秒数
	 * 
	 * @param date
	 * @return
	 */
	public static long DateString2Milliseconds(String date) {
		Date __date = formatDateString2Date(default_format_date_time, date);
		return __date.getTime();
	}

	/**
	 * 比较日期大小 TODO 未实现
	 * < 0 表示 date1 < date2
	 * = 0 表示 date1 = date2
	 * > 0 表示 date1 > date2
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int compareTo(Date date1, Date date2) {
		return 0;
	}

	/**
	 * 比较日期大小 TODO 未实现
	 * < 0 表示 date1 < date2
	 * = 0 表示 date1 = date2
	 * > 0 表示 date1 > date2
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int compareTo(String date1, String date2) {
		return 0;
	}

	/**
	 * 比较日期大小
	 * < 0 表示 date1 < date2
	 * = 0 表示 date1 = date2
	 * > 0 表示 date1 > date2
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int compareTo(long date1, long date2) {
		if (date1 < date2) {
			return -1;
		}else if (date1 > date2) {
			return 1;
		}else{
			return 0;
		}
	}

	/**
	 * 秒数转换成 mm:ss格式
	 * @param seconds 秒数
	 * @return	时间 mm:ss
	 */
	@Deprecated
	public static String Seconds2MMString(long seconds){
		long min = seconds / 60;
		long sec = seconds % 60;
		String minStr = min<10?("0"+min):(""+min);
		String secStr = sec<10?("0"+sec):(""+sec);
		return minStr+":"+secStr;
	}

	/**
	 * 秒数转换成 hh:mm:ss格式
	 * @param seconds 秒数
	 * @return 时间 hh:mm:ss
	 */
	@Deprecated
	public static String Seconds2String(long seconds){
		long hour = seconds / 3600;
		long min = seconds / 60;
		long sec = seconds % 60;
		String hourStr = hour<10?("0"+hour):(""+hour);
		String minStr = min<10?("0"+min):(""+min);
		String secStr = sec<10?("0"+sec):(""+sec);
		return hourStr+":"+minStr+":"+secStr;
	}

	/**
	 * 判断时间是否过去时间
	 * @param date 判断的时间
	 * @return 是否过去时间
	 */
	@Deprecated
	public static boolean isPassDate(java.util.Date date){
		long passTime = getCurrentMilliseconds() - date.getTime();
		if (passTime > 0) {
			return true;
		} else {
			return false;
		}
	}

}
