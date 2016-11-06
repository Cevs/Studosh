package com.cevs.studosh;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

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
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_group, null);
        }

        TextView headerName = (TextView) convertView.findViewById(R.id.expandable_list_header);
        headerName.setText(headerTitle);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        //final String childText = (String) getChild(groupPosition,childPosition);
        ChildPair object = (ChildPair) getChild(groupPosition,childPosition);

        String childText =  object.getName();
        Long id = object.getRowId();

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item,null);
        }

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
