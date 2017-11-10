package com.tools.location;

import android.content.Context;

import com.baidu.location.BaiduJni;
import com.tools.util.Log;


public class ConvertGpsClient {

	private static final String TAG = ConvertGpsClient.class.getSimpleName();
	private static final boolean D = true;

	private static double latitude = 22.544481D;// 纬度
	private static double longitude = 114.130802D; // 经度

	public static void testAMap() {

		//		double latitude = 22.544481D;// 纬度
		//		double longitude = 114.130802D; // 经度

		//		latitude = 23.5005D;// 纬度
		//		longitude = 111.2608D; // 经度

		//		GeoPoint point = CoordinateConvert.fromGpsToAMap(latitude, longitude);

		if(D) Log.e(TAG, "真实GPS坐标经度:"+longitude);
		if(D) Log.e(TAG, "真实GPS坐标纬度:"+latitude);

		//		if(D) Log.e(TAG, "高德纠偏后的point:"+point.toString());
		//		if(D) Log.e(TAG, "高德纠偏后的point:"+point.getLatitudeE6());
		//		if(D) Log.e(TAG, "高德纠偏后的point:"+point.getLongitudeE6());

		//		if(D) Log.e(TAG, "2222222222222222222");

		//		GeoPoint point1 = ConvertGps.fromWgs84ToGcj02(latitude, longitude);
		//		
		//		if(D) Log.e(TAG, "高德纠偏后的point:"+point1.toString());
		//		if(D) Log.e(TAG, "高德纠偏后的point:"+point1.getLatitudeE6());
		//		if(D) Log.e(TAG, "高德纠偏后的point:"+point1.getLongitudeE6());

		double[] daaa = new double[2];
		daaa[0] = longitude;
		daaa[1] = latitude;

		double[] d1 = ConvertGps.fromWgs84ToGcj02( daaa[0], daaa[1] );
		if(D) Log.e(TAG, "fromWgs84ToGcj02 经度:"+d1[0]);
		if(D) Log.e(TAG, "fromWgs84ToGcj02 纬度:"+d1[1]);

		double[] d2 = ConvertGps.fromGcj02ToWgs84Offset(d1[0], d1[1]);
		if(D) Log.e(TAG, "fromGcj02ToWgs84Offset 经度:"+d2[0]);
		if(D) Log.e(TAG, "fromGcj02ToWgs84Offset 纬度:"+d2[1]);

		//		if(D) Log.e(TAG, "3333333333333");

		if(D) Log.e(TAG, "二分查找法");

		d2 = ConvertGps.fromGcj02ToWgs84(d1[0], d1[1]);
		if(D) Log.e(TAG, "高德转真实GPS fromGcj02ToWgs84 经度:"+d2[0]);
		if(D) Log.e(TAG, "高德转真实GPS fromGcj02ToWgs84 纬度:"+d2[1]);

		//		if(D) Log.e(TAG, "相差 经度:"+(d2[0]-longitude)*1000000); // 差10
		//		if(D) Log.e(TAG, "相差 纬度:"+(d2[1]-latitude)*1000000); // 差6

	}

