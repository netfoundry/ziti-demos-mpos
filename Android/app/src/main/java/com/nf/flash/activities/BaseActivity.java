package com.nf.flash.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.provider.Settings;
import android.widget.Toast;

import com.nf.flash.R;
import com.nf.flash.models.DrawerItem;
import com.nf.flash.utils.Constants;
import com.nf.flash.widgets.FActionBar;
import com.nf.flash.widgets.FNavigationDrawer;
import com.nf.flash.widgets.MainTemplates;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.List;

import io.jsonwebtoken.ExpiredJwtException;
import io.netfoundry.ziti.Ziti;

public class BaseActivity extends Activity {

    protected FActionBar fActionBar;
    protected FNavigationDrawer fNavigationDrawer;
    private MainTemplates mainlayout;
    private ProgressDialog progressDialog;

    public KeyStore mKeyStore;

    @Override
    public void setContentView(int viewId) {
        super.setContentView(viewId);
        fActionBar = findViewById(R.id.custom_actionbar);
        if (fActionBar == null)
            return;
        fNavigationDrawer = findViewById(R.id.navigationDrawer);
        if (fNavigationDrawer != null) {
            fActionBar.setQNavigationDrawer(fNavigationDrawer);
            fNavigationDrawer.setScroll();
        }
        mainlayout = findViewById(R.id.mainlayout);
        if (mainlayout != null)
            mainlayout.setQNavigationDrawer(fNavigationDrawer);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (fNavigationDrawer != null) {
            fNavigationDrawer.setScroll();
            fNavigationDrawer.setPosition();
        }
    }

    protected List<DrawerItem> addDrawerItems() {
        List<DrawerItem> dataList = new ArrayList<DrawerItem>();
        dataList.add(new DrawerItem(Constants.Flash_MENU_HOME, (this instanceof HomeActivity) ? R.drawable.flash_home : R.drawable.flash_home));
        dataList.add(new DrawerItem(Constants.Flash_MENU_SALE, (this instanceof SaleTransactionActivity ? R.drawable.flash_sale_icon : R.drawable.flash_sale_icon)));
        dataList.add(new DrawerItem(Constants.Flash_MENU_SIGN_OUT, (this instanceof LoginActivity ? R.drawable.flash_app_logo : R.drawable.flash_app_logo)));
        return dataList;
    }

    public void dismissProgressDialog() {
        if (this.progressDialog != null) {
            this.progressDialog.dismiss();
        }
    }

    public void showProgressDialog(CharSequence message) {
        if (this.progressDialog == null) {
            this.progressDialog = new ProgressDialog(this);
            this.progressDialog.setIndeterminate(true);
            this.progressDialog.setCancelable(false);
            this.progressDialog.setCanceledOnTouchOutside(false);
        }
        this.progressDialog.setMessage(message);
        this.progressDialog.show();

    }

    public void initIntent(Context packageContext, Class<?> cls){
        Intent intent = new Intent(packageContext, cls);
        startActivity(intent);
    }

    public void openFolder(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/*");
        startActivityForResult(intent, 1234);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if(intent.getData().toString().endsWith(".jwt")){

            try{
                Uri uri = intent.getData();
                InputStream inputStream = null;
                try {
                    inputStream = getContentResolver().openInputStream(uri);
                } catch (IOException e) {
                    e.printStackTrace();
                    toaster("Unable To Find File");
                }
                final String deviceName = Settings.Global.getString(this.getContentResolver(), "device_name");
                final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                byte[] data = new byte[16384];

                while(true) {
                    try {
                        int nRead;
                        if (!((nRead = inputStream.read(data, 0, data.length)) != -1)) break;
                        buffer.write(data, 0, nRead);
                    } catch (IOException e) {
                        e.printStackTrace();
                        toaster("Unable To Read File");
                    }
                }

                new ZitiAsyncTask(mKeyStore, buffer.toByteArray(), deviceName).execute();

            }catch (ExpiredJwtException e){
                toaster("Expired Token");

            }catch (Exception e){
                toaster("Something went wrong....");
            }

        }else {
            toaster("Unrelated File Selected");
        }

        initIntent(BaseActivity.this, LoginActivity.class);

    }

    private class ZitiAsyncTask extends AsyncTask<Void, Void, String> {
        KeyStore keyStore;
        byte[] bytes;
        String device;

        ZitiAsyncTask(KeyStore keyStore,byte[] bytes,String device)throws Exception{
            this.keyStore= keyStore;
            this.bytes=bytes;
            this.device=device;
        }

        @Override
        protected String doInBackground(Void... voids) {
            String message = "success";
            try {
                Ziti.enroll(keyStore, bytes, device);

            }catch (ExpiredJwtException expireException){
                message= "Your Token is expired";
            }catch (Exception e){
                message = "Something went wrong..Please try later..";
            }

            return message;


        }

        @Override
        protected void onPostExecute(String  message) {
            super.onPostExecute(message);
            toaster(message);
        }
    }

    public void toaster(final String msg){
        new Handler(getMainLooper()).post(
                new Runnable() {
                    @Override
                    public void run() {
                        Toast toast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
                        toast.show();
                        finish();
                    }
                }
        );

    }
}
