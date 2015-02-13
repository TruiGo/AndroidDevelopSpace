package com.gtr.studyproject.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.gtr.studyproject.activity.ActivityLocalService;
import com.gtr.studyproject.activity.R;
import com.xiaotian.framework.common.Mylog;

/**
 * @version 1.0.0
 * @author XiaoTian
 * @name ServiceMessage
 * @description 联网接收消息服务
 * @date 2014-5-16
 * @link gtrstudio@qq.com
 * @copyright Copyright © 2010-2014 小天天 Studio, All Rights Reserved.
 */
public class ServiceMessage extends Service {
	private int NOTIFICATION = R.string.local_service_started;
	// 提醒管理器
	private NotificationManager mNM;
	//
	private final IBinder mBinder = new LocalBinder();

	@Override
	public IBinder onBind(Intent intent) {
		Mylog.info("onBind");
		return mBinder;
	}

	@Override
	public void onCreate() {
		Mylog.info("onCreate");
		mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		showNotification();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Mylog.info("onStartCommand");
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		Mylog.info("onDestroy");
		mNM.cancel(NOTIFICATION);
	}

	private void showNotification() {
		// 发送状态栏的通知消息
		// expanded notification
		CharSequence text = getText(R.string.local_service_started);
		Intent notificationIntent = new Intent(this, ActivityLocalService.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
		NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext());
		notificationBuilder.setAutoCancel(true).setDefaults(Notification.DEFAULT_ALL).setWhen(System.currentTimeMillis())
				.setSmallIcon(R.drawable.ic_launcher).setTicker("Optional ticker").setContentTitle("Default notification")
				.setContentText("Lorem ipsum dolor sit amet, consectetur adipiscing elit.")
				.setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND).setContentIntent(pendingIntent)
				.setContentInfo("Info");
		// Send the notification.
		mNM.notify(NOTIFICATION, notificationBuilder.build());
	}

	// Inner Class Binder 绑定器,绑定当前服务
	public class LocalBinder extends Binder {
		public LocalBinder() {

		}

		public ServiceMessage getServiceMessage() {
			Mylog.info("ServiceMessage");
			return ServiceMessage.this;
		}
	}
}
