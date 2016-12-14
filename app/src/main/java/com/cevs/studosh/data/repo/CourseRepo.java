package com.cevs.studosh.data.repo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;

import com.cevs.studosh.data.DataBaseManager;
import com.cevs.studosh.data.model.Course;
import com.cevs.studosh.data.model.Semester;

/**
 * Created by TOSHIBA on 28.09.2016..
 */

public class CourseRepo {



    public static String createTable(){
        return "CREATE TABLE " + Course.TABLE_Name + "("
                + Course.COLUMN_CourseId + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Course.COLUMN_CourseName + " TEXT, "
                + Course.COLUMN_CourseECTS + " INTEGER, "
                + Course.COLUMN_SemesterId + " INTEGER, "
                + "FOREIGN KEY (" + Course.COLUMN_SemesterId + ") REFERENCES "
                + Semester.TABLE_Name + "(" + Semester.COLUMN_SemesterId +") ON DELETE CASCADE );";
    }

    public long insertRow(Course course){
        long courseId;
        SQLiteDatabase db = DataBaseManager.getInstance().openDatabase();
        ContentValues values = new ContentValues();
        values.put(Course.COLUMN_CourseName, course.getCourseName());
        values.put(Course.COLUMN_CourseECTS, course.getCourseECTS());
        values.put(Course.COLUMN_SemesterId, course.getSemesterId());
        courseId =  db.insert(Course.TABLE_Name, null, values);
        DataBaseManager.getInstance().closeDatabase();
        return  courseId;
    }

    public void deleteTable(){
        SQLiteDatabase db = DataBaseManager.getInstance().openDatabase();
        db.delete(Course.TABLE_Name, null, null);
        DataBaseManager.getInstance().closeDatabase();
    }
    public boolean deleteRow(long rowId){
        SQLiteDatabase db = DataBaseManager.getInstance().openDatabase();
        String where = Course.COLUMN_CourseId + " = " + rowId;
        int b = db.delete(Course.TABLE_Name, where, null);
        DataBaseManager.getInstance().closeDatabase();
        if (b != -1)
            return true;
        return false;
    }

    public void deleteAllRows(){
        Cursor c = getAllRows();
        long rowId = c.getColumnIndexOrThrow(Course.COLUMN_CourseId);
        if(c.moveToFirst()){
            do{
                deleteRow(c.getLong((int)rowId));
            }while(c.moveToNext());
        }
        c.close();

    }

    public Cursor getAllRows(){
        SQLiteDatabase db = DataBaseManager.getInstance().openDatabase();
        String where = null;
        Cursor c = db.query(true, Course.TABLE_Name, Course.ALL_ROWS, where, null, null, null, null, null);
        if (c!= null){
            c.moveToFirst();
        }
        DataBaseManager.getInstance().closeDatabase();
        return c;
    }

    public Cursor getRow(long rowId){
        SQLiteDatabase db = DataBaseManager.getInstance().openDatabase();
        String where = Course.COLUMN_CourseId + " = " + rowId;
        Cursor c = db.query(true, Course.TABLE_Name,Course.ALL_ROWS, where, null,null,null,null,null);

        if(c!=null){
            c.moveToFirst();
        }
        DataBaseManager.getInstance().closeDatabase();

        return c;
    }

    //returning all rows with same foreign id
    public Cursor getRows(long foreignKey){
        SQLiteDatabase db = DataBaseManager.getInstance().openDatabase();
        String where = Course.COLUMN_SemesterId + " = '" + foreignKey + "'";
        Cursor c = db.query(true,Course.TABLE_Name,Course.ALL_ROWS,where,null,null,null,null,null);
        c.moveToFirst();
        DataBaseManager.getInstance().closeDatabase();
        return c;

    }


    public boolean updateRow(long rowId, Course course){
        SQLiteDatabase db= DataBaseManager.getInstance().openDatabase();
        String where = Course.COLUMN_CourseId + " = " + rowId;
        ContentValues newValues = new ContentValues();
        newValues.put(Course.COLUMN_CourseName, course.getCourseName());
        newValues.put(Course.COLUMN_SemesterId, course.getSemesterId());
        //Možda će trebati ipak postaviti ECTS
        //newValues.put(Course.COLUMN_CourseECTS, course.getCourseECTS());
        if(db.update(Course.TABLE_Name,newValues, where, null)==-1){
            DataBaseManager.getInstance().closeDatabase();
            return false;
        }
        else{
            DataBaseManager.getInstance().closeDatabase();
            return true;
        }
    }

    public boolean findRow(String courseName){
        SQLiteDatabase db = DataBaseManager.getInstance().openDatabase();
        String where = Course.COLUMN_CourseName + " = '" + courseName +"'";

        Cursor c  = db.query(true,Course.TABLE_Name,Course.ALL_ROWS,where,null,null,null,null,null);
        boolean exist = (c.getCount()>0);
        c.close();
        return exist;
    }

}
