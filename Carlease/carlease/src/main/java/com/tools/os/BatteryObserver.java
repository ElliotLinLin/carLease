package com.tools.os;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;

import com.tools.util.Log;

/**
 * 电池观察者
 *
 * 电池和温度和电量事件监听
 * 使用例子：
 *		BatteryObserver batteryObserver = new BatteryObserver(this);
		batteryLevelReceiver = batteryObserver.registerBatteryReceiver(
				new BatteryObserver.ILevelListener() {
			@Override
			public void onLevelChangeMore(int level) {
				Log.d(TAG, "onLevelChangeMore leve l"+level);
			}

			@Override
			public void onLevelChangeLess(int level) {
				Log.d(TAG, "onLevelChangeLess level "+level);

			}

			@Override
			public void onLevelChange(int level) {
				Log.d(TAG, "onLevelChange level "+level);

			}
		}, 100);

		temperatureReceiver = batteryObserver.registerBatteryReceiver(
				new BatteryObserver.ITemperatureListener() {
			@Override
			public void onTemperatureChangeMore(double temperature) {
				Log.d(TAG, "onTemperatureChangeMore temperature "+temperature);
			}

			@Override
			public void onTemperatureChangeLess(double temperature) {
				Log.d(TAG, "onTemperatureChangeLess temperature "+temperature);

			}

			@Override
			public void onTemperatureChange(double temperature) {
				Log.d(TAG, "onTemperatureChange temperature "+temperature);

			}

			@Override
			public void onTemperatureOverHeat(double temperature) {
				Log.d(TAG, "onTemperatureOverHeat temperature "+temperature);
			}

		}, 60.0);
 *
 * @author LMC
 *
 */
public class BatteryObserver {
	private static final String TAG = BatteryObserver.class.getSimpleName();

	private Context context = null;

	public ILevelListener levelListener = null;
	public ITemperatureListener temperatureListener = null;

	/**
	 * 电池电量状态监听器
	 *
	 * @author Liaojp
	 * @date 2014-2-12
	 */
	public interface ILevelListener {
		/**
		 * 电量小于指定值
		 *
		 * @param level
		 */
		void onLevelChangeLess(int level);

		/**
		 * 电量等于指定值
		 *
		 * @param level
		 */
		void onLevelChange(int level);

		/**
		 * 电量大于指定值
		 *
		 * @param level
		 */
		void onLevelChangeMore(int level);

	}

	/**
	 * 电池温度状态监听器
	 *
	 * @author Liaojp
	 * @date 2014-2-12
	 */
	public interface ITemperatureListener {
		/**
		 * 温度小于指定值
		 *
		 * @param temperature
		 */
		void onTemperatureChangeLess(double temperature);

		/**
		 * 温度等于指定值
		 *
		 * @param temperature
		 */
		void onTemperatureChange(double temperature);

		/**
		 * 温度大于指定值
		 *
		 * @param temperature
		 */
		void onTemperatureChangeMore(double temperature);

		/**
		 * 温度过热
		 * @param temperature 过热时的温度 。C
		 */
		void onTemperatureOverHeat(double temperature);
	}

	public BatteryObserver(Context context) {
		this.context = context;

	}

