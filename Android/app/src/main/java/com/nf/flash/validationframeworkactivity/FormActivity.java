package com.nf.flash.validationframeworkactivity;

import com.nf.flash.activities.BaseActivity;

public abstract class FormActivity extends BaseActivity {

    protected abstract void initFields();
    protected abstract void initCallBacks();

    @Override
    public void setContentView(int viewId) {
        super.setContentView(viewId);
        initFields();
        initCallBacks();
    }

}
