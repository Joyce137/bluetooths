package com.health.app.database.impl;

import android.content.Context;

import com.health.app.database.DBConstants;
import com.health.app.database.entity.NightHeartRateDataEntity;
import com.health.app.database.support.DaoSupportImpl;
import com.health.app.statics.BleInfo;
import com.health.app.statics.Util;

/**
 * Created by CaoRuijuan on 6/18/16.
 */
public class NightHeartRateDataDaoImpl extends DaoSupportImpl<NightHeartRateDataEntity> {
    private Context context;
    private final static String TAG = ScanBleDataDaoImpl.class.getSimpleName();
//    private String userName;

    public NightHeartRateDataDaoImpl(Context context) {
        super(context, "ble", false);
        this.context = context;

        try {
            String createSqlStr = DBConstants.NIGHTHEARTRATEDATA_CREATE;
            getDb().execSQL(createSqlStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean insertBleData(String address, String size, String data1, String data2,
                                 String data3, String data4, String data5, String data6,
                                 String data7, String data8, String data9, String data10,
                                 String data11, String data12) {
        NightHeartRateDataEntity scanBleData = new NightHeartRateDataEntity();
        scanBleData.address = address;
        scanBleData.notifyTime = BleInfo.mCurNotifyTime;
        scanBleData.appTime = Util.getAppTime();
        scanBleData.size = size;
        scanBleData.data1 = data1;
        scanBleData.data2 = data2;
        scanBleData.data3 = data3;
        scanBleData.data4 = data4;
        scanBleData.data5 = data5;
        scanBleData.data6 = data6;
        scanBleData.data7 = data7;
        scanBleData.data8 = data8;
        scanBleData.data9 = data9;
        scanBleData.data10 = data10;
        scanBleData.data11 = data11;
        scanBleData.data12 = data12;

        //插入数据
        if(checkDateTimeExistInBledate(size)){
            return false;
        }

        return insert(scanBleData);
    }

    private boolean checkDateTimeExistInBledate(String size) {
        String selector = DBConstants.CYCLEHEARTRATEDATA_SIZE + "=?";
        String[] selectorargs = new String[]{size.trim()};

        if(findEntity(selector,selectorargs).size() == 0){
            return false;
        }
        return true;
    }
}