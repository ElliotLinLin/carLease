package com.hst.Carlease.http.bean;

import java.util.List;

public class SecondCarDetailsBean2 {
private String msg;
private int statu;
private SecondCarDetails model;

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

public SecondCarDetails getModel() {
	return model;
}

public void setModel(SecondCarDetails model) {
	this.model = model;
}

public class SecondCarDetails{
	private int UcID     ;//4,
	private int  CarID     ;//25211,
     private String   PlateNumber     ;// 车牌号
     private String    ShortPlateNum     ;//牌照 默认车牌前二个字符
     private int CarModelID     ;//10377,
     private int  BrandID     ;//10100,
     private String     BrandName     ;//比亚迪",
     private String     CarModelName     ;//比亚迪F3R1.5MT舒适",
     private String   DefinedNo     ;//车辆自定义编号
     private String   Title     ;//标题
     private String   UcYear     ;//年份
     private String   UcStatus     ;//状态标识
     private String   UcStatusName     ;//状态名称
     private String   Mileage     ;//公里数
     private int   Region     ;//区域
     private String  RegionName     ;//区域名称
     private String StrCardDate     ;//上牌时间
     private String  Displacement     ;//排量
     private String  TransmissionCase     ;//变数箱
     private String  Discharge     ;//排放标准
     private String ShouPayment     ;//首付
     private String  WeiPayment     ;//尾付
     private String  TwoMonthly     ;//两年月供
     private String   ThreeMonthly     ;//三年月供
     private String  Disposable     ;//一次性价格
     private String   StrCreateDate     ;//创建时间
     private String  StrUpdateDate     ;//更新时间
     private String  UcRemark     ;//备注
     private int   CompanyID     ;//分公司ID
     private String  CompanyName     ;//分公司名称
     private String  Nature     ;//使用性质
     private String  NatureName     ;//一般用车
     private String   Maintenance     ;//维护 
     private String Source     ;//来源
     private int   SeatCount     ;//座位数
     private String OilType     ;//油料标识
     private String     OilTypeName     ;//油料
//     private String   AnnualInspectionDate     ;//年审到期日
     private String   AnnualInspection     ;//年检,
     private String Phone;//电话
     private String LetherSeat;//真皮座椅
     private String ReverseImage;//倒影影像
     private String GPSnavi;//gps导航
     private String Safe;//保险
     
     private List<String>   CarUsedUrl;//图片附件
	public int getUcID() {
		return UcID;
	}
	public void setUcID(int ucID) {
		UcID = ucID;
	}
	public int getCarID() {
		return CarID;
	}
	public void setCarID(int carID) {
		CarID = carID;
	}
	public String getPlateNumber() {
		return PlateNumber;
	}
	public void setPlateNumber(String plateNumber) {
		PlateNumber = plateNumber;
	}
	public String getShortPlateNum() {
		return ShortPlateNum;
	}
	public void setShortPlateNum(String shortPlateNum) {
		ShortPlateNum = shortPlateNum;
	}
	public int getCarModelID() {
		return CarModelID;
	}
	public void setCarModelID(int carModelID) {
		CarModelID = carModelID;
	}
	
	public String getPhone() {
		return Phone;
	}
	public void setPhone(String phone) {
		Phone = phone;
	}
	public String getLetherSeat() {
		return LetherSeat;
	}
	public void setLetherSeat(String letherSeat) {
		LetherSeat = letherSeat;
	}
	public String getReverseImage() {
		return ReverseImage;
	}
	public void setReverseImage(String reverseImage) {
		ReverseImage = reverseImage;
	}
	public String getGPSnavi() {
		return GPSnavi;
	}
	public void setGPSnavi(String gPSnavi) {
		GPSnavi = gPSnavi;
	}
	public String getSafe() {
		return Safe;
	}
	public void setSafe(String safe) {
		Safe = safe;
	}
	public int getBrandID() {
		return BrandID;
	}
	public void setBrandID(int brandID) {
		BrandID = brandID;
	}
	public String getBrandName() {
		return BrandName;
	}
	public void setBrandName(String brandName) {
		BrandName = brandName;
	}
	public String getCarModelName() {
		return CarModelName;
	}
	public void setCarModelName(String carModelName) {
		CarModelName = carModelName;
	}
	public String getDefinedNo() {
		return DefinedNo;
	}
	public void setDefinedNo(String definedNo) {
		DefinedNo = definedNo;
	}
	public String getTitle() {
		return Title;
	}
	public void setTitle(String title) {
		Title = title;
	}
	public String getUcYear() {
		return UcYear;
	}
	public void setUcYear(String ucYear) {
		UcYear = ucYear;
	}
	public String getUcStatus() {
		return UcStatus;
	}
	public void setUcStatus(String ucStatus) {
		UcStatus = ucStatus;
	}
	public String getUcStatusName() {
		return UcStatusName;
	}
	public void setUcStatusName(String ucStatusName) {
		UcStatusName = ucStatusName;
	}
	public String getMileage() {
		return Mileage;
	}
	public void setMileage(String mileage) {
		Mileage = mileage;
	}
	public int getRegion() {
		return Region;
	}
	public void setRegion(int region) {
		Region = region;
	}
	public String getRegionName() {
		return RegionName;
	}
	public void setRegionName(String regionName) {
		RegionName = regionName;
	}

