package com.cevs.studosh.InitialFragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cevs.studosh.R;

/**
 * Created by TOSHIBA on 03.10.2016..
 */

public class InitialPresenceFragment extends Fragment{

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.initial_fragment_presence,container,false);
        return view;
    }
}
