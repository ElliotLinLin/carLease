package com.tools.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.hst.Carlease.app.MainApplication;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;


public class UIUtils {

	private static final String TAG = "UIUtils";
	public static final TimeZone tz = TimeZone.getTimeZone("GMT+8:00");
	public static final SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy-MM-dd");
	private static final long ONEDAY = 86400000;

	public static Context getContext() {
		return MainApplication.getmContext();
	}

	public static String getPackageName() {
		return getContext().getPackageName();
	}

	public static Resources getResources() {
		return getContext().getResources();
	}

	public static String getString(int resId) {
		return getResources().getString(resId);
	}

	public static int getColor(int resId) {
		return getResources().getColor(resId);
	}

	public static void printScreenSize(FragmentActivity ui) {
		// 方法1 Android获得屏幕的宽和高
		WindowManager windowManager = ui.getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		int screenWidth = screenWidth = display.getWidth();
		int screenHeight = screenHeight = display.getHeight();
		System.out.println("分辨率：" + screenWidth + "*" + screenHeight);
	}

	// 屏幕高
	public static int getScreenHeight(FragmentActivity ui) {
		// 方法1 Android获得屏幕的宽和高
		WindowManager windowManager = ui.getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		return display.getHeight();
	}

	// 屏幕宽
	public static int getScreenWidth(FragmentActivity ui) {
		// 方法1 Android获得屏幕的宽和高
		WindowManager windowManager = ui.getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		return display.getWidth();
	}

	/**
	 * dip转px
	 * 
	 * @param i
	 */
	public static int dip2px(int dip) {
		// dip ---> px

		// 公式 ： px = dp * (dpi / 160)
		// dp = 160 * px / dpi
		// Density = px / dp
		// px = dp * density

		DisplayMetrics metrics = getResources().getDisplayMetrics();
		float density = metrics.density;
		return (int) (dip * density + 0.5f);
	}

	/**
	 * px转dip
	 * 
	 * @param px
	 * @return
	 */
	public static int px2dip(int px) {
		// px = dp * density
		// dp = px/ density

		DisplayMetrics metrics = getResources().getDisplayMetrics();
		float density = metrics.density;
		return (int) (px / density + 0.5f);
	}

	/**
	 * 获取手机imei
	 * 
	 * @param context
	 * @return
	 */
	public static String getIMEI(Context context) {
		// 获取手机IMEI
		TelephonyManager mTelephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

		String imei = mTelephonyMgr.getDeviceId();
		return imei;
	}

	public static String getStringInt(String str) {
		int dis;
		try {
			dis = (int) Double.parseDouble(str);
		} catch (Exception e) {
			dis = 0;
		}

		return dis + "";
	}

