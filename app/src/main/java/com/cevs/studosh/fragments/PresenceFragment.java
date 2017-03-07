package com.cevs.studosh.fragments;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cevs.studosh.Dialogs.DialogHelper;
import com.cevs.studosh.MainActivity;
import com.cevs.studosh.R;
import com.cevs.studosh.data.model.Presence;
import com.cevs.studosh.data.repo.PresenceRepo;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import java.sql.RowId;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.R.attr.type;

/**
 * Created by TOSHIBA on 03.10.2016..
 */

public class PresenceFragment extends Fragment {
    private View view;
    private TextView eventType;
    private long foreignKey;
    private PresenceRepo presenceRepo;
    private Cursor cursor;
    private int color;
    private Button presenceInfo;
    private ImageButton deleteRecord;
    private static final int PRESENT = 1;
    private static final int ABSENT = 2;
    private static final int SIGNED = 3;
    private static final int UNSIGNED = 4;
    private CompactCalendarView compactCalendarView;
    private Date selectedDate;
    private Date lastSelectedDate;
    private Spinner calendarTypeSpinner;
    private int cType;
    private long milliseconds;
    private int rowId;

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

        if(savedInstanceState!=null)
            selectedDate = (Date) savedInstanceState.getSerializable("Selected Date");
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
                view = inflater.inflate(R.layout.fragment_presence,container,false);
                eventType = (TextView) view.findViewById(R.id.textView_eventType);
                compactCalendarView = (CompactCalendarView) view.findViewById(R.id.compactcalendar_view);
                calendarTypeSpinner = (Spinner)view.findViewById(R.id.spinnerCalendar);
                presenceInfo = (Button) view.findViewById(R.id.buttonPresenceInfo);
                deleteRecord = (ImageButton) view.findViewById(R.id.imageButton_deleteRecord);

            }catch (InflateException e){
                Toast.makeText(getContext(),e+"",Toast.LENGTH_LONG).show();
            }
        }

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 1 - puni krug
        // 2 - Obrub
        // 3 - tocka

        compactCalendarView.setEventIndicatorStyle(2);
        rowId=-1;

        //compactCalendarView.setCurrentDayBackgroundColor(R.color.navigationTabStrip);
        String[] daysOfTheWeek = getResources().getStringArray(R.array.daysOfWeek);
        final String[] calendarTypes = getResources().getStringArray(R.array.calendarTypes);
        compactCalendarView.setDayColumnNames(daysOfTheWeek);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),R.layout.spinner_item_layout,R.id.spinnerItem,calendarTypes);
        adapter.setDropDownViewResource(R.layout.spinner_item_layout);
        calendarTypeSpinner.setAdapter(adapter);

        calendarTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                cType = i;
                cursor = presenceRepo.getAllRows(foreignKey, cType);
                compactCalendarView.removeAllEvents();
                if (cursor.getCount()>0){

                    getDates();
                }

                ((MainActivity)getActivity()).setCalendarType(cType);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        final TextView date = (TextView) view.findViewById(R.id.date);

        //Novo
        if(selectedDate != null){
            compactCalendarView.setCurrentDate(selectedDate);
            String sdf  = new SimpleDateFormat("dd.MM.yyyy.").format(selectedDate);
            date.setText(sdf);

        }else{

            String sdf  = new SimpleDateFormat("dd.MM.yyyy.").format(new Date());
            date.setText(sdf);


        }

        presenceRepo = new PresenceRepo();
        cursor = presenceRepo.getAllRows(foreignKey, cType);

        if(cursor.getCount()>0){
            getDates();
        }
        cursor.close();

        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                getSelectedDate(dateClicked, date);
                printEventName(dateClicked);
                selectedDate=dateClicked;
                setButtonAvailability();

            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                selectedDate = firstDayOfNewMonth;
                //lastSelectedDate = selectedDate;
                getSelectedDate(firstDayOfNewMonth, date);
                setButtonAvailability();
            }
        });




        presenceInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogHelper dialogHelper = new DialogHelper();
                dialogHelper.setPresenceInfoDialog();
            }
        });


        deleteRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //Možda napraviti da se omogući button za brisanje kada ima događaja a kada nema da se ugasi
                if(rowId!=-1){
                    presenceRepo.deleteRow(rowId);
                    Toast.makeText(getActivity(),"Obrisano",Toast.LENGTH_SHORT).show();
                    ((MainActivity)getActivity()).setFragments(foreignKey);

                }

            }
        });

        //Setting variable lastSelctedDate on todays dates with hours, minutes and seconds set to 0
        //This fix is mandatory. Without it user cant delete event on first load of presenceFragment
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        selectedDate = cal.getTime();
        printEventName(selectedDate);
        setButtonAvailability();
    }


    public void setButtonAvailability(){
        //We are subtracting hours, minutes and seconds from date because we need date format like: Tue Mar 07 2017 00:00:00 GMT+0100
        long time = selectedDate.getTime();
        long subtract = time%100000;
        milliseconds = selectedDate.getTime()- subtract;

        rowId = searchIfTaken(milliseconds);

        if(rowId!=-1)
        {
            deleteRecord.setEnabled(true);
        }
        else{
            deleteRecord.setEnabled(false);
        }
    }

    public void getDates(){
        presenceRepo = new PresenceRepo();
        cursor = presenceRepo.getAllRows(foreignKey, cType);
        ArrayList<String> dates = new ArrayList<String>();
        ArrayList<Integer> colors = new ArrayList<Integer>();
        ArrayList<Integer> types = new ArrayList<Integer>();

        int type;

        do{
            dates.add(cursor.getString(cursor.getColumnIndex(Presence.COLUMN_DateTime)));
            type = cursor.getInt(cursor.getColumnIndex(Presence.COLUMN_Presence));
            types.add(type);
            if (type == PRESENT){
                colors.add(ContextCompat.getColor(getContext(),R.color.circlePresent));
            }
            else if (type == SIGNED){
                colors.add(ContextCompat.getColor(getContext(),R.color.circleSigned));

            }
            else if (type == ABSENT){
                colors.add(ContextCompat.getColor(getContext(),R.color.circleAbsent));

            }
            else if (type == UNSIGNED){
                colors.add(ContextCompat.getColor(getContext(),R.color.circleUnsigned));
            }



        }while(cursor.moveToNext());
        cursor.close();


        ArrayList<Event> events = new ArrayList<Event>();
        for(int i = 0; i<dates.size();i++){
            long milliseconds = Long.parseLong(dates.get(i));
            color  = colors.get(i);
            type = types.get(i);
            Event event = new Event(color, milliseconds,type);
            events.add(event);
        }

        compactCalendarView.addEvents(events);


    }

    /*public void getEvents(){

         //Zašto smo tu makli sve evente?

        //compactCalendarView.removeAllEvents();
        ArrayList<Event> events = new ArrayList<Event>();
        for(int i = 0; i<dates.size();i++){
            long milliseconds = Long.parseLong(dates.get(i));
            color  = colors.get(i);
            Event event = new Event(color, milliseconds);
            events.add(event);
        }

        compactCalendarView.addEvents(events);
    }*/

    private int searchIfTaken(long time){
        Cursor cursor = presenceRepo.getAllRows(foreignKey, cType);

        while(!cursor.isAfterLast() && cursor.getCount()>0){
            long milliseconds = Long.parseLong(cursor.getString(cursor.getColumnIndex(Presence.COLUMN_DateTime)));
            if (milliseconds == time){
                int takenRowId = cursor.getInt(cursor.getColumnIndex(Presence.COLUMN_PresenceId));
                return takenRowId;
            }

            cursor.moveToNext();
        }
        cursor.close();
        return  -1;
    }



    public void getSelectedDate(Date date, TextView textView){
        String day = (String) android.text.format.DateFormat.format("dd",date);
        String month = (String) android.text.format.DateFormat.format("MM", date);
        String year =  (String) android.text.format.DateFormat.format("yyyy", date);
        textView.setText(day+"."+month+"."+year+".");
    }

    /*
        If there is event created for clicked date, print its name in textView below calendar
        @param = date that is passed from compactCalendar listener
     */
    public void printEventName(Date date){

        List<Event> events = compactCalendarView.getEvents(date);
        if(events.size()>0){
            if ((events.get(0).getType())==PRESENT){
                eventType.setText("Present");

            }else if((events.get(0).getType())==SIGNED){
                eventType.setText("Signed");
                eventType.setTextColor(events.get(0).getColor());
            }else if((events.get(0).getType())==ABSENT){
                eventType.setText("Absent");
                eventType.setTextColor(events.get(0).getColor());
            }else if((events.get(0).getType())==UNSIGNED){
                eventType.setText("Unsigned");
                eventType.setTextColor(events.get(0).getColor());
            }
        }
        else{
            eventType.setText("Empty");
            eventType.setTextColor(ContextCompat.getColor(getActivity(), R.color.black));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("Selected Date", selectedDate);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        if(savedInstanceState!=null){
            selectedDate =  (Date) savedInstanceState.getSerializable("Selected Date");
        }

    }
}
