package com.tools.util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;

import android.util.Xml;

/**
 * XML解析
 *
 * 例如：
 * 				//从XML获取json
				String resultLogin = XmlDOMUtil.getValue(
						http.doPost(HTTPURL.getLogin(), Charset.convert(urlParame, HttpRam.getTextcharset())),
						HttpRam.getTextcharset());
 *
 * @author Liaojp
 * @date 2014-5-7
 */
@Deprecated
public class XmlDOMUtil {
	private static final String TAG = XmlDOMUtil.class.getSimpleName();

	/**
	 * 从字节数组中取得值
	 * @param bytes xml文件字节数组   charset 字符集
	 * @return 标签中的值
	 */
	public static String getValue(byte[] bytes, String charset) {
		String value = "";
		if(bytes == null){
			Log.e(TAG, "Error. bytes is null.");
			return value;
		}
		InputStream stream = new ByteArrayInputStream(bytes);
		XmlPullParser xmlParse = Xml.newPullParser();
		try {
			// get file stream and set encoding
			xmlParse.setInput(stream, "utf-8");
			// get event type
			int evnType = xmlParse.getEventType();
			// continue to end document
			while (evnType != XmlPullParser.END_DOCUMENT) {
				switch (evnType) {
				case XmlPullParser.START_TAG:
					String tag = xmlParse.getName();
					Log.i(TAG, "START_TAG : " + tag);
					break;
				case XmlPullParser.END_TAG:
					String endTag = xmlParse.getName();
					Log.i(TAG, "END_TAG : " + endTag);
					break;
				case XmlPullParser.TEXT:
					value = xmlParse.getText();
					Log.i(TAG, "TEXT : " + value);
					break;
				default:
					break;
				}
				evnType = xmlParse.next();
			}
		} catch (Exception e) {
			Log.d(TAG, e.toString());
		}

		return value;
	}
}
