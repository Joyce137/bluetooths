package com.health.app.ble;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.ScanRecord;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.health.app.database.impl.ScanBleDataDaoImpl;
import com.health.app.statics.BleInfo;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * Created by CaoRuijuan on 6/11/16.
 */
public class BluetoothLeService extends Service{
    private final static String TAG = BluetoothLeService.class.getSimpleName();

    private Handler mTimerHandler = new Handler();
    private boolean mTimerEnabled = false;
    private BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothDevice  mBluetoothDevice = null;
    private String mBluetoothDeviceAddress;
    private BluetoothGatt mBluetoothGatt;
    private BluetoothGattService mBluetoothSelectedService = null;
    private List<BluetoothGattService> mBluetoothGattServices = null;

    private int mConnectionState = STATE_DISCONNECTED;
    private boolean mConnected = false;
    /* defines (in milliseconds) how often RSSI should be updated */
    private static final int RSSI_UPDATE_TIME_INTERVAL = 1500; // 1.5 seconds

    private static final int STATE_DISCONNECTED = 0;
    private static final int STATE_CONNECTING = 1;
    private static final int STATE_CONNECTED = 2;

    public final static String ACTION_GATT_CONNECTED = "com.health.app.ble.ACTION_GATT_CONNECTED";
    public final static String ACTION_GATT_DISCONNECTED = "com.health.app.ble.ACTION_GATT_DISCONNECTED";
    public final static String ACTION_GATT_SERVICES_DISCOVERED = "com.health.app.ble.ACTION_GATT_SERVICES_DISCOVERED";
    public final static String ACTION_DATA_AVAILABLE = "com.health.app.ble.ACTION_DATA_AVAILABLE";
    public final static String UpData = "com.health.app.ble.UpData";
    public final static String ScanData = "com.health.app.ble.ScanData";

    public final static String ACTION_GATT_WIRTE_SUCCESS= "com.health.app.ble.ACTION_GATT_WIRTE_SUCCESS";
    //下面几个是默示连接用的
    private static final long SCAN_PERIOD = 9000;
    private HashMap<String, byte[]> scanrecordsMap = new HashMap<String, byte[]>();
    //private BluetoothAdapter mBluetoothAdapter;//上面有了
    private Handler mHandler = new Handler();
    //private Handler m_Handler=new Handler();
    // static public boolean isSAME = false;

    //private static final int REQUEST_ENABLE_BT = 1;

    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            String intentAction;
            System.out.println("=======status:" + status);
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                mConnected = true;
                intentAction = ACTION_GATT_CONNECTED;
                mConnectionState = STATE_CONNECTED;
                broadcastUpdate(intentAction);
                Log.i(TAG, "Connected to GATT server.");
                // Attempts to discover services after successful connection.
                Log.i(TAG, "Attempting to start service discovery:");

                mBluetoothGatt.readRemoteRssi();

                //call for services discovery
                startServicesDiscovery();

