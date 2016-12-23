package com.cevs.studosh;

import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.cevs.studosh.Dialogs.DialogHelper;
import com.cevs.studosh.Dialogs.UpdateCourseDialog;
import com.cevs.studosh.InitialFragments.InitialCriteriaFragment;
import com.cevs.studosh.InitialFragments.InitialGeneralFragment;
import com.cevs.studosh.InitialFragments.InitialPresenceFragment;
import com.cevs.studosh.data.DBHelper;
import com.cevs.studosh.data.DataBaseManager;
import com.cevs.studosh.data.model.Content;
import com.cevs.studosh.data.model.Course;
import com.cevs.studosh.data.model.Presence;
import com.cevs.studosh.data.model.Semester;
import com.cevs.studosh.data.repo.ContentRepo;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import static android.webkit.ConsoleMessage.MessageLevel.LOG;


public class MainActivity extends AppCompatActivity  {

    GeneralFragment generalFragment;
    CriteriaFragment criteriaFragment;
    PresenceFragment presenceFragment;
    private MyPagerAdapter mPagerAdapter;
    private DialogHelper mDialogHelper;
    private ExpandableListAdapter expandableListAdapter;
    private ExpandableListView expandableListView;
    private List<String> listDataHeader;
    private HashMap<String, List<ChildPair>> listDataChild;
    private DrawerLayout drawer;
    private long courseId;
    private int type;
    private ViewPager mViewPager;
    private final static int LOG_IN = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mDialogHelper = new DialogHelper(getBaseContext(),getFragmentManager());

        DBHelper dbHelper = new DBHelper(this);
        DataBaseManager.initializeInstance(dbHelper);

        final SemesterRepo semesterRepo = new SemesterRepo();
        final CourseRepo courseRepo = new CourseRepo();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        initUI();

        ImageView mainItemIcon = new ImageView(this);
        ImageView subItemIcon1 = new ImageView(this);
        ImageView subItemIcon2 = new ImageView(this);
        ImageView subItemIcon3 = new ImageView(this);
        ImageView subItemIcon4 = new ImageView(this);
        ImageView subItemIcon5 = new ImageView(this);
        ImageView subItemIcon6 = new ImageView(this);



        FloatingActionButton fab = new FloatingActionButton.Builder(this).setContentView(mainItemIcon)
                .setLayoutParams(new FloatingActionButton.LayoutParams(250,250))
                .setPosition(5).build();

        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(this)
                .setLayoutParams(new FloatingActionButton.LayoutParams(160,160));


