package com.cevs.studosh.data.model;

import java.sql.Date;

/**
 * Created by TOSHIBA on 18.10.2016..
 */

public class Presence {
    public static final String TABLE_Name = "Presence";
    public static final String COLUMN_PresenceId = "_id";
    public static final String COLUMN_DateTime = "DateTime";
    public static final String COLUMN_Present = "Present";
    public static final String COLUMN_CourseId = "CourseId";
    public static final String[] ALL_ROWS = {COLUMN_PresenceId, COLUMN_DateTime, COLUMN_Present, COLUMN_CourseId};

    private long id;
    private String dateTime;
    private boolean present;
    private long foreignKey;

    public void setPresenceId(long id){
        this.id = id;
    }

    public long getPresenceId(){return id;}

    public void setDateTime(String dateTime){this.dateTime = dateTime;}

    public String getDateTime(){return dateTime;}

    public void setPresent(boolean present){this.present = present;}

    public boolean getPresent(){return present;}

    public void setForeignKey(long foreignKey){this.foreignKey = foreignKey;}

    public long getForeignKey(){return foreignKey;}
}
