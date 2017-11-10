package com.hst.Carlease.http.bean;

import java.util.ArrayList;

/**
 * Author:lsh
 * Version: 1.0
 * Description:
 * Date: 2017/8/16
 */

public class PurchaseParamsBean {



    public int hcmID;/// 租购套餐标识id
    public int carModelID;  /// 车型Id
    public String carModelName = ""; /// 车型名称

    public String customerName = "" ;/// 客户名称
    public String credentialsNum= ""; /// 客户身份证

    public String mobilePhone= "";/// 本人电话
    public int companyId; /// 业务网点（分公司或门店id）

    public String comAddress= "";// 单位住址
    public int liveAreaID;// 现居住地区域

    public String liveAddress= "";// 现居住地详细地址
    public int maritalStatus;// 婚姻状况

    public String emergyName= "";// 紧急联系人姓名

    public String emergMobile= "";// 紧急联系人电话

    public int emeryRelation;// 紧急联系人与本人的关系

    public String immFamName= "";// 直系亲属的姓名
    public String immFamMobile= "";// 直系亲属电话

    public String immFamRelation= "";// 直系亲属与本人的关系

    public int bankId;// 银行名称ID

    public String AccountName= "";// 开户名称


    public String bankNum= "";// 银行卡号

    public String branchName= "";// 支行名称

    public String DriveFirstDate= "";// 驾驶证初次领证日期


    public String credentialsAddr = "";//身份证地址

    public ArrayList purchaseFiles= null;//附件

    public ArrayList getPurchaseFiles() {
        return purchaseFiles;
    }

    public void setPurchaseFiles(ArrayList purchaseFiles) {
        this.purchaseFiles = purchaseFiles;
    }

    public String getCredentialsAddr() {
        return credentialsAddr;
    }

    public void setCredentialsAddr(String credentialsAddr) {
        this.credentialsAddr = credentialsAddr;
    }

    public int getHcmID() {
        return hcmID;
    }

    public void setHcmID(int hcmID) {
        this.hcmID = hcmID;
    }

    public int getCarModelID() {
        return carModelID;
    }

    public void setCarModelID(int carModelID) {
        this.carModelID = carModelID;
    }

    public String getCarModelName() {
        return carModelName;
    }

    public void setCarModelName(String carModelName) {
        this.carModelName = carModelName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCredentialsNum() {
        return credentialsNum;
    }

    public void setCredentialsNum(String credentialsNum) {
        this.credentialsNum = credentialsNum;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public String getComAddress() {
        return comAddress;
    }

    public void setComAddress(String comAddress) {
        this.comAddress = comAddress;
    }

    public int getLiveAreaID() {
        return liveAreaID;
    }

    public void setLiveAreaID(int liveAreaID) {
        this.liveAreaID = liveAreaID;
    }

    public String getLiveAddress() {
        return liveAddress;
    }

    public void setLiveAddress(String liveAddress) {
        this.liveAddress = liveAddress;
    }

    public int getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(int maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getEmergyName() {
        return emergyName;
    }

    public void setEmergyName(String emergyName) {
        this.emergyName = emergyName;
    }

    public String getEmergMobile() {
        return emergMobile;
    }

    public void setEmergMobile(String emergMobile) {
        this.emergMobile = emergMobile;
    }

    public int getEmeryRelation() {
        return emeryRelation;
    }

    public void setEmeryRelation(int emeryRelation) {
        this.emeryRelation = emeryRelation;
    }

    public String getImmFamName() {
        return immFamName;
    }

    public void setImmFamName(String immFamName) {
        this.immFamName = immFamName;
    }

    public String getImmFamMobile() {
        return immFamMobile;
    }

    public void setImmFamMobile(String immFamMobile) {
        this.immFamMobile = immFamMobile;
    }

    public String getImmFamRelation() {
        return immFamRelation;
    }

    public void setImmFamRelation(String immFamRelation) {
        this.immFamRelation = immFamRelation;
    }

    public int getBankId() {
        return bankId;
    }

    public void setBankId(int bankId) {
        this.bankId = bankId;
    }

    public String getAccountName() {
        return AccountName;
    }

    public void setAccountName(String accountName) {
        AccountName = accountName;
    }

    public String getBankNum() {
        return bankNum;
    }

    public void setBankNum(String bankNum) {
        this.bankNum = bankNum;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getDriveFirstDate() {
        return DriveFirstDate;
    }

    public void setDriveFirstDate(String driveFirstDate) {
        DriveFirstDate = driveFirstDate;
    }
}
