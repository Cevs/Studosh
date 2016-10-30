package com.cevs.studosh.data.model;

/**
 * Created by TOSHIBA on 28.09.2016..
 */

public class Course {

    public static final String TABLE_Name = "Course";
    public static final String COLUMN_CourseId = "_id";
    public static final String COLUMN_CourseName = "CourseName";
    //Novo
    public static final String COLUMN_SemesterId = "SemesterId";
    public static final String[] ALL_ROWS = {COLUMN_CourseId, COLUMN_CourseName, COLUMN_SemesterId};

    private  long courseId;
    private String name;
    private String semester;
    private long semesterId;


    public long getCourseId(){return courseId;}

    public void setCourseId(int courseId){this.courseId = courseId;}

    public String getCourseName(){return name;}

    public void setCourseName(String name){this.name = name;}

    public void setSemesterId(long semesterId){
        this.semesterId = semesterId;
    }

    public long getSemesterId(){
        return semesterId;
    }

}
