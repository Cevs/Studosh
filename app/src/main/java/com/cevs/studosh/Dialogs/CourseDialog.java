package com.cevs.studosh.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cevs.studosh.MainActivity;
import com.cevs.studosh.R;
import com.cevs.studosh.data.DataBaseManager;
import com.cevs.studosh.data.model.Course;
import com.cevs.studosh.data.model.Semester;
import com.cevs.studosh.data.repo.CourseRepo;
import com.cevs.studosh.data.repo.SemesterRepo;

import java.util.Collections;
import java.util.List;

/**
 * Created by TOSHIBA on 28.09.2016..
 */

public class CourseDialog extends DialogFragment {

    LayoutInflater inflater;
    View view;
    Course course;
    EditText courseName;
    Spinner spinner;

    long semesterId;
    String name;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.course_dialog,null);

        courseName = (EditText) view.findViewById(R.id.editText_course);

        spinner = (Spinner) view.findViewById(R.id.spinner);

        SemesterRepo semesterRepo = new SemesterRepo();
        Cursor cursor = semesterRepo.getAllRows();

        String[] semesterNames = new String[]{Semester.COLUMN_SemesterName};

        int[]adapterRowViews = new int[]{R.id.spinnerItem};

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(getActivity(),R.layout.spinner_item_layout,cursor,semesterNames,adapterRowViews,0);
        //defined layout for spinner items (not spinner itself)
        adapter.setDropDownViewResource(R.layout.spinner_item_layout);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long id) {
                semesterId = id;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        builder.setTitle("Dodavanje kolegija");

        builder.setPositiveButton("Dodaj", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                course = new Course();
                CourseRepo courseRepo = new CourseRepo();

                course.setSemesterId(semesterId);
                name = courseName.getText().toString();

                if ((!TextUtils.isEmpty((name)))){
                    course.setCourseName(name);
                    long courseId = courseRepo.insertRow(course);
                    if (courseId!=-1){
                        Toast.makeText(getActivity(),"Kolegij dodan", Toast.LENGTH_SHORT).show();
                        ((MainActivity)getActivity()).populateList();
                        ((MainActivity)getActivity()).setFragments(courseId);
                        ((MainActivity)getActivity()).updateNavigationDrawerSemesterList((int)semesterId,name, courseId);
                    }
                    else
                        Toast.makeText(getActivity(),"Neuspjelo", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast toast = Toast.makeText(getActivity(), "Unesite sve podatke!", Toast.LENGTH_SHORT);
                    TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
                    if (v!= null) v.setGravity(Gravity.CENTER);
                    toast.show();
                }

            }
        });


        builder.setNegativeButton("Prekini", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i){}
        });

        Dialog dialog = builder.create();

        return dialog;
    }


}
