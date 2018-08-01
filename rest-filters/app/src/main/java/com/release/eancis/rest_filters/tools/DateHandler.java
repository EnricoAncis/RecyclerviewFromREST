package com.release.eancis.rest_filters.tools;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Enrico on 08/06/2018.
 */

public class DateHandler {
    private static final String TAG = DateHandler.class.getSimpleName();

    public static Date getPostDate(String dateString){
        Date resultDate = null;

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            resultDate = formatter.parse(dateString);
        }
        catch (ParseException e) {
            Log.e(TAG + " getPostDate", e.getMessage());
        }

        return resultDate;
    }

    public static String changeToEurFormat(String dateString){
        String newDate =" ";

        final String OLD_FORMAT = "yyyy-MM-dd HH:mm:ss";
        final String NEW_FORMAT = "dd-MM-yyyy HH:mm:ss";

/*        String oldDateString = "12/08/2010";
        String newDateString;*/

        SimpleDateFormat sdf = new SimpleDateFormat(OLD_FORMAT);
        Date date = null;
        try {
            date = sdf.parse(dateString);
        } catch (ParseException e) {
            Log.e(TAG + " changeToEurFormat", e.getMessage());
        }
        sdf.applyPattern(NEW_FORMAT);
        newDate = sdf.format(date);

        return newDate;
    }
}
