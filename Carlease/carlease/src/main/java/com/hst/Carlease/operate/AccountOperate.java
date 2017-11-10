package com.hst.Carlease.operate;


import java.util.List;

import android.database.SQLException;

import com.hst.Carlease.sqlite.bean.UserInforDb;
import com.tools.sqlite.SQLiteSingle;
import com.tools.sqlite.SQLiteTable;
import com.tools.util.Log;


/**
 * 账号操作类
 *
 * @author LMC
 *
 */
public class AccountOperate {

	private static final String TAG = AccountOperate.class.getSimpleName();

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
	 * 得到账号信息（读第一 条）
	 * 
	 * @return
	 */
	public static UserInforDb get() {
		// 读一条
		List<UserInforDb> list = SQLiteSingle.getInstance().query(UserInforDb.class, 1);
		if (list != null) {
			if (list.size() > 0) {
				return list.get(0);
			}
		}
		return null;
	}
	
	

	/**
	 * 得到某个账号的信息（读第一 条）
	 * 
	 * @param account
	 * @return
	 */
	public static UserInforDb get(String account) {
		String where = String.format("select * from %s where LoginName = '%s';", SQLiteTable.getTableName(UserInforDb.class), account);
		List<UserInforDb> list = SQLiteSingle.getInstance().query(UserInforDb.class, where);
		if (list != null) {
			if (list.size() > 0) {
				return list.get(0);
			}
		}
		return null;
	}

	/**
	 * 保存账号信息（表里只能保存一条数据）
	 * 
	 * @param accountInfo
	 * @param columnForceUpdate
	 */
	public static void set(UserInforDb accountInfo, String... columnForceUpdate) {
		if (accountInfo == null) {
			return;
		}

		// 如果表为空，则插入，否则更新
		boolean isEmpty = SQLiteSingle.getInstance().isEmptyTable(UserInforDb.class);
		if (isEmpty) {
			// 表为空，则插入数据。
			Log.e(TAG, "set 表为空，则插入数据。 insert...");
			SQLiteSingle.getInstance().insert(accountInfo);
		} else {
			// 表有数据，则更新数据
			Log.e(TAG, "set 表有数据，则更新数据 update...");
			SQLiteSingle.getInstance().update(accountInfo, null, columnForceUpdate);
		}
	}

	/**
	 * 保存账号信息（表里只能保存一条数据）
	 * 
	 * @param accountName
	 * @param accountInfo
	 * @param columnForceUpdate
	 */
	public static void set(String accountName, UserInforDb accountInfo, String... columnForceUpdate) {
		if (accountInfo == null) {
			return;
		}

		accountInfo.setLoginName(accountName);

		set(accountInfo, columnForceUpdate);

		//		String where = String.format("select * from %s where Account = '%s';", SQLiteTable.getTableName(AccountInfo.class), accountName);
		//
		//		// 如果表为空，则插入，否则更新
		//		boolean isEmpty = SQLiteSingle.getInstance().isEmpty(AccountInfo.class, where);
		//		if (isEmpty) {
		//			// // 表为空，则插入数据。
		//			Log.e(TAG, "set 表为空，则插入数据。 insert...");
		//			SQLiteSingle.getInstance().insert(accountInfo);
		//		} else {
		//			// 表有数据，则更新数据
		//			Log.e(TAG, "set 表有数据，则更新数据 update...");
		//			SQLiteSingle.getInstance().update(accountInfo, null, columnForceUpdate);
		//		}
		
	}

	/**
	 * 删除账号信息（清空表）
	 */
	public static void delete() {
		SQLiteSingle.getInstance().deleteTable(UserInforDb.class);
	}

	/**
	 * 删除账号信息
	 */
	public static void delete(UserInforDb accountInfo) {
		if (accountInfo == null) {
			return;
		}

		String where = String.format("LoginName = '%s'", accountInfo.getLoginName());

		SQLiteSingle.getInstance().delete(UserInforDb.class, where);
	}

