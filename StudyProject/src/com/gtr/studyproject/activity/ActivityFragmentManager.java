package com.gtr.studyproject.activity;

import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.xiaotian.framework.activity.BaseFragmentActivity;
import com.xiaotian.framework.common.Mylog;
import com.xiaotian.frameworkxt.android.util.UtilColor;
// Fragment Type:

// 1.ListFragment
// 2.DialogFragment
// 3.PreferenceFragment
// 4.WebViewFragment

public class ActivityFragmentManager extends BaseFragmentActivity {
	public static final String TAG_INNER_FRAGMENT = "TAG_INNER_FRAGMENT";
	boolean isMutilPanel;

	@Override
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 竖屏/横屏配置相应的Layout文件
		setContentView(R.layout.activity_fragment_manager);
		// 1.竖屏动态添加 2.横屏在XML配置
		isMutilPanel = findViewById(R.id.id_0) == null; // 竖屏配置id_0,横屏不配置
		if (isMutilPanel) {
			// 判断为横屏
			if (savedInstanceState != null) return; // 如果不是新建Activity返回不新建Fragment,为了避免多次创建Fragment所以判断保存状态
			// 动态添加Fragment,可以Remove
			InnerListFragment content = new InnerListFragment();
			FragmentManager fragmentManager = getSupportFragmentManager();
			FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
			fragmentTransaction.add(R.id.id_0, content, TAG_INNER_FRAGMENT);
			fragmentTransaction.commit();
		} else {
			// 竖屏
		}
		// 设置ActionBar 属性.
		ActionBar actionBar = getActionBar();
		actionBar.setBackgroundDrawable(new ColorDrawable(UtilColor.randomColor()));

	}

	// 创建选项菜单[有Action Bar 加到Action Bar]
	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Fragment ActionBar Item
		Mylog.info("创建选项菜单 onCreateOptionsMenu");
		MenuInflater inflater = new MenuInflater(getBaseContext());
		inflater.inflate(R.menu.menu_activity_fragment_manager, menu);
		// ActionBar Action View [已经绑定actionLayout显示Layout]
		MenuItem searchItem = menu.findItem(R.id.id_menu_search);
		searchItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
			@Override
			public boolean onMenuItemActionExpand(MenuItem item) {
				// 展开ActionView触发
				EditText editTextSearch = (EditText) item.getActionView();
				Mylog.info("搜索 MenuItem ActionView Hint :" + editTextSearch.getHint());
				return true; // true 展开绑定的ActionView
			}

			@Override
			public boolean onMenuItemActionCollapse(MenuItem item) {
				// 收起ActionView触发
				EditText et = (EditText) item.getActionView();
				Mylog.info("Input Search Key=" + et.getText().toString());
				return true;
			}
		});
		return true;
	}

	// 选项菜单
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.id_menu_3:
			// 返回主页
			Intent intent = new Intent(this, ActivityNavigation.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // 清掉当前栈顶的Activity[当前Activity]
			startActivity(intent);
			return true;
		}
		// 显示Dialog对话框
		InnerDialogFragment.newInstance().show(getSupportFragmentManager(), "TAG-Fragment");
		return true;
	}

	@Override
	public View onCreateView(String name, @NonNull Context context, @NonNull AttributeSet attrs) {
		return super.onCreateView(name, context, attrs);
	}

	/*********************************************** Inner Class ***********************************************/
	// 类别 ListFragment
	public static class InnerListFragment extends ListFragment {
		boolean isMutilPanel;
		String[] itemTitleArray = new String[] { "This is listview item 1", "This is listview item 2", "This is listview item 3",
				"This is listview item 4", "This is listview item 5", "This is listview item 6", "This is listview item 7",
				"This is listview item 8", "This is listview item 9", "This is listview item 10", "This is listview item 11",
				"This is listview item 12", "This is listview item 13", "This is listview item 14" };

		@Override
		public void onActivityCreated(@Nullable Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);
			isMutilPanel = getActivity().findViewById(R.id.id_0) == null;
			// Loader 处理 Support V4 Fragment ->
			// android.support.v4.app.LoaderManager.LoaderCallbacks
			getLoaderManager().initLoader(0, null, new LoaderManager.LoaderCallbacks<String[]>() {
				@Override
				public Loader<String[]> onCreateLoader(int loaderId, Bundle bundle) {
					// loaderId用于重用Loader加载器
					return new RSSCustomLoader(getActivity());
				}

				@Override
				public void onLoadFinished(Loader<String[]> arg0, String[] result) {
					// 加载完成,设置适配器
					Mylog.info("调用Loader方式 加载完成...");
					setListAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, result));
				}

				@Override
				public void onLoaderReset(Loader<String[]> arg0) {}

			}).forceLoad();
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			return super.onCreateView(inflater, container, savedInstanceState);
		}

		@Override
		public void onListItemClick(ListView l, View v, int position, long id) {
			if (isMutilPanel) {
				// 直接显示在右边的详情fragment
				Mylog.info("横屏点击行,直接刷新右边详情.");

			} else {
				// 创建一个Fragment页面并显示
				Mylog.info("竖屏点击行");
				SmallFragment sf = new SmallFragment();
				FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
				FragmentTransaction fragmentTransactioin = fragmentManager.beginTransaction();
				fragmentTransactioin.replace(R.id.id_1, sf);
				fragmentTransactioin.commit();
			}
		}

		/*********************************************** Inner Loader Class ***********************************************/
		// Loader 加载器,异步加载器,简单的直接继承AsyncTaskLoader异步加载
		static class RSSLoader extends AsyncTaskLoader<String[]> {
			public RSSLoader(Context context) {
				super(context);
			}

			@Override
			public String[] loadInBackground() {
				try {
					Thread.sleep(1500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				String[] items = new String[(int) (Math.random() * 500)];
				for (int i = 0; i < items.length; i++) {
					items[i] = "This is the list item number=" + i;
				}
				return items;
			}
		}

		// 自定义Observer/Observable模式的加载器
		static class RSSCustomLoader extends Loader<String[]> implements Observer {
			private Task mTask = null;
			private RSSCustomObserver mTimerObservable = null;

			public RSSCustomLoader(Context context) {
				super(context);
				//
				mTimerObservable = new RSSCustomObserver();
				mTimerObservable.start("http://www.test.com/example");
				mTimerObservable.addObserver(this); // Observer模式的加载器
			}

			@Override
			protected void onStartLoading() {
				if (takeContentChanged()) {
					forceLoad(); // 加载
				}
			}

			@Override
			protected void onForceLoad() {
				// 加载数据方法,调用加载器
				super.onForceLoad();
				onStopLoading();
				mTask = new Task();
				mTask.execute();
			}

			@Override
			protected void onStopLoading() {
				if (mTask != null) {
					boolean result = mTask.cancel(false);// 是否在运行时也打断停止
					Mylog.info("取消线程结果 Result=" + result);
					mTask = null;
				}
			}

			@Override
			protected void onReset() {
				mTimerObservable.stop();
			}

			@Override
			public void update(Observable observable, Object data) {
				// 如果当前加载器已经Start 则调用forcload加载数据
				forceLoad();
				onContentChanged();
			}

			@Override
			public void deliverResult(String[] data) {
				if (isReset()) {
					Mylog.info("加载器数据已经被垃圾回收器回收,不发布加载的结果.");
					return;
				}
				super.deliverResult(data);
			}

			/******************* Inner Class Custom Loader *******************/
			class Task extends AsyncTask<Void, Void, String[]> {
				@Override
				protected String[] doInBackground(Void... params) {
					try {
						Thread.sleep(1500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					String[] items = new String[(int) (Math.random() * 500)];
					for (int i = 0; i < items.length; i++) {
						items[i] = "This is the list item number=" + i;
					}
					return items;
				}

				@Override
				protected void onPostExecute(String[] result) {
					// deliverResult 必须要在UI线程发布
					RSSCustomLoader.this.deliverResult(result); // 发布结果到加载器
				}

			}

			// 可观察对象
			class RSSCustomObserver extends Observable {
				String mUrl;
				Timer mTimer;

				public RSSCustomObserver() {
					mTimer = new Timer();
				}

				public void start(String url) {
					mUrl = url;
					// 重复执行 TimerTask
					mTimer.schedule(new InnerTimer(), 10000, 20000); // 持续时间,时间间隔
				}

				public void stop() {
					mTimer.cancel();
				}

				class InnerTimer extends TimerTask {
					@Override
					public void run() {
						// 访问,加载网络数据
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	}

	// 详情Fragment
	public static class SmallFragment extends Fragment {

		@Override
		public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
			return inflater.inflate(R.layout.layout_framelayout, container, false);
		}
	}

	public static class InnerDialogFragment extends DialogFragment {
		public static InnerDialogFragment newInstance() {
			return new InnerDialogFragment();
		}

		@Override
		@NonNull
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			return new AlertDialog.Builder(getActivity()).setTitle("Dialog").setPositiveButton("Positive", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {

				}
			}).setNegativeButton("Negative", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dismiss();
				}
			}).create();
		}
	}
}
