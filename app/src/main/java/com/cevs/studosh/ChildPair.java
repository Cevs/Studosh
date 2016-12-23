package com.cevs.studosh;

/**
 * Created by TOSHIBA on 03.11.2016..
 */

public class ChildPair {
    //Contains the name of course and the ID in database
    private String name;
    private long rowId;

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public void setRowId(long rowId){
        this.rowId = rowId;
    }

    public long getRowId(){
        return rowId;
    }
}
