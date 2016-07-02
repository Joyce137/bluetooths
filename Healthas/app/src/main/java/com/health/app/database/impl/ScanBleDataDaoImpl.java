package com.health.app.database.impl;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.health.app.database.DBConstants;
import com.health.app.database.entity.ScanBleDataEntity;
import com.health.app.database.support.DaoSupportImpl;
import com.health.app.statics.AppPath;
import com.health.app.statics.Util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Created by CaoRuijuan on 1/27/16.
 */
public class ScanBleDataDaoImpl extends DaoSupportImpl<ScanBleDataEntity> {
    private Context context;
    private final static String TAG = ScanBleDataDaoImpl.class.getSimpleName();
//    private String userName;

    public ScanBleDataDaoImpl(Context context){
        super(context, "ble", false);
        this.context = context;
        //新建username_bledata表
        try {
            String createSqlStr = DBConstants.BLE_DB_CREATE;
            getDb().execSQL(createSqlStr);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public boolean insertBleData(String address, String battery, String broken_state,
                                 String heartrate, String sport_level, String stepnum,
                                 String tx_power, String sos_state) {
        ScanBleDataEntity scanBleDataEntity = new ScanBleDataEntity();
        scanBleDataEntity.address = address;
        scanBleDataEntity.battery = battery;
        scanBleDataEntity.stepnum = stepnum;
        scanBleDataEntity.heartrate = heartrate;
        scanBleDataEntity.broken_state = broken_state;
        scanBleDataEntity.sport_level = sport_level;
        scanBleDataEntity.tx_power = tx_power;
        scanBleDataEntity.sos_state = sos_state;

        //插入数据
        if(checkDateTimeExistInBledate(Util.getDataAndTime())){
            return false;
        }
        return insert(scanBleDataEntity);
    }

    //判断某个datetime是否在bledata表中
    public boolean checkDateTimeExistInBledate(String datetime){
        String selector = DBConstants.BLE_DATATIME + "=?";
        String[] selectorargs = new String[]{datetime.trim()};

        if(findEntity(selector,selectorargs).size() == 0){
            return false;
        }
        return true;
    }

    //查询now之前的蓝牙数据
    public List<ScanBleDataEntity> queryBleData(){
        String now = Util.getDataAndTime();
        String selector = DBConstants.BLE_DATATIME + "<?";
        String[] selectorargs = new String[]{now.trim()};

        return findEntity(selector,selectorargs);
    }

    //存储蓝牙数据到txt文件
    public boolean saveScanBleDataToFile(List<ScanBleDataEntity> scanBleDataEntities){
        String filePath = AppPath.getPathByFileType("scanBleData");
        AppPath.CheckAndMkdirPathExist(filePath);
        filePath += "/scanBleData.txt";
        File file = new File(filePath);
        //如果不存在，则创建
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.out.println("创建文件失败");
                e.printStackTrace();
                return false;
            }
        }

        //若文件不可读写，则false
        if(!(file.isFile()|file.canWrite())){
            return false;
        }

        FileWriter fw = null;

        //true-表示追加式写文件
        try {
            fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);
            for(ScanBleDataEntity scanBleDataEntity : scanBleDataEntities){
                bw.write(scanBleDataEntity.datatime + " "+ scanBleDataEntity.heartrate + " "
                + scanBleDataEntity.stepnum+"\n");
            }
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        finally {

            try {
                fw.close();
            } catch (Exception e) {

                e.printStackTrace();

            }

        }
        return true;
    }

    public void query() {
        // 查询获得游标
        Cursor cursor = getDb().rawQuery("select * from "+DBConstants.TABLE_BLE
                +" where "+DBConstants.BLE_DATATIME+">? and "+DBConstants.TABLE_BLE
                +"< ?", new String[]{"2016-01-10 20:50:30", Util.getDataAndTime()});
        int count = cursor.getCount();
        Log.d(TAG, "-------------------------------  " + count + "  ---------------------------");
    }

    public void queryNewBleData() {
        Cursor cursor = getDb().rawQuery("select * from "+DBConstants.TABLE_BLE
                + " order by "+DBConstants.BLE_DATATIME+" desc limit 0,1",null);
        int count = cursor.getCount();
        cursor.moveToFirst();
//        MainActivity.step = cursor.getString(2);
//        MainActivity.heartRate = cursor.getString(3);
        Log.d(TAG, "----------  " + count + "  -----------" + " " + cursor.getString(2) + " " + cursor.getString(1));
    }

    @Override
    public String getTableName() {
        return DBConstants.TABLE_BLE;
    }
}
