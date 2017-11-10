package com.hst.Carlease.ram;

/**
 * 接口url地址
 * 
 * @author HL
 * 
 */

public class HTTPURL extends HttpDomain {

	// 测试
	private static final String testUrl = getUrl() + "/SmsSend/SendMobileCode?";

	private static final String TokenUrl = getUrl() + "/Common/GetToken?cs=3";
	// 获取token
	private static final String Token = getUrl() + "/Common/GetToken?";

	// 注册
	private static final String Reg = getUrl() + "/CustomerReg/Reg?";
	// 协议
	private static final String GetInfo = getUrl() + "/Articles/GetInfo?";
	// 登录
	private static final String CheckLoginCustomer = getUrl()
			+ "/CustomerReg/CheckLoginCustomer?";
	// 完善资料
	private static final String UpdateCertImg = getUrl()
			+ "/CustomerReg/UpdateCertImg?";
	// 忘记密码(发手机验证码)
	private static final String FindPasswordSendCode = getUrl()
			+ "/CustomerReg/FindPasswordSendCode?";
	// 忘记密码(据验证码修改密码)
	private static final String FindPasswordByCode = getUrl()
			+ "/CustomerReg/FindPasswordByCode?";
	// 修改密码
	private static final String ChangePassword = getUrl()
			+ "/CustomerReg/ChangePassword?";
	// 验证码
	private static final String SendMobileCode = getUrl()
			+ "/SmsSend/SendMobileCodeRegSelf?";
	// 获取最大租车天数
	private static final String GetRentalSelfMaxDay = getUrl()
			+ "/SLOrder/GetRentalSelfMaxDay?";
	// 车型列表
	private static final String GetPositionHourListRentalPriceApp = getUrl()
			+ "/Price/GetPositionHourListRentalPriceApp?";
	// 还车门店
	private static final String GetStoreListSelf = getUrl()
			+ "/Store/GetStoreListSelf?";
	// 增值服务列表
	private static final String GetSelfHelpService = getUrl()
			+ "/Price/GetSelfHelpService?";
	// 我的订单列表
	private static final String GetOrderListSelfApp = getUrl()
			+ "/SLOrder/GetOrderListSelfApp?";
	// 我的违章列表
	private static final String GetListByCustmerRegId = getUrl()
			+ "/ControlPlane/GetListByCustmerRegId?";
	// 我的订单列表
	private static final String GetInfoReg = getUrl() + "/CustomerReg/GetInfo?";
	// 我的订单列表
	private static final String CancelOrderSelfApp = getUrl()
			+ "/SLOrder/CancelOrderSelfApp?";
	// 提交订单
	private static final String InsertOrder = getUrl()
			+ "/SLOrder/InsertOrderSelfApp?";
	// 续租提交订单
	private static final String ContinueRentalSelfApp = getUrl()
			+ "/SLOrder/ContinueRentalSelfApp?";
	// 车型价格和服务列表
	private static final String GetRenewSelfHelpPrice = getUrl()
			+ "/SLOrder/GetRenewSelfHelpPrice?";
	// 支付宝回调
	private static final String AliPayNotify = getUrl()
			+ "/Common/AliPayNotify?";
	// 意见反馈
	private static final String SaveFeedbackSelfApp = getUrl()
			+ "/Home/SaveFeedbackSelfApp?";
	// 选择城市
	private static final String GetAllListForSelf = getUrl()
			+ "/store/GetAllListForSelf?";

	// 首页车型列表
	private static final String getBannerUrl = getUrl()
			+ "/Banner/GetListSelfApp?";

	// 用户当前订单
	private static final String CurrUserOrder = getUrl()
			+ "/SLOrder/GetCurrentOrderSelfApp?";

	// 用户当前订单状态是否可取还车
	private static final String OrderStatus = getUrl()
			+ "/ControlPlane/TakeCarInfo?";

	// 指令下发
	private static final String OrderCarUrl = getUrl()
			+ "/ControlPlane/SendSim?";

	// 获取性能物品
	private static final String GoodsUrl = getUrl()
			+ "/ControlPlane/ListByDAVehicleGoods?";

	// 是否确认了随车物品
	private static final String IsCommitUrl = getUrl()
			+ "/ControlPlane/OpenCarDoorInfo?";

	// 确认随车物品
	private static final String CommitGoodsUrl = getUrl()
			+ "/ControlPlane/InsertDAVehicleGoods?";

	// 取车时验车图片
	private static final String CarStatusUrl = getUrl()
			+ "/ControlPlane/DAVehicleValidateByAnnexs?";

	// 提交验车图片
	private static final String CommitPicUrl = getUrl()
			+ "/ControlPlane/DAVehicleValidateBySave?";

	// 用户开门时勾选过的性能物品
	private static final String ChooseGoodsUrl = getUrl()
			+ "/ControlPlane/ReturnCarByGoods?";

	// 根据车辆id查询经纬度
	private static final String NaLUrl = getUrl()
			+ "/ControlPlane/CarByLngLat?";

