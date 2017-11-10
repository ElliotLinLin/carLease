package com.tools.os;

import java.io.UnsupportedEncodingException;

import com.tools.util.Log;

/**
 * 字符集
 * 
 * TODO
 * new String(bytes, Charset.UTF_32); bytes一定是UTF-32的字符，否则会有意想不到的错误。
 * 也就是说将字节数组转成String前提是要知道bytes的原来的字符集。
 * 第二个参数表示bytes原来的字符集。
 * 
 * text.getBytes(Charset.GBK); 将text转成Charset.GBK的字符集
 * 
 * http://wenwen.sogou.com/z/q76869615.htm
 * 
 * 例子：
 * 
 * try {
			byte[] b_GBK = "中".getBytes(Charset.GBK);
			Log.e(TAG, "b_GBK text:"+new String(b_GBK, Charset.GBK));
			byte[] b_GB2312 = "中".getBytes(Charset.GB2312);
			byte[] b_BIG5 = "中".getBytes(Charset.BIG5);
			byte[] b_GB18030 = "中".getBytes(Charset.GB18030);

			byte[] b_ASCII = "中".getBytes(Charset.ASCII);
			byte[] b_ISO_8859_1 = "中".getBytes(Charset.ISO_8859_1);

			byte[] b_UTF_8 = "中".getBytes(Charset.UTF_8);
			Log.e(TAG, "b_UTF_8 text:"+new String(b_UTF_8, Charset.UTF_8));
			byte[] b_UTF_16 = "中".getBytes(Charset.UTF_16);
			Log.e(TAG, "b_UTF_16 text:"+new String(b_UTF_16, Charset.UTF_16));
			byte[] b_UTF_32 = "中".getBytes(Charset.UTF_32);
			Log.e(TAG, "b_UTF_32 text:"+new String(b_UTF_32, Charset.UTF_32));
			byte[] b_UNICODE = "中".getBytes(Charset.UNICODE);
			Log.e(TAG, "b_UNICODE text:"+new String(b_UNICODE, Charset.UNICODE));

			Log.e(TAG, "b_GBK len:"+b_GBK.length); // 长度为2
			Log.e(TAG, "b_GB2312 len:"+b_GB2312.length); // 长度为2
			Log.e(TAG, "b_BIG5 len:"+b_BIG5.length); // 长度为2
			Log.e(TAG, "b_GB18030 len:"+b_GB18030.length); // 长度为2

			Log.e(TAG, "b_ASCII len:"+b_ASCII.length); // 长度为1
			Log.e(TAG, "b_ISO_8859_1 len:"+b_ISO_8859_1.length); // 长度为1

			Log.e(TAG, "b_UTF_8 len:"+b_UTF_8.length); // 长度为3
			Log.e(TAG, "b_UTF_16 len:"+b_UTF_16.length); // 长度为4
			Log.e(TAG, "b_UTF_32 len:"+b_UTF_32.length); // 长度为8
			Log.e(TAG, "b_UNICODE len:"+b_UNICODE.length); // 长度为4

			String text = new String(b_UTF_32, Charset.UTF_16);
			Log.e(TAG, "text:"+text);
			Log.e(TAG, "len a:"+text.length()); // 长度为1
			Log.e(TAG, "len b:"+text.getBytes().length); // 长度为3 --- 采用系统编码UTF-8
			Log.e(TAG, "GBK len b:"+text.getBytes(Charset.GBK).length); // 长度为1
			Log.e(TAG, "GB2312 len b:"+text.getBytes(Charset.GB2312).length); //  长度为1
			Log.e(TAG, "UTF_8 len b:"+text.getBytes(Charset.UTF_8).length); // 长度为3
			Log.e(TAG, "UTF_16 len b:"+text.getBytes(Charset.UTF_16).length); // 长度为4
			Log.e(TAG, "UTF_32 len b:"+text.getBytes(Charset.UTF_32).length); // 长度为8
			Log.e(TAG, "UNICODE len b:"+text.getBytes(Charset.UNICODE).length); // 长度为4

		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
 * 
 * 04-03 10:48:40.135: E/LauncherUI(5944): b_GBK text:中
04-03 10:48:40.144: E/LauncherUI(5944): b_UTF_8 text:中
04-03 10:48:40.144: E/LauncherUI(5944): b_UTF_16 text:中
04-03 10:48:40.146: E/LauncherUI(5944): b_UTF_32 text:中
04-03 10:48:40.147: E/LauncherUI(5944): b_UNICODE text:中
04-03 10:48:40.148: E/LauncherUI(5944): b_GBK len:2
04-03 10:48:40.149: E/LauncherUI(5944): b_GB2312 len:2
04-03 10:48:40.150: E/LauncherUI(5944): b_BIG5 len:2
04-03 10:48:40.151: E/LauncherUI(5944): b_GB18030 len:2
04-03 10:48:40.151: E/LauncherUI(5944): b_ASCII len:1
04-03 10:48:40.152: E/LauncherUI(5944): b_ISO_8859_1 len:1
04-03 10:48:40.153: E/LauncherUI(5944): b_UTF_8 len:3
04-03 10:48:40.153: E/LauncherUI(5944): b_UTF_16 len:4
04-03 10:48:40.154: E/LauncherUI(5944): b_UTF_32 len:8
04-03 10:48:40.155: E/LauncherUI(5944): b_UNICODE len:4
04-03 10:48:40.157: E/LauncherUI(5944): text:��中��
04-03 10:48:40.158: E/LauncherUI(5944): len a:3
04-03 10:48:40.159: E/LauncherUI(5944): len b:5
04-03 10:48:40.160: E/LauncherUI(5944): GBK len b:4
04-03 10:48:40.161: E/LauncherUI(5944): GB2312 len b:4
04-03 10:48:40.162: E/LauncherUI(5944): UTF_8 len b:5
04-03 10:48:40.163: E/LauncherUI(5944): UTF_16 len b:8
04-03 10:48:40.165: E/LauncherUI(5944): UTF_32 len b:16
04-03 10:48:40.167: E/LauncherUI(5944): UNICODE len b:8

 * 
 * @author LMC
 *
 */
