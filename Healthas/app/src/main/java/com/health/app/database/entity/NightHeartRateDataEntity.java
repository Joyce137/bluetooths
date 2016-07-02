package com.health.app.database.entity;

import com.health.app.database.DBConstants;
import com.health.app.database.annotation.ColumnName;
import com.health.app.database.annotation.PrimaryKey;
import com.health.app.database.annotation.TableName;

/**
 * Created by CaoRuijuan on 6/18/16.
 */
@TableName(DBConstants.TABLE_NIGHTHEARTRATEDATA)
public class NightHeartRateDataEntity {
    @PrimaryKey(autoincrement=true)
    @ColumnName(DBConstants.TABLE_KEY)
    public int id;

    @ColumnName(DBConstants.BLE_ADDRESS)
    public String address;

    @ColumnName(DBConstants.NOTIFY_TIME)
    public String notifyTime;

    @ColumnName(DBConstants.APP_TIME)
    public String appTime;

    @ColumnName(DBConstants.CYCLEHEARTRATEDATA_SIZE)
    public String size;

    @ColumnName(DBConstants.CYCLEHEARTRATEDATA_DATA1)
    public String data1;

    @ColumnName(DBConstants.CYCLEHEARTRATEDATA_DATA2)
    public String data2;

    @ColumnName(DBConstants.CYCLEHEARTRATEDATA_DATA3)
    public String data3;

    @ColumnName(DBConstants.CYCLEHEARTRATEDATA_DATA4)
    public String data4;

    @ColumnName(DBConstants.CYCLEHEARTRATEDATA_DATA5)
    public String data5;

    @ColumnName(DBConstants.CYCLEHEARTRATEDATA_DATA6)
    public String data6;

    @ColumnName(DBConstants.CYCLEHEARTRATEDATA_DATA7)
    public String data7;

    @ColumnName(DBConstants.CYCLEHEARTRATEDATA_DATA8)
    public String data8;

    @ColumnName(DBConstants.CYCLEHEARTRATEDATA_DATA9)
    public String data9;

    @ColumnName(DBConstants.CYCLEHEARTRATEDATA_DATA10)
    public String data10;

    @ColumnName(DBConstants.CYCLEHEARTRATEDATA_DATA11)
    public String data11;

    @ColumnName(DBConstants.CYCLEHEARTRATEDATA_DATA12)
    public String data12;
}
