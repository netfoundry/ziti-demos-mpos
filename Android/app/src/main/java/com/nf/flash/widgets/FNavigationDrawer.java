package com.nf.flash.widgets;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Scroller;
import android.widget.TextView;
import com.nf.flash.R;
import com.nf.flash.activities.HomeActivity;
import com.nf.flash.activities.LoginActivity;
import com.nf.flash.activities.SaleTransactionActivity;
import com.nf.flash.adapters.CustomDrawerAdapter;
import com.nf.flash.models.DrawerItem;
import com.nf.flash.utils.Constants;
import com.nf.flash.utils.SecurePreferenceUtils;
import java.util.List;

public class FNavigationDrawer extends LinearLayout {

    private Context context;
    private Activity activity;
    private Scroller scroller;
    private float scrollerDensity;
    private int scrollerSizeWidth;
    private boolean isNavigationDrawerOpened = false;
    private OnNavigationDrawerOpenOrCloseListener navigatioListener;
    private final String navigationQq = "mLastName";

    public FNavigationDrawer(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public FNavigationDrawer(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    private void init() {
        activity = ((Activity) context);
        scroller = new Scroller(context);
    }

    public void setScroll() {
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int screenWidth = metrics.widthPixels;
        activity.getWindowManager().getDefaultDisplay()
                .getMetrics(metrics);
        this.scrollerDensity = metrics.density;
        scrollerSizeWidth = (int) (screenWidth - (108 * scrollerDensity));
    }

    public void scroll() {
        animationStart();
    }

    @Override
    public void computeScroll() {
        if (scroller.computeScrollOffset()) {
            scrollTo(scroller.getCurrX(), scroller.getCurrY());
            postInvalidate();
        }
    }

    private void animationStart() {
        if (scroller.getCurrX() < 0) {
            setNavigationDrawerOpen(false);
            if (navigatioListener != null)
                navigatioListener.onDrawerOpenOrClosed(false);
            scroller.startScroll(scroller.getCurrX(), scroller.getCurrY(), -1
                    * scroller.getCurrX(), 0, 500);

        } else {
            setNavigationDrawerOpen(true);
            if (navigatioListener != null)
                navigatioListener.onDrawerOpenOrClosed(true);
            scroller.startScroll(scroller.getCurrX(), scroller.getCurrY(),
                    -scrollerSizeWidth, 0, 500);

        }
        invalidate();
    }

    public void findListAndSetAdapter(ListView listView, String activityName, int layout, List<DrawerItem> list) {
        if (list == null || list.size() == 0)
            return;
        CustomDrawerAdapter adapter = new CustomDrawerAdapter(context, layout,
                list, activityName);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new DrawerItemClickListener());
        listView.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return !isNavigationDrawerOpen();
            }
        });
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,long id) {

            Intent intent = null;
            TextView textView = view.findViewById(R.id.drawer_itemName);

            if (!(activity instanceof HomeActivity) && textView.getText().toString().equalsIgnoreCase(Constants.Flash_MENU_HOME)) {
                intent = new Intent(activity, HomeActivity.class);
            }else if (!(activity instanceof SaleTransactionActivity) && textView.getText().toString().equalsIgnoreCase(Constants.Flash_MENU_SALE)) {
                intent = new Intent(activity, SaleTransactionActivity.class);
            }else if (textView.getText().toString().equalsIgnoreCase(Constants.Flash_MENU_SIGN_OUT)) {
                SecurePreferenceUtils.setStringInPreferences("lastsevicecalltimestamp", "");
                SecurePreferenceUtils.setStringInPreferences(navigationQq, "");
                SecurePreferenceUtils.setBooleanInPreferences(Constants.IS_LOGOUT, true);
                activity.startActivity(new Intent(activity, LoginActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP /*| Intent.FLAG_ACTIVITY_CLEAR_TASK*/));
            } else {
                scroll();
            }

            if (intent == null)
                return;
            scroll();
            activity.startActivity(intent);
            activity.overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
        }
    }

    public final boolean isNavigationDrawerOpen() {
        return isNavigationDrawerOpened;
    }

    private final void setNavigationDrawerOpen(boolean isNavigationDrawerOpen) {
        this.isNavigationDrawerOpened = isNavigationDrawerOpen;
    }

    public void setPosition() {
        if (isNavigationDrawerOpen())
            scrollTo(-scrollerSizeWidth, scroller.getCurrY());
    }

    public interface OnNavigationDrawerOpenOrCloseListener {
        void onDrawerOpenOrClosed(boolean isOpen);
    }

}
