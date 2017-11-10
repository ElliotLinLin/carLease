package com.hst.Carlease.http.bean;




public class InsertOrderBean {
	
	private String CarModelID;//车型ID
	private String CarModelName;//车型名称
	private String immFamName;//直属亲属
	private String CustName;//客户名称
	private String CredentialsNum;//身份证号
	private String Mobile;//手机号
	private String immFamRelation;//直系亲属关系
	private String immFamMobile;//直系亲属电话
	private String tokenID;//
	private String Source;//来源 1:ERP平台  2:android、3:IOS、4:微信
	private String OpenId;//微信的Id
	private String MaritalStatus;//婚姻状态
	private String file1;//照片
	private String file2;//照片
	private String orderArea;//订单区域标识
	private String carArea;//提车城市
	private String hcmID     ;
	private String companyid;
	private String BusName;
	private String driveAttach;//驾驶证
	private String SixBankAttach;//六个月银行流水
	private String ThreePhoneAttach;//三个月通话清单
	
	public String getDriveAttach() {
		return driveAttach;
	}
	public void setDriveAttach(String driveAttach) {
		this.driveAttach = driveAttach;
	}
	public String getSixBankAttach() {
		return SixBankAttach;
	}
	public void setSixBankAttach(String sixBankAttach) {
		SixBankAttach = sixBankAttach;
	}
	public String getThreePhoneAttach() {
		return ThreePhoneAttach;
	}
	public void setThreePhoneAttach(String threePhoneAttach) {
		ThreePhoneAttach = threePhoneAttach;
	}
	public String getCarModelID() {
		return CarModelID;
	}
	public void setCarModelID(String carModelID) {
		CarModelID = carModelID;
	}
	public String getHcmID() {
		return hcmID;
	}
	public void setHcmID(String hcmID) {
		this.hcmID = hcmID;
	}
	public String getCompanyid() {
		return companyid;
	}
	public void setCompanyid(String companyid) {
		this.companyid = companyid;
	}
	public String getBusName() {
		return BusName;
	}
	public void setBusName(String busName) {
		BusName = busName;
	}
	public String getCarModelName() {
		return CarModelName;
	}
	public void setCarModelName(String carModelName) {
		CarModelName = carModelName;
	}
	public String getImmFamName() {
		return immFamName;
	}
	public void setImmFamName(String immFamName) {
		this.immFamName = immFamName;
	}
	public String getCustName() {
		return CustName;
	}
	public void setCustName(String custName) {
		CustName = custName;
	}
	public String getCredentialsNum() {
		return CredentialsNum;
	}
	public void setCredentialsNum(String credentialsNum) {
		CredentialsNum = credentialsNum;
	}
	public String getMobile() {
		return Mobile;
	}

	public String getFile1() {
		return file1;
	}
	public void setFile1(String file1) {
		this.file1 = file1;
	}
	public String getFile2() {
		return file2;
	}
	public void setFile2(String file2) {
		this.file2 = file2;
	}
	public void setMobile(String mobile) {
		Mobile = mobile;
	}
	public String getImmFamRelation() {
		return immFamRelation;
	}
	public void setImmFamRelation(String immFamRelation) {
		this.immFamRelation = immFamRelation;
	}
	public String getImmFamMobile() {
		return immFamMobile;
	}
	public void setImmFamMobile(String immFamMobile) {
		this.immFamMobile = immFamMobile;
	}
	public String getTokenID() {
		return tokenID;
	}
	public void setTokenID(String tokenID) {
		this.tokenID = tokenID;
	}
	public String getSource() {
		return Source;
	}
	public void setSource(String source) {
		Source = source;
	}
	public String getOpenId() {
		return OpenId;
	}
	public void setOpenId(String openId) {
		OpenId = openId;
	}
	public String getMaritalStatus() {
		return MaritalStatus;
	}
	public void setMaritalStatus(String maritalStatus) {
		MaritalStatus = maritalStatus;
	}
	public String getOrderArea() {
		return orderArea;
	}
	public void setOrderArea(String orderArea) {
		this.orderArea = orderArea;
	}
	public String getCarArea() {
		return carArea;
	}
	public void setCarArea(String carArea) {
		this.carArea = carArea;
	}
	
}
