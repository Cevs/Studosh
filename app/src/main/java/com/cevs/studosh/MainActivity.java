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
import com.cevs.studosh.data.repo.CourseRepo;
import com.cevs.studosh.fragments.CriteriaFragment;
import com.cevs.studosh.fragments.GeneralFragment;
import com.cevs.studosh.fragments.PagerItem;
import com.cevs.studosh.fragments.PresenceFragment;
import com.gigamole.navigationtabstrip.NavigationTabStrip;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDialogHelper = new DialogHelper(getBaseContext(),getFragmentManager());

        DBHelper dbHelper = new DBHelper(this);
        DataBaseManager.initializeInstance(dbHelper);

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


        //subItemIcon4.setColorFilter(R.color.mainColor);
        //subItemIcon5.setColorFilter(R.color.mainColor);


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
                mViewPager.setCurrentItem(0);
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




        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

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

        if(cursor.getCount()==0){
            setInitialFragments();
        }
        else{
            setFragments(cursor.getLong(cursor.getColumnIndex(Course.COLUMN_CourseId)));
            courseId = cursor.getLong(cursor.getColumnIndex(Course.COLUMN_CourseId));
        }

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

    public void setFragments(long position){
        courseId = position;
        pagerItems = new ArrayList<PagerItem>();
        pagerItems.add(new PagerItem("General fragment",generalFragment.newInstance(position)));
        pagerItems.add(new PagerItem("Criteria fragment", criteriaFragment.newInstance(position)));
        pagerItems.add(new PagerItem("Absence fragment", presenceFragment.newInstance(position)));

        mPagerAdapter.setPagerItems(pagerItems);
        mViewPager.setCurrentItem(0);
        mPagerAdapter.notifyDataSetChanged();

    }



    public void populateList(){
        mCourseRepo = new CourseRepo();
        cursor = mCourseRepo.getAllRows();
        fromFieldNames = new String[]{Course.COLUMN_CourseName};
        int[] toViewIds= new int[]{R.id.textView_course_name};
        simpleCursorAdapter = new SimpleCursorAdapter(getBaseContext(),R.layout.course_item, cursor,fromFieldNames,toViewIds,0);
        ListView list = (ListView)findViewById(R.id.course_list);
        list.setAdapter(simpleCursorAdapter);
        registerForContextMenu(list);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                courseId = l;
                setFragments(l);
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu,v,menuInfo);

        final int DELETE_COURSE = 0;
        final int DELETE_ALL_COURSES = 1;
        final int UPDATE_COURSE = 2;
        if (v.getId()==R.id.course_list){
            menuItems = new String[]{};
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            String title = cursor.getString(cursor.getColumnIndex(Course.COLUMN_CourseName));
            menu.setHeaderTitle(title);

            itemId = info.id;
            menuItems = getResources().getStringArray(R.array.menuItems);
            menu.add(0,DELETE_COURSE,0,menuItems[0]);
            menu.add(0,DELETE_ALL_COURSES,0,menuItems[1]);
            menu.add(0,UPDATE_COURSE,0,menuItems[2]);

        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        long index;
        courseRepo = new CourseRepo();

        String courseName = cursor.getString(cursor.getColumnIndex(Course.COLUMN_CourseName));
        String courseSemester = cursor.getString(cursor.getColumnIndex(Course.COLUMN_Semester));

        //Managing the initialization  of fragments when deleting or updating
        int iid  = item.getItemId();
        switch(iid){
            //When delete course from db, repopulate list in navigationdrawer and fill the fragments in tab with data of
            //fragment at position deleted-1
            //If there is no data then set fragments to initial fragmetns
            case 0:
                index =returnItemId(itemId);
                courseRepo.deleteRow(itemId);
                populateList();
                Toast.makeText(getBaseContext(),courseName + " Deleted", Toast.LENGTH_SHORT).show();
                Cursor cursor;
                courseRepo = new CourseRepo();
                cursor = courseRepo.getAllRows();
                if (index == -1){
                    setInitialFragments();
                }
                else {
                    setFragments(index);
                }
                break;
            //When deleted all courses from db, set fragments to initial
            case 1:
                courseRepo.deleteAllRows();
                populateList();
                setInitialFragments();
                Toast.makeText(getBaseContext(),"All items deleted",Toast.LENGTH_SHORT).show();
                break;
            //When update course name or semester in db, load the changes in fragment
            case 2:
                UpdateCourseDialog mUpdateCourseDialog = UpdateCourseDialog.newInstance(courseName,courseSemester,itemId);
                mUpdateCourseDialog.show(getSupportFragmentManager(),"Update course");
                Toast.makeText(getBaseContext(),courseName + " Updated", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
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
