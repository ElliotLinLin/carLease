package com.hst.Carlease.http.bean;

public class WeixinPayBean2 {
	private	WeixinPay model;//	Object
private String	msg;//	统一下单成功
private int	statu;//	1
public class WeixinPay{
	private String	appid;//应用id	wx3226ca868a7119ae
	private String	noncestr;//	随机字符串2dcb9de37c73455d914b13409d4f7f99
//	private String package	Sign=WXPay
	private String partnerid	;//商户号1461710002
	private String prepayid	;//预支付id wx20170424111812b7588fb3350483272876
	private String sign	;//签名149D37FAEF35563C769C476ECD783F39
	private String timestamp	;//时间戳1493003893
	public String getAppid() {
		return appid;
	}
	public void setAppid(String appid) {
		this.appid = appid;
	}
	public String getNoncestr() {
		return noncestr;
	}
	public void setNoncestr(String noncestr) {
		this.noncestr = noncestr;
	}
	public String getPartnerid() {
		return partnerid;
	}
	public void setPartnerid(String partnerid) {
		this.partnerid = partnerid;
	}
	public String getPrepayid() {
		return prepayid;
	}
	public void setPrepayid(String prepayid) {
		this.prepayid = prepayid;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	
}
public WeixinPay getModel() {
	return model;
}
public void setModel(WeixinPay model) {
	this.model = model;
}
public String getMsg() {
	return msg;
}
public void setMsg(String msg) {
	this.msg = msg;
}
public int getStatu() {
	return statu;
}
public void setStatu(int statu) {
	this.statu = statu;
}

}
