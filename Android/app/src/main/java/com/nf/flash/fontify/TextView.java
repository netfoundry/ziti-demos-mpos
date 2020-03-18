package com.nf.flash.fontify;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.text.Editable;
import android.text.Layout;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.text.TextWatcher;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatTextView;
import com.nf.flash.R;

public class TextView extends AppCompatTextView {
	
	private CharSequence originalText;
	private SpannableStringBuilder builder = new SpannableStringBuilder();
	private Ellipsize textCacheInvalidator;

	public enum EllipsizeRange {
	    ELLIPSIS_AT_START, ELLIPSIS_AT_END
    }
	
	public TextView(Context context) {
		super(context);
		if(isOSIceCreamSandwich()) {
			this.setMaxLines(2);
		}
	}

	public TextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		if (isInEditMode()) return;
		FontManager.getInstance().setFont(this, attrs);
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TextView);
		boolean isCustomEllipSize = a.getBoolean(R.styleable.TextView_customEllipsize, false);

		if(isCustomEllipSize && isOSIceCreamSandwich()) {
			this.setMaxLines(2);
		}else if (isCustomEllipSize) {
			this.setSingleLine(true);
		}
		a.recycle();
	}
	
	public TextView(Context context, AttributeSet attrs, int defStyle) {
	    super(context, attrs, defStyle);

	 		if (isInEditMode()) return;

	 		FontManager.getInstance().setFont(this, attrs);
	 		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TextView);

	 		boolean isCustomEllipSize = a.getBoolean(R.styleable.TextView_customEllipsize, false);

	 		if(isCustomEllipSize && isOSIceCreamSandwich()) {
	 			this.setMaxLines(2);
	 		}else if (isCustomEllipSize) {
				this.setSingleLine(true);
			}
	 		a.recycle();
	  }

	private boolean isOSIceCreamSandwich() {
        return Build.VERSION.SDK_INT == Build.VERSION_CODES.ICE_CREAM_SANDWICH
                || Build.VERSION.SDK_INT == Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1;
    }

	  public CharSequence getText() {
	    if (textCacheInvalidator == null || originalText == null) {
	      return super.getText();
	    }
	    return originalText;
	  }
	
	  @Override
	  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
	    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	    if(textCacheInvalidator == null)
	    	return;
	    Layout layout = getLayout();
	    CharSequence text = layout.getText();
	    if (text instanceof Spanned) {
	      Spanned spanned = (Spanned) text;
	      int ellipsisStart;
	      int ellipsisEnd;
	      TruncateAt where = null;
	      ellipsisStart = spanned.getSpanStart(EllipsizeRange.ELLIPSIS_AT_START);
	      if (ellipsisStart >= 0) {
	        where = TruncateAt.START;
	        ellipsisEnd = spanned.getSpanEnd(EllipsizeRange.ELLIPSIS_AT_START);
	      } else {
	        ellipsisStart = spanned.getSpanStart(EllipsizeRange.ELLIPSIS_AT_END);
	        if (ellipsisStart >= 0) {
	          where = TruncateAt.END;
	          ellipsisEnd = spanned.getSpanEnd(EllipsizeRange.ELLIPSIS_AT_END);
	        } else {
	          return;
	        }
	      }

	      builder.clear();
	      builder.append(text, 0, ellipsisStart).append(text, ellipsisEnd, text.length());
	      float consumed = Layout.getDesiredWidth(builder, layout.getPaint());
	      CharSequence ellipsisText = text.subSequence(ellipsisStart, ellipsisEnd);
	      CharSequence ellipsizedText = TextUtils.ellipsize(ellipsisText, layout.getPaint(),layout.getWidth() - consumed, where);
	      if (ellipsizedText.length() < ellipsisText.length()) {
	        builder.clear();
	        builder.append(text, 0, ellipsisStart).append(ellipsizedText)
	            .append(text, ellipsisEnd, text.length());
	        setText(builder);
	        originalText = text;
	        requestLayout();
	        invalidate();
	      }
	    }
	  }

	private final class Ellipsize implements TextWatcher {

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,int count) {
			originalText = null;
		}

		@Override
		public void afterTextChanged(Editable s) {
			
		}

	}

}
