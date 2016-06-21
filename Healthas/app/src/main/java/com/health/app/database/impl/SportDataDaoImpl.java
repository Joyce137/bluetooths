package com.health.app.database.impl;

import android.content.Context;

import com.health.app.database.DBConstants;
import com.health.app.database.entity.SportData;
import com.health.app.database.support.DaoSupportImpl;

/**
 * Created by CaoRuijuan on 6/18/16.
 */

public class SportDataDaoImpl extends DaoSupportImpl<SportData> {
    private Context context;
    private final static String TAG = ScanBleDataDaoImpl.class.getSimpleName();
//    private String userName;

    public SportDataDaoImpl(Context context) {
        super(context, "ble", false);
        this.context = context;

        try {
            String createSqlStr = DBConstants.SPORTDATA_CREATE;
            getDb().execSQL(createSqlStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean insertBleData(String address, String date, String month, String step,
                                 String propertimes, String strongtimes, String sportstrength, String validtimes) {
        SportData scanBleData = new SportData();
        scanBleData.address = address;
        scanBleData.date = date;
        scanBleData.month = month;
        scanBleData.step = step;
        scanBleData.propertimes = propertimes;
        scanBleData.strongtimes = strongtimes;
        scanBleData.sportstrength = sportstrength;
        scanBleData.validtimes = validtimes;

        return insert(scanBleData);
    }
}
