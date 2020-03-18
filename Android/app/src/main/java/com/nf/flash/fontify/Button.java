package com.nf.flash.fontify;

import android.content.Context;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatButton;

public class Button extends AppCompatButton {
	public Button(Context context) {
		super(context);
	}

	public Button(Context context, AttributeSet attrs) {
		super(context, attrs);
		if (isInEditMode()) return;
		FontManager.getInstance().setFont(this, attrs);
	}

}
