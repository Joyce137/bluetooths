package com.example.ustc.newbluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ustc.newbluetooth.ble.BluetoothLeService;
import com.example.ustc.newbluetooth.ble.DeviceControlService;
import com.example.ustc.newbluetooth.ble.SampleGattAttributes;
import com.example.ustc.newbluetooth.ble.ScanBleActivity;
import com.example.ustc.newbluetooth.datas.CycleHeartRataData;
import com.example.ustc.newbluetooth.datas.Data;
import com.example.ustc.newbluetooth.datas.HeartRataData;
import com.example.ustc.newbluetooth.datas.NightHeartRataData;
import com.example.ustc.newbluetooth.datas.SportData;
import com.example.ustc.newbluetooth.datas.TimeData;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    //蓝牙相关
    private final static String TAG = MainActivity.class
            .getSimpleName();
    byte[] scan;
    public static String mDeviceName;
    public static String mDeviceAddress;
    byte[] sevenData, adjustDate;
    public static final String EXTRAS_DEVICE_NAME = "DEVICE_NAME";
    public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";
    //BLe控制服务
    private ServiceConnection serCon;
    public static boolean isBindServiceConnection = false;
    DeviceControlService devConService;
    private BluetoothAdapter mBluetoothAdapter;

    private static final int REQUEST_ENABLE_BT = 1;

    public static String heartRate = "0", step = "0", kcal = "0";
    private static int count;
    private static String mydate;

    static BluetoothLeService mBluetoothLeService ;

    Button button;
    public static Context context;
    private static TextView tv_toolbar_state;

    TextView steps,heartrate;
    public TextView data1_length,data1_time,data2_length,data2_day,data2_month,
            data2_data1,data2_data3,data2_data4,data2_data5,data2_data6,
            data3_length,data3_size,data3_data1,data3_data2,data3_data3,data3_data4,data3_data5,data3_data6,data3_data7,
            data4_length,data4_size,data4_data1,data4_data2,data4_data3,data4_data4,data4_data5,data4_data6,data4_data7,
            data5_length,data5_day,data5_month,data5_size,
            data5_data1,data5_data2,data5_data3,data5_data4,data5_data5,data5_data6,data5_data7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_notify_data);
        //判断是否支持ble
        context=this;
        initBle();
        registerReceiver(mGattUpdateReceiver01, makeGattUpdateIntentFilter());
        final Intent intent = getIntent();
        if (intent != null) {
            mDeviceName = intent.getStringExtra(EXTRAS_DEVICE_NAME);
            mDeviceAddress = intent.getStringExtra(EXTRAS_DEVICE_ADDRESS);
            scan = intent.getByteArrayExtra("scan");
            if (scan != null) {
                heartRate = String.valueOf(scan[19] & 0xff);
                step = String.valueOf((scan[26] & 0xff) * 256 + (scan[25] & 0xff));
                showData(scan);
            }
            Log.d(TAG, "EXTRAS_DEVICE_NAME:" + mDeviceName + " EXTRAS_DEVICE_ADDRESS:" + mDeviceAddress);
        }
        //绑定服务
        initService();

        // 初始化控件
        initView();

        button = (Button) findViewById(R.id.settingbtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,SettingActivity.class);
                startActivity(intent);
            }
        });

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
                    intDet.setClass(MainActivity.this, ScanBleActivity.class);
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
                        intDet.setClass(MainActivity.this, ScanBleActivity.class);
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
                mDeviceAddress = device_name;
            }
        }
    }

    public void initView(){
        steps = (TextView) findViewById(R.id.steps);
        heartrate = (TextView) findViewById(R.id.heartrate);
        steps.setText(step);
        heartrate.setText(heartRate);
        tv_toolbar_state = (TextView) findViewById(R.id.connect_status);
        data1_length = (TextView) findViewById(R.id.data1_length);
        data1_time = (TextView) findViewById(R.id.data1_time);
        data2_length = (TextView) findViewById(R.id.data2_length);
        data2_day = (TextView) findViewById(R.id.data2_day);
        data2_month = (TextView) findViewById(R.id.data2_month);
        data2_data1 = (TextView) findViewById(R.id.data2_data1);
        data2_data3 = (TextView) findViewById(R.id.data2_data3);
        data2_data4 = (TextView) findViewById(R.id.data2_data4);
        data2_data5 = (TextView) findViewById(R.id.data2_data5);
        data2_data6 = (TextView) findViewById(R.id.data2_data6);
        data3_length = (TextView) findViewById(R.id.data3_length);
        data3_size = (TextView) findViewById(R.id.data3_size);
        data3_data1 = (TextView) findViewById(R.id.data3_data1);
        data3_data2 = (TextView) findViewById(R.id.data3_data2);
        data3_data3 = (TextView) findViewById(R.id.data3_data3);
        data3_data4 = (TextView) findViewById(R.id.data3_data4);
        data3_data5 = (TextView) findViewById(R.id.data3_data5);
        data3_data6 = (TextView) findViewById(R.id.data3_data6);
        data3_data7 = (TextView) findViewById(R.id.data3_data7);
        data4_length = (TextView) findViewById(R.id.data4_length);
        data4_size = (TextView) findViewById(R.id.data4_size);
        data4_data1 = (TextView) findViewById(R.id.data4_data1);
        data4_data2 = (TextView) findViewById(R.id.data4_data2);
        data4_data3 = (TextView) findViewById(R.id.data4_data3);
        data4_data4 = (TextView) findViewById(R.id.data4_data4);
        data4_data5 = (TextView) findViewById(R.id.data4_data5);
        data4_data6 = (TextView) findViewById(R.id.data4_data6);
        data4_data7 = (TextView) findViewById(R.id.data4_data7);
        data5_length = (TextView) findViewById(R.id.data5_length);
        data5_day = (TextView) findViewById(R.id.data5_day);
        data5_month = (TextView) findViewById(R.id.data5_month);
        data5_size = (TextView) findViewById(R.id.data5_size);
        data5_data1 = (TextView) findViewById(R.id.data5_data1);
        data5_data2 = (TextView) findViewById(R.id.data5_data2);
        data5_data3 = (TextView) findViewById(R.id.data5_data3);
        data5_data4 = (TextView) findViewById(R.id.data5_data4);
        data5_data5 = (TextView) findViewById(R.id.data5_data5);
        data5_data6 = (TextView) findViewById(R.id.data5_data6);
        data5_data7 = (TextView) findViewById(R.id.data5_data7);
    }

    public void showData(byte[] dataBytes){
        switch (dataBytes[2]) {
            case 0x01:
                TimeData data1 = TimeData.toData(dataBytes);
                showTimeData(data1);
                break;
            case 0x02:
                SportData data2 = SportData.toData(dataBytes);
                showTimeData(data2);
                break;
            case 0x03:
                CycleHeartRataData data3 = CycleHeartRataData.toData(dataBytes);
                showTimeData(data3);
                break;
            case 0x04:
                NightHeartRataData data4 = NightHeartRataData.toData(dataBytes);
                showTimeData(data4);
                break;
            case 0x05:
                HeartRataData data5 = HeartRataData.toData(dataBytes);
                showTimeData(data5);
                break;
            default:
                break;
        }
    }
    public void showTimeData(TimeData data){
        data1_length.setText(data.length);
        String year = Utils.changeByteToString(data.year);

        data1_time.setText(year+" "+data.month + " "+data.day + " "+data.hour + " "+data.minite + " "+data.second);
    }

    public void showTimeData(SportData data){
        data2_length.setText(data.length);
        data2_day.setText(data.day);
        data2_month.setText(data.month);
        data2_data1.setText(Utils.changeByteToString(data.steps));
        data2_data3.setText(data.properSportTimes);
        data2_data4.setText(data.strongSportTimes);
        data2_data5.setText(data.sportStrength);
        data2_data6.setText(data.validSportTimes);
    }

    public void showTimeData(CycleHeartRataData data){
        data3_length.setText(data.length);
        data3_size.setText(data.size);
        data3_data1.setText(data.data1);
        data3_data2.setText(data.data2);
        data3_data3.setText(data.data3);
        data3_data4.setText(data.data4);
        data3_data5.setText(data.data5);
        data3_data6.setText(data.data6);
        data3_data7.setText(data.data7);
    }

    public void showTimeData(NightHeartRataData data){
        data4_length.setText(data.length);
        data4_size.setText(data.size);
        data4_data1.setText(data.data1);
        data4_data2.setText(data.data2);
        data4_data3.setText(data.data3);
        data4_data4.setText(data.data4);
        data4_data5.setText(data.data5);
        data4_data6.setText(data.data6);
        data4_data7.setText(data.data7);
    }

    public void showTimeData(HeartRataData data){
        data5_length.setText(data.length);
        data5_day.setText(data.day);
        data5_month.setText(data.month);
        data5_size.setText(data.size);
        data5_data1.setText(data.data1);
        data5_data2.setText(data.data2);
        data5_data3.setText(data.data3);
        data5_data4.setText(data.data4);
        data5_data5.setText(data.data5);
        data5_data6.setText(data.data6);
        data5_data7.setText(data.data7);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.scan:
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, ScanBleActivity.class);
                startActivity(intent);
                break;


        }
        return super.onOptionsItemSelected(item);
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

    private void a(){
        SampleGattAttributes.whichactivityconnect=false;
        SharedPreferences sharedPreferences = this.getSharedPreferences("share", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("isFirstRunAday", mydate);
        editor.commit();

        if(!SampleGattAttributes.isinsetactivity){
            mBluetoothLeService.disconnect();//不在设置界面就断开
            SampleGattAttributes.connectedState=false;        }
    }

    public static void  checkfirstrunaday() {
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
            if (!mBluetoothLeService.initialize()) {//引用服务类的初始化
                return;
            }

            if(SampleGattAttributes.isinsetactivity){
                return;
            }
            if(SampleGattAttributes.connectedState)return;

            SampleGattAttributes.whichactivityconnect=true;//允许七天数据

            if (!mBluetoothLeService.connect(MainActivity.mDeviceAddress)) {
                ;
            } else {SampleGattAttributes.connectedState=true;
                //Toast.makeText(MainActivity.this, "已经连接", Toast.LENGTH_LONG).show();

            }
            //注册广播接收器

        }
        else {

            SampleGattAttributes.whichactivityconnect=false;
            Log.d(TAG, "不是today第一次运行！");
            //   Toast.makeText(MainActivity.this, "不是第一次运行！", Toast.LENGTH_LONG).show();
        }
    }

    //service通知Activity扫描
    public static class MyBroadcastReceiver01 extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //  Toast.makeText(context, "已经连接到设备", Toast.LENGTH_SHORT).show();
            tv_toolbar_state.setText("已连接");
            checkfirstrunaday();
        }

    }
    public static class MyBroadcastReceiver02 extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //  Toast.makeText(context, "已经连接到设备", Toast.LENGTH_SHORT).show();
            tv_toolbar_state.setText("连接中");
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

            else if (BluetoothLeService.UpData.equals(action)) {
                scan = intent.getByteArrayExtra("data");
                //  System.out.print("~~~~");
                heartRate = String.valueOf(scan[19] & 0xff);
                step = String.valueOf((scan[26] & 0xff) * 256 + (scan[25] & 0xff));

                heartrate.setText(heartRate);
                steps.setText(step);
            }
            else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {


                if (intent.getStringExtra("DataType").equals("SevenDayData")) {
                    count++;
                    byte[] sevenData=intent.getByteArrayExtra("data");

                    heartRate = String.valueOf(sevenData[19] & 0xff);
                    step = String.valueOf((sevenData[26] & 0xff) * 256 + (sevenData[25] & 0xff));

                    heartrate.setText(heartRate);
                    steps.setText(step);

                    showData(sevenData);
                }

                if(count==7){a();

                    //   Toast.makeText(MainActivity.this, "第一次运行！", Toast.LENGTH_LONG).show();
                }
            }
        }

    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(SampleGattAttributes.connectedState)
            mBluetoothLeService.disconnect();
        unregisterReceiver(mGattUpdateReceiver01);
        unbindService(serCon);//解除绑定，否则会报异常
    }
}
