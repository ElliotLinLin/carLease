package com.tools.os.storage;


import java.io.File;

import android.content.Context;
import android.os.Environment;


/**
 * 存储卡工具类，用于4.0以下，不包括 4.0
 * 
 * @author LMC
 * 
 * @hide
 */
public class StorageTool234 extends AbsStorageTool {

	private static final String TAG = StorageTool234.class.getSimpleName();

	public StorageTool234(Context context) {
		super(context);
		init(context);
	}

	private void init(Context context) {

	}

	@Override
	public String[] getVolumePaths() {
		String[] path = new String[1];
		path[0] = getPrimaryPath();
		return path;
	}

	public String getVolumeState(String path) {

		if (isEmptyString(path)) {
			return null;
		}

		if (path.equalsIgnoreCase(getPrimaryPath())) {
			return Environment.getExternalStorageState();
		}

		return null;
	}

	@Override
	public boolean isPrimary(String path) {

		if (isEmptyString(path)) {
			return false;
		}

		if (path.equalsIgnoreCase(getPrimaryPath())) {
			return true;
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

	//	@Override
	//	public boolean isEmulated(String path) {
	//		Log.e(TAG, "此方法不可用");
	//		return false;
	//	}

	public boolean isInternalStorage(String path) {
		//		printMethodDisabled();
		//		throw new java.lang.UnsupportedOperationException( "目前没有方法判断是内置还是外置" );
		//		return false;

		//		if (isEmptyString(path)) {
		//			return false;
		//		}
		//		
		//		return !isExternalStorage(path);
		return false; // 2.3及以下没有内置存储卡
	}

	public boolean isExternalStorage(String path) {
		//		printMethodDisabled();

		//		if (isEmptyString(path)) {
		//			return false;
		//		}
		//
		//		if (path.equalsIgnoreCase(getPrimaryPath())) {
		//			return true;
		//		}
		//
		//		//		throw new java.lang.UnsupportedOperationException( "目前没有方法判断是内置还是外置" );
		//		return false;
		return true; // 2.3及以下没有内置存储卡，全部认为是外置存储卡
	}

	@Override
	public String getInternalStoragePath() {
		//		printMethodDisabled();
		//		throw new java.lang.UnsupportedOperationException( "目前没有方法判断是内置还是外置" );
		return null;
	}

	@Override
	public String getExternalStoragePath() {
		//		throw new java.lang.UnsupportedOperationException( "目前没有方法判断是内置还是外置" );
		return getPrimaryPath();
	}

}
