package com.tools.os;

import java.nio.ByteOrder;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.DisplayMetrics;


/**
 * 操作跟系统相关的一些信息
 * 
 * @author lmc
 * 版权声明：版权归作者所有，未经协议授权不得使用、转载、抄袭等，否则依法追究法律责任和经济责任。
 */
public class SystemInfo {

	private static final String TAG = SystemInfo.class.getSimpleName();

	protected Activity ui = null;

	protected Context context = null;

	public SystemInfo(Activity ui) {
		init(ui);
	}

	public SystemInfo(Context context) {
		init(context);
	}

	/**
	 * 初始化
	 * 
	 * @param ui
	 */
	private void init(Activity ui) {
		this.ui = ui;
		init(ui.getApplicationContext());
	}

	/**
	 * 初始化
	 * 
	 * @param context
	 */
	private void init(Context context) {
		this.context = context;
	}

	/**
	 * 得到系统的Level（ok）
	 * 
	 * @return
	 */
	public int getLevel() {
		return Build.VERSION.SDK_INT;
	}

	/**
	 * 得到DisplayMetrics
	 * 
	 * @param ui
	 * @return
	 */
	public DisplayMetrics getMetrics() {
		if (ui == null) {
			return null;
		}
		DisplayMetrics metrics = new DisplayMetrics();
		ui.getWindowManager().getDefaultDisplay().getMetrics(metrics);
		return metrics;
	}

	/**
	 * 得到DPI
	 * 
	 * @param ui
	 * @return
	 */
	public int getDPI() {
		DisplayMetrics metrics = getMetrics();
		if (metrics == null) {
			return -1;
		}
		return metrics.densityDpi;
	}

	/**
	 * 得到高度宽度
	 * 
	 * @param ui
	 * @return
	 */
	public int[] getDisplayHW() {
		DisplayMetrics metrics = getMetrics();
		if (metrics == null) {
			return null;
		}
		int[] array = new int[2];
		array[0] = metrics.widthPixels;
		array[1] = metrics.heightPixels;
		return array;
	}

	/**
	 * 得到系统默认的字符集
	 * 
	 * @return
	 */
	public String defaultCharset() {
		return com.tools.os.Charset.defaultCharset();
	}

	/**
	 * 得到系统网格字节顺序
	 * 
	 * @return
	 */
	public java.nio.ByteOrder getByteOrder() {
		return ByteOrder.nativeOrder();
	}

	public static class OSBuild {

		public OSBuild() {

		}

		public static class VERSION {

			public VERSION() {

			}

			public String toString() {
				StringBuilder buffer = new StringBuilder();
				buffer.append("CODENAME:");
				buffer.append( Build.VERSION.CODENAME );
				buffer.append("\n");
				buffer.append("INCREMENTAL:");
				buffer.append( Build.VERSION.INCREMENTAL );
				buffer.append("\n");
				buffer.append("版本(RELEASE):");
				buffer.append( Build.VERSION.RELEASE );
				buffer.append("\n");
				//				buffer.append("SDK:"); // This field is deprecated.
				//				buffer.append( Build.VERSION.SDK );
				//				buffer.append("\n");
				buffer.append("SDK版本代码(SDK_INT):");
				buffer.append( Build.VERSION.SDK_INT );
				return buffer.toString();
			}
		}

		public String toString() {
			StringBuilder buffer = new StringBuilder();
			buffer.append("BOARD:");
			buffer.append( Build.BOARD );
			buffer.append("\n");
			//			buffer.append("BOOTLOADER:");
			//			buffer.append( Build.BOOTLOADER );
			//			buffer.append("\n");
			buffer.append("BRAND:");
			buffer.append( Build.BRAND );
			buffer.append("\n");
			buffer.append("CPU_ABI:");
			buffer.append( Build.CPU_ABI );
			buffer.append("\n");
			//			buffer.append("CPU_ABI2:");
			//			buffer.append( Build.CPU_ABI2 );
			//			buffer.append("\n");
			buffer.append("DEVICE:");
			buffer.append( Build.DEVICE );
			buffer.append("\n");
			buffer.append("DISPLAY:");
			buffer.append( Build.DISPLAY );
			buffer.append("\n");
			buffer.append("FINGERPRINT:");
			buffer.append( Build.FINGERPRINT );
			buffer.append("\n");
			//			buffer.append("HARDWARE:");
			//			buffer.append( Build.HARDWARE );
			//			buffer.append("\n");
			buffer.append("HOST:");
			buffer.append( Build.HOST );
			buffer.append("\n");
			buffer.append("ID:");
			buffer.append( Build.ID );
			buffer.append("\n");
			buffer.append("MANUFACTURER:");
			buffer.append( Build.MANUFACTURER );
			buffer.append("\n");
			buffer.append("MODEL:");
			buffer.append( Build.MODEL );
			buffer.append("\n");
			buffer.append("PRODUCT:");
			buffer.append( Build.PRODUCT );
			buffer.append("\n");
			//			buffer.append("RADIO:");
			//			buffer.append( Build.RADIO );
			//			buffer.append("\n");
			//			buffer.append("SERIAL:");
			//			buffer.append( Build.SERIAL );
			//			buffer.append("\n");
			buffer.append("TAGS:");
			buffer.append( Build.TAGS );
			buffer.append("\n");
			buffer.append("TIME:");
			buffer.append( Build.TIME );
			buffer.append("\n");
			buffer.append("TYPE:");
			buffer.append( Build.TYPE );
			buffer.append("\n");
			buffer.append("USER:");
			buffer.append( Build.USER );
			return buffer.toString();
		}
	}

	/**
	 *  * 系统信息
	 * 
	 * 	android.os.Build
	android.os.Build.VERSION
	android.os.Build.VERSION_CODES
	 * 
	 *  android.os.Debug
 	android.os.Debug.InstructionCount
 	android.os.Debug.MemoryInfo

 	内存、cpu、sd卡、电量、版本

	 * @return
	 */
	public String toCrashString() {
		StringBuilder buffer = new StringBuilder();
		buffer.append(new OSBuild.VERSION().toString());
		buffer.append("\n");
		buffer.append("\n");
		buffer.append(new OSBuild().toString());

		buffer.append("\n");
		buffer.append("系统默认字符集:"+java.nio.charset.Charset.defaultCharset().name());

		return buffer.toString();
	}

	/**
	 * 打印系统信息
	 * 
	 * @param ui
	 */
	public void print() {
		android.util.Log.i(TAG, "------ 系统信息 start ------");
		android.util.Log.i(TAG, "SDK_INT(系统版本):"+getLevel());
		android.util.Log.i(TAG, "DPI(每英寸像素数):"+getDPI());
		int[] hw = getDisplayHW();
		if(hw != null) {
			android.util.Log.i(TAG, "屏幕大小--->宽度width:"+hw[0]+",高度height:"+hw[1]);
		}
		android.util.Log.i(TAG, "大端字节序BIG_ENDIAN>>高位优先>>正序；小端字节序LITTLE_ENDIAN>>低位优先>>高低位对调；");
		android.util.Log.i(TAG, "BIG_ENDIAN--->0x12345678  0x0000ABCD都不变");
		android.util.Log.i(TAG, "LITTLE_ENDIAN--->0x12345678变成0x78563412  0x0000ABCD变成0xCDAB0000");
		android.util.Log.i(TAG, "当前系统网络字节顺序ByteOrder:"+getByteOrder());
		android.util.Log.i(TAG, "系统默认字符集:"+defaultCharset());
		android.util.Log.i(TAG, "------ 系统信息 end ------");
	}

}
