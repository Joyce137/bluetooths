package com.health.app.database;

import android.database.sqlite.SQLiteDatabase;


/**
 * Created by CaoRuijuan on 12/22/15.
 */
public class DBConstants {
    //蓝牙数据库
    public static final String DB_NAME_BLE = "ble.db";
    //用户蓝牙数据
    public static final String TABLE_BLE = "scanbledata";
    public static final String TABLE_KEY = "id";
    public static final String BLE_ADDRESS = "address";
    public static final String BLE_BYTES = "bytes";
    public static final String BLE_DATATIME = "datatime";
    public static final String BLE_HEARTRATE = "heartrate";
    public static final String BLE_STEPNUM = "stepnum";
    public static final String BLE_CALORIE = "calorie";
    public static final String BLE_AMUTOFERCE = "amutoferce";
    //创建_bledata表
    public static String BLE_DB_CREATE = "create table "+
            TABLE_BLE + " ("+
            TABLE_KEY + " integer primary key autoincrement, "+
            BLE_ADDRESS + " text not null, "+
            BLE_DATATIME + " text not null, "+
            BLE_BYTES + " text not null, "+
            BLE_HEARTRATE + " text, "+
            BLE_STEPNUM + " text, "+
            BLE_CALORIE + " text, "+
            BLE_AMUTOFERCE + " text);";
    //删除ble表
    public static final String BLE_DB_DROP = "drop table if exists "+TABLE_BLE;

    //timedata表
    public static final String TABLE_TIMEDATA = "timedata";
    public static final String TIMEDATA_TIME = "time";
    //创建timedata表
    public static final String TIMEDATA_CREATE = "create table "+
            TABLE_TIMEDATA + " ("+
            TABLE_KEY + " integer primary key autoincrement, "+
            BLE_ADDRESS + " text not null, "+
            TIMEDATA_TIME + "text not null);";
    //删除timedata表
    public static final String TIMEDATA_DROP = "drop table if exists "+TABLE_TIMEDATA;

    //sportdata表
    public static final String TABLE_SPORTDATA = "sportdata";
    public static final String SPORTDATA_DATE = "date";
    public static final String SPORTDATA_MONTH = "month";
    public static final String SPORTDATA_STEP = "step";
    public static final String SPORTDATA_PROPERTIMES = "propertimes";
    public static final String SPORTDATA_STRONGTIMES = "strongtimes";
    public static final String SPORTDATA_SPORTSTRENGTH = "sportstrength";
    public static final String SPORTDATA_VALIDTIMES = "validtimes";
    //创建timedata表
    public static final String SPORTDATA_CREATE = "create table "+
            TABLE_SPORTDATA + " ("+
            TABLE_KEY + " integer primary key autoincrement, "+
            BLE_ADDRESS + " text not null, "+
            SPORTDATA_DATE + " text not null, "+
            SPORTDATA_MONTH + " text not null, "+
            SPORTDATA_STEP + " text not null, "+
            SPORTDATA_PROPERTIMES + " text not null, "+
            SPORTDATA_STRONGTIMES + " text not null, "+
            SPORTDATA_SPORTSTRENGTH + " text not null, "+
            SPORTDATA_VALIDTIMES + "text not null);";
    //删除timedata表
    public static final String SPORTDATA_DROP = "drop table if exists "+TABLE_SPORTDATA;

    //cycleheartratedata表
    public static final String TABLE_CYCLEHEARTRATEDATA = "cycleheartratedata";
    public static final String CYCLEHEARTRATEDATA_SIZE = "size";
    public static final String CYCLEHEARTRATEDATA_DATA1 = "data1";
    public static final String CYCLEHEARTRATEDATA_DATA2 = "data2";
    public static final String CYCLEHEARTRATEDATA_DATA3 = "data3";
    public static final String CYCLEHEARTRATEDATA_DATA4 = "data4";
    public static final String CYCLEHEARTRATEDATA_DATA5 = "data5";
    public static final String CYCLEHEARTRATEDATA_DATA6 = "data6";
    public static final String CYCLEHEARTRATEDATA_DATA7 = "data7";
    public static final String CYCLEHEARTRATEDATA_DATA8 = "data8";
    public static final String CYCLEHEARTRATEDATA_DATA9 = "data9";
    public static final String CYCLEHEARTRATEDATA_DATA10 = "data10";
    public static final String CYCLEHEARTRATEDATA_DATA11 = "data11";
    public static final String CYCLEHEARTRATEDATA_DATA12 = "data12";
    //创建cycleheartratedata表
    public static final String CYCLEHEARTRATEDATA_CREATE = "create table "+
            TABLE_CYCLEHEARTRATEDATA + " ("+
            TABLE_KEY + " integer primary key autoincrement, "+
            BLE_ADDRESS + " text not null, "+
            CYCLEHEARTRATEDATA_SIZE + " text not null, "+
            CYCLEHEARTRATEDATA_DATA1 + " text not null, "+
            CYCLEHEARTRATEDATA_DATA2 + " text not null, "+
            CYCLEHEARTRATEDATA_DATA3 + " text not null, "+
            CYCLEHEARTRATEDATA_DATA4 + " text not null, "+
            CYCLEHEARTRATEDATA_DATA5 + " text not null, "+
            CYCLEHEARTRATEDATA_DATA6 + " text not null, "+
            CYCLEHEARTRATEDATA_DATA7 + " text not null, "+
            CYCLEHEARTRATEDATA_DATA8 + " text not null, "+
            CYCLEHEARTRATEDATA_DATA9 + " text not null, "+
            CYCLEHEARTRATEDATA_DATA10 + " text not null, "+
            CYCLEHEARTRATEDATA_DATA11 + " text not null, "+
            CYCLEHEARTRATEDATA_DATA12 + "text not null);";
    //删除cycleheartratedata表
    public static final String CYCLEHEARTRATEDATA_DROP = "drop table if exists "+TABLE_CYCLEHEARTRATEDATA;

