package cs446.uwaterloo.pocketchef.model.util;

import android.annotation.SuppressLint;

import androidx.room.TypeConverter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Converters {
    private static ThreadLocal<SimpleDateFormat> dateFormat =
            new ThreadLocal<SimpleDateFormat>() {
                @Override
                protected SimpleDateFormat initialValue() {
                    @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                    dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                    return dateFormat;
                }
            };

    @TypeConverter
    // date is ISO time string in UTC time zone
    public static Date fromTimeStamp(String date) {
        try {
            return date == null ? null : dateFormat.get().parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    @TypeConverter
    // converted to ISO time string in UTC time zone
    public static String dateToTimestamp(Date date) {
        // getTime returns millis, we want seconds
        return date == null ? null : dateFormat.get().format(date);
    }
}