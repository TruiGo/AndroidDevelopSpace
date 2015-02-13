package com.gtr.studyproject.activity;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.xiaotian.framework.activity.BaseFragmentActivity;
import com.xiaotian.framework.common.Mylog;

//PreferenceFragment 类似于PreferenceActivity
public class ActivityPreferencesFragmentScreen extends BaseFragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 加载Preference Fragment
		getFragmentManager().beginTransaction().add(android.R.id.content, new PreferenceFragmentScreen()).commit();
	}

	// SharedPreference data page
	class PreferenceFragmentScreen extends PreferenceFragment {
		// PreferenceFragment:保存属性值到SharedPreference中的管理器[自动保存,获取],要获取该属性通过SharePreference获取

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			// 放到xml文件夹目录下的资源,添加Preference页面配置资源
			addPreferencesFromResource(R.xml.activity_preference_fragment_screen);
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			setHasOptionsMenu(true);
			return super.onCreateView(inflater, container, savedInstanceState);
		}

		public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
			inflater.inflate(R.menu.menu_activity_fragment_manager, menu);
		}

		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			Mylog.info("onOptionsItemSelected");
			return true;
		}
	}
}
