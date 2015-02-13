package com.gtr.studyproject.activity;

import com.myself.common.Mylog;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

/**
 * @version 1.0.0
 * @author mac
 * @name ActivitySyncAdapterLogin
 * @description 账号校验登陆Activity
 * @date 2014-10-11
 * @link gtrstudio@qq.com
 * @copyright Copyright © 2010-2014 小天天 Studio, All Rights Reserved.
 */
public class ActivitySyncAdapterLogin extends AccountAuthenticatorActivity {
	public static final String AUTHENTICATION_ACCOUNT_TYPE = "com.gtr.studyproject.activity.ACCOUNT_TYPE_SYNCADAPTERLOGIN";
	public static final String EXTRA_PARAM_ACCOUNT = "com.gtr.studyproject.activity.ACCOUNT";
	EditText editAccount, editPassword;
	AccountManager accountManager;

	@Override
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		accountManager = AccountManager.get(this);
		//
		setContentView(R.layout.activity_sync_adapter_login);
		editAccount = (EditText) findViewById(R.id.id_0);
		editPassword = (EditText) findViewById(R.id.id_1);
	}

	// 保存账号
	public void onClickSaveAccount(View view) {
		String accountName = editAccount.getText().toString().trim();
		String password = editPassword.getText().toString().trim();
		// 创建Account账号对象
		Account account = new Account(accountName, AUTHENTICATION_ACCOUNT_TYPE);
		Account[] accounts = accountManager
				.getAccountsByType(AUTHENTICATION_ACCOUNT_TYPE);
		if (accounts == null) {
			Mylog.info("在账号管理中无该类型的账号信息,添加新账号!");
			accountManager.addAccountExplicitly(account, password, null);
		} else {
			boolean hasExistAccount = false;
			for (Account ac : accounts) {
				if (ac.name.equals(account.name)) {
					Mylog.info("在账号管理中有该类型的账号,账号已经存在,修改账号的秘密!");
					hasExistAccount = true;
					accountManager.setPassword(account, password);
					break;
				}
			}
			if (!hasExistAccount) {
				Mylog.info("在账号管理中有该类型的账号,账号不存在,修改账号的秘密!");
				accountManager.addAccountExplicitly(account, password, null);
			}
		}
	}

}
