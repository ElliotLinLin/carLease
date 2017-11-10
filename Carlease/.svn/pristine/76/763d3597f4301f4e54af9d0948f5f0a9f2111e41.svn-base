package com.tools.baidu.map;


import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.utils.DistanceUtil;
import com.tools.util.Log;

public class BMapTool {

	private static final String TAG = BMapTool.class.getSimpleName();
	private static final boolean DEBUG = true;

	public static float defaultZoom = 15.0F;

	
	/**
	 * 设置地图的中心显示点 (纬度,经度)
	 * 
	 * @param aMap
	 * @param point
	 * @param zoom
	 */
	public static void setCenterPoint(com.baidu.mapapi.map.BaiduMap aMap,
			LatLng point) {
		if (aMap == null) {
			throw new NullPointerException(" aMap is null");
		}
		if (point == null) {
			throw new NullPointerException(" point  is null");
		}
		Log.e(TAG, "setCenterPoint()");

		// LatLng marker = new LatLng(point.getLatitude(),
		// point.getLongitude());
		// zoom 4 - 20
		// aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker, zoom));

		MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(point);
		try{
			aMap.setMapStatus(u);
		}catch (NullPointerException e){
			return;
		}

	}
	
	/**
	 * 设置地图的中心显示点 (纬度,经度)
	 * 
	 * @param aMap
	 * @param point
	 * @param zoom
	 */
	public static void setCenterPoint(com.baidu.mapapi.map.BaiduMap aMap,
			LatLng point, float zoom) {
		if (aMap == null) {
			throw new NullPointerException(" aMap is null");
		}
		if (point == null) {
			throw new NullPointerException(" point  is null");
		}
		Log.e(TAG, "setCenterPoint()");

		// LatLng marker = new LatLng(point.getLatitude(),
		// point.getLongitude());
		// zoom 4 - 20
		// aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker, zoom));

		MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(point);
		try{
			aMap.setMapStatus(u);
		}catch (NullPointerException e){
			return;
		}

		// 设置地图缩放比例
		MapStatusUpdate ms = MapStatusUpdateFactory.zoomTo(zoom);
		aMap.setMapStatus(ms);
	}

	/**
	 * 设置地图的中心显示点 (纬度,经度)
	 * 
	 * @param aMap
	 * @param point
	 * @param zoom
	 */
	public static void setCenterPointAnim(com.baidu.mapapi.map.BaiduMap aMap,
			LatLng point, float zoom, int t) {
		if (aMap == null) {
			throw new NullPointerException(" aMap is null");
		}
		if (point == null) {
			throw new NullPointerException(" point  is null");
		}
		Log.e(TAG, "setCenterPoint()");
		// LatLng marker = new LatLng(point.getLatitude(),
		// point.getLongitude());
		// zoom 4 - 20
		// aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker,
		// zoom),t,callback);
		MyLocationData myLoc = new MyLocationData.Builder()
				.latitude(point.latitude).longitude(point.longitude).build();
		// 设置定位数据
		aMap.setMyLocationData(myLoc);

		// 设置地图缩放比例
		MapStatusUpdate ms = MapStatusUpdateFactory.zoomTo(zoom);
		aMap.animateMapStatus(ms, t);
	}

	/**
	 * 改变地图中心，保持地图层级
	 * 
	 * @param aMap
	 * @param point
	 * @date 2013-12-30 上午11:06:34
	 * @author aaa
	 */
	// public static void setCenterPoint(com.amap.api.maps.AMap aMap, LatLng
	// point) {
	// if(aMap==null||point==null){
	// return;
	// }
	// Log.e(TAG, "setCenterPoint()");
	// // zoom 4 - 20
	// aMap.animateCamera(CameraUpdateFactory.changeLatLng(point));
	// }

