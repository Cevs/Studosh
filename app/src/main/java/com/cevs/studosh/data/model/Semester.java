package com.cevs.studosh.data.model;

/**
 * Created by TOSHIBA on 27.10.2016..
 */

public class Semester {
    public static final String TABLE_Name = "Semester";
    public static final String COLUMN_SemesterId = "_id";
    public static final String COLUMN_SemesterName = "Name";
    public static final String[] ALL_ROWS = {Semester.COLUMN_SemesterId, Semester.COLUMN_SemesterName};

    private long id;
    private String name;

    public void setId(long id){
        this.id = id;
    }

    public long getId(){
        return id;
    }
    public void setSemesterName(String name){
        this.name = name;
    }

    public String getSemesterName(){
        return name;
    }

    //This method using ArrayAdapter in class CourseDialog
    //It returns name of current Semester while filling adapter
    public String toString(){
        return name;
    }
}
