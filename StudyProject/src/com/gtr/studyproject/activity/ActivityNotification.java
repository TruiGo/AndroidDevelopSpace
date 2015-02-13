package com.gtr.studyproject.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.android.support.appnavigation.app.ContentViewActivity;
import com.xiaotian.framework.activity.BaseFragmentActivity;
import com.xiaotian.framework.common.Mylog;
import com.xiaotian.frameworkxt.android.service.BaseService;
import com.xiaotian.frameworkxt.android.util.UtilEnvironment;
import com.xiaotian.frameworkxt.android.util.UtilNotification;

public class ActivityNotification extends BaseFragmentActivity {
	public static final String WAKELOCK_TAG = "com.gtr.studyproject.activity";
	UtilNotification utilNotification;
	DevicePolicyManager policyManager;// 机器策略管理器
	ComponentName componentName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notification);
		utilNotification = new UtilNotification(this);
		componentName = new ComponentName(this, LockScreenDeviceAdminReceiver.class);
		policyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
		// 改变屏幕亮度
		executeAsyncTask(new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... params) {
				int i = 0;
				while (i++ < 100) {
					try {
						Thread.sleep(1000);
						publishProgress();
					} catch (InterruptedException e) {}
				}
				return null;
			}

			@Override
			protected void onProgressUpdate(Void... values) {
				UtilEnvironment.changeScreenBrightness(getWindow(), (float) Math.random());
			}
		});
	}

	public void onClickSimple(View view) {
		utilNotification.sendNotification(0x001, R.drawable.ic_launcher, "Notification Title", "information", ActivityNotification.class, null);
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	public void onClickMutilateAction(View view) {
		NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		Intent intent = new Intent(this, ActivityNotification.class);
		PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent, 0);
		NotificationCompat.Builder builder = new NotificationCompat.Builder(this).setSmallIcon(R.drawable.icon_en)
				.setTicker("Mutilate Action Notification").setContentTitle("This is the msg title").setContentText("This is the content message.")
				.setContentIntent(pendingIntent);
		builder.addAction(R.drawable.icon_marka, "Action Mark A", pendingIntent);
		builder.addAction(R.drawable.icon_markb, "Action Mark B", pendingIntent);
		builder.addAction(R.drawable.icon_markc, "Action Mark C", pendingIntent);
		builder.addAction(R.drawable.icon_markd, "Action Mark D", pendingIntent);
		builder.addAction(R.drawable.icon_marke, "Action Mark E", pendingIntent);
		builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
		builder.setVibrate(new long[] { 200, 100, 200, 100 });
		nm.notify(0x002, builder.build());
	}

	public void onClickLockScreenStarActivityOverWakeScreen(View view) {
		// 启动延时服务 锁屏
		if (policyManager.isAdminActive(componentName)) {
			// 组件已有锁屏操作权,锁屏
			Mylog.info("DevicePolicyManager lockNow.");
			startService(new Intent(getBaseContext(), ServicePostWindowsAlert.class));
			policyManager.lockNow();
			// android.os.Process.killProcess(android.os.Process.myPid()); // 关闭当前线程会关闭关联未启动服务
		} else {
			// 请求组件锁屏操作权
			Mylog.info("DevicePolicyManager ACTION_ADD_DEVICE_ADMIN 请求锁定屏幕.");
			Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
			intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
			intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "请求锁定屏幕");
			startActivityForResult(intent, 0x001);
		}
	}

	public void onClickWindowsLevelDialog(View view) {
		// 不依赖本Activity的生命周期
		Intent intent = new Intent(getApplicationContext(), WindowsLevelMessageDialogActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_USER_ACTION);
		startActivity(intent);
	}

	@Override
	protected void onDestroy() {
		// 移除组件操作授权
		if (policyManager != null) policyManager.removeActiveAdmin(componentName);
		super.onDestroy();
	}

	@Override
	protected void onActivityResult(int requestCode, int responseCode, Intent data) {
		switch (requestCode) {
		case 0x001:
			if (policyManager.isAdminActive(componentName)) {
				startService(new Intent(getBaseContext(), ServicePostWindowsAlert.class));
				policyManager.lockNow();
				android.os.Process.killProcess(android.os.Process.myPid());
			}
			break;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		// 如果没授权则提示授权
		if (!policyManager.isAdminActive(componentName)) {
			Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
			intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
			intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "请求锁定屏幕");
			startActivity(intent);
		}
	}

	// 声明Dialog在锁屏的状态下[打开屏幕,显示对话框,保存屏幕开灯]
	@Override
	public void onAttachedToWindow() {
		// Window window = getWindow();
		// window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
		// | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
	}

	public static class LockScreenDeviceAdminReceiver extends DeviceAdminReceiver {
		@Override
		public void onEnabled(Context context, Intent intent) {
			Mylog.info("onEnabled");
			super.onEnabled(context, intent);
		}

		@Override
		public void onDisabled(Context context, Intent intent) {
			Mylog.info("onDisabled");
			super.onDisabled(context, intent);
		}

		@Override
		public void onReceive(Context context, Intent intent) {
			Mylog.info("onReceive");
			super.onReceive(context, intent);
		}
	}

	public static class ServicePostWindowsAlert extends BaseService {
		UtilNotification notification;

		@Override
		public void onCreate() {
			Mylog.info("onCreate");
			notification = new UtilNotification(getApplicationContext());
			super.onCreate();
		}

		@Override
		public void doInBackground() {
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {}
			//
			Mylog.info("Start Alert Windows Dialog");
			Toast.makeText(getBaseContext(), "Start Alert Windows Dialog", Toast.LENGTH_LONG).show();
			// Windows Leve Dialog Activity -> Dialog Theme
			Intent intent = new Intent(getApplicationContext(), WindowsLevelActivityOverLockScreen.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_USER_ACTION);
			startActivity(intent);
			// Notification
			notification.sendNotification(0x001, R.drawable.icon_marka, "ServicePostWindowsAlert", "", ActivityNotification.class, null);
		}
	}

	public static class WindowsLevelMessageDialogActivity extends Activity {
		// Activity->Dialog
		// android:theme="@android:style/Theme.Dialog"
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.interstitial_message);
		}

		public void onViewContent(View v) {
			TaskStackBuilder tsb = TaskStackBuilder.create(this);
			tsb.addParentStack(ActivityNotification.class); // 设置父栈
			Intent intent = new Intent(this, ContentViewActivity.class);
			intent.putExtra(ContentViewActivity.EXTRA_TEXT, "From WindowsLevelMessageDialogActivity Notification");
			tsb.addNextIntent(intent); // 在父栈添加Intent
			tsb.startActivities(); // 启动堆栈中的Activity[父->子]
			finish();
		}
	}

	public static class WindowsLevelActivityOverLockScreen extends Activity {
		// 背景主题
		// android:theme="@android:style/Theme.Wallpaper.NoTitleBar"
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			// 唤醒屏幕,解锁,保持亮屏[Window操作]
			final Window win = getWindow();
			win.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
					| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
			//
			setContentView(R.layout.interstitial_message);
		}

		public void onViewContent(View v) {
			TaskStackBuilder tsb = TaskStackBuilder.create(this);
			tsb.addParentStack(ActivityNotification.class); // 设置父栈
			Intent intent = new Intent(this, ContentViewActivity.class);
			intent.putExtra(ContentViewActivity.EXTRA_TEXT, "From WindowsLevelMessageDialogActivity Notification");
			tsb.addNextIntent(intent); // 在父栈添加Intent
			tsb.startActivities(); // 启动堆栈中的Activity[父->子]
			finish();
		}
	}
}
