package com.health.app.device;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.health.app.MainActivity;
import com.health.app.R;
import com.health.app.widget.FLActivity;


public class BleActivity extends FLActivity {

	ImageView imageBle;
	Button btnNext;
	private BluetoothAdapter mBluetoothAdapter;
	private static final int REQUEST_ENABLE_BT = 1;

	LinearLayout includeBle;
	TextView textTip;
	Button btnCancal,btnReload;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_device_ble);

		linkUiVar();
		bindListener();
		ensureUi();

	}

	@Override
	public void linkUiVar() {
		btnNext  = (Button)findViewById(R.id.btnNext);
		imageBle = (ImageView)findViewById(R.id.imageBle);

		includeBle = (LinearLayout)findViewById(R.id.includeBle);
		textTip = (TextView)findViewById(R.id.textTip);
		btnReload = (Button)findViewById(R.id.btnReload);
		btnCancal = (Button)findViewById(R.id.btnCancal);
	}

	@Override
	public void bindListener() {
		imageBle.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				textTip.setText("生生健康手环，想要打开蓝牙。");
				btnCancal.setText("拒绝");
				btnReload.setText("允许");
				includeBle.setVisibility(View.VISIBLE);
			}
		});
		btnCancal.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				includeBle.setVisibility(View.GONE);
			}
		});
		btnReload.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				includeBle.setVisibility(View.GONE);
				final BluetoothManager bluetoothManager =
						(BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
				mBluetoothAdapter = bluetoothManager.getAdapter();
				Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
				startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);

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

	}

	@Override
	protected void onResume() {
		super.onResume();
	}

}