	public static void testBaidu() {

		double latitude = 22.546981D;// 纬度
		double longitude = 114.125406D; // 经度

		latitude = 22.546981D;// 纬度
		longitude = 114.125406D; // 经度

		if(D) Log.e(TAG, "真实GPS坐标经度:"+longitude);
		if(D) Log.e(TAG, "真实GPS坐标纬度:"+latitude);

		double[] wgs84 = new double[2];
		wgs84[0] = longitude;
		wgs84[1] = latitude;

		double[] gcj02 = ConvertGps.fromWgs84ToGcj02( wgs84[0], wgs84[1] );
		if(D) Log.e(TAG, "fromWgs84ToGcj02 经度:"+gcj02[0]);
		if(D) Log.e(TAG, "fromWgs84ToGcj02 纬度:"+gcj02[1]);

		if(D) Log.e(TAG, "------- TEST ------");

		/*//google 坐标转百度链接 
		 * //http://api.map.convert.com/ag/coord/convert?from=2&to=4&x=116.32715863448607&y=39.990912172420714&callback=BMap.Convertor.cbk_3694
//gps坐标的type=0
//google坐标的type=2
//convert坐标的type=4*/

		double[] gps = BaiduJni.convert( wgs84[0], wgs84[1], "gps2gcj" );
		if(D) Log.e(TAG, "fromGcj02ToBd09 经度2fdSFESFfd22:"+gps[0]);
		if(D) Log.e(TAG, "fromGcj02ToBd09 纬度222:"+gps[1]);

		// 将坐标变成bd09ll
		double[] gps1 = BaiduJni.convert( gps[0], gps[1], "bd09ll" );
		if(D) Log.e(TAG, "fromGcj02ToBd09 经度2fdSFESFfd22:"+gps1[0]);
		if(D) Log.e(TAG, "fromGcj02ToBd09 纬度222:"+gps1[1]);

		// http://api.map.convert.com/ag/coord/convert?from=2&to=4&x=114.125406&y=22.546981
		// {"error":0,"x":"MTE0LjEzMTgyODY4NzM5","y":"MjIuNTUzMzA3MzA5MTI5"}
		// 114.13182868739 22.553307309129

		// http://tools.jb51.net/tools/base64_decode-gb2312.php

		// {"error":0,"x":"MTE0LjEzNjkyNjU0MDYy","y":"MjIuNTUwNTgwMjg3OTA3"}
		// 114.13692654062 22.550580287907

		//		double[] convert = ConvertGps.fromGcj02ToBd09( gcj02[0], gcj02[1] );
		//		if(D) Log.e(TAG, "fromGcj02ToBd09 经度:"+convert[0]);
		//		if(D) Log.e(TAG, "fromGcj02ToBd09 纬度:"+convert[1]);

		//		double[] gps = ConvertGps.fromBd09ToWgs84( convert[0], convert[1] );
		//		if(D) Log.e(TAG, "fromBd09ToWgs84 经度:"+gps[0]);
		//		if(D) Log.e(TAG, "fromBd09ToWgs84 纬度:"+gps[1]);

	}

