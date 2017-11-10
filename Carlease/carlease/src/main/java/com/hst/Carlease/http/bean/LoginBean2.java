package com.hst.Carlease.http.bean;

public class LoginBean2 {

	/**
	 * msg : 登录成功！
	 * statu : 1
	 * model : {"guid":"d14513a2-6290-4911-ab1a-c1959d454c09","isHire":0,"personType":2,"PowerList":null,"companyids":"10028,10292,10297,10298,10299,10300,10303,10304,10305,10308,10309,10311,10312,10313,10314,10318,10319,10323,10324,10325,10328,10329,10330,10338,10341,10342,10343,10344,10381,10382,10386","username":"llz.cs","PositionID":"10485","StaffID":null,"CompanyID":"10028","TypeAdministrator":1}
	 */

	private String msg;
	private int statu;
	private ModelBean model;

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

	public ModelBean getModel() {
		return model;
	}

	public void setModel(ModelBean model) {
		this.model = model;
	}

	public static class ModelBean {
		/**
		 * guid : d14513a2-6290-4911-ab1a-c1959d454c09
		 * isHire : 0
		 * personType : 2
		 * PowerList : null
		 * companyids : 10028,10292,10297,10298,10299,10300,10303,10304,10305,10308,10309,10311,10312,10313,10314,10318,10319,10323,10324,10325,10328,10329,10330,10338,10341,10342,10343,10344,10381,10382,10386
		 * username : llz.cs
		 * PositionID : 10485
		 * StaffID : null
		 * CompanyID : 10028
		 * TypeAdministrator : 1
		 */

		private String guid;
		private int isHire;
		private int personType;
		private Object PowerList;
		private String companyids;
		private String username;
		private String PositionID;
		private String StaffID;
		private String CompanyID;
		private int TypeAdministrator;

		public String getGuid() {
			return guid;
		}

		public void setGuid(String guid) {
			this.guid = guid;
		}

		public int getIsHire() {
			return isHire;
		}

		public void setIsHire(int isHire) {
			this.isHire = isHire;
		}

		public int getPersonType() {
			return personType;
		}

		public void setPersonType(int personType) {
			this.personType = personType;
		}

		public Object getPowerList() {
			return PowerList;
		}

		public void setPowerList(Object PowerList) {
			this.PowerList = PowerList;
		}

		public String getCompanyids() {
			return companyids;
		}

		public void setCompanyids(String companyids) {
			this.companyids = companyids;
		}

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public String getPositionID() {
			return PositionID;
		}

		public void setPositionID(String PositionID) {
			this.PositionID = PositionID;
		}

		public String getStaffID() {
			return StaffID;
		}

		public void setStaffID(String StaffID) {
			this.StaffID = StaffID;
		}

		public String getCompanyID() {
			return CompanyID;
		}

		public void setCompanyID(String CompanyID) {
			this.CompanyID = CompanyID;
		}

		public int getTypeAdministrator() {
			return TypeAdministrator;
		}

		public void setTypeAdministrator(int TypeAdministrator) {
			this.TypeAdministrator = TypeAdministrator;
		}
	}
}
