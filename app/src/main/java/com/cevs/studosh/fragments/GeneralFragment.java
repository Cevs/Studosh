package com.cevs.studosh.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cevs.studosh.MainActivity;
import com.cevs.studosh.R;
import com.cevs.studosh.data.model.Content;
import com.cevs.studosh.data.model.Course;
import com.cevs.studosh.data.repo.ContentRepo;
import com.cevs.studosh.data.repo.CourseRepo;

/**
 * Created by TOSHIBA on 03.10.2016..
 */

public class GeneralFragment extends Fragment {
    TextView name;
    TextView semester;
    TextView points;
    TextView maxPoints;
    TextView mark;
    View view;
    private long courseId;
    CourseRepo courseRepo;
    ContentRepo contentRepo;
    Cursor cursor;

    String grade;
    Double sumOfPoints;
    Double sumOfMaxPoints;

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
        points = (TextView) view.findViewById(R.id.generalFragmentPoints);
        maxPoints = (TextView) view.findViewById(R.id.generalFragmentMaxPoints);
        mark = (TextView)view.findViewById(R.id.generalFragmentMark);

        courseRepo = new CourseRepo();
        cursor = courseRepo.getRow(courseId);

        String courseName = cursor.getString(cursor.getColumnIndex(Course.COLUMN_CourseName));
        //TREBA PROJEMNIT ODAKLE SE VUCE SEMESTAR
        //String courseSemester = cursor.getString(cursor.getColumnIndex(Course.COLUMN_Semester));

        sumOfPoints = 0.0;
        sumOfMaxPoints = 0.0;

        contentRepo = new ContentRepo();
        cursor = contentRepo.getAllRows(courseId);

        if(cursor.getCount()>0){
            cursor.moveToFirst();

            do{
                sumOfPoints += cursor.getDouble(cursor.getColumnIndex(Content.COLUMN_Points));
                sumOfMaxPoints += cursor.getDouble(cursor.getColumnIndex(Content.COLUMN_MaxPoints));
            }while(cursor.moveToNext());

        }

        grade = determineGrade(sumOfPoints);

        points.setText(sumOfPoints+"/");
        maxPoints.setText(sumOfMaxPoints+"");
        mark.setText(grade);
        name.setText(courseName);
        //semester.setText(courseSemester);



        cursor.close();
        return view;
    }


    String determineGrade(double points){

        if(sumOfPoints <50)
            return("Nedovoljan(1)");
        else if(sumOfPoints>=50 && sumOfPoints <60 )
            return("Dovoljan(2)");
        else if (sumOfPoints>=61 && sumOfPoints <75)
            return("Dobar(3)");
        else if (sumOfPoints>=76 && sumOfPoints <90)
            return("Vrlo dobar(4)");
        else
            return("Odlican(5)");
    }


}
