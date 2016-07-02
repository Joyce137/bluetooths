package com.health.app.database.impl;

import android.content.Context;

import com.health.app.database.DBConstants;
import com.health.app.database.entity.TimeDataEntity;
import com.health.app.database.support.DaoSupportImpl;
import com.health.app.statics.Util;

/**
 * Created by CaoRuijuan on 6/18/16.
 */
public class TimeDataDaoImpl extends DaoSupportImpl<TimeDataEntity> {
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
        TimeDataEntity scanBleData = new TimeDataEntity();
        scanBleData.address = address;
        scanBleData.time = time;
        scanBleData.appTime = Util.getAppTime();

        //插入数据
        if(checkDateTimeExistInBledate(time)){
            return false;
        }
        return insert(scanBleData);
    }

    //判断某个datetime是否在bledata表中
    public boolean checkDateTimeExistInBledate(String datetime){
        String selector = DBConstants.TIMEDATA_TIME + "=?";
        String[] selectorargs = new String[]{datetime.trim()};

        if(findEntity(selector,selectorargs).size() == 0){
            return false;
        }
        return true;
    }
}
