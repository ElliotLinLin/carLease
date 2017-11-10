package com.tools.os.storage;

import java.io.File;

import android.content.Context;
import android.os.Environment;

import com.tools.lang.StringUtil;
import com.tools.lang.reflect.ReflectTool;
import com.tools.util.Log;


/**
 * 存储卡工具类，用于4.0及以上
 * 
 * @author LMC
 * 
 * @hide
 */
public class StorageTool4 extends AbsStorageTool {

	private static final String TAG = StorageTool4.class.getSimpleName();

	private static final String STORAGE_SERVICE = "storage";

	private static final String class_StorageManager = "android.os.storage.StorageManager";

	private static final String method_getVolumeList = "getVolumeList";
	private static final String method_getVolumePaths = "getVolumePaths";
	private static final String method_getVolumeState = "getVolumeState";

	private static final String class_StorageVolume = "android.os.storage.StorageVolume";

	private static final String method_getStorageId = "getStorageId";
	private static final String method_getPath = "getPath";
	private static final String method_isEmulated = "isEmulated";
	private static final String method_isRemovable = "isRemovable";

	public StorageTool4(Context context) {
		super(context);
		init(context);
	}

	private void init(Context context) {

	}

	@Override
	public String[] getVolumePaths() {

		//		if (ReflectTool.isClassExists(class_StorageManager) == false) {
		//			return null;
		//		}

		String classPath = class_StorageManager;
		String methodPath = method_getVolumePaths;

		if (ReflectTool.isMethodExists(classPath, methodPath) == false) {
			return null;
		}

		// 得到StorageManager
		// StorageManager storageManager = (StorageManager) getSystemService(Context.STORAGE_SERVICE);
		Object storageManager = context.getSystemService(STORAGE_SERVICE);
		if (storageManager == null) {
			return null;
		}

		// 调用方法
		Object paths = ReflectTool.invokeMethod(storageManager, ReflectTool.getMethod(classPath, methodPath));
		if (paths == null) {
			return null;
		}

		// 强制转成数组
		return (String[])paths;
	}

	public String getVolumeState(String path) {

		if (isEmptyString(path)) {
			return null;
		}

		String classPath = class_StorageManager;
		String methodPath = method_getVolumeState;

		if (ReflectTool.isMethodExists(classPath, methodPath) == false) {
			return null;
		}

		// 得到StorageManager
		// StorageManager storageManager = (StorageManager) getSystemService(Context.STORAGE_SERVICE);
		Object storageManager = context.getSystemService(STORAGE_SERVICE);
		if (storageManager == null) {
			return null;
		}

		// 调用方法
		Object state = ReflectTool.invokeMethod(storageManager, ReflectTool.getMethod(classPath, methodPath, String.class), path);
		if (state == null) {
			return null;
		}

		return (String)state;
	}

	/**
	 * public android.os.storage.StorageVolume[] getVolumeList()
	 * 
	 * @return
	 */
	protected Object[] getVolumeList() {

		//		if (ReflectTool.isClassExists(class_StorageManager) == false) {
		//			return null;
		//		}

		String classPath = class_StorageManager;
		String methodPath = method_getVolumeList;

		if (ReflectTool.isMethodExists(classPath, methodPath) == false) {
			return null;
		}

		// 得到StorageManager
		// StorageManager storageManager = (StorageManager) getSystemService(Context.STORAGE_SERVICE);
		Object storageManager = context.getSystemService(STORAGE_SERVICE);
		if (storageManager == null) {
			return null;
		}

		// 调用方法
		Object list = ReflectTool.invokeMethod(storageManager, ReflectTool.getMethod(classPath, methodPath));
		if (list == null) {
			return null;
		}

		// 强制转成数组
		return (Object[])list;
	}

