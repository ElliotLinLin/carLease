package com.hst.Carlease.operate;


import com.hst.Carlease.sqlite.bean.UserInforDb;
import com.tools.sqlite.SQLiteSingle;
import com.tools.util.Log;



/**
 * 登录操作类
 *
 * @author LMC
 *
 */
public class LoginOperate {

	private static final String TAG = LoginOperate.class.getSimpleName();

	// 是否登录成功（保存在内存中）
	private static boolean isLoginSuccesssed = false;


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
	 * 判断是否登录成功
	 *
	 * @return
	 */
	public static boolean isLoginSuccesssed() {
		Log.e(TAG, "isLoginSuccesssed():"+isLoginSuccesssed);
		return isLoginSuccesssed;
	}

	/**
	 * 设置登录成功标识
	 *
	 * @param isLoginSuccesssed
	 */
	public static void setLoginSuccesssed(boolean isLoginSuccesssed) {
		Log.e(TAG, "setLoginSuccesssed():"+isLoginSuccesssed);
		LoginOperate.isLoginSuccesssed = isLoginSuccesssed;
	}

	/**
	 * 得到当前登录账号
	 *
	 * @return
	 */
		public static String getCurrentAccount() {
			String account = SQLiteSingle.getInstance().queryRowString(UserInforDb.class.getSimpleName(), "account");
			Log.e(TAG, "getCurrentAccount():"+account);
			return account;
		}

	/**
	 * 保存当前登录账号
	 *
	 * @return
	 */
		public static void setCurrentAccount(String account) {
			Log.e(TAG, "setCurrentAccount():"+account);

			if (isEmptyString(account)) {
				return;
			}


			UserInforDb login = new UserInforDb();
			login.setLoginName(account.trim()); // 去掉空格
			
			boolean isEmpty = SQLiteSingle.getInstance().isEmptyTable(UserInforDb.class);
			if (isEmpty == true) {
				SQLiteSingle.getInstance().insert(login);
			}else {
				SQLiteSingle.getInstance().update(login, null);
			}
			Log.e(TAG, "setCurrentAccount()--- end...");
		}
		}