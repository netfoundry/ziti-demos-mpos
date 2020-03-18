package com.nf.flash.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ToggleButton;
import com.nf.flash.R;
import com.nf.flash.database.DatabaseAccess;
import com.nf.flash.fontify.EditText;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import com.nf.flash.fontify.TextView;
import com.nf.flash.utils.Constants;
import com.nf.flash.utils.SecurePreferenceUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.List;

import io.netfoundry.ziti.Ziti;
import io.netfoundry.ziti.ZitiContext;


public class LoginActivity extends BaseActivity {

    private static final String TAG = "LoginActivity";
    private EditText inputUserName;
    private EditText inputPassword;
    private Button submit_credentials_btn;
    private final String qq = "merchant_last_time";
    private TextView helpTv;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

            initZiti();

    }

    private void showAlert(){
        AlertDialog.Builder builder = new AlertDialog.Builder(
                LoginActivity.this);
        builder.setTitle("Your device is not Enrolled");
        builder.setMessage("Please enroll your device to use the services");
        builder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        openFolder();

                    }
                });
        builder.setCancelable(false);
        builder.show();
    }

    public void initZiti(){
        try {
            mKeyStore = KeyStore.getInstance("AndroidKeyStore");
            mKeyStore.load(null);

            List<ZitiContext> ziti=null;
            try{
                ziti =  Ziti.init(mKeyStore, true);

                if (ziti.isEmpty()){
                    showAlert();
                }else{
                    loginActivityInit();
                }

            }catch (ExceptionInInitializerError i){
                i.printStackTrace();
                toaster("Ziti Not Initialized");

            }

        } catch (KeyStoreException | CertificateException | IOException | NoSuchAlgorithmException e ) {
            e.printStackTrace();
            toaster("Key Store Exception");

        }catch (Exception e) {
            e.printStackTrace();
            toaster("Unknown Problem");

        }

    }

    public void loginActivityInit(){
        try{

            setContentView(R.layout.activity_login);

            inputUserName = findViewById(R.id.input_username);
            inputPassword = findViewById(R.id.input_password);

            SecurePreferenceUtils.setStringInPreferences(Constants.Flash_MERCHANT_USER_NAME,inputUserName.getText().toString().trim());

            reSetEnvironmentValues();

            submit_credentials_btn = findViewById(R.id.user_login_btn);
            submit_credentials_btn.setEnabled(true);
            submit_credentials_btn.callOnClick();


            submit_credentials_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (inputUserName.getText().toString().trim().isEmpty())inputUserName.requestFocus();
                    else if (inputPassword.getText().toString().trim().isEmpty())inputPassword.requestFocus();
                    else {
                        try {
                            JSONObject loginRequest = new JSONObject();
                            loginRequest.put("userId", inputUserName.getText().toString().trim());
                            loginRequest.put("password",inputPassword.getText().toString().trim());

                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                            invokeLoginAPI(Constants.LOGIN_URL,loginRequest.toString(),intent);

                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }
            });


            helpTv = findViewById(R.id.layout_help);

            helpTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent intent = new Intent(LoginActivity.this, HelpActivity.class);
                        if (intent == null) return;
                        startActivity(intent);
                        LoginActivity.this.overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
            });

            SecurePreferenceUtils.setStringInPreferences(Constants.Flash_MERCHANT_USER_NAME,inputUserName.getText().toString().trim());

        }catch (Exception e){
            e.printStackTrace();
            toaster("Login Error");
        }
    }

    protected void invokeLoginAPI(String reqURL, String reqData, Intent intent) throws Exception {
        new RequestAsyncTask(reqURL, reqData, intent).execute();
    }

    class RequestAsyncTask extends AsyncTask<String, String, String> {
        private String url;
        private String data;
        Intent intent;
        RequestAsyncTask(String url, String data, Intent intnt) throws Exception{
            this.url = url;
            this.data = data;
            this.intent = intnt;
        }

        protected void onPostExecute(String param) {
            try {
                JSONObject jsonObject = new JSONObject(param);
                JSONObject merchantInfoObject = new JSONObject();
                merchantInfoObject.put("merchantInfo",jsonObject.getJSONObject("merchantInfo"));

                JSONArray terminalList=new JSONArray(jsonObject.getString("terminalList"));
                JSONObject terminalObject = new JSONObject();
                terminalObject.put("terminal",terminalList.get(0));

                SecurePreferenceUtils.setStringInPreferences(Constants.Flash_MERCHANT_NAME, merchantInfoObject.getJSONObject("merchantInfo").getString("entityName"));
                SecurePreferenceUtils.setStringInPreferences(Constants.Flash_FI_ID, merchantInfoObject.getJSONObject("merchantInfo").getString("fiId"));
                SecurePreferenceUtils.setStringInPreferences(Constants.Flash_MERCHANT_ID, terminalObject.getJSONObject("terminal").getString("userId"));
                SecurePreferenceUtils.setStringInPreferences(Constants.Flash_COUNTRY_CODE,merchantInfoObject.getJSONObject("merchantInfo").getString("cntryPhCode"));
                SecurePreferenceUtils.setStringInPreferences(Constants.Flash_CURRENCY, merchantInfoObject.getJSONObject("merchantInfo").getString("currency"));
                SecurePreferenceUtils.setStringInPreferences(Constants.Flash_TERMINAL_ID, terminalObject.getJSONObject("terminal").getString("terminalId"));
                SecurePreferenceUtils.setStringInPreferences(qq, inputPassword.getText().toString());
                SecurePreferenceUtils.setStringInPreferences(Constants.MERCHANT_USER_ID_AUTO_LOGIN, inputUserName.getText().toString());
                SecurePreferenceUtils.setBooleanInPreferences(Constants.IS_LOGOUT, false);

                initIntent(LoginActivity.this, HomeActivity.class);
                LoginActivity.this.overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);

            } catch (Exception e) {
                e.printStackTrace();
                toaster("Login Error");
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
                    if (this.intent != null) startActivity(this.intent);
                    return jsonResponse;
                }
            } catch (Exception e) {
                e.printStackTrace();
                toaster("Login Error");
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

    private void reSetEnvironmentValues() {
        int previousSelection = SecurePreferenceUtils.getIntegerFromPreferences("previousSelection", -2);
        int currentSelection = SecurePreferenceUtils.getIntegerFromPreferences("selectedEnvironment", -1);

        if (previousSelection != currentSelection) {
            inputUserName.setText("");
            inputPassword.setText("");
            inputUserName.requestFocus();
            DatabaseAccess databaseAccess = DatabaseAccess.getInstance(LoginActivity.this);
            databaseAccess.deleteTableData();
        }
    }

}
