package com.cevs.studosh;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.cevs.studosh.Dialogs.UpdateCourseDialog;
import com.cevs.studosh.data.model.Course;
import com.cevs.studosh.data.repo.CourseRepo;

import java.util.HashMap;
import java.util.List;

/**
 * Created by TOSHIBA on 26.10.2016..
 */

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private  Context context;
    //header titles(semestar 1, semestar 2...)
    private  List<String> listDataHeader;
    // child data in format of header title, child title
    //private  HashMap<String, List<String>> listDataChild;
    private HashMap<String, List<ChildPair>> listDataChild;
    ImageButton imageButtonDelete;
    ImageButton imageButtonRename;
    ImageButton imageButtonDeleteSemester;
    ChildPair object;
    String childText;
    String courseName;
    long id;



    public ExpandableListAdapter(Context context, List<String> listDataHeader, HashMap<String, List<ChildPair>> listDataChild){
       this.context = context;
        this.listDataHeader = listDataHeader;
        this.listDataChild = listDataChild;
    }


    @Override
    public int getGroupCount() {
        return this.listDataHeader.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return (listDataChild.get(this.listDataHeader.get(groupPosition)).size());
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.listDataHeader.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return (listDataChild.get(this.listDataHeader.get(groupPosition)).get(childPosition));
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        final String headerTitle = (String) getGroup(groupPosition);
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_group, null);
            imageButtonDeleteSemester = (ImageButton) convertView.findViewById(R.id.imageButton_deleteSemester);

        }

        imageButtonDeleteSemester.setFocusable(false);
        imageButtonDeleteSemester.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //((MainActivity)context).deleteSemester(groupPosition);
                String semesterName = (String) getGroup(groupPosition);
                Toast.makeText(context,"Obrisan "+semesterName,Toast.LENGTH_SHORT).show();
                ((MainActivity)context).deleteSemester(semesterName);
            }
        });
        TextView headerName = (TextView) convertView.findViewById(R.id.expandable_list_header);
        headerName.setText(headerTitle);


        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {



        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item,null);
            imageButtonDelete = (ImageButton) convertView.findViewById(R.id.imageButton_deletecourse);
            imageButtonRename = (ImageButton) convertView.findViewById(R.id.imageButton_renamecourse);
            imageButtonDelete.setFocusable(false);
            imageButtonRename.setFocusable(false);
        }



        imageButtonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                object = (ChildPair) getChild(groupPosition,childPosition);
                id = object.getRowId();
                courseName = object.getName();

                CourseRepo courseRepo = new CourseRepo();
                Cursor cursor  = courseRepo.getRow(id);
                courseRepo.deleteRow(id);
                long foreignKey = cursor.getLong(cursor.getColumnIndex(Course.COLUMN_SemesterId));
                ((MainActivity)context).deleteItem(groupPosition, childPosition, foreignKey);
                Toast.makeText(context,"Obrisan kolegij "+courseName,Toast.LENGTH_SHORT).show();
                cursor.close();



            }
        });
        imageButtonRename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                object = (ChildPair) getChild(groupPosition,childPosition);
                id = object.getRowId();
                courseName = object.getName();
                ((MainActivity)context).updateCourse(courseName,id);


            }
        });

        object = (ChildPair) getChild(groupPosition,childPosition);
        childText = object.getName();
        TextView childName = (TextView) convertView.findViewById(R.id.expandable_list_item);
        childName.setText(childText);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public void newData(List<String> listDataHeader, HashMap<String, List<ChildPair>> listDataChild){
        this.listDataHeader = listDataHeader;
        this.listDataChild = listDataChild;
    }

}