	public static void fromWgs84ToBd09() {

		if(D) Log.e(TAG, "真实GPS坐标经度:"+longitude);
		if(D) Log.e(TAG, "真实GPS坐标纬度:"+latitude);

		double[] wgs84 = new double[2];
		wgs84[0] = longitude;
		wgs84[1] = latitude;

		double[] gps_bd09 = ConvertGps.fromWgs84ToBd09(wgs84[0], wgs84[1]);
		if (gps_bd09 != null) {
			if(D) Log.e(TAG, "fromWgs84ToBd09 经度:"+gps_bd09[0]);
			if(D) Log.e(TAG, "fromWgs84ToBd09 纬度:"+gps_bd09[1]);
		}else{
			if(D) Log.e(TAG, "gps_bd09 == null");
		}

		double[] gps_gcj02 = ConvertGps.fromWgs84ToGcj02(wgs84[0], wgs84[1]);
		if (gps_gcj02 != null) {
			if(D) Log.e(TAG, "fromWgs84ToGcj02 经度:"+gps_gcj02[0]);
			if(D) Log.e(TAG, "fromWgs84ToGcj02 纬度:"+gps_gcj02[1]);
		}else{
			if(D) Log.e(TAG, "gps_gcj02 == null");
		}

		double[] gps = BaiduJni.convert( wgs84[0], wgs84[1], BaiduJni.Bd09 ); // 不行
		if (gps != null) {
			if(D) Log.e(TAG, "aaaaaaaaaaaaaaaa:"+gps[0]);
			if(D) Log.e(TAG, "bbbbbbbbbbbbb:"+gps[1]);
		} else {
			if(D) Log.e(TAG, "gps == null");
		}

		gps = BaiduJni.convert( wgs84[0], wgs84[1], BaiduJni.Bd09ll ); // 行
		if (gps != null) {
			if(D) Log.e(TAG, "aaaaaaaaaaaaaaaa:"+gps[0]);
			if(D) Log.e(TAG, "bbbbbbbbbbbbb:"+gps[1]);
		} else {
			if(D) Log.e(TAG, "gps == null");
		}

		gps = BaiduJni.convert( wgs84[0], wgs84[1], BaiduJni.Gcj02 ); // 不行
		if (gps != null) {
			if(D) Log.e(TAG, "aaaaaaaaaaaaaaaa:"+gps[0]);
			if(D) Log.e(TAG, "bbbbbbbbbbbbb:"+gps[1]);
		} else {
			if(D) Log.e(TAG, "gps == null");
		}

		gps = BaiduJni.convert( wgs84[0], wgs84[1], BaiduJni.Gps2Gcj ); // 行，但与高等自带的方法有差
		if (gps != null) {
			if(D) Log.e(TAG, "aaaaaaaaaaaaaaaa:"+gps[0]);
			if(D) Log.e(TAG, "bbbbbbbbbbbbb:"+gps[1]);
		} else {
			if(D) Log.e(TAG, "gps == null");
		}

		gps = BaiduJni.convert( wgs84[0], wgs84[1], BaiduJni.Bd092Gcj ); // 不行
		if (gps != null) {
			if(D) Log.e(TAG, "aaaaaaaaaaaaaaaa:"+gps[0]);
			if(D) Log.e(TAG, "bbbbbbbbbbbbb:"+gps[1]);
		} else {
			if(D) Log.e(TAG, "gps == null");
		}

		gps = BaiduJni.convert( wgs84[0], wgs84[1], BaiduJni.Bd09ll2Gcj ); // 行
		if (gps != null) {
			if(D) Log.e(TAG, "aaaaaaaaaaaaaaaa:"+gps[0]);
			if(D) Log.e(TAG, "bbbbbbbbbbbbb:"+gps[1]);
		} else {
			if(D) Log.e(TAG, "gps == null");
		}

		//		07-03 11:41:51.090: E/ConvertGpsClient(14268): aaaaaaaaaaaaaaaa:114.137231
		//		07-03 11:41:51.090: E/ConvertGpsClient(14268): bbbbbbbbbbbbb:22.550767
		gps = ConvertGps.fromBd09ToGcj02(114.137231D, 22.550767D);
		if (gps != null) {
			if(D) Log.e(TAG, "fromBd09ToGcj02 经度:"+gps[0]);
			if(D) Log.e(TAG, "fromBd09ToGcj02 纬度:"+gps[1]);
		}else{
			if(D) Log.e(TAG, "gps == null");
		}

		//		GeoPoint point = CoordinateConvert.fromGpsToAMap(longitude, latitude);
		//		if (point != null) {
		//			if(D) Log.e(TAG, "ddd fromGpsToAMap 经度:"+point.getLongitudeE6());
		//			if(D) Log.e(TAG, "fromGpsToAMap 纬度:"+point.getLatitudeE6());
		//		}

//		double[] d = CoordinateConvert.fromSeveralGpsToAMap(wgs84);
//		if (d != null) {
//			if(D) Log.e(TAG, "ddd fromSeveralGpsToAMap 经度:"+d[0]);
//			if(D) Log.e(TAG, "aaaa fromSeveralGpsToAMap 纬度:"+d[1]);
//		}

		//		GeoPoint p2 = CoordinateConvert.bundleDecode(CoordinateConvert.fromWgs84ToBaidu(null));

		//		CoordinateConvert.bundleDecode(CoordinateConvert.fromWgs84ToBaidu(new GeoPoint((int)(gpsLat*1E6), (int)(gpsLon*1E6))))

	}

