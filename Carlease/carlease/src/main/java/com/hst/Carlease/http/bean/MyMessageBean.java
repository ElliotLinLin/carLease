package com.hst.Carlease.http.bean;

import java.util.List;

public class MyMessageBean {
	private int statu;// 0
	private String msg;// 操作成功
	private List<MessageBean> model;// Array
	public class MessageBean {
		private String M_Title;// 标题
		private String M_Content;// 内容
		private String M_CreateDate;// 消息时间
		private String M_CreateUserID	;//1
		private String  M_CustomerID	;//124572
		private boolean  M_DeleteFlag	;//false
		private String  M_DeviceNo	;//aaaa
		private String  M_DeviceType;//	1
		private String  M_MsgType	;//1
		private String  M_SendType	;//1
		private String  M_id	;//13
		
		public String getM_CreateUserID() {
			return M_CreateUserID;
		}

		public void setM_CreateUserID(String m_CreateUserID) {
			M_CreateUserID = m_CreateUserID;
		}

		public String getM_CustomerID() {
			return M_CustomerID;
		}

		public void setM_CustomerID(String m_CustomerID) {
			M_CustomerID = m_CustomerID;
		}

		public boolean isM_DeleteFlag() {
			return M_DeleteFlag;
		}

		public void setM_DeleteFlag(boolean m_DeleteFlag) {
			M_DeleteFlag = m_DeleteFlag;
		}

		public String getM_DeviceNo() {
			return M_DeviceNo;
		}

		public void setM_DeviceNo(String m_DeviceNo) {
			M_DeviceNo = m_DeviceNo;
		}

		public String getM_DeviceType() {
			return M_DeviceType;
		}

		public void setM_DeviceType(String m_DeviceType) {
			M_DeviceType = m_DeviceType;
		}

		public String getM_MsgType() {
			return M_MsgType;
		}

		public void setM_MsgType(String m_MsgType) {
			M_MsgType = m_MsgType;
		}

		public String getM_SendType() {
			return M_SendType;
		}

		public void setM_SendType(String m_SendType) {
			M_SendType = m_SendType;
		}

		public String getM_id() {
			return M_id;
		}

		public void setM_id(String m_id) {
			M_id = m_id;
		}

		public String getM_Title() {
			return M_Title;
		}

		public void setM_Title(String m_Title) {
			M_Title = m_Title;
		}

		public String getM_Content() {
			return M_Content;
		}

		public void setM_Content(String m_Content) {
			M_Content = m_Content;
		}

		public String getM_CreateDate() {
			return M_CreateDate;
		}

		public void setM_CreateDate(String m_CreateDate) {
			M_CreateDate = m_CreateDate;
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

	public List<MessageBean> getModel() {
		return model;
	}

	public void setModel(List<MessageBean> model) {
		this.model = model;
	}

}
