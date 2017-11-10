package com.hst.Carlease.sqlite.bean;

import com.tools.sqlite.annotation.Column;

/**
 * 账号信息
 *
 * @author LMC
 */
public class UserInforDb {


	// 账号区分大小写
	 @Column(key = true, notNull = true, nocase = true, lower = true)
	protected String LoginName; // 账号

	@Column(index = 1)
	protected String Password; // 密码
	
	@Column(index = 2)
	protected String Token; //Md5加密的设备号、用户名、002
	
	public String getToken() {
		return Token;
	}

	public void setToken(String token) {
		Token = token;
	}

	public String getLoginName() {
		return LoginName;
	}

	public void setLoginName(String loginName) {
		LoginName = loginName;
	}

	public String getPassword() {
		return Password;
	}

	public void setPassword(String password) {
		Password = password;
	}


	

	
	



	

}