	/**
	 * 注册电池监听
	 *
	 * @param iLevelListener
	 *            电池电量变化监听器
	 * @param level
	 *            指定监听电池电量
	 * @return 接收器
	 */
	public BroadcastReceiver registerBatteryReceiver(
			ILevelListener iLevelListener, final int level) {
		Log.i(TAG, "registerBatteryReceiver");
		this.levelListener = iLevelListener;
		BroadcastReceiver mReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				String action = intent.getAction();
				int BatteryN; // 目前电量
				int BatteryV; // 电池电压
				double BatteryT; // 电池温度
				String BatteryStatus = ""; // 电池状态
				String BatteryTemp = ""; // 电池使用情况
				if (action.equals(Intent.ACTION_BATTERY_CHANGED)) {
					BatteryN = intent.getIntExtra("level", 0); // 目前电量
					BatteryV = intent.getIntExtra("voltage", 0); // 电池电压
					BatteryT = intent.getIntExtra("temperature", 0); // 电池温度
					if (BatteryN < level) {
						// 小于指定电量
						levelListener.onLevelChangeLess(level);
					} else if (BatteryN == level) {
						// 等于指定电量
						levelListener.onLevelChange(level);
					} else if (BatteryN > level) {
						// 大于指定电量
						levelListener.onLevelChangeMore(level);
					}

					switch (intent.getIntExtra("status",
							BatteryManager.BATTERY_STATUS_UNKNOWN)) {
					case BatteryManager.BATTERY_STATUS_CHARGING:
						BatteryStatus = "充电状态";
						break;
					case BatteryManager.BATTERY_STATUS_DISCHARGING:
						BatteryStatus = "放电状态";
						break;
					case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
						BatteryStatus = "未充电";
						break;
					case BatteryManager.BATTERY_STATUS_FULL:
						BatteryStatus = "充满电";
						break;
					case BatteryManager.BATTERY_STATUS_UNKNOWN:
						BatteryStatus = "未知道状态";
						break;
					}

					switch (intent.getIntExtra("health",
							BatteryManager.BATTERY_HEALTH_UNKNOWN)) {
					case BatteryManager.BATTERY_HEALTH_UNKNOWN:
						BatteryTemp = "未知错误";
						break;
					case BatteryManager.BATTERY_HEALTH_GOOD:
						BatteryTemp = "状态良好";
						break;
					case BatteryManager.BATTERY_HEALTH_DEAD:
						BatteryTemp = "电池没有电";
						break;
					case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE:
						BatteryTemp = "电池电压过高";
						break;
					case BatteryManager.BATTERY_HEALTH_OVERHEAT:

						BatteryTemp = "电池过热";
						break;
					}
					Log.i(TAG, "当前电量为" + BatteryN + "% --- " + BatteryStatus);
				}
			}
		};


		IntentFilter iFilter = new IntentFilter();
		iFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
		context.registerReceiver(mReceiver, iFilter); // 注册电池状态监听

		return mReceiver;
	}

	/**
	 * 注册电池温度监听
	 *
	 * @param iTemperatureListener
	 *            电池温度变化监听器
	 * @param temperature
	 *            指定监听电池温度
	 * @return 接收器
	 */
	public BroadcastReceiver registerBatteryReceiver(
			ITemperatureListener iTemperatureListener, final double temperature) {
		Log.i(TAG, "registerBatteryReceiver");
		this.temperatureListener = iTemperatureListener;
		BroadcastReceiver mReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				String action = intent.getAction();
				int BatteryN; // 目前电量
				int BatteryV; // 电池电压
				double BatteryT; // 电池温度
				String BatteryStatus = ""; // 电池状态
				String BatteryTemp = ""; // 电池使用情况
				if (action.equals(Intent.ACTION_BATTERY_CHANGED)) {
					BatteryN = intent.getIntExtra("level", 0); // 目前电量
					BatteryV = intent.getIntExtra("voltage", 0); // 电池电压
					BatteryT = intent.getIntExtra("temperature", 0); // 电池温度
					BatteryT = BatteryT * 0.1;

					if (BatteryT < temperature) {
						// 小于指定温度
						temperatureListener.onTemperatureChangeLess(temperature);
					} else if (BatteryT == temperature) {
						// 等于指定温度
						temperatureListener.onTemperatureChange(temperature);
					} else if (BatteryT > temperature) {
						// 大于指定温度
						temperatureListener.onTemperatureChangeMore(temperature);
					}

					switch (intent.getIntExtra("status",
							BatteryManager.BATTERY_STATUS_UNKNOWN)) {
					case BatteryManager.BATTERY_STATUS_CHARGING:
						BatteryStatus = "充电状态";
						break;
					case BatteryManager.BATTERY_STATUS_DISCHARGING:
						BatteryStatus = "放电状态";
						break;
					case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
						BatteryStatus = "未充电";
						break;
					case BatteryManager.BATTERY_STATUS_FULL:
						BatteryStatus = "充满电";
						break;
					case BatteryManager.BATTERY_STATUS_UNKNOWN:
						BatteryStatus = "未知道状态";
						break;
					}

					switch (intent.getIntExtra("health",
							BatteryManager.BATTERY_HEALTH_UNKNOWN)) {
					case BatteryManager.BATTERY_HEALTH_UNKNOWN:
						BatteryTemp = "未知错误";
						break;
					case BatteryManager.BATTERY_HEALTH_GOOD:
						BatteryTemp = "状态良好";
						break;
					case BatteryManager.BATTERY_HEALTH_DEAD:
						BatteryTemp = "电池没有电";
						break;
					case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE:
						BatteryTemp = "电池电压过高";
						break;
					case BatteryManager.BATTERY_HEALTH_OVERHEAT:
						temperatureListener.onTemperatureOverHeat(temperature);
						BatteryTemp = "电池过热";
						break;
					}
					Log.i(TAG, "温度为" + BatteryT + "℃");
				}

			}
		};

		IntentFilter iFilter = new IntentFilter();
		iFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
		context.registerReceiver(mReceiver, iFilter); // 注册电池状态监听

		return mReceiver;
	}
}
