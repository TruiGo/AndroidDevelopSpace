package com.gtr.studyproject.activity.v4test;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ActivityViewPagerFragment extends FragmentActivity {
	ViewPager viewPager;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	class PageAdapterFragment extends FragmentStatePagerAdapter {

		public PageAdapterFragment(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int arg0) {
			return null;
		}

		@Override
		public int getCount() {
			return 0;
		}
	}

	static class ImageFragment extends Fragment {
		Context context;

		private ImageFragment(Context context) {
			this.context = context;
		}

		public static ImageFragment instance(Context context) {
			final ImageFragment page = new ImageFragment(context);

			return page;
		}

		@Override
		public void onActivityCreated(@Nullable Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);
		}

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
		}

		@Override
		public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
				@Nullable Bundle savedInstanceState) {
			return super.onCreateView(inflater, container, savedInstanceState);
		}

	}
}
