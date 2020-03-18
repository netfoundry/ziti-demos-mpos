package com.nf.flash.formatters;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;
import com.nf.flash.utils.Constants;
import java.text.DecimalFormat;

public class NumberTextWatcher implements TextWatcher {

	private EditText et;
	private String current = "";
	private TextView tvPayAmount;
	private double amount;
	private static double tipAmount;

	DecimalFormat dec = new DecimalFormat(Constants.DEFAULT_AMOUNT);

	public NumberTextWatcher(EditText et)
	{
		this.et = et;
		this.et.setSelection(this.et.getText().length());
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count){

	}

	@Override
	public void afterTextChanged(Editable s) {
		currencyFormat(s);
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {

	}

	private void currencyFormat(Editable s) {
		String initString = s.toString();
		if(!initString.equals(current)){
			String cleanString = initString.replaceAll("[$,.]", "");
			if (cleanString.length() > 0) {
				Double in= Double.parseDouble(cleanString);
				double percen = in/100;
				String formated = dec.format(percen);
				current = formated;
				tipAmount = Double.parseDouble(formated);
				s.replace(0, s.length(), formated);
			}
		}
		if(tvPayAmount != null) {
			Double totalamount = amount + tipAmount;
			String formatedAmount = dec.format(totalamount);
			tvPayAmount.setText(Constants.Flash_CURRENCY +" " + formatedAmount);
		}
	}

}
