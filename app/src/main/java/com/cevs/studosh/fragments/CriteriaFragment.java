package com.cevs.studosh.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;


import com.cevs.studosh.AdapterData;
import com.cevs.studosh.MyCriteriaListAdapter;
import com.cevs.studosh.R;
import com.cevs.studosh.data.model.Content;
import com.cevs.studosh.data.repo.ContentRepo;

import java.util.ArrayList;

/**
 * Created by TOSHIBA on 03.10.2016..
 */

public class CriteriaFragment extends Fragment {

    ListView lv;
    View view;
    MyCriteriaListAdapter myCriteriaListAdapter;
    long courseId;
    ContentRepo contentRepo;
    Content content;
    Cursor cursor;
    ArrayList<Content> arrayList;

    public static CriteriaFragment newInstance(long rowId){
        CriteriaFragment fragment = new CriteriaFragment();
        Bundle args = new Bundle();
        args.putLong("Id",rowId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        courseId = getArguments().getLong("Id");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view != null){
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent!=null){
                parent.removeView(view);
            }
        }
        try{
            view = inflater.inflate(R.layout.fragment_criteria,container,false);
        }catch (InflateException e){
            Toast.makeText(getContext(),e+"",Toast.LENGTH_LONG).show();
        }

        /*contentRepo = new ContentRepo();
        cursor = contentRepo.getRows(courseId);
        arrayList = new ArrayList<Content>();
        cursor.moveToFirst();
        int i = 0;

        if(cursor.getCount()!=0) {
            while (cursor.isAfterLast()) {
                content = new Content();
                content.setContentId(i++);
                content.setCriteria(cursor.getString(cursor.getColumnIndex(Content.COLUMN_Criteria)));
                content.setPoints(cursor.getDouble(cursor.getColumnIndex(Content.COLUMN_Points)));
                content.setMaxPoints(cursor.getDouble(cursor.getColumnIndex(Content.COLUMN_MaxPoints)));
                content.setCourseId(cursor.getLong(cursor.getColumnIndex(Content.COLUMN_CourseId)));

                arrayList.add(content);

                cursor.moveToNext();
            }
            cursor.close();
        }
        content = new Content();
        content.setContentId(-1);
        content.setCourseId(courseId);
        arrayList.add(content);

        lv = (ListView) view.findViewById(R.id.list_criteria);
        myCriteriaListAdapter = new MyCriteriaListAdapter(arrayList,getActivity());
        lv.setAdapter(myCriteriaListAdapter);*/

        long ID = courseId;
        AdapterData adapterData = new AdapterData(view, getActivity(), courseId);
        adapterData.init();


        return view;
    }

}
