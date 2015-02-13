package com.gtr.studyproject.activity.v4test;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.gtr.studyproject.activity.R;
import com.xiaotian.framework.activity.BaseFragmentActivity;

public class ActivitySwipeRefreshLayout extends BaseFragmentActivity {
	SwipeRefreshLayout swipeRefreshLayout;
	ListView listView;
	List<String> data = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		data.add("This is Test 1");
		data.add("This is Test 2");
		data.add("This is Test 3");
		data.add("This is Test 4");
		data.add("This is Test 5");
		data.add("This is Test 6");
		data.add("This is Test 7");
		data.add("This is Test 8");
		data.add("This is Test 9");
		data.add("This is Test 0");
		data.add("This is Test 1");
		data.add("This is Test 2");
		setContentView(R.layout.activity_swipe_refresh_layout);
		swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.id_0);
		swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
				android.R.color.holo_orange_light, android.R.color.holo_red_light);
		swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						// swipeLayout.setRefreshing(false);
					}
				}, 5000);
			}
		});
		listView = (ListView) findViewById(R.id.id_1);
		listView.setAdapter(new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1,
				android.R.id.text1, data));
	}
}
