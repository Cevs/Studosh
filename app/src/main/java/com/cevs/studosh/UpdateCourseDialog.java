package com.cevs.studosh;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.cevs.studosh.data.DataBaseManager;
import com.cevs.studosh.data.model.Course;
import com.cevs.studosh.data.repo.CourseRepo;

/**
 * Created by TOSHIBA on 01.10.2016..
 */

public class UpdateCourseDialog extends DialogFragment {
    String courseName;
    String courseSemester;
    Long itemId;
    Dialog dialog;
    LayoutInflater inflater;
    View view;

    public UpdateCourseDialog(){}

    public static UpdateCourseDialog newInstance(String course, String semester, long courseId){
        UpdateCourseDialog mDialog = new UpdateCourseDialog();
        Bundle args = new Bundle();
        args.putString("Name",course);
        args.putString("Semester", semester);
        args.putLong("Id", courseId);
        mDialog.setArguments(args);
        return mDialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        courseName = getArguments().getString("Name");
        courseSemester = getArguments().getString("Semester");
        itemId = getArguments().getLong("Id");

        inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.update_course_dialog,null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        builder.setTitle("Update Course");

        final EditText editName = (EditText) view.findViewById(R.id.editText_update_name);
        final EditText editSemester = (EditText) view.findViewById(R.id.editText_update_semester);

        editName.setText(courseName);
        editSemester.setText(courseSemester);

        DataBaseManager.getInstance().openDatabase();
        final CourseRepo courseRepo = new CourseRepo();
        final Course course = new Course();

        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                courseName = editName.getText().toString();
                courseSemester = editSemester.getText().toString();
                course.setCourseName(courseName);
                course.setSemester(courseSemester);
                try {
                    courseRepo.updateRow(itemId, course);
                }catch (Exception e ){
                    Toast.makeText(getActivity(),e+"",Toast.LENGTH_LONG).show();
                }
                ((MainActivity)getActivity()).populateList();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getActivity(),"Canceled",Toast.LENGTH_SHORT).show();
            }
        });

        DataBaseManager.getInstance().closeDatabase();
        dialog = builder.create();
        return dialog;
    }
}
