package com.gtr.studyproject.service;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.NetworkErrorException;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;

import com.myself.common.Mylog;

/**
 * 
 * @version 1.0.0
 * @author mac
 * @name ServiceLogin
 * @description 登陆后台服务
 * @date 2014-10-11
 * @link gtrstudio@qq.com
 * @copyright Copyright © 2010-2014 小天天 Studio, All Rights Reserved.
 */
public class ServiceLogin extends Service {
	private static final Object mSyncAdapterLock = new Object();
	private static ServiceLoginSyncAdapter mLoginSyncAdapter = null;

	@Override
	public void onCreate() {
		Mylog.info("ServiceLogin onCreate()");
		synchronized (mSyncAdapterLock) {
			if (mSyncAdapterLock == null) {
				mLoginSyncAdapter = new ServiceLoginSyncAdapter(
						getApplicationContext(), true);

			}
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		Mylog.info("ServiceLogin onBind(Intent intent)");
		return mLoginSyncAdapter.getSyncAdapterBinder();
	}

	/****************************************************** Inner Class ******************************************************/
	// 账号校验器
	public static class LoginAccountAuthenticator extends
			AbstractAccountAuthenticator {
		private final Context mContext;

		public LoginAccountAuthenticator(Context context) {
			super(context);
			this.mContext = context;
		}

		// 修改账号属性
		@Override
		public Bundle editProperties(AccountAuthenticatorResponse response,
				String accountType) {
			Mylog.info("editProperties(AccountAuthenticatorResponse response,String accountType)");
			return null;
		}

		// 添加账号
		@Override
		public Bundle addAccount(AccountAuthenticatorResponse response,
				String accountType, String authTokenType,
				String[] requiredFeatures, Bundle options)
				throws NetworkErrorException {
			Mylog.info("addAccount(AccountAuthenticatorResponse response,String accountType, String authTokenType,String[] requiredFeatures, Bundle options)");
			return null;
		}

		// 确认凭证
		@Override
		public Bundle confirmCredentials(AccountAuthenticatorResponse response,
				Account account, Bundle options) throws NetworkErrorException {
			Mylog.info("confirmCredentials(AccountAuthenticatorResponse response,Account account, Bundle options)");
			return null;
		}

		// 获取授权Token
		@Override
		public Bundle getAuthToken(AccountAuthenticatorResponse response,
				Account account, String authTokenType, Bundle options)
				throws NetworkErrorException {
			Mylog.info("getAuthToken(AccountAuthenticatorResponse response,Account account, String authTokenType, Bundle options)");
			return null;
		}

		// 获取授权Token标签
		@Override
		public String getAuthTokenLabel(String authTokenType) {
			Mylog.info("getAuthTokenLabel(String authTokenType)");
			return null;
		}

		// 更新证书凭证
		@Override
		public Bundle updateCredentials(AccountAuthenticatorResponse response,
				Account account, String authTokenType, Bundle options)
				throws NetworkErrorException {
			Mylog.info("updateCredentials(AccountAuthenticatorResponse response,Account account, String authTokenType, Bundle options)");
			return null;
		}

		// 有其他参数
		@Override
		public Bundle hasFeatures(AccountAuthenticatorResponse response,
				Account account, String[] features)
				throws NetworkErrorException {
			Mylog.info("hasFeatures(AccountAuthenticatorResponse response,Account account, String[] features)");
			return null;
		}
	}

}