	// 还车保存
	private static final String PostBackCarUrl = getUrl()
			+ "/ControlPlane/RepayCarBySave?";

	// 根据门店id查询经纬度
	private static final String NaLByRoomeIdUrl = getUrl()
			+ "/ControlPlane/SLStoreByLngLat?";

	// 微信支付
	private static final String GetWXPrepayidSelf = getUrl()
			+ "/payment/GetWXPrepayidToSignSelf?";
	
//	GetWXPrepayidSelf

	// 支付回调
	private static final String PayUrl = getUrl() + "/Common/AliPayNotifyApp";

	// 订单是否已过期
	private static final String OrderOutOfUrl = getUrl()
			+ "/ControlPlane/OrderDateNowIsbool?";

	// 导航取车时鸣笛寻车
	private static final String TakeCarOrderUrl = getUrl()
			+ "/ControlPlane/SendSimByXCar?";

	// 锁门还车
	private static final String CloseDoorUrl = getUrl()
			+ "/ControlPlane/SMReturnCar?";


	// 获取消息
	private static final String MessageUrl = getUrl()
			+ "/AppOut/GetSelfAppList?";
	// 删除消息
	private static final String DelMessageUrl = getUrl() + "/AppOut/Delete?";

	// 车辆启动状态
	private static final String CarStartStatusUrl = getUrl()
			+ "/ControlPlane/OkeyNoCarStatus?";

	public static String getTestUrl() {
		return testUrl;
	}

	// 获取toke加密前的url
	public static String getTokenUrl() {
		return TokenUrl;
	}

	// 获取token
	public static String getToken() {
		return Token;
	}

	public static String getReg() {
		return Reg;
	}

	public static String getChecklogincustomer() {
		return CheckLoginCustomer;
	}

	public static String getUpdatecertimg() {
		return UpdateCertImg;
	}

	public static String getSendmobilecode() {
		return SendMobileCode;
	}

	public static String getBannerUrl() {
		return getBannerUrl;
	}

	public static String getGetpositionhourlistrentalpriceapp() {
		return GetPositionHourListRentalPriceApp;
	}

	public static String getGetinfo() {
		return GetInfo;
	}

	public static String getGetselfhelpservice() {
		return GetSelfHelpService;
	}

	public static String getCurrUserOrder() {
		return CurrUserOrder;
	}

	public static String getOrderStatus() {
		return OrderStatus;
	}

	public static String getInsertorder() {
		return InsertOrder;
	}

	public static String getGetorderlistselfapp() {
		return GetOrderListSelfApp;
	}

	public static String getOrderCarUrl() {
		return OrderCarUrl;
	}

	public static String getGoodsUrl() {
		return GoodsUrl;
	}

	public static String getIsCommitUrl() {
		return IsCommitUrl;
	}

	public static String getCommitGoodsUrl() {
		return CommitGoodsUrl;
	}

	public static String getCarStatusUrl() {
		return CarStatusUrl;
	}

	public static String getCancelorderselfapp() {
		return CancelOrderSelfApp;
	}

	public static String getCommitPicUrl() {
		return CommitPicUrl;
	}

	public static String getAlipaynotify() {
		return AliPayNotify;
	}

	public static String getChooseGoodsUrl() {
		return ChooseGoodsUrl;
	}

	public static String getGetinforeg() {
		return GetInfoReg;
	}

	public static String getNaLUrl() {
		return NaLUrl;
	}

	public static String getPostBackCarUrl() {
		return PostBackCarUrl;
	}

	public static String getNaLByRoomeIdUrl() {
		return NaLByRoomeIdUrl;
	}

	public static String getFindpasswordsendcode() {
		return FindPasswordSendCode;
	}

	public static String getFindpasswordbycode() {
		return FindPasswordByCode;
	}

	public static String getChangepassword() {
		return ChangePassword;
	}

	public static String getPayUrl() {
		return PayUrl;
	}

	public static String getOrderOutOfUrl() {
		return OrderOutOfUrl;
	}

	public static String getCloseDoorUrl() {
		return CloseDoorUrl;
	}

	public static String getGetstorelistself() {
		return GetStoreListSelf;
	}

	public static String getTakeCarOrderUrl() {
		return TakeCarOrderUrl;
	}

	public static String getSavefeedbackselfapp() {
		return SaveFeedbackSelfApp;
	}


	public static String getMessageUrl() {
		return MessageUrl;
	}

	public static String getDelMessageUrl() {
		return DelMessageUrl;
	}

	public static String getCarStartStatusUrl() {
		return CarStartStatusUrl;
	}

	public static String getContinuerentalselfapp() {
		return ContinueRentalSelfApp;
	}

	public static String getGetrenewselfhelpprice() {
		return GetRenewSelfHelpPrice;
	}

	public static String getGetlistbycustmerregid() {
		return GetListByCustmerRegId;
	}

	public static String getGetrentalselfmaxday() {
		return GetRentalSelfMaxDay;
	}

	public static String getGetalllistforself() {
		return GetAllListForSelf;
	}

	public static String getGetwxprepayidself() {
		return GetWXPrepayidSelf;
	}

}
