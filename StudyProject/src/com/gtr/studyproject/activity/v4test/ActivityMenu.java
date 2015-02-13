package com.gtr.studyproject.activity.v4test;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;

import com.gtr.studyproject.activity.R;
import com.myself.common.Mylog;
import com.xiaotian.framework.activity.BaseFragmentActivity;
import com.xiaotian.frameworkxt.android.view.FragmentMenu;

public class ActivityMenu extends BaseFragmentActivity {
	Fragment mFragment1;
	DrowMenuWindow popWindow;

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.activity_menu);
		// Make sure the two menu fragments are created.
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		mFragment1 = fm.findFragmentByTag("f1");
		if (mFragment1 == null) {
			mFragment1 = new MenuFragment();
			ft.add(mFragment1, "f1");
		}
		ft.commit();
		Button contextButton = (Button) findViewById(R.id.id_0);
		registerForContextMenu(contextButton);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add(Menu.NONE, R.id.id_00, Menu.NONE, "Menu A");
		menu.add(Menu.NONE, R.id.id_01, Menu.NONE, "Menu B");
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		Mylog.info(featureId + "," + item.getTitle());
		if (item.getTitle().equals("Menu 1a")) {
			Mylog.info("commit Menu 1a");
			popWindow = new DrowMenuWindow(getBaseContext());
			popWindow.showAsDropDown(findViewById(R.id.id_0));
		}
		return super.onMenuItemSelected(featureId, item);
	}

	class MenuFragment extends FragmentMenu {
		@Override
		public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
			MenuItem item;
			item = menu.add("Menu 1a");
			MenuItemCompat.setShowAsAction(item, MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
			item = menu.add("Menu 1b");
			MenuItemCompat.setShowAsAction(item, MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
			item = menu.add("Menu 1c");
			MenuItemCompat.setShowAsAction(item, MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
			item = menu.add("Menu 1d");
			MenuItemCompat.setShowAsAction(item, MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
		}
	}

	class DrowMenuWindow extends PopupWindow {

		public DrowMenuWindow(Context context) {
			super(context);
			this.setContentView(LayoutInflater.from(context).inflate(R.layout.dialog_confirm_xiaotian, null));
		}
	}
}
