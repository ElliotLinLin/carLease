package com.tools.app;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;

import com.tools.content.pm.PermissionTool;
import com.tools.util.Log;

import java.lang.reflect.Method;


/**
 * <uses-permission android:name="android.permission.VIBRATE"/>
 * <p>
 * TODO android.support.v4.app.NotificationCompat.Builder 可以代替此模块。
 * <p>
 * 使用远程视图（Remote View）设置contentView和contentIntent，这样可以为扩展状态显示分配一个你需要的定制UI.
 * <p>
 * 如果要修改定制布局中视图的属性或者外观，可以使用远程视图对象的set*方法
 * mNotification.contentView = new RemoteViews(this.getPackageName(),R.layout.my_status);
 * mNotification.contentIntent = mContentIntent;
 * mNotification.contentView.setImageViewResource(R.id.status_icon,R.drawable.icon);
 * mNotification.contentView.setTextViewText(R.id.status_text, "This is test content");
 * <p>
 * 使用例子：
 * <p>
 * // 创建对象
 * private NotificationUtil notification = null;
 * // 分配对象
 * notification = new NotificationUtil(this);
 * // 通知
 * notification.sendNotification(1, R.drawable.ic_launcher, "aaa", "title", "contentText", new Intent(null, null));
 * // 关闭
 * notification.cancel(1);
 * // 关闭全部
 * notification.cancelAll();
 *
 * @author lmc
 *         版权声明：版权归作者所有，未经协议授权不得使用、转载、抄袭等，否则依法追究法律责任和经济责任。
 */
public class NotificationUtil {

    private static final String TAG = NotificationUtil.class.getSimpleName();

    protected Context context = null;
    protected int icon = 0;
    protected NotificationManager notificationManager;

    // 是否使用提示音
    protected boolean isUseSound = true;
    // 是否使用默认提示音
    protected boolean isUseSoundDefault = true;
    // 是否使用自定义提示音
    protected boolean isUseSoundUri = false;

    // 是否使用震动
    protected boolean isUseVibrate = true;
    // 是否使用闪光
    protected boolean isUseFlashlight = true;

    protected int flags = 0;

    // 自定义提示音参数
    protected android.net.Uri soundUri;
    protected int audioStreamType;

    //是否常驻状态栏
    private boolean isDurableStatusBar = false;

    // 远程视图
    //

    public NotificationUtil(Context context) {
        init(context);
    }


