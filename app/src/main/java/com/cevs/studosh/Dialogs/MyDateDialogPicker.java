package com.cevs.studosh.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.Toast;

import com.cevs.studosh.MainActivity;
import com.cevs.studosh.ConvertIntoMilliseconds;
import com.cevs.studosh.R;
import com.cevs.studosh.data.model.Presence;
import com.cevs.studosh.data.repo.PresenceRepo;

/**
 * Created by TOSHIBA on 21.11.2016..
 */

public class MyDateDialogPicker extends DialogFragment implements View.OnClickListener {


    private Boolean bPresent;
    private Boolean bAbsent;
    private Boolean bSigned;
    private Boolean bUnsigned;
    private Boolean takenDate;
    private DatePicker datePicker;
    private Presence presence;
    private PresenceRepo presenceRepo;
    private long foreignKey;
    private DialogHelper dialogHelper;
    private Toast toast;
    private int cType;
    private long takenRowId;
    private static final int PRESENT = 1;
    private static final int ABSENT = 2;
    private static final int SIGNED = 3;
    private static final int UNSIGNED = 4;

    public static MyDateDialogPicker newInstance(long foreignKey, int type){
        MyDateDialogPicker dialog = new MyDateDialogPicker();
        Bundle args = new Bundle();
        args.putLong("Foreign Key",foreignKey);
        args.putInt("Calendar Type",type);
        dialog.setArguments(args);
        return dialog;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        foreignKey = getArguments().getLong("Foreign Key");
        cType = getArguments().getInt("Calendar Type");

    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater  = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_date_picker, null);
        builder.setView(view);

        datePicker = (DatePicker) view.findViewById(R.id.datePicker);
        dialogHelper  = new DialogHelper();

        bPresent = false;
        bSigned = false;
        bAbsent = false;
        bUnsigned = false;

        Button buttonPresent = (Button) view.findViewById(R.id.button_present);
        Button buttonSigned = (Button) view.findViewById(R.id.button_signed);
        Button buttonAbsent = (Button) view.findViewById(R.id.button_absent);
        Button buttonUnsigned = (Button) view.findViewById(R.id.button_unsigned);
        ImageButton buttonCancel = (ImageButton) view.findViewById(R.id.imageButton_cancel);

        buttonPresent.setOnClickListener(this);
        buttonSigned.setOnClickListener(this);
        buttonAbsent.setOnClickListener(this);
        buttonUnsigned.setOnClickListener(this);
        buttonCancel.setOnClickListener(this);



        Dialog dialog = builder.create();
        return dialog;
    }




    private void saveDate(){
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year = datePicker.getYear();
        //because of indexing it returns month-1 so we need to add +1 to get picked month
        month = month+1;
        long lDateMilliseconds = new ConvertIntoMilliseconds(day,month,year).getMillisecodns();
        String sDate = day+"."+month+"."+year;

        presence = new Presence();
        presenceRepo = new PresenceRepo();
        String[] typeOfPresence = getResources().getStringArray(R.array.presenceType);
        if(bPresent){
            insertInDb(lDateMilliseconds,PRESENT,cType,foreignKey, sDate,typeOfPresence[0]);
        }

        else if(bSigned){
            insertInDb(lDateMilliseconds,SIGNED,cType,foreignKey,sDate, typeOfPresence[1]);
        }
        else if(bAbsent){
            insertInDb(lDateMilliseconds,ABSENT,cType,foreignKey,sDate,typeOfPresence[2]);
        }
        else if(bUnsigned){
            insertInDb(lDateMilliseconds,UNSIGNED,cType,foreignKey, sDate, typeOfPresence[3]);
        }
        else
            showToast("Zatvoreno","");

        //refresh viewpager after changing
        //((MainActivity)getActivity()).setFragments(foreignKey);
    }


    private boolean searchIfTaken(long time){
        Cursor cursor = presenceRepo.getAllRows(foreignKey, cType);

        while(!cursor.isAfterLast() && cursor.getCount()>0){
            long milliseconds = Long.parseLong(cursor.getString(cursor.getColumnIndex(Presence.COLUMN_DateTime)));
            if (milliseconds == time){
                takenRowId = cursor.getInt(cursor.getColumnIndex(Presence.COLUMN_PresenceId));
                return true;
            }

            cursor.moveToNext();
        }
        cursor.close();
        return  false;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_present:{
                bPresent = true;
                bSigned = false;
                bAbsent = false;
                bUnsigned = false;
                saveDate();

            }break;

            case R.id.button_signed:{
                bSigned = true;
                bPresent = false;
                bAbsent = false;
                bUnsigned = false;
                saveDate();

            }break;

            case R.id.button_absent:{
                bAbsent = true;
                bPresent = false;
                bSigned = false;
                bUnsigned = false;
                saveDate();

            }break;

            case R.id.button_unsigned:{
                bUnsigned = true;
                bPresent = false;
                bSigned = false;
                bAbsent = false;
                saveDate();
            }break;

            case R.id.imageButton_cancel:{
                ((MainActivity)getActivity()).setFragments(foreignKey);
                dismiss();

            }break;
        }
    }

    private void showToast(String text, String date){
        if(toast!=null)
            toast.cancel();
        toast = Toast.makeText(getActivity(),text+date,Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM,0,0);
        toast.show();
    }

    private void insertInDb(long lTime, int present, int cType, long foreignKey, String sDate, String message) {

        //Look in db if there is already record of that date for that course
        takenDate = searchIfTaken(lTime);
        if(!takenDate){
            presence.setDateTime(lTime+"");
            presence.setPresenceType(present);
            presence.setCalendarType(cType);
            presence.setForeignKey(foreignKey);

            if(presenceRepo.insertRow(presence)>0){
                showToast(message+": ",sDate);
            }
        }
        else{
            presence.setDateTime(lTime+"");
            presence.setPresenceType(present);
            presence.setCalendarType(cType);
            presence.setForeignKey(foreignKey);
            dialogHelper.setRewriteDateDialog(presence,takenRowId);

        }

    }


}
