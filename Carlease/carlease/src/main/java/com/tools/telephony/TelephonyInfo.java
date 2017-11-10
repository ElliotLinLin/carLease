package com.tools.telephony;

import android.content.Context;

import com.tools.content.pm.PermissionTool;
import com.tools.util.Log;


/**
 * 需要增加权限
 * <uses-permission android:name="android.permission.READ_PHONE_STATE" />
 * 
 * 在获取IMEI或者getLine1Number()时卡住，说明没有加入权限READ_PHONE_STATE
 *
 * @author lcm
 * 版权声明：版权归作者所有，未经协议授权不得使用、转载、抄袭等，否则依法追究法律责任和经济责任。
 */
public class TelephonyInfo {

	private static final String TAG = TelephonyInfo.class.getSimpleName();

	protected Context context = null;

	protected android.telephony.TelephonyManager telephonyManager = null;

	public TelephonyInfo(Context oontext) {
		init(oontext);
	}

	private void init(Context context) {
		if (context == null) {
			throw new java.lang.NullPointerException( "context == null" );
		}

		// 权限检查
		PermissionTool.checkThrow(context, android.Manifest.permission.READ_PHONE_STATE);

		this.context = context;
		telephonyManager = (android.telephony.TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
	}

	/**
	 * 获取IMEI
	 * 
	 * @return
	 */
	public String getIMEI() {
		if (telephonyManager == null) {
			return null;
		}
		return telephonyManager.getDeviceId();
	}

	/**
	 * 获取IMSI
	 * 
	 * @return
	 */
	public String getIMSI() {
		if (telephonyManager == null) {
			return null;
		}
		return telephonyManager.getSubscriberId();
	}

	/**
	 * 获取手机号码
	 * 
	 * 要有权限android.Manifest.permission.READ_PHONE_STATE
	 * java.lang.SecurityException: Requires READ_PHONE_STATE
	 * 
	 * getLine1Number();不一定能够获取到用户的电话，
	 * 因为这个数据是通过手工设定的，而且是可以更改的
	 * 
	 * @return
	 */
	public String getMobileNumber() {
		if (telephonyManager == null) {
			return null;
		}
		return telephonyManager.getLine1Number();
	}

	public void print() {
		Log.i(TAG, "--- TelephonyInfo() start ---");
		if (telephonyManager != null) {
			Log.i(TAG, "getCallState():"+telephonyManager.getCallState());
			Log.i(TAG, "getCellLocation():"+telephonyManager.getCellLocation());
			Log.i(TAG, "getDataActivity():"+telephonyManager.getDataActivity());
			Log.i(TAG, "getDataState():"+telephonyManager.getDataState());
			Log.i(TAG, "getDeviceId():"+telephonyManager.getDeviceId());
			Log.i(TAG, "getDeviceSoftwareVersion():"+telephonyManager.getDeviceSoftwareVersion());
			Log.i(TAG, "hasIccCard():"+telephonyManager.hasIccCard());
			Log.i(TAG, "getLine1Number():"+telephonyManager.getLine1Number());
			Log.i(TAG, "getNetworkCountryIso():"+telephonyManager.getNetworkCountryIso());
			Log.i(TAG, "getNetworkOperator():"+telephonyManager.getNetworkOperator());
			Log.i(TAG, "getNetworkOperatorName():"+telephonyManager.getNetworkOperatorName());
			Log.i(TAG, "getNetworkType():"+telephonyManager.getNetworkType());
			Log.i(TAG, "isNetworkRoaming():"+telephonyManager.isNetworkRoaming());
			Log.i(TAG, "getNeighboringCellInfo():"+telephonyManager.getNeighboringCellInfo());
			Log.i(TAG, "getPhoneType():"+telephonyManager.getPhoneType());
			Log.i(TAG, "getSimCountryIso():"+telephonyManager.getSimCountryIso());
			Log.i(TAG, "getSimOperator():"+telephonyManager.getSimOperator());
			Log.i(TAG, "getSimOperatorName():"+telephonyManager.getSimOperatorName());
			Log.i(TAG, "getSimState():"+telephonyManager.getSimState());
			Log.i(TAG, "getSubscriberId():"+telephonyManager.getSubscriberId());
			Log.i(TAG, "getVoiceMailNumber():"+telephonyManager.getVoiceMailNumber());
			Log.i(TAG, "getVoiceMailAlphaTag():"+telephonyManager.getVoiceMailAlphaTag());
			
		}
		Log.i(TAG, "--- TelephonyInfo() end ---");
	}

	public String toCrashString() {
		if (telephonyManager == null) {
			return "";
		}
		StringBuilder buffer = new StringBuilder();
		buffer.append("getCallState:");
		buffer.append( telephonyManager.getCallState() );
		buffer.append("\n");
		buffer.append("getDataActivity:");
		buffer.append( telephonyManager.getDataActivity() );
		buffer.append("\n");
		buffer.append("getDataState:");
		buffer.append( telephonyManager.getDataState() );
		buffer.append("\n");
		buffer.append("getDeviceId(IMEI):");
		// TODO 此方法不是100%的获取到IMEI，如果电话应用崩溃就不能获取到了。
		buffer.append( telephonyManager.getDeviceId() );
		buffer.append("\n");
		buffer.append("getDeviceSoftwareVersion:");
		buffer.append( telephonyManager.getDeviceSoftwareVersion() );
		buffer.append("\n");
		buffer.append("getLine1Number(号码):");
		buffer.append( "不要乱获取别人的隐私哦。" );
		buffer.append("\n");
		buffer.append("getNetworkCountryIso:");
		buffer.append( telephonyManager.getNetworkCountryIso() );
		buffer.append("\n");
		buffer.append("getNetworkOperator:");
		buffer.append( telephonyManager.getNetworkOperator() );
		buffer.append("\n");
		buffer.append("getNetworkOperatorName:");
		buffer.append( telephonyManager.getNetworkOperatorName() );
		buffer.append("\n");
		buffer.append("getNetworkType:");
		buffer.append( telephonyManager.getNetworkType() );
		buffer.append("\n");
		buffer.append("getPhoneType:");
		buffer.append( telephonyManager.getPhoneType() );
		buffer.append("\n");
		buffer.append("getSimCountryIso:");
		buffer.append( telephonyManager.getSimCountryIso() );
		buffer.append("\n");
		buffer.append("getSimOperator:");
		buffer.append( telephonyManager.getSimOperator() );
		buffer.append("\n");
		buffer.append("getSimOperatorName:");
		buffer.append( telephonyManager.getSimOperatorName() );
		buffer.append("\n");
		buffer.append("getSimSerialNumber:");
		buffer.append( "不要乱获取别人的隐私哦。" );
		buffer.append("\n");
		buffer.append("getSimState:");
		buffer.append( telephonyManager.getSimState() );
		buffer.append("\n");
		buffer.append("getSubscriberId(IMSI):");
		buffer.append( "不要乱获取别人的隐私哦。" );
		buffer.append("\n");
		buffer.append("getVoiceMailAlphaTag:");
		buffer.append( "不要乱获取别人的隐私哦。" );
		buffer.append("\n");
		buffer.append("getVoiceMailNumber:");
		buffer.append( "不要乱获取别人的隐私哦。" );
		buffer.append("\n");
		buffer.append("hasIccCard:");
		buffer.append( telephonyManager.hasIccCard() );
		buffer.append("\n");
		buffer.append("isNetworkRoaming:");
		buffer.append( telephonyManager.isNetworkRoaming() );
		return buffer.toString();
	}

}