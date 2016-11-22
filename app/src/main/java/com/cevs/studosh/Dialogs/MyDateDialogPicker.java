package com.cevs.studosh.Dialogs;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.Toast;

import com.cevs.studosh.MainActivity;
import com.cevs.studosh.R;
import com.cevs.studosh.data.model.Presence;
import com.cevs.studosh.data.repo.PresenceRepo;
import com.cevs.studosh.fragments.PresenceFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by TOSHIBA on 21.11.2016..
 */

public class MyDateDialogPicker extends DialogFragment implements View.OnClickListener {

    View view;
    LayoutInflater inflater;
    int day;
    int month;
    int year;

    Boolean bPresent;
    Boolean bAbsent;
    Boolean bSigned;
    Boolean bUnsigned;
    Boolean takenDate;

    Button buttonPresent;
    Button buttonAbsent;
    Button buttonSigned;
    Button buttonUnsigned;
    ImageButton buttonCancel;

    DatePicker datePicker;
    Presence presence;
    PresenceRepo presenceRepo;
    long foreignKey;
    Cursor cursor;

    static final int PRESENT = 1;
    static final int ABSENT = 2;
    static final int SIGNED = 3;
    static final int UNSIGNED = 4;


    public static MyDateDialogPicker newInstance(long foreignKey){
        MyDateDialogPicker dialog = new MyDateDialogPicker();
        Bundle args = new Bundle();
        args.putLong("Foreign Key",foreignKey);
        dialog.setArguments(args);
        return dialog;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        foreignKey = getArguments().getLong("Foreign Key");
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        inflater  = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.dialog_date_picker, null);
        builder.setView(view);

        datePicker = (DatePicker) view.findViewById(R.id.datePicker);

        bPresent = false;
        bSigned = false;
        bAbsent = false;
        bUnsigned = false;

        buttonPresent = (Button) view.findViewById(R.id.button_present);
        buttonSigned = (Button) view.findViewById(R.id.button_signed);
        buttonAbsent = (Button) view.findViewById(R.id.button_absent);
        buttonUnsigned = (Button) view.findViewById(R.id.button_unsigned);
        buttonCancel = (ImageButton) view.findViewById(R.id.imageButton_cancel);

        buttonPresent.setOnClickListener(this);
        buttonSigned.setOnClickListener(this);
        buttonAbsent.setOnClickListener(this);
        buttonUnsigned.setOnClickListener(this);
        buttonCancel.setOnClickListener(this);



        Dialog dialog = builder.create();
        return dialog;
    }

    public void getDate(){
        day = datePicker.getDayOfMonth();
        month = datePicker.getMonth();
        year = datePicker.getYear();
        saveDate();
    }

    public void saveDate(){


        //because of indexing it returns month-1 so we need to add +1 to get picked month
        month = month+1;
        String sDate = year+"-"+month+"-"+day;
        long milliSecsDate = milliseconds(sDate);

        presence = new Presence();
        presenceRepo = new PresenceRepo();

        if(bPresent){
            //Look in db if there is already record of that date for that course
            takenDate = searchIfTaken(milliSecsDate);
            if (!takenDate){

                presence.setDateTime(milliSecsDate+"");
                presence.setPresence(PRESENT);
                presence.setForeignKey(foreignKey);

                if(presenceRepo.insertRow(presence)>0){
                    Toast.makeText(getActivity(),"Prisustvao: "+sDate,Toast.LENGTH_SHORT).show();
                }
            }
            else
                Toast.makeText(getActivity(),"Već postoji zapis",Toast.LENGTH_SHORT).show();


        }

        else if(bSigned){

            takenDate = searchIfTaken(milliSecsDate);
            if(!takenDate){
                presence.setDateTime(milliSecsDate+"");
                presence.setPresence(SIGNED);
                presence.setForeignKey(foreignKey);

                if(presenceRepo.insertRow(presence)>0){
                    Toast.makeText(getActivity(),"Potpisan: "+sDate,Toast.LENGTH_SHORT).show();
                }

            }
            else{
                Toast.makeText(getActivity(),"Već postoji zapis",Toast.LENGTH_SHORT).show();
            }

        }
        else if(bAbsent){
            takenDate = searchIfTaken(milliSecsDate);
            if (!takenDate){
                presence.setDateTime(milliSecsDate+"");
                presence.setPresence(ABSENT);
                presence.setForeignKey(foreignKey);

                if(presenceRepo.insertRow(presence)>0){
                    Toast.makeText(getActivity(),"Odsutan: "+sDate, Toast.LENGTH_SHORT).show();
                }
            }
            else{
                Toast.makeText(getActivity(),"Već postoji zapis",Toast.LENGTH_SHORT).show();
            }
        }
        else if(bUnsigned){
            takenDate = searchIfTaken(milliSecsDate);
            if(!takenDate){
                presence.setDateTime(milliSecsDate+"");
                presence.setPresence(UNSIGNED);
                presence.setForeignKey(foreignKey);

                if(presenceRepo.insertRow(presence)>0){
                    Toast.makeText(getActivity(),"Propustio potpis: "+sDate,Toast.LENGTH_SHORT).show();
                }
            }
            else{
                Toast.makeText(getActivity(),"Već postoji zapis",Toast.LENGTH_SHORT).show();
            }
        }
        else
            Toast.makeText(getActivity(),"Cancel",Toast.LENGTH_SHORT).show();

        //refresh viewpager after changing
        ((MainActivity)getActivity()).setFragments(foreignKey);
    }


    public long milliseconds(String date){
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


    public boolean searchIfTaken(long time){
        cursor = presenceRepo.getAllRows(foreignKey);

        while(!cursor.isAfterLast() && cursor.getCount()>0){
            long milliseconds = Long.parseLong(cursor.getString(cursor.getColumnIndex(Presence.COLUMN_DateTime)));
            if (milliseconds == time)
                return true;
            cursor.moveToNext();
        }
        return  false;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_present:{
                bPresent = true;
                getDate();
                dismiss();
            }break;

            case R.id.button_signed:{
                bSigned = true;
                getDate();
                dismiss();
            }break;

            case R.id.button_absent:{
                bAbsent = true;
                getDate();
                dismiss();
            }break;

            case R.id.button_unsigned:{
                bUnsigned = true;
                getDate();
                dismiss();
            }break;

            case R.id.imageButton_cancel:{
                dismiss();
            }break;
        }
    }
}