public class Charset {

	private static final String TAG = Charset.class.getSimpleName();

	public static final String ASCII = "ASCII"; // 单字节编码系统
	public static final String US_ASCII = "US-ASCII";
	//	public static final String MBCS = "MBCS"; // 为了扩充ASCII编码   TODO 不支持
	public static final String ISO_8859_1 = "ISO-8859-1";

	public static final String UTF_8 = "UTF-8"; // 一种针对Unicode的可变长度字符编码，又称万国码
	public static final String UTF_16 = "UTF-16";
	public static final String UTF_16BE = "UTF-16BE";
	public static final String UTF_16LE = "UTF-16LE";
	public static final String UTF_32 = "UTF-32";
	public static final String UNICODE = "Unicode"; // 也称“统一码”“万国码”

	// 下面适合中文
	public static final String GBK = "GBK"; // 是汉字编码标准之一
	public static final String GB2312 = "GB2312"; // 信息交换用汉字编码字符集
	public static final String BIG5 = "BIG5"; // 繁体中文
	public static final String GB18030 = "GB18030"; // 是国家新出来的一种字符集，其为一个汉字设计了4个字节

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
	 * 判断字符集是否支持
	 * 
	 * @param charset
	 * @return
	 */
	public static boolean isSupported(String charset) {
		if (isEmptyString(charset)) {
			return false;
		}
		return java.nio.charset.Charset.isSupported(charset);
	}

