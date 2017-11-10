package com.hst.Carlease.http.bean;

import java.util.List;

public class TransactionHistoryBean {
	private List<TransactionHistory> model;// Array
	private String msg;// 获取交易记录成功
	private int statu;// 1

	public class TransactionHistory {
		private String inner_Trade_No;// 12466820170609163456921
		private String orderNo;//
		private String pay_type;// 支付宝
		private float total_fee;// 总金额
		private String trade_body;// 深圳市赢时通汽车服务有限公司订单支付
		private String trade_date;// 支付时间
		private String trade_no;// 支付宝流水号
		private String trade_status;// 支付完成
		private String trade_type;// 费用类型，如 租金……

		public String getInner_Trade_No() {
			return inner_Trade_No;
		}

		public void setInner_Trade_No(String inner_Trade_No) {
			this.inner_Trade_No = inner_Trade_No;
		}

		public String getOrderNo() {
			return orderNo;
		}

		public void setOrderNo(String orderNo) {
			this.orderNo = orderNo;
		}

		public String getPay_type() {
			return pay_type;
		}

		public void setPay_type(String pay_type) {
			this.pay_type = pay_type;
		}

		public float getTotal_fee() {
			return total_fee;
		}

		public void setTotal_fee(float total_fee) {
			this.total_fee = total_fee;
		}

		public String getTrade_body() {
			return trade_body;
		}

		public void setTrade_body(String trade_body) {
			this.trade_body = trade_body;
		}

		public String getTrade_date() {
			return trade_date;
		}

		public void setTrade_date(String trade_date) {
			this.trade_date = trade_date;
		}

		public String getTrade_no() {
			return trade_no;
		}

		public void setTrade_no(String trade_no) {
			this.trade_no = trade_no;
		}

		public String getTrade_status() {
			return trade_status;
		}

		public void setTrade_status(String trade_status) {
			this.trade_status = trade_status;
		}

		public String getTrade_type() {
			return trade_type;
		}

		public void setTrade_type(String trade_type) {
			this.trade_type = trade_type;
		}

	}

	public List<TransactionHistory> getModel() {
		return model;
	}

	public void setModel(List<TransactionHistory> model) {
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
