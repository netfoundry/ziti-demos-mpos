package com.nf.flash.database;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.text.TextUtils;
import android.util.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class DBOpenHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "flash.db";
    public static final String TABLE_NAME = "p2m_sale_transactions";
    public static final String TRANSACTION_ID = "transaction_id";
    public static final String DATE_TIME = "date_time";
    public static final String MOBILE_OR_ALIAS = "mobile_or_alias";
    public static final String AMOUNT = "amount";
    public static final String STATUS = "status";
    public static final String MESSAGE = "message";
    public static final String MERCHANT_ID = "merchant_id";
    public static final String TERMINAL_ID = "terminal_id";
    public static final String FI_ID = "fi_id";
    public static final int DATABASE_VERSION = 1;

    private final Context context;
    private String TAG = DBOpenHelper.class.getSimpleName();
    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME
            + " (" + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TRANSACTION_ID + " TEXT, "
            + DATE_TIME + " TEXT, " + MOBILE_OR_ALIAS + " TEXT, " + AMOUNT + " TEXT, " + STATUS + " TEXT, " + MESSAGE + " TEXT," + MERCHANT_ID + " TEXT, "+ TERMINAL_ID +" TEXT, "+FI_ID +" TEXT)";

    public DBOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
