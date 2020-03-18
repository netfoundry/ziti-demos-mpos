package com.nf.flash;

import android.app.Application;
import com.nf.flash.preferences.SecurePreferences;

public class WalletApp extends Application {
    public static final String TAG = "flash";
    private static WalletApp globalWalletAppInstance;
    private static boolean shutdownApp = false;
    private static SecurePreferences securedStoragePreferences;

    @Override
    public void onCreate() {
        super.onCreate();
        globalWalletAppInstance = this;
    }


    public static WalletApp getWalletAppInstance() {
        return globalWalletAppInstance;
    }

    public SecurePreferences getSecuredPreferences(){
        if(securedStoragePreferences == null){
            securedStoragePreferences = new SecurePreferences(getApplicationContext());
        }
        return securedStoragePreferences;
    }

    public static void setShutdown() {
        shutdownApp = true;
    }


}
