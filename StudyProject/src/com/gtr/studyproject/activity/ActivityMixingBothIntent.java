package com.gtr.studyproject.activity;

import java.util.List;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;

import com.myself.common.Mylog;
import com.xiaotian.framework.activity.BaseFragmentActivity;
import com.xiaotian.frameworkxt.android.util.UtilEnvironment;
import com.xiaotian.frameworkxt.android.util.UtilSystemIntent;

/**
 * 
 * @version 1.0.0
 * @author mac
 * @name ActivityMixingBothIntent
 * @description Mixing Both Intent
 * @date 2014-10-13
 * @link gtrstudio@qq.com
 * @copyright Copyright © 2010-2014 小天天 Studio, All Rights Reserved.
 */
public class ActivityMixingBothIntent extends BaseFragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mixing_bothintent);
	}

	public void onClickChoseImage(View view) {
		Intent pickIntent = new Intent(Intent.ACTION_GET_CONTENT);
		pickIntent.setType("image/*");
		startActivityForResult(Intent.createChooser(pickIntent, "请选择照片"), 0x001);
	}

	public void onClickChoseApp(View view) {
		// 混合Intent
		// 打开Image文件的Intent选取器
		Intent pickIntent = new Intent(Intent.ACTION_GET_CONTENT);
		pickIntent.setType("image/*");
		// 打开相机的Intent,选取器
		Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		// 选择对话
		Intent chooserIntent = Intent.createChooser(pickIntent, "请选择照片选取程序:");
		// 添加打开的Intent到对话框
		chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] { takePhotoIntent });
		// 显示选择对话
		startActivityForResult(chooserIntent, 0x001);
	}

	public void onClickChoseSendEmail(View view) {
		UtilSystemIntent.sendEmailIntent(this, new String[] { "gtrstudio@qq.com" }, "选择打开Email程序", "标题", "信息");
	}

	public void onClickChoseDownloadQQFromMarket(View view) {
		// List All Application
		List<ApplicationInfo> apps = UtilEnvironment.getInstalledAPP(getApplicationContext());
		for (ApplicationInfo app : apps) {
			Mylog.info(app.packageName);
		}
		// 市场下载程序
		if (UtilEnvironment.isAPPAvailable(this, "com.gtr.studyproject.activity")) {
			UtilSystemIntent.downloadAPPFromMarket(this, "com.gtr.studyproject.activity");
		} else {
			toast("已经存在该应用了...");
		}
	}
}
