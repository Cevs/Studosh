package com.cevs.studosh.data.model;

/**
 * Created by TOSHIBA on 28.09.2016..
 */

public class Course {

    public static final String TABLE_Name = "Course";
    public static final String COLUMN_CourseId = "_id";
    public static final String COLUMN_CourseName = "CourseName";
    public static final String COLUMN_Semester = "Semester";
    //public static final String COLUMN_MaxPoints = "MaxPoints";
    public static final String[] ALL_ROWS = {Course.COLUMN_CourseId,Course.COLUMN_CourseName, Course.COLUMN_Semester};

    private  int courseId;
    private String name;
    private String semester;

    public int getCourseId(){return courseId;}

    public void setCourseId(int courseId){this.courseId = courseId;}

    public String getCourseName(){return name;}

    public void setCourseName(String name){this.name = name;}

    public String getSemester(){return semester;}

    public void setSemester(String semester){this.semester = semester;}

}
