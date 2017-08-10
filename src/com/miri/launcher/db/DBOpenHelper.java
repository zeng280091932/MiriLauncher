/*
 * 
 */
package com.miri.launcher.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.miri.launcher.db.manage.RecommendDBHelper;

/**
 * 推荐DB帮助类
 * 
 * @author zengjiantao
 * @date 2013-3-18
 */
public class DBOpenHelper extends SQLiteOpenHelper {

	private static final String DB_NAME = "recommend.db";

	private static final int VERSION = 1;

	private static DBOpenHelper instance;

	private DBOpenHelper(Context context) {
		super(context, DB_NAME, null, VERSION);
	}

	public static DBOpenHelper getInstance(Context context) {
		if (instance == null) {
			instance = new DBOpenHelper(context);
		}
		return instance;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		RecommendDBHelper.onCreate(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		RecommendDBHelper.onUpgrade(db, oldVersion, newVersion);
	}

}