        mainItemIcon.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.menu_button));
        subItemIcon1.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.add_semester));
        subItemIcon2.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.add_course));
        subItemIcon3.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.add_criteria));
        subItemIcon4.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.add_presence));
        subItemIcon5.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.mark));
        subItemIcon6.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.cross));

        SubActionButton subButton1 = itemBuilder.setContentView(subItemIcon1).build();
        SubActionButton subButton2 = itemBuilder.setContentView(subItemIcon2).build();
        SubActionButton subButton3 = itemBuilder.setContentView(subItemIcon3).build();
        SubActionButton subButton4 = itemBuilder.setContentView(subItemIcon4).build();
        SubActionButton subButton5 = itemBuilder.setContentView(subItemIcon5).build();
        SubActionButton subButton6 = itemBuilder.setContentView(subItemIcon6).build();



        FloatingActionMenu actionMenu = new FloatingActionMenu.Builder(this).addSubActionView(subButton1).addSubActionView(subButton2)
                .addSubActionView(subButton3).addSubActionView(subButton4).addSubActionView(subButton5)
                .addSubActionView(subButton6).attachTo(fab).setRadius(300).setStartAngle(200).setEndAngle(340).build();

        /*Sadly, this must be implemented this way because the developers of this library
            didn't create a way to find out or to set id to a buttons*/
        subButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialogHelper.setSemesterDialog();
            }
        });
        subButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(((semesterRepo.getAllRows()).getCount())>0){
                    mDialogHelper.setCourseDialog();

                }
                else
                    Toast.makeText(getBaseContext(),"Treba unijeti barem jedan semestar", Toast.LENGTH_SHORT).show();

            }
        });
        subButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(((courseRepo.getAllRows()).getCount())>0){
                    mDialogHelper = new DialogHelper(getBaseContext(),courseId);
                    mDialogHelper.setContentDialog();
                    mViewPager.setCurrentItem(1);
                }
                else
                    Toast.makeText(getBaseContext(),"Treba unijeti barem jedan kolegij", Toast.LENGTH_SHORT).show();

            }
        });
        subButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(((courseRepo.getAllRows()).getCount())>0){
                    mDialogHelper = new DialogHelper(getBaseContext(),courseId);
                    mDialogHelper.setDateDialog(type);
                    mViewPager.setCurrentItem(2);

                }
                else
                    Toast.makeText(getBaseContext(),"Treba unijeti barem jedan kolegij",Toast.LENGTH_SHORT).show();
            }
        });

        subButton5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewPager.setCurrentItem(2);

            }
        });

        subButton6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewPager.setCurrentItem(2);

            }
        });

        createExpandableList();
    }


    public void initUI(){
        ArrayList<PagerItem> pagerItems = new ArrayList<PagerItem>();
        CourseRepo courseRepo = new CourseRepo();
        Cursor cursor = courseRepo.getAllRows();
        cursor.moveToFirst();


        mViewPager = (ViewPager) findViewById(R.id.vp);
        mPagerAdapter = new MyPagerAdapter(getSupportFragmentManager(),pagerItems);
        mViewPager.setAdapter(mPagerAdapter);
        NavigationTabStrip mCenterNavigationTabStrip = (NavigationTabStrip) findViewById(R.id.nts_center);
        mCenterNavigationTabStrip.setViewPager(mViewPager);
        mCenterNavigationTabStrip.setStripColor(ContextCompat.getColor(getBaseContext(),R.color.navigationTabStrip));


        if(cursor.getCount()==0){
            setInitialFragments();

        }
        else{
            setFragments(cursor.getLong(cursor.getColumnIndex(Course.COLUMN_CourseId)));
            courseId = cursor.getLong(cursor.getColumnIndex(Course.COLUMN_CourseId));
            setActionBar(courseId);

        }
        setNumberOfCoursesAndSemesters();
        cursor.close();

    }

    public void setInitialFragments(){
        ArrayList<PagerItem> pagerItems = new ArrayList<PagerItem>();
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

        ArrayList<PagerItem> pagerItems = new ArrayList<PagerItem>();
        pagerItems.add(new PagerItem("General fragment",generalFragment.newInstance(position)));
        pagerItems.add(new PagerItem("Criteria fragment", criteriaFragment.newInstance(position)));
        pagerItems.add(new PagerItem("Absence fragment", presenceFragment.newInstance(position)));

        if(!same) {
            mViewPager.setCurrentItem(0);
        }
        setActionBar(courseId);
        mPagerAdapter.setPagerItems(pagerItems);
        mPagerAdapter.notifyDataSetChanged();
}


    public void populateList(){

        expandableListView = (ExpandableListView) findViewById(R.id.expandable_list);
        //Set position of indicator arrow
        expandableListView.setIndicatorBounds(5,0);
        sortAscendingHeader(listDataHeader);
        //Collections.sort(listDataHeader);
        expandableListAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
        expandableListView.setAdapter(expandableListAdapter);
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long id) {


                ChildPair object = (ChildPair) expandableListAdapter.getChild(groupPosition,childPosition);
                view.setSelected(true);
                drawer.closeDrawer(GravityCompat.START);
                long row = object.getRowId();
                setActionBar(row);
                setFragments(row);
                return false;
            }
        });
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

                    sortAscendingChildren((ArrayList<ChildPair>) children);
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

        }
        setNumberOfCoursesAndSemesters();
        populateList();
    }

    //Inserting new element in navigationDrawerSemesterList
    //Expandable list
    public void updateNavigationDrawerSemesterList(int semesterId, String courseName, long rowId){
        /*Method for updating item in expandable list adapter that have new set of data
            the rest remains unchanged
            Its called after inserting new course in db (via courseDialog class)*/
        ChildPair pair;
        int k = 0;
        String cName = courseName;
        ArrayList<ChildPair> children = new ArrayList<ChildPair>();
        //-1 because array list of headers starts with index 0 while index in db starts with 1
        int size = expandableListAdapter.getChildrenCount(semesterId);
        if(size>0){
            while(k<size){
                pair = new ChildPair();
                ChildPair object = (ChildPair) expandableListAdapter.getChild(semesterId,k++);
                pair.setName(object.getName());
                pair.setRowId(object.getRowId());
                children.add(pair);
            }
        }

        pair = new ChildPair();
        pair.setName(cName);
        pair.setRowId(rowId);
        children.add(pair);
        sortAscendingChildren(children);
        listDataChild.put(listDataHeader.get(semesterId), children);
        expandableListAdapter.newData(listDataHeader,listDataChild);
        expandableListView.setAdapter(expandableListAdapter);
        expandableListAdapter.notifyDataSetChanged();
        setNumberOfCoursesAndSemesters();
    }

    //Adding the empty header of entered semester
    //Refreshing the list of semesters in navigation drawer after the entering via dialog
    public void updateNavigationDrawerHeader(String nSemester){

        String string = nSemester;
        listDataHeader.add(nSemester);
        listDataChild.put(nSemester,new ArrayList<ChildPair>());
        sortAscendingHeader(listDataHeader);
        expandableListAdapter.newData(listDataHeader,listDataChild);
        expandableListView.setAdapter(expandableListAdapter);
        expandableListAdapter.notifyDataSetChanged();
        setNumberOfCoursesAndSemesters();
    }

    public void deleteSemester(String semesterName){
        SemesterRepo semesterRepo = new SemesterRepo();
        semesterRepo.deleteRow(semesterName);
        createExpandableList();
        setActionBar(-1);
        setInitialFragments();
        setNumberOfCoursesAndSemesters();
    }

    //Updating only the specific header with changes after the deletion of course
    public void deleteItem(int groupPosition, int deletedChildPosition, long foreignKey){
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
            cursor.close();
        }

        expandableListAdapter.newData(listDataHeader,listDataChild);
        expandableListView.setAdapter(expandableListAdapter);
        expandableListAdapter.notifyDataSetChanged();
        expandableListView.expandGroup(groupPosition);

        int childSize = expandableListAdapter.getChildrenCount(groupPosition);
        sortAscendingChildren(children);

        if(childSize>0){

            setFragments(getItemId(groupPosition,deletedChildPosition));
        }
        else
            setInitialFragments();

        setNumberOfCoursesAndSemesters();
    }

    public void updateCourse(String courseName, long id) {
        DialogHelper mDialogHelper = new DialogHelper();
        mDialogHelper.setUpdateCourseDialog(courseName, id);

    }

    @Override
    public void onBackPressed() {
        //drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /*method that returns rowId of next child in list
        if delete item at position >1 return deletedPosition +1
        else return 1*/
    public long getItemId(int groupPosition, int deletedChildPosition){

        int previousChild;
        int nextChild;
        ChildPair object;
        if (deletedChildPosition>0){
            previousChild = deletedChildPosition - 1;
            object = (ChildPair) expandableListAdapter.getChild(groupPosition,previousChild);

            return object.getRowId();
        }
        else{
            nextChild = deletedChildPosition;
            object = (ChildPair) expandableListAdapter.getChild(groupPosition,nextChild);

            return object.getRowId();
        }
    }


    public void sortAscendingHeader(List<String> headers){
        Collections.sort(headers, new Comparator<String>() {
            @Override
            public int compare(String header1, String header2) {
                /*if-else is needed because .compareToIgnoreCase evaluate string wrong if one string is longer than other
                Especially if numbers are involved*/
                if(header1.length()>header2.length())
                    return 1;
                else if (header1.length()<header2.length())
                    return -1;

                return header1.compareToIgnoreCase(header2);
            }
        });
    }

    public void sortAscendingChildren(ArrayList<ChildPair> children){
        //Sorting in ascending order
        Collections.sort(children, new Comparator<ChildPair>() {
            @Override
            public int compare(ChildPair pair1, ChildPair pair2) {
                return pair1.getName().compareToIgnoreCase(pair2.getName());
            }
        });
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
        if (id == R.id.action_login) {
            clearCookies(getBaseContext());
            boolean mobileDataEnabled = false;

            ConnectivityManager cm = (ConnectivityManager) getBaseContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            try{
                WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
                Class cmClass = Class.forName(cm.getClass().getName());
                Method method = cmClass.getDeclaredMethod("getMobileDataEnabled");
                method.setAccessible(true); //make the method callable
                //get the setting for "mobile data"
                mobileDataEnabled = (Boolean) method.invoke(cm);

                //If mobile data and wifi is turned off, turn it on
                if (!mobileDataEnabled && !wifi.isWifiEnabled()){

                    Intent settingsIntent = new Intent(Settings.ACTION_SETTINGS);
                    startActivityForResult(settingsIntent, 9003);

                }

                else{
                    Intent intent = new Intent(MainActivity.this,FoiLogIn.class);
                    startActivityForResult(intent,LOG_IN);
                }


            }catch (Exception e){
                Log.d("Mobile data",e.toString());
            }


            return true;
        }
        if (id == R.id.action_clear){
            Toast.makeText(getApplicationContext(),"Obriši sve", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //Method that gets called from PresenceFragment class when you changed type of calendar via spinner
    public void setCalendarType(int type){
        this.type = type;
    }

    //Deleting all cookies stored in app so we get that user every time needs to log in if he wants to fetch data (courses)
    @SuppressWarnings("deprecation")
    public static void clearCookies(Context context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {

            CookieManager.getInstance().removeAllCookies(null);
            CookieManager.getInstance().flush();
        } else {

            CookieSyncManager cookieSyncMngr = CookieSyncManager.createInstance(context);
            cookieSyncMngr.startSync();
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.removeAllCookie();
            cookieManager.removeSessionCookie();
            cookieSyncMngr.stopSync();
            cookieSyncMngr.sync();
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == LOG_IN && resultCode == RESULT_OK){
            String sessionId = data.getExtras().getString("SESSIONID");
            Log.d("URL",sessionId);
            downloadData(sessionId);
        }
    }

    //download data from server
    public void downloadData(String id){

        ArrayList<Course> courseNames = new ArrayList<Course>();
        String url = "http://nastava-api.azurewebsites.net/api/Subjects/Actual/"+id;
        RequestQueue queue = Volley.newRequestQueue(this);

        final ProgressDialog progressDialog = ProgressDialog.show(this,"Fetching data","Please wait...",true);
        progressDialog.show();
        JsonArrayRequest myRequest = new JsonArrayRequest(Request.Method.GET,url,new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                Log.d("VOLLEY",response.toString());
                CourseRepo courseRepo = new CourseRepo();
                SemesterRepo semesterRepo = new SemesterRepo();
                Course course = new Course();

                for(int i = 0; i<response.length(); i++){
                    try {

                        JSONObject object = response.getJSONObject(i);
                        String courseName = object.getString("Name");
                        int semesterNumber = object.getInt("Semester");
                        int ects = object.getInt("Ects");

                        Semester semester = new Semester();

                        String semesterName = "Semestar "+semesterNumber;
                        Boolean exists = semesterRepo.findRow(semesterName);
                        if(!exists){
                            semester.setSemesterName(semesterName);
                            semesterRepo.insertRow(semester);
                        }


                        Log.d("VOLLEY", courseName);
                        Log.d("VOLLEY",semesterName);
                        Log.d("VOLLEY",ects+"");

                        exists = courseRepo.findRow(courseName);
                        if(!exists){
                            Cursor cursor = semesterRepo.getRow(semesterName);
                            course.setSemesterId(cursor.getLong(cursor.getColumnIndex(Semester.COLUMN_SemesterId)));
                            course.setCourseName(courseName);
                            course.setCourseECTS(ects);
                            courseRepo.insertRow(course);
                            cursor.close();
                        }
                        else{
                            Log.d("VOLLEY","Već postoji");
                        }



                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                createExpandableList();
                progressDialog.dismiss();

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("VOLLEY", error.toString());
            }
        });


        int socketTimeout = 15000;
        myRequest.setRetryPolicy(new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Log.d("Socket",myRequest.getRetryPolicy().getCurrentRetryCount()+"");
        Log.d("Socket",myRequest.getRetryPolicy().getCurrentTimeout()+"");
        myRequest.getRetryPolicy();
        queue.add(myRequest);


    }

    public void setActionBar(long rowId){
        if(rowId!=-1){
            CourseRepo courseRepo = new CourseRepo();
            Cursor cursor = courseRepo.getRow(rowId);
            String courseName = cursor.getString(cursor.getColumnIndex(Course.COLUMN_CourseName));
            getSupportActionBar().setTitle(courseName);
        }
        else
        {
            getSupportActionBar().setTitle("Studosh");
        }

    }

    public void setNumberOfCoursesAndSemesters(){
        //Navigation drawer (header)
        CourseRepo courseRepo = new CourseRepo();
        SemesterRepo semesterRepo = new SemesterRepo();

        Cursor courseCursor = courseRepo.getAllRows();
        Cursor courseSemester = semesterRepo.getAllRows();

        int numberOfSemesters = courseCursor.getCount();
        int numberOfCourses = courseSemester.getCount();

        TextView coursesNumber = (TextView) findViewById(R.id.textView_numberOfCourses);
        TextView semestersNumber = (TextView) findViewById(R.id.textView_numberOfSemesters);

        coursesNumber.setText(numberOfCourses+"");
        semestersNumber.setText(numberOfSemesters+"");
    }
}
