package com.health.app.user;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.handmark.pulltorefresh.library.internal.Utils;
import com.health.app.R;
import com.health.app.ble.BluetoothLeService;
import com.health.app.ble.DeviceControlService;
import com.health.app.ble.SampleGattAttributes;
import com.health.app.ble.SleepMode;
import com.health.app.statics.BleInfo;
import com.health.app.statics.Util;
import com.health.app.widget.FLActivity;

import java.util.Calendar;
import java.util.UUID;


public class DeviceSettingtActivity extends FLActivity {
	LinearLayout ll_power, ll_testxinlv;
	Button scbtn_timesync,scbtn_lxtestxinlv,scbtn_sleepmode,sc_setting_stepstest,sc_setting_autoheartratetest,sc_setting_nightheartratetest;
	TextView tv_power;
	TextView textPwd;

	LinearLayout ly_setting_testxinlv, ly_setting_stepstest, ly_setting_autoheartratetest, ly_setting_nightheartratetest,
			ll_broadcastjiange, ll_broadcasttime, ll_guangbodatatime, ll_dianliang, ll_cycleheartratetesttimes,
			ll_broadcastcalltime, ll_time, ll_sendpower, ll_heartratemax, ll_heartratemin, ll_heartrateqvalue,
			ll_extime, ll_breadfasttime, ll_lunchtime, ll_dinnertime, ll_nighttesttime, ll_autoheartrateteststeps, ll_lcd;

	TextView reconnect,broadcastjiange,broadcasttime, guangbodatatime, dianliang, cycleheartratetesttimes,
			broadcastcalltime, time, sendpower, heartratemax, heartratemin, heartrateqvalue,
			extime, breadfasttime, lunchtime, dinnertime, nighttesttime, autoheartrateteststeps, lcd;

	boolean istestxinlv = false, istestxinlv_open = false, istestxinlv_close = false;
	boolean xinlv_checkflag2=false;
	boolean xinlv_checkflag3=false;

	boolean isstepstest = false, isstepstest_open = false, isstepstest_close = false;
	boolean stepstest_checkflag2=false;
	boolean stepstest_checkflag3=false;

	boolean isautoheartratetest = false, isautoheartratetest_open = false, isautoheartratetest_close = false;
	boolean autoheartratetest_checkflag2=false;
	boolean autoheartratetest_checkflag3=false;

	boolean isnightheartratetest = false, isnightheartratetest_open = false, isnightheartratetest_close = false;
	boolean nightheartratetest_checkflag2=false;
	boolean nightheartratetest_checkflag3=false;

	byte[] p = new byte[4];//这个用来int转换byte的暂存数组
	final byte[] test = new byte[2];
	String newValue = "";

