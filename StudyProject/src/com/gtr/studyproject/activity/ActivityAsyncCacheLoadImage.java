package com.gtr.studyproject.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

/**
 * @version 1.0.0
 * @author XiaoTian
 * @name AsyncCacheLoadImage
 * @description 异步缓冲图片加载
 * @date 2014-1-13
 * @link gtrstudio@qq.com
 * @copyright Copyright © 2010-2014 小天天 Studio, All Rights Reserved.
 */
public class ActivityAsyncCacheLoadImage extends Activity {
	AsyncCacheLoader imageLoader;
	List<String> listUri;

	public ActivityAsyncCacheLoadImage() {
		listUri = new ArrayList<String>();
		imageLoader = new AsyncCacheLoader(this);
		listUri.add("http://t3.gstatic.com/images?q=tbn:ANd9GcTf9wl5cySVwvi09TjfqDM_VVUFXMcyk9ypsLJY2uIbwebG8CrFnA");
		listUri.add("http://t2.gstatic.com/images?q=tbn:ANd9GcSGoyOBnM3GTJvgouUgXUdchhTk2J-_EWlUk_QcrDFQ0P2SWjdG");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		GridView gridView = new GridView(this);
		gridView.setAdapter(new BaseAdapter() {

			@Override
			public long getItemId(int position) {
				return position;
			}

			@Override
			public Object getItem(int position) {
				return position;
			}

			@Override
			public int getCount() {
				return listUri.size();
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				if (convertView == null) {
					ImageView iv = new ImageView(ActivityAsyncCacheLoadImage.this);
					convertView = iv;
				}
				imageLoader.asyncLoadImage(listUri.get(position), (ImageView) convertView);
				return convertView;
			}
		});
	}
}

class AsyncCacheLoader {
	// 1.内存缓存
	// 2.本地文件缓存
	// 3.网络文件资源
	CacheFile cacheFile;
	CacheMemory cacheMemory;
	
	// 1.异步线程加载
	// 2.线程池控制线程加载
	public AsyncCacheLoader(Context context) {

	}

	public void asyncLoadImage(String uri, ImageView imageView) {

	}

	class CacheMemory {

	}

	class CacheFile {

	}
}
