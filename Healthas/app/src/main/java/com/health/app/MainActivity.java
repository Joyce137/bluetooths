package com.health.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.health.app.ble.BluetoothLeService;
import com.health.app.ble.DeviceControlService;
import com.health.app.ble.SampleGattAttributes;
import com.health.app.database.impl.ScanBleDataDaoImpl;
import com.health.app.datas.CycleHeartRateData;
import com.health.app.datas.HeartRateData;
import com.health.app.datas.NightHeartRateData;
import com.health.app.datas.SportData;
import com.health.app.datas.TimeData;
import com.health.app.device.Bind2Activity;
import com.health.app.device.BindActivity;
import com.health.app.fragment.HealthFragment;
import com.health.app.fragment.LeftFragment;
import com.health.app.fragment.MonitorFragment;
import com.health.app.fragment.MsgFragment;
import com.health.app.fragment.SportFragment;
import com.health.app.sport.SportInfoActivity;
import com.health.app.statics.BleInfo;
import com.health.app.statics.CalculateKcal;
import com.health.app.statics.Users;
import com.health.app.statics.Util;
import com.health.app.user.DeviceSettingtActivity;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;

public class MainActivity extends SlidingFragmentActivity {
	private final static String TAG = MainActivity.class.getSimpleName();
	public Activity mActivity;
	MainApplication mainApp;
	ImageButton btnMore,btnSport;
	TextView textNavbarTitle;
	Button btnAttention ;
	FrameLayout frame_content;
	LinearLayout ly_monitor,ly_sport,ly_health,ly_msg;
	Button btnMonitorTab,btnSportTab,btnHealthTab,btnMsgTab;
	TextView tv_monitor,tv_sport,tv_health,tv_mag;

	private Fragment mContent;

	LinearLayout select_ll;
	Button select_btn;
	TextView select_text;

	int flag = 0;
	Fragment health,msg,sport,monitor;

	public static Context context;
	private static BluetoothLeService mBluetoothLeService;
	private static int count;

	private static final int REQUEST_ENABLE_BT = 1;
	public static final String EXTRAS_DEVICE_NAME = "DEVICE_NAME";
	public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";

	//BLe控制服务
	private ServiceConnection serCon;
	public static boolean isBindServiceConnection = false;
	DeviceControlService devConService;
	private BluetoothAdapter mBluetoothAdapter;
	private static String mydate;
	private byte[] scan, scanData;

	//数据库
	ScanBleDataDaoImpl scanBleDataDao;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		context=this;
		scanBleDataDao = new ScanBleDataDaoImpl(context);

		mainApp = MainApplication.getInstance();


		registerReceiver(mGattUpdateReceiver01, makeGattUpdateIntentFilter());
		final Intent intent = getIntent();
		if (intent != null) {
			BleInfo.mDeviceName = intent.getStringExtra(EXTRAS_DEVICE_NAME);
			BleInfo.mDeviceAddress = intent.getStringExtra(EXTRAS_DEVICE_ADDRESS);
			BleInfo.mCurScanAddress = BleInfo.mDeviceAddress;
			scan = intent.getByteArrayExtra("scan");
			if (scan != null) {
				//得到byte[]
				getScanRecordBleData(scan);
			}
		}

		initView();
		initSlidingMenu(savedInstanceState);

