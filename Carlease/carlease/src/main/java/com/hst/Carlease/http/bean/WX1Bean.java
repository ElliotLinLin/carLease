package com.hst.Carlease.http.bean;

/**
 * 微信支付
 * 
 * @author wzy 2016年5月9日
 * 
 */
public class WX1Bean {
	// 提交方式：get
	// 参数：orderCode：订单号 string
	// customerId：登录用户ID string

	private String orderCode;
	private String customerId;

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

}
