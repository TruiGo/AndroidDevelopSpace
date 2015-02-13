package com.gtr.studyproject.activity;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;

import com.xiaotian.framework.activity.BaseActivity;

/**
 * @version 1.0.0
 * @author XiaoTian
 * @name ActivityOtherAPKBundle
 * @description
 * @date 2014-9-27
 * @link gtrstudio@qq.com
 * @copyright Copyright © 2010-2014 小天天 Studio, All Rights Reserved.
 */
public class ActivityOtherAPKBundle extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 读取 android:sharedUserId="com.studyproject.sharebundler" 的APK资源
		try {
			Context bundleContext = createPackageContext(
					"com.example.studyprojectbundle",
					Context.CONTEXT_IGNORE_SECURITY);
			// String bundlerName = bundleContext.getResources().getString(
			// com.example.studyprojectbundle.R.string.bundler_name);
			// Mylog.info("Get Bundle Name=" + bundlerName);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
	}

}
