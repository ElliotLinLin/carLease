package com.tools.thread;


import com.tools.util.Log;


/**
 * thread扩展
 * 
 * Java中有两类线程：User Thread(用户线程)、Daemon Thread(守护线程) 
 * 
 * 用户线程即运行在前台的线程，而守护线程是运行在后台的线程。
 * 
 * 主线程退出了，守护线程也跟着退出，而用户线程不会退出。
 * 
 * 终止线程最好的方法是使用标志位。
 * 终止线程的替代方法：同样是使用标志位，通过控制标志位来终止线程。
 * 
 * 线程可以阻塞于四种状态：

    1、当线程执行Thread.sleep（）时，它一直阻塞到指定的毫秒时间之后，或者阻塞被另一个线程打断；

    2、当线程碰到一条wait（）语句时，它会一直阻塞到接到通知（notify（））、被中断或经过了指定毫秒时间为止（若制定了超时值的话）

    3、线程阻塞与不同I/O的方式有多种。常见的一种方式是InputStream的read（）方法，该方法一直阻塞到从流中读取一个字节的数据为止，它可以无限阻塞，因此不能指定超时时间；

    4、线程也可以阻塞等待获取某个对象锁的排他性访问权限（即等待获得synchronized语句必须的锁时阻塞）。


    注意，并非所有的阻塞状态都是可中断的，以上阻塞状态的前两种可以被中断，后两种不会对中断做出反应
 * 
 * 
 * 
 * 
 * @author lmc
 * 版权声明：版权归作者所有，未经协议授权不得使用、转载、抄袭等，否则依法追究法律责任和经济责任。
 */
public abstract class AbsThread extends Thread {

	private static final String TAG = AbsThread.class.getSimpleName();

	// 是否发出关闭请求
	protected boolean isCanceled = false;

	public AbsThread() {
		init();
	}

	public AbsThread(Runnable runnable) {
		super(runnable);
		init();
	}

	public AbsThread(Runnable runnable, String threadName) {
		super(runnable, threadName);
		init();
	}

	public AbsThread(String threadName) {
		super(threadName);
		init();
	}

	public AbsThread(ThreadGroup group, Runnable runnable) {
		super(group, runnable);
		init();
	}

	public AbsThread(ThreadGroup group, Runnable runnable, String threadName) {
		super(group, runnable, threadName);
		init();
	}

	public AbsThread(ThreadGroup group, String threadName) {
		super(group, threadName);
		init();
	}

	public AbsThread(ThreadGroup group, Runnable runnable, String threadName, long stackSize) {
		super(group, runnable, threadName, stackSize);
		init();
	}

	/**
	 * 初始化
	 * 
	 * @param context
	 */
	private void init() {
		//		if (context == null) {
		//			Log.exception(TAG, new NullPointerException("context == null"));
		//		}
		this.isCanceled = false;
	}

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
	 * sleep
	 * 
	 * @param millis
	 * @param nanos
	 * @return
	 */
	public static boolean sleepMayInterrupt(long millis, int nanos) {
		try {
			Thread.sleep(millis, nanos);
		} catch (InterruptedException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * sleep
	 * 
	 * @param millis
	 * @return
	 */
	public static boolean sleepMayInterrupt(long millis) {
		try {
			// TODO sleep基于cpu时间的，看上面代码用的是uptimeMillis()开机时启动到现在的时间来计算的，系统挂起sleep也会停止计算。
			Log.d(TAG, "...start...sleep...");
			Thread.sleep(millis);
			Log.d(TAG, "...end...sleep...");
		} catch (InterruptedException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 判断线程是否已经开始运行。
	 * 
	 * @return
	 */
	public boolean isStarted() {
		State state = super.getState();
		if (state == Thread.State.NEW || state == Thread.State.TERMINATED) {
			return false;
		}
		return true;
	}

	/**
	 * 判断线程是否执行取消方法cancel()。
	 */
	public boolean isCanceled() {
		return isCanceled;
	}

	/**
	 * 关闭
	 */
	public void cancel() {
		Log.e(TAG, "cancel()");
		isCanceled = true;
		super.interrupt();
	}

}
