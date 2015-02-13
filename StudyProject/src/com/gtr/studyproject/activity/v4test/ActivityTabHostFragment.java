package com.gtr.studyproject.activity.v4test;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gtr.studyproject.activity.R;

public class ActivityTabHostFragment extends FragmentActivity {
	private FragmentTabHost mTabHost;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_tabhost_fragment);
		mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

		mTabHost.addTab(mTabHost.newTabSpec("tab1").setIndicator("Tab1"), Tab1Fragment.class, null);
		mTabHost.addTab(mTabHost.newTabSpec("tab2").setIndicator("Tab2"), Tab2Fragment.class, null);
		mTabHost.addTab(mTabHost.newTabSpec("tab3").setIndicator("Tab3"), Tab3Fragment.class, null);

	}

	public static class Tab1Fragment extends Fragment {
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View V = inflater.inflate(R.layout.activity_fullscreen_xiaotian, container, false);

			return V;
		}
	}

	public static class Tab2Fragment extends Fragment {
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View V = inflater.inflate(R.layout.activity_bluetooth_send, container, false);

			return V;
		}
	}

	public static class Tab3Fragment extends Fragment {
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View V = inflater.inflate(R.layout.activity_main, container, false);

			return V;
		}
	}
}
