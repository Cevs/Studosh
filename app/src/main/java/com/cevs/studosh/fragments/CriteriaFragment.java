package com.cevs.studosh.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.cevs.studosh.R;

/**
 * Created by TOSHIBA on 03.10.2016..
 */

public class CriteriaFragment extends Fragment {

    ListView lv;
    View view;

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

        lv = (ListView) view.findViewById(R.id.list_criteria);
        return view;
    }
}
