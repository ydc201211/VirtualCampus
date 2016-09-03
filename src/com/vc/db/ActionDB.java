package com.vc.db;

import java.util.ArrayList;
import java.util.List;

import dto.AttendanceStatus;
import dto.LikeStatus;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class ActionDB {
	 /**
     * 数据库名
     */
    public static final String DB_NAME = "virtualcompus";

    /**
     * 数据库版本
     */
    public static final int VERSION = 1;

    private static ActionDB actionDB;

    private SQLiteDatabase db;

    /**
     * 将构造方法私有化
     */
    private ActionDB(Context context) {
    	VirtualCampusOpenHelper dbHelper = new VirtualCampusOpenHelper(context,
                DB_NAME, null, VERSION);
        db = dbHelper.getWritableDatabase();
    }

    /**
     * 获取CoolWeatherDB的实例。
     */
    public synchronized static ActionDB getInstance(Context context) {
        if (actionDB == null) {
        	actionDB = new ActionDB(context);
        }
        return actionDB;
    }

    /**
     * 将参与状存储到数据库。
     */
    public void saveAttendanceStatus(AttendanceStatus attendanceStatus) {
            ContentValues values = new ContentValues();
            values.put("action_id", attendanceStatus.getActionId());
            values.put("user_id", attendanceStatus.getUserId());
            
            
            values.put("is_attendance", attendanceStatus.getIsAttendance());
            
            db.insert("attendancestatus", null, values);
            
        
    }
    /**
     * 将点赞信息添加到数据库
     * @param actionId
     * @param userId
     * @param isLike
     */
    public void saveLikeStatus(LikeStatus likeStatus) {
        ContentValues values = new ContentValues();
        values.put("action_id", likeStatus.getActionId());
        values.put("user_id", likeStatus.getUserId());
        values.put("is_like", likeStatus.getIsLike());
        Log.i("abc",likeStatus.getIsLike()+"");
        db.insert("likestatus", null, values);
    
    }
    
    

    /**
     * 从数据库读参与。
     */
    public List<AttendanceStatus> getAttendance() {
        List<AttendanceStatus> list = new ArrayList<AttendanceStatus>();
        Cursor cursor = db.query("attendancestatus", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
            	AttendanceStatus as = new AttendanceStatus();
            	as.setActionId(cursor.getInt(cursor.getColumnIndex("action_id")));
            	as.setUserId(cursor.getColumnIndex("user_id"));
            	as.setIsAttendance(cursor.getColumnIndex("is_attendance"));
                list.add(as);
            } while (cursor.moveToNext());
        }
        return list;
    }

    /**
     * 从数据库读取点赞
     * @return
     */
    public List<LikeStatus> getLike() {
        List<LikeStatus> list = new ArrayList<LikeStatus>();
        Cursor cursor = db.query("likestatus", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
            	LikeStatus ls = new LikeStatus();
            	ls.setActionId(cursor.getInt(cursor.getColumnIndex("action_id")));
            	ls.setUserId(cursor.getColumnIndex("user_id"));
            	ls.setIsLike(cursor.getColumnIndex("is_like"));
                list.add(ls);
            } while (cursor.moveToNext());
        }
        return list;
    }
    
    
    public void deleteAttendance(String actionId){
    	String[] a =new String[]{actionId};
    	db.delete("attendancestatus","action_id=?",a);
    }
    
    public void deleteLike(String actionId){
    	String[] a =new String[]{actionId};
    	db.delete("likestatus","action_id=?",a);
    }
    
   
}
