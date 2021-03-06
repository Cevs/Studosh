package com.cevs.studosh.data.repo;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cevs.studosh.data.DataBaseManager;
import com.cevs.studosh.data.model.Content;
import com.cevs.studosh.data.model.Course;
import com.cevs.studosh.data.model.Presence;

/**
 * Created by TOSHIBA on 18.10.2016..
 */

public class PresenceRepo {

    public static String createTable(){
        return "CREATE TABLE " + Presence.TABLE_Name + "("
                + Presence.COLUMN_PresenceId + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Presence.COLUMN_DateTime + " TEXT, "
                + Presence.COLUMN_Presence + " INTEGER, "
                + Presence.COLUMN_CourseId + " INTEGER, "
                + Presence.COLUMN_CalendarType + " INTEGER, "
                + "FOREIGN KEY (" + Presence.COLUMN_CourseId + ") REFERENCES "
                + Course.TABLE_Name + "(" + Course.COLUMN_CourseId + ") ON DELETE CASCADE);";
    }

    public long insertRow(Presence presence){
        long presenceId;
        SQLiteDatabase db = DataBaseManager.getInstance().openDatabase();
        ContentValues values = new ContentValues();
        values.put(Presence.COLUMN_DateTime, presence.getDateTime());
        values.put(Presence.COLUMN_Presence, presence.getPresenceType());
        values.put(Presence.COLUMN_CalendarType,presence.getCalendarType());
        values.put(Presence.COLUMN_CourseId, presence.getForeignKey());

        presenceId = db.insert(Presence.TABLE_Name, null, values);
        DataBaseManager.getInstance().closeDatabase();
        return presenceId;
    }

    public boolean updateRow(long rowId, Presence presence){
        SQLiteDatabase db = DataBaseManager.getInstance().openDatabase();
        ContentValues newValues = new ContentValues();
        String where = Presence.COLUMN_PresenceId + " = " + rowId;

        newValues.put(Presence.COLUMN_DateTime, presence.getDateTime());
        newValues.put(Presence.COLUMN_Presence, presence.getPresenceType());
        newValues.put(Presence.COLUMN_CalendarType,presence.getCalendarType());
        newValues.put(Presence.COLUMN_CourseId,presence.getForeignKey());

        if(db.update(Presence.TABLE_Name,newValues, where, null)==-1){
            DataBaseManager.getInstance().closeDatabase();
            return false;
        }
        else{
            DataBaseManager.getInstance().closeDatabase();
            return true;
        }
    }
    public void deleteTable(){
        SQLiteDatabase db = DataBaseManager.getInstance().openDatabase();
        db.delete(Presence.TABLE_Name,null,null);
        DataBaseManager.getInstance().closeDatabase();
    }

    public boolean deleteRow(long presenceId){
        SQLiteDatabase db = DataBaseManager.getInstance().openDatabase();
        String where = Presence.COLUMN_PresenceId + " = " + presenceId;
        int b = db.delete(Presence.TABLE_Name,where,null);
        DataBaseManager.getInstance().closeDatabase();

        if(b!=-1){
            return true;
        }
        return false;
    }

    public void deleteRowWithCourseId(long courseId){
        SQLiteDatabase db = DataBaseManager.getInstance().openDatabase();
        String where = Presence.COLUMN_CourseId + " = " + courseId;
        db.delete(Presence.TABLE_Name,where,null);
        DataBaseManager.getInstance().closeDatabase();
    }

    public Cursor getAllRows(long courseId, int type){
        SQLiteDatabase db = DataBaseManager.getInstance().openDatabase();
        String where = Presence.COLUMN_CourseId + " = " + courseId +" AND " + Presence.COLUMN_CalendarType + " = " + type;
        Cursor c = db.query(true,Presence.TABLE_Name,Presence.ALL_ROWS,where,null,null,null,null,null,null);
        if (c!=null){
            c.moveToFirst();
        }
        DataBaseManager.getInstance().closeDatabase();
        return c;
    }


}
