package com.gtr.studyproject.activity;

import com.xiaotian.framework.common.Mylog;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.net.Uri;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;

/**
 * 
 * @version 1.0.0
 * @author mac
 * @name ActivityPreferenceScreen
 * @description Preference Activity 适合于一些简单的参数配置页面,如果是复杂的配置页,
 *              通过继承Activity同时设置为Dialog的模式引用Preference启动的形式实现
 * @date 2014-10-9
 * @link gtrstudio@qq.com
 * @copyright Copyright © 2010-2014 小天天 Studio, All Rights Reserved.
 */
public class ActivityPreferenceScreen extends PreferenceActivity implements
		OnSharedPreferenceChangeListener {

	@Override
	@SuppressWarnings("deprecation")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.activiity_preferencescreen);

		// 绑定点击跳转的Intent
		Preference ratePref = findPreference("pref_rate");
		Uri uri = Uri.parse("market://details?id=" + getPackageName());
		Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
		ratePref.setIntent(goToMarket);
	}

	@Override
	protected void onResume() {
		super.onResume();
		getPreferenceScreen().getSharedPreferences()
				.registerOnSharedPreferenceChangeListener(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		getPreferenceScreen().getSharedPreferences()
				.unregisterOnSharedPreferenceChangeListener(this);
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		Mylog.info("onSharedPreferenceChanged key=" + key + " preferences="
				+ sharedPreferences);
		//
		if (key.equals("pref_username")) {
			EditTextPreference pref = (EditTextPreference) findPreference("pref_username");
			if (pref.getText() == null) {
				pref.setSummary("Username: ?");
			} else {
				pref.setSummary("Username: " + pref.getText());
			}
		}
	}

}