	public static void fromWgs84ToGcj02() {

		if(D) Log.e(TAG, "真实GPS坐标经度:"+longitude);
		if(D) Log.e(TAG, "真实GPS坐标纬度:"+latitude);

		double[] wgs84 = new double[2];
		wgs84[0] = longitude;
		wgs84[1] = latitude;

		double[] gps = ConvertGps.fromWgs84ToGcj02(longitude, latitude);
		if (gps != null) {
			if(D) Log.e(TAG, "amap fromWgs84ToGcj02 经度:"+gps[0]);
			if(D) Log.e(TAG, "amap fromWgs84ToGcj02 纬度:"+gps[1]);
		}

		gps = ConvertGps.baidu(longitude, latitude, BaiduJni.Gps2Gcj);
		if (gps != null) {
			if(D) Log.e(TAG, "baidu fromWgs84ToGcj02 经度:"+gps[0]);
			if(D) Log.e(TAG, "baidu fromWgs84ToGcj02 纬度:"+gps[1]);
		} else {
			if(D) Log.e(TAG, "gps == null");
		}

	}

	public static void fromGcj02ToWgs84Search() {

		if(D) Log.e(TAG, "真实GPS坐标经度:"+longitude);
		if(D) Log.e(TAG, "真实GPS坐标纬度:"+latitude);

		double[] wgs84 = new double[2];
		wgs84[0] = longitude;
		wgs84[1] = latitude;

		double[] gps = ConvertGps.fromWgs84ToGcj02(longitude, latitude);
		if (gps != null) {
			if(D) Log.e(TAG, "amap fromWgs84ToGcj02 经度:"+gps[0]);
			if(D) Log.e(TAG, "amap fromWgs84ToGcj02 纬度:"+gps[1]);
		}

		ConvertGps.fromGcj02ToWgs84Search(gps[0], gps[1]);
	}

	/**
	 * 差0.000001也算相等
	 * 
	 * @return
	 */
	public static boolean __tmp__compareNoLess(double value1, double value2) {

		if (value1 == value2) {
			if(D) Log.e(TAG, "value1 == value2");
			return true;
		}

		if (value1 > value2) {
			if(D) Log.e(TAG, "value1 > value2");
			double aaa = value1*1000000.0D-value2*1000000.0D;
			if(D) Log.e(TAG, "___aaa:"+aaa);
			if ( aaa <= 3.0D ) {
				if(D) Log.e(TAG, "(value1-value2) <= 0.000001D");
				if(D) Log.e(TAG, "value1:"+value1+",value2:"+value2+" == true");
				// 相等
				return true;
			} else {
				if(D) Log.e(TAG, "aaaa:"+(value1-value2));
				if(D) Log.e(TAG, "value1:"+value1+",value2:"+value2+" == false");
				return false;
			}
		} else {
			if(D) Log.e(TAG, "value1 < value2");
			double aaa = value2*1000000.0D-value1*1000000.0D;
			if ( aaa <= 3.0D ) {
				if(D) Log.e(TAG, "(value2-value1) <= 0.000001D");
				if(D) Log.e(TAG, "value1:"+value1+",value2:"+value2+" == true");
				// 相等
				return true;
			} else {
				if(D) Log.e(TAG, "bbbb:"+(value2-value1));
				if(D) Log.e(TAG, "value1:"+value1+",value2:"+value2+" == false");
				return false;
			}
		}

	}

