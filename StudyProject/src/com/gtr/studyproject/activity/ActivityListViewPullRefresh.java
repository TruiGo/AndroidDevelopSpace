package com.gtr.studyproject.activity;

import java.util.Arrays;
import java.util.LinkedList;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xiaotian.framework.activity.BaseActivity;
import com.xiaotian.framework.view.ViewLinearLayoutPullRefresh;
import com.xiaotian.framework.view.ViewListViewPullRefresh;

public class ActivityListViewPullRefresh extends BaseActivity {
	ViewListViewPullRefresh refreshListView;
	private LinkedList<String> mListItems;
	PullAdapter pullAdapter;
	private String[] mStrings = { "ListView Item 1", "ListView Item 2", "ListView Item 3", "ListView Item 4",
			"ListView Item 5" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_listview_pull_refresh);
		mListItems = new LinkedList<String>();
		mListItems.addAll(Arrays.asList(mStrings));

		refreshListView = (ViewListViewPullRefresh) findViewById(R.id.ViewListViewPullRefresh);
		refreshListView.setOnRefreshListener(new ViewLinearLayoutPullRefresh.OnRefreshListener() {
			public void onRefresh() {
				PullTask pullTask = new PullTask(refreshListView, refreshListView.getRefreshType(), pullAdapter,
						mListItems);
				pullTask.execute();
			}
		});
		pullAdapter = new PullAdapter(mListItems, getBaseContext());
		refreshListView.setAdapter(pullAdapter);
	}

	public static class PullTask extends AsyncTask<Void, Void, String> {
		private ViewListViewPullRefresh pullToRefreshListView; // 实现下拉刷新与上拉加载的ListView
		private LinkedList<String> linkedList;
		private BaseAdapter baseAdapter; // ListView适配器，用于提醒ListView数据已经更新
		private int pullState; // 记录判断，上拉与下拉动作

		public PullTask(ViewListViewPullRefresh pullToRefreshListView, int pullState, BaseAdapter baseAdapter,
				LinkedList<String> linkedList) {
			this.pullToRefreshListView = pullToRefreshListView;
			this.pullState = pullState;
			this.baseAdapter = baseAdapter;
			this.linkedList = linkedList;
		}

		@Override
		protected String doInBackground(Void... params) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {}
			return "StringTest";
		}

		@Override
		protected void onPostExecute(String result) {
			if (pullState == 1) {// name="pullDownFromTop" value="0x1" 下拉
				linkedList.addFirst("顶部数据" + Math.random() * 100);
				linkedList.addFirst("顶部数据2" + Math.random() * 100);
				linkedList.addFirst("顶部数据3" + Math.random() * 100);
				linkedList.addFirst("顶部数据4" + Math.random() * 100);
				linkedList.addFirst("顶部数据5" + Math.random() * 100);
				linkedList.addFirst("顶部数据6" + Math.random() * 100);
				linkedList.addFirst("顶部数据7" + Math.random() * 100);
				linkedList.addFirst("顶部数据8" + Math.random() * 100);
			}
			if (pullState == 2) {// name="pullUpFromBottom" value="0x2" 上拉
				linkedList.addLast("底部数据1" + Math.random() * 100);
				linkedList.addLast("底部数据2" + Math.random() * 100);
				linkedList.addLast("底部数据3" + Math.random() * 100);
				linkedList.addLast("底部数据4" + Math.random() * 100);
				linkedList.addLast("底部数据5" + Math.random() * 100);
				linkedList.addLast("底部数据6" + Math.random() * 100);
				linkedList.addLast("底部数据7" + Math.random() * 100);
				linkedList.addLast("底部数据8" + Math.random() * 100);
			}
			baseAdapter.notifyDataSetChanged();
			pullToRefreshListView.onRefreshComplete();
			super.onPostExecute(result);
		}
	}

	public static class PullAdapter extends BaseAdapter {
		private LinkedList<String> linkedList;
		private LayoutInflater mInflater;

		public PullAdapter(LinkedList<String> linkedList, Context context) {
			mInflater = LayoutInflater.from(context);
			this.linkedList = linkedList;
		}

		@Override
		public int getCount() {
			return linkedList.size();
		}

		@Override
		public Object getItem(int position) {
			return linkedList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.layout_main_listitem, null);
				holder.textView = (TextView) convertView.findViewById(R.id.textView);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			if (linkedList.size() > 0) {
				final String dataStr = linkedList.get(position);
				holder.textView.setText(dataStr);
			}
			return convertView;
		}

		private class ViewHolder {
			TextView textView; // 数据显示区域
		}
	}
}
