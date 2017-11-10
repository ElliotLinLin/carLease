

地球坐标系 (WGS-84) 到火星坐标系 (GCJ-02) 的转换算法  Google 搜索 "wgtochina_lb" 
http://blog.csdn.net/coolypf/article/details/8686588


火星坐标系 (GCJ-02) 与百度坐标系 (BD-09) 的转换算法 
http://blog.csdn.net/coolypf/article/details/8569813


WGS-84 -> GCJ-02 C#算法
Offline Navigation for Windows Phone 7
https://on4wp7.codeplex.com/SourceControl/changeset/view/21483#353936

WGS-84 地球坐标系
GCJ-02 火星坐标系
BD-09  百度坐标系

WGS-84 -> GCJ-02 == 有方法
WGS-84 <- GCJ-02 == 无方法

GCJ-02 -> BD-09 == 有方法
GCJ-02 <- BD-09 == 有方法

WGS-84 -> BD-09 == 有方法
WGS-84 <- BD-09 == 无方法


//高德api
//从wgs84坐标系转到gcj02坐标系
CoordinateConvert.fromGpsToAMap(double arg0, double arg1);
CoordinateConvert.fromSeveralGpsToAMap(String arg0);
CoordinateConvert.fromSeveralGpsToAMap(String arg0);


//百度api
CoordinateConvert.fromGcjToBaidu(GeoPoint geoPoint);
//从gcj02坐标系转到bd09坐标系
CoordinateConvert.fromWgs84ToBaidu(GeoPoint geoPoint);
//从wgs84坐标系转到bd09坐标系


地球坐标-火星坐标-百度坐标及之间的转换算法 C#
http://www.cnblogs.com/kelite/p/3549390.html


火星坐标系转换扩展。Earth（国外 WGS84）, mars（国内 GCJ-02）, bearPaw（百度&
http://blog.sina.com.cn/s/blog_6f9200bd0101f5yt.html


百度常见问题
http://developer.baidu.com/map/question.htm#qa0043


百度坐标转换API
http://developer.baidu.com/map/changeposition.htm


找到百度的 API 转换方法为: 
http://api.map.baidu.com/ag/coord/convert?from=0&to=4&x=114.125406&y=22.546981

API 	坐标系
百度地图API 		百度坐标
腾讯搜搜地图API 	火星坐标
搜狐搜狗地图API 	搜狗坐标
阿里云地图API 		火星坐标
图吧MapBar地图API 	图吧坐标
高德MapABC地图API 	火星坐标
灵图51ditu地图API 	火星坐标







