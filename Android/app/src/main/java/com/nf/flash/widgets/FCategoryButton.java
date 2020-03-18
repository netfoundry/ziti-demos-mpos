package com.nf.flash.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import com.nf.flash.R;


public class FCategoryButton extends RelativeLayout {

	public FCategoryButton(Context context) {
		super(context);
		init();
	}

	public FCategoryButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public FCategoryButton(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	private void init() {
		LayoutInflater mInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mInflater.inflate(R.layout.layout_homepage_category_button, this, true);
	}

}
