package com.develop.wiseisland.android.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by zyp on 2018/2/22.
 */
public class DBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = DBConfig.DB_NAME;
    private static final int VERSION = DBConfig.DB_VERSION;
    //数据库表名
    protected String table_name = "";

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 系统全局数据
//        db.execSQL(SysInfoSqlHelper.CREATE_TB_SYS_INFO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}