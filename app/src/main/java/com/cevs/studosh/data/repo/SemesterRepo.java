package com.cevs.studosh.data.repo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cevs.studosh.data.DataBaseManager;
import com.cevs.studosh.data.model.Semester;

/**
 * Created by TOSHIBA on 27.10.2016..
 */

public class SemesterRepo {

    public static String createTable() {
        return "CREATE TABLE " + Semester.TABLE_Name + "("
                + Semester.COLUMN_SemesterId + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Semester.COLUMN_SemesterName + " TEXT, "
                + Semester.COLUMN_SemesterNumber + " TEXT );";
    }

    public long insertRow(Semester semester){
        long semesterId;
        SQLiteDatabase db = DataBaseManager.getInstance().openDatabase();
        ContentValues values = new ContentValues();
        values.put(Semester.COLUMN_SemesterName, semester.getSemesterName());
        values.put(Semester.COLUMN_SemesterNumber, semester.getSemesterNumber());
        semesterId  = db.insert(Semester.TABLE_Name,null,values);
        DataBaseManager.getInstance().closeDatabase();
        return semesterId;
    }

    public boolean deleteRow(long semesterId){
        SQLiteDatabase db = DataBaseManager.getInstance().openDatabase();
        String where = Semester.COLUMN_SemesterId + " = " + semesterId;
        int b = db.delete(Semester.TABLE_Name,where,null);
        DataBaseManager.getInstance().closeDatabase();
        if(b!=-1){
            return true;
        }
        return false;
    }

    public void deleteAllRows(){
        Cursor c = getAllRows();
        long rowId = c.getColumnIndexOrThrow(Semester.COLUMN_SemesterId);
        if (c.moveToFirst()){
            do{
                deleteRow(c.getLong((int)rowId));
            }while(c.moveToNext());
        }
        c.close();
    }

    public Cursor getAllRows(){
        SQLiteDatabase db = DataBaseManager.getInstance().openDatabase();
        String where = null;
        Cursor c = db.query(true, Semester.TABLE_Name, Semester.ALL_ROWS, where, null, null, null, null, null);
        if (c!= null){
            c.moveToFirst();
        }
        DataBaseManager.getInstance().closeDatabase();
        return c;
    }

    public Cursor getRow(long rowId){
        SQLiteDatabase db = DataBaseManager.getInstance().openDatabase();
        String where = Semester.COLUMN_SemesterId + " = " + rowId;
        Cursor c = db.query(true, Semester.TABLE_Name,Semester.ALL_ROWS, where, null,null,null,null,null);

        if(c!=null){
            c.moveToFirst();
        }
        DataBaseManager.getInstance().closeDatabase();

        return c;
    }

    public boolean updateRow(long rowId, Semester semester){
        SQLiteDatabase db= DataBaseManager.getInstance().openDatabase();
        String where = Semester.COLUMN_SemesterId + " = " + rowId;
        ContentValues newValues = new ContentValues();
        newValues.put(Semester.COLUMN_SemesterName, semester.getSemesterName());
        newValues.put(Semester.COLUMN_SemesterNumber,semester.getSemesterNumber());

        if(db.update(Semester.TABLE_Name,newValues, where, null)==-1){
            DataBaseManager.getInstance().closeDatabase();
            return false;
        }
        else{
            DataBaseManager.getInstance().closeDatabase();
            return true;
        }
    }

    public boolean findRow(String semesterNumber){
        SQLiteDatabase db = DataBaseManager.getInstance().openDatabase();
        String where = Semester.COLUMN_SemesterNumber + " = '" + semesterNumber +"'";

        Cursor c  = db.query(true,Semester.TABLE_Name,Semester.ALL_ROWS,where,null,null,null,null,null);
        boolean exist = (c.getCount()>0);
        c.close();
        return exist;
    }
}
