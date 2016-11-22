package com.cevs.studosh.fragments;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import com.cevs.studosh.R;
import com.cevs.studosh.data.model.Presence;
import com.cevs.studosh.data.repo.PresenceRepo;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by TOSHIBA on 03.10.2016..
 */

public class PresenceFragment extends Fragment {
    View view;
    private long foreignKey;
    TextView date;
    PresenceRepo presenceRepo;
    Cursor cursor;
    ArrayList<String> dates;
    ArrayList<Event> events;
    ArrayList<Integer> colors;
    int color;
    String day;
    String month;
    String year;

    static final int PRESENT = 1;
    static final int ABSENT = 2;
    static final int SIGNED = 3;
    static final int UNSIGNED = 4;

    CompactCalendarView compactCalendarView;
    View rootView;

    public static PresenceFragment newInstance(long foreignKey){
        PresenceFragment fragment = new PresenceFragment();
        Bundle args = new Bundle();
        args.putLong("Foreign Key",foreignKey);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        foreignKey = getArguments().getLong("Foreign Key");

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if(view != null){
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent !=null){
                parent.removeView(view);
            }
        }else{
            try{

                String sdf  = new SimpleDateFormat("dd.MM.yyyy.").format(new Date());
                // 1 - puni krug
                // 2 - Obrub
                // 3 - tocka
                view = inflater.inflate(R.layout.fragment_presence,container,false);
                compactCalendarView = (CompactCalendarView) view.findViewById(R.id.compactcalendar_view);
                //compactCalendarView.setEventIndicatorStyle(2);

                date = (TextView) view.findViewById(R.id.date);
                date.setText(sdf);

                String[] daysOfTheWeek = getResources().getStringArray(R.array.daysOfWeek);
                compactCalendarView.setDayColumnNames(daysOfTheWeek);
                compactCalendarView.setCurrentDayBackgroundColor(R.color.headerColor);
                compactCalendarView.setEventIndicatorStyle(2);


                presenceRepo = new PresenceRepo();
                cursor = presenceRepo.getAllRows(foreignKey);

                if(cursor.getCount()>0){
                    getDates();
                    getEvents();
                }



                compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
                    @Override
                    public void onDayClick(Date dateClicked) {
                        selectedDate(dateClicked, date);
                    }

                    @Override
                    public void onMonthScroll(Date firstDayOfNewMonth) {
                        selectedDate(firstDayOfNewMonth, date);

                    }
                });

            }catch (InflateException e){
                Toast.makeText(getContext(),e+"",Toast.LENGTH_LONG).show();
            }
        }


        return view;
    }

    public void getDates(){
        presenceRepo = new PresenceRepo();
        cursor = presenceRepo.getAllRows(foreignKey);
        dates = new ArrayList<String>();
        colors = new ArrayList<Integer>();

        int type;

        do{
            dates.add(cursor.getString(cursor.getColumnIndex(Presence.COLUMN_DateTime)));
            type = cursor.getInt(cursor.getColumnIndex(Presence.COLUMN_Presence));
            if (type == PRESENT){
                colors.add(Color.GREEN);
            }
            else if (type == SIGNED){
                colors.add(Color.BLUE);

            }
            else if (type == ABSENT){
                colors.add(Color.YELLOW);

            }
            else if (type == UNSIGNED){
                colors.add(Color.RED);

            }


        }while(cursor.moveToNext());


    }

    public void getEvents(){
        events = new ArrayList<Event>();
        int m = dates.size();
        for(int i = 0; i<dates.size();i++){
            long milliseconds = Long.parseLong(dates.get(i));
            color  = colors.get(i);
            Event event = new Event(color, milliseconds);
            events.add(event);
        }

        compactCalendarView.addEvents(events);
    }




    public void selectedDate(Date date, TextView textView){
        day = (String) android.text.format.DateFormat.format("dd",date);
        month = (String) android.text.format.DateFormat.format("MM", date);
        year =  (String) android.text.format.DateFormat.format("yyyy", date);
        textView.setText(day+"."+month+"."+year+".");
    }



}
