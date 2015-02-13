package com.gtr.studyproject.service;

import android.accounts.Account;
import android.annotation.TargetApi;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.os.Build;
import android.os.Bundle;

import com.myself.common.Mylog;

/**
 * 
 * @version 1.0.0
 * @author mac
 * @name ServiceSyncAdapter
 * @description SyncAdapter Service
 * @date 2014-10-11
 * @link gtrstudio@qq.com
 * @copyright Copyright © 2010-2014 小天天 Studio, All Rights Reserved.
 */
public class ServiceLoginSyncAdapter extends AbstractThreadedSyncAdapter {

	// 构造器
	public ServiceLoginSyncAdapter(Context context, boolean autoInitialize) {
		super(context, autoInitialize);
		Mylog.info("ServiceSyncAdapter(Context context, boolean autoInitialize) ");
	}

	// 构造器V19
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public ServiceLoginSyncAdapter(Context context, boolean autoInitialize,
			boolean allowParallelSyncs) {
		super(context, autoInitialize, allowParallelSyncs);
		Mylog.info("ServiceSyncAdapter(Context context, boolean autoInitialize,boolean allowParallelSyncs)");
	}

	// 执行同步
	@Override
	public void onPerformSync(Account account, Bundle extras, String authority,
			ContentProviderClient provider, SyncResult syncResult) {
		Mylog.info("onPerformSync(Account account, Bundle extras, String authority,ContentProviderClient provider, SyncResult syncResult)");
	}

}
