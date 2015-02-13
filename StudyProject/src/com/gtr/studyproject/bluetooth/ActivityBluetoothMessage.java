package com.gtr.studyproject.bluetooth;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gtr.studyproject.activity.R;
import com.xiaotian.framework.common.Mylog;

/**
 * @version 1.0.0
 * @author XiaoTian
 * @name ActivityBluetoothMessage
 * @description 蓝牙消息
 * @date 2014-1-5
 * @link gtrstudio@qq.com
 * @copyright Copyright © 2010-2014 小天天 Studio, All Rights Reserved.
 */
/**
 * 1.说明:BlueTooth 权限:uses-permission android:name="android.permission.BLUETOOTH"
 * 2.搜寻远程蓝牙设备 3.建立远程连接[RFCOMM通道]
 */
public class ActivityBluetoothMessage extends Activity {
	private static final int REQUEST_ENABLE_BT = 0x001;

	// Message types sent from the BluetoothChatService Handler
	public static final int MESSAGE_STATE_CHANGE = 1;
	public static final int MESSAGE_READ = 2;
	public static final int MESSAGE_WRITE = 3;
	public static final int MESSAGE_DEVICE_NAME = 4;
	public static final int MESSAGE_TOAST = 5;
	public static final int MESSAGE_FOUND = 6;
	public static final int MESSAGE_OPERATION = 7;

	public static final int STATE_CONNECTING = 1;
	public static final int STATE_CONNECTED = 2;
	public static final int STATE_LISTEN = 3;
	public static final int STATE_NONE = 4;

	// Key names received from the BluetoothChatService Handler
	public static final String DEVICE_NAME = "device_name";
	public static final String FOUND_MSG = "found_message";
	public static final String MESSAGE = "message";
	public static final String TOAST = "toast";

	private BluetoothAdapter mBluetoothAdapter;
	private List<String> mArrayAdapter;
	private String mConnectedDeviceName;
	private BaseAdapter adapterMessage;

	// Connect Name , Access UUID
	private String NAME = "XiaoTian";
	private UUID MY_UUID = UUID.fromString("fa87c0d0-afac-11de-8a39-0800200c9a66");

	private AcceptThread acceptThread;
	private ConnectThread connectThread;
	private ConnectedThread connectedThread;
	private List<String> listMessage;

