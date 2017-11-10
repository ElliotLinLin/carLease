package com.tools.json;


import com.tools.util.Log;


/**
 * 
 * @author LMC
 *
 */
public class GJsonSub {

	private static final String TAG = GJsonSub.class.getSimpleName();

	protected String a;
	protected int b;
	protected boolean c;

	public String getA() {
		return a;
	}

	public void setA(String a) {
		this.a = a;
	}

	public int getB() {
		return b;
	}

	public void setB(int b) {
		this.b = b;
	}

	public boolean isC() {
		return c;
	}

	public void setC(boolean c) {
		this.c = c;
	}

	public void print() {
		Log.i(TAG, "--- GJsonSub() start ---");
		Log.i(TAG, "a:"+a);
		Log.i(TAG, "b:"+b);
		Log.i(TAG, "c:"+c);
		Log.i(TAG, "--- GJsonSub() end ---");
	}

}
