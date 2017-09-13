package com.ssc.quartz;

import java.io.IOException;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class GetNetworkTime
{
  public static void main(String[] args)
  {
    String webUrl1 = "http://www.bjtime.cn";
    String webUrl2 = "http://www.baidu.com";
    String webUrl3 = "http://www.taobao.com";
    String webUrl4 = "http://www.ntsc.ac.cn";
    String webUrl5 = "http://www.360.cn";
    String webUrl6 = "http://www.beijing-time.org";
    System.out.println(getWebsiteDatetime(webUrl1) + " [bjtime]");
    System.out.println(getWebsiteDatetime(webUrl2) + " [百度]");
    System.out.println(getWebsiteDatetime(webUrl3) + " [淘宝]");
    System.out.println(getWebsiteDatetime(webUrl4) + " [中国科学院国家授时中心]");
    System.out.println(getWebsiteDatetime(webUrl5) + " [360安全卫士]");
    System.out.println(getWebsiteDatetime(webUrl6) + " [beijing-time]");
  }
  
  public static String getBaiDuWebTime()
  {
    return getWebsiteDatetime("http://www.baidu.com");
  }
  
  private static String getWebsiteDatetime(String webUrl)
  {
    try
    {
      URL url = new URL(webUrl);
      URLConnection uc = url.openConnection();
      uc.connect();
      long ld = uc.getDate();
      Date date = new Date(ld);
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
      return sdf.format(date);
    }
    catch (MalformedURLException e)
    {
      e.printStackTrace();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    return null;
  }
}
