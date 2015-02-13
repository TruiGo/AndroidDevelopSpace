package com.gtr.studyproject.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher.ViewFactory;

import com.xiaotian.framework.activity.BaseFragmentActivity;
import com.xiaotian.frameworkxt.util.UtilFile;

/**
 * 
 * @version 1.0.0
 * @author mac
 * @name ActivityTextSwitcher
 * @description Switcher Group
 * @date 2014-10-10
 * @link gtrstudio@qq.com
 * @copyright Copyright © 2010-2014 小天天 Studio, All Rights Reserved.
 */
public class ActivityAnimationSwitcher extends BaseFragmentActivity {
	ListView listView;
	BaseAdapter adapter;
	TextSwitcher textSwitcher;
	ImageSwitcher imageSwitcher;
	List<Map<String, String>> listData;

	public ActivityAnimationSwitcher() {
		listData = new ArrayList<Map<String, String>>();
		Map<String, String> item = new HashMap<String, String>();
		item.put("name", "Liming");
		listData.add(item);
		item = new HashMap<String, String>();
		item.put("name", "ZhangSan");
		listData.add(item);
		item = new HashMap<String, String>();
		item.put("name", "LiSi");
		listData.add(item);
		item = new HashMap<String, String>();
		item.put("name", "XiaoMing");
		listData.add(item);
		item = new HashMap<String, String>();
		item.put("name", "TianTian");
		listData.add(item);
		item = new HashMap<String, String>();
		item.put("name", "QingQing");
		listData.add(item);
		item = new HashMap<String, String>();
		item.put("name", "ZhangSan");
		listData.add(item);
		item.put("name", "Liming");
		listData.add(item);
		item = new HashMap<String, String>();
		item.put("name", "ZhangSan");
		listData.add(item);
		item = new HashMap<String, String>();
		item.put("name", "LiSi");
		listData.add(item);
		item = new HashMap<String, String>();
		item.put("name", "XiaoMing");
		listData.add(item);
		item = new HashMap<String, String>();
		item.put("name", "TianTian");
		listData.add(item);
		item = new HashMap<String, String>();
		item.put("name", "QingQing");
		listData.add(item);
		item = new HashMap<String, String>();
		item.put("name", "ZhangSan");
		listData.add(item);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_text_switcher);
		textSwitcher = (TextSwitcher) findViewById(R.id.id_0);
		imageSwitcher = (ImageSwitcher) findViewById(R.id.id_1);
		Animation animationIn = AnimationUtils.loadAnimation(this,
				android.R.anim.fade_in);
		Animation animationOut = AnimationUtils.loadAnimation(this,
				android.R.anim.fade_out);
		// Animation Text Switcher
		textSwitcher.setFactory(new ViewFactory() {
			@Override
			public View makeView() {
				TextView textView = new TextView(ActivityAnimationSwitcher.this);
				textView.setGravity(Gravity.CENTER);
				return textView;
			}
		});
		textSwitcher.setInAnimation(animationIn);
		textSwitcher.setOutAnimation(animationOut);
		// Animation Image Switcher
		imageSwitcher.setFactory(new ViewFactory() {
			@Override
			public View makeView() {
				ImageView v = new ImageView(ActivityAnimationSwitcher.this);
				return v;
			}
		});
		imageSwitcher.setInAnimation(animationIn);
		imageSwitcher.setOutAnimation(animationOut);
		// Animation ListView
		listView = (ListView) findViewById(R.id.id_2);
		adapter = new SimpleAdapter(this, listData,
				android.R.layout.simple_list_item_1, new String[] { "name" },
				new int[] { android.R.id.text1 });
		listView.setAdapter(adapter);
		AnimationSet animationSet = new AnimationSet(true);
		Animation animationAlpha = new AlphaAnimation(0.0f, 1.0f);
		animationAlpha.setDuration(50);
		animationSet.addAnimation(animationAlpha);
		Animation animationTranslate = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
				0.0f, Animation.RELATIVE_TO_SELF, -1.0f,
				Animation.RELATIVE_TO_SELF, 0.0f);
		animationTranslate.setDuration(100);
		animationSet.addAnimation(animationTranslate);
		LayoutAnimationController animationController = new LayoutAnimationController(
				animationSet, 0.5f);// delay between animations
		listView.setLayoutAnimation(animationController);
		//
		executeAsyncTask(new AsyncTask<Void, Void, Void>() {
			int count = 0;

			@Override
			protected Void doInBackground(Void... params) {
				while (count++ < 10) {
					publishProgress();
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
					}
				}
				return null;
			}

			@Override
			protected void onProgressUpdate(Void... values) {
				textSwitcher.setText(UtilFile.getInstance().randomFileName(
						"temp", 30, String.valueOf(count)));
				switch (count % 3) {
				case 0:
					imageSwitcher.setImageResource(R.drawable.icon_marki);
					break;
				case 1:
					imageSwitcher.setImageResource(R.drawable.ic_launcher);
					break;
				case 2:
					imageSwitcher.setImageResource(R.drawable.icon_geo);
					break;
				}
				listView.startLayoutAnimation();
			}
		});
	}
}
