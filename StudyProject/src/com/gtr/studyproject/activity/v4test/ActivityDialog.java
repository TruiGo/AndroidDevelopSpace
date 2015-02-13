package com.gtr.studyproject.activity.v4test;

import android.R;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.xiaotian.framework.activity.BaseFragmentActivity;
import com.xiaotian.frameworkxt.android.view.FragmentDialog;

public class ActivityDialog extends BaseFragmentActivity {

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
		if (prev != null) {
			ft.remove(prev);
		}
		ft.addToBackStack(null);
		// Create and show the dialog.
		DialogFragment newFragment = FragmentDialog.newInstance(R.string.dialog_alert_title);
		newFragment.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
		newFragment.show(ft, "dialog");
	}
}
