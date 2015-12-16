package com.example.util;

import java.util.Calendar;

/**
 * Created by bbong on 2015-11-21.
 */
public class CurrentTime
{
    public static String getCurrentTime()
    {
        Calendar calendar = Calendar.getInstance();
        String hour = ""+calendar.get(Calendar.HOUR_OF_DAY);
        if(hour.length()==1)
        {
            hour="0"+hour;
        }
        String min = ""+calendar.get(Calendar.MINUTE);
        if(min.length()==1)
        {
            min="0"+min;
        }
        String sec=""+calendar.get(Calendar.SECOND);
        if(sec.length()==1)
        {
            sec="0"+sec;
        }

        String time = hour+min+sec;
        return time;
    }
}
