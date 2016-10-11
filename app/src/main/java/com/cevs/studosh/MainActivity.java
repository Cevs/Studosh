package com.cevs.studosh;

import android.app.FragmentManager;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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

    private ViewPager mViewPager;
    private NavigationTabStrip mCenterNavigationTabStrip;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Ovo je stavljeno tu tako da se fragment manager odma prosljedi Dailogu helperu da ne bude crashova
        DialogHelper mDialogHelepr = new DialogHelper(getBaseContext(),getFragmentManager());

        DBHelper dbHelper = new DBHelper(this);
        DataBaseManager.initializeInstance(dbHelper);

        fragmentManager = getFragmentManager();
        supportFragmentManager = getSupportFragmentManager();
        //promjenit
        pagerItems = new ArrayList<PagerItem>();
        pagerItems.add(new PagerItem("General fragment", new InitialGeneralFragment()));
        pagerItems.add(new PagerItem("Criteria fragment", new InitialCriteriaFragment()));
        pagerItems.add(new PagerItem("Absence fragment", new InitialPresenceFragment()));

        initUI();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogHelper mDialogHelper = new DialogHelper(getBaseContext(), fragmentManager);
                mDialogHelper.setCourseDialog();
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

    //Novo
    public void initUI(){
        mViewPager = (ViewPager) findViewById(R.id.vp);
        mPagerAdapter = new MyPagerAdapter(supportFragmentManager,pagerItems);
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setCurrentItem(0);
        mCenterNavigationTabStrip = (NavigationTabStrip) findViewById(R.id.nts_center);
        mCenterNavigationTabStrip.setViewPager(mViewPager);
        mPagerAdapter.setPagerItems(pagerItems);
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
                pagerItems = new ArrayList<PagerItem>();
                pagerItems.add(new PagerItem("General fragment",generalFragment.newInstance(l)));
                pagerItems.add(new PagerItem("Criteria fragment", criteriaFragment.newInstance(l)));
                pagerItems.add(new PagerItem("Absence fragment", presenceFragment.newInstance(l)));


                mPagerAdapter.setPagerItems(pagerItems);
                mViewPager.setCurrentItem(0);
                mPagerAdapter.notifyDataSetChanged();

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
                courseRepo.deleteRow(itemId);
                populateList();
                Toast.makeText(getBaseContext(),courseName + " Deleted", Toast.LENGTH_SHORT).show();
                Cursor cursor;
                courseRepo = new CourseRepo();
                cursor = courseRepo.getAllRows();
                if (cursor.getCount()==0){
                    setInitialFragments();
                }
                else {
                    setFragments(itemId - 1);
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
        pagerItems = new ArrayList<PagerItem>();
        pagerItems.add(new PagerItem("General fragment",generalFragment.newInstance(position)));
        pagerItems.add(new PagerItem("Criteria fragment", criteriaFragment.newInstance(position)));
        pagerItems.add(new PagerItem("Absence fragment", presenceFragment.newInstance(position)));

        mPagerAdapter.setPagerItems(pagerItems);
        mViewPager.setCurrentItem(0);
        mPagerAdapter.notifyDataSetChanged();


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
}
