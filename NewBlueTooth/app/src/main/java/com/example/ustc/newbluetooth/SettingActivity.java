package com.example.ustc.newbluetooth;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ustc.newbluetooth.ble.BluetoothLeService;
import com.example.ustc.newbluetooth.ble.DeviceControlService;
import com.example.ustc.newbluetooth.ble.SampleGattAttributes;

import java.util.Calendar;
import java.util.UUID;

/**
 * Created by CaoRuijuan on 5/17/16.
 */
public class SettingActivity extends AppCompatActivity {
    private Context context;
    String newValue = "";

    private static final String TAG = "SettingActivity";
    LinearLayout ly_setting_testxinlv, ly_setting_stepstest, ly_setting_autoheartratetest, ly_setting_nightheartratetest,
            ll_broadcastjiange, ll_broadcasttime, ll_guangbodatatime, ll_dianliang, ll_cycleheartratetesttimes,
            ll_broadcastcalltime, ll_time, ll_sendpower, ll_heartratemax, ll_heartratemin, ll_heartrateqvalue,
            ll_extime, ll_breadfasttime, ll_lunchtime, ll_dinnertime, ll_nighttesttime, ll_autoheartrateteststeps, ll_lcd;
    SwitchCompat sc_setting_testxinlv, sc_setting_stepstest, sc_setting_autoheartratetest, sc_setting_nightheartratetest;
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

    private Handler mHandler = new Handler();
    Runnable runnable;

    BluetoothLeService mBluetoothLeService = DeviceControlService.mBluetoothLeService;