	/**
	 * 得到StorageVolume实例
	 * 
	 * @param path
	 * @return
	 */
	protected Object getStorageVolume(String path) {

		if (isEmptyString(path)) {
			return null;
		}

		Object[] storageVolumeList = getVolumeList();

		if (storageVolumeList == null || storageVolumeList.length <= 0) {
			return null;
		}

		String classPath = class_StorageVolume;
		String methodPath = method_getPath;

		for (Object storageVolume : storageVolumeList) {
			Object __path = ReflectTool.invokeMethod(storageVolume, ReflectTool.getMethod(classPath, methodPath));
			if (__path != null) {
				// 比较路径是否相等
				if (path.equalsIgnoreCase( StringUtil.Object2String(__path) )) {
					// 找到
					return storageVolume;
				}
			}
		}

		return null;
	}

	/*
	 * android.os.storage.StorageVolume.java
	 * public int getStorageId()
	 * public java.lang.String getPath()
	 * 
	 */
	@Override
	public boolean isPrimary(String path) {

		Object storageVolume = getStorageVolume(path);
		if (storageVolume == null) {
			return false;
		}

		String classPath = class_StorageVolume;
		String methodPath = method_getStorageId;

		Object storageId = ReflectTool.invokeMethod(storageVolume, ReflectTool.getMethod(classPath, methodPath));
		if (storageId != null) {
			int _id = StringUtil.Object2Integer(storageId);
			Log.e(TAG, "storageId:"+_id);
			if (_id == primary_storage_id) {
				return true;
			}
		}

		return false;
	}

	@Override
	public String getPrimaryPath() {
		File file = Environment.getExternalStorageDirectory();
		if (file == null) {
			return null;
		}
		return file.getAbsolutePath();
	}

	@Override
	public boolean isMounted(String path) {

		String state = getVolumeState(path);
		if (isEmptyString(state)) {
			return false;
		}

		if (state.equalsIgnoreCase(Environment.MEDIA_MOUNTED)) {
			return true;
		}

		return false;
	}

	/*
	 * android.os.storage.StorageVolume.java
	 * public boolean isEmulated()
	 */
	//	@Override
	//	public boolean isEmulated(String path) {
	//
	//		Log.e(TAG, "此方法不准确");
	//
	//		Object storageVolume = getStorageVolume(path);
	//		if (storageVolume == null) {
	//			return false;
	//		}
	//
	//		String classPath = class_StorageVolume;
	//		String methodPath = method_isEmulated;
	//
	//		Object isEmulated = ReflectTool.invokeMethod(storageVolume, ReflectTool.getMethod(classPath, methodPath));
	//		if (isEmulated != null) {
	//			return StringUtil.Object2Boolean(isEmulated);
	//		}
	//
	//		return false;
	//	}

	/**
	 * 调用StorageVolume.java里的isRemovable()方法
	 * 
	 * @param path
	 * @return
	 */
	protected boolean isRemovable(String path) {

		Object storageVolume = getStorageVolume(path);
		if (storageVolume == null) {
			return false;
		}

		String classPath = class_StorageVolume;
		String methodPath = method_isRemovable;

		Object isRemovable = ReflectTool.invokeMethod(storageVolume, ReflectTool.getMethod(classPath, methodPath));
		if (isRemovable != null) {
			return StringUtil.Object2Boolean(isRemovable);
		}

		return false;
	}

	public boolean isInternalStorage(String path) {
		return !isExternalStorage(path);
	}

	public boolean isExternalStorage(String path) {

		if (isRemovable(path)) {
			// isRemovable() == true时，表示可移除，说明是外置存储卡
			return true;
		}

		return false;
	}

	@Override
	public String getInternalStoragePath() {

		String[] paths = getVolumePaths();
		if (paths == null) {
			return null;
		}

		for (String path : paths) {
			if (isInternalStorage(path)) {
				return path;
			}
		}

		return null;
	}

	@Override
	public String getExternalStoragePath() {

		String[] paths = getVolumePaths();
		if (paths == null) {
			return null;
		}

		for (String path : paths) {
			if (isExternalStorage(path)) {
				return path;
			}
		}

		return null;
	}

}
