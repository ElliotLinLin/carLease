package com.tools.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.tools.util.Log;

/**
 * 常驻服务
 * 
 * Android 常驻的后台服务的一种实现 
 * http://blog.csdn.net/u010538765/article/details/10859943
 * 
 * 
 * PendingIntent.FLAG_CANCEL_CURRENT（将新的替代旧的）
 * 如果PendingIntent描述已经存在,当前一个是取消前生成一个新的。
 * PendingIntent.FLAG_NO_CREATE
 * 如果PendingIntent描述不存在,那么简单地返回null而不是创建它
 * PendingIntent.FLAG_ONE_SHOT
 * 这PendingIntent只能使用一次
 * PendingIntent.FLAG_UPDATE_CURRENT
 * 如果PendingIntent描述已经存在,然后保持它但它取代其额外的数据在这个新的是什么意图
 * 
 * 
 * 
 * 例子：
 * 	Intent intent = new Intent(getApplicationContext(), PushService.class);
	AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
	PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, Intent.FLAG_ACTIVITY_NEW_TASK); 
    long now = System.currentTimeMillis();
    alarmManager.setInexactRepeating(AlarmManager.RTC, now, 5000, pendingIntent);

 *
 * 例子2：
 * 
 * 		// 启动常驻服务
		DurableService durableService = new DurableService(context);
		// 创建一个
		Intent intent = new Intent(context, PushService.class);
		intent.putExtra(PushService.Key_Command, PushService.Command_Push);
		// 添加
		durableService.add(intent, 1L * 60L * 1000L); // 1分钟
		// 开始
		durableService.start();

		// 停止常驻服务
		DurableService durableService = new DurableService(context);
		// 创建一个
		Intent intent = new Intent(context, PushService.class);
		// 取消
		durableService.cancel(intent);

 * @author LMC
 *
 */
public class DurableService {

	private static final String TAG = DurableService.class.getSimpleName();

	private Context context = null;

	//	private Set< Intent > intentList = new HashSet< Intent >();
	//	private Set< ServiceBean > serviceBeanList = new HashSet< ServiceBean >();
	private List< ServiceBean > serviceBeanList = new ArrayList< ServiceBean >();
	//	private Set< Long > intervalList = new HashSet< Long >();

	private AlarmManager alarmManager = null;

	/**
	 * 内部类
	 * 
	 * @author LMC
	 *
	 */
	private class ServiceBean {
		private Intent intent = null;
		private long interval = 0L;
		public Intent getIntent() {
			return intent;
		}
		public void setIntent(Intent intent) {
			this.intent = intent;
		}
		public long getInterval() {
			return interval;
		}
		public void setInterval(long interval) {
			this.interval = interval;
		}
	}

	public DurableService(Context context) {
		init(context);
	}

	/**
	 * 初始化
	 * 
	 * @param context
	 */
	private void init(Context context) {
		this.context = context;
		alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
	}

	/**
	 * 添加
	 * 
	 * @param intent
	 * @param intervalMillis 毫秒
	 */
	public void add(Intent intent, long intervalMillis) {
		if (intent == null) {
			return;
		}
		Log.d(TAG, "add():getIntentClassName():"+getIntentClassName(intent)+",intervalMillis:"+intervalMillis);
		ServiceBean serviceBean = new ServiceBean();
		serviceBean.setIntent(intent);
		serviceBean.setInterval(intervalMillis);
		serviceBeanList.add( serviceBean );
		Log.d(TAG, "add():size:"+serviceBeanList.size());
	}

