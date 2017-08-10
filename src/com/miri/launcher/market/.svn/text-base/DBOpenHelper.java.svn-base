package com.miri.launcher.market;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBOpenHelper extends SQLiteOpenHelper {

    static final String DATABASE_NAME = "mirimarket.db";

    static final int DATABASE_VERSION = 1;

    public DBOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE downloading(url VARCHAR(1024), filePath VARCHAR(1024), thid INTEGER, done INTEGER, appName VARCHAR(256),iconPath VARCHAR(1024), PRIMARY KEY(url, filePath, thid))");
        db.execSQL("CREATE TABLE completed(url VARCHAR(1024), filePath VARCHAR(1024), fileLen INTEGER, appName VARCHAR(256),  PRIMARY KEY(filePath))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO 数据库的结构变化,需要对oldVersion和 newVersion进行判断之后再决定使用什么策略来更新数据
        db.execSQL("DROP TABLE IF EXISTS downloading");
        db.execSQL("DROP TABLE IF EXISTS completed");
        onCreate(db);
    }
}
