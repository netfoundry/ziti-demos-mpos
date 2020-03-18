package com.nf.flash.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import com.nf.flash.R;
import com.nf.flash.utils.Constants;
import com.nf.flash.widgets.FCategoryButton;

public class HomeActivity extends BaseActivity {

    private TextView flashMerchantName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        fNavigationDrawer.findListAndSetAdapter((ListView) findViewById(R.id.left_drawer), Constants.Flash_MENU_SALE, R.layout.custom_drawer_item, addDrawerItems());
        fNavigationDrawer.findListAndSetAdapter((ListView) findViewById(R.id.left_drawer), Constants.Flash_MENU_SIGN_OUT, R.layout.custom_drawer_item, addDrawerItems());
        FCategoryButton sale = findViewById(R.id.sale_link_btn);
        sale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initSaleTransaction();
            }
        });
    }

    private void initSaleTransaction() {
        flashMerchantName = findViewById(R.id.merchant_name_tv);
        flashMerchantName.setText(Constants.Flash_MERCHANT_NAME);
        Intent intent = new Intent(HomeActivity.this, SaleTransactionActivity.class);
        if (intent == null) return;
        startActivity(intent);
        this.overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("isqNavDrawerOpen", fNavigationDrawer.isNavigationDrawerOpen());
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
                    "call FActionBar.setQNavigationDrawer() to set FNavigationDrawer.");
        fNavigationDrawer.scroll();
    }

}