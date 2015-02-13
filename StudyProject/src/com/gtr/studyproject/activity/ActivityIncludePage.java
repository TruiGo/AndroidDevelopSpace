package com.gtr.studyproject.activity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;

import com.baidu.mapapi.SDKInitializer;
import com.xiaotian.framework.activity.BaseFragmentActivity;

/**
 * 
 * @version 1.0.0
 * @author mac
 * @name ActivityIncludPage
 * @description Include Tag
 * @date 2014-10-9
 * @link gtrstudio@qq.com
 * @copyright Copyright © 2010-2014 小天天 Studio, All Rights Reserved.
 */
public class ActivityIncludePage extends BaseFragmentActivity {
	ViewStub viewStubBaiduMap; // 延时加载的ViewStub,用于延时加载/实例化View

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SDKInitializer.initialize(getApplicationContext());

		setContentView(R.layout.activity_include_page);
		viewStubBaiduMap = (ViewStub) findViewById(R.id.id_0);
	}

	public void onClickShowBaiduMap(View view) {
		// 设置 Visible 时才加载View[或者调用inflate方法]
		viewStubBaiduMap.setVisibility(View.VISIBLE);// View Stub
	}
}
