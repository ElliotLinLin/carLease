package com.tools.os.storage;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Environment;
import android.os.FileObserver;
import android.os.StatFs;

import com.tools.util.Log;

/**
 * 存储器观察者
 *
 * 存储器的状态改变监听
 * 	需要实现IObserverListener接口
 *  例子：
 * 				StorageObserver so = new StorageObserver(context);
				so.registerSDCardReceiver(new IObserverListener() {
					@Override
					public void onStateChange(String state) {

					}

					@Override
					public void onMemoryLimit() {

					}
				}, path, sdcardDir);
 * @author LMC
 *
 */
public class StorageObserver {
	private static final String TAG = StorageObserver.class.getSimpleName();
//	// 接入SD卡
//	public static final String ACTION_SDCARD_MOUNTED = "SDObserver.action.SDCARD_MOUNTED";
//	// 拔出SD卡
//	public static final String ACTION_SDCARD_UNMOUNTED = "SDObserver.action.SDCARD_UNMOUNTED";
//	// SD卡内存限制
//	public static final String ACTION_SDCARD_MEMORY_LIMIT = "SDObserver.action.SDCARD_MEMORY_LIMIT";
	// 内存限制的大小 KB
	private long memoryLimit = 500*1024;//500*1024
	private Context context = null;
	//SD卡的路径
	private String sdcardDir = "";
	String path1;
	
	/**
	 * 监听接口
	 * @author Liaojp
	 * @date 2014-2-18
	 */
	public interface IObserverListener{
		//存储媒体状态变化
		void onStateChange(String state);

		//内存空间限制
		void onMemoryLimit();
	}

//	public interface IObserverListener{
//		//在没有挂载前存储媒体已经被移除
//		void onBadRemoval();
//		//正在检查存储媒体
//		void onChecking();
//		//存储媒体已经挂载，并且挂载点可读/写
//		void onMounted();
//		//存储媒体已经挂载，挂载点只读
//		void onMountedReadOnly();
//		//存储媒体是空白或是不支持的文件系统
//		void onNOFS();
//		//存储媒体被移除
//		void onRemoved();
//		//存储媒体正在通过USB共享
//		void onShared();
//		//存储媒体无法挂载
//		void onUnMountable();
//		//存储媒体没有挂载
//		void onUnMounted();
//
//		//内存空间限制
//		void onMemoryLimit();
//	}

	public IObserverListener iListener;

	public StorageObserver(Context context){
		this.context = context;
	}


