package com.tools.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.ActivityManager;
import android.content.Context;

import com.tools.os.storage.StorageTool;
import com.tools.thread.AbsThread;

/**
 * Logcat信息抓取类 , 内含抓取线程（通过shell命令locat得到log信息)
 * @author aaa
 *完成 2013-10-21
 */
public class LogcatHelper {   
	// 保存本应用所有进程ID
	private List<String> processIDList = new ArrayList<String>();;
	private static final String TAG=LogcatHelper.class.getSimpleName();
	//单例
	private static LogcatHelper INSTANCE = null;   
	//log目录
	private static String PATH_LOGCAT;   
	//当前log文件路径
	private static String CURR_LOG_PATH;
	//抓取线程
	private LogDumper mLogDumper = null;   
	//程序进程名
	private int mPId;   
	//记录开始时间，用于构建 log文件名
	private String logTime;
	//日志文件最大值，默认1M  
	private   int LOG_FILE_MAX_SIZE = 1* 1024 * 1024; 
	// 日志文件大小监控时间间隔，10分钟
	private   int LOG_FILE_MONITOR_INTERVAL = 10 * 60 * 1000;   
	//日志文件保存期限，默认3天
	private  int LOG_FILE_LIFE=3;
	//检测log大小线程
	private Thread checkThread;
	//上下文context
	private Context context;

	public  String getPATH_LOGCAT() {
		return PATH_LOGCAT;
	}

	public void setPATH_LOGCAT(String pATH_LOGCAT) {
		PATH_LOGCAT = pATH_LOGCAT;
	}

	public  int getLOG_FILE_MAX_SIZE() {
		return LOG_FILE_MAX_SIZE;
	}

	public void setLOG_FILE_MAX_SIZE(int lOG_FILE_MAX_SIZE) {
		LOG_FILE_MAX_SIZE = lOG_FILE_MAX_SIZE;
	}

	public  int getLOG_FILE_MONITOR_INTERVAL() {
		return LOG_FILE_MONITOR_INTERVAL;
	}

	public void setLOG_FILE_MONITOR_INTERVAL(int lOG_FILE_MONITOR_INTERVAL) {
		LOG_FILE_MONITOR_INTERVAL = lOG_FILE_MONITOR_INTERVAL;
	}

	public  int getLOG_FILE_LIFE() {
		return LOG_FILE_LIFE;
	}

	public void setLOG_FILE_LIFE(int lOG_FILE_LIFE) {
		LOG_FILE_LIFE = lOG_FILE_LIFE;
	}

	/**  
	 *   
	 * 初始化日志
	 * @param context
	 * @param logDir 日志目录
	 * @param logSize 日志保存大小 （单位 ：字节）
	 * @param life  日志保存期限 （单位 :天)
	 *   @author aaa
	 * */  
	public void logcatHelperInit(Context context,String logDir) {   
		//获取上下文
		this.context=context;
		//记录开始时间,并以0_作前缀(文件名如：0_2013-10-22 10:11:10)
		logTime="0_"+DatetimeUtil.getCurrentDate()+" "+DatetimeUtil.getCurrentTime();
		//替换 : 为 - ,文件名不能存在":"
		logTime=logTime.replaceAll(":", ".");
		//获取日志保存目录
		this.PATH_LOGCAT=logDir;
		File file = new File(this.PATH_LOGCAT);   
		if(!file.exists()) {   
			file.mkdirs();   
		}   
	}   

	/**
	 * 获取操作单例
	 * @param context
	 * @return
	 *@date 2013-10-24 下午4:15:43
	 *@author aaa
	 */
	public static LogcatHelper getInstance() {   
		if(INSTANCE == null) {   
			Log.e(TAG,"INSTANCE is null , new it pid:"+android.os.Process.myPid());
			INSTANCE = new LogcatHelper();   
		}   
		return INSTANCE;   
	}   

	/**
	 * 构造函数 初始化
	 * @param context
	 *@date 2013-10-24 下午4:16:51
	 */
	private LogcatHelper() {   
		mPId = android.os.Process.myPid();   
		Log.e(TAG,TAG+ " LogcatHelper constructor init pid "+mPId);
	}   

