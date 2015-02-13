package com.gtr.studyproject.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.ActionListener;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.net.wifi.p2p.WifiP2pManager.ConnectionInfoListener;
import android.net.wifi.p2p.WifiP2pManager.PeerListListener;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.xiaotian.framework.activity.BaseFragmentActivity;
import com.xiaotian.framework.common.Mylog;

public class ActivityWiFiDirect extends BaseFragmentActivity {
	// 设备WI-Fi直连
	WifiP2pManager manager;
	IntentFilter intentFilter;
	BroadcastReceiver receiver;
	WifiP2pDevice device;
	Channel channel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wifi_direct);
		manager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
		channel = manager.initialize(this, getMainLooper(), null);
		intentFilter = new IntentFilter();
		// 添加Action 过滤器
		intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
		intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
		intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
		intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
		// 广播接收器
		receiver = new BroadcastReceiverWiFiDirect(manager, channel, this);
		registerReceiver(receiver, intentFilter); // 注册广播接收器
	}

	public void onClickSearching(View view) {
		// 发现搜索Device
		// cause BroadcastReceiver to receive the WIFI_P2P_PEERS_CHANGED_ACTION intent.
		manager.discoverPeers(channel, new ActionListener() {
			@Override
			public void onSuccess() {
				Mylog.info("WiFiP2PManager discoverPeers onSuccess");
				toast("Finding Peers");
			}

			@Override
			public void onFailure(int reason) {
				Mylog.info("WiFiP2PManager discoverPeers onFailure");
				toast("Couldn't find Peers");
			}
		});
	}

	public void onClickConnecting(View view) {
		// 连接发现的Device
		WifiP2pConfig config = new WifiP2pConfig();
		if (device != null) {
			config.deviceAddress = device.deviceAddress;
			manager.connect(channel, config, new ActionListener() {
				@Override
				public void onSuccess() {
					toast("连接成功...");
				}

				@Override
				public void onFailure(int reason) {
					toast("连接失败...");
				}
			});
		} else {
			toast("couldn't connect,device is not found");
		}
	}

	/************************************ Static Class Method ************************************/
	public class BroadcastReceiverWiFiDirect extends BroadcastReceiver {
		WifiP2pManager manager;
		Channel channel;
		Context context;

		public BroadcastReceiverWiFiDirect(WifiP2pManager manager, Channel channel, Context context) {
			this.manager = manager;
			this.channel = channel;
			this.context = context;
		}

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			Mylog.info("BroadcastReceiverWiFiDirect onReceive action=" + action);
			if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
				// P2P状态改变广播接收器
				int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
				switch (state) {
				case WifiP2pManager.WIFI_P2P_STATE_ENABLED:
					Toast.makeText(context, "WiFi direct is enabled", Toast.LENGTH_LONG).show();
					break;
				default:
					Toast.makeText(context, "WiFi direct is disabled", Toast.LENGTH_LONG).show();
					break;
				}
			} else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
				// peer list change WiFi连接的设备改变侦听
				// request peers from the wifi p2p manager
				if (manager != null) {
					// for the callback when the peer list is available.
					manager.requestPeers(channel, new PeerListListener() {
						@Override
						public void onPeersAvailable(WifiP2pDeviceList peers) {
							Mylog.info("BroadcastReceiverWiFiDirect onPeersAvailable");
							// 可用连接回调
							for (WifiP2pDevice wpd : peers.getDeviceList()) {
								Mylog.info("PeersAvailable Set Device.");
								device = wpd;
								break;
							}
						}
					});
				}
			} else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
				// P2P连接改变
				if (manager == null) return;
				NetworkInfo networkInfo = intent.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);
				if (networkInfo.isConnected()) {
					// 网络已连接
					// for the callback when the connection info is available.
					manager.requestConnectionInfo(channel, new ConnectionInfoListener() {
						@Override
						public void onConnectionInfoAvailable(WifiP2pInfo info) {
							String infoName = info.groupOwnerAddress.toString();
							Mylog.info("onConnectionInfoAvailable : " + infoName);
						}
					});
				} else {
					// Is's a disconnect
				}
			} else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
				// 设备改变
			}
		}
	}
}