	/**
	 * 将字节数组转成指定的字符集字节数组
	 * 
	 * @param bytes 字节数组
	 * @param bytesCharset 字节数组的字符集
	 * @param destCharset 将转换成目标字符集
	 * @return
	 */
	public static byte[] convert(byte[] bytes, String bytesCharset, String destCharset) {
		if (bytes == null) {
			return null;
		}

		if (isEmptyString(bytesCharset)) {
			return null;
		}

		if (isEmptyString(destCharset)) {
			return null;
		}

		if (java.nio.charset.Charset.isSupported(bytesCharset) == false) {
			return null;
		}

		if (java.nio.charset.Charset.isSupported(destCharset) == false) {
			return null;
		}

		// 相等则直接返回
		if (bytesCharset.equalsIgnoreCase(destCharset)) {
			return bytes;
		}

		try {
			String text = new String(bytes, bytesCharset);
			if (isEmptyString(text)) {
				return null;
			}
			return text.getBytes(destCharset);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 将字节数组转成指定的字符集字符串
	 * 
	 * @param bytes 字节数组
	 * @param bytesCharset 字节数组的字符集
	 * @param destCharset 将转换成目标字符集
	 * @return
	 */
	public static String convertString(byte[] bytes, String bytesCharset, String destCharset) {
		return bytes2String( convert(bytes, bytesCharset, destCharset), destCharset );
	}

	/**
	 * 将字符串转成指定的字符集字符串
	 * 
	 * @param sourceBuffer 源字符串
	 * @param destCharset 将转换成目标字符集
	 * @return
	 */
	public static String convertString(String sourceBuffer, String destCharset) {
		if (isEmptyString(sourceBuffer)) {
			return null;
		}

		if (isEmptyString(destCharset)) {
			return null;
		}

		if (java.nio.charset.Charset.isSupported(destCharset) == false) {
			return null;
		}

		try {
			// 转成charset的字节数组
			byte[] bytes = sourceBuffer.getBytes(destCharset);
			// 将字节数组转成相应的字符集
			if (bytes != null) {
				return new String(bytes, destCharset);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 字节数组转成对应字符集的字符串（该字节数组就是由此字符集转换的）
	 * 
	 * @param bytes 字节数组
	 * @param bytesCharset 字节数组的字符集
	 * @return
	 */
	public static String bytes2String(byte[] bytes, String bytesCharset) {
		if (bytes == null) {
			return null;
		}

		if (isEmptyString(bytesCharset)) {
			return null;
		}

		if (java.nio.charset.Charset.isSupported(bytesCharset) == false) {
			return null;
		}

		try {
			return new String(bytes, bytesCharset);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 将字符串转成指定字符集的字节数组
	 * 
	 * @param text
	 * @param destCharset
	 * @return
	 */
	public static byte[] string2Bytes(String text, String destCharset) {
		if (text == null) {
			return null;
		}

		if (isEmptyString(destCharset)) {
			return null;
		}

		if (java.nio.charset.Charset.isSupported(destCharset) == false) {
			return null;
		}

		try {
			return text.getBytes(destCharset);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 得到系统默认的字符集
	 * 
	 * @return
	 */
	public static String defaultCharset() {
		return java.nio.charset.Charset.defaultCharset().name();
	}

	/**
	 * 测试
	 */
	public void test() {
		try {
			byte[] b_GBK = "中".getBytes(Charset.GBK);
			Log.e(TAG, "b_GBK text:"+new String(b_GBK, Charset.GBK));
			byte[] b_GB2312 = "中".getBytes(Charset.GB2312);
			byte[] b_BIG5 = "中".getBytes(Charset.BIG5);
			byte[] b_GB18030 = "中".getBytes(Charset.GB18030);

			byte[] b_ASCII = "中".getBytes(Charset.ASCII);
			byte[] b_ISO_8859_1 = "中".getBytes(Charset.ISO_8859_1);

			byte[] b_UTF_8 = "中".getBytes(Charset.UTF_8);
			Log.e(TAG, "b_UTF_8 text:"+new String(b_UTF_8, Charset.UTF_8));
			byte[] b_UTF_16 = "中".getBytes(Charset.UTF_16);
			Log.e(TAG, "b_UTF_16 text:"+new String(b_UTF_16, Charset.UTF_16));
			byte[] b_UTF_32 = "中".getBytes(Charset.UTF_32);
			Log.e(TAG, "b_UTF_32 text:"+new String(b_UTF_32, Charset.UTF_32));
			byte[] b_UNICODE = "中".getBytes(Charset.UNICODE);
			Log.e(TAG, "b_UNICODE text:"+new String(b_UNICODE, Charset.UNICODE));

			Log.e(TAG, "b_GBK len:"+b_GBK.length); // 长度为2
			Log.e(TAG, "b_GB2312 len:"+b_GB2312.length); // 长度为2
			Log.e(TAG, "b_BIG5 len:"+b_BIG5.length); // 长度为2
			Log.e(TAG, "b_GB18030 len:"+b_GB18030.length); // 长度为2

			Log.e(TAG, "b_ASCII len:"+b_ASCII.length); // 长度为1
			Log.e(TAG, "b_ISO_8859_1 len:"+b_ISO_8859_1.length); // 长度为1

			Log.e(TAG, "b_UTF_8 len:"+b_UTF_8.length); // 长度为3
			Log.e(TAG, "b_UTF_16 len:"+b_UTF_16.length); // 长度为4
			Log.e(TAG, "b_UTF_32 len:"+b_UTF_32.length); // 长度为8
			Log.e(TAG, "b_UNICODE len:"+b_UNICODE.length); // 长度为4

			String text = new String(b_UTF_32, Charset.UTF_16);
			Log.e(TAG, "text:"+text);
			Log.e(TAG, "len a:"+text.length()); // 长度为1
			Log.e(TAG, "len b:"+text.getBytes().length); // 长度为3 --- 采用系统编码UTF-8
			Log.e(TAG, "GBK len b:"+text.getBytes(Charset.GBK).length); // 长度为1
			Log.e(TAG, "GB2312 len b:"+text.getBytes(Charset.GB2312).length); //  长度为1
			Log.e(TAG, "UTF_8 len b:"+text.getBytes(Charset.UTF_8).length); // 长度为3
			Log.e(TAG, "UTF_16 len b:"+text.getBytes(Charset.UTF_16).length); // 长度为4
			Log.e(TAG, "UTF_32 len b:"+text.getBytes(Charset.UTF_32).length); // 长度为8
			Log.e(TAG, "UNICODE len b:"+text.getBytes(Charset.UNICODE).length); // 长度为4

		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * 打印
	 * 
	 * 04-03 11:58:21.909: I/Charset(8020): 系统默认String字符集是displayName(跟locale有关):UTF-8
04-03 11:58:21.910: I/Charset(8020): 系统默认String字符集是name:UTF-8
04-03 11:58:21.911: I/Charset(8020): isSupported(ASCII):true
04-03 11:58:21.911: I/Charset(8020): isSupported(US_ASCII):true
04-03 11:58:21.912: I/Charset(8020): isSupported(ISO_8859_1):true
04-03 11:58:21.913: I/Charset(8020): isSupported(UTF_8):true
04-03 11:58:21.915: I/Charset(8020): isSupported(UTF_16):true
04-03 11:58:21.917: I/Charset(8020): isSupported(UTF_16BE):true
04-03 11:58:21.919: I/Charset(8020): isSupported(UTF_16LE):true
04-03 11:58:21.920: I/Charset(8020): isSupported(UTF_32):true
04-03 11:58:21.922: I/Charset(8020): isSupported(UNICODE):true
04-03 11:58:21.926: I/Charset(8020): isSupported(GBK):true
04-03 11:58:21.927: I/Charset(8020): isSupported(GB2312):true
04-03 11:58:21.930: I/Charset(8020): isSupported(BIG5):true
04-03 11:58:21.935: I/Charset(8020): isSupported(GB18030):true
04-03 11:58:21.937: I/Charset(8020): isSupported(MBCS):false

	 */
	public void print() {
		Log.i(TAG, "系统默认String字符集是displayName(跟locale有关):"+java.nio.charset.Charset.defaultCharset().displayName());
		Log.i(TAG, "系统默认String字符集是name:"+java.nio.charset.Charset.defaultCharset().name());

		Log.i(TAG, "isSupported(ASCII):"+java.nio.charset.Charset.isSupported(ASCII));
		Log.i(TAG, "isSupported(US_ASCII):"+java.nio.charset.Charset.isSupported(US_ASCII));
		Log.i(TAG, "isSupported(ISO_8859_1):"+java.nio.charset.Charset.isSupported(ISO_8859_1));

		Log.i(TAG, "isSupported(UTF_8):"+java.nio.charset.Charset.isSupported(UTF_8));
		Log.i(TAG, "isSupported(UTF_16):"+java.nio.charset.Charset.isSupported(UTF_16));
		Log.i(TAG, "isSupported(UTF_16BE):"+java.nio.charset.Charset.isSupported(UTF_16BE));
		Log.i(TAG, "isSupported(UTF_16LE):"+java.nio.charset.Charset.isSupported(UTF_16LE));
		Log.i(TAG, "isSupported(UTF_32):"+java.nio.charset.Charset.isSupported(UTF_32));
		Log.i(TAG, "isSupported(UNICODE):"+java.nio.charset.Charset.isSupported(UNICODE));

		Log.i(TAG, "isSupported(GBK):"+java.nio.charset.Charset.isSupported(GBK));
		Log.i(TAG, "isSupported(GB2312):"+java.nio.charset.Charset.isSupported(GB2312));
		Log.i(TAG, "isSupported(BIG5):"+java.nio.charset.Charset.isSupported(BIG5));
		Log.i(TAG, "isSupported(GB18030):"+java.nio.charset.Charset.isSupported(GB18030));

		Log.i(TAG, "isSupported(MBCS):"+java.nio.charset.Charset.isSupported("MBCS"));

	}

}
