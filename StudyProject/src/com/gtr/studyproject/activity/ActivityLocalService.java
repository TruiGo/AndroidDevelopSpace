package com.gtr.studyproject.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;

import com.gtr.studyproject.service.ServiceMessage;
import com.myself.common.Mylog;
import com.xiaotian.framework.activity.BaseActivity;

/**
 * @version 1.0.0
 * @author XiaoTian
 * @name ActivitiesLocalService
 * @description 本地服务
 * @date 2014-5-16
 * @link gtrstudio@qq.com
 * @copyright Copyright © 2010-2014 小天天 Studio, All Rights Reserved.
 */
public class ActivityLocalService extends BaseActivity {
	boolean isBindServer;

	private ServiceMessage serviceMessage;
	
	// Service服务连接状态侦听
	private ServiceConnection serviceConnection = new ServiceConnection() {
		// 绑定服务连接服务时触发
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			Mylog.info("onServiceConnected");
			serviceMessage = ((ServiceMessage.LocalBinder) service).getServiceMessage();

		}

		// 断开连接时触发
		@Override
		public void onServiceDisconnected(ComponentName name) {
			Mylog.info("onServiceDisconnected");
			serviceMessage = null;
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fullscreen_xiaotian);
		doBindService();
	}

	void doBindService() {
		Mylog.info("doBindService");
		Intent service = new Intent(this, ServiceMessage.class); // 创建Service的Intent
		isBindServer = bindService(service, serviceConnection, Context.BIND_AUTO_CREATE); // 绑定Service
		Mylog.info("binded=" + isBindServer);
	}

	void doUnBindService() {
		Mylog.info("doUnBindService");
		if (isBindServer) unbindService(serviceConnection);// 解绑Service
		isBindServer = false;
	}

	public void onClickDummyButton(View view) {
		if (isBindServer) {
			doUnBindService();
		} else {
			doBindService();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		doUnBindService();
	}

}
