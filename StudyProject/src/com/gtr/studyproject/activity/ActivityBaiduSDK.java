package com.gtr.studyproject.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.navi.BaiduMapAppNotSupportNaviException;
import com.baidu.mapapi.navi.BaiduMapNavigation;
import com.baidu.mapapi.navi.NaviPara;
import com.xiaotian.framework.activity.BaseFragmentActivity;
import com.xiaotian.framework.common.Mylog;
import com.xiaotian.frameworkxt.android.view.FragmentMenu;

/**
 * 
 * @version 1.0.0
 * @author mac
 * @name ActivityBaiduSDK
 * @description 百度SDK使用,加入下载指定功能的Lib包
 * @date 2014-10-8
 * @link gtrstudio@qq.com
 * @copyright Copyright © 2010-2014 小天天 Studio, All Rights Reserved.
 */
public class ActivityBaiduSDK extends BaseFragmentActivity {
	String MY_PRO_NAME = "com.gtr.studyproject";
	// Baidu Map Key
	public static final String BAIDU_SDK_AK = "8h4P1BDpWmv4DvYVXHycEqkN";
	//
	private MapView mMapView;
	private BaiduMap mBaiduMap;
	private List<LatLng> mListMapOverMaker = new ArrayList<LatLng>();
	BitmapDescriptor bdGround; // 范围覆盖Bimap
	BitmapDescriptor bd; // 点标识Bimap

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Baidu SDK 初始化
		SDKInitializer.initialize(getApplicationContext());// 声明为AppContext上下文
		// 必须SDK初始化
		bd = BitmapDescriptorFactory.fromResource(R.drawable.icon_gcoding);
		bdGround = BitmapDescriptorFactory.fromResource(R.drawable.ground_overlay);
		//
		setContentView(R.layout.activity_baidu_sdk);
		mMapView = (MapView) findViewById(R.id.id_0);
		mBaiduMap = mMapView.getMap();
		// BaiDu Map 配置
		mBaiduMap.setTrafficEnabled(true);
		// 添加 Fragment 组件
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		Fragment mFragment1 = fm.findFragmentByTag("menuFragment_1");
		if (mFragment1 == null) {
			mFragment1 = new MenuFragment();
			ft.add(mFragment1, "menuFragment_1");
		}
		ft.commit();
		try {
			// 获取Application Mete-data 配置参数
			PackageManager pm = getBaseContext().getPackageManager();
			ApplicationInfo ai;
			ai = pm.getApplicationInfo(getBaseContext().getPackageName(), PackageManager.GET_META_DATA);
			if (ai != null && ai.metaData != null) {
				Bundle bundle = ai.metaData; // 获取Meta配置的数据
				String ak = bundle.getString("com.baidu.lbsapi.API_KEY");
				Mylog.info("get application mata-data ak=" + ak);
			}
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		Mylog.info(featureId + "," + item.getTitle());
		if (item.getTitle().equals("定位当前")) {
			final LocationClient locationClient = new LocationClient(getBaseContext());
			LocationClientOption lco = new LocationClientOption();
			lco.SetIgnoreCacheException(true);
			lco.setScanSpan(5 * 1000);
			lco.setTimeOut(50 * 1000);
			lco.setProdName(MY_PRO_NAME);
			lco.setOpenGps(true);
			lco.setCoorType("bd09ll");// 返回百度定位纬度,默认gcj02:国测经纬度,bd09:墨卡托坐标系,bd09ll:百度纬度坐标
			// Hight_Accuracy高精度、Battery_Saving低功耗、Device_Sensors仅设备(GPS)
			lco.setLocationMode(LocationMode.Hight_Accuracy);// 定位模式
			locationClient.setLocOption(lco);
			lco.setNeedDeviceDirect(true);// 包含手机机头方向
			lco.setIsNeedAddress(true);// 加载地址信息
			locationClient.registerLocationListener(new BDLocationListener() {
				@Override
				public void onReceiveLocation(BDLocation location) {
					if (location == null) return;
					StringBuffer sb = new StringBuffer();
					sb.append("time:");
					sb.append(location.getTime());
					sb.append("\nerror code : ");
					sb.append(location.getLocType());
					sb.append("\nlatitude : ");
					sb.append(location.getLatitude());
					sb.append("\nlontitude : ");
					sb.append(location.getLongitude());
					sb.append("\nradius : ");
					sb.append(location.getRadius());
					if (location.getLocType() == BDLocation.TypeGpsLocation) {
						sb.append("\nspeed : ");
						sb.append(location.getSpeed());
						sb.append("\nsatellite : ");
						sb.append(location.getSatelliteNumber());
					} else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
						sb.append("\naddr : ");
						sb.append(location.getAddrStr());
					}
					Mylog.info(sb.toString());
					locationClient.stop();
					// 获取定位成功
					mBaiduMap.setMyLocationEnabled(true); // 显示我的位置图层
					// 构造定位数据
					MyLocationData.Builder builder = new MyLocationData.Builder();
					builder.accuracy(location.getRadius());
					// 此处设置开发者获取到的方向信息，顺时针0-360
					builder.direction(100);
					builder.latitude(location.getLatitude());
					builder.longitude(location.getLongitude());
					MyLocationData locData = builder.build();
					// 设置定位数据
					mBaiduMap.setMyLocationData(locData);
					// // 设置定位图层的配置（定位模式，是否允许方向信息，用户自定义定位图标）
					MyLocationConfiguration config = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL, true, null);
					mBaiduMap.setMyLocationConfigeration(config);
					LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
					MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(14.0f); // 放大14.0倍,1公里
					mBaiduMap.setMapStatus(msu);
					//
					MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
					mBaiduMap.animateMapStatus(u); // 移动地图到定位点
				}
			}); // 注册侦听
			if (!locationClient.isStarted()) {
				locationClient.start();
				locationClient.requestLocation();
			} else {
				locationClient.requestLocation();
			}
		} else if ("显示标识覆盖物".equals(item.getTitle())) {
			showOverlayMarker(0);
		} else if ("随机改变覆盖物".equals(item.getTitle())) {
			resetOverlayMarker();
		} else if ("清空覆盖物".equals(item.getTitle())) {
			clearOverlayMarker();
		} else if ("调用百度导航: 天安门-百度大夏".equals(item.getTitle())) {
			starBaiduMapNavi();
		} else if ("调用网页导航: 天安门-百度大夏".equals(item.getTitle())) {
			starWebNavi();
		}
		return super.onMenuItemSelected(featureId, item);
	}

	private void showOverlayMarker(double rang) {
		// 标识位置
		LatLng llA = new LatLng(39.963175 + rang, 116.400244 + rang);
		LatLng llB = new LatLng(39.942821 + rang, 116.369199 + rang);
		LatLng llC = new LatLng(39.939723 + rang, 116.425541 + rang);
		LatLng llD = new LatLng(39.906965 + rang, 116.401394 + rang);
		// 覆盖物 A
		OverlayOptions ooA = new MarkerOptions().position(llA).icon(bd).zIndex(9).draggable(true);
		mBaiduMap.addOverlay(ooA);
		// 覆盖物iB
		OverlayOptions ooB = new MarkerOptions().position(llB).icon(bd).zIndex(9).draggable(true);
		mBaiduMap.addOverlay(ooB);
		OverlayOptions ooC = new MarkerOptions().position(llC).icon(bd).zIndex(9).draggable(true);
		mBaiduMap.addOverlay(ooC);
		OverlayOptions ooD = new MarkerOptions().position(llD).icon(bd).zIndex(9).draggable(true);
		mBaiduMap.addOverlay(ooD);
		// 获取加入的Marker的中心,移动地图到中心
		LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
		boundsBuilder.include(llA);
		boundsBuilder.include(llB);
		boundsBuilder.include(llC);
		boundsBuilder.include(llD);
		//
		MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(boundsBuilder.build().getCenter());
		mBaiduMap.animateMapStatus(msu);
	}

	public void clearOverlayMarker() {
		mBaiduMap.clear();
	}

	public void resetOverlayMarker() {
		mBaiduMap.clear();
		showOverlayMarker(Math.random() * 10);
	}

	public void starBaiduMapNavi() {
		mBaiduMap.clear();
		LatLng pt1 = new LatLng(39.915291, 116.403857);// 天安门
		LatLng pt2 = new LatLng(40.056858, 116.308194);// 百度大夏
		NaviPara para = new NaviPara();
		para.startPoint = pt1;
		para.startName = "天安门";
		para.endPoint = pt2;
		para.endName = "百度大夏";
		//
		try {
			BaiduMapNavigation.openBaiduMapNavi(para, this); // 调用百度地图导航
		} catch (BaiduMapAppNotSupportNaviException e) {
			e.printStackTrace();
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("您尚未安装百度地图app或app版本过低，点击确认安装？");
			builder.setTitle("提示");
			builder.setPositiveButton("确认", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					BaiduMapNavigation.getLatestBaiduMapApp(ActivityBaiduSDK.this); // 下载新版本百度地图
				}
			});

			builder.setNegativeButton("取消", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			builder.create().show();
		}
	}

	public void starWebNavi() {
		LatLng pt1 = new LatLng(39.915291, 116.403857);// 天安门
		LatLng pt2 = new LatLng(40.056858, 116.308194);// 百度大夏
		// 构建 导航参数
		NaviPara para = new NaviPara();
		para.startPoint = pt1;
		para.endPoint = pt2;
		BaiduMapNavigation.openWebBaiduMapNavi(para, this);
	}
	
	@Override
	protected void onDestroy() {
		mBaiduMap.setMyLocationEnabled(false);
		mMapView.onDestroy();
		super.onDestroy();
		// Marker Bitmap Recycle, 回收Bitmap资源
	}

	@Override
	protected void onResume() {
		mMapView.onResume();
		super.onResume();
	}

	@Override
	protected void onPause() {
		mMapView.onPause();
		super.onPause();
	}

	class MenuFragment extends FragmentMenu {
		@Override
		public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
			MenuItem item;
			item = menu.add("定位当前");
			MenuItemCompat.setShowAsAction(item, MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
			item = menu.add("显示标识覆盖物");
			MenuItemCompat.setShowAsAction(item, MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
			item = menu.add("随机改变覆盖物");
			MenuItemCompat.setShowAsAction(item, MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
			item = menu.add("清空覆盖物");
			MenuItemCompat.setShowAsAction(item, MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
			item = menu.add("调用百度导航: 天安门-百度大夏");
			MenuItemCompat.setShowAsAction(item, MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
			item = menu.add("调用网页导航: 天安门-百度大夏");
			MenuItemCompat.setShowAsAction(item, MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
		}
	}
}
