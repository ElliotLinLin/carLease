package com.hst.Carlease.http.bean;

public class AlipaypayBean2 {
private String	 token;//客户标识
private int contract;//合同ID
private String subject;//商品名称
private String body;//商品详情
private String total_fee;//总金额
private String goods_type;// 商品类型
private String promo_params;//商户优惠活动
private String extend_params;//业务扩展参数
private String dataFrom;//"1"表示合同支付"2"表示租购定金支付

private String orderNo ;//dataFrom="1 "时，orderNo可为" "；dataFrom="2"时，orderNo为租购合同号 


public String getToken() {
	return token;
}
public void setToken(String token) {
	this.token = token;
}
public int getContract() {
	return contract;
}
public void setContract(int contract) {
	this.contract = contract;
}
public String getSubject() {
	return subject;
}
public void setSubject(String subject) {
	this.subject = subject;
}
public String getBody() {
	return body;
}
public void setBody(String body) {
	this.body = body;
}
public String getTotal_fee() {
	return total_fee;
}
public void setTotal_fee(String total_fee) {
	this.total_fee = total_fee;
}
public String getGoods_type() {
	return goods_type;
}
public void setGoods_type(String goods_type) {
	this.goods_type = goods_type;
}
public String getPromo_params() {
	return promo_params;
}
public void setPromo_params(String promo_params) {
	this.promo_params = promo_params;
}
public String getExtend_params() {
	return extend_params;
}
public void setExtend_params(String extend_params) {
	this.extend_params = extend_params;
}
public String getDataFrom() {
	return dataFrom;
}
public void setDataFrom(String dataFrom) {
	this.dataFrom = dataFrom;
}
public String getOrderNo() {
	return orderNo;
}
public void setOrderNo(String orderNo) {
	this.orderNo = orderNo;
}

}