                //get RSSI value to be updated periodically
                startMonitoringRssiValue();
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                mConnected = false;
                intentAction = ACTION_GATT_DISCONNECTED;
                mConnectionState = STATE_DISCONNECTED;
                Log.i(TAG, "Disconnected from GATT server.");
                broadcastUpdate(intentAction);
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                SampleGattAttributes.notify(mBluetoothGatt, UUID.fromString(SampleGattAttributes.CHARACTERISTIC_NOTIFY));
//                if(SampleGattAttributes.whichactivityconnect) {
//                    SampleGattAttributes.notify(gatt, UUID.fromString(SampleGattAttributes.CHARACTERISTIC_NOTIFY));//启动等待notify监听
//                }
                broadcastUpdate(ACTION_GATT_SERVICES_DISCOVERED);
                getSupportedServices();

            } else {
                Log.w(TAG, "onServicesDiscovered received: " + status);
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            System.out.println("onCharacteristicRead");
            if (status == BluetoothGatt.GATT_SUCCESS) {
                broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
            }
        }

        /**@Override public void onDescriptorWrite(BluetoothGatt gatt,
        BluetoothGattDescriptor descriptor, int status) {
        System.out.println("onDescriptorWriteonDescriptorWrite = " + status
        + ", descriptor =" + descriptor.getUuid().toString());
        }*/
        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
            if (characteristic.getValue() != null) {
                System.out.println(characteristic.getStringValue(0));
            }
            System.out.println("--------onCharacteristicChanged-----");
        }

        @Override
        public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
            System.out.println("rssi = " + rssi);
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt,
                                          BluetoothGattCharacteristic characteristic, int status) {
            System.out.println("--------write success----- status:" + status);
            broadcastUpdate(ACTION_GATT_WIRTE_SUCCESS);
        };
    };

    private void broadcastUpdate(final String action) {
        final Intent intent = new Intent(action);
        sendBroadcast(intent);
    }

    private void broadcastScanData(final String action, byte[] updata) {
        final Intent intent = new Intent(action);
        intent.putExtra("scandata", updata);
        sendBroadcast(intent);
        System.out.println("bofang");
    }

    private void broadcastUpdate(final String action, byte[] updata) {
        final Intent intent = new Intent(action);
        intent.putExtra("data", updata);
        sendBroadcast(intent);
        System.out.println("bofang");
    }

    private void broadcastUpdate(final String action,
                                 final BluetoothGattCharacteristic characteristic) {
        final Intent intent = new Intent(action);
        int[] value = new int[6];
        int[] format = new int[3];
        format[1] = BluetoothGattCharacteristic.FORMAT_UINT8;
        format[2] = BluetoothGattCharacteristic.FORMAT_UINT16;
        final UUID uuid, temp;
        {
            uuid = characteristic.getUuid();
            if (uuid.equals(UUID.fromString(SampleGattAttributes.CHARACTERISTIC_NOTIFY))) {
                intent.putExtra("DataType", "SevenDayData");
                intent.putExtra("data", characteristic.getValue());
            }
            //广播间隔
            if (uuid.equals(UUID.fromString(SampleGattAttributes.broadcastperiodRW))) {
                intent.putExtra("DataType", "broadcastperiod");

                intent.putExtra("data", characteristic.getValue());
            }
            //广播时间
            if (uuid.equals(UUID.fromString(SampleGattAttributes.broadcastTimeRW))) {
                intent.putExtra("DataType", "broadcastTime");

                intent.putExtra("data", characteristic.getValue());
            }
            //广播数据时间
            if (uuid.equals(UUID.fromString(SampleGattAttributes.BROADCASTLASTRW))) {
                intent.putExtra("DataType", "broadcastdatatime");

                intent.putExtra("data", characteristic.getValue());
            }
            //周期性心率测试次数
            if (uuid.equals(UUID.fromString(SampleGattAttributes.HEARTRATEPERIODRW))) {
                intent.putExtra("DataType", "CollectPeriod");

                intent.putExtra("data", characteristic.getValue());
            }
            //电量
            if (uuid.equals(UUID.fromString(SampleGattAttributes.POWER))) {
                intent.putExtra("DataType", "Power");
                intent.putExtra("data", characteristic.getValue());
            }
            //同步时间
            if (uuid.equals(UUID.fromString(SampleGattAttributes.TIMEADJUSTRW))) {//格式为：秒（1Byte）+分(1Byte)+ 时(1Byte)+ 日(1Byte)+月(Byte)+年(2Bytes)
                intent.putExtra("DataType", "TimeAdjust");

                intent.putExtra("data", characteristic.getValue());
            }
            //广播呼叫持续时间
            if (uuid.equals(UUID.fromString(SampleGattAttributes.SOSRW))) {
                intent.putExtra("DataType", "SOSTime");

                intent.putExtra("data", characteristic.getValue());
            }
            //发射功率
            if (uuid.equals(UUID.fromString(SampleGattAttributes.sendPowerRW))) {
                intent.putExtra("DataType", "sendPower");

                intent.putExtra("data", characteristic.getValue());
            }
            //心率上下限和Q值
            if (uuid.equals(UUID.fromString(SampleGattAttributes.heartRateMaxMinQ))) {
                intent.putExtra("DataType", "heartRateMaxMinQ");

                intent.putExtra("data", characteristic.getValue());
            }
            //晨练和用餐时间
            if (uuid.equals(UUID.fromString(SampleGattAttributes.sportMealTime))) {
                intent.putExtra("DataType", "sportMealTime");

                intent.putExtra("data", characteristic.getValue());
            }
            //夜间检测时间
            if (uuid.equals(UUID.fromString(SampleGattAttributes.nightCheckTime))) {
                intent.putExtra("DataType", "nightCheckTime");

                intent.putExtra("data", characteristic.getValue());
            }
            //自动心率检测步数
            if (uuid.equals(UUID.fromString(SampleGattAttributes.autoCheckHeartTimeSteps))) {
                intent.putExtra("DataType", "autoCheckHeartTimeSteps");

                intent.putExtra("data", characteristic.getValue());
            }
            //LCD刷屏
            if (uuid.equals(UUID.fromString(SampleGattAttributes.lcdControl))) {
                intent.putExtra("DataType", "lcdControl");

                intent.putExtra("data", characteristic.getValue());
            }
        }

        sendBroadcast(intent);
    }

    public void notify8001() {
        if (mBluetoothAdapter == null || mBluetoothGatt == null)
            return;
        SampleGattAttributes.notify(mBluetoothGatt, UUID.fromString(SampleGattAttributes.CHARACTERISTIC_NOTIFY));
    }

    /**
     * Intent intent = getIntent();
     * String pose_name = intent.getStringExtra("pose_name");
     * String img_file_name = intent.getStringExtra("img_file_name");
     * Bundle b=this.getIntent().getExtras();
     * pose_title = b.getStringArray("pose_title");
     */

    public class LocalBinder extends Binder {
        BluetoothLeService getService() {
            return BluetoothLeService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        // After using a given device, you should make sure that
        // BluetoothGatt.close() is called
        // such that resources are cleaned up properly. In this particular
        // example, close() is
        // invoked when the UI is disconnected from the Service.
        close();
        return super.onUnbind(intent);
    }

    private final IBinder mBinder = new LocalBinder();

    /**
     * Initializes a reference to the local Bluetooth adapter.
     *
     * @return Return true if the initialization is successful.
     */
    public boolean initialize() {
        //判断是否支持蓝牙，并打开蓝牙
        if (mBluetoothManager == null) {
            mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
            if (mBluetoothManager == null) {
                Log.e(TAG, "Unable to initialize BluetoothManager.");
                return false;
            }
        }

        if(mBluetoothAdapter == null)
            mBluetoothAdapter = mBluetoothManager.getAdapter();
        if (mBluetoothAdapter == null) {
            Log.e(TAG, "Unable to obtain a BluetoothAdapter.");
            return false;
        }

        return true;
    }

    /**
     * Connects to the GATT server hosted on the Bluetooth LE device.
     *
     * @param address The device address of the destination device.
     *                <p/>
     *                * @return Return true if the connection is initiated successfully. The
     *                connection result is reported asynchronously through the
     *                {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
     *                callback.
     */
    public boolean connect(final String address) {
        if (mBluetoothAdapter == null || address == null) {
            Log.w(TAG, "BluetoothAdapter not initialized or unspecified address.");
            return false;
        }

        // Previously connected device. Try to reconnect. (先前连接的设备。 尝试重新连接)
        final BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);//可能蓝牙啥的掉了
        if (device == null) {
            Log.w(TAG, "Device not found.  Unable to connect.");
            return false;
        }

        if(mBluetoothGatt != null && mBluetoothGatt.getDevice().getAddress().equals(address))
        {
            return mBluetoothGatt.connect();
        }
        else {
            mBluetoothDevice = mBluetoothAdapter.getRemoteDevice(address);
            if(mBluetoothDevice == null){
                return false;
            }
            mBluetoothGatt = device.connectGatt(this, false, mGattCallback);//真正连接代码
            Log.d(TAG, "Trying to create a new connection.");
            mBluetoothDeviceAddress = address;
            mConnectionState = STATE_CONNECTING;
        }

        return true;
    }

    /**
     * Disconnects an existing connection or cancel a pending connection. The
     * disconnection result is reported asynchronously through the
     * {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
     * callback.
     */
    public void disconnect() {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.disconnect();


    }

    /**
     * After using a given BLE device, the app must call this method to ensure
     * resources are released properly.
     */
    public void close() {//因该是自动处理函数
        if (mBluetoothGatt == null) {
            return;
        }
        mBluetoothGatt.close();
        mBluetoothGatt = null;
    }

    /**
     * 请求新的RSSI值用于连接
     */
    public void readPeriodicalyRssiValue(final boolean repeat) {
        mTimerEnabled = repeat;
        // check if we should stop checking RSSI value
        if(mConnected == false || mBluetoothGatt == null || mTimerEnabled == false) {
            mTimerEnabled = false;
            return;
        }

        mTimerHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mBluetoothGatt == null ||
                        mBluetoothAdapter == null ||
                        mConnected == false) {
                    mTimerEnabled = false;
                    return;
                }

                // request RSSI value
                mBluetoothGatt.readRemoteRssi();
                // add call it once more in the future
                readPeriodicalyRssiValue(mTimerEnabled);
            }
        }, RSSI_UPDATE_TIME_INTERVAL);
    }


    /* starts monitoring RSSI value */
    public void startMonitoringRssiValue() {
        readPeriodicalyRssiValue(true);
    }

    /* stops monitoring of RSSI value */
    public void stopMonitoringRssiValue() {
        readPeriodicalyRssiValue(false);
    }

    /* request to discover all services available on the remote devices
     * results are delivered through callback object */
    public void startServicesDiscovery() {
        if(mBluetoothGatt != null) mBluetoothGatt.discoverServices();
    }


    public void writeCharacteristic(BluetoothGattCharacteristic characteristic) {

        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }

        mBluetoothGatt.writeCharacteristic(characteristic);//真正回调的原因

    }

    /**
     * Request a read on a given {@code BluetoothGattCharacteristic}. The read
     * result is reported asynchronously through the
     * {@code BluetoothGattCallback#onCharacteristicRead(android.bluetooth.BluetoothGatt, android.bluetooth.BluetoothGattCharacteristic, int)}
     * callback.
     *
     * @param
     *///系统调用
    public List<BluetoothGattService> getSupportedServices() {
        if(mBluetoothGattServices != null && mBluetoothGattServices.size() > 0)
            mBluetoothGattServices.clear();

        if (mBluetoothGatt == null)
            return null;

        mBluetoothGattServices = mBluetoothGatt.getServices();
        return mBluetoothGattServices;
    }

    public void getCharacteristicsForService(final BluetoothGattService service) {
        if(service == null) return;
        List<BluetoothGattCharacteristic> chars = null;

        chars = service.getCharacteristics();
        // keep reference to the last selected service
        mBluetoothSelectedService = service;
    }


    public boolean readMessage(UUID uuid) {
        return SampleGattAttributes.readMessage(mBluetoothGatt, uuid);
    }

    public boolean writeMessage(UUID uuid, byte[] data) {
        System.out.println("--------try to write  in sevice----- ");
        return SampleGattAttributes.sendMessage(mBluetoothGatt, data, uuid);
    }


    public void weilianjie(){
        Intent intent = new Intent("com.health.app.ble.MY_BROADCAST02");
        sendBroadcast(intent);

    }
    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {

        @Override
        public void onLeScan(final BluetoothDevice device, int rssi, final byte[] scanRecord) {
            //先存储
            BleInfo.mCurScanAddress = device.getAddress();
            BleInfo.mCurScanRecord = scanRecord;
            broadcastScanData(ScanData, scanRecord);

            Log.e(TAG, "onLeScan");
            SharedPreferences sharedPreferences = getApplication().getSharedPreferences("share", MODE_PRIVATE);
            String device_name = sharedPreferences.getString("DEVICE_NAME", "FFFFFFFFFFF");

            //当前扫描设备
            if (device.getAddress().equals(device_name)) {
                scanLeDevice(false);
                //Toast显示已经扫描到设备
                Intent intent = new Intent("com.health.app.ble.MY_BROADCAST01");
                sendBroadcast(intent);
                Log.e(TAG, "scanLeDevice(false)");
                if (!(Arrays.equals(scanrecordsMap.get(BleInfo.mDeviceAddress), scanRecord))) {
                    //Log.e(TAG, MainActivity.mDeviceAddress);
                    //int i;if((scanrecordsMap.get(DeviceControlActivity.mDeviceAddress)!=null))for(i=0;i<=61;i++)Log.e(TAG,Integer.toHexString(scanRecord[i] & 0x000000ff) + ""+Integer.toHexString(scanrecordsMap.get(DeviceControlActivity.mDeviceAddress)[i] & 0x000000ff));    // Sets up UI references.
                    scanrecordsMap.put(device.getAddress(), scanRecord);
                    broadcastUpdate(UpData, scanrecordsMap.get(BleInfo.mDeviceAddress));
                }
            }

        }

    };

    public void scanLeDevice(final boolean enable) {
       /* Intent intent = new Intent("com.example.administrator.ble.MY_BROADCAST");
        sendBroadcast(intent);*/
        Log.e(TAG, "scanLeDevice(final boolean enable)");
        if (enable) {
            // Stops scanning after a pre-defined scan period.
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                }
            }, SCAN_PERIOD);//after a period it will run only itself
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        } else {
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        stopSelf();
    }
}
