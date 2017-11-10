package com.hst.Carlease.ram;

/**
 * 信息管理里的大分类
 * 
 * 1.0版本
Type为1时，其他的推送消息
Type为2时，结算界面

 * @author LMC
 *
 */
public class PushTypeRam {

	/**
	 * 其他
	 * 
	 * @return
	 */
	public static int getAlert() {
		return 1;
	}

	/**
	 * 结算
	 * 
	 * @return
	 */
	public static int getViolation() {
		return 2;
	}

	

}