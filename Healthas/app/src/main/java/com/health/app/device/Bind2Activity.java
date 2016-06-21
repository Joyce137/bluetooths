package com.health.app.device;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.health.app.MainActivity;
import com.health.app.R;
import com.health.app.adapter.DeviceAdapter;
import com.health.app.statics.BleInfo;
import com.health.app.type.DeviceBean;
import com.health.app.util.Preferences;
import com.health.app.widget.FLActivity;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class Bind2Activity extends FLActivity {
	private RecyclerView scan_recyclerView;
	private DeviceAdapter adapter;
	Button btnNext;
	private BluetoothAdapter mBluetoothAdapter;
	private static final int REQUEST_ENABLE_BT = 1;
	private static final long SCAN_PERIOD = 10000;
	private Handler scanHandler;
	private boolean mScanning;
	private HashMap<String, byte[]> scanrecordsMap = new HashMap<String, byte[]>();
	private HashMap<String, String> deviceHashMap = new HashMap<>();
	public List<DeviceBean> deviceBeans = new ArrayList<DeviceBean>();
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_device_bind2);

		initView();

		linkUiVar();
		bindListener();
		ensureUi();

	}

	public void startActivity(final View v, final DeviceBean bean) {
		AlertDialog.Builder builder = new AlertDialog.Builder(Bind2Activity.this);
		// builder.setIcon(R.mipmap.ic_warning_amber);
		builder.setMessage("您选的设备是：" + bean.getName().trim() + "，是否绑定");
		builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				BleInfo.scanStaus = false;
				Intent intent = new Intent(Bind2Activity.this, MainActivity.class);
				intent.putExtra("DEVICE_NAME", bean.getName());
				intent.putExtra("DEVICE_ADDRESS", bean.getAddress());
				intent.putExtra("scan", scanrecordsMap.get(bean.getAddress()));//哈希表返回的是地址对应的扫描号
				SharedPreferences sharedPreferences = getSharedPreferences("share", MODE_PRIVATE);
				SharedPreferences.Editor editor = sharedPreferences.edit();
				editor.putString("DEVICE_NAME", bean.getAddress());
				editor.commit();
				Log.d(TAG, bean.getName() + bean.getAddress());
				startActivity(intent);
			}
		});
		builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.show();
	}

	private void initView() {
		scan_recyclerView = (RecyclerView) findViewById(R.id.scan_recyclerview);
		scan_recyclerView.setHasFixedSize(true);
		scan_recyclerView.setLayoutManager(new LinearLayoutManager(this));
		adapter = new DeviceAdapter(Bind2Activity.this, deviceBeans);
		scan_recyclerView.setAdapter(adapter);
	}

	@Override
	public void linkUiVar() {
		btnNext = (Button) findViewById(R.id.btnNext);
	}

	@Override
	public void bindListener() {
		btnNext.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				sendBroadcast(Preferences.BROADCAST_ACTION.GOTOMAIN);

				Intent intent = new Intent();
				intent.setClass(mActivity, MainActivity.class);
				startActivity(intent);
				finish();
			}
		});
	}

	@Override
	public void ensureUi() {
		scan();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	public void scan() {

		mScanning = true;

		scanHandler = new Handler();
		//1.android:required="false",判断系统是否支持BLE
		if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
			Toast.makeText(this, "此设备不支持BLE", Toast.LENGTH_SHORT).show();
			finish();
		}
		//2.获取BluetoothAdapter
		final BluetoothManager bluetoothManager =
				(BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
		mBluetoothAdapter = bluetoothManager.getAdapter();

		//3.判断是否支持蓝牙，并打开蓝牙
		if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
			Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
		}
		//4.搜索BLE设备
		//当找到对应的设备后，立即停止扫描；
		//不要循环搜索设备，为每次搜索设置适合的时间限制。避免设备不在可用范围的时候持续不停扫描，消耗电量。
		scanLeDevice(true);
		showMessage("搜索手环");
	}
	private void scanLeDevice(final boolean enable) {

		if (enable) {
			scanHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					mScanning = false;
					mBluetoothAdapter.stopLeScan(mLeScanCallback);
				}
			}, SCAN_PERIOD);
			mScanning = true;
			mBluetoothAdapter.startLeScan(mLeScanCallback);
		} else {
			mScanning = false;
			mBluetoothAdapter.stopLeScan(mLeScanCallback);
		}
	}

	private BluetoothAdapter.LeScanCallback mLeScanCallback =
			new BluetoothAdapter.LeScanCallback() {
				@Override
				public void onLeScan(final BluetoothDevice device, int rssi,final byte[] scanRecord) {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							deviceHashMap.put(device.getAddress(),device.getName());
							scanrecordsMap.put(device.getAddress(), scanRecord);
							deviceBeans.clear();
							Iterator iter = deviceHashMap.entrySet().iterator();
							while (iter.hasNext()) {
								Map.Entry entry = (Map.Entry) iter.next();
								String address = (String) entry.getKey();
								String name = (String) entry.getValue();
								deviceBeans.add(new DeviceBean(name, address));
							}

							scan_recyclerView.scrollToPosition(adapter.getItemCount() - 1);
							adapter.notifyDataSetChanged();
							Log.e("LeScanCallback", device.getName() + device.getAddress());
						}
					});
				}

			};

	public void reScan() {
		scanLeDevice(false);//停止
		scanrecordsMap.clear();
		scanLeDevice(true);//开始
	}

}
