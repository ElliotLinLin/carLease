package com.hst.Carlease.http.bean;

public class MessageDetailsBean {
	private MessageDetails model;// Object
	private String msg;// 成功获取
	private int statu;// 1

	public class MessageDetails {
		private String M_Title;// 标题
		private String M_Content;// 内容
		private String M_CreateDate;// 消息时间
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

	public MessageDetails getModel() {
		return model;
	}

	public void setModel(MessageDetails model) {
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
