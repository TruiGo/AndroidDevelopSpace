package com.gtr.studyproject.activity.v4test;

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
 * @name ActivityFragmentNavigation
 * @description Fragment Navigation V4 Test 导航
 * @date 2014-8-25
 * @link gtrstudio@qq.com
 * @copyright Copyright © 2010-2014 小天天 Studio, All Rights Reserved.
 */
public class ActivityFragmentNavigation extends ListActivity {
	// ListActivity 包含一个 ListView
	public static final String CATEGORY_ACTIVITY = "com.gtr.studyproject.activity.v4test.FRAGEMENT_ACTIVITY";
	public static final String EXTRA_PARAM_ACTIVITY_ROOTPATH = "com.gtr.studyproject.activity.v4test.PATH";
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
		// 传入的Path
		String activityRootPath = getIntent().getStringExtra(EXTRA_PARAM_ACTIVITY_ROOTPATH);
		//
		List<Map<String, Object>> simpleAdapterData = getSimpleAdapterData(activityRootPath); // ListData
		String[] simpleAdapterMappingDataKey = new String[] { "title" }; // View-Data-Map-Key
		int[] simpleAdapterMappingDataViewId = new int[] { android.R.id.text1 };// View-id
		//
		setListAdapter(new SimpleAdapter(this, simpleAdapterData, android.R.layout.simple_list_item_1,
				simpleAdapterMappingDataKey, simpleAdapterMappingDataViewId));
		getListView().setTextFilterEnabled(true);
	}

	protected List<Map<String, Object>> getSimpleAdapterData(String prefix) {
		List<Map<String, Object>> myData = new ArrayList<Map<String, Object>>();// Map-Data

		Intent mainIntent = new Intent(Intent.ACTION_MAIN, null); // Action
		mainIntent.addCategory(CATEGORY_ACTIVITY); // Category
		// Retrieve all activities that can be performed for the given intent.
		PackageManager pm = getPackageManager();
		List<ResolveInfo> list = pm.queryIntentActivities(mainIntent, 0);
		if (null == list) return myData;
		//
		String[] prefixPath;
		String prefixWithSlash = ""; // 斜杠前缀

		if (prefix == null) {
			prefixPath = null;
		} else {
			prefixPath = prefix.split("/");
			prefixWithSlash = prefix + "/";
		}

		Map<String, Boolean> entries = new HashMap<String, Boolean>();
		for (int i = 0; i < list.size(); i++) {
			ResolveInfo info = list.get(i);
			// Activity android:label -> Application android:label
			CharSequence labelSeq = info.loadLabel(pm); // android:label

			String label = labelSeq != null ? labelSeq.toString() : info.activityInfo.name;

			if (prefixWithSlash.length() == 0 || label.startsWith(prefixWithSlash)) {

				String[] labelPath = label.split("/");

				String nextLabel = prefixPath == null ? labelPath[0] : labelPath[prefixPath.length];

				if ((prefixPath != null ? prefixPath.length : 0) == labelPath.length - 1) {
					addItem(myData, nextLabel,
							createActivityIntent(info.activityInfo.applicationInfo.packageName, info.activityInfo.name));
				} else {
					if (entries.get(nextLabel) == null) {
						addItem(myData, nextLabel, browseIntent(prefix == null ? nextLabel : prefix + "/" + nextLabel));
						entries.put(nextLabel, true);
					}
				}
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

	protected Intent browseIntent(String path) {
		Intent result = new Intent();
		result.setClass(this, ActivityFragmentNavigation.class);
		result.putExtra(EXTRA_PARAM_ACTIVITY_ROOTPATH, path);
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
