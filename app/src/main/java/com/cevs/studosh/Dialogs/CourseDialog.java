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
import android.widget.ArrayAdapter;
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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
    long spinnerId;
    String name;
    Semester semester;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.course_dialog,null);

        courseName = (EditText) view.findViewById(R.id.editText_course);

        spinner = (Spinner) view.findViewById(R.id.spinner);

        SemesterRepo semesterRepo = new SemesterRepo();
        Cursor cursor = semesterRepo.getAllRows();


        final ArrayList<Semester> listOfSemesters = new ArrayList<Semester>();

        do{
            semester = new Semester();
            String name = cursor.getString(cursor.getColumnIndex(Semester.COLUMN_SemesterName));
            Long id = cursor.getLong(cursor.getColumnIndex(Semester.COLUMN_SemesterId));

            semester.setId(id);
            semester.setSemesterName(name);

            listOfSemesters.add(semester);

        }while(cursor.moveToNext());
        cursor.close();

        //Comparing names of semester
        Collections.sort(listOfSemesters, new Comparator<Semester>() {
            @Override
            public int compare(Semester semester1, Semester semester2) {
                if(semester1.getSemesterName().length()>semester2.getSemesterName().length())
                    return 1;
                else if (semester1.getSemesterName().length()<semester2.getSemesterName().length())
                    return -1;
                return semester1.getSemesterName().compareToIgnoreCase(semester2.getSemesterName());
            }
        });


        ArrayAdapter<Semester> spinnerAdapter = new ArrayAdapter<Semester>(getActivity(), R.layout.spinner_item_layout,
                R.id.spinnerItem, listOfSemesters);
        //defined layout for spinner items (not spinner itself)
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_item_layout);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long id) {
                //List consists of items that are type objects 'Semester'
                //get id of row in DB
                semesterId = listOfSemesters.get((int)id).getId();
                spinnerId = id;
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
                        ((MainActivity)getActivity()).setFragments(courseId);
                        ((MainActivity)getActivity()).updateNavigationDrawerSemesterList((int)spinnerId,name, courseId);
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