	public String getDisplacement() {
		return Displacement;
	}
	public void setDisplacement(String displacement) {
		Displacement = displacement;
	}
	public String getTransmissionCase() {
		return TransmissionCase;
	}
	public void setTransmissionCase(String transmissionCase) {
		TransmissionCase = transmissionCase;
	}
	public String getDischarge() {
		return Discharge;
	}
	public void setDischarge(String discharge) {
		Discharge = discharge;
	}
	public String getShouPayment() {
		return ShouPayment;
	}
	public void setShouPayment(String shouPayment) {
		ShouPayment = shouPayment;
	}
	public String getWeiPayment() {
		return WeiPayment;
	}
	public void setWeiPayment(String weiPayment) {
		WeiPayment = weiPayment;
	}
	public String getTwoMonthly() {
		return TwoMonthly;
	}
	public void setTwoMonthly(String twoMonthly) {
		TwoMonthly = twoMonthly;
	}
	public String getThreeMonthly() {
		return ThreeMonthly;
	}
	public void setThreeMonthly(String threeMonthly) {
		ThreeMonthly = threeMonthly;
	}
	public String getDisposable() {
		return Disposable;
	}
	public void setDisposable(String disposable) {
		Disposable = disposable;
	}
	public String getUcRemark() {
		return UcRemark;
	}
	public void setUcRemark(String ucRemark) {
		UcRemark = ucRemark;
	}
	public int getCompanyID() {
		return CompanyID;
	}
	public void setCompanyID(int companyID) {
		CompanyID = companyID;
	}
	public String getCompanyName() {
		return CompanyName;
	}
	public void setCompanyName(String companyName) {
		CompanyName = companyName;
	}
	public String getNature() {
		return Nature;
	}
	public void setNature(String nature) {
		Nature = nature;
	}
	public String getNatureName() {
		return NatureName;
	}
	public void setNatureName(String natureName) {
		NatureName = natureName;
	}
	public String getMaintenance() {
		return Maintenance;
	}
	public void setMaintenance(String maintenance) {
		Maintenance = maintenance;
	}
	public String getSource() {
		return Source;
	}
	public void setSource(String source) {
		Source = source;
	}
	public int getSeatCount() {
		return SeatCount;
	}
	public void setSeatCount(int seatCount) {
		SeatCount = seatCount;
	}
	public String getOilType() {
		return OilType;
	}
	public void setOilType(String oilType) {
		OilType = oilType;
	}
	public String getOilTypeName() {
		return OilTypeName;
	}
	public void setOilTypeName(String oilTypeName) {
		OilTypeName = oilTypeName;
	}
	
	public String getStrCardDate() {
		return StrCardDate;
	}
	public void setStrCardDate(String strCardDate) {
		StrCardDate = strCardDate;
	}
	public String getStrCreateDate() {
		return StrCreateDate;
	}
	public void setStrCreateDate(String strCreateDate) {
		StrCreateDate = strCreateDate;
	}
	public String getStrUpdateDate() {
		return StrUpdateDate;
	}
	public void setStrUpdateDate(String strUpdateDate) {
		StrUpdateDate = strUpdateDate;
	}
	public String getAnnualInspection() {
		return AnnualInspection;
	}
	public void setAnnualInspection(String annualInspection) {
		AnnualInspection = annualInspection;
	}
	public List<String> getCarUsedUrl() {
		return CarUsedUrl;
	}
	public void setCarUsedUrl(List<String> carUsedUrl) {
		CarUsedUrl = carUsedUrl;
	}
     
     
}
}
