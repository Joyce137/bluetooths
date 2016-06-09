package com.health.app.device;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.health.app.MainActivity;
import com.health.app.R;
import com.health.app.util.Preferences;
import com.health.app.widget.CustomDialog;
import com.health.app.widget.FLActivity;


public class BindActivity extends FLActivity {

	ImageView imageBand;
	Button btnNext;
	private BluetoothAdapter mBluetoothAdapter;
	private static final int REQUEST_ENABLE_BT = 1;

	BroadcastReceiver bindBroadcastReceiver;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_main);
		navSetContentView(R.layout.activity_device_bind1);
		linkUiVar();
		bindListener();
		ensureUi();

		bindBroadcastReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				if(intent.getAction().equals(Preferences.BROADCAST_ACTION.GOTOMAIN)){
					finish();
				}
			}
		};
		registerReceiver(bindBroadcastReceiver,new IntentFilter(Preferences.BROADCAST_ACTION.GOTOMAIN));
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(bindBroadcastReceiver);
	}

	@Override
	public void linkUiVar() {
		btnNext  = (Button)findViewById(R.id.btnNext);
		imageBand = (ImageView)findViewById(R.id.imageBand);
	}

	@Override
	public void bindListener() {
		imageBand.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				//2.获取BluetoothAdapter
				final BluetoothManager bluetoothManager =
						(BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
				mBluetoothAdapter = bluetoothManager.getAdapter();
				//3.判断是否支持蓝牙，并打开蓝牙
				if (mBluetoothAdapter == null) {
					showMessage("该手机不支持蓝牙");

				}else {
					if(!mBluetoothAdapter.isEnabled()){
						Intent intent = new Intent();
						intent.setClass(mActivity, BleActivity.class);
						startActivity(intent);
					}else{
						Intent intent = new Intent();
						intent.setClass(mActivity, Bind2Activity.class);
						startActivity(intent);
					}
				}
			}
		});
		btnNext.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent();
				intent.setClass(mActivity, MainActivity.class);
				startActivity(intent);
				finish();
			}
		});
	}

	@Override
	public void ensureUi() {
		hideLeft(true);
		setNavbarTitleText("绑定设备");
		initBle();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	//是否支持BLE
	private void initBle() {
		//1.android:required="false",判断系统是否支持BLE
		if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
//			Toast.makeText(this, "此设备不支持BLE", Toast.LENGTH_SHORT).show();
			CustomDialog.Builder builder = new CustomDialog.Builder(mActivity);
			builder.setTitle("退出");
			builder.setMessage("此设备不支持BLE，是否退出？");
			builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
//					Toast.makeText(mActivity, "此设备支持BLE", Toast.LENGTH_SHORT).show();
					finish();
				}
			});
			builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
//					Toast.makeText(mActivity, "此设备不支持BLE", Toast.LENGTH_SHORT).show();
					dialog.dismiss();
				}
			});
			builder.create().show();
		}

	}
}
