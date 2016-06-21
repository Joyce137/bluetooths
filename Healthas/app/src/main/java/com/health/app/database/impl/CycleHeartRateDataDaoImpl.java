package com.health.app.database.impl;

import android.content.Context;

import com.health.app.database.DBConstants;
import com.health.app.database.entity.CycleHeartRateData;
import com.health.app.database.support.DaoSupportImpl;

/**
 * Created by CaoRuijuan on 6/18/16.
 */
public class CycleHeartRateDataDaoImpl extends DaoSupportImpl<CycleHeartRateData> {
    private Context context;
    private final static String TAG = ScanBleDataDaoImpl.class.getSimpleName();
//    private String userName;

    public CycleHeartRateDataDaoImpl(Context context) {
        super(context, "ble", false);
        this.context = context;

        try {
            String createSqlStr = DBConstants.CYCLEHEARTRATEDATA_CREATE;
            getDb().execSQL(createSqlStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean insertBleData(String address, String size, String data1, String data2,
                                 String data3, String data4, String data5, String data6,
                                 String data7, String data8, String data9, String data10,
                                 String data11, String data12) {
        CycleHeartRateData scanBleData = new CycleHeartRateData();
        scanBleData.address = address;
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

        return insert(scanBleData);
    }
}
