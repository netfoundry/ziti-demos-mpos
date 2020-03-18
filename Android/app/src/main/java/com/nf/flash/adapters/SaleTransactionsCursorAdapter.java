package com.nf.flash.adapters;

import android.content.Context;
import android.database.Cursor;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CursorAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.nf.flash.R;
import com.nf.flash.database.DBOpenHelper;
import com.nf.flash.utils.Constants;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;


public class SaleTransactionsCursorAdapter extends CursorAdapter {
    public SaleTransactionsCursorAdapter(Context context, Cursor c) {
        super(context, c, true);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_p2m_sale_transactions, parent, false);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView date = view.findViewById(R.id.date);
        TextView time = view.findViewById(R.id.time);
        TextView phone = view.findViewById(R.id.recipient);
        TextView amount = view.findViewById(R.id.amount);
        TextView status = view.findViewById(R.id.status);
        RelativeLayout dateTimeLayout = view.findViewById(R.id.date_time_layout);

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        int x = (int) ((metrics.widthPixels) * .80);
        int y = (int) ((metrics.widthPixels) * .20);
        dateTimeLayout.getLayoutParams().width = x / 3;
        int px = Math.round(12 * (metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT));
        dateTimeLayout.setPadding(px, 0, 0, 0);
        phone.setWidth(x / 3);
        phone.setPadding(px, 0, 0, 0);
        amount.setWidth(y);
        status.setWidth(x / 3);

        long timeInMilliSec = cursor.getLong(cursor.getColumnIndex(DBOpenHelper.DATE_TIME));
        String[] dateTime = getDateTime(timeInMilliSec);
        if(dateTime != null) {
            date.setText(dateTime[0]);
            time.setText(dateTime[1]);
        }else{
            date.setText("--");
            time.setText("--");
        }


        String mobile_alias = cursor.getString(cursor.getColumnIndex(DBOpenHelper.MOBILE_OR_ALIAS));
        if (isNumber(mobile_alias)) {
            phone.setText("***-***-" + getLast4Characters(mobile_alias, 4));
        } else {
            phone.setText(mobile_alias);
        }

        amount.setText(cursor.getString(cursor.getColumnIndex(DBOpenHelper.AMOUNT)));
        String statusInDB = cursor.getString(cursor.getColumnIndex(DBOpenHelper.STATUS))==null?"Approved":cursor.getString(cursor.getColumnIndex(DBOpenHelper.STATUS));
    }

    private boolean isNumber(String value) {
        boolean isValidInteger;
        try {
            Double.parseDouble(value);
            isValidInteger = true;
        } catch (NumberFormatException ex) {
            isValidInteger = false;
        }
        return isValidInteger;
    }

    private String getLast4Characters(String inputString,
                                      int subStringLength) {
        int length = inputString.length();
        if (length <= subStringLength) {
            return inputString;
        }
        int startIndex = length - subStringLength;
        return inputString.substring(startIndex);
    }

    private String[] getDateTime(long fromDate) {
        try {
            String date = getUTCMillToDeviceTime(fromDate);
            String[] dateTime = {date.substring(0, 10), date.substring(11)};
            return dateTime;
        }catch (Exception e){
            Log.e("CursorAdapter ", e.getMessage());
        }
        return null;
    }

    private String getUTCMillToDeviceTime(long fromDate) {

        try {
            Date currDate = new Date(fromDate);
            String date;
            DateFormat dateFormat = new SimpleDateFormat(Constants.P2M_DATE_TIME_FORMAT);
            dateFormat.setTimeZone(TimeZone.getDefault());
            date = dateFormat.format(currDate);
            return date;
        }catch (Exception e){
            Log.e("getUTCMillToDeviceTime ", e.getMessage());
        }
        return "";
    }
}
