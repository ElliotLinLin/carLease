package com.hst.Carlease.ram;

/**
 * 本地参数的key值
 * 
 * @author HL
 * 
 */
public class ShareSet {

	private static final String name = "set";

	public static String getName() {
		return name;
	}

	public static String KeyTokeTime() {
		return "KeyTokeTime";
	}

	public static String KeyTokeString() {
		return "KeyTokeString";
	}

	public static String KeyAddressString() {
		return "KeyAddressString";
	}
	
	public static String KeyTakeCarStatus(){
		return "KeyTakeCarStatus";
	}
	
	/**
	 * 通知ID
	 * @return
	 */

	public static String KeyLoginSuccessString() {
		return "KeyLoginSuccessString";
	}

	/**
	 * 通知ID
	 * @return
	 */
	public static String keyNotifedID() {
		return "notifed_id";
	}

}
