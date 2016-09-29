package com.cevs.studosh.data;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.cevs.studosh.data.model.Course;
import com.cevs.studosh.data.repo.CourseRepo;

/**
 * Created by TOSHIBA on 28.09.2016..
 */

public class DBHelper  extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION =1 ;
    private static final String DATABASE_NAME= "studoshDB.db";
    private Context context;


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        SQLiteDatabase db = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try{
            db.execSQL(CourseRepo.createTable());
            //db.execSQL(ContentRepo.createTable());

        }catch(SQLException e){
            Toast.makeText(context, e+"", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try{
            db.execSQL("DROP TABLE IF EXISTS "+ Course.TABLE_Name);
            //db.execSQL("DROP TABLE IF EXISTS " + Content.TABLE);
            onCreate(db);
        }catch (SQLException e){
            Toast.makeText(context, e+"", Toast.LENGTH_LONG).show();
        }

    }
}