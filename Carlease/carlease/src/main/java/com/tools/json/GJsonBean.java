package com.tools.json;


import com.tools.util.Log;


/**
 * 
 * @author LMC
 *
 */
public class GJsonBean {

	private static final String TAG = GJsonBean.class.getSimpleName();

	private GJsonData[] GJsonData;
	private GJsonSub GJsonSub;
	private String Msg;
	private int Count;
	private String tStringEmpty;
	private String tStringText;
	private String tStringNull;
	private boolean tBooleanTrue;
	private boolean tBooleanFalse;
	private int tInteger;
	private int t_Integer;
	private double tDouble;
	private double t_Double;

	public GJsonData[] getGJsonData() {
		return GJsonData;
	}

	public void setGJsonData(GJsonData[] gJsonData) {
		GJsonData = gJsonData;
	}

	public GJsonSub getGJsonSub() {
		return GJsonSub;
	}

	public void setGJsonSub(GJsonSub gJsonSub) {
		GJsonSub = gJsonSub;
	}

	public String getMsg() {
		return Msg;
	}

	public void setMsg(String msg) {
		Msg = msg;
	}

	public int getCount() {
		return Count;
	}

	public void setCount(int count) {
		Count = count;
	}

	public String gettStringEmpty() {
		return tStringEmpty;
	}

	public void settStringEmpty(String tStringEmpty) {
		this.tStringEmpty = tStringEmpty;
	}

	public String gettStringText() {
		return tStringText;
	}

	public void settStringText(String tStringText) {
		this.tStringText = tStringText;
	}

	public String gettStringNull() {
		return tStringNull;
	}

	public void settStringNull(String tStringNull) {
		this.tStringNull = tStringNull;
	}

	public boolean istBooleanTrue() {
		return tBooleanTrue;
	}

	public void settBooleanTrue(boolean tBooleanTrue) {
		this.tBooleanTrue = tBooleanTrue;
	}

	public boolean istBooleanFalse() {
		return tBooleanFalse;
	}

	public void settBooleanFalse(boolean tBooleanFalse) {
		this.tBooleanFalse = tBooleanFalse;
	}

	public int gettInteger() {
		return tInteger;
	}

	public void settInteger(int tInteger) {
		this.tInteger = tInteger;
	}

	public int getT_Integer() {
		return t_Integer;
	}

	public void setT_Integer(int t_Integer) {
		this.t_Integer = t_Integer;
	}

	public double gettDouble() {
		return tDouble;
	}

	public void settDouble(double tDouble) {
		this.tDouble = tDouble;
	}

	public double getT_Double() {
		return t_Double;
	}

	public void setT_Double(double t_Double) {
		this.t_Double = t_Double;
	}

	public void print() {
		Log.i(TAG, "--- GJsonBean() start ---");
		Log.i(TAG, "getGJsonData():"+getGJsonData());
		Log.i(TAG, "getGJsonSub():"+getGJsonSub());
		Log.i(TAG, "getMsg():"+getMsg());
		Log.i(TAG, "getCount():"+getCount());
		Log.i(TAG, "gettStringEmpty():"+gettStringEmpty());
		Log.i(TAG, "gettStringText():"+gettStringText());
		Log.i(TAG, "gettStringNull():"+gettStringNull());
		Log.i(TAG, "istBooleanTrue():"+istBooleanTrue());
		Log.i(TAG, "istBooleanFalse():"+istBooleanFalse());
		Log.i(TAG, "gettInteger()():"+gettInteger());
		Log.i(TAG, "getT_Integer()():"+getT_Integer());
		Log.i(TAG, "gettDouble()():"+gettDouble());
		Log.i(TAG, "getT_Double()():"+getT_Double());
		Log.i(TAG, "--- GJsonBean() end ---");
	}

}
