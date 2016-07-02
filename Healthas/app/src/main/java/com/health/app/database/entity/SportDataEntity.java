package com.health.app.database.entity;

import com.health.app.database.DBConstants;
import com.health.app.database.annotation.ColumnName;
import com.health.app.database.annotation.PrimaryKey;
import com.health.app.database.annotation.TableName;

/**
 * Created by CaoRuijuan on 6/18/16.
 */

@TableName(DBConstants.TABLE_SPORTDATA)
public class SportDataEntity {
    @PrimaryKey(autoincrement=true)
    @ColumnName(DBConstants.TABLE_KEY)
    public int id;

    @ColumnName(DBConstants.BLE_ADDRESS)
    public String address;

    @ColumnName(DBConstants.NOTIFY_TIME)
    public String notifyTime;

    @ColumnName(DBConstants.APP_TIME)
    public String appTime;

    @ColumnName(DBConstants.SPORTDATA_DATE)
    public String date;

    @ColumnName(DBConstants.SPORTDATA_MONTH)
    public String month;

    @ColumnName(DBConstants.SPORTDATA_STEP)
    public String step;

    @ColumnName(DBConstants.SPORTDATA_PROPERTIMES)
    public String propertimes;

    @ColumnName(DBConstants.SPORTDATA_STRONGTIMES)
    public String strongtimes;

    @ColumnName(DBConstants.SPORTDATA_SPORTSTRENGTH)
    public String sportstrength;

    @ColumnName(DBConstants.SPORTDATA_VALIDTIMES)
    public String validtimes;
}