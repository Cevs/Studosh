package com.cevs.studosh.Dialogs;

import android.app.FragmentManager;
import android.content.Context;

import com.cevs.studosh.Dialogs.ContentDialog;
import com.cevs.studosh.Dialogs.CourseDialog;

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

    public DialogHelper(){}

    public void setCourseDialog(){
        CourseDialog mCourseDialog = new CourseDialog();
        mCourseDialog.show(fragmentManager, "CourseDialog");
    }

    public void setContentDialog(){
        ContentDialog mContentDialog;
        mContentDialog = ContentDialog.newInstance(courseId);
        mContentDialog.show(fragmentManager,"ContentDialog");

    }

    public void setUpdateContentDialog(String criterion, long rowId){
        UpdateContentDialog mUpdateContentDialog;
        mUpdateContentDialog = UpdateContentDialog.newInstance(criterion,rowId);
        mUpdateContentDialog.show(fragmentManager,"UpdateContentDialog");
    }


}
