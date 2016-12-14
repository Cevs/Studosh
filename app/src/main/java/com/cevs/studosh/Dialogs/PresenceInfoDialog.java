package com.cevs.studosh.Dialogs;

;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.cevs.studosh.R;

/**
 * Created by TOSHIBA on 08.12.2016..
 */

public class PresenceInfoDialog extends DialogFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //tu ce ici naslov za odredjeni kalendar
        builder.setTitle("Predavanja");


        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_presence_info,null);
        builder.setView(view);

        TextView nPresence = (TextView) view.findViewById(R.id.presentNumber);
        TextView nSigned = (TextView) view.findViewById(R.id.signedNumber);
        TextView nAbsent = (TextView) view.findViewById(R.id.absentNumber);
        TextView nUnsigned = (TextView) view.findViewById(R.id.unsignedNumber);

        Dialog dialog = builder.create();
        return dialog;
    }
}
