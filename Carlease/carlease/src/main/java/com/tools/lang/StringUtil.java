package com.tools.lang;


/**
 * 转换，配合数据库com.sqlite一起使用
 * 
 * @author lmc
 * 版权声明：版权归作者所有，未经协议授权不得使用、转载、抄袭等，否则依法追究法律责任和经济责任。
 */
public class StringUtil {

	private static final String emptyString = "empty string.";
	private static final String emptyObject = "empty object.";

	/**
	 * 判断字符串是否为空，等于null或者长度不大于零都视为空字符串
	 * 
	 * @param src
	 * @return
	 */
	public static boolean isEmptyString(String text) {
		if (text == null) {
			return true;
		}

		if (text.length() <= 0) {
			return true;
		}

		return false;
	}

	public static boolean String2Boolean(String value) {
		if (isEmptyString(value)) {
			throw new IllegalArgumentException(emptyString);
		}
		return Boolean.valueOf(value).booleanValue();
	}

	public static int String2Integer(String value) {
		if (isEmptyString(value)) {
			throw new IllegalArgumentException(emptyString);
		}
		return Integer.valueOf(value).intValue();
	}

	public static long String2Long(String value) {
		if (isEmptyString(value)) {
			throw new IllegalArgumentException(emptyString);
		}
		return Long.valueOf(value).longValue();
	}

	public static float String2Float(String value) {
		if (isEmptyString(value)) {
			throw new IllegalArgumentException(emptyString);
		}
		return Float.valueOf(value).floatValue();
	}

	public static double String2Double(String value) {
		if (isEmptyString(value)) {
			throw new IllegalArgumentException(emptyString);
		}
		return Double.valueOf(value).doubleValue();
	}

	public static Object String2Object(String value) {
		if (value == null) {
			new NullPointerException(emptyString).printStackTrace();
			return null;
		}
		// new String 遇到 null 会出错
		return new String(value);
	}

	public static String Boolean2String(boolean value) {
		return String.valueOf(value);
	}

	public static String Integer2String(int value) {
		return String.valueOf(value);
	}

	public static String Long2String(long value) {
		return String.valueOf(value);
	}

	public static String Float2String(float value) {
		return String.valueOf(value);
	}

	public static String Double2String(double value) {
		return String.valueOf(value);
	}

	public static String Object2String(Object object) {
		if (object == null) {
			new NullPointerException(emptyObject).printStackTrace();
			return null;
		}
		return object.toString();
	}

	public static boolean Object2Boolean(Object object) {
		if (object == null) {
			throw new IllegalArgumentException(emptyObject);
		}
		return String2Boolean(object.toString());
	}

	public static int Object2Integer(Object object) {
		if (object == null) {
			throw new IllegalArgumentException(emptyObject);
		}
		return String2Integer(object.toString());
	}

	public static long Object2Long(Object object) {
		if (object == null) {
			throw new IllegalArgumentException(emptyObject);
		}
		return String2Long(object.toString());
	}

	public static float Object2Float(Object object) {
		if (object == null) {
			throw new IllegalArgumentException(emptyObject);
		}
		return String2Float(object.toString());
	}

	public static double Object2Double(Object object) {
		if (object == null) {
			throw new IllegalArgumentException(emptyObject);
		}
		return String2Double(object.toString());
	}

	public static Object Boolean2Object(boolean value) {
		return new Boolean(value);
	}

	public static Object Integer2Object(int value) {
		return new Integer(value);
	}

	public static Object Long2Object(long value) {
		return new Long(value);
	}

	public static Object Float2Object(float value) {
		return new Float(value);
	}

	public static Object Double2Object(double value) {
		return new Double(value);
	}

}
