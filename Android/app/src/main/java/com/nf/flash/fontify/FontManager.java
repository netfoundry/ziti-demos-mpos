package com.nf.flash.fontify;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;
import com.nf.flash.R;
import java.util.HashMap;
import java.util.Map;

public class FontManager {
	private static FontManager fontManager;
	private Map<String, Typeface> mCache;

	private FontManager() {
		mCache = new HashMap<String, Typeface>();
	}

	public static FontManager getInstance() {
		if (fontManager == null) {
			fontManager = new FontManager();
		}
		return fontManager;
	}

	public void setFont(TextView tv, AttributeSet attrs) {
		String fontName = getFontName(tv.getContext(), attrs);
		int fontStyle = getFontStyle(tv.getContext(), attrs);
		setFont(tv, fontName, fontStyle);
	}

	public void setFont(TextView tv, String fontName) {
		if (fontName == null) return;
		Typeface typeface = mCache.get(fontName);
		if (typeface == null) {
			typeface = Typeface.createFromAsset(tv.getContext().getAssets(), fontName);
			mCache.put(fontName, typeface);
		}
		tv.setTypeface(typeface);
		
	}

	public void setFont(TextView tv, String fontName , int fontStyle) {
		if (fontName == null) return;
		Typeface typeface = mCache.get(fontName);
		if (typeface == null) {
			typeface = Typeface.createFromAsset(tv.getContext().getAssets(), fontName);
			mCache.put(fontName, typeface);
		}
		tv.setTypeface(typeface,fontStyle);
		
	}

	public static String getFontName(Context c, AttributeSet attrs) {
		TypedArray arr = c.obtainStyledAttributes(attrs, R.styleable.Fontify);
		String fontName = arr.getString(R.styleable.Fontify_font_name);
		arr.recycle();
		return fontName;
	}

	public static int getFontStyle(Context c, AttributeSet attrs){
		TypedArray arr = c.obtainStyledAttributes(attrs, R.styleable.Fontify);
		String fontStyle = arr.getString(R.styleable.Fontify_style);
		arr.recycle();
		return ((fontStyle!=null) ? Integer.parseInt(fontStyle) :  Typeface.NORMAL);
	}
}