	/**
	 * 移除
	 * 
	 * @param intent
	 */
	public void remove(Intent intent) {
		Log.d(TAG, "remove()");
		if (intent == null) {
			return;
		}

		//		Log.d(TAG, "remove():rrr:"+intent.getAction());
		//		Log.d(TAG, "remove():rrr:"+intent.getDataString());
		//		Log.d(TAG, "remove():rrr:"+intent.getPackage());
		//		Log.d(TAG, "remove():rrr:"+intent.getScheme());
		//		Log.d(TAG, "remove():rrr:"+intent.getComponent().getClassName());
		//		Log.d(TAG, "remove():rrr:"+intent.getComponent().getPackageName());
		//		Log.d(TAG, "remove():rrr:"+intent.getComponent().getShortClassName());
		//		Log.d(TAG, "remove():rrr:"+intent.getData());

		Log.d(TAG, "remove():size:"+serviceBeanList.size());

		Iterator<ServiceBean> iterator = serviceBeanList.iterator();
		while (iterator != null && iterator.hasNext() ) {
			Log.d(TAG, "remove():hasNext....");
			ServiceBean serviceBean = iterator.next();
			if (serviceBean != null) {
				if ( equalsIntent(serviceBean.getIntent(), intent) ) {
					Log.d(TAG, "remove():execute......");
					iterator.remove();
				}
			}
		}
	}

	/**
	 * 比较两个Intent是否相等
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	protected boolean equalsIntent(Intent a, Intent b) {
		boolean equalsIntent = false;

		if (a != null && b != null) {
			ComponentName componentNameA = a.getComponent();
			ComponentName componentNameB = b.getComponent();
			if (componentNameA != null && componentNameB != null) {
				String classNameA = componentNameA.getClassName();
				String classNameB = componentNameB.getClassName();
				if (classNameA != null && classNameB != null) {
					if ( classNameA.equals(classNameB) ) {
						equalsIntent = true;
					}
				}
			}
		}

		Log.d(TAG, "equalsIntent():"+equalsIntent);
		return equalsIntent;
	}

	/**
	 * 得到Intent的绝对类路径
	 * 
	 * @param intent
	 * @return
	 */
	protected String getIntentClassName(Intent intent) {
		if (intent == null) {
			return null;
		}

		String className = null;
		ComponentName componentName = intent.getComponent();
		if (componentName != null) {
			className = componentName.getClassName();
		}
		return className;
	}

	/**
	 * 判断是否存存
	 * 
	 * @return
	 */
	public boolean isExists(Intent intent) {
		boolean isExists = false; 
		if (intent != null) {
			PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_NO_CREATE);
			if (pendingIntent != null) {
				isExists = true;
			}
		}
		Log.d(TAG, "isExists():"+isExists);
		return isExists;
	}

	/**
	 * 开始全部
	 */
	public void start() {
		Log.d(TAG, "start()");
		Log.d(TAG, "start():size:"+serviceBeanList.size());
		for (ServiceBean serviceBean : serviceBeanList) {
			// 执行
			execute(serviceBean);
		}
	}

	/**
	 * 执行
	 * 
	 * @param serviceBean
	 */
	protected void execute(ServiceBean serviceBean) {
		if (alarmManager == null) {
			return;
		}

		if (serviceBean == null) {
			return;
		}

		Log.d(TAG, "execute():getIntentClassName():"+getIntentClassName(serviceBean.getIntent())+",getInterval():"+serviceBean.getInterval());

		Intent intent = serviceBean.getIntent();
		long interval = serviceBean.getInterval();

		// 会替代旧的
		PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT); 
		long nowTimeMillis = System.currentTimeMillis();
		alarmManager.setRepeating(AlarmManager.RTC, nowTimeMillis, interval, pendingIntent);
	}

	/**
	 * 取消某个
	 * 
	 * @param intent
	 */
	public void cancel(Intent intent) {
		if (alarmManager == null) {
			return;
		}

		if (intent == null) {
			return;
		}
		
		Log.d(TAG, "cancel():intent.getAction():"+intent.getAction());
		PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_NO_CREATE);
		if (pendingIntent != null) {
			Log.d(TAG, "cancel():pendingIntent != null");
			// 取消正在执行的服务
			pendingIntent.cancel();
			alarmManager.cancel(pendingIntent);
		} else {
			Log.d(TAG, "cancel():pendingIntent == null");
		}
	}

	/**
	 * 停止全部
	 */
	public void stop() {
		Log.d(TAG, "stop()");
		Log.d(TAG, "stop():size:"+serviceBeanList.size());
		for (ServiceBean serviceBean : serviceBeanList) {
			// 取消
			cancel(serviceBean.getIntent());
		}
	}

}