	/**
	 * 将字符串转换为时间
	 * 
	 * @param str
	 * @return
	 * @throws ParseException
	 * 
	 */
	@SuppressLint("SimpleDateFormat")
	public static Date getDate4String(String str) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.parse(str);
	}

	/**
	 * 计算两个日期型的时间相差多少时间
	 * 
	 * @param startDate
	 *            开始日期
	 * @param endDate
	 *            结束日期
	 * @return
	 */
	public String twoDateDistance(Date startDate, Date endDate) {

		if (startDate == null || endDate == null) {
			return null;
		}
		long timeLong = endDate.getTime() - startDate.getTime();
		if (timeLong < 60 * 1000)
			return timeLong / 1000 + "秒前";
		else if (timeLong < 60 * 60 * 1000) {
			timeLong = timeLong / 1000 / 60;
			return timeLong + "分钟前";
		} else if (timeLong < 60 * 60 * 24 * 1000) {
			timeLong = timeLong / 60 / 60 / 1000;
			return timeLong + "小时前";
		} else if (timeLong < 60 * 60 * 24 * 1000 * 7) {
			timeLong = timeLong / 1000 / 60 / 60 / 24;
			return timeLong + "天前";
		} else if (timeLong < 60 * 60 * 24 * 1000 * 7 * 4) {
			timeLong = timeLong / 1000 / 60 / 60 / 24 / 7;
			return timeLong + "周前";
		} else {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			sdf.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));
			return sdf.format(startDate);
		}
	}

	/**
	 * 判断两个时间是否在某段时间内---以秒计算
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static boolean isInDistanceDate(Date startDate, Date endDate, int distanceSecond) {
		if (startDate == null || endDate == null) {
			return false;
		}
		long timeLong = endDate.getTime() - startDate.getTime();
		if (timeLong < 0) {
			return false;
		}
		if (timeLong < distanceSecond * 1000) {
			return true;
		}
		return false;
	}

	/**
	 * 判断enddate是否大于startdate某段时间
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static boolean isMoreThanDistanceDate(Date startDate, Date endDate, int distanceSecond) {
		if (startDate == null || endDate == null) {
			return false;
		}
		long timeLong = endDate.getTime() - startDate.getTime();
		if (timeLong < 0) {
			return false;
		}
		if (timeLong >= distanceSecond * 1000) {
			return true;
		}
		return false;
	}

	/**
	 * @param dbName
	 *            数据库名字
	 * @return 数据库是否存在
	 */
	public static boolean isExist(String dbName, Context ui) {
		File file = new File(ui.getFilesDir().getPath() + "/" + dbName);
		Log.e(TAG, file.getAbsolutePath() + ":数据库文件是否存在 :" + file.exists());
		return file.exists();
	}

	/**
	 * @param dbName
	 *            数据库名字
	 * @return 数据库是否存在
	 */
	public static boolean isExist(String filename) {
		File file = new File(MainApplication.getmContext().getFilesDir().getPath() + "/" + filename);
		Log.e(TAG, file.getAbsolutePath() + ":要拷贝的文件是否存在 :" + file.exists());
		return file.exists();
	}

	/**
	 * 获取文件路径
	 * 
	 * @param ui
	 * @return
	 */
	public static String getFilesPath(Context ui) {
		return ui.getFilesDir().getPath();
	}

	public static String getFileAbsPath() {
		return MainApplication.getmContext().getFilesDir().getPath();
	}

	public static void copyFile(final String filename) {
		final Context context = MainApplication.getmContext();
		if (isExist(filename)) {
			return;
		}
		new Thread() {
			public void run() {

				FileOutputStream fos = null;
				InputStream is = null;
				try {
					fos = context.openFileOutput(filename, context.MODE_PRIVATE);
					// 获取AssetManager
					AssetManager am = context.getAssets();
					is = am.open(filename);
					// 流的读写数据
					byte[] buffer = new byte[1024];
					// 读取的字节数
					int len = is.read(buffer);
					int totalSize = 0;

					while (len != -1) {
						totalSize++;

						fos.write(buffer, 0, len);// 防止最后一次读不满
						if (totalSize % 10 == 0) {// 每读10次 刷新数据
							fos.flush();// 写到文件
						}
						len = is.read(buffer);// 继续读取数据
					}
					// io流关闭

				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					if (is != null) {
						try {
							is.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					if (fos != null) {
						try {
							fos.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			};
		}.start();

	}

	/**
	 * 拷贝assert目录下的数据库到files目录
	 * 
	 * @param string
	 */
	public static void copyDB(final String dbName, final Context ui) {
		if (isExist(dbName, ui)) {
			return;
		}
		new Thread() {
			public void run() {

				FileOutputStream fos = null;
				InputStream is = null;
				try {
					fos = ui.openFileOutput(dbName, ui.MODE_PRIVATE);
					// 获取AssetManager
					AssetManager am = ui.getAssets();
					is = am.open(dbName);
					// 流的读写数据
					byte[] buffer = new byte[1024];
					// 读取的字节数
					int len = is.read(buffer);
					int totalSize = 0;

					while (len != -1) {
						totalSize++;

						fos.write(buffer, 0, len);// 防止最后一次读不满
						if (totalSize % 10 == 0) {// 每读10次 刷新数据
							fos.flush();// 写到文件
						}
						len = is.read(buffer);// 继续读取数据
					}
					// io流关闭

				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					if (is != null) {
						try {
							is.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					if (fos != null) {
						try {
							fos.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			};
		}.start();

	}

	/**
	 * 通过一个 date形式的String，转化为指定格式的 string 原string格式：SimpleDateFormat sdf = new
	 * SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 * 
	 * SimpleDateFormat tarteforramt = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	 * 
	 * @param dateString
	 * @param format
	 * @return
	 * @throws ParseException
	 */
	public static String getString4Date(String dateString, SimpleDateFormat format) throws ParseException {

		Date date = getDate4String(dateString);

		return getNumberString(format.format(date));
	}

	/**
	 * 将字符串中的中文转化为阿拉伯数字
	 * 
	 * @param str
	 * @return
	 */

	public static String getNumberString(String str) {
		if (str == null) {
			return "";
		}
		str = str.replaceAll("一", "1");
		str = str.replaceAll("二", "2");
		str = str.replaceAll("三", "3");
		str = str.replaceAll("四", "4");
		str = str.replaceAll("五", "5");
		str = str.replaceAll("六", "6");
		str = str.replaceAll("七", "7");
		str = str.replaceAll("八", "8");
		str = str.replaceAll("九", "9");
		str = str.replaceAll("十", "1");
		return str;
	}

	/**
	 * 判断应用是否前台运行
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isBackground(Context context) {
		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
		for (RunningAppProcessInfo appProcess : appProcesses) {
			if (appProcess.processName.equals(context.getPackageName())) {
				/*
				 * BACKGROUND=400 EMPTY=500 FOREGROUND=100 GONE=1000
				 * PERCEPTIBLE=130 SERVICE=300 ISIBLE=200
				 */
				Log.i(context.getPackageName(), "此appimportace =" + appProcess.importance + ",context.getClass().getName()="
						+ context.getClass().getName());
				if (appProcess.importance != RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
					Log.i(context.getPackageName(), "处于后台" + appProcess.processName);
					return true;
				} else {
					Log.i(context.getPackageName(), "处于前台" + appProcess.processName);
					return false;
				}
			}
		}
		return false;
	}

	public static String get24HoursDateString(String dateString) throws ParseException {
		Date date = getDate4String(dateString + " 00:00:00");
		long time = date.getTime();
		// 减去24小时
		time = time - 24 * 60 * 60 * 1000;
		Date lastdate = new Date(time);
		SimpleDateFormat tarteforramt = new SimpleDateFormat("yyyy-MM-dd");

		return tarteforramt.format(lastdate);
	}

	/**
	 * 获取今天昨天-星期
	 * 
	 * @param time
	 * @param type
	 * @return
	 */
	public static String getDateString(long time) {
		Calendar c = Calendar.getInstance();
		c = Calendar.getInstance(tz);
		c.setTimeInMillis(time);
		long currentTime = System.currentTimeMillis();
		Calendar current_c = Calendar.getInstance();
		current_c = Calendar.getInstance(tz);
		current_c.setTimeInMillis(currentTime);

		int currentYear = current_c.get(Calendar.YEAR);
		int y = c.get(Calendar.YEAR);
		int m = c.get(Calendar.MONTH) + 1;
		int d = c.get(Calendar.DAY_OF_MONTH);
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int week = c.get((Calendar.DAY_OF_WEEK));
		int minute = c.get(Calendar.MINUTE);
		int second = c.get(Calendar.SECOND);
		long t = currentTime - time;

		long t2 = currentTime - getCurrentDayTime();
		String dateStr = "";

		if (t < t2 && t > 0) {

			dateStr = "今天  " + (hour < 10 ? "0" + hour : hour) + ":" + (minute < 10 ? "0" + minute : minute);

		} else if (t < (t2 + ONEDAY) && t > 0) {

			dateStr = "昨天  " + (hour < 10 ? "0" + hour : hour) + ":" + (minute < 10 ? "0" + minute : minute);

		} else {
			if (t < (t2 + ONEDAY * 6) && t > 0) {
				dateStr = "星期" + getNumber(week) + "  " + (hour < 10 ? "0" + hour : hour) + ":" + (minute < 10 ? "0" + minute : minute);
			} else {
				dateStr = m + "月" + d + "日" + "  " + (hour < 10 ? "0" + hour : hour) + ":" + (minute < 10 ? "0" + minute : minute);
			}
		}
		return dateStr;
	}

	/**
	 * 获取当前当天日期的毫秒数 2012-03-21的毫秒数
	 * 
	 * @return
	 */
	public static long getCurrentDayTime() {
		Date d = new Date(System.currentTimeMillis());
		String formatDate = yearFormat.format(d);
		try {
			return (yearFormat.parse(formatDate)).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static String getNumber(int i) {
		Log.e(TAG, i + " dayofweek");
		String numberString[] = { "", "天", "一", "二", "三", "四", "五", "六" };
		return numberString[i];

	}

	public static boolean isEmail(String email) {
		if (null == email || "".equals(email))
			return false;
		// Pattern p = Pattern.compile("\\w+@(\\w+.)+[a-z]{2,3}"); //简单匹配
		Pattern p = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");// 复杂匹配
		Matcher m = p.matcher(email);
		return m.matches();
	}

	public static void sendSMS(String smsBody, FragmentActivity ui)

	{

		Uri smsToUri = Uri.parse("smsto:");

		Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);

		intent.putExtra("sms_body", smsBody);

		ui.startActivity(intent);

	}

	/**
	 * 获取给定时间过后的某个时间
	 * 
	 * @param nowTime
	 * @param hours
	 * @param format
	 * @return
	 */
	public static String getFuture(String nowTime, int hours, SimpleDateFormat format) {
		try {
			Date date = getDate4String(nowTime);
			long time = date.getTime() + hours * 60 * 60 * 1000L;

			date.setTime(time);

			return format.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return "00:00:00";
		}

	}

	// 判断今天是不是本周的最后一天
	public static String getWeekDay() {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY); // 获取本周一的日期
		// 这种输出的是上个星期周日的日期，因为老外那边把周日当成第一天
		cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		// 增加一个星期，才是我们中国人理解的本周日的日期
		cal.add(Calendar.WEEK_OF_YEAR, 1);
		return df.format(cal.getTime());
	}

	// 判断今天是不是本月的最后一天
	public static String getMonthDate() {
		// 获取Calendar
		Calendar calendar = Calendar.getInstance();
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		// 设置时间,当前时间不用设置
		calendar.set(Calendar.DATE, calendar.getActualMinimum(Calendar.DATE));
		// 设置日期为本月最大日期
		calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
		return format.format(calendar.getTime());
	}

	public static String getPhoneNumber(Context context) {
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		String tel = tm.getLine1Number();
		return tel;
	}

	/**传入的数据是Km的单位
	 * @param distance
	 * @return
	 */
	public static String getDistanceString(double distance) {
		String content = "";
		try {
			if (distance >= 1) {
				// 去小数点后一位
				content = Math.round(distance * 10) / 10.0 + "Km";
			} else {
				// 小于1Km显示单位m
				content = ((int) (distance * 1000d)) + "m";
			}
		} catch (Exception e) {
			content = "0m";
		} finally {

		}
		return content;
	}
	/**是否是平板
	 * @param context
	 * @return
	 */
	public static boolean isTablet(Context context) {
		return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
	}

}
