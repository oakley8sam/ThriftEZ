package com.oakley8sam.thriftez.Helpers;

import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* DecimalLimitFilter
    An override of the basic Charsequence filter, allowing us to limit decimal textViews to
    two numbers after the decimal, as is normal with values representing money.
 */

public class DecimalLimitFilter implements InputFilter{
    Pattern pattern;

    //creates a pattern that limits the number to 2 places after the decimal
    public DecimalLimitFilter (int digitsAfterZero) {
        pattern = Pattern.compile("[0-9]+((\\.[0-9]{0," + (digitsAfterZero - 1) + "})?)||(\\.)?");
    }

    //override CharSequence filter with our custom filter to limit decimal places
    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest,
                               int dstart, int dend){
        Matcher matcher = pattern.matcher(TextUtils.concat(dest.subSequence(0,dstart),
                                          source.subSequence(start,end),
                                          dest.subSequence(dend,dest.length())));
        if(!matcher.matches())
            return "";
        return null;
    }
}
