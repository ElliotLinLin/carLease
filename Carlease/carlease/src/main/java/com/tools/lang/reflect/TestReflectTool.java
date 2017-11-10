package com.tools.lang.reflect;


import java.util.List;

import com.tools.util.Log;

public class TestReflectTool {

	private static final String TAG = TestReflectTool.class.getSimpleName();

	public static void isClassExists() {
		ReflectTool.isClassExists("com.tools.lang.reflect.ReflectToolClient"); // == true
		ReflectTool.isClassExists("android.app.DownloadManager"); // == true
		ReflectTool.isClassExists("android.app.DownloadManagerCCC"); // == false
	}

	public static void isMethodExists() {
		ReflectTool.isMethodExists("com.tools.lang.reflect.ReflectBBB", "max");
		ReflectTool.isMethodExists("com.tools.lang.reflect.ReflectBBB", "public_printA");
		ReflectTool.isMethodExists("com.tools.lang.reflect.ReflectBBB", "maxCC");
	}

	public static void isFieldExists() {
		ReflectTool.isFieldExists("com.tools.lang.reflect.ReflectBBB", "private_static_int_A");
		ReflectTool.isFieldExists("com.tools.lang.reflect.ReflectBBB", "public_printA");
		ReflectTool.isFieldExists("com.tools.lang.reflect.ReflectBBB", "public_printAcc");
		ReflectTool.isFieldExists("com.tools.lang.reflect.ReflectBBB", "public_static_final_String");
	}

	public static void getMethod() {

		// +++ 自身类

		// 无参数

		java.lang.reflect.Method m1 = ReflectTool.getMethod("com.tools.lang.reflect.ReflectBBB", "private_static_print");
		ReflectTool.invokeStaticMethod(m1);

		// 有参数
		java.lang.reflect.Method m2 = ReflectTool.getMethod("com.tools.lang.reflect.ReflectBBB", "public_static_print", int.class, String.class);
		ReflectTool.invokeStaticMethod(m2, 3, "aaaa");

		// +++ 父类

		// 无参数
		java.lang.reflect.Method m3 = ReflectTool.getMethod("com.tools.lang.reflect.ReflectBBB", "super_public_print");
		//		ReflectTool.invokeMethod(m3);

		// 有参数
		long a = System.currentTimeMillis();
		java.lang.reflect.Method m4 = ReflectTool.getMethod("com.tools.lang.reflect.ReflectBBB", "super_public_static_print", int.class, String.class);
		ReflectTool.invokeStaticMethod(m4, 46, "bbb");
		long b = System.currentTimeMillis();
		Log.e(TAG, "用时:"+(b-a));

	}

	public static void invokeMethod() {
		getMethod();
	}

	public static void getFieldObject() {
		// 对象
		TestReflectBBB bbb = new TestReflectBBB(23, "");
		bbb.set___private_int_A(2390);
		Object obj1 = ReflectTool.getFieldValue(bbb, "com.tools.lang.reflect.ReflectBBB", "private_int_A");
		Log.e(TAG, "value:"+obj1.toString());

		// 静态
		Object obj2 = ReflectTool.getStaticFieldValue("com.tools.lang.reflect.ReflectBBB", "private_static_int_A");
		Log.e(TAG, "value:"+obj2.toString());
	}

	public static void printClass() {
		ReflectTool.printClass("com.tools.lang.reflect.ReflectBBB");
	}

	public static void createObject() {
		// 得到构造函数
		java.lang.reflect.Constructor<?> c = ReflectTool.getConstructor("com.tools.lang.reflect.ReflectBBB", int.class, String.class);
		// 创建类的对象
		Object objectClass = ReflectTool.createObject(c, 34, "abvnnnn");
		// 得到方法
		java.lang.reflect.Method method = ReflectTool.getMethod("com.tools.lang.reflect.ReflectBBB", "public_print", int.class, String.class);
		// 使用此对象，调用一个方法
		Object object2 = ReflectTool.invokeMethod(objectClass, method, 23, "cccc");

		// +++ 创建无参数的构造函数
		// 得到构造函数
		java.lang.reflect.Constructor<?> cNull = ReflectTool.getConstructor("com.tools.lang.reflect.ReflectBBB");
		// 创建类的对象
		Object objectClass2 = ReflectTool.createObject(cNull);

	}

