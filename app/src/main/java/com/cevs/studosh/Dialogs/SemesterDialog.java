package com.cevs.studosh.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import com.cevs.studosh.R;
import com.cevs.studosh.data.model.Semester;
import com.cevs.studosh.data.repo.SemesterRepo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by TOSHIBA on 29.10.2016..
 */

public class SemesterDialog extends DialogFragment {

    LayoutInflater inflater;
    View view;
    EditText semester;
    String nSemester;
    ArrayList<String> spinnerArray;
    SemesterRepo semesterRepo;


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.dialog_semester,null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);



        builder.setTitle("Dodaj semestar");

        builder.setPositiveButton("Dodaj", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                semester = (EditText)view.findViewById(R.id.editText_dialogSemester_semester);
                boolean bSemester = true;

                nSemester = semester.getText().toString();

                if(nSemester.length() == 0){
                    bSemester = false;
                }else{
                    nSemester = "Semestar " + nSemester;
                }



                if(bSemester){
                    Semester semester = new Semester();
                    SemesterRepo semesterRepo = new SemesterRepo();

                    //Checks if row already exists
                    if(semesterRepo.findRow(nSemester)){
                        Toast.makeText(getActivity(),"Vec postoji taj semestar",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        semester.setSemesterName(nSemester);
                        long semesterId = semesterRepo.insertRow(semester);

                        if(semesterId!=-1){
                            Toast.makeText(getActivity(),"Semestar dodan", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(getActivity(),"Greška pri unosu", Toast.LENGTH_SHORT).show();
                        }
                    }

                }
                else{
                    Toast.makeText(getActivity(),"Unesite sve podatke",Toast.LENGTH_SHORT).show();
                }


            }
        });

        builder.setNegativeButton("Odustani", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getActivity(),"Odustali ste",Toast.LENGTH_SHORT).show();
            }
        });



        Dialog dialog = builder.create();
        return dialog;
    }
}
