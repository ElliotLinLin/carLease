package com.hst.Carlease.http.bean;

import java.util.List;

public class BrandOfficeBean {
private List<BrandOffice>	model;//	Array
private String	msg	;//成功获取
private int	statu	;//1
public List<BrandOffice> getModel() {
	return model;
}
public void setModel(List<BrandOffice> model) {
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
