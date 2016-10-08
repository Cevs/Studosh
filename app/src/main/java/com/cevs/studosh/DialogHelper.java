package com.cevs.studosh;

import android.app.FragmentManager;
import android.content.Context;

/**
 * Created by TOSHIBA on 28.09.2016..
 */

public class DialogHelper {
    static FragmentManager fragmentManager;
    Context context;
    long courseId;

    public DialogHelper(Context context, FragmentManager fragmentManager) {
        this.context = context;
        this.fragmentManager = fragmentManager;
    }

    public DialogHelper(Context context, long courseId){
        this.context = context;
        this.courseId = courseId;
    }

    public void setCourseDialog(){
        CourseDialog mCourseDialog = new CourseDialog();
        mCourseDialog.show(fragmentManager, "CourseDialog");
    }

    public void setContentDialog(){
        ContentDialog mContentDialog;
        //dodan context
        mContentDialog = ContentDialog.newInstance(courseId);
        mContentDialog.show(fragmentManager,"ContentDialog");

    }


}