	/**
	 * 差0.000001也算相等
	 * 
	 * compareNoLess(114.896756, 30.978435, 0.000003D);
	 * 
	 * @return
	 */
	public static boolean compareNoLess(double value1, double value2, double decimalsRange) {
		if(D) Log.d(TAG, "compareNoLess(value1:"+value1+",value2:"+value2+",decimalsRange:"+decimalsRange+")");

		double differ = 0.0D;

		if (value1 == value2) {
			if(D) Log.d(TAG, "compareNoLess() ---> value1 == value2");
			//			if(D) Log.e(TAG, "compareNoLess():differ:"+differ);
			//			if(D) Log.e(TAG, "compareNoLess(value1:"+value1+",value2:"+value2+",decimalsRange:"+decimalsRange+") == true");
			//			return true;
		} else if (value1 > value2) {
			if(D) Log.d(TAG, "compareNoLess() ---> value1 > value2");
			differ = value1 - value2;
		} else {
			if(D) Log.d(TAG, "compareNoLess() ---> value1 < value2");
			differ = value2 - value1;
		}

		if(D) Log.e(TAG, "compareNoLess():differ:"+differ);
		if ( differ <= decimalsRange ) {
			// 相减不超过decimalsRange
			if(D) Log.e(TAG, "compareNoLess(value1:"+value1+",value2:"+value2+",decimalsRange:"+decimalsRange+") == true");
			return true;
		} else {
			// 相减已超过decimalsRange
			if(D) Log.e(TAG, "compareNoLess(value1:"+value1+",value2:"+value2+",decimalsRange:"+decimalsRange+") == false");
			return false;
		}

	}

