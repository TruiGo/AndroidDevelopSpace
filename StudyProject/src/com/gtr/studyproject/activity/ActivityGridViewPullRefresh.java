package com.gtr.studyproject.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.xiaotian.framework.activity.BaseActivity;
import com.xiaotian.framework.util.async.loadimage.ImageCache;
import com.xiaotian.framework.util.async.loadimage.ImageWorkerResizerFetcher;
import com.xiaotian.framework.view.ViewGridViewPullRefresh;
import com.xiaotian.framework.view.ViewLinearLayoutPullRefresh;

/**
 * @version 1.0.0
 * @author XiaoTian
 * @name ActivityGridViewPullRefresh
 * @description Grid View 拉动刷新,网络图片缓冲
 * @date 2014-9-29
 * @link gtrstudio@qq.com
 * @copyright Copyright © 2010-2014 小天天 Studio, All Rights Reserved.
 */
public class ActivityGridViewPullRefresh extends BaseActivity {
	List<String> images = new ArrayList<String>();
	int mImageThumbSize, mImageThumbSpacing;
	ImageWorkerResizerFetcher imageWorker;
	ViewGridViewPullRefresh gridView;
	ImageAdapter mAdapter;

	public ActivityGridViewPullRefresh() {
		images.add("http://c.hiphotos.baidu.com/image/pic/item/f2deb48f8c5494ee4a92ed232ef5e0fe99257e5c.jpg");
		images.add("http://f.hiphotos.baidu.com/image/pic/item/b999a9014c086e06582ddd7101087bf40ad1cb07.jpg");
		images.add("http://imgt9.bdstatic.com/it/u=2,931955011&fm=25&gp=0.jpg");
		images.add("http://h.hiphotos.baidu.com/image/pic/item/00e93901213fb80e24d849da34d12f2eb93894b5.jpg");
		images.add("http://e.hiphotos.baidu.com/image/pic/item/cf1b9d16fdfaaf515f1156b78e5494eef11f7acc.jpg");
		images.add("http://f.hiphotos.baidu.com/image/pic/item/86d6277f9e2f0708c5a73608eb24b899a901f21c.jpg");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//
		ImageCache.ImageCacheParams cacheParams = new ImageCache.ImageCacheParams();
		cacheParams.memoryCacheEnabled = true;
		cacheParams.memCacheSize = 10 * 1024;
		cacheParams.diskCacheEnabled = true;
		cacheParams.diskCacheSize = 80 * 1024;
		//
		imageWorker = new ImageWorkerResizerFetcher(getBaseContext(), Integer.MAX_VALUE);
		imageWorker.addImageCache(null, cacheParams);

		setContentView(R.layout.activity_gridview_pullrefresh);
		gridView = (ViewGridViewPullRefresh) findViewById(R.id.ViewGridViewPullRefresh);
		gridView.setDisableScrollingWhileRefreshing(false);
		mAdapter = new ImageAdapter(this);
		gridView.setAdapter(mAdapter);
		gridView.setOnRefreshListener(new ViewLinearLayoutPullRefresh.OnRefreshListener() {
			public void onRefresh() {
				executeAsyncTask(new AsyncTask<Void, Void, Void>() {
					@Override
					protected Void doInBackground(Void... params) {
						try {
							Thread.sleep(2000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						switch (gridView.getRefreshType()) {
						case 1:
							images.add("http://d.hiphotos.baidu.com/image/pic/item/d058ccbf6c81800ab2144202b33533fa838b47cd.jpg");
							images.add("http://f.hiphotos.baidu.com/image/pic/item/a71ea8d3fd1f41343a283576271f95cad1c85e9e.jpg");
							images.addAll(images);
							break;
						case 2:
							images.add("http://a.hiphotos.baidu.com/image/pic/item/8ad4b31c8701a18b6f270d529c2f07082938fedb.jpg");
							images.add("http://a.hiphotos.baidu.com/image/pic/item/f11f3a292df5e0fe0141420d5e6034a85edf7265.jpg");
							images.addAll(images);
							break;
						}
						return null;
					}

					@Override
					protected void onPostExecute(Void result) {
						mAdapter.notifyDataSetChanged();
						gridView.onRefreshComplete();
					}

				});
			}
		});
		mImageThumbSize = getResources().getDimensionPixelSize(R.dimen.dimen_100);
		mImageThumbSpacing = getResources().getDimensionPixelSize(R.dimen.dimen_1);
		gridView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				if (mAdapter.getNumColumns() == 0) {
					final int numColumns = (int) Math.floor(gridView.getWidth()
							/ (mImageThumbSize + mImageThumbSpacing));
					if (numColumns > 0) {
						final int columnWidth = (gridView.getWidth() / numColumns) - mImageThumbSpacing;
						mAdapter.setNumColumns(numColumns);
						mAdapter.setItemHeight(columnWidth);
					}
				}
			}
		});
	}

	class ImageAdapter extends BaseAdapter {
		private final Context mContext;
		private int mItemHeight = 0;
		private int mNumColumns = 0;
		private GridView.LayoutParams mImageViewLayoutParams;

		public ImageAdapter(Context context) {
			super();
			mContext = context;
			mImageViewLayoutParams = new GridView.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		}

		@Override
		public int getCount() {
			// Size + number of columns for top empty row
			return images.size();
		}

		@Override
		public Object getItem(int position) {
			return images.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public int getViewTypeCount() {
			// Two types of views, the normal ImageView and the top row of empty
			// views
			return 2;
		}

		@Override
		public int getItemViewType(int position) {
			return (position < mNumColumns) ? 1 : 0;
		}

		@Override
		public boolean hasStableIds() {
			return true;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup container) {
			// Now handle the main ImageView thumbnails
			ImageView imageView;
			if (convertView == null) {
				RelativeLayout relativeLayout = (RelativeLayout) LayoutInflater.from(getBaseContext()).inflate(
						R.layout.item_gridview_pullrefresh, container, false);
				imageView = (ImageView) relativeLayout.findViewById(R.id.id_0);
				imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
				imageView.setLayoutParams(mImageViewLayoutParams);
			} else { // Otherwise re-use the converted view
				imageView = (ImageView) convertView;
			}
			// Check the height matches our calculated column width
			if (imageView.getLayoutParams().height != mItemHeight) {
				imageView.setLayoutParams(mImageViewLayoutParams);
			}
			// Finally load the image asynchronously into the ImageView, this
			// also takes care of
			// setting a placeholder image while the background thread runs
			imageWorker.loadImage(getItem(position), imageView);
			return imageView;
		}

		/**
		 * Sets the item height. Useful for when we know the column width so the
		 * height can be set to match.
		 * 
		 * @param height
		 */
		public void setItemHeight(int height) {
			if (height == mItemHeight) {
				return;
			}
			mItemHeight = height;
			mImageViewLayoutParams = new GridView.LayoutParams(LayoutParams.MATCH_PARENT, mItemHeight);
			imageWorker.setImageSize(height);
			notifyDataSetChanged();
		}

		public void setNumColumns(int numColumns) {
			mNumColumns = numColumns;
		}

		public int getNumColumns() {
			return mNumColumns;
		}
	}
}
