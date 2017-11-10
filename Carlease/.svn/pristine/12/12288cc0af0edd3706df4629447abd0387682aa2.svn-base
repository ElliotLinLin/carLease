package com.tools.lang.reflect;

import com.tools.util.Log;

public class TestReflectBBB extends TestReflectAAA {

	private static final String TAG = TestReflectBBB.class.getSimpleName();

	private int private_int_A = 0;
	
	private static int private_static_int_A = 28;
	private static final int private_static_final_int_B = 90;

	protected static final int protected_static_final_int_C = 56;
	protected static int protected_static_int_D = 95;

	public static int public_static_int_E = 47;
	public static final int public_static_final_int_F = 23;
	public static final Integer public_static_final_Integer_G = 52;

	public static final String public_static_final_String = "string....";

	public void set___private_int_A(int a) {
		private_int_A = a;
	}
	
	public TestReflectBBB() {
		Log.e(TAG, "public ReflectBBB(无参数)");
	}

	protected TestReflectBBB(int a) {
		String string = String.format("protected ReflectBBB:%d", a);
		Log.e(TAG, string);
	}

	public TestReflectBBB(int a, String text) {
		String string = String.format("public ReflectBBB:%d,%s", a, text);
		Log.e(TAG, string);
	}
	
	public void public_printA() {
		Log.e(TAG, "public_printA");
	}
	
	private void private_print() {
		Log.e(TAG, "private_print");
	}

	private static void private_static_print() {
		Log.e(TAG, "private_static_print");
	}

	protected void protected_print(int a, String text) {
		Log.e(TAG, "protected_print");
	}

	protected static void protected_static_print(int a, String text) {
		String string = String.format("protected_static_print:%d,%s", a, text);
		Log.e(TAG, string);
	}

	public String public_print(int a, String text) {
		String string = String.format("public_print:%d,%s", a, text);
		Log.e(TAG, string);
		return null;
	}

	public static String public_static_print(int a, String text) {
		String string = String.format("public_static_print:%d,%s", a, text);
		Log.e(TAG, string);
		return null;
	}

	public static int max(int a, int b) {
		return (a>=b)?a:b;
	}

	public String[] long_Parame(int a, long d, String s, Integer[] t, Double c) {
		return null;
	}

}
