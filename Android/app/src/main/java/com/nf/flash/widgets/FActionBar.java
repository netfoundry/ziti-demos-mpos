package com.nf.flash.widgets;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.nf.flash.R;
import com.nf.flash.activities.HomeActivity;
import com.nf.flash.utils.Constants;
import com.nf.flash.utils.Utils;
import android.view.WindowManager;

public class FActionBar extends RelativeLayout {

    private Context context;
    private RelativeLayout actionBarMainLayout;
    private ImageView navigationDrawer;
    private TextView logOutTv;
    private TextView lefLabel;
    private FNavigationDrawer fNavigationDrawer;


    public FActionBar(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public FActionBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public FActionBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    private void init() {
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mInflater.inflate(R.layout.qaction_bar, this, true);
        actionBarMainLayout = findViewById(R.id.actionbar_main_layout);
        navigationDrawer = findViewById(R.id.navigation_drawer);
        logOutTv = findViewById(R.id.log_out_tv);
        lefLabel = findViewById(R.id.left_navigation_label);
        initCallBacks();
        setDrawerMenu();
    }

    private void initCallBacks(){
        actionBarMainLayout.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        logOutTv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            switch (getRightLabel(v)){
                case Constants.Flash_CANCEL:
                     Intent  intent = new Intent(context, HomeActivity.class);
                    context.startActivity(intent);
                    ((Activity)context).overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
                    break;
            }
            }
        });
    }

    public void setDrawerMenu() {
        if(isTablet(context)){
            navigationDrawer.getLayoutParams().height = dpToPx(40);
            navigationDrawer.getLayoutParams().width = dpToPx(50);
            navigationDrawer.requestLayout();
            navigationDrawer.setImageResource(R.drawable.navigation_slider_button_tablet);
        }else {
            navigationDrawer.setImageResource((R.drawable.navigation_slider_button));
        }

        navigationDrawer.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("navigationDrawer", "navigationDrawer "+ navigationDrawer);
                if(fNavigationDrawer == null)
                    throw new NullPointerException("QNavigationDrawer cannot be null, " +
                            "call QActionBar.setQNavigationDrawer() to set QNavigationDrawer.");
                fNavigationDrawer.scroll();
                ((Activity)context).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                Utils.hideKeyboard(((Activity)context));
            }
        });
    }

    public void setQNavigationDrawer(FNavigationDrawer qDrawer) {
        this.fNavigationDrawer = qDrawer;
    }

    public String getRightLabel(View v){
        TextView  tv = (TextView)v;
        return tv.getText().toString();
    }

    public static boolean isTablet(Context ctx){
        return (ctx.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    private int dpToPx(int dp) {
        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int px = Math.round(dp * (metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }
}