    byte[] p = new byte[4];//这个用来int转换byte的暂存数组
    final byte[] test = new byte[2];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_setting);
        context = this.getApplicationContext();
        initView();
    }

    public void initView(){
        reconnect = (TextView)findViewById(R.id.reconnect);
        broadcastjiange = (TextView)findViewById(R.id.broadcastjiange);
        broadcasttime = (TextView)findViewById(R.id.broadcasttime);
        guangbodatatime = (TextView)findViewById(R.id.guangbodatatime);
        dianliang = (TextView)findViewById(R.id.dianliang);
        cycleheartratetesttimes = (TextView)findViewById(R.id.cycleheartratetesttimes);
        broadcastcalltime = (TextView)findViewById(R.id.broadcastcalltime);
        time = (TextView)findViewById(R.id.time);
        sendpower = (TextView)findViewById(R.id.sendpower);
        heartratemax = (TextView)findViewById(R.id.heartratemax);
        heartratemin = (TextView)findViewById(R.id.heartratemin);
        heartrateqvalue = (TextView)findViewById(R.id.heartrateqvalue);
        extime = (TextView)findViewById(R.id.extime);
        breadfasttime = (TextView)findViewById(R.id.breadfasttime);
        lunchtime = (TextView)findViewById(R.id.lunchtime);
        dinnertime = (TextView)findViewById(R.id.dinnertime);
        nighttesttime = (TextView)findViewById(R.id.nighttesttime);
        autoheartrateteststeps = (TextView)findViewById(R.id.autoheartrateteststeps);
        lcd = (TextView)findViewById(R.id.lcd);

        //注册广播接收器
        registerReceiver(mGattUpdateReceiver01, makeGattUpdateIntentFilter());
        runnable = new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                //要做的事情
                System.out.println("判断中");
                if(SampleGattAttributes.lastrecall.compareTo(SampleGattAttributes.thisrecall)==0&&SampleGattAttributes.connectedState){mBluetoothLeService.disconnect();}//
                else SampleGattAttributes.lastrecall=SampleGattAttributes.thisrecall;
                mHandler.postDelayed(this, 30000);
            }
        };
        reconnect.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!SampleGattAttributes.connectedState) {
                    if (!mBluetoothLeService.connect(MainActivity.mDeviceAddress)) {
                        Toast.makeText(SettingActivity.this, "重连失败", Toast.LENGTH_LONG).show();
                    }}
                else{
                    SampleGattAttributes.connectedState=true;
                    reconnect.setVisibility(View.GONE);
                    Toast.makeText(SettingActivity.this, "重连成功", Toast.LENGTH_LONG).show();
                    if(SampleGattAttributes.isinsetactivity)
                        mHandler.postDelayed(runnable, 30000);
                }
            }
        }) ;

        ly_setting_testxinlv = (LinearLayout) findViewById(R.id.ly_setting_testxinlv);
        ly_setting_testxinlv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
            }
        });
        sc_setting_testxinlv = (SwitchCompat) findViewById(R.id.sc_settin_testxinlv);
        sc_setting_testxinlv.setSwitchPadding(40);
        sc_setting_testxinlv.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                xinlv(isChecked);
            }
        });

        if (SampleGattAttributes.xinlv_checkflag==false) {
            sc_setting_testxinlv.setChecked(false);
            System.out.print(SampleGattAttributes.xinlv_checkflag);
        }
        else{
            sc_setting_testxinlv.setChecked(true);
            System.out.print(SampleGattAttributes.xinlv_checkflag);
        }

        if (!mBluetoothLeService.initialize()) {//引用服务类的初始化
            Toast.makeText(SettingActivity.this, "未初始化", Toast.LENGTH_LONG).show();
        }
        if(!SampleGattAttributes.connectedState) {
            if (!mBluetoothLeService.connect(MainActivity.mDeviceAddress)) {
                Toast.makeText(SettingActivity.this, "未连接上", Toast.LENGTH_LONG).show();
                reconnect.setVisibility(View.VISIBLE);
                System.out.println("断线重连显示");
            }
            else {
                Toast.makeText(SettingActivity.this, "已经连接", Toast.LENGTH_LONG).show();
            }
        }
        else {
            Toast.makeText(SettingActivity.this, "已经连接", Toast.LENGTH_LONG).show();
        }


        ly_setting_stepstest = (LinearLayout) findViewById(R.id.ly_setting_stepstest);
        ly_setting_stepstest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
            }
        });
        sc_setting_stepstest = (SwitchCompat) findViewById(R.id.sc_setting_stepstest);
        sc_setting_stepstest.setSwitchPadding(40);
        sc_setting_stepstest.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                stepstest(isChecked);
            }
        });

        ly_setting_autoheartratetest = (LinearLayout) findViewById(R.id.ly_setting_autoheartratetest);
        ly_setting_autoheartratetest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
            }
        });
        sc_setting_autoheartratetest = (SwitchCompat) findViewById(R.id.sc_setting_autoheartratetest);
        sc_setting_autoheartratetest.setSwitchPadding(40);
        sc_setting_autoheartratetest.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                autoheartratetest(isChecked);
            }
        });

        ly_setting_nightheartratetest = (LinearLayout) findViewById(R.id.ly_setting_nightheartratetest);
        ly_setting_nightheartratetest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
            }
        });
        sc_setting_nightheartratetest = (SwitchCompat) findViewById(R.id.sc_setting_nightheartratetest);
        sc_setting_nightheartratetest.setSwitchPadding(40);
        sc_setting_nightheartratetest.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                nightheartratetest(isChecked);
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
        ll_dianliang = (LinearLayout) findViewById(R.id.ll_dianliang);
        ll_dianliang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dianliang();
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
        ll_time = (LinearLayout) findViewById(R.id.ll_time);
        ll_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time();
            }
        });
        ll_sendpower = (LinearLayout) findViewById(R.id.ll_sendpower);
        ll_sendpower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
            sc_setting_testxinlv.setChecked(false);
            mBluetoothLeService.writeMessage(UUID.fromString(SampleGattAttributes.CHARACTERISTIC_NOTIFY), test);
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
            sc_setting_testxinlv.setChecked(true);
            mBluetoothLeService.writeMessage(UUID.fromString(SampleGattAttributes.CHARACTERISTIC_NOTIFY), test);
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
            sc_setting_stepstest.setChecked(false);
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
            sc_setting_stepstest.setChecked(true);
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
            sc_setting_autoheartratetest.setChecked(false);
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
            sc_setting_autoheartratetest.setChecked(true);
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
            sc_setting_nightheartratetest.setChecked(false);
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
            sc_setting_nightheartratetest.setChecked(true);
            mBluetoothLeService.writeMessage(UUID.fromString(SampleGattAttributes.CHARACTERISTIC_NOTIFY), test);
        }
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
                p = Utils.shortToLH(value);
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
                p = Utils.shortToLH(value);
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
                p = Utils.shortToLH(value);
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

    //获取电量
    private void dianliang() {
        istestxinlv = false;
        mBluetoothLeService.readMessage(UUID.fromString(SampleGattAttributes.POWER));//
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
                p = Utils.shortToLH(value);
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
                p = Utils.shortToLH(value);
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

    //同步时间
    private void time() {
        istestxinlv = false;
        Calendar c = Calendar.getInstance();//首先要获取日历对象
        int mYear = c.get(Calendar.YEAR); // 获取当前年份
        int mMonth = c.get(Calendar.MONTH) + 1;// 获取当前月份
        int mDay = c.get(Calendar.DAY_OF_MONTH);// 获取当日期
        //int mWay = c.get(Calendar.DAY_OF_WEEK);// 获取当前日期的星期
        int mHour = c.get(Calendar.HOUR_OF_DAY);//时
        int mMinute = c.get(Calendar.MINUTE);
        int msecond = c.get(Calendar.SECOND);
        p = Utils.shortToLH(mYear);
        System.out.println("年" + mYear + "月份" + mMonth + "时" + mHour + "分" + mMinute + "秒" + msecond);
        byte[] data = {(byte) msecond, (byte) mMinute, (byte) mHour, (byte) mDay,
                (byte) mMonth, p[0], p[1]};
        mBluetoothLeService.writeMessage(UUID.fromString(SampleGattAttributes.TIMEADJUSTRW), data);//
        Toast.makeText(SettingActivity.this, "时间同步", Toast.LENGTH_LONG).show();
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
                p = Utils.shortToLH(value);
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
                p = Utils.shortToLH(value);
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
                p = Utils.shortToLH(value);
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
                p = Utils.shortToLH(value);
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
                p = Utils.shortToLH(value);
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
                p = Utils.shortToLH(value);
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
                p = Utils.shortToLH(value);
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
                p = Utils.shortToLH(value);
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
                p = Utils.shortToLH(value);
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
        p = Utils.shortToLH(100);
        mBluetoothLeService.writeMessage(UUID.fromString(SampleGattAttributes.lcdControl), p);////写入
    }

    public void close(View view){
        istestxinlv_close = true;
        istestxinlv_open = false;
        test[1] = 0x55;
        test[0] = 0x02;
        sc_setting_autoheartratetest.setChecked(true);
        mBluetoothLeService.writeMessage(UUID.fromString(SampleGattAttributes.CHARACTERISTIC_NOTIFY), test);
    };

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
                reconnect.setVisibility(View.GONE);//如果连接成功了，重连按键消失
//                autodisconnect();
                if(SampleGattAttributes.isinsetactivity)
                    mHandler.postDelayed(runnable, 30000);
            }
            //没有连接上BLE
            else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                reconnect.setVisibility(View.VISIBLE);
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
                        Toast.makeText(SettingActivity.this, "打开成功！", Toast.LENGTH_LONG).show();
                        SampleGattAttributes.xinlv_checkflag=true;

                        xinlv_checkflag2=true;//告诉listener不是人操作
                        sc_setting_testxinlv.setChecked(true);
                    }

                    if (istestxinlv_close) {
                        Toast.makeText(SettingActivity.this, "关闭成功！", Toast.LENGTH_LONG).show();
                        SampleGattAttributes.xinlv_checkflag=false;
                        xinlv_checkflag2=true;
                        sc_setting_testxinlv.setChecked(false);
                    }
                }
                if (isstepstest) {
                    if (isstepstest_open) {
                        Toast.makeText(SettingActivity.this, "打开成功！", Toast.LENGTH_LONG).show();
                        SampleGattAttributes.stepstest_checkflag=true;

                        stepstest_checkflag2=true;//告诉listener不是人操作
                        sc_setting_stepstest.setChecked(true);
                    }

                    if (isstepstest_close) {
                        Toast.makeText(SettingActivity.this, "关闭成功！", Toast.LENGTH_LONG).show();
                        SampleGattAttributes.stepstest_checkflag=false;
                        stepstest_checkflag2=true;
                        sc_setting_stepstest.setChecked(false);
                    }
                }
                if (isautoheartratetest) {
                    if (isautoheartratetest_open) {
                        Toast.makeText(SettingActivity.this, "打开成功！", Toast.LENGTH_LONG).show();
                        SampleGattAttributes.autoheartratetest_checkflag=true;

                        autoheartratetest_checkflag2=true;//告诉listener不是人操作
                        sc_setting_autoheartratetest.setChecked(true);
                    }

                    if (isautoheartratetest_close) {
                        Toast.makeText(SettingActivity.this, "关闭成功！", Toast.LENGTH_LONG).show();
                        SampleGattAttributes.autoheartratetest_checkflag=false;
                        autoheartratetest_checkflag2=true;
                        sc_setting_autoheartratetest.setChecked(false);
                    }
                }
                if (isnightheartratetest) {
                    if (isnightheartratetest_open) {
                        Toast.makeText(SettingActivity.this, "打开成功！", Toast.LENGTH_LONG).show();
                        SampleGattAttributes.nightheartratetest_checkflag=true;

                        nightheartratetest_checkflag2=true;//告诉listener不是人操作
                        sc_setting_nightheartratetest.setChecked(true);
                    }

                    if (isnightheartratetest_close) {
                        Toast.makeText(SettingActivity.this, "关闭成功！", Toast.LENGTH_LONG).show();
                        SampleGattAttributes.nightheartratetest_checkflag=false;
                        nightheartratetest_checkflag2=true;
                        sc_setting_nightheartratetest.setChecked(false);
                    }
                }
            }
            else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {

                if (intent.getStringExtra("DataType").equals("SevenDayData")) {
                    byte[] dataBytes = intent.getByteArrayExtra("data");
                    String powerStr = dataBytes.length+"%";
                    dianliang.setText(powerStr);
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
                    dianliang.setText(powerStr);
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

    protected void onDestroy() {
        super.onDestroy();

        SampleGattAttributes.isinsetactivity=false;//出去了
        mBluetoothLeService.disconnect();
        SampleGattAttributes.connectedState=false;
        mHandler.removeCallbacks(runnable);
        unregisterReceiver(mGattUpdateReceiver01);
        istestxinlv = false;
        istestxinlv_close = false;
        istestxinlv_open = false;
    }

}
