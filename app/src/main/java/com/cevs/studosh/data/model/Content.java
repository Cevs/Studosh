package com.cevs.studosh.data.model;

/**
 * Created by TOSHIBA on 28.09.2016..
 */

public class Content {
    public  static final String TABLE_Name = "Content";
    public static final String COLUMN_ContentId = "_id";
    public static final String COLUMN_Criteria = "Criteria";
    public static final String COLUMN_Points = "Points";
    public static final String COLUMN_MaxPoints ="MaxPoints";
    public static final String COLUMN_fk_CourseId = "CourseId";
    public static final String[] ALL_ROWS = {COLUMN_ContentId, COLUMN_Criteria, COLUMN_Points, COLUMN_MaxPoints, COLUMN_fk_CourseId};


    private long id;
    private String criteria;
    private double points;
    private double maxPoints;
    private long foreignKey;

    public long getContentId(){return id;}

    public void setContentId (int id){this.id = id;}

    public String getCriteria(){return criteria;}

    public void setCriteria(String criteria){this.criteria = criteria;}

    public double getPoints(){return points;}

    public void setPoints(double points){this.points = points;}

    public double getMaxPoints(){return maxPoints;}

    public void setMaxPoints(double maxPoints){this.maxPoints = maxPoints;}

    public long getCourseId(){return foreignKey;}

    public void setCourseId(long foreignKey){this.foreignKey = foreignKey;}

}