	/**
	 * 从数据库里取全国经纬数据，一个一个的测试。
	 */
	public static void fromGcj02ToWgs84Search__SQLite(Context context) {

//		//		SQLiteInfo sqliteInfo = new SQLiteInfo(context, "sfsfes.db", 1);
//
//		Log.e(TAG, "fromGcj02ToWgs84Search__SQLite() --- start --- ");
//
//		// 加载百度定位库，里面有坐标转换方法
//		ConvertGps.initLibrary("locSDK4b");
//
//		File file = new File("/mnt/sdcard/aaa.db");
//		SQLiteInfo sqliteInfo = new SQLiteInfo(file, 1);
//		SQLiteHelper sqliteHelper = new SQLiteHelper(context, sqliteInfo.getDBName(), sqliteInfo.getVersion());
//
//		SQLiteManager sqliteManager = new SQLiteManager(context, sqliteInfo, sqliteHelper);
//		Log.e(TAG, "open():"+sqliteManager.open());
//		Log.e(TAG, "isOpen():"+sqliteManager.isOpen());
//
//		Log.e(TAG, "size:"+sqliteManager.getRowCount(AddressInfo.class));
//
//		int count = 0;
//		int countA = 0; // 相等总数量
//		int countB = 0; // 不相等总数量
//
//		/*
//		 * 纠偏出错数据
//		 * 
//		 * count == 999
//		 * GPS坐标(数据来源于sqlite)Lng:120.573229
//		 * GPS坐标(数据来源于sqlite)Lat:30.001005
//		 * 得出的结果相减为-0.000026
//		 * 
//		 * count == 1647
//		 * GPS坐标(数据来源于sqlite)Lng:112.524571
//		 * GPS坐标(数据来源于sqlite)Lat:33.000337
//		 * 得出的结果相减为-0.000022
//		 * 
//		 * count == 1700
//		 * GPS坐标(数据来源于sqlite)Lng:114.354853
//		 * GPS坐标(数据来源于sqlite)Lat:33.000411
//		 * 得出的结果相减为-0.000022
//		 * 
//		 * count == 1863
//		 * GPS坐标(数据来源于sqlite)Lng:111.274962
//		 * GPS坐标(数据来源于sqlite)Lat:27.000691
//		 * 得出的结果相减为-0.000015
//		 * 
//		 * count == 2834
//		 * GPS坐标(数据来源于sqlite)Lng:106.937931
//		 * GPS坐标(数据来源于sqlite)Lat:33.001519
//		 * 得出的结果相减为-0.000059 == 物理约6米
//		 * 
//		 * count == 3009
//		 * GPS坐标(数据来源于sqlite)Lng:97.029222
//		 * GPS坐标(数据来源于sqlite)Lat:33.001134
//		 * 得出的结果相减为-0.000021
//		 * 
//		 * 
//		 * 
//		 * */
//
//		List<AddressInfo> list = sqliteManager.queryAll(AddressInfo.class);
//		if (list != null) {
//			Log.e(TAG, "list != null.");
//			for (AddressInfo info : list) {
//
//				count++;
//				Log.e(TAG, "当前 for current count:"+count);
//
//				//				if (count < 997) {
//				//					continue;
//				//				}
//
//				if (count == 999 || count == 1647 || count == 1700 || count == 1863 || count == 2834 || count == 3009) {
//					Log.e(TAG, "出错的坐标count:"+count);
//					Log.e(TAG, "出错的坐标Lng:"+info.getNewLng());
//					Log.e(TAG, "出错的坐标Lat:"+info.getNewlat());
//					//					try {
//					//						Thread.sleep(5000);
//					//					} catch (InterruptedException e) {
//					//						e.printStackTrace();
//					//					}
//					//					continue;
//				}
//
//				if (info != null) {
//					double sqliteLng = info.getNewLng();
//					double sqliteLat = info.getNewlat();
//					if(D) Log.e(TAG, "GPS坐标(数据来源于sqlite)Lng:"+sqliteLng);
//					if(D) Log.e(TAG, "GPS坐标(数据来源于sqlite)Lat:"+sqliteLat);
//
//					double[] gcj02 = ConvertGps.fromWgs84ToGcj02(sqliteLng, sqliteLat);
//					if(D) Log.e(TAG, "GPS坐标转成高德坐标Lng:"+gcj02[0]);
//					if(D) Log.e(TAG, "GPS坐标转成高德坐标lat:"+gcj02[1]);
//
//					double[] gps = ConvertGps.fromGcj02ToWgs84Search(gcj02[0], gcj02[1]);
//					if(D) Log.e(TAG, "高德坐标转成GPS坐标Lng:"+gps[0]);
//					if(D) Log.e(TAG, "高德坐标转成GPS坐标Lat:"+gps[1]);
//
//					double roundLng = ConvertGps.roundDecimals(gps[0], 6);
//					double roundLat = ConvertGps.roundDecimals(gps[1], 6);
//					if(D) Log.e(TAG, "上述坐标四舍五入Lat:"+roundLng);
//					if(D) Log.e(TAG, "上述坐标四舍五入Lng:"+roundLat);
//
//					//					if (roundLng == sqliteLng && roundLat == sqliteLat) {
//					//						if(D) Log.e(TAG, " == 反纠偏高德坐标与原GPS坐标相等.");
//					//						countA++;
//					//					}else{
//					//						if(D) Log.e(TAG, " != 反纠偏高德坐标与原GPS坐标不相等.");
//					//						countB++;
//					//					}
//
//					if (compareNoLess(sqliteLng, roundLng, 0.000061D) && 
//							compareNoLess(sqliteLat, roundLat, 0.000061D)) {
//						if(D) Log.e(TAG, " == 反纠偏高德坐标与原GPS坐标相等.");
//						countA++;
//					}else{
//						if(D) Log.e(TAG, " != 反纠偏高德坐标与原GPS坐标不相等.");
//						countB++;
//						if(D) Log.e(TAG, " 222 GPS坐标(数据来源于sqlite)Lng:"+sqliteLng);
//						if(D) Log.e(TAG, " 222 GPS坐标(数据来源于sqlite)Lat:"+sqliteLat);
//						break;
//					}
//				}
//			}
//
//			Log.e(TAG, "相等数量:"+countA);
//			Log.e(TAG, "不相等数量:"+countB);
//
//		}else{
//			Log.e(TAG, "list == null.");
//		} // end if
//
//		Log.e(TAG, "fromGcj02ToWgs84Search__SQLite() --- end --- ");

	}

