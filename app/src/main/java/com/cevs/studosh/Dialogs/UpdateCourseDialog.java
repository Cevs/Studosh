package com.cevs.studosh.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.cevs.studosh.AdapterData;
import com.cevs.studosh.MainActivity;
import com.cevs.studosh.R;
import com.cevs.studosh.data.DataBaseManager;
import com.cevs.studosh.data.model.Course;
import com.cevs.studosh.data.model.Semester;
import com.cevs.studosh.data.repo.CourseRepo;
import com.cevs.studosh.data.repo.SemesterRepo;

/**
 * Created by TOSHIBA on 01.10.2016..
 */

public class UpdateCourseDialog extends android.support.v4.app.DialogFragment {
    String oldName;
    String newName;
    Long rowId;
    Dialog dialog;
    LayoutInflater inflater;
    View view;
    CourseRepo courseRepo;
    Course course;
    EditText editName;
    Spinner spinner;
    int semesterId;

    public UpdateCourseDialog(){}

    public static UpdateCourseDialog newInstance(String course, long courseId){
        UpdateCourseDialog mDialog = new UpdateCourseDialog();
        Bundle args = new Bundle();
        args.putString("Name",course);
        args.putLong("Id", courseId);
        mDialog.setArguments(args);
        return mDialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        oldName = getArguments().getString("Name");
        rowId = getArguments().getLong("Id");



        inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.course_dialog,null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        builder.setTitle("Ažuriraj kolegij");

        editName = (EditText) view.findViewById(R.id.editText_course);
        editName.setText(oldName);

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
                semesterId = (int)id;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        builder.setPositiveButton("Uredu", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                courseRepo = new CourseRepo();
                course = new Course();
                newName = editName.getText().toString();
                course.setCourseName(newName);
                course.setSemesterId(semesterId);

                try{
                    courseRepo.updateRow(rowId, course);
                    Toast.makeText(getContext(),oldName + " Updated", Toast.LENGTH_SHORT).show();
                }catch (Exception e){
                    Toast.makeText(getActivity(),e+"",Toast.LENGTH_LONG).show();
                }

                ((MainActivity)getActivity()).setFragments(rowId);
                ((MainActivity)getActivity()).createExpandableList();
                ((MainActivity)getActivity()).populateList();
            }
        });




        builder.setNegativeButton("Odustani", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i){}
        });

        cursor.close();
        dialog = builder.create();
        return dialog;
    }
}
