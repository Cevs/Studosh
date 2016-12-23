package com.cevs.studosh.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.cevs.studosh.MainActivity;
import com.cevs.studosh.data.model.Presence;
import com.cevs.studosh.data.repo.PresenceRepo;

import java.io.Serializable;

/**
 * Created by TOSHIBA on 25.11.2016..
 */

public class RewriteDateDialog extends DialogFragment {
    private String lDate;
    private int calendarType;
    private int presenceType;
    private long foreignKey;
    private long takenRowId;



    public static RewriteDateDialog newInstance(Presence presence, long rowId){
        RewriteDateDialog dialog = new RewriteDateDialog();
        Bundle args = new Bundle();

        args.putSerializable("Presence",  presence);
        args.putLong("Row Id",rowId);
        dialog.setArguments(args);
        return dialog;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Presence presence = (Presence) getArguments().getSerializable("Presence");
        calendarType = presence.getCalendarType();
        presenceType = presence.getPresenceType();
        foreignKey = presence.getForeignKey();
        lDate = presence.getDateTime();
        takenRowId = getArguments().getLong("Row Id");

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Prijepis datuma");
        builder.setMessage("Da li ste sigurni da želite prepisati datum ? ");

        builder.setPositiveButton("Da", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Presence presence = new Presence();
                PresenceRepo presenceRepo = new PresenceRepo();

                presence.setDateTime(lDate+"");
                presence.setCalendarType(calendarType);
                presence.setPresenceType(presenceType);
                presence.setForeignKey(foreignKey);

                try{
                    if(presenceRepo.updateRow(takenRowId,presence)){
                        Toast.makeText(getActivity(),"Prijepis obavljen",Toast.LENGTH_SHORT).show();

                    }
                    else
                        Toast.makeText(getActivity(),"Neuspjelo",Toast.LENGTH_SHORT).show();
                }catch(Exception e){
                    Toast.makeText(getActivity(),e+"",Toast.LENGTH_SHORT).show();
                }
            }

        });

        builder.setNegativeButton("Ne", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getActivity(),"Ne",Toast.LENGTH_SHORT).show();
            }
        });



        Dialog dialog = builder.create();
        return dialog;

    }


}
