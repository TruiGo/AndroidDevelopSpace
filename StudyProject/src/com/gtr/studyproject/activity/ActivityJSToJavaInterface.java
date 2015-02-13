package com.gtr.studyproject.activity;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.xiaotian.framework.activity.BaseFragmentActivity;
import com.xiaotian.framework.common.Mylog;
import com.xiaotian.frameworkxt.android.common.MyRunnable;
import com.xiaotian.frameworkxt.android.util.UtilEnvironment;

public class ActivityJSToJavaInterface extends BaseFragmentActivity {
	List<String> listData = new ArrayList<String>();
	WebView mWebView;

	public ActivityJSToJavaInterface() {
		for (int i = 0; i < 5; i++) {
			listData.add("我是 Java List 中的第" + (i + 1) + "个Item数据.");
		}
	}

	@Override
	@SuppressLint({ "SetJavaScriptEnabled", "JavascriptInterface" })
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_jsto_java_interface);
		mWebView = (WebView) findViewById(R.id.id_0);
		// 开启javascript设置
		mWebView.getSettings().setJavaScriptEnabled(true);
		// 把RIAExample的一个实例添加到js的全局对象window中
		// 这样就可以使用window.javatojs来调用它的方法
		mWebView.addJavascriptInterface(this, "InvokeTagName");
		mWebView.addJavascriptInterface(this, "javatojs"); // Js 调用本实例[This]接口Tag
		//
		mWebView.loadUrl("file:///android_asset/getphonenumber.html"); // 加载Html
	}

	@JavascriptInterface
	public String GetPhoneNumber() {
		// 调用JS显示号码/获取号码
		Mylog.info("调用JS显示号码/获取号码");
		String phone = UtilEnvironment.getSIMPhoneNumber(getApplicationContext());
		Mylog.info(phone);
		mWebView.post(new MyRunnable<String>(phone) {
			@Override
			public void run() {
				mWebView.loadUrl("javascript:ShowPhoneNumber(" + getInitParams(0) + ")");
			}
		});

		return "1234";
	}

	// JS->Java[通过TAG绑定调用]
	// [4.2 以上必须为方法声明为JS接口注解]
	@JavascriptInterface
	public void Callfunction() {
		Mylog.info("JS Call Function.");
		// 必须在WebView线程中调用LoadUrl方法
		mWebView.post(new Runnable() {
			@Override
			public void run() {
				// Java->JS[Java 调用加载的Html中的JS],java->js效率比js->java底
				mWebView.loadUrl("javascript:GetList()");
			}
		});
	}

	// JS->Java[通过TAG绑定调用]
	@JavascriptInterface
	public String GetObject(int index) {
		Mylog.info("GetList index=" + index);
		return listData.get(index);
	}

	// JS->Java[通过TAG绑定调用]
	@JavascriptInterface
	public int getSize() {
		return listData.size();
	}
}
