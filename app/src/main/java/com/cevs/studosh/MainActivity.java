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

        mViewPager = (ViewPager) findViewById(R.id.vp);
        mPagerAdapter = new MyPagerAdapter(supportFragmentManager,pagerItems);
        mViewPager.setAdapter(mPagerAdapter);
        mCenterNavigationTabStrip = (NavigationTabStrip) findViewById(R.id.nts_center);
        mCenterNavigationTabStrip.setViewPager(mViewPager);

        mPagerAdapter.setPagerItems(pagerItems);
        mPagerAdapter.notifyDataSetChanged();

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
        if (v.getId()==R.id.course_list){
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            String title = cursor.getString(cursor.getColumnIndex(Course.COLUMN_CourseName));
            menu.setHeaderTitle(title);

            itemId = info.id;

            menuItems = getResources().getStringArray(R.array.menuItems);
            for (int i = 0;i<menuItems.length;i++){
                menu.add(menuItems[i]);
            }
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        courseRepo = new CourseRepo();
        String option = item.getTitle().toString();
        String courseName = cursor.getString(cursor.getColumnIndex(Course.COLUMN_CourseName));
        String courseSemester = cursor.getString(cursor.getColumnIndex(Course.COLUMN_Semester));
        Toast.makeText(getBaseContext(),courseName+"",Toast.LENGTH_SHORT).show();


        switch(option){
            case "Delete":
                courseRepo.deleteRow(itemId);
                populateList();
                Toast.makeText(getBaseContext(),courseName + " Deleted", Toast.LENGTH_SHORT).show();
                populateList();
                break;
            case "Delete All":
                courseRepo.deleteAllRows();
                populateList();
                Toast.makeText(getBaseContext(),"All items deleted",Toast.LENGTH_SHORT).show();
                break;
            case "Update":
                UpdateCourseDialog mUpdateCourseDialog = UpdateCourseDialog.newInstance(courseName,courseSemester,itemId);
                mUpdateCourseDialog.show(fragmentManager,"Update course");
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
}
