package com.gtr.studyproject.activity.train;

import java.io.ByteArrayOutputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewStub;
import android.widget.EditText;
import android.widget.ImageView;

import com.gtr.studyproject.activity.R;
import com.xiaotian.framework.activity.BaseActivity;
import com.xiaotian.frameworkxt.android.common.Mylog;
import com.xiaotian.frameworkxt.net.HttpNetworkException;

/**
 * @version 1.0.0
 * @author mac
 * @name ActivityTrain
 * @description 自动刷火车票
 * @date Dec 19, 2014
 * @link gtrstudio@qq.com
 * @copyright Copyright © 2010-2014 小天天 Studio, All Rights Reserved.
 */
public class ActivityTrain extends BaseActivity {
	ViewStub mViewStubLogin;
	RequestTrain mRequestTrain;
	ImageView ivLoginCode;
	EditText etLoginAccount;
	EditText etLoginPassword;
	EditText etLoginCode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initializingView();
		initializingData();
	}

	@Override
	protected void initializingView() {
		setContentView(R.layout.activity_train);
		mViewStubLogin = (ViewStub) findViewById(R.id.ViewStubLogin);
		View viewLogin = mViewStubLogin.inflate();
		//
		etLoginAccount = (EditText) viewLogin.findViewById(R.id.id_0);
		etLoginPassword = (EditText) viewLogin.findViewById(R.id.id_1);
		etLoginCode = (EditText) viewLogin.findViewById(R.id.id_2);
		ivLoginCode = (ImageView) viewLogin.findViewById(R.id.id_3);
		//
	}

	@Override
	protected void initializingData() {
		onClickRefleshPassCodeLogin(null);
	}

	// 刷新校验码
	public void onClickRefleshPassCodeLogin(View view) {
		ivLoginCode.setImageBitmap(null);
		executeAsyncTask(new AsyncTask<Void, Void, Bitmap>() {
			@Override
			protected Bitmap doInBackground(Void... params) {
				try {
					ByteArrayOutputStream baos = getRequestTrain().getPassCodeNew("login", "sjrand");
					if (baos == null) return null;
					return BitmapFactory.decodeByteArray(baos.toByteArray(), 0, baos.size());
				} catch (HttpNetworkException e) {
					e.printStackTrace();
				}
				return null;
			}

			@Override
			protected void onPostExecute(Bitmap result) {
				if (result != null) {
					LayoutParams params = ivLoginCode.getLayoutParams();
					float height = ivLoginCode.getMeasuredHeight();
					int width = (int) (height / result.getHeight() * result.getWidth());
					params.width = width;
					ivLoginCode.setImageBitmap(result);
				}
			}
		});
	}

	// 登录
	public void onClickLogin(View view) {
		executeAsyncTask(new NetWorkAsyncTask() {
			@Override
			protected HttpResponse doInBackground(String... params) {
				String account = etLoginAccount.getText().toString();
				String pass = etLoginPassword.getText().toString();
				String code = etLoginCode.getText().toString().trim();
				try {
					getRequestTrain().checkPassCode("sjrand", code);
					return getRequestTrain().login(account, pass, code);
				} catch (HttpNetworkException e) {
					e.printStackTrace();
				}
				return null;
			}

			@Override
			protected void onPostExecute(HttpResponse result) {
				dismissLoadingDialog();
				if (result != null) {
					Mylog.infoClassField(result);
				}
			}
		});
	}

	RequestTrain getRequestTrain() {
		if (mRequestTrain != null) return mRequestTrain;
		mRequestTrain = new RequestTrain();
		while (true) {
			try {
				mRequestTrain.initSessionParams();
				return mRequestTrain;
			} catch (HttpNetworkException e) {
				e.printStackTrace();
			}
		}
	}

	/********************************* Inner Class *********************************/

	public abstract class MyAsyncTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {
		@Override
		protected void onPreExecute() {
			showLoadingDialog(R.string.loading_data);
		}

		@Override
		protected void onPostExecute(Result result) {
			dismissLoadingDialog();
		}
	}

	public abstract class NetWorkAsyncTask extends AsyncTask<String, Void, HttpResponse> {
		@Override
		protected void onPreExecute() {
			showLoadingDialog(R.string.loading_data);
		}

		@Override
		protected void onPostExecute(HttpResponse result) {
			dismissLoadingDialog();
		}
	}
}