	/**
	 * 添加标注，默认图片 例子： AMapTool.addMarker(aMap, point,
	 * BitmapDescriptorFactory.HUE_RED, title, content);
	 * 
	 * @param aMap
	 * @param point
	 * @param color
	 * @param title
	 *            标题
	 * @param content
	 *            内容
	 * @return
	 */
	// public static com.amap.api.maps.model.Marker addMarker(
	// com.amap.api.maps.AMap aMap,
	// LatLonPoint point,
	// float color,
	// String title,
	// String content) {
	// LatLng latlng = new LatLng(point.getLatitude(), point.getLongitude());
	// MarkerOptions options = new MarkerOptions();
	// // 位置
	// options.position(latlng);
	// // 标题
	// options.title(title);
	// // 内容
	// options.snippet(content);
	// // 设置标记是否可拖动
	// options.draggable(true);
	// // 图标
	// options.icon(BitmapDescriptorFactory.defaultMarker(color));
	// // 添加
	// return aMap.addMarker(options);
	// }

	/**
	 * 使用Asset添加标注
	 * 
	 * @param point
	 * @param resId
	 */
	// public static com.amap.api.maps.model.Marker addMarker(
	// com.amap.api.maps.AMap aMap,
	// LatLonPoint point,
	// String assetName,
	// String title,
	// String content) {
	//
	// LatLng latlng = new LatLng(point.getLatitude(), point.getLongitude());
	// MarkerOptions options = new MarkerOptions();
	// // 位置
	// options.position(latlng);
	// // 标题
	// options.title(title);
	// // 内容
	// options.snippet(content);
	// // 设置标记是否可拖动
	// options.draggable(true);
	// // 图标
	// // TODO 下述高德地图SDK的方法有问题，图片的ID会跑偏，
	// // 建议采用fromAsset(...)方法
	// options.icon(BitmapDescriptorFactory.fromAsset(assetName));
	// // 添加
	// return aMap.addMarker(options);
	// }

	/**
	 * 更新Marker
	 * 
	 * @param marker
	 * @param point
	 * @param bitmap
	 * @param infoTitle
	 * @param infoText
	 */
	public static void updateMarker(Marker marker, LatLng point,
			android.graphics.Bitmap bitmap) {
		if (marker == null || point == null || bitmap == null) {
			return;
		}
		try {
			// 标题
			// marker.setTitle(infoTitle);
			// 内容
			// marker.setSnippet(infoText);
			// 坐标
			// marker.setPosition(LatLonPoint2LatLng(point));
			// // 图片
			// marker.setIcon(BitmapDescriptorFactory.fromBitmap(bitmap));
			// // 通过隐藏和显示来更新内容，否则内容不会自动更新。
			// if (marker.isInfoWindowShown()) {
			// marker.hideInfoWindow();
			// marker.showInfoWindow();
			// }
		} catch (Exception e) {
			return;
		}
	}

	/**
	 * 更新Marker信息
	 * 
	 * @param marker
	 * @param point
	 * @param bitmap
	 * @param infoTitle
	 * @param infoText
	 */
	public static void updateMarkerInfo(Marker marker) {
		if (marker == null) {
			return;
		}
		try {
			// 标题
			// marker.setTitle(infoTitle);
			// 内容
			// marker.setSnippet(infoText);
			// 坐标
			// marker.setPosition(LatLonPoint2LatLng(point));
			// 图片
			// marker.setIcon(BitmapDescriptorFactory.fromBitmap(bitmap));
			// 通过隐藏和显示来更新内容，否则内容不会自动更新。
			// if (marker.isInfoWindowShown()) {
			// marker.hideInfoWindow();
			// marker.showInfoWindow();
			// }
		} catch (Exception e) {
			return;
		}
	}

	/**
	 * 清空地图上所有已经标注的Marker
	 */
	public static void clear(BaiduMap aMap) {
		aMap.clear();
	}

