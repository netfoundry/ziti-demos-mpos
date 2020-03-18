package com.nf.flash.services;

import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;
import com.nf.flash.WalletApp;
import com.nf.flash.utils.Constants;

public class TimeoutService extends Service {

    private BroadcastReceiver mIntentReceiver;
    public TimeoutService() {

    }

    @Override
    public void onCreate() {
        super.onCreate();
        mIntentReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if ( action.equals(Constants.TIMEOUT) ) {
                    timeout(context);
                }
            }
        };
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.TIMEOUT);
        registerReceiver(mIntentReceiver, filter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }


    private void timeout(Context context) {
        WalletApp.getWalletAppInstance().setShutdown();
        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        nm.cancelAll();
        stopSelf();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mIntentReceiver);
    }

    public class TimeoutBinder extends Binder {
        public TimeoutService getService() {
            return TimeoutService.this;
        }
    }

    private final IBinder mBinder = new TimeoutBinder();

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

}
