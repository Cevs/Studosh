package com.cevs.studosh;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cevs.studosh.data.model.Course;

/**
 * Created by TOSHIBA on 20.10.2016..
 */

public class MyNavigationDrawerAdapter extends CursorAdapter {
    private LayoutInflater inflater;
    View view;
    ViewHolder holder;
    String courseName;

    private static class ViewHolder{
        TextView textCourseName;
        ImageView image;
        int nameIndex;
    }


    public MyNavigationDrawerAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, 0);
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.course_item,null);
        holder = new ViewHolder();
        holder.textCourseName = (TextView) view.findViewById(R.id.courseNameList);
        holder.image = (ImageView) view.findViewById(R.id.courseImageList);
        holder.nameIndex = cursor.getColumnIndexOrThrow(Course.COLUMN_CourseName);
        view.setTag(holder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        holder = (ViewHolder) view.getTag();

        holder.textCourseName.setText(cursor.getString(holder.nameIndex));
        holder.image.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.notebook));

    }
}
