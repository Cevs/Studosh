package com.cevs.studosh.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.cevs.studosh.AdapterData;
import com.cevs.studosh.R;
import com.cevs.studosh.data.model.Content;
import com.cevs.studosh.data.repo.ContentRepo;

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

    double doublePoints;
    double doubleMaxPoints;
    String stringCriterion;


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

                criterion = (EditText) view.findViewById(R.id.eT_criterion);
                points = (EditText) view.findViewById(R.id.eT_points);
                maxPoints = (EditText) view.findViewById(R.id.eT_maxPoints);

                boolean validEntry = true;
                boolean validPoints = true;

                try{
                    doublePoints  = Double.parseDouble(points.getText().toString());
                    doubleMaxPoints = Double.parseDouble(maxPoints.getText().toString());
                }catch (NumberFormatException e){
                    validEntry = false;
                }

                if (doubleMaxPoints<doublePoints){
                    validPoints = false;
                }

                stringCriterion = criterion.getText().toString();

                if(!TextUtils.isEmpty(stringCriterion) && validEntry && validPoints){
                    content = new Content();
                    contentRepo = new ContentRepo();
                    content.setPoints(doublePoints);
                    content.setMaxPoints(doubleMaxPoints);
                    content.setCriteria(stringCriterion);
                    long m = courseId;
                    content.setCourseId(courseId);
                    if (contentRepo.insertRow(content)!=-1){
                        Toast.makeText(getActivity(),"Kriterij dodan",Toast.LENGTH_SHORT).show();
                        AdapterData adapterData = new AdapterData(getActivity(), courseId);
                        adapterData.init();
                    }
                    else{
                        Toast.makeText(getActivity(),"Neuspjelo dodavanje kriterija",Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    if(!validPoints){
                        Toast.makeText(getActivity(),"Neispravan unos bodova",Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getActivity(),"Ispunite sva polja",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        builder.setNegativeButton("Odustani", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        Dialog dialog = builder.create();

        return dialog;
    }


}
