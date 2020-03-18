package com.nf.flash.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import com.nf.flash.models.SaleTransactionDetails;
import com.nf.flash.utils.Constants;
import com.nf.flash.utils.SecurePreferenceUtils;
import java.util.Date;

public class DatabaseAccess {
    private DBOpenHelper openHelper;
    private SQLiteDatabase database;
    private static DatabaseAccess instance;
    private static final int DB_INSERT_FAIL = -1;

    private DatabaseAccess(Context context) {
        this.openHelper = new DBOpenHelper(context);
    }

    public static DatabaseAccess getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseAccess(context);
        }
        return instance;
    }

    public void getWritableDBOpen() {
        database = openHelper.getWritableDatabase();
    }

    public void getReadableDBOpen() {
        database = openHelper.getReadableDatabase();
    }

    public Cursor saleTransactions() {
         getReadableDBOpen();
        String whereClause = DBOpenHelper.MERCHANT_ID + "=? AND "+DBOpenHelper.TERMINAL_ID + " =? AND "+DBOpenHelper.FI_ID +"=?";
        String[] whereArgs = {SecurePreferenceUtils.getStringFromPreferences(Constants.Flash_MERCHANT_ID,""),SecurePreferenceUtils.getStringFromPreferences(Constants.Flash_TERMINAL_ID,""),SecurePreferenceUtils.getStringFromPreferences(Constants.Flash_FI_ID,"") };
        String[] columns = {BaseColumns._ID, DBOpenHelper.TRANSACTION_ID, DBOpenHelper.DATE_TIME, DBOpenHelper.MOBILE_OR_ALIAS, DBOpenHelper.AMOUNT, DBOpenHelper.STATUS, DBOpenHelper.MESSAGE};
        Cursor cursor = database.query(DBOpenHelper.TABLE_NAME, columns, whereClause, whereArgs, null, null, DBOpenHelper.DATE_TIME + " DESC");
        return cursor;
    }

    public void deleteTableData() {
        getWritableDBOpen();
        if (!isTableExists())
            return;
        database.delete(DBOpenHelper.TABLE_NAME, null, null);
        if (database.isOpen()) {
            database.close();
        }
    }

    public boolean isTableExists() {
        getReadableDBOpen();
        Cursor cursor = database.query(DBOpenHelper.TABLE_NAME, null, null, null, null, null, null);
        if (cursor != null) {
            if (cursor.getCount() >= 0) {
                cursor.close();
                return true;
            }
            cursor.close();
        }
        return false;
    }

    public void insertTransactionDetails(final SaleTransactionDetails p2MInitiationDetails) {

        getWritableDBOpen();
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.TRANSACTION_ID, p2MInitiationDetails.getSaleTransactionId());
        values.put(DBOpenHelper.DATE_TIME, p2MInitiationDetails.getSaleDateTime());
        values.put(DBOpenHelper.MOBILE_OR_ALIAS, p2MInitiationDetails.getSaleMobileAlias());
        values.put(DBOpenHelper.AMOUNT, p2MInitiationDetails.getAmount());
        values.put(DBOpenHelper.STATUS, p2MInitiationDetails.getStatus());
        values.put(DBOpenHelper.MERCHANT_ID, SecurePreferenceUtils.getStringFromPreferences(Constants.Flash_MERCHANT_ID,""));
        values.put(DBOpenHelper.TERMINAL_ID, SecurePreferenceUtils.getStringFromPreferences(Constants.Flash_TERMINAL_ID,""));
        values.put(DBOpenHelper.FI_ID, SecurePreferenceUtils.getStringFromPreferences(Constants.Flash_FI_ID,""));

        long rowsEffected = database.insertOrThrow(DBOpenHelper.TABLE_NAME, null, values);
        if (rowsEffected == DB_INSERT_FAIL) {
            return;
        } else if (rowsEffected > 0) {
        }
        if (database.isOpen()) {
            database.close();
        }
    }

    public Cursor getPendingTransactionIDs() {
        getReadableDBOpen();
        String whereClause = DBOpenHelper.STATUS + "=? AND "+DBOpenHelper.MERCHANT_ID + "=? AND "+DBOpenHelper.TERMINAL_ID + " =? AND "+DBOpenHelper.FI_ID +" =?";
        String[] whereArgs = {Constants.P2M_TRANSACTION_PENDING,SecurePreferenceUtils.getStringFromPreferences(Constants.Flash_MERCHANT_ID,""),SecurePreferenceUtils.getStringFromPreferences(Constants.Flash_TERMINAL_ID,""),SecurePreferenceUtils.getStringFromPreferences(Constants.Flash_FI_ID,"") };
        String[] columns = {DBOpenHelper.TRANSACTION_ID};
        Cursor cursor = database.query(DBOpenHelper.TABLE_NAME, columns, whereClause, whereArgs, null, null, DBOpenHelper.DATE_TIME + " DESC");
        return cursor;
    }

    public void closeDB() {
        openHelper.close();
    }

}
