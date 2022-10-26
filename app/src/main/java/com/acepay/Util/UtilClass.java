package com.acepay.Util;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Currency;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Created by pro22 on 1/12/17.
 */

public class UtilClass {
    public static SortedMap<Currency, Locale> currencyLocaleMap;

    static {
        currencyLocaleMap = new TreeMap<Currency, Locale>(new Comparator<Currency>() {
            public int compare(Currency c1, Currency c2) {
                return c1.getCurrencyCode().compareTo(c2.getCurrencyCode());
            }
        });
        for (Locale locale : Locale.getAvailableLocales()) {
            try {
                Currency currency = Currency.getInstance(locale);
                currencyLocaleMap.put(currency, locale);
            } catch (Exception e) {
            }
        }
    }

    public static String compareDates(String date1, String date2) {

        String pattern = "dd-MM-yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        String result="";

        try {
            Date date11 = sdf.parse(date1);
            Date date22= sdf.parse(date2);

            if(date11.before(date22)) {
                result="before";
            }
            else if(date11.after(date22))
            {
                result="after";
            }
            else {
                result="equal";
            }
        } catch (ParseException e){
            e.printStackTrace();
        }
        return result;
    }


    public static String compareTiminng(String time, String endtime) {

        String pattern = "HH:mm";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        String result="";

        try {
            Date date1 = sdf.parse(time);
            Date date2 = sdf.parse(endtime);

            if(date1.before(date2)) {
                result="before";
            }
            else if(date1.after(date2))
            {
                result="after";
            }
            else {
                result="equal";
            }
        } catch (ParseException e){
            e.printStackTrace();
        }
        return result;
    }

    public static String compareTiminng24(String time, String endtime) {

        String pattern = "HH:mm aa";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        String result="";

        try {
            Date date1 = sdf.parse(time);
            Date date2 = sdf.parse(endtime);

            if(date1.before(date2)) {
                result="before";
            }
            else if(date1.after(date2))
            {
                result="after";
            }
            else {
                result="equal";
            }
        } catch (ParseException e){
            e.printStackTrace();
        }
        return result;
    }



    public static String getCurrencySymbol(String currencyCode) {
        Currency currency = Currency.getInstance(currencyCode);
        System.out.println(currencyCode + ":-" + currency.getSymbol(currencyLocaleMap.get(currency)));
        return currency.getSymbol(currencyLocaleMap.get(currency));
    }

    public static String getCurrentDate() {
        int cYear = Calendar.getInstance().get(Calendar.YEAR);
        int cMonth = Calendar.getInstance().get(Calendar.MONTH);
        int cDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        final String[] Days = {"1", "2", "3", "4", "5", "6", "7", "8", "9"};
        String day = "";

        if (cDay < Days.length) {
            day = "0" + cDay;
        } else {
            day = "" + cDay;
        }

        int currentMonth = cMonth + 1;
        final String[] Months = {"1", "2", "3", "4", "5", "6", "7", "8", "9"};
        String mm = "";

        if (currentMonth < Months.length) {
            mm = "0" + currentMonth;
        } else {
            mm = "" + currentMonth;
        }
//30-05-2020
        String currentDate= day+"-"+mm+"-"+cYear;
        Log.e("setDatePicker: Current ",currentDate);

        return currentDate;
    }

    public static long difference2Dates(String selected,String current)
    {
        long elapsedDays = 0;

        SimpleDateFormat dateFormat=new SimpleDateFormat("dd-MM-yyyy");
        try {
            Date ddSelected=dateFormat.parse(selected);
            Date ddCurrent=dateFormat.parse(current);
            long difference=ddSelected.getTime()-ddCurrent.getTime();
            Log.e("DatesDifference ",difference+"");

            long secondsInMilli = 1000;
            long minutesInMilli = secondsInMilli * 60;
            long hoursInMilli = minutesInMilli * 60;
            long daysInMilli = hoursInMilli * 24;

            elapsedDays = difference / daysInMilli;
//      difference = difference % daysInMilli;

            Log.e("DatesDifference ",difference+"");

        }
        catch (ParseException e) {
            e.printStackTrace();
        }

        return elapsedDays;
    }


    public static String getCurrentTime()
    {
        Calendar c1 = Calendar.getInstance();

        int currentHour = c1.get(Calendar.HOUR_OF_DAY);
        int currentMinute = c1.get(Calendar.MINUTE);
        Log.e("CurrentMinute ",currentMinute+"");
        Log.e("currentHour ",currentHour+"");


String hour="",minute="";
         if (currentHour<10)
         {
             hour="0"+String.valueOf(currentHour);
         }else {
             hour=String.valueOf(currentHour);
         }

        String currentTime=String.valueOf(hour)+":"+String.valueOf(currentMinute);

        Log.e("setTimeCurrent ",currentTime);

        return currentTime;
    }

    public static long diff2Times(String endTime,String startTime)
    {
        long time=0;


        try
        {
            SimpleDateFormat format = new SimpleDateFormat("HH:mm");
            Date date1 = format.parse(startTime);
            Date date2 = format.parse(endTime);
             time = date2.getTime() - date1.getTime();
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }

        return time;
    }

   /* public static String compare2Times(String t1,String t2)
    {
        String differenec="";

        try{
            String startTime = "10:00";
            String endTime = "12:00";
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            Date d1 = sdf.parse(startTime);
            Date d2 = sdf.parse(endTime);
            long elapsed = d2.getTime() - d1.getTime();
            Log.e("ElapsedDifference ",elapsed+"");
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }



    }*/

    public static String getHoursMinutesTime(int seconds)
    {
        String timeee="";
        int p1 = seconds % 60;
        int p2 = seconds / 60;
        int p3 = p2 % 60;
        p2 = p2 / 60;

        timeee= p2 + ":" + p3 + ":" + p1;

        return  timeee;
    }

}

