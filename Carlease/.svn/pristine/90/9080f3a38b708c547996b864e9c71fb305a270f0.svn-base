package com.baidu.location;

import java.lang.reflect.Method;

import com.tools.lang.reflect.ReflectTool;
import com.tools.location.ConvertGps;
import com.tools.util.Log;

/**
 * 
 * 方法
 * Java_com_baidu_location_Jni_b
 * 
 * http://developer.baidu.com/map/changeposition.htm
 * 
 * @author LMC
 *
 */
public class BaiduJni {

	private static final String TAG = BaiduJni.class.getSimpleName();
//	private static final boolean D = true;

	public static final String Bd09 = "bd09";
	public static final String Bd09ll = "bd09ll";
	public static final String Gcj02 = "gcj02";
	public static final String Gps2Gcj = "gps2gcj";
	public static final String Bd092Gcj = "bd092gcj";
	public static final String Bd09ll2Gcj = "bd09ll2gcj";

	private static int e6 = 0;
	private static int e4 = 1;
	private static int e5 = 2;
	private static int e1 = 11;
	private static int e2 = 12;
	private static int eZ = 13;
	private static int eY = 14;
	private static int e0 = 1024;
	private static boolean e3 = false; // 用于判断locSDK4b.so是否存在

	//	private static native String a(byte[] paramArrayOfByte, int paramInt);
	//	private static native String b(double paramDouble1, double paramDouble2, int paramInt1, int paramInt2);
	//	private static native String c(byte[] paramArrayOfByte, int paramInt);
	//	private static native String g(byte[] paramArrayOfByte);
	//	private static native void f(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2);

	/**
	 * 初始化  注：static{}比方法快。
	 * 
	 * @param library 库名
	 */
	//	public static void initLibrary(String __libraryName) {
	//		libraryName = __libraryName;
	//	}

	/**
	 * 转换坐标
	 * 
	 * @param paramDouble1
	 * @param paramDouble2
	 * @param paramString
	 * @return
	 */
	public static double[] convert(double paramDouble1, double paramDouble2, String paramString) {

		//		double[] arrayOfDouble = { 0.0D, 0.0D };

		if (e3) {
			String errorText = String.format("no found the lib%s.so file.", ConvertGps.libraryName);
			Log.e(TAG, errorText);
			errorText = String.format("lib%s.so不存在.", ConvertGps.libraryName);
			Log.e(TAG, errorText);

			errorText = String.format("no found the lib%s.so file. 库文件不存在.", ConvertGps.libraryName);
			throw new IllegalStateException(errorText);
			//	Log.dxception(TAG, new IllegalStateException("no found the liblocSDK4b.so file. locSDK4b.so不存在."));
			//	return arrayOfDouble;
		}
		Log.d(TAG, "库文件存在:"+ConvertGps.libraryName);

		Log.d(TAG, "convert():paramDouble1经度:"+paramDouble1);
		Log.d(TAG, "convert():paramDouble2纬度:"+paramDouble2);
		Log.d(TAG, "convert():paramString第三个参数:"+paramString);

		String str = null;
		int i = -1;

		//		private static int e6 = 0; // yes
		//		private static int e4 = 1; // yes
		//		private static int e5 = 2; // yes
		//		private static int e1 = 11; // yes
		//		private static int e2 = 12; // yes
		//		private static int eZ = 13; // yes
		//		private static int eY = 14; // no
		//		private static int e0 = 1024; // no

		if (paramString.equals(Bd09)) // 不行，因Bd02是米制坐标。
			i = e6;
		else if (paramString.equals(Bd09ll)) // 可行，将高德坐标转百度经纬坐标bd09ll。
			i = e4;
		else if (paramString.equals(Gcj02)) // 不行。
			i = e5;
		else if (paramString.equals(Gps2Gcj)) // 可行，将GPS坐标转高德坐标，此方法与高德自带的方法有点儿差别。
			i = e1;
		else if (paramString.equals(Bd092Gcj)) // 不行，因Bd02是米制坐标。
			i = e2;
		else if (paramString.equals(Bd09ll2Gcj)) // 可行，将百度经纬坐标转高德坐标。
			i = eZ;

		//		i = 1024;
		Log.d(TAG, "convert():i:"+i);

		try
		{
			// private static native String b(double paramDouble1, double paramDouble2, int paramInt1, int paramInt2);
			// public static double[] jdMethod_if(double paramDouble1, double paramDouble2, String paramString)

			String classPath = "com.baidu.location.Jni";
			String methodName = "if";
			Log.d(TAG, "convert():isMethodExists:"+ReflectTool.isMethodExists(classPath, methodName)); // true
			Log.d(TAG, "convert():isMethodExists:"+ReflectTool.isMethodExists(classPath, "if")); // true
			Log.d(TAG, "convert():isMethodExists:"+ReflectTool.isMethodExists(classPath, "jdMethod_if")); // false

			// 使用反射调用
			//			Method b = ReflectTool.getMethod(classPath, methodName, double.class, double.class, int.class, int.class);
			//			Object object = ReflectTool.invokeStaticMethod(b, paramDouble1, paramDouble2, i, 132456);
			//			if (object != null) {
			//				str = object.toString();
			//			}

			Method method = ReflectTool.getMethod(classPath, methodName, double.class, double.class, String.class);
			double[] object = (double[])ReflectTool.invokeStaticMethod(method, paramDouble1, paramDouble2, paramString);
			if (object != null && object.length >= 2) {
				double[] arrayOfDouble = { object[0], object[1] };
				Log.d(TAG, "convert():arrayOfDouble[0]转换经度:"+arrayOfDouble[0]);
				Log.d(TAG, "convert():arrayOfDouble[1]转换纬度:"+arrayOfDouble[1]);
				return arrayOfDouble;
			}

		}catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	static
	{
		try
		{
			//			System.loadLibrary("locSDK4b");
			System.loadLibrary(ConvertGps.libraryName);
			//			System.loadLibrary(libraryName);
		}
		catch (UnsatisfiedLinkError localUnsatisfiedLinkError)
		{
			localUnsatisfiedLinkError.printStackTrace();
			e3 = true;
			//			throw new IllegalStateException("no found the liblocSDK4b.so file, please correct settings");
		}
	}

}
