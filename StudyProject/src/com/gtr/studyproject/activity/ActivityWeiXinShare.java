package com.gtr.studyproject.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.SyncStateContract.Constants;

import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.xiaotian.framework.activity.BaseFragmentActivity;

/**
 * @version 1.0.0
 * @author mac
 * @name ActivityWeiXinShare
 * @description 微信分享
 * @date Nov 26, 2014
 * @link gtrstudio@qq.com
 * @copyright Copyright © 2010-2014 小天天 Studio, All Rights Reserved.
 */
public class ActivityWeiXinShare extends BaseFragmentActivity {
	String APP_ID = "wxd930ea5d5a258f4f";
	
	public class AppRegister extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			final IWXAPI api = WXAPIFactory.createWXAPI(context, null);

			// 注册APP ID
			api.registerApp(APP_ID);
		}
	}
}
