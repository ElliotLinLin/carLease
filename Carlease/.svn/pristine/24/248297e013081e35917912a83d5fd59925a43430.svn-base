package com.hst.Carlease.http.bean;

import java.io.Serializable;
import java.util.List;

public class MypayBean implements Serializable  {
	private List<PayBean> model;// Object
	private String msg;// 操作成功
	private int statu;// 1

	public class PayBean implements Serializable {
		private List<BillItem> BillItem;// 费用列表
		private int ContractID;// 合同ID
		private String ContractStatus;// 合同状态
		private String PlateNumber;// 蒙AZ9131
		private String SerialNumber;// 合同编号
		private String MonthlyRent;// 当月应缴纳的租金
		private String OverdueFee;// 逾期管理费
		private String PaymentDate;// 2016-11-11
		private String LateFee;// 罚息
		private String HireSerialNumber;//支付单号

		public class BillItem implements Serializable {
			private int plid;
			private String dnt_name;// 费用类型
			private float pl_amount;// 费用金额
			private String pl_date;// 支付时间
			private int pl_payStatus;// 0 不可支付，1可支付
			private float pl_receipts;// 实收金额
			private String pl_remark;// 首付
			private String pl_status;// 费用状态，0未收，1未收全，2已收
			private int plday;// //应付日期
			private int plmonth;// //应付月份
			private int plyear;// 应付年份

			public int getPlid() {
				return plid;
			}

			public void setPlid(int plid) {
				this.plid = plid;
			}

			public String getDnt_name() {
				return dnt_name;
			}

			public void setDnt_name(String dnt_name) {
				this.dnt_name = dnt_name;
			}

			public float getPl_amount() {
				return pl_amount;
			}

			public void setPl_amount(float pl_amount) {
				this.pl_amount = pl_amount;
			}

			public String getPl_date() {
				return pl_date;
			}

			public void setPl_date(String pl_date) {
				this.pl_date = pl_date;
			}

			public int getPl_payStatus() {
				return pl_payStatus;
			}

			public void setPl_payStatus(int pl_payStatus) {
				this.pl_payStatus = pl_payStatus;
			}

			public float getPl_receipts() {
				return pl_receipts;
			}

			public void setPl_receipts(float pl_receipts) {
				this.pl_receipts = pl_receipts;
			}

			public String getPl_remark() {
				return pl_remark;
			}

			public void setPl_remark(String pl_remark) {
				this.pl_remark = pl_remark;
			}

			public String getPl_status() {
				return pl_status;
			}

			public void setPl_status(String pl_status) {
				this.pl_status = pl_status;
			}

			public int getPlday() {
				return plday;
			}

			public void setPlday(int plday) {
				this.plday = plday;
			}

			public int getPlmonth() {
				return plmonth;
			}

			public void setPlmonth(int plmonth) {
				this.plmonth = plmonth;
			}

			public int getPlyear() {
				return plyear;
			}

			public void setPlyear(int plyear) {
				this.plyear = plyear;
			}

		}

		public List<BillItem> getBillItem() {
			return BillItem;
		}

		public void setBillItem(List<BillItem> billItem) {
			BillItem = billItem;
		}

		public int getContractID() {
			return ContractID;
		}

		public void setContractID(int contractID) {
			ContractID = contractID;
		}

		public String getContractStatus() {
			return ContractStatus;
		}

		public void setContractStatus(String contractStatus) {
			ContractStatus = contractStatus;
		}

		public String getPlateNumber() {
			return PlateNumber;
		}

		public void setPlateNumber(String plateNumber) {
			PlateNumber = plateNumber;
		}

		public String getSerialNumber() {
			return SerialNumber;
		}

		public void setSerialNumber(String serialNumber) {
			SerialNumber = serialNumber;
		}

		public String getMonthlyRent() {
			return MonthlyRent;
		}

		public void setMonthlyRent(String monthlyRent) {
			MonthlyRent = monthlyRent;
		}

		public String getOverdueFee() {
			return OverdueFee;
		}

		public void setOverdueFee(String overdueFee) {
			OverdueFee = overdueFee;
		}

		public String getPaymentDate() {
			return PaymentDate;
		}

		public void setPaymentDate(String paymentDate) {
			PaymentDate = paymentDate;
		}

		public String getLateFee() {
			return LateFee;
		}

		public void setLateFee(String lateFee) {
			LateFee = lateFee;
		}

		public String getHireSerialNumber() {
			return HireSerialNumber;
		}

		public void setHireSerialNumber(String hireSerialNumber) {
			HireSerialNumber = hireSerialNumber;
		}
		

	}

	public List<PayBean> getModel() {
		return model;
	}

	public void setModel(List<PayBean> model) {
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