	public static void test_parseClass() {

		Log.e(TAG, "getName 下述是 ReflectMethod ReflectMode.Public");

		ReflectClass c = ReflectTool.parseClass(TestReflectBeanA.class, ReflectMode.Public);
		List<ReflectMethod> list = c.getMethods();
		for (ReflectMethod rm : list) {
			rm.print();
		}

		Log.e(TAG, "getName 下述是 ReflectField ReflectMode.Public");

		List<ReflectField> listB = c.getFields();
		for (ReflectField rf : listB) {
			rf.print();
		}

		Log.e(TAG, "getName 下述是 ReflectMethod ReflectMode.Self");

		c = ReflectTool.parseClass(TestReflectBeanA.class, ReflectMode.Self);
		list = c.getMethods();
		for (ReflectMethod rm : list) {
			rm.print();
		}

		Log.e(TAG, "getName 下述是 ReflectField ReflectMode.Self");

		listB = c.getFields();
		for (ReflectField rf : listB) {
			rf.print();
		}

		Log.e(TAG, "getName 下述是 ReflectMethod ReflectMode.Public_Exclude_Object");

		c = ReflectTool.parseClass(TestReflectBeanA.class, ReflectMode.Public_Exclude_Object);
		list = c.getMethods();
		for (ReflectMethod rm : list) {
			rm.print();
		}

		Log.e(TAG, "getName 下述是 ReflectField ReflectMode.Public_Exclude_Object");

		listB = c.getFields();
		for (ReflectField rf : listB) {
			rf.print();
		}

	}

	//	public static int getIntA() {
	//		try {
	//			Class<?> c = Class.forName("android.view.View");
	//			java.lang.reflect.Field f = c.getDeclaredField("OVER_SCROLL_ALWAYS");
	//			f.setAccessible(true); // 允许访问
	//			// m.setAccessible(true);
	//
	//			Object object = new Object();
	//			Log.e(TAG, "aa:"+object.toString());
	//			f.getInt(object);
	//			Log.e(TAG, "bb:"+object.toString());
	//
	//			//			java.lang.reflect.Field f = c.getField("OVER_SCROLL_ALWAYS");
	//
	//			//			Method m = c.getMethod("getRuntime", new Class[] {});
	//			//			Object obj = m.invoke(c, new Object[] {});
	//			//			if (obj != null) {
	//			//				// 得到对象了
	//			//				Method m2 = c.getMethod("gcSoftReferences", new Class[] {});
	//			//				//				Method m2 = c.getMethod("getMinimumHeapSize", new Class[] {});
	//			//				Object obj2 = m2.invoke(obj, new Object[] {});
	//			//				if (obj2 != null) {
	//			//					Log.e(TAG, "obj2 != null __ invodefs  get msfe....:object:"+obj2.toString());
	//			//				}
	//			//				Log.e(TAG, "obj2 == null __ invodefs  get msfe....:object");
	//			//			}else {
	//			//				Log.e(TAG, "obj == null");
	//			//			}
	//		} catch (ClassNotFoundException e) {
	//			e.printStackTrace();
	//		} catch (SecurityException e) {
	//			e.printStackTrace();
	//		} catch (IllegalArgumentException e) {
	//			e.printStackTrace();
	//		} catch (NoSuchFieldException e) {
	//			e.printStackTrace();
	//		} catch (IllegalAccessException e) {
	//			e.printStackTrace();
	//		}
	//		return 3;
	//	}

}
