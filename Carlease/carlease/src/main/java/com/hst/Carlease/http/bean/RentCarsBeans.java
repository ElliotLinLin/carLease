package com.hst.Carlease.http.bean;

import java.util.List;

public class RentCarsBeans {
	private List<RentCars> model;// Array
	private String msg;// 操作成功
	private int statu;// 1

	public class RentCars {
		private String ContractID;// 合同标识
		private String FrtPay;// 首付
		private String MonthRent;// 月租
		private String PlateNumber;// 车牌号
		private String SerialNumber;// 合同编号

		public String getContractID() {
			return ContractID;
		}

		public void setContractID(String contractID) {
			ContractID = contractID;
		}

		public String getFrtPay() {
			return FrtPay;
		}

		public void setFrtPay(String frtPay) {
			FrtPay = frtPay;
		}

		public String getMonthRent() {
			return MonthRent;
		}

		public void setMonthRent(String monthRent) {
			MonthRent = monthRent;
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

	}
	public List<RentCars> getModel() {
		return model;
	}

	public void setModel(List<RentCars> model) {
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
