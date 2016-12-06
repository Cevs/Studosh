package com.cevs.studosh.data.model;

import java.io.Serializable;
import java.sql.Date;

/**
 * Created by TOSHIBA on 18.10.2016..
 */

public class Presence implements Serializable {
    public static final String TABLE_Name = "Presence";
    public static final String COLUMN_PresenceId = "_id";
    public static final String COLUMN_DateTime = "DateTime";
    public static final String COLUMN_Presence = "PresenceType";
    public static final String COLUMN_CalendarType = "CalendarType";
    public static final String COLUMN_CourseId = "CourseId";
    public static final String[] ALL_ROWS = {COLUMN_PresenceId, COLUMN_DateTime, COLUMN_Presence,COLUMN_CalendarType, COLUMN_CourseId};

    private long id;
    private String dateTime;
    private int presence;
    private long foreignKey;
    private int type;

    public void setPresenceId(long id){
        this.id = id;
    }

    public long getPresenceId(){return id;}

    public void setDateTime(String dateTime){this.dateTime = dateTime;}

    public String getDateTime(){return dateTime;}

    public void setPresenceType(int presence){this.presence = presence;}

    public int getPresenceType(){return presence;}

    public void setCalendarType(int type){this.type = type;}

    public int getCalendarType(){return type;}

    public void setForeignKey(long foreignKey){this.foreignKey = foreignKey;}

    public long getForeignKey(){return foreignKey;}
}
