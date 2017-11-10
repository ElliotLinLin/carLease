package com.tools.os.storage;


import android.content.Context;


/**
 * 存储卡工具抽象类（包括内置和外置）
 * 
 * 
 * 假设：
 * 2.2没有内置存储卡，从2.3源代码分析没有内置存储卡，而4.0及以上有内置存储卡功能
 * 
 * 
 * 通过StorageVolume.java里的getDescription()方法可以判断是内还是外，但不是每个手机都能getDescription()得到字符串
 * 02-11 18:12:47.886: E/LauncherUI(5096): getDescription:Native Memory Card
 * 02-11 18:12:47.903: E/LauncherUI(5096): getDescription:External Memory Card
 *
 *		String classPath = "android.os.storage.StorageManager";
 *
 * 		// 2.2 == true 2.3.3 == true 3.0 == true 4.0 == true
		Log.e(TAG, "isClassExists StorageManager:"+ReflectTool.isClassExists(classPath));
		// 2.2 == false 2.3.3 == false 3.0 == false 4.0 == true
		Log.e(TAG, "isMethodExists getVolumePaths:"+ReflectTool.isMethodExists(classPath, "getVolumePaths"));
		// 2.2 == false 2.3.3 == false 3.0 == false 4.0 == true
		Log.e(TAG, "isMethodExists getVolumeList:"+ReflectTool.isMethodExists(classPath, "getVolumeList"));
		// 2.2 == false 2.3.3 == false 3.0 == false 4.0 == false
		Log.e(TAG, "isMethodExists getPrimaryVolume:"+ReflectTool.isMethodExists(classPath, "getPrimaryVolume"));

		String classPath2 = "android.os.storage.StorageVolume";
		// 2.2 == false 2.3.3 == false 3.0 == false 4.0 == true
		Log.e(TAG, "isClassExists StorageVolume:"+ReflectTool.isClassExists(classPath2));
 * 
 * 
 * @author LMC
 *
 */
public abstract class AbsStorageTool {

	private static final String TAG = AbsStorageTool.class.getSimpleName();

	// 系统内部存储路径
	//	private static final String Default_System_Internal_Storage_Path = "/data";

	protected Context context = null;

	// storage ID is 0x00010001 for primary storage,
	// then 0x00020001, 0x00030001, etc. for secondary storages
	public static final int primary_storage_id = 0x00010001;
	public static final int secondary_storage_id_1 = 0x00020001;
	public static final int secondary_storage_id_2 = 0x00030001;

	public AbsStorageTool(Context context) {
		this.context = context;
	}

	/**
	 * 判断字符串是否为空，等于null或者长度不大于零都视为空字符串
	 * 
	 * @param src
	 * @return
	 */
	protected static boolean isEmptyString(String src) {
		if (src == null) {
			return true;
		}

		if (src.length() <= 0) {
			return true;
		}

		return false;
	}

	/**
	 * 得到全部存储卡的StorageVolume，不管是否有效可用
	 * 
	 * @return
	 */
	//	public abstract StorageVolume[] getVolumeList();

	/**
	 * 得到全部存储卡的路径
	 * 
	 * @return
	 */
	public abstract String[] getVolumePaths();

	/**
	 * 通过存储卡路径得到状态
	 * 
	 * 返回字符串，共计9种
	 * android.os.Environment.MEDIA_BAD_REMOVAL
	 * android.os.Environment.MEDIA_CHECKING
	 * android.os.Environment.MEDIA_MOUNTED
	 * android.os.Environment.MEDIA_MOUNTED_READ_ONLY
	 * android.os.Environment.MEDIA_NOFS
	 * 
	 * android.os.Environment.MEDIA_REMOVED
	 * android.os.Environment.MEDIA_SHARED
	 * android.os.Environment.MEDIA_UNMOUNTABLE
	 * android.os.Environment.MEDIA_UNMOUNTED
	 * 
	 * @return
	 */
	public abstract String getVolumeState(String path);

	/**
	 * 判断是否为"当前使用卡"
	 * 
	 * @param path
	 * @return
	 */
	public abstract boolean isPrimary(String path);

	/**
	 * 得到"当前使用卡"的路径
	 * 
	 * Environment.getExternalStorageDirectory()得到就是"当前使用卡"，跟内置或者外置无关
	 * 
	 * @param path
	 * @return
	 */
	public abstract String getPrimaryPath();

	/**
	 * 通过路径来判断是否挂载
	 * 
	 * @param path
	 * @return
	 */
	public abstract boolean isMounted(String path);

	/**
	 * 是否为模拟器创建的存储卡
	 * 
	 * @return
	 */
	//	public abstract boolean isEmulated(String path);

	/**
	 * 通过路径来判断是否为内置存储卡
	 * 
	 * @return
	 */
	public abstract boolean isInternalStorage(String path);

	/**
	 * 通过路径来判断是否为外置存储卡
	 * 
	 * @param path
	 * @return
	 */
	public abstract boolean isExternalStorage(String path);

	/**
	 * 得到内置存储卡路径
	 * 
	 * @return
	 */
	public abstract String getInternalStoragePath();

	/**
	 * 得到外置存储卡路径
	 * 
	 * @return
	 */
	public abstract String getExternalStoragePath();

	//	public void printMethodDisabled() {
	//		Log.e(TAG, "此方法不可用");
	//	}

}
