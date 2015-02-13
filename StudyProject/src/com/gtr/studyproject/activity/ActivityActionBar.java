package com.gtr.studyproject.activity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.ActionProvider;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MenuItem.OnActionExpandListener;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.SubMenu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ShareActionProvider;
import android.widget.Toast;

import com.xiaotian.framework.activity.BaseFragmentActivity;

public class ActivityActionBar extends BaseFragmentActivity {
	ShareActionProvider mShareActionProvider; // 系统分享菜单 Action Provider

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 隐藏 Action Bar
		// android:theme="@android:style/Theme.Holo.NoActionBar"
		// Spit Action Bar[API 14]当不够空间时分离Action Bar到底部
		// android:uiOptions="splitActionBarWhenNarrow"
	}

	// Action Bar :: TAG SDK 11 以上
	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// 加载 Action Bar
		MenuInflater inflater = new MenuInflater(this);
		inflater.inflate(R.menu.menu_activity_action_bar, menu);
		// 处理系统的ShareActionProvider[SDK14以上才支持分享功能]
		MenuItem item = menu.findItem(R.id.id_5);
		mShareActionProvider = (ShareActionProvider) item.getActionProvider();
		mShareActionProvider.setShareHistoryFileName(ShareActionProvider.DEFAULT_SHARE_HISTORY_FILE_NAME);
		// 设置分享打开的Intent
		Intent shareIntent = new Intent(Intent.ACTION_SEND);
		shareIntent.setType("text/plain"); // 分享类型 Text
		shareIntent.putExtra(Intent.EXTRA_TEXT, "www.somesite.com"); // 分享的Text内容
		// 设置打开的分享Dialog
		mShareActionProvider.setShareIntent(shareIntent);
		// 展开ActionLayout触发事件
		MenuItem itemCollapse = menu.findItem(R.id.id_6);
		itemCollapse.setOnActionExpandListener(new OnActionExpandListener() {
			@Override
			public boolean onMenuItemActionExpand(MenuItem item) {
				Toast.makeText(ActivityActionBar.this, item.getTitle() + " button is expanded", Toast.LENGTH_LONG).show();
				return true;
			}

			@Override
			public boolean onMenuItemActionCollapse(MenuItem item) {
				Toast.makeText(ActivityActionBar.this, item.getTitle() + " button is collapsed", Toast.LENGTH_LONG).show();
				return true;
			}
		});
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// 点击Action Bar
		return super.onOptionsItemSelected(item);
	}

	// 自定义Action[必须SDK 14 以上]
	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	public static class IconActionProvider extends ActionProvider {
		Context mContext;

		public IconActionProvider(Context context) {
			super(context);
			mContext = context;
		}

		@Override
		public View onCreateActionView() {
			// 创建Action View调用
			LayoutInflater inflater = LayoutInflater.from(mContext);
			View view = inflater.inflate(R.layout.action_provider_activity_actionbar_ico, null);
			// 注册点击侦听[Action Provider 默认的点击侦听被取消]
			ImageButton button = (ImageButton) view.findViewById(R.id.id_0);
			button.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Toast.makeText(mContext, "点击ICO Action", Toast.LENGTH_SHORT).show();
				}
			});
			// 返回Action View
			return view;
		}

		@Override
		public boolean onPerformDefaultAction() {
			// 执行默认Action,菜单点击后执行
			Toast.makeText(mContext, "Action Provider Click", Toast.LENGTH_SHORT).show();
			return true;
		}
	}

	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	public static class SubMenuActionProvider extends ActionProvider implements OnMenuItemClickListener {
		Context mContext;

		public SubMenuActionProvider(Context context) {
			super(context);
			mContext = context;
		}

		@Override
		public View onCreateActionView() {
			return null;
		}

		// 返回True,声明有子级菜单
		@Override
		public boolean hasSubMenu() {
			// we implemented it as returning true because we have menu
			return true;
		}

		@Override
		public void onPrepareSubMenu(SubMenu subMenu) {
			// In order to add submenus, we should override this method
			// we dynamically created submenus
			subMenu.clear();
			subMenu.add("SubItem1").setOnMenuItemClickListener(this);
			subMenu.add("SubItem2").setOnMenuItemClickListener(this);
		}

		@Override
		public boolean onMenuItemClick(MenuItem item) {
			Toast.makeText(mContext, "Sub Item click", Toast.LENGTH_LONG).show();
			return false;
		}
	}
}