	/**
	 * 清空指定Marker
	 */
	public static void clearMarker(Marker marker) {
		if (marker != null) {
			// marker.remove();
			// marker.destroy();
			marker.remove();
		}
	}

//	/**
//	 * 把LatLng对象转化为LatLonPoint对象（LatLng是角度）
//	 */
//	public static LatLonPoint LatLng2LatLonPoint(LatLng latlon) {
//		return new LatLonPoint(latlon.latitude, latlon.longitude);
//	}
//
//	/**
//	 * 把LatLonPoint对象转化为LatLng对象（LatLng是角度）
//	 */
//	public static LatLng LatLonPoint2LatLng(LatLonPoint point) {
//		return new LatLng(point.getLatitude(), point.getLongitude());
//	}

//	/**
//	 * 把LatLonPoint对象转化为LatLng对象（LatLng是角度）
//	 */
//	public static LatLng LatLonPoint2LatLng(
//			com.amap.api.services.core.LatLonPoint point) {
//		return new LatLng(point.getLatitude(), point.getLongitude());
//	}

	/**
	 * 计算两点之间的距离
	 * 
	 * @return
	 */
	public static float calculateLineDistance(LatLng a, LatLng b) {
		return (float) DistanceUtil.getDistance(a, b);
	}

//	/**
//	 * 搜索POI
//	 * 
//	 * @param context
//	 * @param poiName
//	 * @return
//	 */
//	public static PoiPagedResult searchPOI(Context context, String poiName) {
//		// 设置搜索字符串，"010为城市区号"
//		PoiSearch poiSearch = new PoiSearch(context, new PoiSearch.Query(
//				poiName, PoiTypeDef.All));
//		poiSearch.setPageSize(50);// 设置搜索每次最多返回结果数
//		// 设置搜索范围
//		// poiSearch.setBound(new SearchBound(point, radiusInMeter));
//		PoiPagedResult result = null;
//		try {
//			result = poiSearch.searchPOI();
//			if (result != null) {
//				int count = result.getPageCount();
//				Log.e(TAG, "PoiPagedResult count:" + count);
//			}
//		} catch (AMapException e) {
//			e.printStackTrace();
//		}
//		return result;
//	}

//	/**
//	 * 搜索周边POI
//	 * 
//	 * @param context
//	 * @param poiName
//	 * @param point
//	 * @param radiusInMeter
//	 * @return
//	 */
//	public static PoiPagedResult searchNearbyPOI(Context context,
//			String poiName, LatLonPoint point, int radiusInMeter) {
//		// 设置搜索字符串，"010为城市区号"
//		PoiSearch poiSearch = new PoiSearch(context, new PoiSearch.Query(
//				poiName, PoiTypeDef.All));
//		poiSearch.setPageSize(50);// 设置搜索每次最多返回结果数
//		// 设置搜索范围
//		poiSearch.setBound(new SearchBound(point, radiusInMeter));
//		PoiPagedResult result = null;
//		try {
//			result = poiSearch.searchPOI();
//			if (result != null) {
//				int count = result.getPageCount();
//				Log.e(TAG, "PoiPagedResult count:" + count);
//			}
//		} catch (AMapException e) {
//			e.printStackTrace();
//		}
//		return result;
//	}

