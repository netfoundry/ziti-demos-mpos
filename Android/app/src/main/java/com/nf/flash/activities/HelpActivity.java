package com.nf.flash.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import com.nf.flash.fontify.TextView;
import com.nf.flash.R;
import com.nf.flash.utils.Constants;

public class HelpActivity extends BaseActivity {

    private TextView versionTv;
    private LinearLayout backLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        versionTv = findViewById(R.id.version_tv);
        versionTv.setText(Constants.APP_VERSION);
        backLayout = findViewById(R.id.left_navigation_layout);
        backLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
    }
}
