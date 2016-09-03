package com.vc.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class VirtualCampusOpenHelper extends SQLiteOpenHelper {
	
	private static final String Table_Activity = "create table activity("
            + "id integer primary key autoincrement, "
            + "action_id integer,"
            + "action_host text," 
            + "action_picPath text," 
            + "action_site text," 
            + "action_time text,"
            + "action_theme text,"
            + "action_detail text,"
			+ "attendance_nums integer,"
			+ "like_nums integer)";
	
	private static final String Table_AttendanceStatus = "create table attendancestatus("
			+ "id integer primary key autoincrement,"
			+ "action_id integer,"
			+ "user_id integer,"
			+ "is_attendance integer)";
	private static final String Table_LikeStatus = "create table likestatus("
			+ "id integer primary key autoincrement,"
			+ "action_id integer,"
			+ "user_id integer,"
			+ "is_like integer)";
	
	public VirtualCampusOpenHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL("drop table if exists activity");
		db.execSQL("drop table if exists attendancestatus");
		db.execSQL("drop table if exists likestatus");
		
		db.execSQL(Table_Activity);  // 创建activity表
		db.execSQL(Table_AttendanceStatus); //创建参与信息表
		db.execSQL(Table_LikeStatus); //创建点赞信息表
		 
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}

}
