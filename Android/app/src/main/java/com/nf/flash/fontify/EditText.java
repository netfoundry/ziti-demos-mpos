package com.nf.flash.fontify;

import android.content.Context;
import android.os.Build;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.method.DigitsKeyListener;
import android.text.method.KeyListener;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatEditText;
import java.util.regex.Pattern;

public class EditText extends AppCompatEditText {
	public EditText(Context context) {
		super(context);
	}

	public EditText(Context context, AttributeSet attrs) {
		super(context, attrs);

		if (isInEditMode()) return;

		FontManager.getInstance().setFont(this, attrs);
	}

	@Override
	public void setInputType(int type) {
			super.setInputType(type);
	}

	public void currencyFormatEditTextSettings(android.widget.EditText etAmount) {
		InputFilter filer = new InputFilter() {
			@Override
			public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
				Pattern p = Pattern.compile("([0-9]*[.]{1})?[0-9]+");
				if(p.matcher(source).matches()) {
					return source;
				}
				return "";
			}
		};

		KeyListener keyListener = DigitsKeyListener.getInstance("0123456789.");
		etAmount.setKeyListener(keyListener);
		etAmount.setFilters(new InputFilter[]{filer,new InputFilter.LengthFilter(10)});
	}



}
