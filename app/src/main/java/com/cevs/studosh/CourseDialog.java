package com.cevs.studosh;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cevs.studosh.data.DataBaseManager;
import com.cevs.studosh.data.model.Course;
import com.cevs.studosh.data.repo.CourseRepo;

/**
 * Created by TOSHIBA on 28.09.2016..
 */

public class CourseDialog extends DialogFragment {

    LayoutInflater inflater;
    View view;
    Course course;
    EditText courseName;
    EditText courseSemester;

    int semester;
    String name;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.course_dialog,null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        builder.setTitle("Add course in list of courses");

        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                DataBaseManager.getInstance().openDatabase();
                course = new Course();
                CourseRepo courseRepo = new CourseRepo();

                courseName = (EditText) view.findViewById(R.id.editText_course);
                courseSemester = (EditText) view.findViewById(R.id.editText_semester);

                boolean bsemester = true;

                try{
                    semester = Integer.parseInt(courseSemester.getText().toString());
                    course.setSemester(semester+"");
                }catch (NumberFormatException e){
                    bsemester = false;
                }

                name = courseName.getText().toString();
                if ((!TextUtils.isEmpty((name)))&&(bsemester)){
                    course.setCourseName(name);
                    if (courseRepo.insertRow(course) !=-1){
                        Toast.makeText(getActivity(),"Course was added", Toast.LENGTH_SHORT).show();
                        ((MainActivity)getActivity()).populateList();
                    }
                    else
                        Toast.makeText(getActivity(),"Failed to add Course", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast toast = Toast.makeText(getActivity(), "Enter both data", Toast.LENGTH_SHORT);
                    TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
                    if (v!= null) v.setGravity(Gravity.CENTER);
                    toast.show();
                }
            }
        });


        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getActivity(),"Cancel was clicked", Toast.LENGTH_SHORT).show();
            }
        });

        Dialog dialog = builder.create();
        DataBaseManager.getInstance().closeDatabase();
        return dialog;
    }
}
