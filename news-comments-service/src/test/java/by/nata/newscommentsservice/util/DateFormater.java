package by.nata.newscommentsservice.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormater {
    public static String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }
}