	private final Handler messageHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MESSAGE_STATE_CHANGE:
				Mylog.info("MESSAGE_STATE_CHANGE: " + msg.arg1);
				switch (msg.arg1) {
				case STATE_CONNECTED:
					listMessage.add("Connect To " + mConnectedDeviceName + " Success!");
					break;
				case STATE_CONNECTING:
					listMessage.add("Connecting To " + mConnectedDeviceName + "...");
					break;
				case STATE_LISTEN:
				case STATE_NONE:
					listMessage.add("Socket Is Listening ...");
					break;
				}
				break;
			case MESSAGE_WRITE:
				byte[] writeBuf = (byte[]) msg.obj;
				// construct a string from the buffer
				String writeMessage = new String(writeBuf);
				listMessage.add("Me:  " + writeMessage);
				break;
			case MESSAGE_READ:
				byte[] readBuf = (byte[]) msg.obj;
				// construct a string from the valid bytes in the buffer
				String readMessage = new String(readBuf, 0, msg.arg1);
				listMessage.add(mConnectedDeviceName + ":  " + readMessage);
				break;
			case MESSAGE_DEVICE_NAME:
				// save the connected device's name
				mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
				Toast.makeText(getApplicationContext(), "Connected to " + mConnectedDeviceName, Toast.LENGTH_SHORT)
						.show();
				break;
			case MESSAGE_TOAST:
				Toast.makeText(getApplicationContext(), msg.getData().getString(TOAST), Toast.LENGTH_SHORT).show();
				break;
			case MESSAGE_FOUND:
				listMessage.add(msg.getData().getString(FOUND_MSG));
				break;
			case MESSAGE_OPERATION:
				listMessage.add(msg.getData().getString(MESSAGE));
				break;
			}
			adapterMessage.notifyDataSetChanged();
		}
	};

	public ActivityBluetoothMessage() {
		mArrayAdapter = new ArrayList<String>();
		listMessage = new ArrayList<String>();
	}

	// Discovering Devices
	// Create a BroadcastReceiver for ACTION_FOUND
	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			// When discovery finds a device
			if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				// Get the BluetoothDevice object from the Intent
				BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				// Add the name and address to an array adapter to show in a
				// ListView Adapter
				mArrayAdapter.add(device.getName() + "\n" + device.getAddress());
				Mylog.info("Found Device:" + device.getName() + "::" + device.getAddress());
				Message msg = messageHandler.obtainMessage(MESSAGE_FOUND);
				Bundle data = new Bundle();
				data.putString(FOUND_MSG, "Found Device:" + device.getName() + "::" + device.getAddress());
				msg.setData(data);
				messageHandler.sendMessage(msg);
			}
		}
	};

	//
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bluetooth_send);
		ListView lv = (ListView) findViewById(R.id.id_0);
		adapterMessage = new BaseAdapter() {
			@Override
			public long getItemId(int arg0) {
				return arg0;
			}

			@Override
			public Object getItem(int arg0) {
				return listMessage.get(arg0);
			}

			@Override
			public int getCount() {
				return listMessage.size();
			}

			@Override
			public View getView(int arg0, View arg1, ViewGroup arg2) {
				if (arg1 == null) {
					TextView tv = new TextView(ActivityBluetoothMessage.this);
					tv.setPadding(10, 10, 10, 10);
					arg1 = tv;
				}
				TextView tv = (TextView) arg1;
				tv.setText(listMessage.get(arg0));
				return arg1;
			}
		};
		lv.setAdapter(adapterMessage);
		// UI
		findViewById(R.id.id_1).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
				discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
				startActivity(discoverableIntent);
				Message msg = messageHandler.obtainMessage(MESSAGE_OPERATION);
				Bundle data = new Bundle();
				data.putString(MESSAGE, "开放蓝牙设备...");
				msg.setData(data);
				messageHandler.sendMessage(msg);
			}
		});
		findViewById(R.id.id_2).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mBluetoothAdapter.startDiscovery();
				Message msg = messageHandler.obtainMessage(MESSAGE_OPERATION);
				Bundle data = new Bundle();
				data.putString(MESSAGE, "搜寻开放的蓝牙设备...");
				msg.setData(data);
				messageHandler.sendMessage(msg);
			}
		});
		findViewById(R.id.id_3).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startServerSideAccess();
				Message msg = messageHandler.obtainMessage(MESSAGE_OPERATION);
				Bundle data = new Bundle();
				data.putString(MESSAGE, "开启本蓝牙设备Socket通信侦听...");
				msg.setData(data);
				messageHandler.sendMessage(msg);
			}
		});
		findViewById(R.id.id_4).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String info = mArrayAdapter.get(0);
				String address = info.substring(info.length() - 17);
				boolean addressAvailable = BluetoothAdapter.checkBluetoothAddress(address);
				if (addressAvailable) {
					BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
					startClientSideConnect(device);
				}
				Message msg = messageHandler.obtainMessage(MESSAGE_OPERATION);
				Bundle data = new Bundle();
				data.putString(MESSAGE, "请求连接到" + info + "等待连接,请稍后...");
				msg.setData(data);
				messageHandler.sendMessage(msg);
			}
		});
		findViewById(R.id.id_5).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (connectedThread == null) {
					Message msg = messageHandler.obtainMessage(MESSAGE_OPERATION);
					Bundle data = new Bundle();
					data.putString(MESSAGE, "还没有连接到配对的蓝牙设备");
					msg.setData(data);
					messageHandler.sendMessage(msg);
					return;
				}
				connectedThread.write("Hello this is XiaoTianTian Project.".getBytes());
				Message msg = messageHandler.obtainMessage(MESSAGE_OPERATION);
				Bundle data = new Bundle();
				data.putString(MESSAGE, "Hello this is XiaoTianTian Project.");
				msg.setData(data);
				messageHandler.sendMessage(msg);
			}
		});

		// Data
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (mBluetoothAdapter == null) {
			Toast.makeText(this, "不支持蓝牙设备...", Toast.LENGTH_LONG).show();
		}
		if (!mBluetoothAdapter.isEnabled()) {
			Toast.makeText(this, "请求开启蓝牙设备...", Toast.LENGTH_LONG).show();
			Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
		}
		// Discovery Drivers
		// mBluetoothAdapter.startDiscovery();
		// Bonded Devices
		Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
		// If there are paired devices
		if (pairedDevices.size() > 0) {
			// Loop through paired devices
			for (BluetoothDevice device : pairedDevices) {
				// Add the name and address to an array adapter to show in a
				// ListView
				mArrayAdapter.add(device.getName() + "\n" + device.getAddress());
			}
			Mylog.info("Bonded Devices:");
			Mylog.info(mArrayAdapter);
		}
		// Register the BroadcastReceiver
		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		registerReceiver(mReceiver, filter); // Don't forget to unregister
												// during onDestroy
		// Enabling discoverability 300s
		Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
		discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
		startActivity(discoverableIntent);

		// 连接前要先停止蓝牙搜寻
		// RFCOMM only allows one connected client per channel at a time[first
		// close() then connect another]
	}

	public void startServerSideAccess() {
		if (connectThread != null) {
			connectThread.cancel();
			connectThread = null;
		}
		if (connectedThread != null) {
			connectedThread.cancel();
			connectedThread = null;
		}
		if (acceptThread == null) {
			acceptThread = new AcceptThread();
			acceptThread.start();
		}

	}

	public void startClientSideConnect(BluetoothDevice device) {
		if (connectThread != null) {
			connectThread.cancel();
			connectThread = null;
		}
		if (connectedThread != null) {
			connectedThread.cancel();
			connectedThread = null;
		}
		if (connectThread == null) {
			connectThread = new ConnectThread(device);
			connectThread.start();
		}
	}

	// 侦听连接
	private class AcceptThread extends Thread {
		private final BluetoothServerSocket mmServerSocket;

		public AcceptThread() {
			// Use a temporary object that is later assigned to mmServerSocket,
			// because mmServerSocket is final
			BluetoothServerSocket tmp = null;
			try {
				// MY_UUID is the app's UUID string, also used by the client
				// code
				tmp = mBluetoothAdapter.listenUsingRfcommWithServiceRecord(NAME, MY_UUID);
			} catch (IOException e) {}
			mmServerSocket = tmp;
		}

		public void run() {
			BluetoothSocket socket = null;
			// Keep listening until exception occurs or a socket is returned
			while (true) {
				try {
					socket = mmServerSocket.accept(); // 等待连接
				} catch (IOException e) {
					break;
				}
				// If a connection was accepted
				if (socket != null) {
					// Do work to manage the connection (in a separate thread)
					manageConnectedSocket(socket);
					// TODO 关闭侦听Socket
					try {
						mmServerSocket.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					break;
				}
			}
		}

		private void manageConnectedSocket(BluetoothSocket socket) {
			// TODO 连接成功,处理流
			Mylog.info("connected socket success::Access");
			connectedThread = new ConnectedThread(socket);
			connectedThread.start();
			//
			mConnectedDeviceName = socket.getRemoteDevice().getName();
			Message msg = messageHandler.obtainMessage(MESSAGE_STATE_CHANGE, STATE_CONNECTED, STATE_CONNECTED);
			messageHandler.sendMessage(msg);
		}

		/** Will cancel the listening socket, and cause the thread to finish */
		public void cancel() {
			try {
				mmServerSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private class ConnectThread extends Thread {
		private final BluetoothSocket mmSocket;
		private final BluetoothDevice mmDevice;

		public ConnectThread(BluetoothDevice device) {
			// Use a temporary object that is later assigned to mmSocket,
			// because mmSocket is final
			BluetoothSocket tmp = null;
			mmDevice = device;
			// Get a BluetoothSocket to connect with the given BluetoothDevice
			try {
				// MY_UUID is the app's UUID string, also used by the server
				// code
				tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
			} catch (IOException e) {}
			mmSocket = tmp;
		}

		public void run() {
			// Cancel discovery because it will slow down the connection
			if (mBluetoothAdapter != null && mBluetoothAdapter.isDiscovering()) {
				mBluetoothAdapter.cancelDiscovery();
			}

			try {
				// Connect the device through the socket. This will block
				// until it succeeds or throws an exception
				mmSocket.connect();
			} catch (IOException connectException) {
				// Unable to connect; close the socket and get out
				try {
					mmSocket.close();
				} catch (IOException closeException) {}
				return;
			}

			// Do work to manage the connection (in a separate thread)
			manageConnectedSocket(mmSocket);
		}

		private void manageConnectedSocket(BluetoothSocket mmSocket2) {
			// TODO 连接成功,处理数据流
			Mylog.info("connected socket success::Connecter");
			// TODO Read/writer
			connectedThread = new ConnectedThread(mmSocket2);
			connectedThread.start();
			//
			mConnectedDeviceName = mmSocket2.getRemoteDevice().getName();
			Message msg = messageHandler.obtainMessage(MESSAGE_STATE_CHANGE, STATE_CONNECTED, STATE_CONNECTED);
			messageHandler.sendMessage(msg);
		}

		/** Will cancel an in-progress connection, and close the socket */
		public void cancel() {
			try {
				mmSocket.close();
			} catch (IOException e) {}
		}

	}

	private class ConnectedThread extends Thread {
		private final BluetoothSocket mmSocket;
		private final InputStream mmInStream;
		private final OutputStream mmOutStream;

		public ConnectedThread(BluetoothSocket socket) {
			mmSocket = socket;
			InputStream tmpIn = null;
			OutputStream tmpOut = null;

			// Get the input and output streams, using temp objects because
			// member streams are final
			try {
				tmpIn = socket.getInputStream();
				tmpOut = socket.getOutputStream();
			} catch (IOException e) {}

			mmInStream = tmpIn;
			mmOutStream = tmpOut;
		}

		public void run() {
			byte[] buffer = new byte[1024]; // buffer store for the stream
			int bytes; // bytes returned from read()
			Mylog.info("start read message");
			// Keep listening to the InputStream until an exception occurs
			while (true) {
				try {
					// Read from the InputStream
					bytes = mmInStream.read(buffer);
					String message = new String(buffer, 0, bytes);
					Mylog.info("Read Data =" + message);
					// Send the obtained bytes to the UI activity
					messageHandler.obtainMessage(MESSAGE_READ, bytes, -1, buffer).sendToTarget();
				} catch (IOException e) {
					break;
				}
			}
		}

		/*
		 * Call this from the main activity to send data to the remote device
		 */
		public void write(byte[] bytes) {
			try {
				mmOutStream.write(bytes);
			} catch (IOException e) {}
		}

		/* Call this from the main activity to shutdown the connection */
		public void cancel() {
			try {
				mmSocket.close();
			} catch (IOException e) {}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK) {
			return;
		}
		switch (requestCode) {
		case REQUEST_ENABLE_BT:
			// TODO 打开蓝牙
			break;
		default:
			return;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(mReceiver);
		if (acceptThread != null) acceptThread.cancel();
		if (connectThread != null) connectThread.cancel();
	}

}
