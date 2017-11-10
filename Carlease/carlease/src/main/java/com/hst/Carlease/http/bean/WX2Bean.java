package com.hst.Carlease.http.bean;

/**
 * 微信支付
 * 
 * @author wzy 2016年5月9日
 * 
 */
public class WX2Bean {
	// "code":0,"msg":"操作成功","total":1
	// ,"data":
	// 返回值数据字段：
	// appid:应用ID string
	// mch_id:商户号 string
	// nonce_str:微信返回的随机字符串 string
	// sign:微信返回的签名 string
	// prepay_id:预支付交易会话标识 string
	// err_code:错误代码 string
	// err_code_des:错误代码描述 string

	private int code;
	private String msg;
	private int total;
	private Wdata data;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public Wdata getData() {
		return data;
	}

	public void setData(Wdata data) {
		this.data = data;
	}

	public class Wdata {
		// appid:应用ID string
		// partnerId:商户号 string
		// prepayId：预支付交易会话标识 string
		// package：string
		// nonceStr:随机字符串 string
		// timeStamp:时间戳 string
		// sign:签名 string

		private String appid;
		private String mch_id;
		private String nonceStr;
		private String sign;
		private String prepayId;
		private String err_code;
		private String err_code_des;
		private String timeStamp;
		private String partnerId;

		public String getAppid() {
			return appid;
		}

		public void setAppid(String appid) {
			this.appid = appid;
		}

		public String getMch_id() {
			return mch_id;
		}

		public void setMch_id(String mch_id) {
			this.mch_id = mch_id;
		}

		public String getNonceStr() {
			return nonceStr;
		}

		public void setNonceStr(String nonceStr) {
			this.nonceStr = nonceStr;
		}

		public String getSign() {
			return sign;
		}

		public void setSign(String sign) {
			this.sign = sign;
		}

		public String getPrepayId() {
			return prepayId;
		}

		public void setPrepayId(String prepayId) {
			this.prepayId = prepayId;
		}

		public String getErr_code() {
			return err_code;
		}

		public void setErr_code(String err_code) {
			this.err_code = err_code;
		}

		public String getErr_code_des() {
			return err_code_des;
		}

		public void setErr_code_des(String err_code_des) {
			this.err_code_des = err_code_des;
		}

		public String getTimeStamp() {
			return timeStamp;
		}

		public void setTimeStamp(String timeStamp) {
			this.timeStamp = timeStamp;
		}

		public String getPartnerId() {
			return partnerId;
		}

		public void setPartnerId(String partnerId) {
			this.partnerId = partnerId;
		}

	}

}
