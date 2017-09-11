package com.ssc.util;

import java.io.PrintStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class DateUtil
{
  private static final SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy");
  private static final SimpleDateFormat sdfDay = new SimpleDateFormat(
    "yyyy-MM-dd");
  private static final SimpleDateFormat sdfDays = new SimpleDateFormat(
    "yyyyMMdd");
  private static final SimpleDateFormat sdfTime = new SimpleDateFormat(
    "yyyy-MM-dd HH:mm:ss");
  private static final SimpleDateFormat sdfMonth = new SimpleDateFormat("yyyy-mm");
  
  public static String getYear()
  {
    return sdfYear.format(new Date());
  }
  
  public static String getDay()
  {
    return sdfDay.format(new Date());
  }
  
  public static String getNumber()
  {
    Date aUpdate = new Date();
    SimpleDateFormat sd = new SimpleDateFormat("yyyyMMddHHmmsss");
    String str = sd.format(aUpdate);
    StringBuffer buffer = new StringBuffer("0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ");
    StringBuffer sb = new StringBuffer();
    Random r = new Random();
    int range = buffer.length();
    sb.append(buffer.charAt(r.nextInt(range)));
    str = str + sb.toString();
    return str;
  }
  
  public static String getData_Number()
  {
    Date aUpdate = new Date();
    SimpleDateFormat sd = new SimpleDateFormat("yyyyMMdd");
    String str = sd.format(aUpdate);
    str = str + "_001";
    return str;
  }
  
  public static String getDays()
  {
    return sdfDays.format(new Date());
  }
  
  public static String getTime()
  {
    return sdfTime.format(new Date());
  }
  
  public static boolean compareDate(String s, String e)
  {
    if ((fomatDate(s) == null) || (fomatDate(e) == null)) {
      return false;
    }
    return fomatDate(s).getTime() >= fomatDate(e).getTime();
  }
  
  public static Date fomatDate(String date)
  {
    DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
    try
    {
      return fmt.parse(date);
    }
    catch (ParseException e)
    {
      e.printStackTrace();
    }
    return null;
  }
  
  public static String getString(Object date)
  {
    if ((date == null) || (date.equals(""))) {
      return "";
    }
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    Date uDate = (Date)date;
    Date sDate = new Date(uDate.getTime());
    String strDate = df.format(sDate);
    return strDate;
  }
  
  public static boolean isValidDate(String s)
  {
    DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
    try
    {
      fmt.parse(s);
      return true;
    }
    catch (Exception e) {}
    return false;
  }
  
  public static int getDiffYear(String startTime, String endTime)
  {
    DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
    try
    {
      long aa = 0L;
      return (int)((fmt.parse(endTime).getTime() - fmt.parse(startTime).getTime()) / 86400000L / 365L);
    }
    catch (Exception e) {}
    return 0;
  }
  
  public static long getDaySub(String beginDateStr, String endDateStr)
  {
    long day = 0L;
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    Date beginDate = null;
    Date endDate = null;
    try
    {
      beginDate = format.parse(beginDateStr);
      endDate = format.parse(endDateStr);
    }
    catch (ParseException e)
    {
      e.printStackTrace();
    }
    day = (endDate.getTime() - beginDate.getTime()) / 86400000L;
    

    return day;
  }
  
  public static String getAfterDayDate(String days)
  {
    int daysInt = Integer.parseInt(days);
    
    Calendar canlendar = Calendar.getInstance();
    canlendar.add(5, daysInt);
    Date date = canlendar.getTime();
    
    SimpleDateFormat sdfd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String dateStr = sdfd.format(date);
    
    return dateStr;
  }
  
  public static String getAfterDayWeek(String days)
  {
    int daysInt = Integer.parseInt(days);
    
    Calendar canlendar = Calendar.getInstance();
    canlendar.add(5, daysInt);
    Date date = canlendar.getTime();
    
    SimpleDateFormat sdf = new SimpleDateFormat("E");
    String dateStr = sdf.format(date);
    
    return dateStr;
  }
  
  public static void main(String[] args)
  {
    System.out.println(getDays());
    System.out.println(getAfterDayWeek("3"));
  }
  
  public static int getDaysByYearMonth(int year, int month)
  {
    Calendar a = Calendar.getInstance();
    a.set(1, year);
    a.set(2, month - 1);
    a.set(5, 1);
    a.roll(5, -1);
    int maxDate = a.get(5);
    return maxDate;
  }
  
  public static String splitNowDate(Date date)
  {
    String str = "";
    SimpleDateFormat sdf = new SimpleDateFormat(" yyyy 年 MM 月 dd 日 ");
    if (date == null) {
      str = sdf.format(new Date());
    } else {
      str = sdf.format(date);
    }
    return str;
  }
  
  public static boolean getDateMAX(String str1)
  {
    boolean is_true = false;
    try
    {
      Date d1 = sdfMonth.parse(str1);
      SimpleDateFormat sdfd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      String dateStr = sdfd.format(new Date());
      Date d2 = sdfMonth.parse(dateStr);
      if (d1.getTime() - d2.getTime() > 0L) {
        is_true = false;
      } else {
        is_true = true;
      }
    }
    catch (ParseException e)
    {
      e.printStackTrace();
    }
    return is_true;
  }
  
  public static int monthSpace(String str1)
    throws ParseException
  {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    String str2 = sdf.format(new Date());
    
    Calendar bef = Calendar.getInstance();
    Calendar aft = Calendar.getInstance();
    bef.setTime(sdf.parse(str1));
    aft.setTime(sdf.parse(str2));
    
    int result = aft.get(1) - bef.get(1);
    if (result == 0) {
      result = aft.get(2) - bef.get(2);
    }
    return result;
  }
}
