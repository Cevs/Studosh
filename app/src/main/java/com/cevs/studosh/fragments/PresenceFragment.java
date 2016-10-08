package com.cevs.studosh.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cevs.studosh.R;

/**
 * Created by TOSHIBA on 03.10.2016..
 */

public class PresenceFragment extends Fragment {
    View view;
    private long courseId;

    public static PresenceFragment newInstance(long id){
        PresenceFragment fragment = new PresenceFragment();
        Bundle args = new Bundle();
        args.putLong("Id",id);
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

        if(view != null){
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent !=null){
                parent.removeView(view);
            }
        }
        try{
            view = inflater.inflate(R.layout.fragment_presence,container,false);
        }catch (InflateException e){
            Toast.makeText(getContext(),e+"",Toast.LENGTH_LONG).show();
        }

        return view;
    }
}