    //nightheartratedata表
    public static final String TABLE_NIGHTHEARTRATEDATA = "nightheartratedata";
//    public static final String NIGHTHEARTRATEDATA_SIZE = "size";
//    public static final String NIGHTHEARTRATEDATA_DATA1 = "data1";
//    public static final String NIGHTHEARTRATEDATA_DATA2 = "data2";
//    public static final String NIGHTHEARTRATEDATA_DATA3 = "data3";
//    public static final String NIGHTHEARTRATEDATA_DATA4 = "data4";
//    public static final String NIGHTHEARTRATEDATA_DATA5 = "data5";
//    public static final String NIGHTHEARTRATEDATA_DATA6 = "data6";
//    public static final String NIGHTHEARTRATEDATA_DATA7 = "data7";
//    public static final String NIGHTHEARTRATEDATA_DATA8 = "data8";
//    public static final String NIGHTHEARTRATEDATA_DATA9 = "data9";
//    public static final String NIGHTHEARTRATEDATA_DATA10 = "data10";
//    public static final String NIGHTHEARTRATEDATA_DATA11 = "data11";
//    public static final String NIGHTHEARTRATEDATA_DATA12 = "data12";
    //创建nightheartratedata表
    public static final String NIGHTHEARTRATEDATA_CREATE = "create table "+
            TABLE_NIGHTHEARTRATEDATA + " ("+
            TABLE_KEY + " integer primary key autoincrement, "+
            BLE_ADDRESS + " text not null, "+
            CYCLEHEARTRATEDATA_SIZE + " text not null, "+
            CYCLEHEARTRATEDATA_DATA1 + " text not null, "+
            CYCLEHEARTRATEDATA_DATA2 + " text not null, "+
            CYCLEHEARTRATEDATA_DATA3 + " text not null, "+
            CYCLEHEARTRATEDATA_DATA4 + " text not null, "+
            CYCLEHEARTRATEDATA_DATA5 + " text not null, "+
            CYCLEHEARTRATEDATA_DATA6 + " text not null, "+
            CYCLEHEARTRATEDATA_DATA7 + " text not null, "+
            CYCLEHEARTRATEDATA_DATA8 + " text not null, "+
            CYCLEHEARTRATEDATA_DATA9 + " text not null, "+
            CYCLEHEARTRATEDATA_DATA10 + " text not null, "+
            CYCLEHEARTRATEDATA_DATA11 + " text not null, "+
            CYCLEHEARTRATEDATA_DATA12 + "text not null);";
    //删除nightheartratedata表
    public static final String NIGHTHEARTRATEDATA_DROP = "drop table if exists "+TABLE_NIGHTHEARTRATEDATA;

    //heartratedata表
    public static final String TABLE_HEARTRATEDATA = "heartratedata";
    public static final String HEARTRATEDATA_CREATE = "create table "+
            TABLE_HEARTRATEDATA + " ("+
            TABLE_KEY + " integer primary key autoincrement, "+
            BLE_ADDRESS + " text not null, "+
            SPORTDATA_DATE + " text not null, "+
            SPORTDATA_MONTH + " text not null, "+
            CYCLEHEARTRATEDATA_SIZE + " text not null, "+
            CYCLEHEARTRATEDATA_DATA1 + " text not null, "+
            CYCLEHEARTRATEDATA_DATA2 + " text not null, "+
            CYCLEHEARTRATEDATA_DATA3 + " text not null, "+
            CYCLEHEARTRATEDATA_DATA4 + " text not null, "+
            CYCLEHEARTRATEDATA_DATA5 + " text not null, "+
            CYCLEHEARTRATEDATA_DATA6 + " text not null, "+
            CYCLEHEARTRATEDATA_DATA7 + " text not null, "+
            CYCLEHEARTRATEDATA_DATA8 + " text not null, "+
            CYCLEHEARTRATEDATA_DATA9 + " text not null, "+
            CYCLEHEARTRATEDATA_DATA10 + " text not null, "+
            CYCLEHEARTRATEDATA_DATA11 + " text not null, "+
            CYCLEHEARTRATEDATA_DATA12 + "text not null);";
    //删除heartratedata表
    public static final String HEARTRATEDATA_DROP = "drop table if exists "+TABLE_HEARTRATEDATA;

    //心率数据表
    public static final String TABLE_HEARTRATE = "heartRateData";
    //创建heartRateData表
    public static String HEARTBEAT_TABLE_CREATE = "create table "+
            TABLE_HEARTRATE + " ("+
            TABLE_KEY + " integer primary key autoincrement, "+
            BLE_DATATIME + " text not null, "+
            BLE_HEARTRATE + " text not null);";
    //删除ble表
    public static final String HEARTBEAT_TABLE_DROP = "drop table if exists "+TABLE_HEARTRATE;


    //数据库版本
    public static final int DB_VERSION = 1;


//    //检查表是是否存在在数据库中
//    public static boolean checkTableIfExistsInDatabase(SQLiteDatabase database,String table){
//        String checkStr = "if not exists (select * from sysobjects where id = object_id('"+table
//                +"') and OBJECTPROPERTY(id, 'IsUserTable') = 1)";
//        database.execSQL(checkStr);
//
//    }
}
