package com.tools.net.http;



/**
 * HTTP的Header字段
 * 
 * @author LMC
 *
 */
public class HttpHeaderField {

	// +++ 请求字段
	public static final String Field_Content_Type = "Content-Type"; // Content-Type: text/html; charset=utf-8
	public static final String Field_Charset = "Charset";
	public static final String Field_Content_Length = "Content-Length"; // Content-Length: 348
	public static final String Field_Content_Language = "Content-Language"; // Content-Language: en,zh

	public static final String Field_Connection = "Connection"; // Connection: close  Connection: keep-alive

	public static final String Field_Pragma = "Pragma"; // Pragma:no-cache

	public static final String Field_Cookie = "Cookie"; // Cookie:ASP.NET_SessionId=kujejueq12yks445zmsuby3w; path=/; HttpOnly
	
	// http://jingyan.baidu.com/article/ceb9fb100516e88cac2ba076.html
	public static final String Field_Accept_Encoding = "Accept-Encoding"; // Accept-Encoding: gzip, deflate
	
	// 文件传输编码	Transfer-Encoding:chunked
	public static final String Field_Transfer_Encoding = "Transfer-Encoding";
	public static final String Field_Content_MD5 = "Content-MD5"; // Content-MD5: Q2hlY2sgSW50ZWdyaXR5IQ==
	public static final String Field_Content_Range = "Content-Range"; // Content-Range: bytes 21010-47021/47022

	// +++ 响应字段
	public static final String Field_Content_Encoding = "Content-Encoding"; // Content-Encoding: gzip
	
	public static final String Field_Set_Cookie = "Set-Cookie"; // Set-Cookie: UserID=JohnDoe; Max-Age=3600; Version=1

	public static final String Field_Cache_Control = "Cache-Control"; // Cache-Control: no-cache Cache-Control: max-age=0

	public static final String Field_User_Agent = "User-Agent"; // User-Agent: Mozilla/5.0 (Linux; X11)
	public static final String Field_Agent = "Accept"; // Accept: */*
	public static final String Field_Accept_Language = "Accept-Language"; // Accept-Language: en,zh

	public static final String Field_Accept_Charset = "Accept-Charset"; // Accept-Encoding: utf-8

	// http://blog.csdn.net/rainysia/article/details/8131174
	public static final String Field_Allow = "Allow"; //
	public static final String Field_Date = "Date"; //
	public static final String Field_Expires = "Expires"; //
	public static final String Field_Last_Modified = "Last-Modified"; // Last-modified:Tue,17Apr200106:46:28GMT 
	public static final String Field_Location = "Location"; //
	public static final String Field_Refresh = "Refresh"; //
	public static final String Field_Server = "Server"; // Server:Apache/1.3.14(Unix) 
	public static final String Field_www_Authenticate = "www-Authenticate"; //
	public static final String Field_Etag = "Etag"; // Etag:"a030f020ac7c01:1e9f" 

	// +++ 值
	public static final String Connection_Close = "close";
	public static final String Connection_Keep_Alive = "keep-alive";

	public static final String Transfer_Encoding_Chunked = "chunked";

	public static final String Cache_Control_No_Cache = "no-cache";
	public static final String Cache_Control_Private = "private";

	// 可以看出deflate是最核心的算法，而zlib和gzip格式的区别仅仅是头部和尾部不一样，
	// 而实际的内容都是deflate编码的
	// gzip = gzip头(10字节) + deflate编码的实际内容 + gzip尾(8字节)
	// zlib = zlib头 + deflate编码的实际内容 + zlib尾
	// 有一些是Content-Encoding: gzip
	// 有一些是Content-Encoding: gzip, deflate
	// 请求Accept-Encoding:"gzip, deflate" (客户端默认设置此项)
	// 响应Content-Encoding:"gzip, deflate"
	public static final String Encoding_GZIP = "gzip";
	public static final String Encoding_Deflate = "deflate";
	public static final String Encoding_GZIP_Deflate = "gzip, deflate";

	public static final String Encoding_None = "none";

}
