package com.nf.flash.fontify;

import android.content.Context;
import android.util.AttributeSet;


public class ExtractEditText extends android.inputmethodservice.ExtractEditText {
	public ExtractEditText(Context context) {
		super(context);
	}

	public ExtractEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		if (isInEditMode()) return;
		FontManager.getInstance().setFont(this, attrs);
	}
}
