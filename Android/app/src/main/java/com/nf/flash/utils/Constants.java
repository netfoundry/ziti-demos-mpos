package com.nf.flash.utils;

import com.nf.flash.BuildConfig;

public class Constants {

    public static final String Flash_MERCHANT_USER_NAME ="userName";
    public static final String Flash_REMEMBER_ME = "rememberMe";
    public static final String Flash_CANCEL ="Cancel";
    public static final String Flash_MERCHANT_NAME = "Indian Merchant";
    public static String Flash_FI_ID ="fiId";
    public static String Flash_TERMINAL_ID ="terminalId";
    public static final String Flash_UTC_TIME_ZONE = "America/Los_Angeles";
    public static  final String Flash_MERCHANT_ID ="merchantId";
    public static  final String Flash_MOB ="74859635";
    public static final String Flash_COUNTRY_CODE = "countryCode";
    public static final String Flash_CURRENCY = "currency";
    public static final String MERCHANT_USER_ID_AUTO_LOGIN = "merchantUserIdForAutoLogin";
    public static final String Flash_SETTINGS_SCREEN_PASSWORDD_DIALOG = "isSettingPasswordClose";
    public static final String Flash_Transaction_APPROVED = "Approved";
    public static final String P2M_TRANSACTION_PENDING = "P2M_TRANSACTION_PENDING";
    public static final String P2M_TRANSACTION_APPROVED = "SUCCESS";
    public static final String OK = "OK";
    public static final String P2M_DATE_TIME_FORMAT = "dd/MM/yyyy HH:mm:ss";
    public static final long   HOURS = 1 * 60 * 60 * 1000;//1 hour
    public static final String TRANSACTION_ID = "0000000";
    public static final String Flash_MENU_HOME ="Home";
    public static final String Flash_MENU_SALE = "Sale";
    public static final String Flash_MENU_SIGN_OUT ="Sign Out";
    public static final String REQUEST_PAYMENT = "Request Payment";
    public static final String DEFAULT_AMOUNT = "0.00";
    public static final String TIMEOUT = "com.nf.mobilewallet.timeout";
    public static final String IS_LOGOUT = "isLogOut";
    public static final String PLEASE_WAIT = "Please wait...";
    public static String APP_VERSION = BuildConfig.VERSION_NAME + " QA";
    public static final String BASE_URL = "http://mposservice:3000/";
    public static String LOGIN_URL = BASE_URL+"login";
    public static String OTP_REQUEST_URL = BASE_URL+"salep2m";
    public static String OTP_SUBMIT_URL = BASE_URL+"salep2motpresponse";

}
