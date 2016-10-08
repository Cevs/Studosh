package com.cevs.studosh;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.cevs.studosh.data.model.Content;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by TOSHIBA on 06.10.2016..
 */

public class MyCriteriaListAdapter extends BaseAdapter {

    ArrayList<Content> array;

    Context adapterContext;
    View rowView;

    public MyCriteriaListAdapter(ArrayList<Content> array, Context context) {
        this.array = array;
        adapterContext = context;

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
        return 2;
    }


    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) adapterContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (array.get(i).getCourseId()==-1){
            rowView = inflater.inflate(R.layout.listview_row_criterria_button,viewGroup,false);
            Button button = (Button) rowView.findViewById(R.id.button_listView_criteriaAdd);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //array.get(0).getCourseId - GREŠKA
                    //Vanjski kljucevi krvo napravljeni
                    DialogHelper mDialogHelper = new DialogHelper(adapterContext, array.get(0).getCourseId());
                    mDialogHelper.setContentDialog();
                }
            });
        }

        else{
            rowView = inflater.inflate(R.layout.listview_row_criteria,viewGroup,false);

            TextView numberTv = (TextView) rowView.findViewById(R.id.tV_cNo);
            TextView criteriaTv = (TextView) rowView.findViewById(R.id.tV_listView_criteria);
            TextView pointsTv = (TextView) rowView.findViewById(R.id.tV_listView_points);
            TextView maxPointsTv = (TextView) rowView.findViewById(R.id.tV_listView_maxPoints);

            numberTv.setText(i+"");
            criteriaTv.setText(array.get(i).getCriteria());
            pointsTv.setText(array.get(i).getPoints()+"");
            maxPointsTv.setText(array.get(i).getMaxPoints()+"");

        }

        return  rowView;
    }


}
