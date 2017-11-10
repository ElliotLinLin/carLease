package com.tools.util;


import android.test.AndroidTestCase;


public class DatatimeTestCase extends AndroidTestCase {

	private static final String TAG = DatatimeTestCase.class.getSimpleName();

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

	public void testRun() {
		//		Log.e(TAG, "sss:"+TimeZone.getDefault().getDSTSavings()); // 中国标准时间
		//		Log.e(TAG, "sss:"+TimeZone.getDefault().getID()); // 中国标准时间
		//		Log.e(TAG, "sss:"+Locale.getDefault().getLanguage()); // zh
		//		Log.e(TAG, "sss:"+TimeZone.getDefault().getRawOffset()); // 中国标准时间.
		//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
		//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//		sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
		//		String dateTime = sdf.format(new Date(System.currentTimeMillis()));
		//		return dateTime;
		//		Log.e(TAG, "dateTime:"+DatetimeUtil.getCurrent("yyyy-MM-dd HH:mm:ss", Locale.getDefault(), TimeZone.getDefault()));
		//		Log.e(TAG, "dateTime:"+DatetimeUtil.getCurrent("yyyy-MM-dd HH:mm:ss", TimeZone.getDefault()));
		//		Log.e(TAG, "dateTime:"+DatetimeUtil.getCurrent("yyyy-MM-dd HH:mm:ss.SSS"));
		//		Log.e(TAG, "dateTime:"+DatetimeUtil.getCurrentDatetime());
		//		Log.e(TAG, "dateTime:"+DatetimeUtil.getCurrentDate());
		//		Log.e(TAG, "dateTime:"+DatetimeUtil.getCurrentTime());
		java.util.Date d = DatetimeUtil.parseDatetime("2102-12-09");
		Log.printDate(TAG, d);
	}

}
