package com.cevs.studosh;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.cevs.studosh.fragments.PagerItem;

import java.util.ArrayList;

/**
 * Created by TOSHIBA on 03.10.2016..
 */

public class MyPagerAdapter extends FragmentStatePagerAdapter {

    private FragmentManager mFragmentManager;
    private ArrayList<PagerItem> mPagerItems;
    public MyPagerAdapter(FragmentManager fm, ArrayList<PagerItem> pagerItems) {
        super(fm);
        mFragmentManager = fm;
        mPagerItems = pagerItems;
    }



    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return mPagerItems.get(0).getFragment();
            case 1:
                return mPagerItems.get(1).getFragment();
            case 2:
                return mPagerItems.get(2).getFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    public void setPagerItems(ArrayList<PagerItem> pagerItems){
        if (mPagerItems != null){
            for (int i = 0; i<mPagerItems.size(); i++){
                mFragmentManager.beginTransaction().remove(mPagerItems.get(i).getFragment()).commit();
            }
        }
        mPagerItems = pagerItems;

    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
