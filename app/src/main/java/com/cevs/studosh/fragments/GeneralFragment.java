package com.cevs.studosh.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cevs.studosh.R;
import com.cevs.studosh.data.model.Course;
import com.cevs.studosh.data.repo.CourseRepo;

/**
 * Created by TOSHIBA on 03.10.2016..
 */

public class GeneralFragment extends Fragment {
    TextView name;
    TextView semester;
    View view;
    private long courseId;
    CourseRepo courseRepo;
    Cursor cursor;

    public GeneralFragment(){}

    public static GeneralFragment newInstance(long id){
        GeneralFragment mFragment = new GeneralFragment();
        Bundle args = new Bundle();
        args.putLong("Course id",id);
        mFragment.setArguments(args);
        return mFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        courseId =  getArguments().getLong("Course id");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_general,container,false);
        name = (TextView) view.findViewById(R.id.tvFragment_courseName);
        semester = (TextView) view.findViewById(R.id.tvFragment_semester);

        courseRepo = new CourseRepo();
        cursor = courseRepo.getRow(courseId);

        String courseName = cursor.getString(cursor.getColumnIndex(Course.COLUMN_CourseName));
        String courseSemester = cursor.getString(cursor.getColumnIndex(Course.COLUMN_Semester));


        name.setText(courseName);
        semester.setText(courseSemester);


        cursor.close();
        return view;
    }


}
