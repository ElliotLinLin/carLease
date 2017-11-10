package com.hst.Carlease.http.bean;

/**
 * Author:lsh
 * Version: 1.0
 * Description:
 * Date: 2017/8/17
 */

public class NewInsertOrderBean {


    private int Source;

    private String OpenId;
    private String tokenID;
    private String PurchaseParams;
    private String file1;
    private String file2;
    private String driveAttach;
    private String SixBankAttach;
    private String ThreePhoneAttach ;


    public int getSource() {
        return Source;
    }

    public void setSource(int source) {
        Source = source;
    }

    public String getOpenId() {
        return OpenId;
    }

    public void setOpenId(String openId) {
        OpenId = openId;
    }

    public String getTokenID() {
        return tokenID;
    }

    public void setTokenID(String tokenID) {
        this.tokenID = tokenID;
    }

    public String getPurchaseParams() {
        return PurchaseParams;
    }

    public void setPurchaseParams(String purchaseParams) {
        PurchaseParams = purchaseParams;
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
}
