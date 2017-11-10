package com.tools.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import android.content.Context;
import android.net.Uri;

import com.tools.content.pm.PermissionTool;
import com.tools.util.Log;

/**
 * 文件操作类
 *
 * @author mosq
 */
public class FileTool {

	public static final String TAG = FileTool.class.getSimpleName();

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
	 * 判断目录或者文件是否存在（已验证通过）
	 *
	 * @param file
	 * @return
	 */
	public static boolean isExists(File file) {
		if (file == null) {
			return false;
		}
		return file.exists();
	}

	/**
	 * 创建目录及子目录（参数只能传入文件夹，不能将文件路径传入）
	 *
	 * 要加权限才可用
	 *
	 * 例子：
	 * FileTool.makeDirs(context, new File(SD.getPath(true)+"/testMakeDir/aaa111/dddd"));
	 *
	 * @param file
	 */
	public static void makeDirs(Context context, File file) {
		if (context != null && file != null) {
			// 检查权限
			PermissionTool.checkThrow(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
			// 判断文件是否存在
			//		Log.e(TAG, "file.exists--->"+file.exists());
			if (!file.exists()) {
				boolean mk = file.mkdirs();
				//			Log.e(TAG, "file.mkdirs--->"+mk);
			}
		}
	}

	/**
	 * 创建文件的父路径
	 * 
	 * @param context
	 * @param file 这是文件路径
	 */
	public static void makeFileDirs(Context context, File file) {
		if (context != null && file != null) {
			// 检查权限
			PermissionTool.checkThrow(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
			// 创建目录及子目录
			makeDirs(context, file.getParentFile());
		}
	}

	//	/**
	//	 * 删除当前文件夹下的所有文件和子文件夹，包括当前文件夹
	//	 *
	//	 * @param dirs
	//	 *            void 2013-12-19
	//	 */
	//	public static boolean deleteDirs(String dirs) {
	//		// 检查权限
	//		PermissionTool.checkThrow(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
	//
	//		if (dirs == null) {
	//			Log.i(TAG,"---dirs为空---");
	//			return false;
	//		}
	//
	//		File file = new File(dirs);
	//
	//		// 当且仅当此抽象路径名表示的文件存在且 是一个目录时，返回 true
	//		// if (!file.isDirectory()) {
	//		// file.delete();
	//		// } else
	//
	//		if (file != null && file.exists() && file.isDirectory()) {
	//			String[] filelist = file.list();
	//			if (filelist != null) {
	//				for (int i = 0; i < filelist.length; i++) {
	//					File delfile = new File(dirs + "/" + filelist[i]);
	//					if (!delfile.isDirectory()) {
	//						delfile.delete();
	//						Log.i(TAG,delfile.getAbsolutePath()+"---删除文件成功---");
	//					} else if (delfile.isDirectory()) {
	//						deleteDirs(dirs + "/" + filelist[i]);
	//					}
	//				}
	//			}
	//			file.delete();
	//			Log.i(TAG,file.getAbsolutePath()+"---删除成功---");
	//			return true;
	//		}
	//		return false;
	//
	//	}

	/**
	 * 删除指定目录，包括子目录及子文件 （验证通过）
	 *
	 * @param file
	 * @return
	 */
	public static boolean deleteDirs(Context context,File file) {

		// 检查权限
		PermissionTool.checkThrow(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);

		Log.i(TAG,"---删除文件---");
		boolean success = true;
		if(file.exists()) {
			Log.i(TAG,"---文件存在---");
			File[] list = file.listFiles() ;
			if(list != null) {
				Log.i(TAG,"---文件存在---list != null");
				int len = list.length;
				for(int i = 0 ; i < len ; ++i) {
					if(list[i].isDirectory()) {
						// 遍历
						deleteDirs(context,list[i]);
					} else {
						boolean ret = list[i].delete();
						Log.i(TAG,list[i].getAbsolutePath()+"---删除文件---"+ret);
						if(!ret) {
							success = false;
						}
					}
				}
			}
		} else {
			success = false;
		}
		if(success) {
			boolean fileDele = file.delete();
			Log.i(TAG,file.getAbsolutePath()+"---删除文件---"+fileDele);
		}
		return success;
	}

	/**
	 * 删除当前文件夹下的所有文件和子文件夹，包括当前文件夹
	 *
	 * @param uri
	 *            void 2013-12-19
	 */
	public static boolean deleteDirs(Context context,Uri uri) {

		// 检查权限
		PermissionTool.checkThrow(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);

		if (uri == null) {
			Log.i(TAG,"---uri为空---");
			return false;
		}

		String path = uri.getEncodedSchemeSpecificPart();
		if (path == null) {
			return false;
		}
		File file = new File(path);

		// 当且仅当此抽象路径名表示的文件存在且 是一个目录时，返回 true
		// if (!file.isDirectory()) {
		// file.delete();
		// } else

		if (file != null && file.exists() && file.isDirectory()) {
			File[] filelist = file.listFiles();

			if (filelist != null && filelist.length != 0) {
				for (int i = 0; i < filelist.length; i++) {

					//					File delfile = new File(path + "/" + filelist[i]);

					if (filelist[i] != null && !filelist[i].isDirectory()) {
						boolean isDele = filelist[i].delete();
						Log.i(TAG,filelist[i].getAbsolutePath()+"---删除文件---"+isDele);
					} else if (filelist[i].isDirectory()) {
						deleteDirs(context,filelist[i]);

						//						deleteDirs(path + "/" + filelist[i]);

					}
				}
			}
			boolean fileDele = file.delete();
			Log.i(TAG,file.getAbsolutePath()+"---删除---"+fileDele);
			return true;
		}
		return false;

	}

	/**
	 * 拷贝文件
	 *
	 * @param fromFile
	 * @param toFile
	 * @return
	 */
	public static boolean copyFile(Context context, File fromFile, File toFile) {

		// 检查权限
		PermissionTool.checkThrow(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);

		if (fromFile == null || toFile == null) {
			Log.e(TAG, "fromFile == null || toFile == null");
			return false;
		}

		if (!fromFile.exists()) {
			Log.e(TAG, "fromFile.exists() == false");
			return false;
		}

		if (!fromFile.isFile()) {
			Log.e(TAG, "fromFile.isFile() == false");
			return false;
		}

		if (!fromFile.canRead()) {
			Log.e(TAG, "fromFile.canRead() == false");
			return false;
		}

		// 判断文件不存在时，上级目录可能也不存在，上级目录不存在时，要创建
		// 创建子目录
		makeDirs(context, toFile.getParentFile());

		if (toFile.exists()) {
			toFile.delete(); // 删除目标文件
		}

		boolean result = false;

		try {
			java.io.FileInputStream input = new java.io.FileInputStream(fromFile);
			java.io.FileOutputStream output = new java.io.FileOutputStream(toFile);
			byte bt[] = new byte[1024];

			int c = 0;

			while ((c = input.read(bt)) > 0) {
				output.write(bt, 0, c);
				output.flush();
			}

			input.close();
			output.close();

			result = true;

		} catch (FileNotFoundException e) {
			Log.e(TAG, "FileNotFoundException():可能没有加入权限WRITE_EXTERNAL_STORAGE");
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * 将assets资源里的指定文件拷贝到指定路径
	 *
	 * 例子：
	 * copyAssetsDirs(context, "assets/fonts/a.rar", "/mnt/sdcard/download");
	 *
	 * @param context
	 * @param assetsPath assets资源文件路径
	 * @param destPath 指定输出路径
	 * @return 是否拷贝成功
	 */
	public static boolean copyAssets(Context context, String assetsPath, String destPath) {
		Log.d(TAG, "assetsPath:"+assetsPath+", destPath:"+destPath);
		// 检查权限
		PermissionTool.checkThrow(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);

		try {
			File destFile = new File(destPath);
			if (destFile.exists()) {
				Log.d(TAG, "destPath is already exists.");
			} else {
				if (destFile.mkdirs()) {
					Log.d(TAG, "create directory   " + destFile.getAbsolutePath());
				} else {
					Log.e(TAG, "cannot create directory.");
					return false;
				}
			}
			if (assetsPath.contains(".") && !destPath.contains(".")) {
				String fileName = assetsPath.substring(assetsPath.lastIndexOf("/") + 1);
				destPath = destPath + fileName;
				Log.d(TAG, "destPath:" + destPath);
			}
			InputStream is = context.getResources().getAssets().open(assetsPath);
			FileOutputStream fos = new FileOutputStream(destPath);
			byte[] buffer = new byte[1024];
			int count = 0;
			while ((count = is.read(buffer)) > 0) {
				fos.write(buffer, 0, count);
			}
			fos.flush();
			fos.close();
			is.close();
			Log.d(TAG,	"Copy  " + assetsPath + "  To  " + destPath);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * 将资源assets里的某个目录拷贝到指定目录下
	 *
	 * 如：
	 * 将工程项目/assets/fonts目录拷贝到/mnt/sdcard/download目录下
	 *
	 * 拷贝成功后fonts的路径就是/mnt/sdcard/download/fonts
	 *
	 * copyAssetsDirs(context, "assets/fonts", "/mnt/sdcard/download");
	 *
	 * @param context
	 * @param assetsDir assets资源文件路径    例如目录"a/a" (为空字符串时默认目录assets)
	 * @param destDir 指定输出路径
	 * @return 是否拷贝成功
	 */
	public static boolean copyAssetsDirs(Context context, String assetsDir, String destDir) {
		Log.d(TAG, "assetsDir:"+assetsDir+", destDir:"+destDir);
		// 检查权限
		PermissionTool.checkThrow(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
		String[] files;
		try {
			files = context.getResources().getAssets().list(assetsDir);
		} catch (IOException e) {
			return false;
		}
		File mWorkingDir = new File(destDir);
		// if this directory does not exists, make one.
		if (!mWorkingDir.exists()) {
			if (mWorkingDir.mkdirs()) {
				//Log.d(TAG, "create directory   "+mWorkingDir.getAbsolutePath());
			} else {
				Log.e(TAG, "cannot create directory.");
			}
		}
		for (int i = 0; i < files.length; i++) {
			try {
				String fileName = files[i];
				// we make sure file name not contains '.' to be a folder.
				if (!fileName.contains(".")) {
					if (0 == assetsDir.length()) {
						copyAssetsDirs(context, fileName, destDir + fileName + "/");
						Log.d(TAG, "CopyAssets递归 copyAssetsDirs:"+fileName+"  dir:"+destDir + fileName + "/");
					} else {
						copyAssetsDirs(context, assetsDir + "/" + fileName, destDir + fileName + "/");
						Log.d(TAG, "CopyAssets递归 copyAssetsDirs:"+assetsDir + "/" + fileName+"  dir:"+destDir + fileName + "/");
					}
					continue;
				}
				File outFile = new File(mWorkingDir, fileName);
				if (outFile.exists()) {
					outFile.delete();
				}
				InputStream in = null;
				if (0 != assetsDir.length()) {
					in = context.getAssets().open(assetsDir + "/" + fileName);
				} else {
					in = context.getAssets().open(fileName);
				}
				OutputStream out = new FileOutputStream(outFile);

				// Transfer bytes from in to out
				byte[] buf = new byte[1024];
				int len;
				while ((len = in.read(buf)) > 0) {
					out.write(buf, 0, len);
				}
				Log.d(TAG,	"Copy  " + fileName + "  To  " + outFile.getAbsolutePath());
				in.close();
				out.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				return false;
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}

	/**
	 * 将res/raw资源里的文件拷贝到指定路径
	 *
	 * 例子：
	 * FileTool.copyRawResource(this.getApplicationContext(), R.drawable.camera, new File(path+"/camera.png"));
	 *
	 * FileTool.copyRawResource(context, R.raw.beep, new File("/mnt/sdcard/beep.ogg"));
	 *
	 * 注意：R.drawable.camera与"/camera.png" 资源文件格式与输出文件格式需求一致
	 * @param context
	 * @param rawId 资源文件的id
	 * @param toFile	 拷贝到指定路径
	 * @return 是否拷贝成功
	 */
	public static boolean copyRawResource(Context context, int rawResId, File toFile) {

		// 检查权限
		PermissionTool.checkThrow(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);

		if(toFile == null){
			return false;
		}
		InputStream is = context.getResources().openRawResource(rawResId);	//获取资源文件的输入流
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(toFile);
			byte[] buffer = new byte[1024];
			int count = 0;
			while ((count = is.read(buffer)) > 0) {
				fos.write(buffer, 0, count);
			}
			fos.close();
			is.close();
			Log.d(TAG,	"Copy  rawResId：" + rawResId + "  To  " + toFile.getAbsolutePath());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/**
	 * 得到扩展名（验证通过）
	 *
	 * 如果是9.png则得到9.png
	 *
	 * 返回的是小写
	 *
	 * @param path
	 * @return
	 */
	public static String getExtName(String path) {

		if (isEmptyString(path)) {
			return null;
		}

		Log.e(TAG, "path:"+path);

		String extName = null;
		int dot = path.lastIndexOf('.');
		if ((dot >-1) && (dot < (path.length() - 1))) {
			extName = path.substring(dot + 1).toLowerCase();
			if ("png".equals(extName)) {
				if (path.length() >= 7 && path.charAt(dot -1) == '9' && path.charAt(dot -2) == '.') {
					Log.e(TAG, "find nine patch.");
					return "9.png";
				}
				return extName;
			}else{
				return extName;
			}
		}

		return null;
	}

	/**
	 * 由URL得到文件名（验证通过）
	 * http://www.ss.com/ss/
	 * http://www.ss.com/ss/ss.rar
	 * 上述两者都验证通过
	 * @param url
	 * @return
	 */
	public static String getFileNameFromURL(URL url) {
		if (url == null) {
			return null;
		}
		String path = url.toExternalForm();
		if (isEmptyString(path)) {
			return null;
		}
		int index = path.lastIndexOf('/');
		if (index < 0) {
			return null;
		}
		String fileName = path.substring(index + 1);
		return fileName;
	}

	/**
	 * 由URI得到文件名（验证通过）
	 *
	 * @param uri
	 * @return
	 */
	public static String getFileNameFromURI(URI uri) {
		if (uri == null) {
			return null;
		}
		try {
			return getFileNameFromURL(uri.toURL());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取文件大小
	 * @param f 文件
	 * @return 文件大小 byte
	 * @throws IOException
	 */
	public static long getFileSizes(File f) {
		long size = 0;
		if (f.exists()) {
			FileInputStream fis;
			try {
				fis = new FileInputStream(f);
				size = fis.available();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return size;
	}


}