	/**添加过滤PID
	 * @param pidList
	 *@date 2013-12-10 下午2:15:21
	 *@author aaa
	 */
	public void setFilter(List<String> pidList){
		if(processIDList==null||pidList==null){
			return;
		}
		for(String pid :pidList){
			if(!processIDList.contains(pid)){
				processIDList.add(pid);
			}
		}
	}
	/**
	 * 线程启动
	 *@date 2013-10-24 下午4:13:15
	 *@author aaa
	 */
	public void start(Context context) {  
		if(context==null){
			return;
		}
		if(mLogDumper != null) {
			//先停止
			stop();
		}
		mLogDumper = new LogDumper(context,String.valueOf(mPId), PATH_LOGCAT);   
		//抓取线程启动
		Log.e(TAG, "LogcatHelper LogDumper start");
		mLogDumper.start();   
	}   


	/**线程重启动，保留相关配置
	 * @param context
	 *@date 2013-12-13 上午9:25:45
	 *@author aaa
	 */
	public void restart(Context context){
		if(mLogDumper != null) {
			//先停止
			stop();
		}
		mLogDumper = new LogDumper(context,null, PATH_LOGCAT);   
		//抓取线程启动
		Log.e(TAG, "LogcatHelper LogDumper start");
		mLogDumper.start();   
	}
	/**
	 * 线程关闭
	 *@date 2013-10-24 下午4:13:36
	 *@author aaa
	 */
	public void stop() {   
		//抓取线程停止
		if(mLogDumper != null) {   
			Log.e(TAG, "LogcatHelper LogDumper stop");
			mLogDumper.stopLogs();   
			mLogDumper = null;   
		}   
		//停止检测线程
		if(checkThread!=null){
			Log.e(TAG, "LogcatHelper checkThread stop");
			//中断线程,不保证马上能中断,配合状态标识完成中断线程功能
			checkThread.interrupt();
			//			checkThread.stop();    //方法已弃用
			checkThread=null;
		}

	}   

	/** 
	 * 检查日志文件大小是否超过了规定大小 如果超过了重新开启一个日志收集进程 
	 */  
	private void checkLogSize()  
	{  
		File file = new File(CURR_LOG_PATH);  
		if (!file.exists())  
		{  
			return;  
		}  
		Log.d(TAG, "LogcatHelper checkLog() ==> The size of the log is too big? "+file.length());  
		if (file.length() >= this.LOG_FILE_MAX_SIZE)  
		{  
			Log.d(TAG, "LogcatHelper The log's size is too big! " + this.LOG_FILE_MAX_SIZE+" is limit");  
			//关闭当前抓取线程
			stop();
			//重新开启另一个抓取线程
			restart(this.context);
		}  

	}  
	/**
	 * LogCat信息抓取线程
	 * @author aaa
	 *
	 */
	private class LogDumper extends AbsThread {   

		//上下文Context
		private Context context;
		//shell进程
		private Process logcatProc;   
		//缓冲输入流
		private BufferedReader mReader = null;   
		//线程运行状态标识
		private boolean mRunning = true;   
		//logcat shell命令
		String cmds = null;   
		//进程名称
		private String mPID;   
		//写文件输出流
		private FileOutputStream out = null;   

		public LogDumper(Context context,String pid, String dir) {
			super();
			this.context=context;
			init(pid, dir);
		}