	/**
	 * 全国经纬数据，一个一个的测试。
	 */
	public static void fromGcj02ToWgs84Search__AllChina(Context context) {

		Log.e(TAG, "fromGcj02ToWgs84Search__AllChina() --- start --- ");

		// 加载百度定位库，里面有坐标转换方法
		ConvertGps.initLibrary("locSDK4b");

		int count = 0;
		int countA = 0; // 相等总数量
		int countB = 0; // 不相等总数量

		/*
		 * 纠偏出错数据
		 * 
		 * */

		// TODO 假设中国是一个正方形，此正方形的左下角坐标为
		// 73.5,2.1 而正方形的右上角为135.1,53.6

		double start_lng = 73.5D;
		double start_lat = 2.1D;

		double end_lng = 135.1D;
		double end_lat = 53.6D;
		// 515000 616000
		// 3172.4亿个0.0001
		double offset = 0.000100D; // 100

		//		List<AddressInfo> list = sqliteManager.queryAll(AddressInfo.class);
		//		if (list != null) {
		//			Log.e(TAG, "list != null.");
		//			for (AddressInfo info : list) {
		for (int n = 0; n < 10; n++)

			count++;
		Log.e(TAG, "当前 for current count:"+count);

		//				if (count < 997) {
		//					continue;
		//				}

		if (count == 999 || count == 1647 || count == 1700 || count == 1863 || count == 2834 || count == 3009) {
			Log.e(TAG, "出错的坐标count:"+count);
			//			Log.e(TAG, "出错的坐标Lng:"+info.getNewLng());
			//			Log.e(TAG, "出错的坐标Lat:"+info.getNewlat());
			//					try {
			//						Thread.sleep(5000);
			//					} catch (InterruptedException e) {
			//						e.printStackTrace();
			//					}
			//					continue;
		}

		double sqliteLng = 0.0D;
		double sqliteLat = 0.0D;
		if(D) Log.e(TAG, "GPS坐标(数据来源于sqlite)Lng:"+sqliteLng);
		if(D) Log.e(TAG, "GPS坐标(数据来源于sqlite)Lat:"+sqliteLat);

		double[] gcj02 = ConvertGps.fromWgs84ToGcj02(sqliteLng, sqliteLat);
		if(D) Log.e(TAG, "GPS坐标转成高德坐标Lng:"+gcj02[0]);
		if(D) Log.e(TAG, "GPS坐标转成高德坐标lat:"+gcj02[1]);

		double[] gps = ConvertGps.fromGcj02ToWgs84Search(gcj02[0], gcj02[1]);
		if(D) Log.e(TAG, "高德坐标转成GPS坐标Lng:"+gps[0]);
		if(D) Log.e(TAG, "高德坐标转成GPS坐标Lat:"+gps[1]);

		double roundLng = ConvertGps.roundDecimals(gps[0], 6);
		double roundLat = ConvertGps.roundDecimals(gps[1], 6);
		if(D) Log.e(TAG, "上述坐标四舍五入Lat:"+roundLng);
		if(D) Log.e(TAG, "上述坐标四舍五入Lng:"+roundLat);

		//					if (roundLng == sqliteLng && roundLat == sqliteLat) {
		//						if(D) Log.e(TAG, " == 反纠偏高德坐标与原GPS坐标相等.");
		//						countA++;
		//					}else{
		//						if(D) Log.e(TAG, " != 反纠偏高德坐标与原GPS坐标不相等.");
		//						countB++;
		//					}

		if (compareNoLess(sqliteLng, roundLng, 0.000061D) && 
				compareNoLess(sqliteLat, roundLat, 0.000061D)) {
			if(D) Log.e(TAG, " == 反纠偏高德坐标与原GPS坐标相等.");
			countA++;
		}else{
			if(D) Log.e(TAG, " != 反纠偏高德坐标与原GPS坐标不相等.");
			countB++;
			if(D) Log.e(TAG, " 222 GPS坐标(数据来源于sqlite)Lng:"+sqliteLng);
			if(D) Log.e(TAG, " 222 GPS坐标(数据来源于sqlite)Lat:"+sqliteLat);
			//			break;
		}

		Log.e(TAG, "相等数量:"+countA);
		Log.e(TAG, "不相等数量:"+countB);

		//		}else{
		//			Log.e(TAG, "list == null.");
		//		} // end if

		Log.e(TAG, "fromGcj02ToWgs84Search__AllChina() --- end --- ");

	}
}
