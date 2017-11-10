package com.hst.Carlease.sqlite.bean;

import com.tools.sqlite.annotation.Column;

/**
 * 账号信息
 *
 * @author LMC
 */
public class AccountInfo {

	// 账号区分大小写
	// @Column(key = true, notNull = true, nocase = true, lower = true)
	@Column(key = true, notNull = true)
	// 不区分大小写
	protected String Account; // 账号

	@Column(index = 1)
	protected String Password; // 密码

	@Column(index = 2)
	protected boolean isAutoLogin; // 是否自动登录

	protected int UserID; // 可不要
	protected String UserName; // 姓名
	protected String Telephone;
	protected String Email; // 可不要
	protected String Certificate;//身份证
	protected String PreferenceArea;
	protected String PreferenceModel;
	protected String token;//设备号，
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public boolean isAutoLogin() {
		return isAutoLogin;
	}

	public void setAutoLogin(boolean isAutoLogin) {
		this.isAutoLogin = isAutoLogin;
	}

	public String getAccount() {
		return Account;
	}

	public void setAccount(String account) {
		Account = account;
	}

	public String getPassword() {
		return Password;
	}

	public void setPassword(String password) {
		Password = password;
	}

	public int getUserID() {
		return UserID;
	}

	public void setUserID(int userID) {
		UserID = userID;
	}

	public String getUserName() {
		return UserName;
	}

	public void setUserName(String userName) {
		UserName = userName;
	}

	public String getTelephone() {
		return Telephone;
	}

	public void setTelephone(String telephone) {
		Telephone = telephone;
	}

	public String getEmail() {
		return Email;
	}

	public void setEmail(String email) {
		Email = email;
	}

	public String getCertificate() {
		return Certificate;
	}

	public void setCertificate(String certificate) {
		Certificate = certificate;
	}

	public String getPreferenceArea() {
		return PreferenceArea;
	}

	public void setPreferenceArea(String preferenceArea) {
		PreferenceArea = preferenceArea;
	}

	public String getPreferenceModel() {
		return PreferenceModel;
	}

	public void setPreferenceModel(String preferenceModel) {
		PreferenceModel = preferenceModel;
	}

}
