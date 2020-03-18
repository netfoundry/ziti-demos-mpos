package com.nf.flash.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.nf.flash.R;


public class MerchantHeader extends RelativeLayout {

    private Context context;
    private RelativeLayout mainLayout;
    private TextView merchant_name,module_tv;

    public MerchantHeader(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public MerchantHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public MerchantHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    private void init() {
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mInflater.inflate(R.layout.merchant_header_layout, this, true);
        mainLayout = findViewById(R.id.main_layout);
        merchant_name = findViewById(R.id.merchantName_tv);
        module_tv = findViewById(R.id.module_tv);
    }

    public void setMerchantName(String name) {
        this.merchant_name.setText(name);
    }

    public void setTitle(String title){
        module_tv.setText(title);
    }

}
