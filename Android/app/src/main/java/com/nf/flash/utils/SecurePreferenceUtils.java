package com.nf.flash.utils;

import android.app.Application;
 import com.nf.flash.WalletApp;

public class SecurePreferenceUtils extends Application {

    public static void setStringInPreferences(String key, String value) {
        WalletApp.getWalletAppInstance().getSecuredPreferences().edit().putString(key, value).commit();
    }

    public static String getStringFromPreferences(String key, String defaultValue) {
        return WalletApp.getWalletAppInstance().getSecuredPreferences().getString(key, defaultValue);
    }

    public static void clearDataFromPreferences() {
        WalletApp.getWalletAppInstance().getSecuredPreferences().edit().clear().commit();
    }

    public static void setBooleanInPreferences(String key, boolean value) {
        WalletApp.getWalletAppInstance().getSecuredPreferences().edit()
                .putBoolean(key, value).commit();
    }

    public static int getIntegerFromPreferences(String key, Integer defaultValue) {
        return WalletApp.getWalletAppInstance().getSecuredPreferences()
                .getInt(key, defaultValue);
    }

}
