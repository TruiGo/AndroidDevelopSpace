package com.gtr.studyproject.activity;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ListActivity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

/**
 * @version 1.0.0
 * @author XiaoTian
 * @name ActivityNavigation
 * @description Activity Navigation
 * @date 2014-9-28
 * @link gtrstudio@qq.com
 * @copyright Copyright © 2010-2014 小天天 Studio, All Rights Reserved.
 */
public class ActivityNavigation extends ListActivity {
	// 只包含单层Activity,Activity配置以下Category和Action Main
	public static final String CATEGORY_ACTIVITY = "com.gtr.studyproject.activity.UIActivity";

	// Sort Comparator
	private final static Comparator<Map<String, Object>> sDisplayNameComparator = new Comparator<Map<String, Object>>() {
		private final Collator collator = Collator.getInstance();

		public int compare(Map<String, Object> map1, Map<String, Object> map2) {
			return collator.compare(map1.get("title"), map2.get("title"));
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//
		List<Map<String, Object>> simpleAdapterData = getSimpleAdapterData(); // ListData
		String[] simpleAdapterMappingDataKey = new String[] { "title" }; // View-Data-Map-Key
		int[] simpleAdapterMappingDataViewId = new int[] { android.R.id.text1 };// View-id
		//
		setListAdapter(new SimpleAdapter(this, simpleAdapterData, android.R.layout.simple_list_item_1,
				simpleAdapterMappingDataKey, simpleAdapterMappingDataViewId));
		getListView().setTextFilterEnabled(true);
	}

	protected List<Map<String, Object>> getSimpleAdapterData() {
		List<Map<String, Object>> myData = new ArrayList<Map<String, Object>>();// Map-Data

		Intent mainIntent = new Intent(Intent.ACTION_MAIN, null); // Action
		mainIntent.addCategory(CATEGORY_ACTIVITY); // Category
		// Retrieve all activities that can be performed for the given intent.
		PackageManager pm = getPackageManager();
		List<ResolveInfo> list = pm.queryIntentActivities(mainIntent, 0);
		if (null == list) return myData;
		for (int i = 0; i < list.size(); i++) {
			ResolveInfo info = list.get(i);
			// Activity android:label -> Application android:label
			CharSequence labelSeq = info.loadLabel(pm); // 配置了android:label
			if (labelSeq == null) {
				String label = info.activityInfo.name;
				String[] labelPath = label.split("/");
				String nextLabel = labelPath[labelPath.length - 1];
				addItem(myData, nextLabel,
						createActivityIntent(info.activityInfo.applicationInfo.packageName, info.activityInfo.name));
			} else {
				addItem(myData, String.valueOf(labelSeq),
						createActivityIntent(info.activityInfo.applicationInfo.packageName, info.activityInfo.name));
			}
		}
		Collections.sort(myData, sDisplayNameComparator);
		return myData;
	}

	protected Intent createActivityIntent(String pkg, String componentName) {
		Intent result = new Intent();
		result.setClassName(pkg, componentName);
		return result;
	}

	protected void addItem(List<Map<String, Object>> data, String name, Intent intent) {
		Map<String, Object> temp = new HashMap<String, Object>();
		temp.put("title", name);
		temp.put("intent", intent);
		data.add(temp);
	}

	@Override
	@SuppressWarnings("unchecked")
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Map<String, Object> map = (Map<String, Object>) l.getItemAtPosition(position);

		Intent intent = (Intent) map.get("intent");
		startActivity(intent);
	}
}