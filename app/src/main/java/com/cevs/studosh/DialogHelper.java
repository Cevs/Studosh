package com.cevs.studosh;

import android.app.FragmentManager;
import android.content.Context;

/**
 * Created by TOSHIBA on 28.09.2016..
 */

public class DialogHelper {
    static FragmentManager fragmentManager;
    Context context;

    public DialogHelper(Context context, FragmentManager fragmentManager) {
        this.context = context;
        this.fragmentManager = fragmentManager;
    }

    public void setCourseDialog(){
        CourseDialog mCourseDialog = new CourseDialog();
        mCourseDialog.show(fragmentManager, "CourseDialog");
    }

    public void setContentDialog(){

    }


}
