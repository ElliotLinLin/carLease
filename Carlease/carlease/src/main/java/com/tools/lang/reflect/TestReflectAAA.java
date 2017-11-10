package com.tools.lang.reflect;

import com.tools.util.Log;

public class TestReflectAAA {

	private static final String TAG = TestReflectAAA.class.getSimpleName();

	private int super_A = 1;
	protected int super_B = 2;
	public String super_C = "333";

	public static String super_D = "444";

	private void super_private_print() {
		Log.e(TAG, "super_private_print");
	}

	protected void super_protected_print(int a, String text) {
		String string = String.format("super_protected_print:%d,%s", a, text);
		Log.e(TAG, string);
	}

	public String super_public_print() {
		String string = String.format("super_public_print");
		Log.e(TAG, string);
		return null;
	}

	public static String super_public_static_print(int a, String text) {
		String string = String.format("super_public_static_print:%d,%s", a, text);
		Log.e(TAG, string);
		return null;
	}

}