		/**
		 * 初始化
		 * @param pid  进程名
		 * @param dir   log目录
		 */
		public void init(String pid, String dir) {   
			//获取进程名
			if(pid!=null&&dir!=null){
				mPID = pid;   
				if(processIDList==null){
					processIDList=new ArrayList<String>();
				}
				processIDList.clear();
				processIDList.add(mPID);
			}
			try{   
				//清理超期的log文件
				clearLog(dir, LOG_FILE_LIFE);
				if(logcatProc!=null){
					logcatProc.destroy();
				}
				//shell执行命令清缓存
				logcatProc = Runtime.getRuntime().exec("logcat -c");   
				//log文件路径
				CURR_LOG_PATH=dir+"/"  
						+getLogFileNamePre()+"_"+getLogFileName()+ ".log";
				Log.e(TAG,"LogcatHelper getLogFilePath "+CURR_LOG_PATH);
				//关闭流
				if(out!=null){
					out.close();
				}
				//以追加的方式写入log
				out = new FileOutputStream(CURR_LOG_PATH,true);   
			} catch(FileNotFoundException e) {   
				// TODO Auto-generated catch block    
				e.printStackTrace();   
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}   

			/**  
			 *   
			 * 日志等级：*:v , *:d , *:w , *:e , *:f , *:s  
			 *   
			 * 显示当前mPID程序的 E和W等级的日志.  
			 *   
			 * */  

			// cmds = "logcat *:e *:w | grep \"(" + mPID + ")\"";    
			// cmds = "logcat  | grep \"(" + mPID + ")\"";//打印所有日志信息    
			// cmds = "logcat -s way";
			//shell命令打印标签过滤信息,只抓取本程序的信息
			//adb logcat -c 清缓存

			//			cmds = "logcat  *:e *:i | grep \"("+ mPID + ")\"";   
			cmds = "logcat  *:e *:i *:w *:d";//打印所有log
		}   

		/**
		 * 抓取线程停止标识
		 */
		public void stopLogs() {   
			mRunning = false;   
			//停止线程
			super.cancel();
		}   

		/**
		 * 获取保存Log文件名，根据logTime命名，以1（2,3....)_作前缀,文件名如：0_2013-10-22 10:11:10
		 * @return
		 */
		private String getLogFileName(){
			int index=Integer.valueOf(logTime.substring(0,logTime.indexOf("_")));
			++index;
			//变换log文件名
			logTime=index+logTime.substring(logTime.indexOf("_"), logTime.length());
			Log.e(TAG,"LogcatHelper  current logFileName  没前后缀  "+logTime);
			return logTime;
		}

		/**
		 * 返回Log文件名的前缀, 格式：_company_app_log_logType_
		 * @return
		 *@date 2013-10-25 下午3:25:59
		 *@author aaa
		 */
		private String getLogFileNamePre(){
			String namePre="";
			if(PATH_LOGCAT!=null&&!PATH_LOGCAT.equals("")){
				String namePath = null;
				if(PATH_LOGCAT.contains("flowlog")){
					namePath=PATH_LOGCAT.substring(PATH_LOGCAT.indexOf("flowlog"));
				}else if(PATH_LOGCAT.contains("pushlog")){
					namePath=PATH_LOGCAT.substring(PATH_LOGCAT.indexOf("pushlog"));
				}

				namePre=namePath.replace("/", "_");
			}
			return namePre;
		}


		/**判断空间是否满了
		 * @param path
		 */
		private boolean isFull(Context context,String path){
			boolean isfull =false;
			if(context==null||path==null){
				return isfull;
			}
			StorageTool tool=new StorageTool(context);
			if(tool.getAvailableSize(path)<10*1024*1024){
				isfull=true;
			}
			return isfull;
		}

