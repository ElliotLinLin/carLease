package com.tools.content;

import android.content.Intent;
import android.net.Uri;

/**
 * 
 * 
 * @author LMC
 *
 */
public class IntentUtil {

	private static final String TAG = IntentUtil.class.getSimpleName();

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
	 * 拨打电话，调用拨号程序: 
	 * 
	 * @param telNumber
	 * @return
	 */
	public static Intent createTel(String telNumber) {
		if(isEmptyString(telNumber)) {
			return null;
		}
		String tel = String.format("tel:%s", telNumber);
		Uri uri = Uri.parse( tel );
		Intent intent = new Intent(Intent.ACTION_DIAL, uri);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // 要加的
		return intent;
	}

}
