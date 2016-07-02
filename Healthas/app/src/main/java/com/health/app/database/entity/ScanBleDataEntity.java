package com.health.app.database.entity;

import com.health.app.database.DBConstants;
import com.health.app.database.annotation.ColumnName;
import com.health.app.database.annotation.PrimaryKey;
import com.health.app.database.annotation.TableName;
import com.health.app.statics.Util;

/**
 * Created by CaoRuijuan on 3/15/16.
 */
@TableName("bleData")
public class ScanBleDataEntity {
    @PrimaryKey(autoincrement=true)
    @ColumnName(DBConstants.TABLE_KEY)
    public int id;

    @ColumnName(DBConstants.BLE_DATATIME)
    public String datatime;

    @ColumnName(DBConstants.BLE_ADDRESS)
    public String address;

    @ColumnName(DBConstants.BLE_BATTERY)
    public String battery;

    @ColumnName(DBConstants.BLE_BROKENSTATE)
    public String broken_state;

    @ColumnName(DBConstants.BLE_HEARTRATE)
    public String heartrate;

    @ColumnName(DBConstants.BLE_SPORTLEVEL)
    public String sport_level;

    @ColumnName(DBConstants.BLE_STEPNUM)
    public String stepnum;

    @ColumnName(DBConstants.BLE_TXPOWER)
    public String tx_power;

    @ColumnName(DBConstants.BLE_SOSSTATE)
    public String sos_state;

    public ScanBleDataEntity(){
        datatime = Util.getDataAndTime();
    }
}
