package com.hst.Carlease.ram;



/**
 * 
 * 各种基本类型所占比个位数如下
boolean   1 
byte         8 
char        Unicode(16) 
short      16 
int          32
float       32
long       64
double   64 

int占32位，共4个字节。

 * @author LMC
 *
 */
public class PushRam {

	public static final short ITSMSG_LOGIN = 0x0001; // 客户端登录
	public static final int ITSMSG_LOGIN_RESP = 0x8001;// 客户端登录响应  // TODO 这个是ITS系统一直在用的
	public static final short ITSMSG_TEST = 0x1002; // 链路测试
	public static final int ITSMSG_TEST_RESP = 0x8002; // 链路测试响应

	// 命令ID
	public static final short ITSMSG_OBD_PUSH_MESSAGE = 0x1031; // OBD消息推送 4145
	public static final char ITSMSG_OBD_PUSH_MESSAGE_RESP = 0x8031; // OBD消息推送响应

	/*byte 1字节
short 2字节
int 4字节
long 8字节
float 4字节
double 8字节
char 2字节
boolean 1字节*/
	// TODO ITSMSG_OBD_PUSH_MESSAGE_RESP超过了short最大值，所以
	//	public static final char ITSMSG_OBD_PUSH_MESSAGE_RESP_1 = 0x3180; // OBD消息推送响应
	//	public static final char ITSMSG_OBD_PUSH_MESSAGE_RESP_1 = 0x8031; // OBD消息推送响应

	public static final int ITSMSG_HEADER_SIZE = 10; // 包头大小

}
