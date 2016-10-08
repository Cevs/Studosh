package com.cevs.studosh.data;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by TOSHIBA on 28.09.2016..
 */

public class DataBaseManager {

    private int mOpenCounter = 0;

    private static DataBaseManager instance;
    private static SQLiteOpenHelper mDatabaseHelper;
    private SQLiteDatabase mDatabase;

    public static synchronized void initializeInstance(SQLiteOpenHelper helper){
        if (instance == null){
            instance = new DataBaseManager();
            mDatabaseHelper = helper;
        }
    }

    public static synchronized  DataBaseManager getInstance(){
        if (instance == null){
            throw new IllegalStateException(DataBaseManager.class.getSimpleName() +
                    " is not initialized, call initializeInstance(..) method first. ");
        }
        return instance;
    }

    public synchronized  SQLiteDatabase openDatabase(){
        mOpenCounter += 1;
        if (mOpenCounter == 1) {
            mDatabase = mDatabaseHelper.getWritableDatabase();
            mDatabase.execSQL("PRAGMA foreign_keys=ON");
        }
        return mDatabase;
    }

    public synchronized  void closeDatabase(){
        mOpenCounter -= 1;
        if (mOpenCounter == 0) {
            mDatabase.close();
        }
    }
}
