package com.ssc.util;

import com.ssc.service.system.user.UserService;

public final class ServiceHelper
{
  public static Object getService(String serviceName)
  {
    return Const.WEB_APP_CONTEXT.getBean(serviceName);
  }
  
  public static UserService getUserService()
  {
    return (UserService)getService("userService");
  }
  
}
