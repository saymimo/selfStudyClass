package com.ssc.util;

import cn.jpush.api.ErrorCodeEnum;
import cn.jpush.api.IOSExtra;
import cn.jpush.api.JPushClient;
import cn.jpush.api.MessageResult;
import com.ssc.controller.base.BaseController;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping({"/jpush"})
public class JPushClientUtil
  extends BaseController
{
  private static final String appKey = "737cea7b852bf8c60cecbf51";
  private static final String masterSecret = "c0e2c47796f2095cbc56a516";
  private static JPushClient jpush = null;
  public static final int MAX = 2147483647;
  public static final int MIN = 1073741823;
  private static long timeToLive = 86400L;
  
  public static void main(String[] args)
  {
    String msgTitle = "推送测试";
    String msgContent = "看到信息了么，看到就推送成功了！";
    String userid = "7753b9c538de44c791bb44eed1980d39";
    pushMessage(msgTitle, msgContent, userid);
  }
  
  private static void init()
  {
    jpush = new JPushClient("c0e2c47796f2095cbc56a516", "737cea7b852bf8c60cecbf51", timeToLive);
  }
  
  public static void pushMessage(String msgTitle, String msgContent, String userid)
  {
    String result = "";
    
    init();
    


    int sendNo = getRandomSendNo();
    

    Map<String, Object> extra = new HashMap();
    IOSExtra iosExtra = new IOSExtra(10, "WindowsLogonSound.wav");
    extra.put("ios", iosExtra);
    






    MessageResult msgResult = jpush.sendNotificationWithAppKey(sendNo, msgTitle, msgContent);
    if (msgResult != null)
    {
      result = "服务器返回数据: " + msgResult.toString();
      System.out.println(result);
      if (msgResult.getErrcode() == ErrorCodeEnum.NOERROR.value())
      {
        result = String.format(
          "发送成功， sendNo= %s,messageId= %s", new Object[] {
          Integer.valueOf(msgResult.getSendno()), 
          msgResult.getMsg_id() });
        

        System.out.println(result);
      }
      else
      {
        result = 
          "发送失败， 错误代码=" + msgResult.getErrcode() + ", 错误消息=" + msgResult.getErrmsg();
        System.out.println(result);
      }
    }
    else
    {
      System.out.println("无法获取数据");
    }
  }
  
  public static int getRandomSendNo()
  {
    return (int)(1073741823.0D + Math.random() * 1073741824.0D);
  }
}