	/**
	 * 注册监听存储媒体状态变化和剩余空间不足
	 * @param iObserverListener 监听接口
	 * @param path 监听的文件路径
	 * @param sdcardDir SD卡路径
	 * @return BroadcastReceiver 	需要在onDestroy()方法中注销unregisterReceiver(mReceiver);
	 */
	public BroadcastReceiver registerSDCardReceiver(
			IObserverListener iObserverListener, String path, String sdcardDir) {
		Log.i(TAG, "registerSDCardReceiver");
		this.iListener = iObserverListener;
		this.path1 = path;
		this.sdcardDir = sdcardDir;
		BroadcastReceiver mReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				Log.i(TAG, "Component: " + intent.getComponent());

				Log.i(TAG, "Aciton: " + intent.getAction());
				Log.i(TAG, "Categories: " + intent.getCategories());

				Log.i(TAG, "Data: " + intent.getData());
				Log.i(TAG, "DataType: " + intent.getType());
				Log.i(TAG, "DataSchema: " + intent.getScheme());

				iListener.onStateChange(intent.getAction());
//				if (intent.getAction().equals(Intent.ACTION_MEDIA_MOUNTED)) {
//					iListener.onMounted();		//SD卡挂载成功
//				} else if (intent.getAction().equals(Intent.ACTION_MEDIA_UNMOUNTED)) {
//					iListener.onUnMounted();
//				} else if (intent.getAction().equals(Intent.ACTION_MEDIA_CHECKING)){
//					iListener.onChecking();
//				} else if (intent.getAction().equals(Intent.ACTION_MEDIA_NOFS)){
//					iListener.onNOFS();
//				} else if (intent.getAction().equals(Intent.ACTION_MEDIA_REMOVED)){
//					iListener.onRemoved();
//				} else if (intent.getAction().equals(Intent.ACTION_MEDIA_SHARED)){
//					iListener.onShared();
//				} else if (intent.getAction().equals(Intent.ACTION_MEDIA_BAD_REMOVAL)){
//					iListener.onBadRemoval();
//				} else if (intent.getAction().equals(Intent.ACTION_MEDIA_UNMOUNTED)){
//					iListener.onUnMounted();
//				} else if (intent.getAction().equals(Intent.ACTION_MEDIA_UNMOUNTABLE)){
//					iListener.onUnMountable();
//				}


			}
		};

		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(Intent.ACTION_MEDIA_MOUNTED);
		intentFilter.addAction(Intent.ACTION_MEDIA_UNMOUNTED);
        intentFilter.addAction(Intent.ACTION_MEDIA_SCANNER_STARTED);
        intentFilter.addAction(Intent.ACTION_MEDIA_SCANNER_FINISHED);
        intentFilter.addAction(Intent.ACTION_MEDIA_EJECT);
        intentFilter.addAction(Intent.ACTION_MEDIA_BAD_REMOVAL);
        intentFilter.addAction(Intent.ACTION_MEDIA_REMOVED);
        intentFilter.addAction(Intent.ACTION_MEDIA_CHECKING);
        intentFilter.addAction(Intent.ACTION_MEDIA_NOFS);
        intentFilter.addDataScheme("file");
		context.registerReceiver(mReceiver, intentFilter);		//注册SD卡状态监听

		startWatching(path, sdcardDir);	//开始监听文件变化

		return mReceiver;
	}

	/**
	 * 开始监听文件变化
	 * 注意只能监听当前目录，不能递归监听
	 * @param path 监听路径 例如：sdcard/file
	 */
	public void startWatching(String path, String sdcardDir){
//		path = "/mnt/sdcard/wisdomgps/carlife/videoRecord/";
		fileObserver = new SDCardFileObserver(path, FileObserver.ALL_EVENTS);
		this.sdcardDir = sdcardDir;
		fileObserver.startWatching();	//启用文件监听器
	}
	SDCardFileObserver fileObserver = null;
	/**
	 * SDCard文件监听器
	 * @author liaojp
	 * @date 2014-2-12
	 */
	class SDCardFileObserver extends FileObserver{

		/**
		 * 指定要监听的事件类型，默认为FileObserver.ALL_EVENTS
		 * @param path
		 * @param mask
		 */
		public SDCardFileObserver(String path, int mask) {
			super(path, mask);
			Log.i(TAG, "SDCardFileObserver-->"+path);
		}

		@Override
		public void onEvent(int event, String path) {
			Log.i(TAG, "onEvent-->"+path);
			Log.i(TAG, "onEvent-->"+event);
			switch (event) {
			case FileObserver.CREATE:
				//文件或目录被创建
				Log.i(TAG, "Action FileObserver.CREATE path:"+path);
				if (isMemoryLimit()) {
					// SD卡内存限制
					// context.sendBroadcast(new
					// Intent(ACTION_SDCARD_MEMORY_LIMIT));
					iListener.onMemoryLimit();
				}
				break;
			case FileObserver.MODIFY://这个case会连续不断的发送，所以，在接收时需要特别注意。比如有时候录制会莫名其妙的中断，很可能是这个问题导致
				//文件或目录被修改
//				Log.i(TAG, "Action FileObserver.MODIFY path:"+path);
				if (isMemoryLimit()) {
					// SD卡内存限制
					// context.sendBroadcast(new
					// Intent(ACTION_SDCARD_MEMORY_LIMIT));
					iListener.onMemoryLimit();
				}

				break;
			case FileObserver.CLOSE_WRITE:
				//文件或目录被闭
				Log.i(TAG, "onEvent--> Action FileObserver.CLOSE_WRITE path:"+path);

				if (isMemoryLimit()) {
					// SD卡内存限制
					// context.sendBroadcast(new
					// Intent(ACTION_SDCARD_MEMORY_LIMIT));
					iListener.onMemoryLimit();
				} else {
					fileObserver = new SDCardFileObserver(path1, FileObserver.ALL_EVENTS);
					fileObserver.startWatching();	//启用文件监听器
				}
				break;
			case 32768:
				Log.i(TAG, "onEvent =="+32768);
				Log.i(TAG, "onEvent getMemoryLimit()=="+getMemoryLimit());
				Log.i(TAG, "onEvent isMemoryLimit()=="+isMemoryLimit());
				if (isMemoryLimit()) {
					// SD卡内存限制
					iListener.onMemoryLimit();
				} else {
					fileObserver = new SDCardFileObserver(path1, FileObserver.ALL_EVENTS);
					fileObserver.startWatching();	//启用文件监听器
					Log.i(TAG, "onEvent fileObserver=="+fileObserver);
				}
			default:
				break;
			}

		}

	}

	/**
	 * 是否内存限制
	 * @return 内存小于500*1024KB返回true
	 */
	public boolean isMemoryLimit() {
		StorageTool st = new StorageTool(context);
		String state = st.getVolumeState(sdcardDir);
//		Log.i(TAG, "sdcardDir-->"+sdcardDir);
//		Log.i(TAG, "Environment.MEDIA_MOUNTED.equals(state)-->"+Environment.MEDIA_MOUNTED.equals(state));
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			//File sdcardDir = Environment.getExternalStorageDirectory();
			StatFs sf = new StatFs(sdcardDir); // 获取文件系统的状态
			long blockSize = sf.getBlockSize();				//内存块大小
			long availCount = sf.getAvailableBlocks();		//可用的内存块数量
			long availMemory = availCount * blockSize / 1024;	//剩余可用的内存大小 单位KB

			Log.i(TAG, "可用的block数目" + availCount + "   剩余空间:" + availCount
					* blockSize / 1024 / 1024 + "MB");
			if (availMemory < getMemoryLimit()) {
				Log.d(TAG, "availMemory:"+availMemory+" <  getMemoryLimit:"+getMemoryLimit());
				return true;	//内存大小受限制
			} else {
				return false;
			}
		}
		return false;
	}


	/**
	 * 获取最少内存限制值
	 * @return 默认500*1024KB
	 */
	public long getMemoryLimit() {
		return memoryLimit;
	}

	/**
	 * 设置最少内存限制值
	 * @param memoryLimit 默认500*1024KB
	 */
	public void setMemoryLimit(long memoryLimit) {
		this.memoryLimit = memoryLimit;
	}

	public String getSdcardDir() {
		return sdcardDir;
	}

	public void setSdcardDir(String sdcardDir) {
		this.sdcardDir = sdcardDir;
	}

}
