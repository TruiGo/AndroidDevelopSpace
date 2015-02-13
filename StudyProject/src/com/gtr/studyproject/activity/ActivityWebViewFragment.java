package com.gtr.studyproject.activity;

import android.os.Bundle;
import android.webkit.WebViewFragment;

import com.xiaotian.framework.activity.BaseFragmentActivity;

public class ActivityWebViewFragment extends BaseFragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getFragmentManager().beginTransaction().add(android.R.id.content, new MyWebViewFragment()).commit();
	}

	// WebViewFragment
	class MyWebViewFragment extends WebViewFragment {
		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);
			getWebView().loadUrl("http://www.baidu.com");
		}
	}
}