		/**
		 * 清理指定目录下的超期log文件
		 * @param dir    目录
		 * @param day  保存期限，超过则清理掉
		 *@date 2013-10-25 下午3:29:34
		 *@author aaa
		 */
		private void clearLog(String dir,int day){
			try {
				File logDir = new File(dir);
				if(!logDir.exists()){
					logDir.mkdirs();
				}
				File[] files=logDir.listFiles();
				if(files.length>0){
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					String trace = sdf.format(new Date().getTime());
					//                LinkedList<File> overdueLog = new LinkedList<File>(); //过期的日志文件列表
					long nowDate = sdf.parse(trace).getTime();//当天时间值

					//判断SD空间
					boolean isfull=isFull(context, dir);
					for (File file : files) {
						//获取日期间隔, TODO 抛出nullpointer警告
						long logTime=(nowDate - sdf.parse(file.toString(), new ParsePosition(file.toString().lastIndexOf('_') + 1)).getTime()) / (86400 * 1000);//一天的时间间隔
						//						Log.e(TAG,"LogcatHelper checkLogFile "+file.getName()+
						//								" have saved "+logTime+" day " +" logFileLife is "+day );
						if(isfull){
							//删除过期日志记录
							file.delete();
						}else{
							//筛选过期的文件(3天过期)
							if (logTime > day) {
								Log.e(TAG,"LogcatHelper  deletefile  "+file.getName());
								//删除过期日志记录
								file.delete();
							}
						}

					}

				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}

		}

		@Override  
		public void run() {   
			try{   
				//与抓取线程同生命周期
				if(mRunning){
					if(checkThread==null){
						//启动检测log文件
						checkThread=new Thread(new Runnable() {
							@Override
							public void run() {
								// TODO Auto-generated method stub
								while(mRunning){
									try {
										//检测log大小
										checkLogSize();
										/**
										 * 延时检测
										 * 在阻塞操作时如Thread.sleep()时被中断会抛出InterruptedException(不会导致程序崩溃)
										 */
										Thread.sleep(LOG_FILE_MONITOR_INTERVAL);
									} catch (InterruptedException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
							}
						});
					}
					checkThread.start();
				}
				//shell旧进程关闭
				if(logcatProc!=null){
					logcatProc.destroy();
				}
				//shell执行命令
				logcatProc = Runtime.getRuntime().exec(cmds);   
				Log.e(TAG,TAG+" logcatProc runTime exec "+cmds);
				//关闭流
				if(mReader!=null){
					mReader.close();
				}
				//获取logcat输入流
				mReader = new BufferedReader(new InputStreamReader(   
						logcatProc.getInputStream()), 1024);   
				String line = null;   
				while(mRunning && (line = mReader.readLine()) != null) {   
					if(!mRunning) {   
						break;   
					}   
					if(line.length() == 0) {   
						continue;   
					}   
					//写入只包含本程序进程名的信息
					if(processIDList!=null&&processIDList.size()>0){
						int listSize = processIDList.size();
						for(int i=0;i<listSize;i++){
							try{
								String str =processIDList.get(i);
								if(str!=null&&!str.equals("")){
									if(out != null&& line.contains(str)) {   
										out.write((DatetimeUtil.getCurrentDatetime() + "  "+ line + "\n")   
												.getBytes());   
									}

								}

							}catch (IndexOutOfBoundsException e){
								Log.e(TAG,TAG+" listSize "+listSize+" current index "+i);
								e.printStackTrace();
							}	
						}

					}   
				}   

			} catch(IOException e) {   
				e.printStackTrace();   
			} finally{   
				if(logcatProc != null) {   
					logcatProc.destroy();   
					logcatProc = null;   
				}   
				if(mReader != null) {   
					try{   
						mReader.close();   
						mReader = null;   
					} catch(IOException e) {   
						e.printStackTrace();   
					}   
				}   
				if(out != null) {   
					try{   
						out.close();   
					} catch(IOException e) {   
						e.printStackTrace();   
					}   
					out = null;   
				}   

			}   

		}   

	}   


	/** 获得本应用所有进程ID
	 * @param packageName
	 * @return
	 *@date 2013-12-10 上午11:52:38
	 *@author aaa
	 */
	public static  List<String> getRunningAppProcessInfo(Context ui) {
		// ProcessInfo Model类   用来保存所有进程信息
		List<String> processIDList = new ArrayList<String>();
		// 通过调用ActivityManager的getRunningAppProcesses()方法获得系统里所有正在运行的进程
		List<ActivityManager.RunningAppProcessInfo> appProcessList =((ActivityManager) ui.getSystemService(Context.ACTIVITY_SERVICE))
				.getRunningAppProcesses();
		for (ActivityManager.RunningAppProcessInfo appProcessInfo : appProcessList) {
			// 进程ID号
			int pid = appProcessInfo.pid;
			// 进程名，默认是包名或者由属性android：process=""指定
			String processName = appProcessInfo.processName;
			if(processName.contains(ui.getPackageName())){
				Log.i(TAG, "processName: " + processName + "  pid: " + pid);
				processIDList.add(""+pid);
			}
		}
		Log.e(TAG,TAG+" getRunningAppProcessInfo "+processIDList.toString());
		return processIDList;
	}
}  