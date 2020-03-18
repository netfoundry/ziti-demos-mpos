package com.nf.flash.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

public class MainTemplates extends RelativeLayout {

    private FNavigationDrawer qNavigationDrawer;

    public MainTemplates(Context context) {
        super(context);
    }

    public MainTemplates(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MainTemplates(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        boolean isWhat = false;
        if(qNavigationDrawer != null)
            isWhat = qNavigationDrawer.isNavigationDrawerOpen();
        if(isWhat)
            return isWhat;
        return super.dispatchTouchEvent(event);
    }

    public void setQNavigationDrawer(FNavigationDrawer qDrawer) {
        this.qNavigationDrawer = qDrawer;
    }
}
