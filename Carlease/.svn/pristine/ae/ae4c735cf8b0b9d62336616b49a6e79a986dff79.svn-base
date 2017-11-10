package com.tools.content.res;

import android.content.Context;

import com.tools.util.Log;

/**
 * 资源管理器
 * 
 * 例子：
 * 	ResManager res = new ResManager(context);
		String text = res.parseString(R.array.tools_http_status_code, 400254, R.array.tools_http_status_caption);
		Log.e(TAG, "text11111:"+text);
		
		text = res.parseString(R.array.tools_http_status_code, 400, R.array.tools_http_status_caption);
		Log.e(TAG, "text2222:"+text);
		
		int int__ = res.parseInteger(R.array.tools_http_status_caption, "ddd", R.array.tools_http_status_code);
		Log.e(TAG, "text11111:"+int__);
		
		int__ = res.parseInteger(R.array.tools_http_status_caption, "已接受", R.array.tools_http_status_code);
		Log.e(TAG, "text22222:"+int__);
 * 
 * @author LMC
 *
 */
public class ResManager {

	private static final String TAG = ResManager.class.getSimpleName();

	protected Context context = null;

	public ResManager(Context context) {
		init(context);
	}

	private void init(Context context) {
		this.context = context;
	}

	/**
	 * 由整数数组的值得么对应字符串数组的值
	 * 
	 * @param integerArrayResId
	 * @param integerValue
	 * @param stringArrayResId
	 * @return
	 */
	public String parseString(int integerArrayResId, int integerValue, int stringArrayResId) {

		// 得到整数数组
		int[] integerArray = context.getResources().getIntArray(integerArrayResId);
		if (integerArray == null || integerArray.length <= 0) {
			return null;
		}

		// 得到索引
		int index = -1;
		int integerArrayLen = integerArray.length;
		for (int n = 0; n < integerArrayLen; n++) {
			if (integerValue == integerArray[n]) {
				index = n;
				break;
			}
		}
		Log.i(TAG, "index:"+index);
		if (index < 0) {
			return null;
		}
		
		// 得到字符串数组
		String[] stringArray = context.getResources().getStringArray(stringArrayResId);
		if (stringArray == null || stringArray.length <= 0) {
			return null;
		}

		return stringArray[index];
	}

	/**
	 * 由字符串数组的值得到对应的整数数组的值
	 * 
	 * @param stringArrayResId
	 * @param stringValue
	 * @param integerArrayResId
	 * @return
	 */
	public int parseInteger(int stringArrayResId, String stringValue, int integerArrayResId) {

		// 得到字符串数组
		String[] stringArray = context.getResources().getStringArray(stringArrayResId);
		if (stringArray == null || stringArray.length <= 0) {
			return -1;
		}

		// 得到索引
		int index = -1;
		int stringArrayLen = stringArray.length;
		for (int n = 0; n < stringArrayLen; n++) {
			if (stringArray[n].equals(stringValue)) {
				index = n;
				break;
			}
		}
		Log.i(TAG, "index:"+index);
		if (index < 0) {
			return -1;
		}

		// 得到整数数组
		int[] integerArray = context.getResources().getIntArray(integerArrayResId);
		if (integerArray == null || integerArray.length <= 0) {
			return -1;
		}

		return integerArray[index];
	}

}
