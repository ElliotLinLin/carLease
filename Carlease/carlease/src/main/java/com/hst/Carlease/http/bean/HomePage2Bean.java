package com.hst.Carlease.http.bean;

import java.util.List;

/**
 * 首页接收返回值的bean
 * 
 * @author lyq
 * 
 */
public class HomePage2Bean {
private  HomePageInfo	model;//	Object
private String	msg	;//获取成功
private int	statu;//	1
public class HomePageInfo{
	private List<Carousel> Carousel;//	Array
	private List<newArrival> newArrival;//	Array
	
	public List<Carousel> getCarousel() {
		return Carousel;
	}
	public void setCarousel(List<Carousel> carousel) {
		Carousel = carousel;
	}
	public List<newArrival> getNewArrival() {
		return newArrival;
	}
	public void setNewArrival(List<newArrival> newArrival) {
		this.newArrival = newArrival;
	}
	public class Carousel{
		private int	id	;//1018;//
		private String	url	;//http://192.168.60.211:8088/files/PurchaseCar/201610181732020389427463.jpg
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public String getUrl() {
			return url;
		}
		public void setUrl(String url) {
			this.url = url;
		}
		
	}
	public class newArrival{
		private int hcm_ID;//租购ID
		private String	 CarModerName;//车型名称
		private String	 CarBuanm;//
		private String Mpayment;
		private String TwoFMMpay;
	
		public String getTwoFMMpay() {
			return TwoFMMpay;
		}
		public void setTwoFMMpay(String twoFMMpay) {
			TwoFMMpay = twoFMMpay;
		}
		private List<String> CarModelUrl;
		
		public List<String> getCarModelUrl() {
			return CarModelUrl;
		}
		public void setCarModelUrl(List<String> carModelUrl) {
			CarModelUrl = carModelUrl;
		}
		public int getHcm_ID() {
			return hcm_ID;
		}
		public void setHcm_ID(int hcm_ID) {
			this.hcm_ID = hcm_ID;
		}
		public String getCarModerName() {
			return CarModerName;
		}
		public void setCarModerName(String carModerName) {
			CarModerName = carModerName;
		}
		public String getCarBuanm() {
			return CarBuanm;
		}
		public void setCarBuanm(String carBuanm) {
			CarBuanm = carBuanm;
		}
		public String getMpayment() {
			return Mpayment;
		}
		public void setMpayment(String mpayment) {
			Mpayment = mpayment;
		}
		
	}
}
public HomePageInfo getModel() {
	return model;
}
public void setModel(HomePageInfo model) {
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
