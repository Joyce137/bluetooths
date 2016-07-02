package com.health.app.database.impl;

import android.content.Context;

import com.health.app.database.DBConstants;
import com.health.app.database.entity.SleepModeHeartRateDataEntity;
import com.health.app.database.support.DaoSupportImpl;
import com.health.app.statics.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CaoRuijuan on 3/20/16.
 */
public class SleepModeHeartRataDataDaoImpl extends DaoSupportImpl<SleepModeHeartRateDataEntity> {
    private Context context;
    private final static String TAG = SleepModeHeartRataDataDaoImpl.class.getSimpleName();

    public SleepModeHeartRataDataDaoImpl(Context context){
        super(context, "ble", false);
        //新建heartRateData表
        try {
            getDb().execSQL(DBConstants.HEARTBEAT_TABLE_CREATE);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public boolean insertHeartRataData(String heartrate) {
        SleepModeHeartRateDataEntity sleepModeHeartRateDataEntity = new SleepModeHeartRateDataEntity();
        sleepModeHeartRateDataEntity.heartrate = heartrate;
        sleepModeHeartRateDataEntity.datatime = Util.getDataAndTime();

        //插入数据
        if(checkDateTimeExistInHeartRateDatadate(Util.getDataAndTime())){
            return false;
        }
         if(checkNowThreeMinute())
            return insert(sleepModeHeartRateDataEntity);
        else {
            return false;
        }
    }

    //判断某个datetime是否存在在heartRateData表中
    public boolean checkDateTimeExistInHeartRateDatadate(String datetime){
        String selector = DBConstants.BLE_DATATIME + "=?";
        String[] selectorargs = new String[]{datetime.trim()};

        if(findEntity(selector,selectorargs).size() == 0){
            return false;
        }
        return true;
    }

    //判断当前datetime是否符合三分钟一次
    public boolean checkNowThreeMinute(){
        String now = Util.getDataAndTime();
        String lastTime = now;

        String sqlStr = "select "+DBConstants.BLE_DATATIME + " from "+DBConstants.TABLE_HEARTRATE+
                " order by "+ DBConstants.TABLE_KEY+" desc";
        ArrayList<SleepModeHeartRateDataEntity> dateTimeResult = executeSql(sqlStr);
        if(dateTimeResult.size() == 0){
            return true;
        }
        lastTime = dateTimeResult.get(0).datatime;
        return Util.checkThreeMinute(lastTime,now);
    }

    //查询now之前的心率数据
    public List<SleepModeHeartRateDataEntity> queryHeartRateData(){
        String now = Util.getDataAndTime();
        String selector = DBConstants.BLE_DATATIME + "<?";
        String[] selectorargs = new String[]{now.trim()};

        return findEntity(selector,selectorargs);
    }
}
