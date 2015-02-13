package com.gtr.studyproject.reveiver;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.widget.Toast;

import com.gtr.studyproject.activity.ActivityMain;
import com.gtr.studyproject.activity.R;
import com.xiaotian.frameworkxt.android.util.UtilNotification;
import com.xiaotian.frameworkxt.android.util.UtilSDKVersion;

/**
 * @version 1.0.0
 * @author XiaoTian
 * @name BroadcastReceiverBootTime
 * @description Boot Receiver 必须安装在内部储蓄卡中,不然会接收不到逛广播
 * @date 2014-10-17
 * @link gtrstudio@qq.com
 * @copyright Copyright © 2010-2014 小天天 Studio, All Rights Reserved.
 */
public class BroadcastReceiverBootTime extends com.xiaotian.frameworkxt.android.receiver.BroadcastReceiverBootTime {
	public BroadcastReceiverBootTime() {
		super(null);
	}

	public BroadcastReceiverBootTime(Context context, Object[] initPerometers) {
		super(context, initPerometers);
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		UtilNotification un = new UtilNotification(context);
		un.sendNotification(0x001, R.drawable.ic_drawer, "Title", "Content...", ActivityMain.class, null);
		Toast.makeText(context, "手机启动了,开启后台服务.", Toast.LENGTH_LONG).show();
		// 手动启动服务,尽量在这里不执行多的任务
		Intent serviceIntent = new Intent(context, BootTimeService.class);
		context.startService(serviceIntent);
	}

	public static class BootTimeService extends Service {
		AsyncTask<Void, Void, Void> execute = new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... params) {
				int count = 0;
				while (count++ < 20) {
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {}
					publishProgress();
				}
				return null;
			}

			@Override
			protected void onProgressUpdate(Void... values) {
				UtilNotification un = new UtilNotification(getApplicationContext());
				un.sendNotification(0x002, R.drawable.ic_drawer, "手机启动了,onStartCommand", "Execute AsynctTask In Service.", ActivityMain.class, null);
				Toast.makeText(getApplication(), "Execute AsynctTask In Service.", Toast.LENGTH_SHORT).show();
			}
		};

		@Override
		public int onStartCommand(Intent intent, int flags, int startId) {
			UtilNotification un = new UtilNotification(getApplicationContext());
			un.sendNotification(0x002, R.drawable.ic_drawer, "手机启动了,onStartCommand", "Content...", ActivityMain.class, null);
			if (UtilSDKVersion.hasHoneycomb()) {
				execute.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
			} else {
				execute.execute();
			}
			return START_STICKY;
		}

		@Override
		public IBinder onBind(Intent intent) {
			return null;
		}
	}
}
