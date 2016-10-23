package com.cevs.studosh;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.cevs.studosh.Dialogs.DialogHelper;
import com.cevs.studosh.data.model.Content;

import java.util.ArrayList;

/**
 * Created by TOSHIBA on 06.10.2016..
 */

public class MyCriteriaListAdapter extends BaseAdapter {

    ArrayList<Content> array;
    ViewHolder holder;

    View rowView;
    LayoutInflater inflater;

    private static class ViewHolder
    {
        TextView textViewNumber;
        TextView textViewCriteria;
        TextView textViewPoints;
        TextView textViewMaxPoints;
    }

    public MyCriteriaListAdapter(ArrayList<Content> array, Context context) {
        this.array = array;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return array.size() ;
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemViewType(int position) {
        return 1;
    }

    @Override
    public View getView(int i, View rowView, ViewGroup viewGroup) {

        if (rowView == null) {
            rowView = inflater.inflate(R.layout.listview_row_criteria, viewGroup, false);

            holder = new ViewHolder();
            holder.textViewNumber = (TextView) rowView.findViewById(R.id.tV_cNo);
            holder.textViewCriteria = (TextView) rowView.findViewById(R.id.tV_listView_criteria);
            holder.textViewPoints = (TextView) rowView.findViewById(R.id.tV_listView_points);
            holder.textViewMaxPoints = (TextView) rowView.findViewById(R.id.tV_listView_maxPoints);

            rowView.setTag(holder);
        }
        else{
            holder = (ViewHolder)rowView.getTag();
        }

        //i-position (starts with 0)
        holder.textViewNumber.setText(i+1+"");
        holder.textViewCriteria.setText(array.get(i).getCriteria());
        holder.textViewPoints.setText(array.get(i).getPoints()+"");
        holder.textViewMaxPoints.setText(array.get(i).getMaxPoints()+"");

        return  rowView;
    }


}
