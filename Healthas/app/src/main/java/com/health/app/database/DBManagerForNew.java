package com.health.app.database;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by CaoRuijuan on 12/22/15.
 * 进一步统一管理数据库
 */
public class DBManagerForNew {
    private DBHelperForNew dbHelper;
    private Context context;

    //创建数据库
    public DBManagerForNew(Context context, String dbName){
        this.context = context;

        //user数据库
        switch (dbName){

            //ble数据库
            case "ble":
                dbHelper = new DBHelperForNew(context,DBConstants.DB_NAME_BLE,
                        DBConstants.BLE_DB_CREATE,DBConstants.BLE_DB_DROP);
                break;

            default:
                dbHelper = null;
                break;
        }

    }

    //获取数据库
    public SQLiteDatabase getWritableDatabase(){
        return dbHelper.getWritableDatabase();
    }

    public SQLiteDatabase getReadableDatabase(){
        return dbHelper.getReadableDatabase();
    }
}
