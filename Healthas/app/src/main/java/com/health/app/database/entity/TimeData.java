package com.health.app.database.entity;

import com.health.app.database.DBConstants;
import com.health.app.database.annotation.ColumnName;
import com.health.app.database.annotation.PrimaryKey;
import com.health.app.database.annotation.TableName;

/**
 * Created by CaoRuijuan on 6/18/16.
 */
@TableName(DBConstants.TABLE_TIMEDATA)
public class TimeData {
    @PrimaryKey(autoincrement=true)
    @ColumnName(DBConstants.TABLE_KEY)
    public int id;

    @ColumnName(DBConstants.BLE_ADDRESS)
    public String address;

    @ColumnName(DBConstants.TIMEDATA_TIME)
    public String time;
}
