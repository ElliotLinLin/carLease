package com.hst.Carlease.http.bean;


public class GetOrderListBean {
	private String tokenID;//tokenID 
	private int hpid;//订单标识ID
	private int parentid;

	public String getTokenID() {
		return tokenID;
	}

	public void setTokenID(String tokenID) {
		this.tokenID = tokenID;
	}

	public int getHpid() {
		return hpid;
	}

	public void setHpid(int hpid) {
		this.hpid = hpid;
	}

	public int getParentid() {
		return parentid;
	}

	public void setParentid(int parentid) {
		this.parentid = parentid;
	}
	
}
