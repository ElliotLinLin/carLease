package com.tools.util;

import java.io.File;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import android.content.Context;

/**
 * 日记记录器  使用文件记录器Logger和格式转换器Formatter
 * @author aaa
 *
 */
public class StatusLog {

	//文件记录器 
	private static Logger fileLogger;
	//单例
	private static StatusLog INSTANCE = null;   
	//log目录
	private static String PATH_LOGCAT;   
	//当前log文件路径
	private static String CURR_LOG_PATH;
	//日志文件最大值，默认1M  
	private   int LOG_FILE_MAX_SIZE = 1 * 1024 * 1024; 
	//日志文件保存期限，默认3天
	private  int LOG_FILE_LIFE=3;
	//上下文context
	private Context context;


	 
	
	/**
	 * 获取日志目录
	 * @param context
	 * @return
	 */
//	private static String  getFileDir(){
//		String fileDir="";
//		if(Environment.getExternalStorageState().equals(   
//				Environment.MEDIA_MOUNTED)) {
//			fileDir = Environment.getExternalStorageDirectory()   
//					.getAbsolutePath()+SDRam.getStatusLogPath() ;   
//		} else{
//			// 如果SD卡不存在，就保存到本应用的目录下    
//			fileDir = "/data/data/app/com.aaaaaaaaaaa.com"+SDRam.getStatusLogPath();   
//		}   
//		File file = new File(fileDir);   
//		if(!file.exists()) {   
//			file.mkdirs();   
//		}   
//		return fileDir;
//	}

	public static String getPATH_LOGCAT() {
		return PATH_LOGCAT;
	}
	public static void setPATH_LOGCAT(String pATH_LOGCAT) {
		PATH_LOGCAT = pATH_LOGCAT;
	}
	public  int getLOG_FILE_MAX_SIZE() {
		return LOG_FILE_MAX_SIZE;
	}
	public void setLOG_FILE_MAX_SIZE(int lOG_FILE_MAX_SIZE) {
		LOG_FILE_MAX_SIZE = lOG_FILE_MAX_SIZE;
	}
	public  int getLOG_FILE_LIFE() {
		return LOG_FILE_LIFE;
	}
	public void setLOG_FILE_LIFE(int lOG_FILE_LIFE) {
		LOG_FILE_LIFE = lOG_FILE_LIFE;
	}
	/**
	 * 返回Log文件名的前缀, 格式：_company_app_log_logType_
	 * @return
	 *@date 2013-10-25 下午3:25:59
	 *@author aaa
	 */
	private static String getLogFileNamePre(){
		String namePre="";
		if(PATH_LOGCAT!=null&&!PATH_LOGCAT.equals("")){
			String namePath=PATH_LOGCAT.substring(PATH_LOGCAT.indexOf("statuslog"));
			namePre=namePath.replace("/", "_");
		}
		return namePre;
	}
	/**
	 * 构造函数
	 */
	private StatusLog(){

	}

	/**
	 * @param context
	 * @param logDir   日志目录
	 * @param logSize 日志大小
	 * @param life         日志期限
	 *@date 2013-10-29 下午2:44:37
	 *@author aaa
	 */
	public void init(Context context,String logDir) {
		
		// 判断权限 TODO 就算AndroidManifest.xml加了READ_LOGS也会throw
//		PermissionTool.checkThrow(context, android.Manifest.permission.READ_LOGS);
		
		//获取上下文
		this.context=context;
		//获取日志保存目录
		this.PATH_LOGCAT=logDir;
		File file = new File(PATH_LOGCAT);   
		if(!file.exists()) {   
			file.mkdirs();   
		}   
		//初始化日志记录器
		fileLogger = Logger.getLogger("com.yst.log.statuslog");
		fileLogger.setLevel(Level.INFO);
		Handler[] hs = fileLogger.getHandlers();
		for (Handler h : hs) {
			h.close();
			fileLogger.removeHandler(h);
		}
		try {
			/**
			 * 文件 日志文件名 根据日期组成
			 *  日志最大写入为1M
			 *   日志文件保存期限3天
			 *   如果文件没有达到规定大小则将日志文件添加到已有文件
			 *   文件记录超过大小，自动新建文件记录新内容
			 */
			//日志文件绝对路径
			String logFilePath = PATH_LOGCAT+"/"+getLogFileNamePre();
			StatusLogFileHandler fh = new StatusLogFileHandler(logFilePath, 
					LOG_FILE_MAX_SIZE, LOG_FILE_LIFE, true); 
			fh.setEncoding("UTF-8");
			fh.setFormatter(new StatusLogFormatter());
			fileLogger.setUseParentHandlers(false);  
			fileLogger.addHandler(fh);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	
	}
	/**
	 * 返回一个文件记录实例
	 */
	public static synchronized Logger getFileLogger() {
		return fileLogger;
	}

	/**
	 * 获取操作单例
	 * @param context
	 * @return
	 *@date 2013-10-24 下午4:15:43
	 *@author aaa
	 */
	public static StatusLog getInstance() {   
		if(INSTANCE == null) {   
			INSTANCE = new StatusLog();   
		}   
		return INSTANCE;   
	}   


	//    public static void main(String[] args) {
	//        for (int i = 0; i < 10; i++) {
	//            fileLogger.log(Level.INFO, "我被记录了吗?");
	//        }
	//    }
}