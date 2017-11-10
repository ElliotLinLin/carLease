package com.hst.Carlease.util;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.tools.location.ConvertGps;

public class LocationChangedUtils implements AMapLocationListener{
	private static final String TAG = LocationChangedUtils.class.getSimpleName();
//	public static double latitude = 22.541d;// 纬度
//	public static double longitude = 114.128d;// 经度
	private Context context;// 上下文
	private FragmentActivity ui;// 上下文
	public static String cityname = "深圳市";// 城市名
	// 定位状态
	public static boolean isLocating = false;
	private GDLocationManager mLocationManager;

	public LocationChangedUtils(Context context, FragmentActivity ui) {
		this.context = context;
		init();
	}

	@Override
	public void onLocationChanged(AMapLocation aLocation) {
		if (aLocation != null) {
			Log.e(TAG, "onLocationChanged(AMapLocation)->location:" + aLocation.getLatitude());
			Log.e(TAG, "onLocationChanged(AMapLocation)->location:" + aLocation.getLongitude());
//			double[] gps = ConvertGps.fromGcj02ToWgs84(aLocation.getLongitude(), aLocation.getLatitude());
//			if (gps != null && gps.length>=2) {
//				latitude = gps[1];
//				longitude = gps[0];
//			}
			cityname =aLocation.getCity();
			Log.i(TAG, "onLocationChanged(AMapLocation)->location"+aLocation.getCity());
			if (TextUtils.isEmpty(cityname)) {
				cityname = "深圳市";
			}
		} else {
			Log.e(TAG,"定位失败请检查网络！");
		}
	}
	private void init() {
		mLocationManager = new GDLocationManager(context, this);
	}

//	public void start() {
//		mLocationManager.start(20 * 1000);
//	}
	public void startOnce(){
		mLocationManager.startOnce();
	}

	public void stop() {
		mLocationManager.stop();
	}

	public void destory() {
		mLocationManager.destory();
	}
}
