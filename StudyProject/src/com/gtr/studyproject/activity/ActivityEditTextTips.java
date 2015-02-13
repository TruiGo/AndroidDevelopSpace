package com.gtr.studyproject.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.xiaotian.framework.activity.BaseFragmentActivity;

/**
 * 
 * @version 1.0.0
 * @author mac
 * @name ActivityEditTextTips
 * @description View 使用技巧
 * @date 2014-10-10
 * @link gtrstudio@qq.com
 * @copyright Copyright © 2010-2014 小天天 Studio, All Rights Reserved.
 */
public class ActivityEditTextTips extends BaseFragmentActivity {
	Button buttonEdit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edittext_tips);
		buttonEdit = (Button) findViewById(R.id.id_0);

	}

	public void onClickSelectDate(View view) {
		inputDate("Chose Date", null);
	}

}
