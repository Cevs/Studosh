package com.cevs.studosh.data.model;

/**
 * Created by TOSHIBA on 28.09.2016..
 */

public class Content {
    public  static final String TABLE_Name = "Content";
    public static final String COLUMN_ContentId = "_id";
    public static final String COULUMN_Criteria = "Criteria";
    public static final String COLUMN_Points = "Points";
    public static final String COLUMN_MaxPoints ="MaxPoints";
    public static final String COLUMN_CourseId = "CourseId";

    private int id;
    private String criteria;
    private String points;
    private String maxPoints;
    private int foreignKey;

    public int getContentId(){return id;}

    public void setContentId (int id){this.id = id;}

    public String getCriteria(){return criteria;}

    public void setCriteria(String criteria){this.criteria = criteria;}

    public String getPoints(){return points;}

    public void setPoints(String points){this.points = points;}

    public String getMaxPoints(){return maxPoints;}

    public void setMaxPoints(String maxPoints){this.maxPoints = maxPoints;}

    public int getCourseId(){return foreignKey;}

    public void setCourseId(int foreginKey){this.foreignKey = foreignKey;}

}
