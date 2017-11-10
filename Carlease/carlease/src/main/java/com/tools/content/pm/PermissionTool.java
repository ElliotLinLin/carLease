package com.tools.content.pm;


import android.content.Context;
import android.content.pm.PackageManager;

import com.tools.app.Config;
import com.tools.util.Log;
import com.tools.widget.Prompt;


/**
 * 权限检查工具
 * 
 * 例子：
 * Log.e(TAG, "ddd:"+PermissionTool.check(context, android.Manifest.permission.BLUETOOTH));
		Log.e(TAG, "ddd:"+PermissionTool.check(context, android.Manifest.permission.INTERNET));
		Log.e(TAG, "ddd:"+PermissionTool.checkThrow(context, android.Manifest.permission.INTERNET));
		Log.e(TAG, "ddd:"+PermissionTool.checkThrow(context, android.Manifest.permission.BLUETOOTH));

		PermissionTool.checkThrow(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
		PermissionTool.checkThrow(context, android.Manifest.permission.VIBRATE);
 * 
 * 
 * 其中check开头的，只做检查。enforce开头的，不单检查，没有权限的还会抛出异常。
 * Log.e(TAG, "permission:"+context.checkCallingOrSelfPermission(android.Manifest.permission.READ_LOGS));
 * context.enforceCallingOrSelfPermission(android.Manifest.permission.READ_LOGS, "msge == 1");
 * 
 * @author levovo
 *
 */
public class PermissionTool {

	private static final String TAG = PermissionTool.class.getSimpleName();

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
	 * 检查是否有权限
	 * 
	 * @param permission
	 * @return
	 */
	public static boolean check(Context context, String permission) {
		return __check__checkCallingOrSelfPermission(context, permission);
	}

	/**
	 * 检查是否有权限，采用packageManager.checkPermission
	 * 
	 * TODO 4.3版本检查READ_LOGS不正确
	 * 
	 * @param context
	 * @param permission
	 * @return
	 */
	private static boolean __check__checkPermission(Context context, String permission) {
		if (context == null) {
			return false;
		}

		if (isEmptyString(permission)) {
			return false;
		}

		PackageManager packageManager = context.getPackageManager();
		if (packageManager == null) {
			return false;
		}

		// 检查权限
		int result = packageManager.checkPermission(permission, context.getPackageName());
		Log.d(TAG, "result:"+result);

		/*  下述方法可以得到此应用的全部权限
		 * 	try {
			String[] aa = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_PERMISSIONS).requestedPermissions;
			if (aa != null) {
				for (String text : aa) {
					Log.e(TAG, "aaa:"+text);
				}
			}
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		 * */

		boolean isHasPermission = false;

		// PERMISSION_GRANTED == 0 存在
		// PERMISSION_DENIED  == -1 不存在

		if (result == PackageManager.PERMISSION_GRANTED) {
			// 存在
			isHasPermission = true;
		}

		return isHasPermission;
	}

	/**
	 * 检查是否有权限，采用context.checkCallingOrSelfPermission
	 * 
	 * @param context
	 * @param permission
	 * @return
	 */
	private static boolean __check__checkCallingOrSelfPermission(Context context, String permission) {
		if (context == null) {
			return false;
		}

		if (isEmptyString(permission)) {
			return false;
		}

		// 检查权限
		int result = context.checkCallingOrSelfPermission(permission);
		Log.d(TAG, "result:"+result);

		boolean isHasPermission = false;

		// PERMISSION_GRANTED == 0 存在
		// PERMISSION_DENIED  == -1 不存在

		if (result == PackageManager.PERMISSION_GRANTED) {
			// 存在
			isHasPermission = true;
		}

		return isHasPermission;
	}

	/**
	 * 检查是否有权限，如果没有则抛出异常，否则返回boolean
	 * 
	 * @param permission
	 * @return
	 */
	public static boolean checkThrow(Context context, String permission) {

		if (Config.getVersionDevelop() == Config.VersionDevelop.Final) {
			Log.e(TAG, "如果为正式版，就不要checkThrow()了，因不是所有手机都支持系统自带的android.Manifest.permission.");
			return false;
		}

		boolean isHasPermission = false;

		isHasPermission = check(context, permission);
		Log.e(TAG, "isHasPermission:"+isHasPermission);
		
		if (isHasPermission == false) {
			// 没有权限，则抛出异常
			// <uses-permission android:name="android.permission.VIBRATE" />
			String throwMsg = String.format("需要权限："+"<uses-permission android:name=\"%s\" />", permission);
			//			throw new java.lang.SecurityException( throwMsg );
			// 不要主动throw new Exception，因有一些情况，明明有，切检查没有。
			// 采用提示的方式。
//			Log.exception(TAG, throwMsg);
//			Prompt.showError(context, throwMsg);
		}

		return isHasPermission;
	}

}
