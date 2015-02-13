package com.gtr.studyproject.activity;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xiaotian.framework.activity.BaseFragmentActivity;

public class ActivityActionBarNavigation extends BaseFragmentActivity {
	// Fragment Navigation

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_actionbar_navigation);
		// Navigation Mode ActionBar[SDK 11以上]
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS); // TAB Mode
		// Tab 1
		Tab tab = actionBar.newTab();
		tab.setText("First Tab");
		tab.setTabListener(new ChapterTabListener<FragmentA>(this, "fragmentA", FragmentA.class));
		actionBar.addTab(tab);
		// Tab 2
		tab = actionBar.newTab();
		tab.setText("Second Tab");
		tab.setTabListener(new ChapterTabListener<FragmentB>(this, "fragmentB", FragmentB.class));
		actionBar.addTab(tab);
	}

	class ChapterTabListener<T extends Fragment> implements TabListener {
		Fragment mFragment;
		Activity mActivity;
		Class<?> mClazz;
		String mTag;

		public ChapterTabListener(Activity activity, String tag, Class<T> clazz) {
			this.mActivity = activity;
			this.mTag = tag;
			this.mClazz = clazz;
		}

		// 初始化Fragment
		@Override
		@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			// 初始化并且添加Fragment到Activity
			if (mFragment == null) {
				mFragment = Fragment.instantiate(mActivity, mClazz.getName()); // 实例化一个Fragment
				ft.add(android.R.id.content, mFragment, mTag);
			} else {
				// If it exists, we simply attach it
				ft.attach(mFragment);
			}
		}

		@Override
		@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
		public void onTabUnselected(Tab tab, FragmentTransaction ft) {
			// in this method we detach the fragment because
			// it shouldn't be displayed
			if (mFragment != null) {
				// 不显示Fragment,Detach 加载的Fragment
				// Remove Fragment from activity
				ft.detach(mFragment);
			}
		}

		@Override
		public void onTabReselected(Tab tab, FragmentTransaction ft) {
			// This method is called when the tab is reselected
		}

	}

	public static class FragmentA extends Fragment {

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View view = inflater.inflate(R.layout.activity_fullscreen_xiaotian, container, false);
			return view;
		}
	}

	public static class FragmentB extends Fragment {

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View view = inflater.inflate(R.layout.activity_include_page, container, false);
			return view;
		}
	}
}