	BluetoothLeService mBluetoothLeService = DeviceControlService.mBluetoothLeService;
	private Handler mHandler = new Handler();
	Runnable runnable;
	SleepMode sleepMode;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_user_update_pwd);
		navSetContentView(R.layout.activity_user_device_setting);

		//注册广播接收器
		registerReceiver(mGattUpdateReceiver01, makeGattUpdateIntentFilter());

		if (!mBluetoothLeService.initialize()) {//引用服务类的初始化
			Toast.makeText(DeviceSettingtActivity.this, "未初始化", Toast.LENGTH_LONG).show();
		}
		if(!SampleGattAttributes.connectedState) {
			if (!mBluetoothLeService.connect(BleInfo.mDeviceAddress)) {
				Toast.makeText(DeviceSettingtActivity.this, "未连接上", Toast.LENGTH_LONG).show();
//				reconnect.setVisibility(View.VISIBLE);
				System.out.println("断线重连显示");
			}
			else {
				Toast.makeText(DeviceSettingtActivity.this, "已经连接", Toast.LENGTH_LONG).show();
			}
		}
		else {
			Toast.makeText(DeviceSettingtActivity.this, "已经连接", Toast.LENGTH_LONG).show();
		}

		linkUiVar();
		bindListener();
		ensureUi();

		sleepMode = new SleepMode(getApplicationContext());
	}

	@Override
	public void linkUiVar() {
		textPwd = (TextView) findViewById(R.id.textPwd);
		ll_power = (LinearLayout) findViewById(R.id.ly_power);
		tv_power = (TextView) findViewById(R.id.textPower);
		ll_power.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mBluetoothLeService.readMessage(UUID.fromString(SampleGattAttributes.POWER));//
			}
		});
		scbtn_timesync = (Button) findViewById(R.id.btnTime);
		scbtn_timesync.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (SampleGattAttributes.timesync_checkflag)
					timesync(false);
				else
					timesync(true);
			}
		});
		scbtn_lxtestxinlv = (Button) findViewById(R.id.btnHeart);
		scbtn_lxtestxinlv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (SampleGattAttributes.xinlv_checkflag)
					xinlv(false);
				else
					xinlv(true);
			}
		});
		scbtn_sleepmode = (Button) findViewById(R.id.btnSleep);
		scbtn_sleepmode.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (SampleGattAttributes.sleepmode_checkflag)
					sleepmode(false);
				else
					sleepmode(true);
			}
		});

		sc_setting_stepstest = (Button) findViewById(R.id.sc_setting_stepstest);
		sc_setting_stepstest.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(SampleGattAttributes.stepstest_checkflag)
					stepstest(false);
				else
					stepstest(true);
			}
		});

		sc_setting_autoheartratetest = (Button) findViewById(R.id.sc_setting_autoheartratetest);
		sc_setting_autoheartratetest.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(SampleGattAttributes.autoheartratetest_checkflag)
					autoheartratetest(false);
				else
					autoheartratetest(true);
			}
		});

		sc_setting_nightheartratetest = (Button) findViewById(R.id.sc_setting_nightheartratetest);
		sc_setting_nightheartratetest.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(SampleGattAttributes.nightheartratetest_checkflag)
					nightheartratetest(false);
				else
					nightheartratetest(true);
			}
		});

		ll_broadcastjiange = (LinearLayout) findViewById(R.id.ll_broadcastjiange);
		ll_broadcastjiange.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				broadcastjiange();
			}
		});
		ll_broadcasttime = (LinearLayout) findViewById(R.id.ll_broadcasttime);
		ll_broadcasttime.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				broadcasttime();
			}
		});
		ll_guangbodatatime = (LinearLayout) findViewById(R.id.ll_guangbodatatime);
		ll_guangbodatatime.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				guangbodatatime();
			}
		});

		ll_cycleheartratetesttimes = (LinearLayout) findViewById(R.id.ll_cycleheartratetesttimes);
		ll_cycleheartratetesttimes.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				cycleheartratetesttimes();
			}
		});
		ll_broadcastcalltime = (LinearLayout) findViewById(R.id.ll_broadcastcalltime);
		ll_broadcastcalltime.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				broadcastcalltime();
			}
		});

		ll_sendpower = (LinearLayout) findViewById(R.id.ll_sendpower);
		ll_sendpower.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setSendPower();
			}
		});
		ll_heartratemax = (LinearLayout) findViewById(R.id.ll_heartratemax);
		ll_heartratemax.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				heartratemax();
			}
		});
		ll_heartratemin = (LinearLayout) findViewById(R.id.ll_heartratemin);
		ll_heartratemin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				heartratemin();
			}
		});
		ll_heartrateqvalue = (LinearLayout) findViewById(R.id.ll_heartrateqvalue);
		ll_heartrateqvalue.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				heartrateqvalue();
			}
		});
		ll_extime = (LinearLayout) findViewById(R.id.ll_extime);
		ll_extime.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				extime();
			}
		});
		ll_breadfasttime = (LinearLayout) findViewById(R.id.ll_breadfasttime);
		ll_breadfasttime.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				breadfasttime();
			}
		});
		ll_lunchtime = (LinearLayout) findViewById(R.id.ll_lunchtime);
		ll_lunchtime.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				lunchtime();
			}
		});
		ll_dinnertime = (LinearLayout) findViewById(R.id.ll_dinnertime);
		ll_dinnertime.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dinnertime();
			}
		});
		ll_nighttesttime = (LinearLayout) findViewById(R.id.ll_nighttesttime);
		ll_nighttesttime.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				nighttesttime();
			}
		});
		ll_autoheartrateteststeps = (LinearLayout) findViewById(R.id.ll_autoheartrateteststeps);
		ll_autoheartrateteststeps.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				autoheartrateteststeps();
			}
		});
		ll_lcd = (LinearLayout) findViewById(R.id.ll_lcd);
		ll_lcd.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				lcd();
			}
		});
	}


	private void sleepmode(boolean isChecked) {
		sleepMode.testConnect();
		if(isChecked){
			istestxinlv = false;
			sleepMode.openSleepMode();
			SleepMode.openSleepModeSuccess = true;
			SampleGattAttributes.sleepmode_checkflag = true;
			scbtn_sleepmode.setBackgroundResource(R.drawable.on);
		}
		else {
			sleepMode.closeSleepMode();
			SleepMode.closeSleepModeSuccess = true;
			SampleGattAttributes.sleepmode_checkflag = false;
			scbtn_sleepmode.setBackgroundResource(R.drawable.off);
		}
	}

	public void timesync(boolean isChecked){
		if(isChecked){
			istestxinlv = false;
			Calendar c = Calendar.getInstance();//首先要获取日历对象
			int mYear = c.get(Calendar.YEAR); // 获取当前年份
			int mMonth = c.get(Calendar.MONTH) + 1;// 获取当前月份
			int mDay = c.get(Calendar.DAY_OF_MONTH);// 获取当日期
			//int mWay = c.get(Calendar.DAY_OF_WEEK);// 获取当前日期的星期
			int mHour = c.get(Calendar.HOUR_OF_DAY);//时
			int mMinute = c.get(Calendar.MINUTE);
			int msecond = c.get(Calendar.SECOND);
			byte[] p = Util.shortToLH(mYear);
			System.out.println("年" + mYear + "月份" + mMonth + "时" + mHour + "分" + mMinute + "秒" + msecond);
			byte[] data = {(byte) msecond, (byte) mMinute, (byte) mHour, (byte) mDay,
					(byte) mMonth, p[0], p[1]};
			mBluetoothLeService.writeMessage(UUID.fromString(SampleGattAttributes.TIMEADJUSTRW), data);//
			scbtn_timesync.setBackgroundResource(R.drawable.on);
		}
		else{
			scbtn_timesync.setBackgroundResource(R.drawable.off);
		}
	}

	public void xinlv(boolean isChecked){
		istestxinlv = true;
		if (isChecked) {
			if(xinlv_checkflag2==true)
			{
				xinlv_checkflag2=false;
				return;
			}
			if(SampleGattAttributes.xinlv_checkflag==true)
			{
				return;
			}
			istestxinlv_open = true;
			istestxinlv_close = false;

			test[1] = 0x55;
			test[0] = 0x01;
			scbtn_lxtestxinlv.setFocusable(true);
//					.setChecked(false);
			mBluetoothLeService.writeMessage(UUID.fromString(SampleGattAttributes.controlStatus_Write), test);
			Log.e(TAG, "连续测试的开始");
		} else {
			if(xinlv_checkflag2==true)
			{
				xinlv_checkflag2=false;
				return;
			}
			if(xinlv_checkflag3==true)
			{
				xinlv_checkflag3=false;//还原
				return;
			}
			istestxinlv_close = true;
			istestxinlv_open = false;

			test[1] = 0x55;
			test[0] = 0x02;
			scbtn_lxtestxinlv.setFocusable(true);
//					.setChecked(true);
			mBluetoothLeService.writeMessage(UUID.fromString(SampleGattAttributes.controlStatus_Write), test);
		}

	}

	//开启、关闭计步监测
	private void stepstest(boolean isChecked) {
		isstepstest = true;
		if (isChecked) {
			if(stepstest_checkflag2==true)
			{
				stepstest_checkflag2=false;
				return;
			}
			if(SampleGattAttributes.stepstest_checkflag==true)
			{
				return;
			}
			isstepstest_open = true;
			isstepstest_close = false;

			test[1] = 0x55;
			test[0] = 0x03;

			mBluetoothLeService.writeMessage(UUID.fromString(SampleGattAttributes.CHARACTERISTIC_NOTIFY), test);
		} else {
			if(stepstest_checkflag2==true)
			{
				stepstest_checkflag2=false;
				return;
			}
			if(stepstest_checkflag3==true)
			{
				stepstest_checkflag3=false;//还原
				return;
			}
			isstepstest_close = true;
			isstepstest_open = false;

			test[1] = 0x55;
			test[0] = 0x04;

			mBluetoothLeService.writeMessage(UUID.fromString(SampleGattAttributes.CHARACTERISTIC_NOTIFY), test);
		}
	}

	//开启、关闭自动心率监测
	private void autoheartratetest(boolean isChecked) {
		isautoheartratetest = false;
		if (isChecked) {
			if(autoheartratetest_checkflag2==true)
			{
				autoheartratetest_checkflag2=false;
				return;
			}
			if(SampleGattAttributes.autoheartratetest_checkflag==true)
			{
				return;
			}
			isautoheartratetest_open = true;
			isautoheartratetest_close = false;

			test[1] = 0x55;
			test[0] = 0x05;

			mBluetoothLeService.writeMessage(UUID.fromString(SampleGattAttributes.CHARACTERISTIC_NOTIFY), test);
		} else {
			if(autoheartratetest_checkflag2==true)
			{
				autoheartratetest_checkflag2=false;
				return;
			}
			if(autoheartratetest_checkflag3==true)
			{
				autoheartratetest_checkflag3=false;//还原
				return;
			}
			isautoheartratetest_close = true;
			isautoheartratetest_open = false;

			test[1] = 0x55;
			test[0] = 0x06;

			mBluetoothLeService.writeMessage(UUID.fromString(SampleGattAttributes.CHARACTERISTIC_NOTIFY), test);
		}
	}

	//开启、关闭夜间心率监测
	private void nightheartratetest(boolean isChecked) {
		isnightheartratetest = false;
		if (isChecked) {
			if(nightheartratetest_checkflag2==true)
			{
				nightheartratetest_checkflag2=false;
				return;
			}
			if(SampleGattAttributes.nightheartratetest_checkflag==true)
			{
				return;
			}
			isnightheartratetest_open = true;
			isnightheartratetest_close = false;

			test[1] = 0x55;
			test[0] = 0x07;

			mBluetoothLeService.writeMessage(UUID.fromString(SampleGattAttributes.CHARACTERISTIC_NOTIFY), test);
		} else {
			if(nightheartratetest_checkflag2==true)
			{
				nightheartratetest_checkflag2=false;
				return;
			}
			if(nightheartratetest_checkflag3==true)
			{
				nightheartratetest_checkflag3=false;//还原
				return;
			}
			isnightheartratetest_close = true;
			isnightheartratetest_open = false;

			test[1] = 0x55;
			test[0] = 0x08;

			mBluetoothLeService.writeMessage(UUID.fromString(SampleGattAttributes.CHARACTERISTIC_NOTIFY), test);
		}
	}

	//设置发射功率
	private void setSendPower() {
		AlertDialog.Builder customDia1=new AlertDialog.Builder(this);
		customDia1.setIcon(R.mipmap.ic_launcher);
		final View viewDia1= LayoutInflater.from(this).inflate(R.layout.alert_dialog, null);
		TextView textView1 = (TextView) viewDia1.findViewById(R.id.unit);
		textView1.setText("W");
		TextView textView2 = (TextView) viewDia1.findViewById(R.id.fanwei);
		textView2.setText("范围：0W-10W");
		customDia1.setTitle("设置发射功率");
		customDia1.setView(viewDia1);
		customDia1.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				TextView textView = (TextView) viewDia1.findViewById(R.id.newValue);
				newValue = textView.getText().toString();
				broadcastjiange.setText(newValue + "W");
				int value = Integer.valueOf(newValue);
				p = Util.shortToLH(value);
				mBluetoothLeService.writeMessage(UUID.fromString(SampleGattAttributes.sendPowerRW), p);////写入
			}
		});
		customDia1.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		customDia1.create().show();
	}

	//设置广播间隔，100ms
	private void broadcastjiange() {
		AlertDialog.Builder customDia1=new AlertDialog.Builder(this);
		customDia1.setIcon(R.mipmap.ic_launcher);
		final View viewDia1= LayoutInflater.from(this).inflate(R.layout.alert_dialog, null);
		TextView textView1 = (TextView) viewDia1.findViewById(R.id.unit);
		textView1.setText("ms");
		TextView textView2 = (TextView) viewDia1.findViewById(R.id.fanwei);
		textView2.setText("范围：20ms-300ms");
		customDia1.setTitle("设置广播间隔");
		customDia1.setView(viewDia1);
		customDia1.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				TextView textView = (TextView) viewDia1.findViewById(R.id.newValue);
				newValue = textView.getText().toString();
				broadcastjiange.setText(newValue + "ms");
				int value = Integer.valueOf(newValue);
				p = Util.shortToLH(value);
				mBluetoothLeService.writeMessage(UUID.fromString(SampleGattAttributes.broadcastperiodRW), p);////写入
			}
		});
		customDia1.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		customDia1.create().show();
	}

	//设置广播时间，10000ms
	private void broadcasttime() {
		AlertDialog.Builder customDia1=new AlertDialog.Builder(this);
		customDia1.setIcon(R.mipmap.ic_launcher);
		final View viewDia1= LayoutInflater.from(this).inflate(R.layout.alert_dialog, null);
		TextView textView1 = (TextView) viewDia1.findViewById(R.id.unit);
		textView1.setText("ms");
		TextView textView2 = (TextView) viewDia1.findViewById(R.id.fanwei);
		textView2.setText("范围：1000ms-30000ms");
		customDia1.setTitle("设置广播时间");
		customDia1.setView(viewDia1);
		customDia1.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				TextView textView = (TextView) viewDia1.findViewById(R.id.newValue);
				newValue = textView.getText().toString();
				broadcasttime.setText(newValue + "ms");
				int value = Integer.valueOf(newValue);
				p = Util.shortToLH(value);
				mBluetoothLeService.writeMessage(UUID.fromString(SampleGattAttributes.broadcastTimeRW), p);////写入
			}
		});
		customDia1.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		customDia1.create().show();
	}

	//设置数据时间，150ms
	private void guangbodatatime() {
		AlertDialog.Builder customDia1=new AlertDialog.Builder(this);
		customDia1.setIcon(R.mipmap.ic_launcher);
		final View viewDia1= LayoutInflater.from(this).inflate(R.layout.alert_dialog, null);
		TextView textView1 = (TextView) viewDia1.findViewById(R.id.unit);
		textView1.setText("ms");
		TextView textView2 = (TextView) viewDia1.findViewById(R.id.fanwei);
		textView2.setText("范围：100ms-300ms");
		customDia1.setTitle("设置广播数据时间");
		customDia1.setView(viewDia1);
		customDia1.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				TextView textView = (TextView) viewDia1.findViewById(R.id.newValue);
				newValue = textView.getText().toString();
				guangbodatatime.setText(newValue + "ms");
				int value = Integer.valueOf(newValue);
				p = Util.shortToLH(value);
				mBluetoothLeService.writeMessage(UUID.fromString(SampleGattAttributes.BROADCASTLASTRW), p);////写入
			}
		});
		customDia1.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		customDia1.create().show();

	}

	//设置周期性心率测试次数，12
	private void cycleheartratetesttimes() {
		istestxinlv = false;
		AlertDialog.Builder customDia1=new AlertDialog.Builder(this);
		customDia1.setIcon(R.mipmap.ic_launcher);
		final View viewDia1= LayoutInflater.from(this).inflate(R.layout.alert_dialog, null);
		TextView textView1 = (TextView) viewDia1.findViewById(R.id.unit);
		textView1.setText("次");
		TextView textView2 = (TextView) viewDia1.findViewById(R.id.fanwei);
		textView2.setText("范围：1-1440");
		customDia1.setTitle("设置周期性心率测试次数");
		customDia1.setView(viewDia1);
		customDia1.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				TextView textView = (TextView) viewDia1.findViewById(R.id.newValue);
				newValue = textView.getText().toString();
				cycleheartratetesttimes.setText(newValue + "次");
				int value = Integer.valueOf(newValue);
				p = Util.shortToLH(value);
				mBluetoothLeService.writeMessage(UUID.fromString(SampleGattAttributes.HEARTRATEPERIODRW), p);////写入
			}
		});
		customDia1.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		customDia1.create().show();
	}

	//设置广播呼叫持续时间，5000ms
	private void broadcastcalltime() {
		AlertDialog.Builder customDia1=new AlertDialog.Builder(this);
		customDia1.setIcon(R.mipmap.ic_launcher);
		final View viewDia1= LayoutInflater.from(this).inflate(R.layout.alert_dialog, null);
		TextView textView1 = (TextView) viewDia1.findViewById(R.id.unit);
		textView1.setText("ms");
		TextView textView2 = (TextView) viewDia1.findViewById(R.id.fanwei);
		textView2.setText("范围：1000ms-20000ms");
		customDia1.setTitle("设置广播呼叫持续时间");
		customDia1.setView(viewDia1);
		customDia1.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				TextView textView = (TextView) viewDia1.findViewById(R.id.newValue);
				newValue = textView.getText().toString();
				broadcastcalltime.setText(newValue + "ms");
				int value = Integer.valueOf(newValue);
				p = Util.shortToLH(value);
				mBluetoothLeService.writeMessage(UUID.fromString(SampleGattAttributes.SOSRW), p);////写入
			}
		});
		customDia1.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		customDia1.create().show();

	}


	//设置心率上限，120
	private void heartratemax() {
		AlertDialog.Builder customDia1=new AlertDialog.Builder(this);
		customDia1.setIcon(R.mipmap.ic_launcher);
		final View viewDia1= LayoutInflater.from(this).inflate(R.layout.alert_dialog, null);
		TextView textView1 = (TextView) viewDia1.findViewById(R.id.unit);
		textView1.setText("bpm");
		TextView textView2 = (TextView) viewDia1.findViewById(R.id.fanwei);
		textView2.setText("范围：(220-age)*80% ~ 220-age");
		customDia1.setTitle("设置心率上限");
		customDia1.setView(viewDia1);
		customDia1.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				TextView textView = (TextView) viewDia1.findViewById(R.id.newValue);
				newValue = textView.getText().toString();
				heartratemax.setText(newValue + "bpm");
				int value = Integer.valueOf(newValue);
				p = Util.shortToLH(value);
				mBluetoothLeService.writeMessage(UUID.fromString(SampleGattAttributes.heartRateMaxMinQ), p);////写入
			}
		});
		customDia1.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		customDia1.create().show();

	}

	//设置心率下限，40
	private void heartratemin() {
		AlertDialog.Builder customDia1=new AlertDialog.Builder(this);
		customDia1.setIcon(R.mipmap.ic_launcher);
		final View viewDia1= LayoutInflater.from(this).inflate(R.layout.alert_dialog, null);
		TextView textView1 = (TextView) viewDia1.findViewById(R.id.unit);
		textView1.setText("bpm");
		TextView textView2 = (TextView) viewDia1.findViewById(R.id.fanwei);
		textView2.setText("范围：40 - 60");
		customDia1.setTitle("设置心率下限");
		customDia1.setView(viewDia1);
		customDia1.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				TextView textView = (TextView) viewDia1.findViewById(R.id.newValue);
				newValue = textView.getText().toString();
				heartratemin.setText(newValue + "bpm");
				int value = Integer.valueOf(newValue);
				p = Util.shortToLH(value);
				mBluetoothLeService.writeMessage(UUID.fromString(SampleGattAttributes.heartRateMaxMinQ), p);////写入
			}
		});
		customDia1.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		customDia1.create().show();
	}

	//设置心率q值，90
	private void heartrateqvalue() {
		AlertDialog.Builder customDia1=new AlertDialog.Builder(this);
		customDia1.setIcon(R.mipmap.ic_launcher);
		final View viewDia1= LayoutInflater.from(this).inflate(R.layout.alert_dialog, null);
		TextView textView1 = (TextView) viewDia1.findViewById(R.id.unit);
		textView1.setText("bpm");
		TextView textView2 = (TextView) viewDia1.findViewById(R.id.fanwei);
		textView2.setText("范围：(220-age)*60% ~ (220-age)*80%");
		customDia1.setTitle("设置心率q值");
		customDia1.setView(viewDia1);
		customDia1.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				TextView textView = (TextView) viewDia1.findViewById(R.id.newValue);
				newValue = textView.getText().toString();
				heartrateqvalue.setText(newValue + "bpm");
				int value = Integer.valueOf(newValue);
				p = Util.shortToLH(value);
				mBluetoothLeService.writeMessage(UUID.fromString(SampleGattAttributes.heartRateMaxMinQ), p);////写入
			}
		});
		customDia1.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		customDia1.create().show();
	}

	//设置锻炼时间，17
	private void extime() {
		AlertDialog.Builder customDia1=new AlertDialog.Builder(this);
		customDia1.setIcon(R.mipmap.ic_launcher);
		final View viewDia1= LayoutInflater.from(this).inflate(R.layout.alert_dialog, null);
		TextView textView1 = (TextView) viewDia1.findViewById(R.id.unit);
		textView1.setText("时");
		TextView textView2 = (TextView) viewDia1.findViewById(R.id.fanwei);
		textView2.setText("范围：5-9");
		customDia1.setTitle("设置锻炼时间");
		customDia1.setView(viewDia1);
		customDia1.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				TextView textView = (TextView) viewDia1.findViewById(R.id.newValue);
				newValue = textView.getText().toString();
				extime.setText(newValue + "时");
				int value = Integer.valueOf(newValue);
				p = Util.shortToLH(value);
				mBluetoothLeService.writeMessage(UUID.fromString(SampleGattAttributes.sportMealTime), p);////写入
			}
		});
		customDia1.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		customDia1.create().show();

	}

	//设置早餐时间，6
	private void breadfasttime() {
		AlertDialog.Builder customDia1=new AlertDialog.Builder(this);
		customDia1.setIcon(R.mipmap.ic_launcher);
		final View viewDia1= LayoutInflater.from(this).inflate(R.layout.alert_dialog, null);
		TextView textView1 = (TextView) viewDia1.findViewById(R.id.unit);
		textView1.setText("时");
		TextView textView2 = (TextView) viewDia1.findViewById(R.id.fanwei);
		textView2.setText("范围：6-10");
		customDia1.setTitle("设置早餐时间");
		customDia1.setView(viewDia1);
		customDia1.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				TextView textView = (TextView) viewDia1.findViewById(R.id.newValue);
				newValue = textView.getText().toString();
				breadfasttime.setText(newValue + "时");
				int value = Integer.valueOf(newValue);
				p = Util.shortToLH(value);
				mBluetoothLeService.writeMessage(UUID.fromString(SampleGattAttributes.sportMealTime), p);////写入
			}
		});
		customDia1.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		customDia1.create().show();

	}

	//设置午餐时间，11
	private void lunchtime() {
		AlertDialog.Builder customDia1=new AlertDialog.Builder(this);
		customDia1.setIcon(R.mipmap.ic_launcher);
		final View viewDia1= LayoutInflater.from(this).inflate(R.layout.alert_dialog, null);
		TextView textView1 = (TextView) viewDia1.findViewById(R.id.unit);
		textView1.setText("时");
		TextView textView2 = (TextView) viewDia1.findViewById(R.id.fanwei);
		textView2.setText("范围：10-15");
		customDia1.setTitle("设置午餐时间");
		customDia1.setView(viewDia1);
		customDia1.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				TextView textView = (TextView) viewDia1.findViewById(R.id.newValue);
				newValue = textView.getText().toString();
				lunchtime.setText(newValue + "时");
				int value = Integer.valueOf(newValue);
				p = Util.shortToLH(value);
				mBluetoothLeService.writeMessage(UUID.fromString(SampleGattAttributes.sportMealTime), p);////写入
			}
		});
		customDia1.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		customDia1.create().show();
	}

	//设置晚餐时间，19
	private void dinnertime() {
		AlertDialog.Builder customDia1=new AlertDialog.Builder(this);
		customDia1.setIcon(R.mipmap.ic_launcher);
		final View viewDia1= LayoutInflater.from(this).inflate(R.layout.alert_dialog, null);
		TextView textView1 = (TextView) viewDia1.findViewById(R.id.unit);
		textView1.setText("时");
		TextView textView2 = (TextView) viewDia1.findViewById(R.id.fanwei);
		textView2.setText("范围：16-20");
		customDia1.setTitle("设置晚餐时间");
		customDia1.setView(viewDia1);
		customDia1.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				TextView textView = (TextView) viewDia1.findViewById(R.id.newValue);
				newValue = textView.getText().toString();
				dinnertime.setText(newValue + "时");
				int value = Integer.valueOf(newValue);
				p = Util.shortToLH(value);
				mBluetoothLeService.writeMessage(UUID.fromString(SampleGattAttributes.sportMealTime), p);////写入
			}
		});
		customDia1.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		customDia1.create().show();
	}

	//设置夜间检测时间，20
	private void nighttesttime() {
		AlertDialog.Builder customDia1=new AlertDialog.Builder(this);
		customDia1.setIcon(R.mipmap.ic_launcher);
		final View viewDia1= LayoutInflater.from(this).inflate(R.layout.alert_dialog, null);
		TextView textView1 = (TextView) viewDia1.findViewById(R.id.unit);
		textView1.setText("min");
		TextView textView2 = (TextView) viewDia1.findViewById(R.id.fanwei);
		textView2.setText("范围：5-480");
		customDia1.setTitle("设置夜间检测时间");
		customDia1.setView(viewDia1);
		customDia1.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				TextView textView = (TextView) viewDia1.findViewById(R.id.newValue);
				newValue = textView.getText().toString();
				nighttesttime.setText(newValue + "min");
				int value = Integer.valueOf(newValue);
				p = Util.shortToLH(value);
				mBluetoothLeService.writeMessage(UUID.fromString(SampleGattAttributes.nightCheckTime), p);////写入
			}
		});
		customDia1.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		customDia1.create().show();
	}

	//设置自动心率检测步数，500
	private void autoheartrateteststeps() {
		AlertDialog.Builder customDia1=new AlertDialog.Builder(this);
		customDia1.setIcon(R.mipmap.ic_launcher);
		final View viewDia1= LayoutInflater.from(this).inflate(R.layout.alert_dialog, null);
		TextView textView1 = (TextView) viewDia1.findViewById(R.id.unit);
		textView1.setText("步");
		TextView textView2 = (TextView) viewDia1.findViewById(R.id.fanwei);
		textView2.setText("范围：10-1000");
		customDia1.setTitle("设置自动心率检测步数");
		customDia1.setView(viewDia1);
		customDia1.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				TextView textView = (TextView) viewDia1.findViewById(R.id.newValue);
				newValue = textView.getText().toString();
				autoheartrateteststeps.setText(newValue + "步");
				int value = Integer.valueOf(newValue);
				p = Util.shortToLH(value);
				mBluetoothLeService.writeMessage(UUID.fromString(SampleGattAttributes.autoCheckHeartTimeSteps), p);////写入
			}
		});
		customDia1.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		customDia1.create().show();

	}

	//LCD刷屏
	private void lcd() {
		lcd.setText(100+"ms");
		p = Util.shortToLH(100);
		mBluetoothLeService.writeMessage(UUID.fromString(SampleGattAttributes.lcdControl), p);////写入
	}


	@Override
	public void bindListener() {
		textPwd.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent6 = new Intent();
				intent6.setClass(mContext, DeviceUpdatePwdActivity.class);
				startActivity(intent6);
			}
		});
	}

	@Override
	public void ensureUi() {
		setNavbarTitleText("设备设置");
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	//自定义广播
	private static IntentFilter makeGattUpdateIntentFilter() {
		final IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
		intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
		intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
		intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
		// intentFilter.addAction(BluetoothLeService.UpData);
		intentFilter.addAction(BluetoothLeService.ACTION_GATT_WIRTE_SUCCESS);
		return intentFilter;
	}
	//定义一个广播接收器
	private final BroadcastReceiver mGattUpdateReceiver01 = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			final String action = intent.getAction();
			//已经连接上BLE

			SampleGattAttributes.thisrecall= Calendar.getInstance();//记录回掉时间

			if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
				System.out.println("到ACTION_GATT_CONNECTED");
				SampleGattAttributes.connectedState=true;
//				reconnect.setVisibility(View.GONE);//如果连接成功了，重连按键消失
//                autodisconnect();
				if(SampleGattAttributes.isinsetactivity)
					mHandler.postDelayed(runnable, 30000);
			}
			//没有连接上BLE
			else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
