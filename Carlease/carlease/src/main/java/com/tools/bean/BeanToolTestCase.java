package com.tools.bean;



import android.test.AndroidTestCase;


public class BeanToolTestCase extends AndroidTestCase {

	private static final String TAG = BeanToolTestCase.class.getCanonicalName();

	/*
	 * 初始化资源
	 */
	@Override
	protected void setUp() throws Exception {
		//		Log.e(TAG, "setUp");
		super.setUp();
	}

	@Override
	protected void runTest() throws Throwable {
		//		Log.e(TAG, "runTest");
		super.runTest();
	}

	/*
	 * 垃圾清理与资源回收
	 */
	@Override
	protected void tearDown() throws Exception {
		//		Log.e(TAG, "tearDown");
		super.tearDown();
	}

	public void testRun() throws IllegalArgumentException, IllegalAccessException {

//		VersionBean bean = new VersionBean();
//		bean.setVersionCode(1);
//		bean.setVersionName("你好啊中国1=&1");

//		String a = BeanTool.toURLString(bean);
//		Log.e(TAG, "toURLString:"+a);
//
//		String b = BeanTool.toURLEncoder(bean, "UTF-8");
//		Log.e(TAG, "toURLEncoder:"+b);

		//		Class<?> cls = v.getClass();
		//
		//		Field[] fs = cls.getDeclaredFields();
		//
		//		StringBuffer buffer = new StringBuffer();
		//		
		//		for(int i = 0 ; i < fs.length; i++) {
		//			
		//			Field f = fs[i];
		//			f.setAccessible(true); //设置些属性是可以访问的
		//			
		//			// class java.lang.String
		//			// int
		////			Log.e(TAG, "getGenericType:"+f.getGenericType().toString()); // 得到类型
		//			
		//			// java.lang.String
		//			// int
		//			// java.lang.Long
		//			Log.e(TAG, "getType A:"+f.getType().getCanonicalName()); // 得到类型
		//			Log.e(TAG, "getType D:"+f.getDeclaringClass().getCanonicalName());
		//			
		////			Log.e(TAG, "toGenericString:"+f.toGenericString());
		////			Log.e(TAG, "toString:"+f.toString());
		//			
		////			Log.e(TAG, "getModifiers:"+f.getModifiers());
		//			
		//			Object val = f.get(v); // 得到此属性的值   
		//			Log.e(TAG, "fieldName:"+f.getName()+", fieldValue = "+val);
		//			if (i == 1) {
		////				f.set(v, "b"); //给属性设值
		//			}else if (i == 2) {
		////				f.set(v, 22); //给属性设值
		//			}
		//			
		//			// append string
		//			buffer.append(f.getName()+"=");
		//			buffer.append(val.toString()+"&");
		//
		//		}
		//		
		//		Log.e(TAG, "setVersionCode:"+v.getVersionCode());
		//		Log.e(TAG, "getVersionName:"+v.getVersionName());
		//		
		//		Log.e(TAG, "string:"+buffer.toString());

	}

}
