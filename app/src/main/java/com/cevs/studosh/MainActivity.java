package com.cevs.studosh;

import android.app.FragmentManager;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.ContextMenu;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.cevs.studosh.Dialogs.DialogHelper;
import com.cevs.studosh.Dialogs.UpdateCourseDialog;
import com.cevs.studosh.InitialFragments.InitialCriteriaFragment;
import com.cevs.studosh.InitialFragments.InitialGeneralFragment;
import com.cevs.studosh.InitialFragments.InitialPresenceFragment;
import com.cevs.studosh.data.DBHelper;
import com.cevs.studosh.data.DataBaseManager;
import com.cevs.studosh.data.model.Course;
import com.cevs.studosh.data.model.Semester;
import com.cevs.studosh.data.repo.CourseRepo;
import com.cevs.studosh.data.repo.SemesterRepo;
import com.cevs.studosh.fragments.CriteriaFragment;
import com.cevs.studosh.fragments.GeneralFragment;
import com.cevs.studosh.fragments.PagerItem;
import com.cevs.studosh.fragments.PresenceFragment;
import com.gigamole.navigationtabstrip.NavigationTabStrip;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    CourseRepo mCourseRepo;
    android.support.v4.app.FragmentManager supportFragmentManager;
    FragmentManager fragmentManager;
    CourseRepo courseRepo;
    String[] fromFieldNames;
    String[] menuItems;
    SimpleCursorAdapter simpleCursorAdapter;
    Cursor cursor;
    long itemId;

    MyPagerAdapter mPagerAdapter;
    GeneralFragment generalFragment;
    CriteriaFragment criteriaFragment;
    PresenceFragment presenceFragment;
    ArrayList<PagerItem> pagerItems;
    DialogHelper mDialogHelper;

    private ViewPager mViewPager;
    private NavigationTabStrip mCenterNavigationTabStrip;

    ArrayList<Long> arrayOfIds;

    ImageView mainItemIcon, subItemIcon1, subItemIcon2, subItemIcon3, subItemIcon4, subItemIcon5;
    SubActionButton  subButton1, subButton2, subButton3, subButton4, subButton5;
    FloatingActionMenu actionMenu;

    long courseId;
    ExpandableListAdapter expandableListAdapter;
    ExpandableListView expandableListView;
    List<String> listDataHeader;
    HashMap<String, List<ChildPair>> listDataChild;
    DrawerLayout drawer;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDialogHelper = new DialogHelper(getBaseContext(),getFragmentManager());

        DBHelper dbHelper = new DBHelper(this);
        DataBaseManager.initializeInstance(dbHelper);

        SemesterRepo semesterRepo = new SemesterRepo();

        fragmentManager = getFragmentManager();
        supportFragmentManager = getSupportFragmentManager();
        courseRepo = new CourseRepo();
        pagerItems = new ArrayList<PagerItem>();

        initUI();


        mainItemIcon = new ImageView(this);
        subItemIcon1 = new ImageView(this);
        subItemIcon2 = new ImageView(this);
        subItemIcon3 = new ImageView(this);
        subItemIcon4 = new ImageView(this);
        subItemIcon5 = new ImageView(this);


        FloatingActionButton fab = new FloatingActionButton.Builder(this).setContentView(mainItemIcon)
                .setLayoutParams(new FloatingActionButton.LayoutParams(250,250))
                .setPosition(5).build();

        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(this)
                .setLayoutParams(new FloatingActionButton.LayoutParams(160,160));


        mainItemIcon.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.menu_button));
        subItemIcon1.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.add_course));
        subItemIcon2.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.add_criteria));
        subItemIcon3.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.add_presence));
        subItemIcon4.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.mark));
        subItemIcon5.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.cross));

        subButton1 = itemBuilder.setContentView(subItemIcon1).build();
        subButton2 = itemBuilder.setContentView(subItemIcon2).build();
        subButton3 = itemBuilder.setContentView(subItemIcon3).build();
        subButton4 = itemBuilder.setContentView(subItemIcon4).build();
        subButton5 = itemBuilder.setContentView(subItemIcon5).build();


        actionMenu = new FloatingActionMenu.Builder(this).addSubActionView(subButton1)
                .addSubActionView(subButton2).addSubActionView(subButton3).addSubActionView(subButton4)
                .addSubActionView(subButton5).attachTo(fab).setRadius(300).setStartAngle(200).setEndAngle(340).build();

        //Sadly, this must be implemented this way because the developers of this library
        //didn't create a way to find out or to set id to a buttons
        subButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialogHelper.setCourseDialog();
                actionMenu.close(true);

            }
        });
        subButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialogHelper = new DialogHelper(getBaseContext(),courseId);
                mDialogHelper.setContentDialog();
                mViewPager.setCurrentItem(1);
                actionMenu.close(true);


            }
        });
        subButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewPager.setCurrentItem(2);
                actionMenu.close(true);
            }
        });

        subButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewPager.setCurrentItem(2);
                actionMenu.close(true);
            }
        });

        subButton5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewPager.setCurrentItem(2);
                actionMenu.close(true);
            }
        });


        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        Button addNewSemester = (Button) drawer.findViewById(R.id.button_add_semester);
        addNewSemester.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialogHelper.setSemesterDialog();
            }
        });

        createExpandableList();
        populateList();

    }


    public void initUI(){
        cursor = courseRepo.getAllRows();
        cursor.moveToFirst();

        mViewPager = (ViewPager) findViewById(R.id.vp);
        mPagerAdapter = new MyPagerAdapter(supportFragmentManager,pagerItems);
        mViewPager.setAdapter(mPagerAdapter);
        cursor = courseRepo.getAllRows();
        mCenterNavigationTabStrip = (NavigationTabStrip) findViewById(R.id.nts_center);
        mCenterNavigationTabStrip.setViewPager(mViewPager);
        mCenterNavigationTabStrip.setStripColor(ContextCompat.getColor(getBaseContext(),R.color.navigationTabStrip));

        if(cursor.getCount()==0){
            setInitialFragments();
        }
        else{
            setFragments(cursor.getLong(cursor.getColumnIndex(Course.COLUMN_CourseId)));
            courseId = cursor.getLong(cursor.getColumnIndex(Course.COLUMN_CourseId));
        }
        cursor.close();

    }

    public void setInitialFragments(){
        pagerItems = new ArrayList<PagerItem>();
        pagerItems.add(new PagerItem("General fragment", new InitialGeneralFragment()));
        pagerItems.add(new PagerItem("Criteria fragment", new InitialCriteriaFragment()));
        pagerItems.add(new PagerItem("Absence fragment", new InitialPresenceFragment()));

        mPagerAdapter.setPagerItems(pagerItems);
        mViewPager.setCurrentItem(0);
        mPagerAdapter.notifyDataSetChanged();

    }


    //Method for refreshing fragments when old data set changed or for creating new fragments wih new data set
    public void setFragments(long position){
        Boolean same = false;
        if (courseId != position)
            courseId = position;
        else
            same = true;

        pagerItems = new ArrayList<PagerItem>();
        pagerItems.add(new PagerItem("General fragment",generalFragment.newInstance(position)));
        pagerItems.add(new PagerItem("Criteria fragment", criteriaFragment.newInstance(position)));
        pagerItems.add(new PagerItem("Absence fragment", presenceFragment.newInstance(position)));

        if(!same) {
            mViewPager.setCurrentItem(0);
        }
        mPagerAdapter.setPagerItems(pagerItems);
        mPagerAdapter.notifyDataSetChanged();
    }


    public void populateList(){

        expandableListView = (ExpandableListView) findViewById(R.id.expandable_list);
        expandableListAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
        expandableListView.setAdapter(expandableListAdapter);
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long id) {

                registerForContextMenu(expandableListView);

                ChildPair object = (ChildPair) expandableListAdapter.getChild(groupPosition,childPosition);
                view.setSelected(true);
                drawer.closeDrawer(GravityCompat.START);
                long row = object.getRowId();
                setFragments(row);
                return false;
            }
        });

        registerForContextMenu(expandableListView);
    }

    //Called only in first accessing the application
    public void createExpandableList(){
        //ArrayList of semester names that will be shown as main item
        listDataHeader = new ArrayList<String>();
        //ArrayList of courses that will be shown as subitem of specific semester
        //Data type is ChildPair that have two attributes, name of course and Id of row in database
        listDataChild = new HashMap<String, List<ChildPair>>();
        List<ChildPair> children;
        long foreignKey;

        SemesterRepo semesterRepo = new SemesterRepo();
        CourseRepo courseRepo = new CourseRepo();
        Cursor semesterCursor = semesterRepo.getAllRows();
        Cursor courseCursor;

        if(semesterCursor.getCount()>0) {
            //Number of current item in listDataHeader
            int i = 0;
            semesterCursor.moveToFirst();
            do{
                children = new ArrayList<ChildPair>();
                listDataHeader.add(semesterCursor.getString(semesterCursor.getColumnIndex(Semester.COLUMN_SemesterName)));
                foreignKey = semesterCursor.getLong(semesterCursor.getColumnIndex(Semester.COLUMN_SemesterId));
                //get all rows from db that have foreign key equal to id of current semester
                courseCursor = courseRepo.getRows(foreignKey);

                if (courseCursor.getCount() > 0) {
                    //If there is any course in that semester do...
                    courseCursor.moveToFirst();
                    do{
                        ChildPair pair = new ChildPair();
                        pair.setName(courseCursor.getString(courseCursor.getColumnIndex(Course.COLUMN_CourseName)));
                        pair.setRowId(courseCursor.getLong(courseCursor.getColumnIndex(Course.COLUMN_CourseId)));
                        children.add(pair);
                    }while(courseCursor.moveToNext());

                    listDataChild.put(listDataHeader.get(i), children);
                }else{
                    //If current semester don't have courses, set  empty ArrayList
                    listDataChild.put(listDataHeader.get(i), new ArrayList<ChildPair>());
                }
                //go to the next item in listDataHeader
               i++;
            }while(semesterCursor.moveToNext());
            semesterCursor.close();
            courseCursor.close();
            Collections.sort(listDataHeader);
        }
    }

    //Inserting new element in navigationDrawerSemesterList
    //Expandable list
    public void updateNavigationDrawerSemesterList(int semesterId, String courseName, long rowId){
        //Method for updating item in expandable list adapter that have new set of data
        //the rest remains unchanged
        //Its called after inserting new course in db (via courseDialog class)
        ChildPair pair;
        int k = 0;
        String cName = courseName;
        Long id = rowId;
        ArrayList<ChildPair> children = new ArrayList<ChildPair>();
        //-1 because array list of headers starts with index 0 while index in db starts with 1
        int size = expandableListAdapter.getChildrenCount(semesterId-1);
        if(size>0){
            while(k<size){
                pair = new ChildPair();
                ChildPair object = (ChildPair) expandableListAdapter.getChild(semesterId-1,k++);
                pair.setName(object.getName());
                pair.setRowId(object.getRowId());
                children.add(pair);
            }
        }

        pair = new ChildPair();
        pair.setName(cName);
        pair.setRowId(rowId);
        children.add(pair);
        listDataChild.put(listDataHeader.get(semesterId-1), children);
        expandableListAdapter.newData(listDataHeader,listDataChild);
        expandableListView.setAdapter(expandableListAdapter);
        expandableListAdapter.notifyDataSetChanged();
    }

    //Adding the empty header of entered semester
    //Refreshing the list of semesters in navigation drawer after the entering via dialog
    public void updateNavigationDrawerHeader(String nSemester){

        listDataHeader.add(nSemester);
        listDataChild.put(nSemester,new ArrayList<ChildPair>());

        expandableListAdapter.newData(listDataHeader,listDataChild);
        expandableListView.setAdapter(expandableListAdapter);
        expandableListAdapter.notifyDataSetChanged();

    }

    //Updating only header with changes after the deletion of course
    public void deleteItem(int groupPosition, long foreignKey){
        ChildPair pair;
        int k = 0;
        ArrayList<ChildPair> children = new ArrayList<ChildPair>();

        CourseRepo courseRepo = new CourseRepo();
        Cursor cursor = courseRepo.getRows(foreignKey);

        int size = cursor.getCount();

        if(size>0){
            cursor.moveToFirst();
            do{
                pair = new ChildPair();
                String name = cursor.getString(cursor.getColumnIndex(Course.COLUMN_CourseName));
                long rowId = cursor.getLong(cursor.getColumnIndex(Course.COLUMN_CourseId));
                pair.setName(name);
                pair.setRowId(rowId);

                children.add(pair);

            }while(cursor.moveToNext());

            listDataChild.put(listDataHeader.get(groupPosition), children);
            cursor.close();
        }
        else{
            listDataChild.put(listDataHeader.get(groupPosition), new ArrayList<ChildPair>());
        }


        expandableListAdapter.newData(listDataHeader,listDataChild);
        expandableListView.setAdapter(expandableListAdapter);
        expandableListAdapter.notifyDataSetChanged();
    }



    @Override
    public void onBackPressed() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu,v,menuInfo);

        ExpandableListView.ExpandableListContextMenuInfo info = (ExpandableListView.ExpandableListContextMenuInfo) menuInfo;

        int type = ExpandableListView.getPackedPositionType(info.packedPosition);
        int groupPosition = ExpandableListView.getPackedPositionGroup(info.packedPosition);
        int childPosition = ExpandableListView.getPackedPositionChild(info.packedPosition);

        if(type == ExpandableListView.PACKED_POSITION_TYPE_CHILD){
            ChildPair object =  (ChildPair) expandableListAdapter.getChild(groupPosition,childPosition);
            String title = object.getName();


            final int DELETE_COURSE = 0;
            final int UPDATE_COURSE = 1;

            menuItems = new String[]{};
            menu.setHeaderTitle(title);

            menuItems = getResources().getStringArray(R.array.menuItems);
            menu.add(0,DELETE_COURSE,0,menuItems[0]);
            menu.add(0,UPDATE_COURSE,0,menuItems[1]);

        }
    }
    @Override
    public boolean onContextItemSelected(MenuItem menuItem) {
        ExpandableListView.ExpandableListContextMenuInfo info =
                (ExpandableListView.ExpandableListContextMenuInfo) menuItem.getMenuInfo();

        int groupPosition = 0;
        int childPosition = 0;

        int type = ExpandableListView.getPackedPositionType(info.packedPosition);
        if (type == ExpandableListView.PACKED_POSITION_TYPE_CHILD){
            groupPosition = ExpandableListView.getPackedPositionGroup(info.packedPosition);
            childPosition = ExpandableListView.getPackedPositionChild(info.packedPosition);
        }

        ChildPair object = (ChildPair) expandableListAdapter.getChild(groupPosition,childPosition);
        String course = object.getName();
        long id = object.getRowId();

        switch (menuItem.getItemId()){
            case 0:{
                courseRepo = new CourseRepo();
                Cursor cursor  = courseRepo.getRow(id);
                courseRepo.deleteRow(id);
                long foreignKey = cursor.getLong(cursor.getColumnIndex(Course.COLUMN_SemesterId));
                deleteItem(groupPosition, foreignKey);
                Toast.makeText(getBaseContext(),"DELETE "+course,Toast.LENGTH_SHORT).show();
                break;
            }

            case 1:{
                UpdateCourseDialog mUpdateCourseDialog = UpdateCourseDialog.newInstance(course,id);
                mUpdateCourseDialog.show(getSupportFragmentManager(),"Update course");
                Toast.makeText(getBaseContext(),course + " Updated", Toast.LENGTH_SHORT).show();
                break;
            }
            default:
                return super.onContextItemSelected(menuItem);
        }
        return true;
    }

    //Function returnItemId takes care of proper indexing course entries
    //When deleting courses from database this function returns id of  previously inserted entry
    public long returnItemId(long id){
        arrayOfIds = new ArrayList<Long>(){};
        cursor = courseRepo.getAllRows();
        cursor.moveToFirst();
        //Make array list of ids that are written in database
        do{
            arrayOfIds.add(cursor.getLong(cursor.getColumnIndex(Course.COLUMN_CourseId)));
        }while(cursor.moveToNext());
        cursor.close();

        //Get the index of specific id in array list
        //And delete it from array
        long index = arrayOfIds.indexOf(id);
        arrayOfIds.remove(id);

        if(cursor.getCount() > 1 && index != 0){
            return  arrayOfIds.get((int)index-1);
        }
        else if(cursor.getCount()> 1 && index == 0){
            return  arrayOfIds.get((int)index);
        }
        else
            return -1;

    }


}
