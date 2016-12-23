package com.cevs.studosh;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.cevs.studosh.data.model.Content;
import com.cevs.studosh.data.repo.ContentRepo;

import java.util.ArrayList;

/**
 * Created by TOSHIBA on 08.10.2016..
 */

public class AdapterData {

    private ArrayList<Content> items;
    private long courseId;
    private static View view;
    private Context context;

    public AdapterData(View view, Context context, long courseId) {
        this.view = view;
        this.context = context;
        this.courseId = courseId;
    }

    public AdapterData(Context context, long courseId){
        this.context = context;
        this.courseId = courseId;
    }

    public void init(){
        ListView listView = (ListView)view.findViewById(R.id.list_criteria);
        items = getData();
        MyCriteriaListAdapter myCriteriaListAdapter = new MyCriteriaListAdapter(items,context);
        myCriteriaListAdapter.notifyDataSetChanged();
        listView.setAdapter(myCriteriaListAdapter);
    }
    private ArrayList<Content> getData(){
        items = new ArrayList<Content>();
        ContentRepo contentRepo = new ContentRepo();
        Cursor cursor = contentRepo.getAllRows(courseId);

        cursor.moveToFirst();
        int n = cursor.getCount();
        if(n != 0) {
            while (!cursor.isAfterLast()) {
                Content content = new Content();
                content.setCriteria(cursor.getString(cursor.getColumnIndex(Content.COLUMN_Criteria)));
                content.setPoints(cursor.getFloat(cursor.getColumnIndex(Content.COLUMN_Points)));
                content.setMaxPoints(cursor.getFloat(cursor.getColumnIndex(Content.COLUMN_MaxPoints)));
                content.setCourseId(cursor.getLong(cursor.getColumnIndex(Content.COLUMN_fk_CourseId)));

                cursor.moveToNext();

                items.add(content);

            }
        }
        cursor.close();


        return items;
    }

}
