package com.ssc.util;

import java.io.File;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class PathUtil
{
  public static String getPicturePath(String pathType, String pathCategory)
  {
    String strResult = "";
    HttpServletRequest request = ((ServletRequestAttributes)
      RequestContextHolder.getRequestAttributes()).getRequest();
    StringBuffer strBuf = new StringBuffer();
    if ((!"visit".equals(pathType)) && 
      ("save".equals(pathType)))
    {
      String projectPath = PublicUtil.getPorjectPath().replaceAll("\\\\", 
        "/");
      projectPath = splitString(projectPath, "bin/");
      
      strBuf.append(projectPath);
      strBuf.append("webapps/ROOT/");
    }
    strResult = strBuf.toString();
    
    return strResult;
  }
  
  private static String splitString(String str, String param)
  {
    String result = str;
    if (str.contains(param))
    {
      int start = str.indexOf(param);
      result = str.substring(0, start);
    }
    return result;
  }
  
  public static String getClasspath()
  {
    String path = (String.valueOf(Thread.currentThread().getContextClassLoader().getResource("")) + "../../").replaceAll("file:/", "").replaceAll("%20", " ").trim();
    if (path.indexOf(":") != 1) {
      path = File.separator + path;
    }
    return path;
  }
  
  public static String getClassResources()
  {
    String path = String.valueOf(Thread.currentThread().getContextClassLoader().getResource("")).replaceAll("file:/", "").replaceAll("%20", " ").trim();
    if (path.indexOf(":") != 1) {
      path = File.separator + path;
    }
    return path;
  }
  
  public static String PathAddress()
  {
    String strResult = "";
    
    HttpServletRequest request = ((ServletRequestAttributes)
      RequestContextHolder.getRequestAttributes()).getRequest();
    
    StringBuffer strBuf = new StringBuffer();
    
    strBuf.append(request.getScheme() + "://");
    strBuf.append(request.getServerName() + ":");
    strBuf.append(request.getServerPort());
    
    strBuf.append(request.getContextPath() + "/");
    
    strResult = strBuf.toString();
    
    return strResult;
  }
}
