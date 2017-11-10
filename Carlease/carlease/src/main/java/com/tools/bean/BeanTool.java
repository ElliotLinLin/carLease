package com.tools.bean;

import java.lang.reflect.Field;
import java.util.List;

import org.apache.http.NameValuePair;

import com.tools.net.http.HttpTool;
import com.tools.os.Charset;
import com.tools.util.Log;

/**
 * 专用处理JavaBean
 *
 * 奇怪 "application/x-www-form-urlencoded"不能用，因为参数是编码。
 *
 * 例子：
 * 		VersionBean v = new VersionBean();
		v.setVersionCode(1);
		v.setVersionName("aaa");
		String resultString = BeanTool.toURLEncoder(v, "UTF-8");
		if (DEBUG) Log.i(TAG, "resultString:"+resultString);
 *
 * @author lmc
 * 版权声明：版权归作者所有，未经协议授权不得使用、转载、抄袭等，否则依法追究法律责任和经济责任。
 */
public class BeanTool {

	private static final String TAG = BeanTool.class.getSimpleName();
	private static final boolean D = true;

	public enum Mode {
		NONE, // 什么都不做
		TRIM // 删除两旁空格
	}

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
	 * 将Object实例转成http的URL参数（支持父类） 
	 * 
	 * 默认不删除两旁空格。
	 *
	 * @param object
	 * @param charsetName
	 * @return
	 */
	public static String toURLEncoder(Object object) {
		return toURLEncoder(object, Charset.UTF_8);
	}

	/**
	 * 将Object实例转成http的URL参数（支持父类） 
	 * 
	 * 默认不删除两旁空格。
	 *
	 * @param object
	 * @param charsetName
	 * @return
	 */
	public static String toURLEncoder(Object object, String charsetName) {
		// TODO 下一个项目修改为
		// return toURLEncoder(object, charsetName, Mode.TRIM);
		return toURLEncoder(object, charsetName, Mode.NONE);

		//		if (object == null) {
		//			return null;
		//		}
		//
		//		StringBuffer buffer = new StringBuffer();
		//
		//		Class<?> clazz = object.getClass();
		//		if (clazz == null) {
		//			return null;
		//		}
		//
		//		boolean first = true; // 第一个
		//		while ( clazz != null && clazz.equals(Object.class) == false ) {
		//			String urlParame = toURLEncoder(object, clazz, charsetName);
		//			if (isEmptyString(urlParame) == false) {
		//				// 不是第一个，则追加&
		//				if (first == false) {
		//					buffer.append('&');
		//				}
		//				// 由class和object得到url
		//				buffer.append( urlParame );
		//				//
		//				first = false;
		//			}
		//			// 下一个
		//			clazz = clazz.getSuperclass();
		//		}
		//		//添加验证key
		//		//		buffer.append(getLoginKeyParame());
		//		return buffer.toString();
	}

	/**
	 * 将Object实例转成http的URL参数（支持父类） --- while体
	 *
	 * 支持删除两旁空格功能。 
	 * 
	 * @param object
	 * @param charsetName
	 * @param mode 等于TRIM时，会删除两旁空格功能。
	 * @return
	 */
	public static String toURLEncoder(Object object, String charsetName, Mode mode) {
		if (object == null) {
			return null;
		}

		if (mode == null) {
			return null;
		}

		StringBuffer buffer = new StringBuffer();

		Class<?> clazz = object.getClass();
		if (clazz == null) {
			return null;
		}

		boolean first = true; // 第一个
		while ( clazz != null && clazz.equals(Object.class) == false ) {
			String urlParame = toURLEncoder(object, clazz, charsetName, mode);
			if (isEmptyString(urlParame) == false) {
				// 不是第一个，则追加&
				if (first == false) {
					buffer.append('&');
				}
				// 由class和object得到url
				buffer.append( urlParame );
				//
				first = false;
			}
			// 下一个
			clazz = clazz.getSuperclass();
		}

		return buffer.toString();
	}


	/**
	 * 将Object实例转成http的URL参数（支持父类）
	 *
	 * @param object
	 * @param clazz
	 * @param charsetName
	 * @param mode
	 * @return
	 */
	protected static String toURLEncoder(Object object, Class<?> clazz, String charsetName, Mode mode) {

		if (object == null) {
			return null;
		}

		if (clazz == null) {
			return null;
		}

		if (mode == null) {
			return null;
		}

		Log.e(TAG, "toURLEncoder():classPath:"+clazz.getCanonicalName());

		// 是否需要编码
		boolean bURLEncoder = true;
		if(isEmptyString(charsetName)) {
			bURLEncoder = false;
		}

		// getFields()获得某个类的所有的公共（public）的字段，包括父类。 
		// getDeclaredFields()获得某个类的所有申明的字段，即包括public、private和proteced，
		// 但是不包括父类的申明字段。 
		// 得到属性数组
		Field[] fields = clazz.getDeclaredFields();
		if (fields == null) {
			return null;
		}

		int len = fields.length;
		//		Log.e(TAG, "length:"+len);
		if (len <= 0) {
			return null;
		}

		// new string
		StringBuffer buffer = new StringBuffer();

		boolean first = true; // 第一个

		for(int i = 0 ; i < len; i++) {

			Field field = fields[i];

			field.setAccessible(true); //设置些属性是可以访问的

			try {
				Object objectValue = field.get(object);
				if (objectValue != null) {
					// 不是第一个，则append &
					if (first == false) {
						//						Log.e(TAG, "i:"+i);
						//						Log.e(TAG, "append &....");
						buffer.append('&');
					}
					// append string
					buffer.append(field.getName());
					buffer.append('=');

					String value = objectValue.toString();
					if (mode == Mode.TRIM) {
						// 删除两旁空格功能
						value = value.trim();
					}

					if (bURLEncoder) { // 需要编码
						buffer.append( HttpTool.encode(value, charsetName) );
					}else { // 不需要编码
						buffer.append(value);
					}

					// set first = false
					first = false;
				}
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			// 得到此属性的值
			//			if (DEBUG) Log.i(TAG, "fieldName:"+f.getName()+", fieldValue = "+val);
		}

		String resultString = buffer.toString();
		if (D) Log.e(TAG, "toURLEncoder->resultString:"+resultString);
		return resultString;
	}

	//  TODO 没完成
	public List<NameValuePair> toNameValuePair(Object object, Class<?> clazz, String charset, Mode mode) {
		return null;
	}

}
