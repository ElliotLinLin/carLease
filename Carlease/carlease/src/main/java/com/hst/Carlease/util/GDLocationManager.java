package com.hst.Carlease.util;

import android.content.Context;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;

public class GDLocationManager implements AMapLocationListener{
	public static String cityName="深圳市";
	public static double lat=22.541d;
	public static double lon=114.128d;
	private AMapLocationClient mLocationClient = null;//AMapLocationClient类对象
	private AMapLocationListener mLocationListener = null;//回调监听器
	private AMapLocationClientOption mLocationOption;//定位回调监听
	public GDLocationManager(Context context, AMapLocationListener listener) {
		init(context, listener);
		
	}

	//定位功能初始化
	private void init(Context context, AMapLocationListener l) {
		mLocationClient = new AMapLocationClient(context);
		mLocationOption = new AMapLocationClientOption();
		mLocationOption.setLocationMode(AMapLocationMode.Hight_Accuracy);//混合定位
		mLocationOption.setNeedAddress(true);//设置是否返回地址信息（默认返回地址信息）
//		mLocationOption.setOnceLocation(false);//设置是否只定位一次,默认为false
		mLocationOption.setWifiActiveScan(true);//设置是否强制刷新WIFI，默认为强制刷新
		mLocationOption.setMockEnable(true);//设置是否允许模拟位置,默认为false，不允许模拟位置
		mLocationOption.setInterval(5000);//设置定位间隔,单位毫秒,默认为2000ms
		mLocationClient.setLocationOption(mLocationOption);
		mLocationClient.setLocationListener(this);
		this.mLocationListener=l;
	}
	/**
	 * 开启连续定位
	 * @param repectTime重复时间 毫秒
	 */
	public void start(int repectTime) {
		stop();
		mLocationOption.setOnceLocation(false);//设置是否只定位一次,默认为false
		mLocationOption.setInterval(repectTime);
		mLocationClient.setLocationOption(mLocationOption);
		mLocationClient.startLocation();
	}
	/**
	 * 开启定位
	 * @param context
	 */
	public void startOnce() {
		stop();
		mLocationOption.setOnceLocation(true);//设置是否只定位一次,默认为false
		mLocationClient.setLocationOption(mLocationOption);
		mLocationClient.startLocation();
	}
	/**
	 * 获取最后一次可知的位置
	 * @return
	 */
	public AMapLocation getLastKnownLocation(){
		AMapLocation lastKnownLocation = mLocationClient.getLastKnownLocation();
		return lastKnownLocation;
	}
	/**
	 * 停止定位
	 */
	public void stop() {
		if (null != mLocationClient) {
			mLocationClient.stopLocation();
		}
	}

	/**
	 * 销毁定位
	 */
	public void destory() {
		stop();
		if (null != mLocationClient) {
			mLocationClient.onDestroy();
			mLocationClient = null;
		}
	}

	@Override
	public void onLocationChanged(AMapLocation location) {
		if(null!=mLocationListener){
			mLocationListener.onLocationChanged(location);
		}
		if(null!=location){
			if(location.getLatitude()==0){
				cityName="深圳市";
				return;
			}
			lon=location.getLongitude();
			lat=location.getLatitude();
			cityName=location.getCity();
			if (location.getCity()==null) {
				cityName="深圳市";
			}
			Log.e("定位是 ", "GDLocationManager:"+cityName+"--"+lat+"--"+lon);
		}
	}
}
