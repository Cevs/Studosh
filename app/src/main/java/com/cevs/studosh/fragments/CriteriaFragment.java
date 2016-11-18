package com.cevs.studosh.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;


import com.cevs.studosh.AdapterData;
import com.cevs.studosh.Dialogs.DialogHelper;
import com.cevs.studosh.Dialogs.UpdateContentDialog;
import com.cevs.studosh.MainActivity;
import com.cevs.studosh.MyCriteriaListAdapter;
import com.cevs.studosh.MyPagerAdapter;
import com.cevs.studosh.R;
import com.cevs.studosh.data.model.Content;
import com.cevs.studosh.data.repo.ContentRepo;

import java.util.ArrayList;

/**
 * Created by TOSHIBA on 03.10.2016..
 */

public class CriteriaFragment extends Fragment {

    ListView lv;
    View view;
    MyCriteriaListAdapter myCriteriaListAdapter;
    long courseId;
    ContentRepo contentRepo;
    Content content;
    Cursor cursor;
    ArrayList<Content> arrayList;
    AdapterData adapterData;
    String title;
    DialogHelper dialogHelper;


    long position;
    String[] menuItems;
    ArrayList<Long> arrayOfContentIds;

    public static CriteriaFragment newInstance(long rowId){
        CriteriaFragment fragment = new CriteriaFragment();
        Bundle args = new Bundle();
        args.putLong("Id",rowId);
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
        dialogHelper = new DialogHelper();
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

        arrayOfContentIds = new ArrayList<Long>(){};


        ListView list = (ListView) view.findViewById(R.id.list_criteria);
        registerForContextMenu(list);
        list.setOnCreateContextMenuListener(this);

        adapterData = new AdapterData(view, getActivity(), courseId);
        adapterData.init();


        return view;
    }



    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        //super.onCreateContextMenu(menu,v,menuInfo);
        final int DELETE_CONTENT = 0;
        final int UPDATE_CONTENT = 1;

        v.setSelected(true);

        if (v.getId() == R.id.list_criteria);{
            menuItems = new String[]{};
            contentRepo = new ContentRepo();

            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;

            cursor = contentRepo.getAllRows(courseId);
            cursor.moveToFirst();
            position = info.id;
            int p = 0;
            while(p++!=position){
                cursor.moveToNext();
            }
            title = cursor.getString(cursor.getColumnIndex(Content.COLUMN_Criteria));
            cursor.close();
            menu.setHeaderTitle(title);


            menuItems = getResources().getStringArray(R.array.menuItems);

            menu.add(1,DELETE_CONTENT,0,menuItems[0]);
            menu.add(1,UPDATE_CONTENT,0,menuItems[1]);


            for(int i = 0; i<menu.size(); i++){
                menu.getItem(i).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {

                        int iid = menuItem.getItemId();

                        switch (iid){
                            case 0:
                                contentRepo.deleteRow(getItemId(position));
                                Toast.makeText(getView().getContext(),title+" obrisan",Toast.LENGTH_SHORT).show();
                                adapterData.init();
                                ((MainActivity)getActivity()).setFragments(courseId);

                                break;

                            case 1:
                                dialogHelper.setUpdateContentDialog(title,courseId);
                                Toast.makeText(getActivity(),title + " ažurirano",Toast.LENGTH_SHORT).show();

                                break;
                            default:
                                Toast.makeText(getView().getContext(),"Nesto se desilo",Toast.LENGTH_SHORT).show();
                                break;
                        }
                        return false;
                    }
                });
            }

        }
    }

    public long getItemId(long position){
        arrayOfContentIds = new ArrayList<Long>(){};
        cursor = contentRepo.getAllRows(courseId);
        cursor.moveToFirst();
        //Make array list of ids that are written in database
        do{
            arrayOfContentIds.add(cursor.getLong(cursor.getColumnIndex(Content.COLUMN_ContentId)));
        }while(cursor.moveToNext());
        cursor.close();

        //Get the ID of content written in array list
        //Delete it from array
        long itemId = arrayOfContentIds.get((int)position);
        arrayOfContentIds.remove(position);

        //return ID of item
        return itemId;


    }

}
