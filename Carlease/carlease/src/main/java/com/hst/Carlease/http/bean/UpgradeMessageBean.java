package com.hst.Carlease.http.bean;


public class UpgradeMessageBean {
	private String msg;
	private int statu;
	private UpgradeMessage model;
	public class UpgradeMessage{
		private String VersionLink;	//http://192.168.60.211:6211/Android/20170105/CarLease-release.apk
		private String 	VersionNo;//	V1.1.0.0
		public String getVersionLink() {
			return VersionLink;
		}
		public void setVersionLink(String versionLink) {
			VersionLink = versionLink;
		}
		public String getVersionNo() {
			return VersionNo;
		}
		public void setVersionNo(String versionNo) {
			VersionNo = versionNo;
		}
		
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
	public UpgradeMessage getModel() {
		return model;
	}
	public void setModel(UpgradeMessage model) {
		this.model = model;
	}
	
	
	
}
