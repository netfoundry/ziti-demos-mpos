package com.nf.flash.utils;

import android.app.Activity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class Utils {

    public static void hideKeyboard(Activity activity) {
        InputMethodManager inputMethodMgr = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        inputMethodMgr.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

   public static boolean isSessionTimeOut(){
        return false;
    }
}
