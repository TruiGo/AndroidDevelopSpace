package com.gtr.studyproject.common;

import android.content.Context;
import android.content.DialogInterface;
import android.preference.DialogPreference;
import android.util.AttributeSet;

/**
 * 
 * @version 1.0.0
 * @author mac
 * @name DialogPreferenceEmail
 * @description Preference Email Dialog
 * @date 2014-10-9
 * @link gtrstudio@qq.com
 * @copyright Copyright © 2010-2014 小天天 Studio, All Rights Reserved.
 */
public class DialogPreferenceAbout extends DialogPreference {

	public DialogPreferenceAbout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		super.onClick(dialog, which);
		if (DialogInterface.BUTTON_POSITIVE == which) {

		}
	}
}
