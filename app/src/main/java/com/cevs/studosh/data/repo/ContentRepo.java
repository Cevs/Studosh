package com.cevs.studosh.data.repo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cevs.studosh.data.DataBaseManager;
import com.cevs.studosh.data.model.Content;
import com.cevs.studosh.data.model.Course;

/**
 * Created by TOSHIBA on 04.10.2016..
 */

public class ContentRepo {

    public static String createTable(){
        return "CREATE TABLE " + Content.TABLE_Name + "("
                + Content.COLUMN_ContentId + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Content.COLUMN_Criteria + " TEXT, "
                + Content.COLUMN_Points + " REAL, "
                + Content.COLUMN_MaxPoints + " REAL, "
                + Content.COLUMN_fk_CourseId + " INTEGER, "
                + "FOREIGN KEY ("+Content.COLUMN_fk_CourseId + ") REFERENCES "
                + Course.TABLE_Name + "(" + Course.COLUMN_CourseId +") ON DELETE CASCADE );";
    }

    public long insertRow(Content content){
        long contentId;
        SQLiteDatabase db = DataBaseManager.getInstance().openDatabase();
        ContentValues values = new ContentValues();
        values.put(Content.COLUMN_Criteria, content.getCriteria());
        values.put(Content.COLUMN_Points, content.getPoints());
        values.put(Content.COLUMN_MaxPoints, content.getMaxPoints());
        values.put(Content.COLUMN_fk_CourseId, content.getCourseId());

        contentId = db.insert(Content.TABLE_Name,null,values);
        DataBaseManager.getInstance().closeDatabase();
        return  contentId;
    }

    public void deleteTable(){
        SQLiteDatabase db = DataBaseManager.getInstance().openDatabase();
        db.delete(Content.TABLE_Name,null,null);
        DataBaseManager.getInstance().closeDatabase();
    }


    public boolean deleteRow(String criterion, long rowId){
        SQLiteDatabase db = DataBaseManager.getInstance().openDatabase();
        String where = Content.COLUMN_Criteria + " = '" + criterion + "' AND " + Content.COLUMN_fk_CourseId
                + " = " + rowId;
        int b  = db.delete(Content.TABLE_Name,where,null);
        DataBaseManager.getInstance().closeDatabase();
        if (b!=-1)
            return true;
        return false;
    }

    public void deleteAllRows(long courseId){
        Cursor c = getAllRows(courseId);

        if (c.moveToFirst()){
            do{
                String criterion  = c.getString(c.getColumnIndexOrThrow(Content.COLUMN_Criteria));
                deleteRow(criterion,courseId);
            }while(c.moveToNext());
        }
        c.close();
    }

    public Cursor getRow(long id, String criterion){
        SQLiteDatabase db = DataBaseManager.getInstance().openDatabase();
        String where = Content.COLUMN_Criteria + " = '" + criterion + "' AND "
                + Content.COLUMN_fk_CourseId + " = " + id;
        Cursor c = db.query(true,Content.TABLE_Name,Content.ALL_ROWS,where,null,null,null,null,null);
        c.moveToFirst();
        DataBaseManager.getInstance().closeDatabase();
        return c;

    }

    public Cursor getRows(long id){
        SQLiteDatabase db = DataBaseManager.getInstance().openDatabase();
        String where = Content.COLUMN_fk_CourseId + " = " + id;
        Cursor c = db.query(true,Content.TABLE_Name,Content.ALL_ROWS,where,null,null,null,null,null);
        if (c!=null){
            c.moveToFirst();
        }
        DataBaseManager.getInstance().closeDatabase();
        return c;
    }

    public Cursor getAllRows(long courseId){
        SQLiteDatabase db = DataBaseManager.getInstance().openDatabase();
        String where = Content.COLUMN_fk_CourseId + " = " + courseId;
        Cursor c = db.query(true,Content.TABLE_Name,Content.ALL_ROWS, where, null, null, null, null, null, null);
        if (c!=null){
            c.moveToFirst();
        }
        DataBaseManager.getInstance().closeDatabase();
        return c;
    }

    public boolean updateRow(String title, Content content){
        String criterion = title;
        long courseId = content.getCourseId();
        SQLiteDatabase db = DataBaseManager.getInstance().openDatabase();
        String where = Content.COLUMN_Criteria + " = '" + criterion + "' AND " + Content.COLUMN_fk_CourseId
                + " = " + courseId;
        ContentValues newValues = new ContentValues();
        newValues.put(Content.COLUMN_Criteria, content.getCriteria());
        newValues.put(Content.COLUMN_Points, content.getPoints());
        newValues.put(Content.COLUMN_MaxPoints, content.getMaxPoints());
        newValues.put(Content.COLUMN_fk_CourseId, content.getCourseId());

        if(db.update(Content.TABLE_Name,newValues, where, null)==-1){
            DataBaseManager.getInstance().closeDatabase();
            return false;
        }
        else{
            DataBaseManager.getInstance().closeDatabase();
            return true;
        }
    }


}