	/**
	 * 得到此账号的UserInfo
	 *
	 * @param account
	 * @return
	 */
		public static UserInforDb getUserInfo(String account) {
			if (isEmptyString(account)) {
				return null;
			}
			String sql = String.format("select * from UserInfo where LoginName = '%s';", account);
			try {
				List<UserInforDb> userInfo = SQLiteSingle.getInstance().query(sql, 1, UserInforDb.class);
				if (userInfo != null && userInfo.size() > 0) {
					return userInfo.get(0);
				}
			} catch (SQLException e) {
				return null;
			}
			return null;
		}

	/**
	 * 更新此账号的数据
	 *
	 * @param account
	 * @param userInfo
	 * @return
	 */
	//	public static void setUserInfo(String account, UserInfo userInfo, String... columnForceUpdate) {
	//		if (isEmptyString(account)) {
	//			return;
	//		}
	//
	//		if (userInfo == null) {
	//			return;
	//		}
	//
	//		//userInfo.setAccount(account);
	//
	//		// 账号不区分大小写）
	//		//		account = account.toLowerCase();
	//
	//		//		String sql = String.format("select * from UserInfo where account = '%s';", account);
	//		//		int rowCount = SQLiteSingle.getInstance().getRowCount(sql);
	//		String where = String.format("account = '%s'", account);
	//		boolean isEmpty = SQLiteSingle.getInstance().isEmpty(UserInfo.class, where);
	//		if (isEmpty) {
	//			// 没有数据时，则插入。
	//			Log.e(TAG, "setUserInfo  无数据，则insert...");
	//			//			UserInfo userInfo = new UserInfo();
	//			//			userInfo.setAccount(account);
	//			//			userInfo.setPassword(password);
	//			//			sql = String.format("INSERT INTO UserInfo(account, password) VALUES('%s','%s');", account, password);
	//			// 保存push时间
	////			String pushStartTime = DatetimeUtil.getCurrent("yyyy-MM-dd HH:mm:ss");
	////			userInfo.setPushStartTime(pushStartTime);
	////			SQLiteSingle.getInstance().insert(userInfo);
	//		}else {
	//			Log.e(TAG, "setUserInfo  已有数据时，则更新。");
	//			// 已有数据时，则更新。
	//			//			UserInfo userInfo = new UserInfo();
	//			//			userInfo.setPassword(password);
	//			//			sql = String.format("update UserInfo set password = '%s' where account = '%s';", password, account);
	//			//			String where = String.format("account = '%s'", account);
	//			SQLiteSingle.getInstance().update(userInfo, where, columnForceUpdate);
	//		}
	//	}

	/**
	 * 得到当前UserInfo
	 *
	 * @return
	 */
		public static UserInforDb getCurrentUserInfo() {
			return get(LoginOperate.getCurrentAccount() );
		}
	//
	//	/**
	//	 * 判断账号是否为自动登录
	//	 *
	//	 * @param account
	//	 * @return
	//	 */
	//	public static boolean isAutoLogin(String account) {
	//		Account userInfo = getCurrentUserInfo();
	//		if (userInfo != null) {
	//			return userInfo.isAutoLogin();
	//		}
	//		return false;
	//	}
	//
	//	/**
	//	 * 判断当前账号是否为自动登录
	//	 *
	//	 * @return
	//	 */
	//	public static boolean isCurrentAutoLogin() {
	//		return isAutoLogin( LoginOperate.getCurrentAccount() );
	//	}
	//
	//	/**
	//	 * 判断账号是否存在
	//	 *
	//	 * @param account
	//	 * @param password
	//	 */
	//	public static boolean isAccountExists(String account) {
	//		if (isEmptyString(account)) {
	//			return false;
	//		}
	//
	//		// 账号不区分大小写）
	//		account = account.toLowerCase();
	//
	//		boolean isAccountExists = false;
	//
	//		//		String sql = String.format("select * from UserInfo where account = '%s';", account);
	//		//		int rowCount = SQLiteSingle.getInstance().getRowCount(sql);
	//		String where = String.format("account = '%s'", account);
	//		boolean isEmpty = SQLiteSingle.getInstance().isEmpty(Account.class, where);
	//		if (isEmpty == false) {
	//			//		if (rowCount > 0) {
	//			isAccountExists = true;
	//		}
	//		//		SQLiteSingle.getInstance().execSQL(sql);
	//		return isAccountExists;
	//	}

}
