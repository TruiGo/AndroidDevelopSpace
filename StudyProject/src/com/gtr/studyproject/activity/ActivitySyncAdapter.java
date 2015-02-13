package com.gtr.studyproject.activity;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.os.Bundle;

import com.xiaotian.framework.activity.BaseFragmentActivity;

/**
 * @version 1.0.0
 * @author mac
 * @name ActivitySyncAdapter
 * @description
 * @date 2014-10-13
 * @link gtrstudio@qq.com
 * @copyright Copyright © 2010-2014 小天天 Studio, All Rights Reserved.
 */
public class ActivitySyncAdapter extends BaseFragmentActivity {
	public static final String PARAM_ACCOUNT_TYPE = "com.gtr.studyproject.activity.ACCOUNT_TYPE";
	AccountManager mAccountManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 获取账号管理器
		mAccountManager = AccountManager.get(this);
		// 获取指定类型的账号
		Account[] accounts = mAccountManager
				.getAccountsByType(PARAM_ACCOUNT_TYPE);
		if (accounts.length == 0) {
			// 账号为空,登陆页面
			Intent intent = new Intent(this, ActivitySyncAdapterLogin.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
			startActivityForResult(intent, 0x001);
		} else {
			// 获取账号密码
			String password = mAccountManager.getPassword(accounts[0]);
			if (password == null) {
				// 密码为空,登陆页面
				Intent intent = new Intent(this, ActivitySyncAdapterLogin.class);
				intent.putExtra(ActivitySyncAdapterLogin.EXTRA_PARAM_ACCOUNT,
						accounts[0].name);
				startActivityForResult(intent, 0x002);
			} else {
				// 成功获取账号,密码
				toast("从AccountManager 中获取的到账号:" + accounts[0].name + " 密码:"
						+ password);
			}
		}
	}

}
