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
import android.widget.EditText;
import android.widget.Toast;

import com.cevs.studosh.AdapterData;
import com.cevs.studosh.MainActivity;
import com.cevs.studosh.R;
import com.cevs.studosh.data.DataBaseManager;
import com.cevs.studosh.data.model.Content;
import com.cevs.studosh.data.repo.ContentRepo;

import java.util.zip.Inflater;

/**
 * Created by TOSHIBA on 10.10.2016..
 */

public class UpdateContentDialog extends DialogFragment {
    View view;
    LayoutInflater inflater;
    EditText criterion;
    EditText points;
    EditText maxPoints;
    long courseId;
    String newCriterion;
    double dPoints;
    double dMaxPoints;
    String oldCriterion;
    ContentRepo contentRepo;
    Cursor cursor;
    Content content;
    Dialog dialog;



    public static UpdateContentDialog newInstance(String criterion, long rowId){
        UpdateContentDialog fragment = new UpdateContentDialog();
        Bundle args = new Bundle();
        args.putLong("Id",rowId);
        args.putString("Criterion",criterion);
        fragment.setArguments(args);
        return fragment;
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        DataBaseManager.getInstance().openDatabase();
        inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.dialog_content,null);
        courseId = getArguments().getLong("Id");
        oldCriterion = getArguments().getString("Criterion");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        builder.setTitle("Ažuriraj kriterij");

        criterion = (EditText)view.findViewById(R.id.eT_criterion);
        points = (EditText)view.findViewById(R.id.eT_points);
        maxPoints = (EditText)view.findViewById(R.id.eT_maxPoints);

        contentRepo = new ContentRepo();
        cursor = contentRepo.getRow(courseId,oldCriterion);

        criterion.setText(cursor.getString(cursor.getColumnIndex(Content.COLUMN_Criteria)));
        points.setText(cursor.getDouble(cursor.getColumnIndex(Content.COLUMN_Points))+"");
        maxPoints.setText(cursor.getDouble(cursor.getColumnIndex(Content.COLUMN_MaxPoints))+"");


        builder.setPositiveButton("Ažuriraj", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                newCriterion = criterion.getText().toString();
                dPoints = Double.parseDouble(points.getText().toString());
                dMaxPoints = Double.parseDouble(maxPoints.getText().toString());

                content = new Content();
                content.setCriteria(newCriterion);
                content.setPoints(dPoints);
                content.setMaxPoints(dMaxPoints);
                content.setCourseId(courseId);

                try {
                    contentRepo.updateRow(oldCriterion, content);
                    Toast.makeText(getActivity(),"Ažurirano",Toast.LENGTH_LONG).show();
                    AdapterData adapterData = new AdapterData(getActivity(),courseId);
                    adapterData.init();
                    ((MainActivity)getActivity()).setFragments(courseId);
                }catch (Exception e ){
                    Toast.makeText(getActivity(),e+"",Toast.LENGTH_LONG).show();
                }



            }
        });

        builder.setNegativeButton("Odustani", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getActivity(),"Prekinuto", Toast.LENGTH_SHORT).show();
            }
        });

        DataBaseManager.getInstance().closeDatabase();
        dialog = builder.create();
        return dialog;


    }
}
