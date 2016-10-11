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
    ListView listView;
    static View view;
    Context context;
    Content content;
    ContentRepo contentRepo;
    Cursor cursor;
    ArrayList<Content> items;
    long courseId;
    MyCriteriaListAdapter myCriteriaListAdapter;

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
        listView = (ListView)view.findViewById(R.id.list_criteria);
        items = getData();
        myCriteriaListAdapter = new MyCriteriaListAdapter(items,context);
        listView.setAdapter(myCriteriaListAdapter);
        myCriteriaListAdapter.notifyDataSetChanged();
        listView.setAdapter(myCriteriaListAdapter);

    }

    private ArrayList<Content> getData(){
        items = new ArrayList<Content>();
        contentRepo = new ContentRepo();
        cursor = contentRepo.getRows(courseId);

        cursor.moveToFirst();
        int n = cursor.getCount();
        if(n != 0) {
            while (cursor.isAfterLast() == false) {
                content = new Content();
                content.setCriteria(cursor.getString(cursor.getColumnIndex(Content.COLUMN_Criteria)));
                content.setPoints(cursor.getDouble(cursor.getColumnIndex(Content.COLUMN_Points)));
                content.setMaxPoints(cursor.getDouble(cursor.getColumnIndex(Content.COLUMN_MaxPoints)));
                content.setCourseId(cursor.getLong(cursor.getColumnIndex(Content.COLUMN_fk_CourseId)));

                cursor.moveToNext();

                items.add(content);

            }
        }
        int m = items.size();
        cursor.close();
        content = new Content();
        content.setContentId(-1);
        content.setPoints(-1);
        content.setMaxPoints(-1);
        content.setCourseId(courseId);
        items.add(content);

        return items;
    }

}