    /**
     * 初始化
     *
     * @param context
     */
    private void init(Context context) {
        // 检查权限
        PermissionTool.checkThrow(context, android.Manifest.permission.VIBRATE);

        this.context = context;

        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
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
     * 是否使用提示音
     */
    public void setUseSound(boolean use) {
        isUseSound = use;
    }

    /**
     * 设置为默认的提示音
     */
    public void setSoundDefault() {
        setUseSound(true);
        this.isUseSoundDefault = true;
        this.isUseSoundUri = false;
    }


    /**
     * 是否常驻状态栏
     *
     * @return boolean
     * 2014-3-17
     * @author MoSQ
     */
    public boolean isDurableStatusBar() {
        return isDurableStatusBar;
    }

    /**
     * 设置是否常驻状态栏
     *
     * @param isDurableStatusBar void
     *                           2014-3-17
     * @author MoSQ
     */
    public void setDurableStatusBar(boolean isDurableStatusBar) {
        this.isDurableStatusBar = isDurableStatusBar;
    }

    /**
     * 设置自定义提示音
     * //	int n = AudioManager.STREAM_ALARM;
     * //	int a = AudioManager.STREAM_MUSIC;
     * <p>
     * wav 文件请使用：
     * notif.setSound(uri, AudioManager.STREAM_MUSIC);
     * notif.setSound(uri, AudioManager.STREAM_DTMF);
     * notif.setSound(uri, AudioManager.STREAM_ALARM); // 不行
     * notif.setSound(uri, AudioManager.STREAM_VOICE_CALL); // 不行
     * notif.setSound(uri, AudioManager.STREAM_NOTIFICATION);
     * notif.setSound(uri, AudioManager.STREAM_RING);
     * notif.setSound(uri, AudioManager.STREAM_SYSTEM);
     * <p>
     * 下面方法可以使用，使用R.raw里的ID（已验证）
     * android.net.Uri uri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + R.raw.test);
     * 下面方法不能使用
     * android.net.Uri uri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + "aa.wav");
     */
    public void setSound(android.net.Uri soundUri, int audioStreamType) {
        setUseSound(true);
        this.isUseSoundDefault = false;
        this.isUseSoundUri = true;
        //
        this.soundUri = soundUri;
        if (this.soundUri != null) {
            Log.e(TAG, "soundUri:" + this.soundUri.toString());
        }
        this.audioStreamType = audioStreamType;
    }

    /**
     * 是否使用震动
     */
    public void setUseVibrate(boolean use) {
        isUseVibrate = use;
    }

    /**
     * 设置震动
     */
    private void setVibrate(Notification notification) {
        notification.defaults |= Notification.DEFAULT_VIBRATE;
    }

    /**
     * 是否使用闪光灯
     */
    public void setUseFlashlight(boolean use) {
        isUseFlashlight = use;
    }

    /**
     * 设置闪光灯
     */
    private void setFlashlight(Notification notification) {
        notification.ledARGB = Color.BLUE;
        notification.ledOffMS = 0;
        notification.ledOnMS = 1;
        notification.flags |= Notification.FLAG_SHOW_LIGHTS;
    }

    /**
     * 设置风格
     *
     * @param flags
     */
    public void setFlags(int flags) {
        this.flags = flags;
    }

    /**
     * 发送可关闭的通知
     *
     * @param id
     * @param icon
     * @param tickerText
     * @param contentTitle
     * @param contentText
     */
    public void sendNotification(int id,
                                 int icon,
                                 String tickerText,
                                 String contentTitle,
                                 String contentText) {

        sendNotification(id, icon, tickerText, contentTitle, contentText, null);

    }

    /**
     * 发送通知
     *
     * @param id           通知ID
     * @param icon         图标
     * @param tickerText   状态栏闪现内容
     * @param contentTitle 标题
     * @param contentText  内容
     * @param intent       Intent
     */
    public void sendNotification(int id,
                                 int icon,
                                 String tickerText,
                                 String contentTitle,
                                 String contentText,
                                 Intent intent) {

        Notification notification = new Notification();
        //		android.support.v4.app.NotificationCompat.Builder notification = new android.support.v4.app.NotificationCompat.Builder(context);

        notification.when = System.currentTimeMillis();
        //		notification.setWhen(System.currentTimeMillis());

        notification.tickerText = tickerText;
        //		notification.setTicker(tickerText);

        notification.icon = icon;
        //		notification.setSmallIcon(icon);

        // 点击可关闭
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        //		notification.setAutoCancel(true);

        if (flags != 0) {
            notification.flags |= flags;
        }

        if (isUseSound) {
            if (this.isUseSoundDefault) {
                notification.defaults |= Notification.DEFAULT_SOUND;
            } else if (this.isUseSoundUri) {
                notification.audioStreamType = this.audioStreamType;
                notification.sound = this.soundUri;
//								notification.setSound(soundUri);
            }
        }

        if (isUseVibrate) {
            setVibrate(notification);
        }

        if (isUseFlashlight) {
            setFlashlight(notification);
        }

        if (intent == null) {
            intent = new Intent();
        }

		/*
         * PendingIntent的使用
		 * Flags的类型：
    FLAG_ONE_SHOT：得到的pi只能使用一次，第二次使用该pi时报错
    FLAG_NO_CREATE： 当pi不存在时，不创建，返回null  
    FLAG_CANCEL_CURRENT： 每次都创建一个新的pi  
    FLAG_UPDATE_CURRENT： 不存在时就创建，创建好了以后就一直用它，每次使用时都会更新pi的数据(使用较多) 
		 * */

        // 下面语句一定要加入intent对象，不能为null
        // http://blog.csdn.net/ydpl2007/article/details/7591642
        // http://blog.csdn.net/jjmm2009/article/details/7240506
        // PendingIntent pendingIntent = PendingIntent.getActivity( context, 0, intent, 0 ); // flags == 0 是不行的，intent数据不会更新
        // TODO 注意第二个参数
        PendingIntent pendingIntent = PendingIntent.getActivity(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//		notification.setLatestEventInfo(context, contentTitle, contentText, pendingIntent);


        if (Build.VERSION.SDK_INT < 16) {
            Class clazz = notification.getClass();
            try {
                Method m2 = clazz.getDeclaredMethod("setLatestEventInfo", Context.class, CharSequence.class, CharSequence.class, PendingIntent.class);
                m2.invoke(notification, context, contentTitle,
                        contentText, pendingIntent);
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }

        } else {
            notification = new Notification.Builder(context)
                    .setAutoCancel(true)
                    .setContentTitle(contentTitle)
                    .setContentText(contentText)
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(icon)
                    .setWhen(System.currentTimeMillis())
                    .build();
        }


        notificationManager.notify(id, notification);
    }

    /**
     * 发送"正在运行中的"通知
     */
    public void sendOngoing(int id,
                            int icon,
                            String tickerText,
                            String contentTitle,
                            String contentText,
                            Intent intent) {

        if (intent == null) {
            Log.exception(TAG, new NullPointerException("intent == null"));
            return;
        }

        Notification notification = new Notification();

        notification.when = System.currentTimeMillis();
        notification.tickerText = tickerText;
        notification.icon = icon;

        boolean isDurableStatus = isDurableStatusBar();
        if (isDurableStatus) {
            //常驻状态栏，要加上这一句Notification.FLAG_ONGOING_EVENT
            notification.flags |= Notification.FLAG_ONGOING_EVENT;
        }

        notification.defaults |= Notification.DEFAULT_SOUND;


        if (isUseFlashlight) {
            setFlashlight(notification);
        }

        // 下面语句一定要加入intent对象，不能为null
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
//        notification.setLatestEventInfo(context, contentTitle, contentText, pendingIntent);

        if (Build.VERSION.SDK_INT < 16) {
            Class clazz = notification.getClass();
            try {
                Method m2 = clazz.getDeclaredMethod("setLatestEventInfo", Context.class, CharSequence.class, CharSequence.class, PendingIntent.class);
                m2.invoke(notification, context, contentTitle,
                        contentText, pendingIntent);
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }

        } else {
            notification = new Notification.Builder(context)
                    .setAutoCancel(true)
                    .setContentTitle(contentTitle)
                    .setContentText(contentText)
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(icon)
                    .setWhen(System.currentTimeMillis())
                    .build();
        }


        cancel(id);//把之前的取消掉


        notificationManager.notify(id, notification);

    }

    /**
     * 关闭
     *
     * @param id
     */
    public void cancel(int id) {
        if (notificationManager == null) {
            return;
        }
        notificationManager.cancel(id);
    }

    /**
     * 关闭全部
     */
    public void cancelAll() {
        if (notificationManager == null) {
            return;
        }
        notificationManager.cancelAll();
    }


}
