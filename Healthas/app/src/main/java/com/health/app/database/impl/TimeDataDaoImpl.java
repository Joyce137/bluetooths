package com.health.app.database.impl;

import android.content.Context;

import com.health.app.database.DBConstants;
import com.health.app.database.entity.ScanBleData;
import com.health.app.database.entity.TimeData;
import com.health.app.database.support.DaoSupportImpl;
import com.health.app.statics.Util;

/**
 * Created by CaoRuijuan on 6/18/16.
 */
public class TimeDataDaoImpl extends DaoSupportImpl<TimeData> {
    private Context context;
    private final static String TAG = ScanBleDataDaoImpl.class.getSimpleName();
//    private String userName;

    public TimeDataDaoImpl(Context context) {
        super(context, "ble", false);
        this.context = context;

        try {
            String createSqlStr = DBConstants.TIMEDATA_CREATE;
            getDb().execSQL(createSqlStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean insertBleData(String address, String time) {
        TimeData scanBleData = new TimeData();
        scanBleData.address = address;
        scanBleData.time = time;

        return insert(scanBleData);
    }
}
