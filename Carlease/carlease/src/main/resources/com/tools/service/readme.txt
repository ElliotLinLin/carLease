

接收系统的时间流逝的广播 ACTION_TIME_TICK 一分钟发一次	----- 只有通过Context.registerReceiver()方法来注册
屏幕开启关闭的广播，	----- 只有通过Context.registerReceiver()方法来注册  --- 应用退出就没用了
电量改变的广播。----- 只有通过Context.registerReceiver()方法来注册
SIGNAL_STRENGTH_CHANGED_ACTION 广播：电话的信号强度已经改变。 ----- 找不到此Action
STATISTICS_STATE_CHANGED_ACTION 广播：统计信息服务的状态已经改变。 ----- 找不到此Action


硬件时钟 --- mAlarmManager = (AlarmManager)getSystemService(ALARM_SERVICE); 
http://blog.csdn.net/u010538765/article/details/10859943


ACTION_TIME_TICK   当前时间改变，每分钟都发送，不能通过组件声明来接收，只有通过Context.registerReceiver()方法来注册

ACTION_BATTERY_CHANGED 电池的充电状态、电荷级别改变，不能通过组建声明接收这个广播，只有通过Context.registerReceiver()注册


　String  SIGNAL_STRENGTH_CHANGED_ACTION  广播：电话的信号强度已经改变。  "android.intent.action.SIG_STR"
　String  STATISTICS_STATE_CHANGED_ACTION  广播：统计信息服务的状态已经改变。  "android.intent.action.STATISTICS_STATE_CHANGED"



String  SCREEN_OFF_ACTION  广播：屏幕被关闭。  "android.intent.action.SCREEN_OFF"
String  SCREEN_ON_ACTION  广播：屏幕已经被打开。  "android.intent.action.SCREEN_ON"


这些广播在清单文件里注册时，看不到效果。在这个例子中通过在一个服务里面用代码注册的方式，成功接收了这些广播


