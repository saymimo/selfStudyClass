package com.ssc.controller.base;

import com.ssc.entity.Page;
import com.ssc.util.Logger;
import com.ssc.util.PageData;
import com.ssc.util.UuidUtil;
import java.io.PrintStream;
import java.util.Date;
import java.util.Random;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

public class BaseController
{
  protected Logger logger = Logger.getLogger(getClass());
  private static final long serialVersionUID = 6357869213649815390L;
  
  public PageData getPageData(){
    return new PageData(getRequest());
  }
  
  public PageData getPdFromJson(String req){
	  return new PageData(req);
  }
  
  public ModelAndView getModelAndView()
  {
    return new ModelAndView();
  }
  
  public HttpServletRequest getRequest()
  {
    HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
    
    return request;
  }
  
  public String get32UUID()
  {
    return UuidUtil.get32UUID();
  }
  
  public static String getRandomCode()
  {
    String str = "0123456789";
    StringBuilder sb = new StringBuilder(4);
    for (int i = 0; i < 4; i++)
    {
      char ch = str.charAt(new Random().nextInt(str.length()));
      sb.append(ch);
    }
    System.out.println(sb.toString());
    return sb.toString();
  }
  
  public Page getPage()
  {
    return new Page();
  }
  
  public static void logBefore(Logger logger, String interfaceName)
  {
    logger.info("");
    logger.info("start");
    logger.info(interfaceName);
  }
  
  public static void logAfter(Logger logger)
  {
    logger.info("end");
    logger.info("");
  }
  
  public int DateDifference(Date start, Date end)
  {
    int t = (int)((end.getTime() - start.getTime()) / 86400000L);
    return t;
  }
}
