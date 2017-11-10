package com.hst.Carlease.operate;

import com.hst.Carlease.ram.ShareSet;
import com.hst.Carlease.share.ShareSingle;

/**
 * 保存本地参数
 * 
 * @author HL
 * 
 */
public class ShareOperate {

	/**
	 * 获取token获取时间
	 * 
	 * @return
	 */
	public static long getTokenTime() {
		return ShareSingle.getInstance().getLong(ShareSet.KeyTokeTime(), 0);
	}

	/**
	 * 保存token获取时间
	 * 
	 * @return
	 */
	public static void putTokenTime(long tokenTime) {
		ShareSingle.getInstance().putLong(ShareSet.KeyTokeTime(), tokenTime);
	}

	/**
	 * 获取token
	 * 
	 * @return
	 */
	public static String getTokenString() {
		return ShareSingle.getInstance()
				.getString(ShareSet.KeyTokeString(), "");
	}

	/**
	 * 保存token
	 * 
	 * @return
	 */
	public static void putTokenString(String token) {
		ShareSingle.getInstance().putString(ShareSet.KeyTokeString(), token);
	}

	
	/**
	 * 获取是否取车成功
	 * @return
	 */
	public static boolean getTakeCarStatus(){
		return ShareSingle.getInstance().getBoolean(ShareSet.KeyTakeCarStatus(), false);
	}
	
	/**
	 * 保存取车状态
	 * @return
	 */
	public static void putTakeCarStatus(boolean bool){
		ShareSingle.getInstance().putBoolean(ShareSet.KeyTakeCarStatus(), bool);
	}

	/**
	 * 获取登录是否成功
	 * 
	 * @return
	 */
	public static boolean isLoginSuccess() {
		return ShareSingle.getInstance().getBoolean(
				ShareSet.KeyLoginSuccessString(), false);
	}

	/**
	 * 保存登陆是否成功
	 * 
	 * @return
	 */
	public static void putLoginSuccess(boolean login) {
		ShareSingle.getInstance().putBoolean(ShareSet.KeyLoginSuccessString(),
				login);
	}
	
	/**
	 * 保存图片路径
	 * 
	 * @return
	 */
	public static void setPicAddress(String address) {
		ShareSingle.getInstance().putString(ShareSet.KeyAddressString(),
				address);
	}
	
	/**
	 * 获取图片路径
	 * 
	 * @return
	 */
	public static String getPicAddress() {
		return ShareSingle.getInstance().getString(ShareSet.KeyAddressString(),"");
	}
}
