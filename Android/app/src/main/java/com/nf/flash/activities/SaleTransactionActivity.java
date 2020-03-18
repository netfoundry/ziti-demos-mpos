package com.nf.flash.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.ScrollView;
import com.nf.flash.R;
import com.nf.flash.adapters.SaleTransactionsCursorAdapter;
import com.nf.flash.database.DatabaseAccess;
import com.nf.flash.fontify.EditText;
import com.nf.flash.formatters.NumberTextWatcher;
import com.nf.flash.models.SaleTransactionDetails;
import com.nf.flash.utils.Constants;
import com.nf.flash.fontify.TextView;
import com.nf.flash.fontify.Button;
import com.nf.flash.utils.SecurePreferenceUtils;
import com.nf.flash.utils.Utils;
import com.nf.flash.widgets.MerchantHeader;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.Random;
import java.util.TimeZone;

public class SaleTransactionActivity extends BaseActivity {
    private MerchantHeader merchantName;
    private EditText inputMobileNumber, inputAmount, inputPin;
    private TextView emptyDashBoard;
    private ListView dashBoardTransactionsListView;
    private TextView dashBoardDateLabel, dashBoardPhoneLabel, dashBoardAmountLabel, dashBoardStatusLabel;
    private ScrollView dashBoardScrollView;
    private Button submitTransaction;
    private Button cancelTransaction;
    private DatabaseAccess databaseAccess;
    private SaleTransactionsCursorAdapter dashBoardTransactionsCursorAdapter;
    private final String IS_ON_CREATE_CALLED = "isOnCreateCalled";
    private AlertDialog alertTransactionStatus;
    private AlertDialog errorAlertTransactionStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions);

        fNavigationDrawer.findListAndSetAdapter((ListView) findViewById(R.id.left_drawer), Constants.Flash_MENU_HOME, R.layout.custom_drawer_item, addDrawerItems());
        fNavigationDrawer.findListAndSetAdapter((ListView) findViewById(R.id.left_drawer), Constants.Flash_MENU_SIGN_OUT, R.layout.custom_drawer_item, addDrawerItems());
        dashBoardTransactionsListView = findViewById(R.id.transactionsDashBoardListView);

        SecurePreferenceUtils.setBooleanInPreferences(Constants.Flash_SETTINGS_SCREEN_PASSWORDD_DIALOG, false);
        merchantName = findViewById(R.id.loggedIn_merchant_layout);
        merchantName.setMerchantName(SecurePreferenceUtils.getStringFromPreferences(Constants.Flash_MERCHANT_NAME, ""));
        merchantName.setTitle(Constants.REQUEST_PAYMENT);

        inputMobileNumber = findViewById(R.id.input_mobile_number);
        inputMobileNumber.setEnabled(true);

        inputAmount = findViewById(R.id.input_amount);
        inputAmount.currencyFormatEditTextSettings(inputAmount);
        inputAmount.setEnabled(true);
        inputAmount.setHint(getResources().getString(R.string.p2m_amount_hint_text));

        inputPin = findViewById(R.id.input_pin);
        inputPin.setText("");
        inputPin.setVisibility(View.GONE);
        inputPin.setEnabled(true);

        emptyDashBoard = findViewById(R.id.empty_transaction_label);

        dashBoardTransactionsListView = findViewById(R.id.transactionsDashBoardListView);

        submitTransaction = findViewById(R.id.button_submit_transaction);
        cancelTransaction = findViewById(R.id.button_cancel_transaction);
        dashBoardScrollView = findViewById(R.id.scroll_view);

        dashBoardDateLabel = findViewById(R.id.dashboard_date_label);
        dashBoardPhoneLabel = findViewById(R.id.dashboard_recipient_label);
        dashBoardAmountLabel = findViewById(R.id.dashboard_amount_label);
        dashBoardStatusLabel = findViewById(R.id.dashboard_status_label);

        DisplayMetrics metrics = new DisplayMetrics();

        this.getWindowManager().getDefaultDisplay().getMetrics(metrics);

        int x = (int) ((metrics.widthPixels) * .80);
        int y = (int) ((metrics.widthPixels) * .20);

        dashBoardDateLabel.getLayoutParams().width = x / 3;

        int px = Math.round(12 * (metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT));

        dashBoardDateLabel.setPadding(px, 0, 0, 0);
        dashBoardPhoneLabel.setWidth(x / 3);
        dashBoardPhoneLabel.setPadding(px, 0, 0, 0);
        dashBoardAmountLabel.setWidth(y);
        dashBoardStatusLabel.setWidth(x / 3);

        submitTransaction.setEnabled(false);
        cancelTransaction.setEnabled(true);

        allowListViewScrollInScrollView();

        inputAmount.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View arg0, MotionEvent event) {
                if (inputAmount.getText().toString().trim().equalsIgnoreCase("") || inputAmount.getText().toString().trim().equalsIgnoreCase(Constants.DEFAULT_AMOUNT)) {
                    inputAmount.setText(Constants.DEFAULT_AMOUNT);
                    submitTransaction.setEnabled(true);
                    submitTransaction.setBackgroundResource(R.drawable.login_bg_enabled);
                    inputAmount.requestFocus();
                    inputAmount.addTextChangedListener(new NumberTextWatcher(inputAmount));
                }
                return false;
            }
        });

        submitTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inputMobileNumber.getText().toString().length()==10 && Double.parseDouble(inputAmount.getText().toString())>0.9) {
                    if(inputPin.getText().toString().length()>0){
                        if(inputPin.getText().toString().length()==4) saleTransaction();
                    }else {
                        initiateTransaction();
                    }
                }
            }
        });

        cancelTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoHome();
                inputMobileNumber.setEnabled(true);
                inputMobileNumber.setEnabled(true);
            }
        });

        databaseAccess = DatabaseAccess.getInstance(getApplicationContext());
        updateCursorAdapterData();

        if (savedInstanceState != null) {
            boolean isFirstTimeApiCalled = savedInstanceState.getBoolean(IS_ON_CREATE_CALLED);
            if(!SecurePreferenceUtils.getStringFromPreferences("message","").equals("")){
                showSaleDialog(SecurePreferenceUtils.getStringFromPreferences("message",""));
            }
            if(!SecurePreferenceUtils.getStringFromPreferences("errorMessage","").equals("")){
                showErrorDialog(SecurePreferenceUtils.getStringFromPreferences("errorMessage",""));
            }
            if (isFirstTimeApiCalled) {
                setAdapterForListView(databaseAccess.saleTransactions());
            } else {
                invokeSaleResponseApiFirstTime();
            }
        } else {
            invokeSaleResponseApiFirstTime();
        }
    }

    private void invokeSaleResponseApiFirstTime() {
        if (databaseAccess.getPendingTransactionIDs().getCount() > 0) ;
        else setAdapterForListView(databaseAccess.saleTransactions());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(IS_ON_CREATE_CALLED, true);
        outState.putBoolean("isfNavDrawerOpen", fNavigationDrawer.isNavigationDrawerOpen());
    }

    private  void gotoHome(){
        Intent intent = new Intent(SaleTransactionActivity.this, HomeActivity.class);
        startActivity(intent);
    }

    private  void initiateTransaction(){

        JSONObject otpRequest = new JSONObject();
        JSONObject purchaseInfo = new JSONObject();

        try {
            purchaseInfo.put("merchantId",SecurePreferenceUtils.getStringFromPreferences(Constants.Flash_MERCHANT_ID,"enlume1"));
            otpRequest.put("mobile", inputMobileNumber.getText().toString());
            otpRequest.put("amount", inputAmount.getText().toString());
            otpRequest.put("purchaseInfo",purchaseInfo);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try{
            invokeTransactionAPI(Constants.OTP_REQUEST_URL,otpRequest.toString(),"");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private  void saleTransaction(){

        JSONObject submitOTPRequest = new JSONObject();
        JSONObject purchaseInfo = new JSONObject();

        try {
            purchaseInfo.put("merchantId",SecurePreferenceUtils.getStringFromPreferences(Constants.Flash_MERCHANT_ID,"enlume1"));
            submitOTPRequest.put("mobile", inputMobileNumber.getText().toString());
            submitOTPRequest.put("otp", inputPin.getText().toString());
            submitOTPRequest.put("purchaseInfo",purchaseInfo);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try{
            invokeTransactionAPI(Constants.OTP_SUBMIT_URL,submitOTPRequest.toString(),inputMobileNumber.getText().toString());
            dismissProgressDialog();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    protected void invokeTransactionAPI(String reqURL, String reqData, String mob) throws Exception {
        new RequestAsyncTask(reqURL, reqData,mob).execute();
    }

    class RequestAsyncTask extends AsyncTask<String, String, String> {

        private String url;
        private String data;
        private  String mbl;
        private  String transactionID;

        RequestAsyncTask(String url, String data,String mob) throws Exception{
            this.url = url;
            this.data = data;
            this.mbl=mob;
        }

        protected void onPostExecute(String response) {
             try{
                JSONObject jsonObject = new JSONObject(response);

                JSONObject receipt = new JSONObject();
                receipt.put("receipt",jsonObject.getJSONObject("receipt"));

                JSONObject transAmount = new JSONObject();
                transAmount.put("transactionAmount",receipt.getJSONObject("receipt").getJSONObject("transactionAmount"));

                try {
                    transactionID = receipt.getJSONObject("receipt").getString("transactionId");
                }catch (Exception e){
                    transactionID=String.format("%06d",new Random().nextInt(999999999));
                }

                SecurePreferenceUtils.setStringInPreferences(Constants.TRANSACTION_ID, transactionID);

                if(jsonObject.getString("message").startsWith("Transaction Successful")){

                    SaleTransactionDetails p2MInitiationDetails = new SaleTransactionDetails();
                    p2MInitiationDetails.setSaleTransactionId(SecurePreferenceUtils.getStringFromPreferences(Constants.TRANSACTION_ID,"1000000000"));

                    Calendar utcCalendar = Calendar.getInstance(TimeZone.getTimeZone(Constants.Flash_UTC_TIME_ZONE));

                    p2MInitiationDetails.setSaleDateTime(utcCalendar.getTimeInMillis());

                    SecurePreferenceUtils.setStringInPreferences(Constants.Flash_MOB,this.mbl);

                    p2MInitiationDetails.setSaleMobileAlias(SecurePreferenceUtils.getStringFromPreferences(Constants.Flash_MOB,"1000000000"));
                    p2MInitiationDetails.setAmount(transAmount.getJSONObject("transactionAmount").getString("amount"));
                    p2MInitiationDetails.setStatus(Constants.P2M_TRANSACTION_APPROVED);

                    databaseAccess.insertTransactionDetails(p2MInitiationDetails);
                    SecurePreferenceUtils.setStringInPreferences("message","SUCCESS");

                    inputMobileNumber.setEnabled(true);
                    inputMobileNumber.setText("");

                    inputAmount.setEnabled(true);
                    inputAmount.setText("");

                    inputPin.setVisibility(View.GONE);
                    inputPin.setText("");
                    inputPin.setEnabled(false);

                    submitTransaction.setEnabled(false);

                }else if(jsonObject.getString("message").startsWith("Payment request initiated")){
                    inputMobileNumber.setEnabled(false);
                    inputAmount.setEnabled(false);
                    inputPin.setVisibility(View.VISIBLE);
                    inputPin.setText("");
                    inputPin.setEnabled(true);
                    inputPin.requestFocus();
                }
                showSaleDialog(jsonObject.getString("message"));

             }catch (Exception e){
                    showSaleDialog("Server Busy, Try Again Later...!");
                    e.printStackTrace();
             }
        }

        @Override
        protected String doInBackground(String... voids)throws NullPointerException {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String jsonResponse = "";

            try {
                URL url = new URL(this.url);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty("Accept", "application/json");

                Writer writer = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream(), StandardCharsets.UTF_8));
                writer.write(this.data);
                writer.close();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();

                if (inputStream == null) {
                    return null;
                }

                reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));

                String inputLine;

                while ((inputLine = reader.readLine()) != null) {
                    buffer.append(inputLine + "\n");
                }

                if (buffer.length() == 0) {
                    throw new NullPointerException();
                } else {
                    jsonResponse = buffer.toString();
                    return jsonResponse;
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }
    }

    private void allowListViewScrollInScrollView() {
        dashBoardScrollView.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                dashBoardTransactionsListView.getParent().requestDisallowInterceptTouchEvent(false);
                return false;
            }
        });
        dashBoardTransactionsListView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setLabelsWidth();
    }

    private void setLabelsWidth() {
        TextView date = findViewById(R.id.dashboard_date_label);
        TextView phone = findViewById(R.id.dashboard_recipient_label);
        TextView amountLabel = findViewById(R.id.dashboard_amount_label);
        TextView status = findViewById(R.id.dashboard_status_label);
        DisplayMetrics metrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int x = (int) ((metrics.widthPixels) * .80);
        int y = (int) ((metrics.widthPixels) * .20);
        date.getLayoutParams().width = x / 3;
        int px = Math.round(12 * (metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT));
        date.setPadding(px, 0, 0, 0);
        phone.setWidth(x / 3);
        phone.setPadding(px, 0, 0, 0);
        amountLabel.setWidth(y);
        status.setWidth(x / 3);
    }

    private void setAdapterForListView(Cursor cursor) {
        if (cursor != null && cursor.getCount() > 0) {
            setListViewVisibility(true);
            dashBoardTransactionsCursorAdapter = new SaleTransactionsCursorAdapter(SaleTransactionActivity.this, cursor);
            dashBoardTransactionsListView.setAdapter(dashBoardTransactionsCursorAdapter);
        } else {
            setListViewVisibility(false);
        }
    }

    @Override
    protected void onDestroy() {
        databaseAccess.closeDB();
        super.onDestroy();
    }

    private void updateCursorAdapterData() {
        showProgressDialog(Constants.PLEASE_WAIT);
        Cursor cursor = databaseAccess.saleTransactions();
        if (cursor != null && cursor.getCount() > 0) {
            setListViewVisibility(true);
            if (dashBoardTransactionsCursorAdapter == null) {
                dashBoardTransactionsCursorAdapter = new SaleTransactionsCursorAdapter(SaleTransactionActivity.this, cursor);
                dashBoardTransactionsListView.setAdapter(dashBoardTransactionsCursorAdapter);
            } else {
                dashBoardTransactionsCursorAdapter.changeCursor(cursor);
                dashBoardTransactionsCursorAdapter.notifyDataSetChanged();
            }
        } else {
            setListViewVisibility(false);
        }
        dismissProgressDialog();
    }

    public void showSaleDialog(String message) {
        dismissProgressDialog();
        alertTransactionStatus = new AlertDialog.Builder(SaleTransactionActivity.this).create();
        alertTransactionStatus.setMessage(message);
        alertTransactionStatus.setCancelable(false);
        alertTransactionStatus.setButton(AlertDialog.BUTTON_POSITIVE, Constants.OK, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (alertTransactionStatus.isShowing()) {
                    alertTransactionStatus.dismiss();
                    SecurePreferenceUtils.setStringInPreferences("message","");
                     updateCursorAdapterData();
                } else {
                    return;
                }
            }
        });
        alertTransactionStatus.show();
    }

    private void setListViewVisibility(boolean isDataAvailable) {
        if (isDataAvailable) {
            dashBoardTransactionsListView.setVisibility(View.VISIBLE);
            emptyDashBoard.setVisibility(View.GONE);
        } else {
            dashBoardTransactionsListView.setVisibility(View.GONE);
            emptyDashBoard.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(alertTransactionStatus!=null){
            alertTransactionStatus.dismiss();
        }
        if(errorAlertTransactionStatus !=null){
            errorAlertTransactionStatus.dismiss();
        }
    }

    public void showErrorDialog(String message) {
        errorAlertTransactionStatus = new AlertDialog.Builder(this).create();
        errorAlertTransactionStatus.setMessage(message);
        errorAlertTransactionStatus.setCancelable(false);
        errorAlertTransactionStatus.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (Utils.isSessionTimeOut()) {
                    return;
                }
                if (errorAlertTransactionStatus.isShowing()) {
                    SecurePreferenceUtils.setStringInPreferences("errorMessage","");
                }
                errorAlertTransactionStatus.dismiss();
            }
        });
        errorAlertTransactionStatus.show();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if(savedInstanceState != null){
            boolean isDrawerOpen = savedInstanceState.getBoolean("isqNavDrawerOpen");
            if(isDrawerOpen){
                setNavigationDrawerOpen();
            }
        }
    }

    private void setNavigationDrawerOpen(){
        if(fNavigationDrawer == null)
            throw new NullPointerException("FNavigationDrawer cannot be null, " +
                    "call FActionBar.setFNavigationDrawer() to set FNavigationDrawer.");
        fNavigationDrawer.scroll();
    }
}