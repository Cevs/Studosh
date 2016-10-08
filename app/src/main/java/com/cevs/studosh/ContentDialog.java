package com.cevs.studosh;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.cevs.studosh.data.DataBaseManager;
import com.cevs.studosh.data.model.Content;
import com.cevs.studosh.data.repo.ContentRepo;
import com.cevs.studosh.fragments.CriteriaFragment;

import java.util.ArrayList;

/**
 * Created by TOSHIBA on 06.10.2016..
 */

public class ContentDialog extends DialogFragment {
    LayoutInflater inflater;
    View view;
    Content content;
    ContentRepo contentRepo;
    EditText criterion;
    EditText points;
    EditText maxPoints;
    long courseId;

    public static ContentDialog newInstance(long id){
        ContentDialog contentDialog = new ContentDialog();
        Bundle args = new Bundle();
        args.putLong("Id",id);
        contentDialog.setArguments(args);
        return contentDialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        courseId = getArguments().getLong("Id");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.dialog_content,null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        builder.setTitle("Dodavanje kriterija iz modela pracenja");

        builder.setPositiveButton("Dodaj", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                content = new Content();
                contentRepo = new ContentRepo();

                criterion = (EditText) view.findViewById(R.id.eT_criterion);
                points = (EditText) view.findViewById(R.id.eT_points);
                maxPoints = (EditText) view.findViewById(R.id.eT_maxPoints);


                content.setCriteria(criterion.getText().toString());
                content.setPoints(Double.parseDouble(points.getText().toString()));
                content.setMaxPoints(Double.parseDouble(maxPoints.getText().toString()));
                content.setCourseId(courseId);
                contentRepo.insertRow(content);

                AdapterData adapterData = new AdapterData(getActivity(), courseId);
                adapterData.init();

            }
        });

        builder.setNegativeButton("Prekini", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getActivity(),"Prekini je stisnuto",Toast.LENGTH_SHORT).show();
            }
        });

        Dialog dialog = builder.create();

        return dialog;
    }


}
