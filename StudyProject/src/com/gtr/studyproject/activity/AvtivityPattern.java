package com.gtr.studyproject.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;

import com.xiaotian.framework.activity.BaseActivity;
import com.xiaotian.framework.common.Mylog;
import com.xiaotian.framework.widget.pattern.Point;
import com.xiaotian.framework.widget.pattern.ViewLockPattern;
import com.xiaotian.framework.widget.pattern.ViewLockPattern.PatternListener;

/**
 * @version 1.0.0
 * @author XiaoTian
 * @name AvtivityPattern
 * @description 图形密码匹配插件
 * @date 2014-9-29
 * @link gtrstudio@qq.com
 * @copyright Copyright © 2010-2014 小天天 Studio, All Rights Reserved.
 */
public class AvtivityPattern extends BaseActivity {
	ViewLockPattern patternView;
	List<Point> patternPoint = new ArrayList<Point>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pattern);
		patternView = (ViewLockPattern) findViewById(R.id.ViewLockPattern);
		patternView.setPracticeMode(true);
		patternView.setGridLength(3);
		patternView.invalidate();
		patternView.setPatternListener(new PatternListener() {
			@Override
			public void testPracticeStart() {
				Mylog.info("Start Pattern!");
			}

			@Override
			public boolean testPatternPractice(List<Point> mCurrentPattern) {
				return mCurrentPattern.equals(patternPoint);
			}
		});
		patternPoint.add(new Point(0, 1));
		patternPoint.add(new Point(1, 2));
		patternPoint.add(new Point(2, 1));
		patternPoint.add(new Point(1, 0));
		patternPoint.add(new Point(1, 1));
		//
		patternView.setHighlightMode(new ViewLockPattern.RainbowHighlight(), true);
		patternView.setPracticeMode(false);
		patternView.setPattern(patternPoint);
		patternView.tactileExecute(500);
		patternView.setPatternFinish(true);

		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				if (!patternView.getPracticeMode()) {
					patternView.setPatternFinish(false);
					patternView.setPracticeMode(true);
					patternView.resetPattern();
					patternView.invalidate();
				}
			}
		}, 3000);
	}
}
