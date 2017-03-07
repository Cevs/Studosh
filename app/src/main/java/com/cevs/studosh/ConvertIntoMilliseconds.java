package com.cevs.studosh;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by TOSHIBA on 21.2.2017..
 */

public class ConvertIntoMilliseconds {
    String date;

    public ConvertIntoMilliseconds(int day, int month, int year){
        this.date=year+"-"+month+"-"+day;
    }

    public long getMillisecodns(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try{
            Date mDate = sdf.parse(date);
            long timeInMilliseconds = mDate.getTime();
            return timeInMilliseconds;
        }
        catch (ParseException e){

        }
        return 0;
    }
}
