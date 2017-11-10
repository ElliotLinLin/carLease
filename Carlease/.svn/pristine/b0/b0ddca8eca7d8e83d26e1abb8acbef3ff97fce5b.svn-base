package com.tools.os.storage;


import java.io.File;
import java.math.BigDecimal;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;

import com.tools.lang.reflect.ReflectTool;
import com.tools.util.Log;


/**
 * 存储卡工具类
 *
 * 在线源码
 * http://grepcode.com/file/repository.grepcode.com/java/ext/com.google.android/android/2.3.4_r1/android/os/Environment.java
 *
 * @author LMC
 *
 */
public class StorageTool {

	private static final String TAG = StorageTool.class.getSimpleName();

	protected AbsStorageTool storageTool = null;

	private static final long errorSize = -1L;

	public StorageTool(Context context) {
		init(context);
	}

	private void init(Context context) {
		if (com.tools.os.Build.VERSION.SDK_INT < com.tools.os.Build.VERSION_CODES.Level14) {
			// 4.0以下
			storageTool = new StorageTool234(context);
		} else {
			// 4.0及以上
			storageTool = new StorageTool4(context);
		}
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

	public String[] getVolumePaths() {
		return storageTool.getVolumePaths();
	}

	/**
	 * Volume数量
	 * 
	 * @return
	 */
	public int getVolumeCount() {
		int count = 0;
		String[] paths = getVolumePaths();
		if (paths != null) {
			count = paths.length;
		}
		return count;
	}

	public String getVolumeState(String path) {
		return storageTool.getVolumeState(path);
	}

	public boolean isPrimary(String path) {
		return storageTool.isPrimary(path);
	}

	public String getPrimaryPath() {
		return storageTool.getPrimaryPath();
	}

	public boolean isMounted(String path) {
		return storageTool.isMounted(path);
	}

	/**
	 * 得到一个已成功挂载Mounted的存储卡路径(外置优先)
	 *
	 * @return
	 */
	public String getMountedPath() {

		String path = null;

		// 外置优先
		path = getExternalStoragePath();
		if (isMounted(path)) {
			return path;
		}

		// 如果外置没mounted，则判断内置
		path = getInternalStoragePath();
		if (isMounted(path)) {
			return path;
		}

		return null;
	}

	public boolean isInternalStorage(String path) {
		return storageTool.isInternalStorage(path);
	}

	public boolean isExternalStorage(String path) {
		return storageTool.isExternalStorage(path);
	}

	public String getInternalStoragePath() {
		return storageTool.getInternalStoragePath();
	}

	public String getExternalStoragePath() {
		return storageTool.getExternalStoragePath();
	}

	/**
	 * 得到/data系统内部存储路径
	 *
	 * ROM路径为/data
	 *
	 * @return
	 */
	public String getSystemInternalStoragePath() {
		File file = Environment.getDataDirectory();
		if (file == null) {
			return null;
		}
		return file.getAbsolutePath();
	}

	/**
	 * 得到/system路径
	 *
	 * @return
	 */
	public String getSystemPath() {
		File file = Environment.getRootDirectory();
		if (file == null) {
			return null;
		}
		return file.getAbsolutePath();
	}

	/**
	 * 通过路径得到StatFs
	 *
	 * @param path
	 * @return
	 */
	protected StatFs getStatFs(String path) {
		try {
			return new StatFs(path);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获得总空间大小（检测通过）
	 *
	 * @param path
	 * @return
	 */
	public long getTotalSize(String path) {

		if (isEmptyString(path)) {
			return errorSize;
		}

		StatFs statFs = getStatFs(path);
		if (statFs == null) {
			return errorSize;
		}

		long blockCount = statFs.getBlockCount();
		long blocksSize = statFs.getBlockSize();

		return blockCount * blocksSize;
	}

	/**
	 * 获得可供程序使用的空闲存储空间大小（检测通过）
	 *
	 * 获得可使用的空间，就用此方法
	 *
	 * @return
	 */
	public long getAvailableSize(String path) {

		if (isEmptyString(path)) {
			return errorSize;
		}

		StatFs statFs = getStatFs(path);
		if (statFs == null) {
			return errorSize;
		}

		long availableBlocks = statFs.getAvailableBlocks();
		long blocksSize = statFs.getBlockSize();

		return availableBlocks * blocksSize;
	}

	/**
	 * 获得剩下所有数量(包括预留的一般程序无法使用的块)的空闲存储空间大小（检测通过）
	 *
	 * @return
	 */
	public long getFreeSize(String path) {

		if (isEmptyString(path)) {
			return errorSize;
		}

		StatFs statFs = getStatFs(path);
		if (statFs == null) {
			return errorSize;
		}

		long freeBlocks = statFs.getFreeBlocks();
		long blocksSize = statFs.getBlockSize();

		return freeBlocks * blocksSize;
	}

	/**
	 * 获得已使用存储空间大小（总空间-可使用的空闲存储空间）（检测通过）
	 *
	 * @return
	 */
	public long getUsedSize(String path) {

		long totalSize = getTotalSize(path);
		if (totalSize <= errorSize) {
			return errorSize;
		}

		long availableSize = getAvailableSize(path);
		if (availableSize <= errorSize) {
			return errorSize;
		}

		if (totalSize < availableSize) {
			return errorSize;
		}

		return totalSize - availableSize;
	}

	/**
	 * 转为KB单位
	 *
	 * @param size
	 * @return
	 */
	public static double toKB(long size) {
		return size / 1024.0D;
	}

	/**
	 * 转为KB单位 四舍五入
	 * @param size 大小
	 * @param digits	保留小数位数
	 * @return KB
	 */
	public static double toKB(long size, int digits) {
		BigDecimal bd = new BigDecimal(toKB(size));
		return bd.setScale(digits, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	/**
	 * 转为M单位
	 *
	 * @param size
	 * @return
	 */
	public static double toM(long size) {
		return toKB(size) / 1024.0D;
	}

	/**
	 * 转为MB单位 四舍五入
	 * @param size 大小
	 * @param digits	保留小数位数
	 * @return MB
	 */
	public static double toM(long size, int digits) {
		BigDecimal bd = new BigDecimal(toM(size));
		return bd.setScale(digits, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	/**
	 * 转为G单位
	 *
	 * @param size
	 * @return
	 */
	public static double toG(long size) {
		return toM(size) / 1024.0D;
	}

	/**
	 * 转为GB单位 四舍五入
	 * @param size 大小
	 * @param digits	保留小数位数
	 * @return GB
	 */
	public static double toG(long size, int digits) {
		BigDecimal bd = new BigDecimal(toG(size));
		return bd.setScale(digits, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	/**
	 * 打印大小
	 *
	 * @param size
	 */
	public void printSize(long size) {
		String formatSize = String.format("size:%dB,%fKB,%fM,%fG", size, toKB(size), toM(size),toG(size));
		Log.e(TAG, formatSize);
	}

	/**
	 * 打印大小
	 *
	 * @param path
	 */
	public void printSize(String path) {
		Log.e(TAG, "------------------ start printSize ------------------");
		Log.e(TAG, "路径:"+path);
		Log.e(TAG, "getTotalSize总存储空间大小:");
		printSize(getTotalSize(path));
		Log.e(TAG, "getAvailableSize有效可用存储空间大小:");
		printSize(getAvailableSize(path));
		Log.e(TAG, "getFreeSize有效可用存储空间大小(包括预留无法使用的块):");
		printSize(getFreeSize(path));
		Log.e(TAG, "getUsedSize已使用存储空间大小:");
		printSize(getUsedSize(path));
		Log.e(TAG, "------------------ end printSize ------------------");
	}

	/**
	 * 打印是否存在
	 */
	public void printExists() {
		Log.e(TAG, "------------------ start StorageTool exists ------------------");

		String classEnvironment = "android.os.Environment";

		Log.e(TAG, "isMethodExists Environment.isExternalStorageRemovable:"+ReflectTool.isMethodExists(classEnvironment, "isExternalStorageRemovable"));

		/*
		 * 在线源码
		 * http://grepcode.com/file/repository.grepcode.com/java/ext/com.google.android/android/2.3.4_r1/android/os/Environment.java
		 *
		 * 2.3.7源码:
		 *  public static boolean  [More ...] isExternalStorageRemovable() {
        		return Resources.getSystem().getBoolean(
                com.android.internal.R.bool.config_externalStorageRemovable);
    		}
		 * */

		String classStorageManager = "android.os.storage.StorageManager";
		// 2.2 == true 2.3.3 == true 3.0 == true 4.0 == true
		Log.e(TAG, "isClassExists StorageManager:"+ReflectTool.isClassExists(classStorageManager));
		// 2.2 == false 2.3.3 == false 3.0 == false 4.0 == true
		Log.e(TAG, "isMethodExists getVolumePaths:"+ReflectTool.isMethodExists(classStorageManager, "getVolumePaths"));
		// 2.2 == false 2.3.3 == false 3.0 == false 4.0 == true
		Log.e(TAG, "isMethodExists getVolumeList:"+ReflectTool.isMethodExists(classStorageManager, "getVolumeList"));
		// 4.0 == true
		Log.e(TAG, "isMethodExists getVolumeState:"+ReflectTool.isMethodExists(classStorageManager, "getVolumeState"));

		String classStorageVolume = "android.os.storage.StorageVolume";
		// 2.2 == false 2.3.3 == false 3.0 == false 4.0 == true
		Log.e(TAG, "isClassExists StorageVolume:"+ReflectTool.isClassExists(classStorageVolume));

		String classIMountService = "android.os.storage.IMountService";
		// 2.3.3 == true 4.0 == true
		Log.e(TAG, "isClassExists IMountService:"+ReflectTool.isClassExists(classIMountService));

		Log.e(TAG, "------------------ end StorageTool exists ------------------");
	}

	/**
	 * 打印方法
	 */
	public void print() {
		Log.e(TAG, "------------------ start StorageTool ------------------");

		// 打印大小
		printSize( getSystemInternalStoragePath() );

		// 打印大小
		printSize( getSystemPath() );

		String[] list = getVolumePaths();
		if (list != null && list.length > 0) {

			int len = list.length;
			Log.e(TAG, "len:"+len);

			for (String path : list) {
				Log.e(TAG, "  ------------------ start VolumeState ------------------");

				Log.e(TAG, "路径:"+path);

				// 打印大小
				printSize(path);

				Log.e(TAG, "    ------------------ start fileList ------------------");
				File file = new File(path);
				String[] fileList = file.list();
				if (fileList != null) {
					int count = 0;
					for (String fileText : fileList) {
						if (count >= 10) {
							break;
						}
						Log.e(TAG, "fileList:"+fileText);
						count++;
					}
				}
				Log.e(TAG, "    ------------------ end fileList ------------------");

				Log.e(TAG, "getVolumeState:"+getVolumeState(path));
				Log.e(TAG, "isPrimary:"+isPrimary(path));
				Log.e(TAG, "isMounted:"+isMounted(path));
				//				Log.e(TAG, "isEmulated:"+isEmulated(text));
				Log.e(TAG, "isInternalStorage:"+isInternalStorage(path));
				Log.e(TAG, "isExternalStorage:"+isExternalStorage(path));

				Log.e(TAG, "  ------------------ end VolumeState ------------------");
			}

			Log.e(TAG, "getPrimaryPath:"+getPrimaryPath());
			Log.e(TAG, "getInternalStoragePath:"+getInternalStoragePath());
			Log.e(TAG, "getExternalStoragePath:"+getExternalStoragePath());
			Log.e(TAG, "getSystemInternalStoragePath:"+getSystemInternalStoragePath());
			Log.e(TAG, "getSystemPath:"+getSystemPath());
		}

		Log.e(TAG, "------------------ end StorageTool ------------------");
	}

}