		setSelect(btnMonitorTab, tv_monitor);
		initFragment(0);
	}

	@Override
	protected void onResume() {
		super.onResume();
		initBle();
		//绑定服务
		initService();

		initSearchBle();

		checkfirstrunaday();

//		mBluetoothLeService = DeviceControlService.mBluetoothLeService;
//		if(mBluetoothLeService == null){
//			return;
//		}
//		if (!mBluetoothLeService.initialize()) {//引用服务类的初始化
//			return;
//		}
//		if(SampleGattAttributes.connectedState)return;
//
//		SampleGattAttributes.whichactivityconnect=true;//允许七天数据
//
//		if (!mBluetoothLeService.connect(BleInfo.mDeviceAddress)) {
//			;
//		} else {
//			SampleGattAttributes.connectedState=true;
////				Toast.makeText(context, "已经连接", Toast.LENGTH_LONG).show();
//		}


	}

	private void getScanRecordBleData(byte[] scanDataBytes) {
		//将广播数据存储到SQLite数据库
		if(BleInfo.mCurScanAddress == null)
			return;
		String heartRate = String.valueOf(scanDataBytes[19] & 0xff);
		if(heartRate.equals("255"))
			heartRate = "0";
		String step = String.valueOf((scanDataBytes[26] & 0xff) * 256 + (scanDataBytes[25] & 0xff));
		String kcal = CalculateKcal.getKcal(step);
		String byteStr = Util.bytesToHexString(scanDataBytes);
		scanBleDataDao.insertBleData(BleInfo.mCurScanAddress,byteStr,step,heartRate,kcal,scanDataBytes.length+"");
	}

	//current connected ble data
	private void getScanRecordBleDataAfterConnected(byte[] scan) {
		String heartRate = String.valueOf(scan[19] & 0xff);
		String step = String.valueOf((scan[26] & 0xff) * 256 + (scan[25] & 0xff));
	}


	private void getNotifyBleData(byte[] dataBytes) {
		switch (dataBytes[2]) {
			case 0x01:
				TimeData data1 = TimeData.toData(dataBytes);
				showData(data1);
				break;
			case 0x02:
				SportData data2 = SportData.toData(dataBytes);
				showData(data2);
				break;
			case 0x03:
				CycleHeartRateData data3 = CycleHeartRateData.toData(dataBytes);
				showData(data3);
				break;
			case 0x04:
				NightHeartRateData data4 = NightHeartRateData.toData(dataBytes);
				showData(data4);
				break;
			case 0x05:
				HeartRateData data5 = HeartRateData.toData(dataBytes);
				showData(data5);
				break;
			default:
				break;
		}
	}

	public void showData(TimeData data){
		Toast.makeText(context,"TimeData......",Toast.LENGTH_SHORT).show();
	}

	public void showData(SportData data){
		Toast.makeText(context,"SportData......",Toast.LENGTH_SHORT).show();
	}

	public void showData(CycleHeartRateData data){
		Toast.makeText(context,"CycleHeartRateData......",Toast.LENGTH_SHORT).show();
	}

	public void showData(NightHeartRateData data){
		Toast.makeText(context,"NightHeartRateData......",Toast.LENGTH_SHORT).show();
	}

	public void showData(HeartRateData data){
		Toast.makeText(context,"SleepModeHeartRateData......",Toast.LENGTH_SHORT).show();
	}



	private void initService() {
		serCon = new ServiceConnection() {
			@Override
			public void onServiceConnected(ComponentName componentName, IBinder service) {
				devConService = ((DeviceControlService.LocalBinder) service).getService();
			}
			@Override
			public void onServiceDisconnected(ComponentName componentName) {
			}
		};
		Intent devConServiceIntent = new Intent(this, DeviceControlService.class);
		boolean isbind = bindService(devConServiceIntent, serCon,
				BIND_AUTO_CREATE);//绑定
		if (isbind) {
			Log.d(TAG, "bindService success!!");
		} else {
			Log.d(TAG, "bindService failed!!");
		}
	}

	//是否支持BLE
	private boolean initBle() {
		//1.android:required="false",判断系统是否支持BLE
		if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
			Toast.makeText(this, "此设备不支持BLE", Toast.LENGTH_SHORT).show();
			AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
			builder.setIcon(R.mipmap.ic_launcher);
			builder.setTitle("退出对话框");
			builder.setMessage("此设备不支持BLE，是否退出？");
			builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Toast.makeText(MainActivity.this, "此设备支持BLE", Toast.LENGTH_SHORT).show();
					finish();
				}
			});
			builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Toast.makeText(MainActivity.this, "此设备不支持BLE", Toast.LENGTH_SHORT).show();
				}
			});
			builder.show();
		}
		//2.获取BluetoothAdapter
		final BluetoothManager bluetoothManager =
				(BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
		mBluetoothAdapter = bluetoothManager.getAdapter();
		//3.判断是否支持蓝牙，并打开蓝牙
		if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
			Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
			return true;
		}
		return false;
	}

	public void initSearchBle(){
		//检查程序是否为第一次运行
		if (checkIsFirstRun()) {
			AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
			builder.setIcon(R.mipmap.ic_launcher);
			builder.setTitle("第一次运行");
			builder.setMessage("此设备为第一次运行，是否扫描BLE？");
			builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Intent intDet = new Intent();
					intDet.setClass(MainActivity.this, Bind2Activity.class);
					startActivity(intDet);
				}
			});
			builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Toast.makeText(MainActivity.this, "不扫描", Toast.LENGTH_SHORT).show();
				}
			});
			builder.show();

		} else {
			SharedPreferences sharedPreferences = this.getSharedPreferences("share", MODE_PRIVATE);
			String device_name = sharedPreferences.getString("DEVICE_NAME", "");
			Log.d(TAG, device_name);
			if (device_name.equals("")) {
				AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
				builder.setIcon(R.mipmap.ic_launcher);
				builder.setTitle("是否扫描绑定BLE");
				builder.setMessage("此设备还没绑定BLE，是否前去绑定？");
				builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Intent intDet = new Intent();
						intDet.setClass(MainActivity.this, DeviceSettingtActivity.class);
						startActivity(intDet);
					}
				});
				builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Toast.makeText(MainActivity.this, "不绑定", Toast.LENGTH_SHORT).show();
					}
				});
				builder.show();

			} else {
				BleInfo.mDeviceAddress = device_name;
			}
		}
	}

	//检查程序是否为第一次运行
	private boolean checkIsFirstRun() {
		// 通过检查程序中的缓存文件判断程序是否是第一次运行
		SharedPreferences sharedPreferences = this.getSharedPreferences("share", MODE_PRIVATE);
		boolean isFirstRun = sharedPreferences.getBoolean("isFirstRun", true);
		if (isFirstRun) {
			SharedPreferences.Editor editor = sharedPreferences.edit();
			Log.d(TAG, "第一次运行");
			//   Toast.makeText(MainActivity.this, "第一次运行！", Toast.LENGTH_LONG).show();
			editor.putBoolean("isFirstRun", false);
			editor.commit();

		} else {
			Log.d(TAG, "不是第一次运行！");
			//   Toast.makeText(MainActivity.this, "不是第一次运行！", Toast.LENGTH_LONG).show();
		}

		return isFirstRun;
	}

	public static void checkfirstrunaday() {
		System.out.println("查询每天第一次");
		SharedPreferences sharedPreferences = context.getSharedPreferences("share", MODE_PRIVATE);
		String isFirstRunAday= sharedPreferences.getString("isFirstRunAday", null);
		Calendar c = Calendar.getInstance();//首先要获取日历对象
		int mYear = c.get(Calendar.YEAR); // 获取当前年份
		int mMonth = c.get(Calendar.MONTH) + 1;// 获取当前月份
		int mDay = c.get(Calendar.DAY_OF_MONTH);// 获取当日期
		mydate=(""+mYear+mMonth+mDay);
		if (isFirstRunAday==null||!isFirstRunAday.equals(mydate)) {
			count=0;
			System.out.println("赋值");
			mBluetoothLeService = DeviceControlService.mBluetoothLeService;
			if(mBluetoothLeService == null){
				return;
			}
			if (!mBluetoothLeService.initialize()) {//引用服务类的初始化
				return;
			}

			if(SampleGattAttributes.isinsetactivity){
				return;
			}
			if(SampleGattAttributes.connectedState)return;

			SampleGattAttributes.whichactivityconnect=true;//允许七天数据

			if(BleInfo.scanStaus == true)
				return;

			if (!mBluetoothLeService.connect(BleInfo.mDeviceAddress)) {
				;
			} else {
				SampleGattAttributes.connectedState=true;
//				Toast.makeText(context, "已经连接", Toast.LENGTH_LONG).show();
			}
			//注册广播接收器

		}
		else {

			SampleGattAttributes.whichactivityconnect=false;
			Log.d(TAG, "不是today第一次运行！");
			//   Toast.makeText(MainActivity.this, "不是第一次运行！", Toast.LENGTH_LONG).show();
		}
	}

	private void initView() {
		btnMore = (ImageButton) findViewById(R.id.btnMore);
		btnSport = (ImageButton) findViewById(R.id.btnSport);
		textNavbarTitle = (TextView) findViewById(R.id.textNavbarTitle);
		textNavbarTitle.setText(Users.sLoginUsername);
		btnAttention = (Button) findViewById(R.id.btnAttention);
		frame_content = (FrameLayout) findViewById(R.id.frame_content);
//		include_bottom = (LinearLayout) findViewById(R.id.include_bottom);
		ly_monitor = (LinearLayout) findViewById(R.id.ly_monitor);
		ly_sport = (LinearLayout) findViewById(R.id.ly_sport);
		ly_health = (LinearLayout) findViewById(R.id.ly_health);
		ly_msg = (LinearLayout) findViewById(R.id.ly_msg);
		btnMonitorTab = (Button) findViewById(R.id.btnMonitorTab);
		btnSportTab = (Button) findViewById(R.id.btnSportTab);
		btnHealthTab = (Button) findViewById(R.id.btnHealthTab);
		btnMsgTab = (Button) findViewById(R.id.btnMsgTab);
		tv_monitor = (TextView) findViewById(R.id.tv_monitor);
		tv_sport = (TextView) findViewById(R.id.tv_sport);
		tv_health = (TextView) findViewById(R.id.tv_health);
		tv_mag = (TextView) findViewById(R.id.tv_mag);

		btnMore.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Log.e("btnMore", "onClick:toggle ");
				toggle();
			}
		});
		btnAttention.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent();
				intent.setClass(getBaseContext(), BindActivity.class);
				startActivity(intent);
			}
		});
		btnSport.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent();
				intent.setClass(getBaseContext(),SportInfoActivity.class);
				startActivity(intent);

			}
		});
		ly_monitor.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				setSelect(btnMonitorTab,tv_monitor);
				initFragment(0);
			}
		});
		btnMonitorTab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				setSelect(btnMonitorTab, tv_monitor);
				initFragment(0);
			}
		});
		ly_sport.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				setSelect(btnSportTab, tv_sport);
				initFragment(1);
			}
		});
		btnSportTab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				setSelect(btnSportTab, tv_sport);
				initFragment(1);
			}
		});
		ly_health.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				setSelect(btnHealthTab, tv_health);
				initFragment(2);
			}
		});
		btnHealthTab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				setSelect(btnHealthTab, tv_health);
				initFragment(2);
			}
		});
		ly_msg.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				setSelect(btnMsgTab, tv_mag);
				initFragment(3);
			}
		});
		btnMsgTab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				setSelect(btnMsgTab, tv_mag);
				initFragment(3);
			}
		});
	}
	/**
	 * 初始化侧边栏
	 */
	private void initSlidingMenu(Bundle savedInstanceState) {
		// 如果保存的状态不为空则得到之前保存的Fragment，否则实例化MyFragment
		if (savedInstanceState != null) {
//			mContent = getSupportFragmentManager().getFragment(
//					savedInstanceState, "mContent");
		}

		// 设置左侧滑动菜单
		setBehindContentView(R.layout.menu_frame_left);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.menu_frame, new LeftFragment()).commitAllowingStateLoss();

		// 实例化滑动菜单对象
		SlidingMenu sm = getSlidingMenu();
		// 设置可以左右滑动的菜单
		sm.setMode(SlidingMenu.LEFT);
		// 设置滑动阴影的宽度
		sm.setShadowWidthRes(R.dimen.shadow_width);
		// 设置滑动菜单阴影的图像资源
		sm.setShadowDrawable(null);
		// 设置滑动菜单视图的宽度
		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		// 设置渐入渐出效果的值
		sm.setFadeDegree(0.35f);
		// 设置触摸屏幕的模式,这里设置为全屏
		sm.setTouchModeAbove(SlidingMenu.LEFT);
		// 设置下方视图的在滚动时的缩放比例
		sm.setBehindScrollScale(0.0f);

	}
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		//	getSupportFragmentManager().putFragment(outState, "mContent", mContent);
	}

	public void setSelect(Button button,TextView textView){
		if (select_btn != null){
			select_btn.setSelected(false);
			select_text.setTextColor(getResources().getColor(R.color.tab_n));
		}
		select_btn  = button;
		select_text  = textView;
		select_btn.setSelected(true);
		select_text.setTextColor(getResources().getColor(R.color.tab_o));
	}
	private void initFragment(int index) {
		// 由于是引用了V4包下的Fragment，所以这里的管理器要用getSupportFragmentManager获取
		FragmentManager fragmentManager = getSupportFragmentManager();
		// 开启事务
		FragmentTransaction transaction = fragmentManager.beginTransaction();
//        // 隐藏所有Fragment
//        hideFragment(transaction);
//        if (fragmentManager.getFragments() != null && fragmentManager.getFragments().size() > 0) {
//            for (Fragment cf : fragmentManager.getFragments()) {
////                if(cf instanceof FileDealActivity
////                        || cf instanceof MyhealthActivity
////                        || cf instanceof SearchDoctor
////                        || cf instanceof SearchMedicine
////                        || cf instanceof MedicineList)
//                    transaction.hide(cf);
//            }
//        }

		switch (index) {
			//实时监测
			case 0:
				btnSport.setVisibility(View.GONE);
				monitor = new MonitorFragment();
				transaction.replace(R.id.frame_content, monitor);
				break;
			//运动
			case 1:
				btnSport.setVisibility(View.VISIBLE);
				sport = new SportFragment();
				transaction.replace(R.id.frame_content, sport);
				break;
			//健康
			case 2:
				btnSport.setVisibility(View.GONE);
				health = new HealthFragment();
				transaction.replace(R.id.frame_content, health);
				break;
			//消息
			case 3:
				btnSport.setVisibility(View.GONE);
				msg = new MsgFragment();
				transaction.replace(R.id.frame_content, msg);
				break;
			default:
				break;
		}

		// 提交事务
		transaction.commit();

	}
	public boolean onKeyDown(int keyCode, KeyEvent event) {// 捕捉返回键

		if (keyCode == KeyEvent.KEYCODE_BACK) {


			if (flag == 0) {
//				showMessage("再按一次离开乐活旅行");
				Toast.makeText(getBaseContext(),"再按一次退出应用",Toast.LENGTH_SHORT).show();
				flag = 2;
			} else if (flag == 2) {
				// sendBroadcast(Preferences.BROADCAST_ACTION.FINISH);
				finish();
			}

			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	//service通知Activity扫描
	public static class MyBroadcastReceiver01 extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			//  Toast.makeText(context, "已经连接到设备", Toast.LENGTH_SHORT).show();
//			tv_toolbar_state.setText("已连接");
			checkfirstrunaday();
		}

	}
	public static class MyBroadcastReceiver02 extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			//  Toast.makeText(context, "已经连接到设备", Toast.LENGTH_SHORT).show();