//				reconnect.setVisibility(View.VISIBLE);
				mHandler.removeCallbacks(runnable);//关掉自动关闭连接的线程
				System.out.println("断线重连显示，并关闭判断中");
			}
			else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED
					.equals(action)) {
//                Toast.makeText(SettingActivity.this, "服务获取", Toast.LENGTH_LONG).show();
			}
			else if (BluetoothLeService.ACTION_GATT_WIRTE_SUCCESS.equals(action)) {
				if (istestxinlv) {
					if (istestxinlv_open) {
						Toast.makeText(DeviceSettingtActivity.this, "打开成功！", Toast.LENGTH_LONG).show();
						SampleGattAttributes.xinlv_checkflag=true;

						xinlv_checkflag2=true;//告诉listener不是人操作
						scbtn_lxtestxinlv.setBackgroundResource(R.drawable.on);
					}

					if (istestxinlv_close) {
						Toast.makeText(DeviceSettingtActivity.this, "关闭成功！", Toast.LENGTH_LONG).show();
						SampleGattAttributes.xinlv_checkflag=false;
						xinlv_checkflag2=true;
						scbtn_lxtestxinlv.setBackgroundResource(R.drawable.off);
					}
				}
//				if (isstepstest) {
//					if (isstepstest_open) {
//						Toast.makeText(DeviceSettingtActivity.this, "打开成功！", Toast.LENGTH_LONG).show();
//						SampleGattAttributes.stepstest_checkflag=true;
//
//						stepstest_checkflag2=true;//告诉listener不是人操作
//						sc_setting_stepstest.setChecked(true);
//					}
//
//					if (isstepstest_close) {
//						Toast.makeText(SettingActivity.this, "关闭成功！", Toast.LENGTH_LONG).show();
//						SampleGattAttributes.stepstest_checkflag=false;
//						stepstest_checkflag2=true;
//						sc_setting_stepstest.setChecked(false);
//					}
//				}
//				if (isautoheartratetest) {
//					if (isautoheartratetest_open) {
//						Toast.makeText(SettingActivity.this, "打开成功！", Toast.LENGTH_LONG).show();
//						SampleGattAttributes.autoheartratetest_checkflag=true;
//
//						autoheartratetest_checkflag2=true;//告诉listener不是人操作
//						sc_setting_autoheartratetest.setChecked(true);
//					}
//
//					if (isautoheartratetest_close) {
//						Toast.makeText(DeviceSettingtActivity.this, "关闭成功！", Toast.LENGTH_LONG).show();
//						SampleGattAttributes.autoheartratetest_checkflag=false;
//						autoheartratetest_checkflag2=true;
//						sc_setting_autoheartratetest.setChecked(false);
//					}
//				}
//				if (isnightheartratetest) {
//					if (isnightheartratetest_open) {
//						Toast.makeText(DeviceSettingtActivity.this, "打开成功！", Toast.LENGTH_LONG).show();
//						SampleGattAttributes.nightheartratetest_checkflag=true;
//
//						nightheartratetest_checkflag2=true;//告诉listener不是人操作
//						sc_setting_nightheartratetest.setChecked(true);
//					}
//
//					if (isnightheartratetest_close) {
//						Toast.makeText(DeviceSettingtActivity.this, "关闭成功！", Toast.LENGTH_LONG).show();
//						SampleGattAttributes.nightheartratetest_checkflag=false;
//						nightheartratetest_checkflag2=true;
//						sc_setting_nightheartratetest.setChecked(false);
//					}
//				}
			}
			else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {

				if (intent.getStringExtra("DataType").equals("SevenDayData")) {
					//
					int x = 000;
				}
				//广播间隔
				else if (intent.getStringExtra("DataType").equals("broadcastperiod")) {

				}
				//广播时间
				else if (intent.getStringExtra("DataType").equals("broadcastTime")) {

				}
				//广播数据时间
				else if (intent.getStringExtra("DataType").equals("broadcastdatatime")) {

				}
				//周期性测心率次数
				else if (intent.getStringExtra("DataType").equals("CollectPeriod")) {

				}
				//sos时间
				else if (intent.getStringExtra("DataType").equals("SOSTime")) {

				}
				//发射功率
				else if (intent.getStringExtra("DataType").equals("sendPower")) {

				}
				//心率上下限q
				else if (intent.getStringExtra("DataType").equals("heartRateMaxMinQ")) {

				}
				//运动用餐时间
				else if (intent.getStringExtra("DataType").equals("sportMealTime")) {

				}
				//夜间检测时间
				else if (intent.getStringExtra("DataType").equals("nightCheckTime")) {

				}
				//自动心率检测步数
				else if (intent.getStringExtra("DataType").equals("autoCheckHeartTimeSteps")) {

				}
				//lcd刷屏
				else if (intent.getStringExtra("DataType").equals("lcdControl")) {

				}

				else if (intent.getStringExtra("DataType").equals("Power")) {
					byte[] dataBytes = intent.getByteArrayExtra("data");
					String powerStr = dataBytes[0]+"%";
					tv_power.setText(powerStr);
					SharedPreferences sharedPreferences = getSharedPreferences("share", MODE_PRIVATE);
					SharedPreferences.Editor editor = sharedPreferences.edit();
					editor.putString("DEVICE_POWER", powerStr);
					editor.commit();
				}
				else if (intent.getStringExtra("DataType").equals("TimeAdjust")) {

				}
			}
		}
	};

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(SampleGattAttributes.connectedState) {
			mBluetoothLeService.disconnect();
		}
	}
}
