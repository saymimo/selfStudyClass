package com.ssc.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class WebAppContextListener
  implements ServletContextListener
{
  public void contextDestroyed(ServletContextEvent event) {}
  
  public void contextInitialized(ServletContextEvent event)
  {
    com.ssc.util.Const.WEB_APP_CONTEXT = WebApplicationContextUtils.getWebApplicationContext(event.getServletContext());
  }
}
