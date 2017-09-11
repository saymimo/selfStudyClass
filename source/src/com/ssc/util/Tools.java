package com.ssc.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tools
{
  public static int getRandomNum()
  {
    Random r = new Random();
    return r.nextInt(900000) + 100000;
  }
  
  public static boolean notEmpty(String s)
  {
    return (s != null) && (!"".equals(s)) && (!"null".equals(s));
  }
  
  public static boolean isEmpty(String s)
  {
    return (s == null) || ("".equals(s)) || ("null".equals(s));
  }
  
  public static String[] str2StrArray(String str, String splitRegex)
  {
    if (isEmpty(str)) {
      return null;
    }
    return str.split(splitRegex);
  }
  
  public static boolean isValidDate(String str)
  {
    boolean convertSuccess = true;
    
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    try
    {
      format.setLenient(false);
      format.parse(str);
    }
    catch (ParseException e)
    {
      convertSuccess = false;
    }
    return convertSuccess;
  }
  
  public static String[] str2StrArray(String str)
  {
    return str2StrArray(str, ",\\s*");
  }
  
  public static String date2Str(Date date)
  {
    return date2Str(date, "yyyy-MM-dd HH:mm:ss");
  }
  
  public static Date str2Date(String date)
  {
    if (notEmpty(date))
    {
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      try
      {
        return sdf.parse(date);
      }
      catch (ParseException e)
      {
        e.printStackTrace();
        
        return new Date();
      }
    }
    return null;
  }
  
  public static String date2Str(Date date, String format)
  {
    if (date != null)
    {
      SimpleDateFormat sdf = new SimpleDateFormat(format);
      return sdf.format(date);
    }
    return "";
  }
  
  public static String getTimes(String StrDate)
  {
    String resultTimes = "";
    
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    try
    {
      Date now = new Date();
      Date date = df.parse(StrDate);
      long times = now.getTime() - date.getTime();
      long day = times / 86400000L;
      long hour = times / 3600000L - day * 24L;
      long min = times / 60000L - day * 24L * 60L - hour * 60L;
      long sec = times / 1000L - day * 24L * 60L * 60L - hour * 60L * 60L - min * 60L;
      
      StringBuffer sb = new StringBuffer();
      if (hour > 0L) {
        sb.append(hour + "小时前");
      } else if (min > 0L) {
        sb.append(min + "分钟前");
      } else {
        sb.append(sec + "秒前");
      }
      resultTimes = sb.toString();
    }
    catch (ParseException e)
    {
      e.printStackTrace();
    }
    return resultTimes;
  }
  
  public static void writeFile(String fileP, String content)
  {
    String filePath = String.valueOf(Thread.currentThread().getContextClassLoader().getResource("")) + "../../";
    filePath = (filePath.trim() + fileP.trim()).substring(6).trim();
    if (filePath.indexOf(":") != 1) {
      filePath = File.separator + filePath;
    }
    try
    {
      OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(filePath), "utf-8");
      BufferedWriter writer = new BufferedWriter(write);
      writer.write(content);
      writer.close();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }
  
  public static boolean checkEmail(String email)
  {
    boolean flag = false;
    try
    {
      String check = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
      Pattern regex = Pattern.compile(check);
      Matcher matcher = regex.matcher(email);
      flag = matcher.matches();
    }
    catch (Exception e)
    {
      flag = false;
    }
    return flag;
  }
  
  public static boolean checkMobileNumber(String mobileNumber)
  {
    boolean flag = false;
    try
    {
      Pattern regex = Pattern.compile("^(((13[0-9])|(15([0-3]|[5-9]))|(18([0-4]|[5-9])))\\d{8})|(0\\d{2}-\\d{8})|(0\\d{3}-\\d{7})$");
      Matcher matcher = regex.matcher(mobileNumber);
      flag = matcher.matches();
    }
    catch (Exception e)
    {
      flag = false;
    }
    return flag;
  }
  
  public static boolean checkKey(String paraname, String FKEY)
  {
    paraname = paraname == null ? "" : paraname;
    return MD5.md5(paraname + DateUtil.getDays() + ",fh,").equals(FKEY);
  }
  
  public static String readTxtFile(String fileP)
  {
    try
    {
      String filePath = String.valueOf(Thread.currentThread().getContextClassLoader().getResource("")) + "../../";
      filePath = filePath.replaceAll("file:/", "");
      filePath = filePath.replaceAll("%20", " ");
      filePath = filePath.trim() + fileP.trim();
      if (filePath.indexOf(":") != 1) {
        filePath = File.separator + filePath;
      }
      String encoding = "utf-8";
      File file = new File(filePath);
      if ((file.isFile()) && (file.exists()))
      {
        InputStreamReader read = new InputStreamReader(
          new FileInputStream(file), encoding);
        BufferedReader bufferedReader = new BufferedReader(read);
        String lineTxt = null;
        if ((lineTxt = bufferedReader.readLine()) != null) {
          return lineTxt;
        }
        read.close();
      }
      else
      {
        System.out.println("找不到指定的文件,查看此路径是否正确:" + filePath);
      }
    }
    catch (Exception e)
    {
      System.out.println("读取文件内容出错");
    }
    return "";
  }
  
  public static void main(String[] args)
  {
    System.out.println(primarykeyvalue());
  }
  
  public static String primarykeyvalue()
  {
    return 
    
      Ruandchar() + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + random1000() + Ruandchar();
  }
  
  public static char Ruandchar()
  {
    Random random = new Random();
    int r = 0;
    do
    {
      r = random.nextInt(57) + 65;
    } while (((r > 90) && (r < 97)) || (r == 0));
    char a = (char)r;
    
    return a;
  }
  
  public static String random1000()
  {
    int num = (int)(Math.random() * 1000.0D);
    System.out.println(num);
    DecimalFormat df = new DecimalFormat("000");
    String str2 = df.format(num);
    System.out.println(str2);
    return str2;
  }
  
  public static String getAreaInfoOfIDCard(String idNumber)
    throws IOException
  {
    String result = "";
    if ((idNumber == null) || (idNumber.length() != 18)) {
      return result;
    }
    InputStream provinceInputStream = Tools.class.getClassLoader()
      .getResourceAsStream("Province.properties");
    Properties provinceProperties = new Properties();
    provinceProperties.load(provinceInputStream);
    
    InputStream cityInputStream = Tools.class.getClassLoader()
      .getResourceAsStream("City.properties");
    Properties cityProperties = new Properties();
    cityProperties.load(cityInputStream);
    
    InputStream countyInputStream = Tools.class.getClassLoader()
      .getResourceAsStream("County.properties");
    Properties countyProperties = new Properties();
    countyProperties.load(countyInputStream);
    
    String idProvincePart = idNumber.substring(0, 2);
    String provinceInfo = provinceProperties.getProperty(idProvincePart);
    
    String idCityPart = idNumber.substring(0, 4);
    String cityInfo = cityProperties.getProperty(idCityPart);
    
    String idCountyPart = idNumber.substring(0, 6);
    String countyInfo = countyProperties.getProperty(idCountyPart);
    result = provinceInfo + "-" + cityInfo + "-" + countyInfo;
    return result;
  }
  
  public static String getBirthByIdCard(String idNumber)
  {
    if ((idNumber == null) || (idNumber.length() != 18)) {
      return "";
    }
    String birthYYYYPart = idNumber.substring(6, 10);
    String birthMonthPart = idNumber.substring(10, 12);
    String birthDayPart = idNumber.substring(12, 14);
    return birthYYYYPart + "-" + birthMonthPart + "-" + birthDayPart;
  }
  
  public static String getGenderByIdCard(String idNumber)
  {
    String result = "未知";
    if ((idNumber == null) || (idNumber.length() != 18)) {
      return "";
    }
    String genderPart = idNumber.substring(16, 17);
    if (Integer.parseInt(genderPart) % 2 != 0) {
      result = "0";
    } else {
      result = "1";
    }
    return result;
  }
  
  public static final int[] POWER = { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 
    10, 5, 8, 4, 2 };
  
  public static boolean validateIdNumber(String idNumber)
  {
    if (!initIDValidate(idNumber)) {
      return Boolean.FALSE.booleanValue();
    }
    if (!idCheckCodeValidate(idNumber)) {
      return Boolean.FALSE.booleanValue();
    }
    return Boolean.TRUE.booleanValue();
  }
  
  public static boolean initIDValidate(String idNumber)
  {
    boolean result = Boolean.FALSE.booleanValue();
    if ((idNumber == null) || (idNumber.length() != 18)) {
      return result;
    }
    String idNumberDataPart = idNumber.substring(0, 17);
    if (idNumberDataPart.matches("^[0-9]*$")) {
      result = Boolean.TRUE.booleanValue();
    }
    return result;
  }
  
  public static boolean idCheckCodeValidate(String idNumber)
  {
    boolean result = Boolean.FALSE.booleanValue();
    
    String idNumberDataPart = idNumber.substring(0, 17);
    
    String idNubmerCheckCode = idNumber.substring(17, 18);
    
    char[] cArr = idNumberDataPart.toCharArray();
    int[] iCard = converCharToInt(cArr);
    
    int sumByPower = getPowerSum(iCard);
    
    String computeCheckCode = getCheckCode(sumByPower);
    if (computeCheckCode.equalsIgnoreCase(idNubmerCheckCode)) {
      result = Boolean.TRUE.booleanValue();
    }
    return result;
  }
  
  public static int[] converCharToInt(char[] allChars)
  {
    int len = allChars.length;
    int[] result = new int[len];
    try
    {
      for (int i = 0; i < len; i++) {
        result[i] = Integer.parseInt(String.valueOf(allChars[i]));
      }
    }
    catch (NumberFormatException e)
    {
      e.printStackTrace();
    }
    return result;
  }
  
  public static int getPowerSum(int[] idNubmerArray)
  {
    int result = 0;
    if (POWER.length == idNubmerArray.length) {
      for (int i = 0; i < idNubmerArray.length; i++) {
        for (int j = 0; j < POWER.length; j++) {
          if (i == j) {
            result += idNubmerArray[i] * POWER[j];
          }
        }
      }
    }
    return result;
  }
  
  public static String getCheckCode(int powerSum)
  {
    String sCode = "";
    switch (powerSum % 11)
    {
    case 10: 
      sCode = "2";
      break;
    case 9: 
      sCode = "3";
      break;
    case 8: 
      sCode = "4";
      break;
    case 7: 
      sCode = "5";
      break;
    case 6: 
      sCode = "6";
      break;
    case 5: 
      sCode = "7";
      break;
    case 4: 
      sCode = "8";
      break;
    case 3: 
      sCode = "9";
      break;
    case 2: 
      sCode = "x";
      break;
    case 1: 
      sCode = "0";
      break;
    case 0: 
      sCode = "1";
    }
    return sCode;
  }
  
  public static String convertIdcarBy15bit(String idcard)
  {
    String idcard17 = null;
    if (idcard.length() != 15) {
      return null;
    }
    if (isDigital(idcard))
    {
      String birthday = idcard.substring(6, 12);
      Date birthdate = null;
      try
      {
        birthdate = new SimpleDateFormat("yyMMdd").parse(birthday);
      }
      catch (ParseException e)
      {
        e.printStackTrace();
      }
      Calendar cday = Calendar.getInstance();
      cday.setTime(birthdate);
      String year = String.valueOf(cday.get(1));
      
      idcard17 = idcard.substring(0, 6) + year + idcard.substring(8);
      
      char[] c = idcard17.toCharArray();
      String checkCode = "";
      if (c != null)
      {
        int[] bit = new int[idcard17.length()];
        

        bit = converCharToInt(c);
        int sum17 = 0;
        sum17 = getPowerSum(bit);
        

        checkCode = getCheckCode(sum17);
        if (checkCode == null) {
          return null;
        }
        idcard17 = idcard17 + checkCode;
      }
    }
    else
    {
      return null;
    }
    return idcard17;
  }
  
  public static boolean isDigital(String str)
  {
    return (str == null) || ("".equals(str)) ? false : str.matches("^[0-9]*$");
  }
}