//			tv_toolbar_state.setText("连接中");
			//iv_toolbar_refresh.setVisibility(View.VISIBLE);

		}
	}

	private static IntentFilter makeGattUpdateIntentFilter() {
		final IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
		intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
		intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
		intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
		intentFilter.addAction(BluetoothLeService.UpData);
		intentFilter.addAction(BluetoothLeService.ScanData);
		intentFilter.addAction(BluetoothLeService.ACTION_GATT_WIRTE_SUCCESS);
		return intentFilter;
	}

	private final BroadcastReceiver mGattUpdateReceiver01 = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			final String action = intent.getAction();
			//已经连接上BLE
			if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
			}
			//没有连接上BLE
			if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
				SampleGattAttributes.connectedState=false;

			}
			else if (BluetoothLeService.ScanData.equals(action)) {
				scanData = intent.getByteArrayExtra("scandata");
				getScanRecordBleData(scanData);
			}
			else if (BluetoothLeService.UpData.equals(action)) {
				scan = intent.getByteArrayExtra("data");
				getScanRecordBleDataAfterConnected(scan);
			}
			else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {


				if (intent.getStringExtra("DataType").equals("SevenDayData")) {
					count++;
					byte[] sevenData=intent.getByteArrayExtra("data");
					getNotifyBleData(sevenData);
				}

				if(count==7){
					firstDayInAWeek();
				}
			}
		}

	};

	private void firstDayInAWeek() {
		SampleGattAttributes.whichactivityconnect=false;
		SharedPreferences sharedPreferences = this.getSharedPreferences("share", MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString("isFirstRunAday", mydate);
		editor.commit();

		if(!SampleGattAttributes.isinsetactivity){
			mBluetoothLeService.disconnect();//不在设置界面就断开
			SampleGattAttributes.connectedState=false;        }
	}

	protected void onDestroy() {
		super.onDestroy();
		if(SampleGattAttributes.connectedState)
			mBluetoothLeService.disconnect();
		unregisterReceiver(mGattUpdateReceiver01);
		unbindService(serCon);//解除绑定，否则会报异常
	}
}
