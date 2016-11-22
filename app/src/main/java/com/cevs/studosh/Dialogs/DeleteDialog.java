package com.cevs.studosh.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.cevs.studosh.MainActivity;
import com.cevs.studosh.R;
import com.cevs.studosh.data.repo.SemesterRepo;

/**
 * Created by TOSHIBA on 18.11.2016..
 */

public class DeleteDialog extends DialogFragment {


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());


        builder.setTitle("Obriši podatke");
        builder.setMessage("Da li ste sigurni da želite obrisati sve podatke?");

        builder.setPositiveButton("Da", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                SemesterRepo semesterRepo = new SemesterRepo();
                semesterRepo.deleteAllRows();
                ((MainActivity)getActivity()).createExpandableList();
                ((MainActivity)getActivity()).setInitialFragments();

            }
        });

        builder.setNegativeButton("Ne", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        Dialog dialog = builder.create();
        return dialog;
    }
}