	/**
	 * 开始进行poi搜索
	 */
//	public static PoiResult searchPoiAll(Context context, String keyword,
//			com.amap.api.services.core.LatLonPoint lp) {
//		if (context == null || keyword == null || lp == null) {
//			return null;
//		}
//		PoiResult pois = null;
//		com.amap.api.services.poisearch.PoiSearch.Query
//
//		query = new com.amap.api.services.poisearch.PoiSearch.Query
//
//		(keyword, "", "");// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
//		query.setPageSize(50);// 设置每页最多返回多少条poiitem
//		query.setPageNum(0);// 设置查第一页
//
//		if (lp != null) {
//			com.amap.api.services.poisearch.PoiSearch
//
//			poiSearch = new com.amap.api.services.poisearch.PoiSearch(context,
//					query);
//			poiSearch
//					.setBound(new com.amap.api.services.poisearch.PoiSearch.SearchBound(
//							lp, 2000, true));//
//			try {
//				pois = poiSearch.searchPOI();
//			} catch (com.amap.api.services.core.AMapException e) {
//				e.printStackTrace();
//			}
//		}
//		return pois;
//	}

//	public static void toStringPoiItem(PoiItem poi) {
//		if (DEBUG)
//			Log.e(TAG, "--- start ---");
//		if (DEBUG)
//			Log.e(TAG, "toString:" + poi.toString());
//		if (DEBUG)
//			Log.e(TAG, "getAdCode:" + poi.getAdCode());
//		if (DEBUG)
//			Log.e(TAG, "getCityCode:" + poi.getCityCode());
//		if (DEBUG)
//			Log.e(TAG, "getDistance:" + poi.getDistance());
//		if (DEBUG)
//			Log.e(TAG, "getPoiId:" + poi.getPoiId());
//		if (DEBUG)
//			Log.e(TAG, "getSnippet:" + poi.getSnippet());
//		if (DEBUG)
//			Log.e(TAG, "getTel:" + poi.getTel());
//		if (DEBUG)
//			Log.e(TAG, "getTitle:" + poi.getTitle());
//		if (DEBUG)
//			Log.e(TAG, "getXmlNode:" + poi.getXmlNode());
//		if (DEBUG)
//			Log.e(TAG, "getLatitude:" + poi.getPoint().getLatitude());
//		if (DEBUG)
//			Log.e(TAG, "getLongitude:" + poi.getPoint().getLongitude());
//		if (DEBUG)
//			Log.e(TAG, "--- end ---");
//	}

//	/**
//	 * 打印
//	 * 
//	 * @param location
//	 */
//	public static void toStringAMapLocation(AMapLocation location) {
//		if (location != null) {
//			Double geoLat = location.getLatitude();
//			Double geoLng = location.getLongitude();
//			String cityCode = "";
//			String desc = "";
//			Bundle locBundle = location.getExtras();
//			if (locBundle != null) {
//				cityCode = locBundle.getString("citycode");
//				desc = locBundle.getString("desc");
//			}
//			// String str = ("定位成功:(" + geoLng + "," + geoLat + ")"
//			// + "\n精    度    :" + location.getAccuracy() + "米"
//			// + "\n定位方式:" + location.getProvider() + "\n定位时间:"
//			// + AMapUtil.convertToTime(location.getTime()) + "\n城市编码:"
//			// + cityCode + "\n位置描述:" + desc + "\n省:"
//			// + location.getProvince() + "\n市：" + location.getCity()
//			// + "\n区(县)：" + location.getDistrict() + "\n城市编码："
//			// + location.getCityCode() + "\n区域编码：" + location.getAdCode());
//			// Log.e(TAG, "AMapLocation String:"+str);
//		}
//	}

	/**
	 * 对PoiItem进行从近到远排序
	 * 
	 * @param poi
	 * @return
	 */
//	public static PoiItem sortPoiItem(PoiItem poi) {
//		return poi;
//	}

	/**
	 * 将两点同时显示在地图上, TODO 只能在onMapLoaded之后使用
	 * 
	 * // java.lang.IllegalArgumentException: southern 南方 latitude // exceeds
	 * northern 北方 latitude (22.55684 > 22.5449) // setBounds(myPoint,
	 * carPoint);
	 * 
	 * 
	 */
	/**
	 * @param aMap
	 * @param startPoint
	 * @param endPoint
	 * @date 2013-12-20 上午11:19:12
	 * @author aaa
	 */
	public static void setBounds(BaiduMap aMap,
			LatLng startPoint, LatLng endPoint) {
		if (aMap == null || startPoint == null || endPoint == null) {
			return;
		}
		// 设置所有maker显示在View中
		LatLngBounds bounds = new LatLngBounds.Builder()
				.include(new LatLng(startPoint.latitude, startPoint.longitude))
				.include(new LatLng(endPoint.latitude, endPoint.longitude)).build();
		// 移动地图，所有marker自适应显示。LatLngBounds与地图边缘10像素的填充区域
		// aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 10));

	}
}
