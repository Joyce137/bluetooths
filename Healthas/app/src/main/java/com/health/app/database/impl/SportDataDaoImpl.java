package com.health.app.database.impl;

import android.content.Context;

import com.health.app.database.DBConstants;
import com.health.app.database.entity.SportDataEntity;
import com.health.app.database.support.DaoSupportImpl;
import com.health.app.statics.BleInfo;
import com.health.app.statics.Util;

/**
 * Created by CaoRuijuan on 6/18/16.
 */

public class SportDataDaoImpl extends DaoSupportImpl<SportDataEntity> {
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
        SportDataEntity scanBleData = new SportDataEntity();
        scanBleData.address = address;
        scanBleData.notifyTime = BleInfo.mCurNotifyTime;
        scanBleData.appTime = Util.getAppTime();
        scanBleData.date = date;
        scanBleData.month = month;
        scanBleData.step = step;
        scanBleData.propertimes = propertimes;
        scanBleData.strongtimes = strongtimes;
        scanBleData.sportstrength = sportstrength;
        scanBleData.validtimes = validtimes;

        if(date.equals("0"))
            return false;

        //插入数据
        if(checkDateTimeExistInBledate(date)){
            return false;
        }
        return insert(scanBleData);
    }

    private boolean checkDateTimeExistInBledate(String date) {
        String selector = DBConstants.SPORTDATA_DATE + "=?";
        String[] selectorargs = new String[]{date.trim()};

        if(findEntity(selector,selectorargs).size() == 0){
            return false;
        }
        return true;
    }
}
