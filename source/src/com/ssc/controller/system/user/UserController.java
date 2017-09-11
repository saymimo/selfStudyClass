package com.ssc.controller.system.user;

import com.ssc.controller.base.BaseController;
import com.ssc.service.system.user.UserService;
import com.ssc.util.HttpUtils;
import com.ssc.util.Logger;
import com.ssc.util.PageData;
import java.io.PrintStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import net.sf.json.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping({"/api/v1/member"})
public class UserController
  extends BaseController
{
  @Resource(name="userService")
  private UserService userService;
  
  @RequestMapping({"/test"})
  public ModelAndView test()
  {
    ModelAndView mv = getModelAndView();
    mv.setViewName("test");
    return mv;
  }
  /**
   * 发送验证码    
   * 2017-9-11 zxk_senNy
   * @param ub
   * @return
   */
  @RequestMapping(value="/verificationCode",method=RequestMethod.POST)
  @ResponseBody
  public Object verificationCode(@RequestBody String ub){
    PageData pd = new PageData();
    PageData userPd = new PageData();
    JSONObject uJson = JSONObject.fromObject(ub);
    String message = "ok";
    String code = "200";
    String phone = uJson.getString("mobile");
    pd.put("PHONE", phone);
    try
    {
      userPd = this.userService.findByUPhone(pd);
    }
    catch (Exception e1)
    {
      this.logger.error(e1.toString(), e1);
      message = "userService.findByUPhone(pd) ERROR!";
      code = "500";
    }
    String verCode = getRandomCode();
    String host = "http://sms.market.alicloudapi.com";
    String path = "/singleSendSms";
    String method = "GET";
    String appcode = "2dc57d3d8fa44802a604d9bc031ae9fd";
    Map<String, String> headers = new HashMap();
    
    headers.put("Authorization", "APPCODE " + appcode);
    Map<String, String> querys = new HashMap();
    querys.put("ParamString", "{'verificationCode':'" + verCode + "'}");
    querys.put("RecNum", phone);
    querys.put("SignName", "自习课");
    querys.put("TemplateCode", "SMS_86745124");
    try
    {
      HttpResponse response = HttpUtils.doGet(host, path, method, headers, querys);
      System.out.println(response.toString());
      
      System.out.println(EntityUtils.toString(response.getEntity()));
    }
    catch (Exception e)
    {
      this.logger.error(e.toString(), e);
      message = "ERROR:When Send Msg To User";
      code = "400";
    }
    String user_id = get32UUID();
    pd.put("PHONE", phone);
    pd.put("VERIFICATION_CODE", verCode);
    if ((userPd != null) && (userPd.size() > 0)){
      try{
        this.userService.updateUser(pd);
      }catch (Exception e){
        this.logger.error(e.toString(), e);
        message = "ERROR:When Update UserVerCode";
        code = "500";
      }
    }else{
      pd.put("USER_ID", user_id);
      pd.put("USERNAME", phone);
      try{
        this.userService.saveU(pd);
      }catch (Exception e){
        this.logger.error(e.toString(), e);
        message = "ERROR:When Save User";
        code = "500";
      }
    }
    JSONObject data = new JSONObject();
    data.put("phone", phone);
    JSONObject jsonObject = new JSONObject();
    
    jsonObject.put("code", code);
    jsonObject.put("message", message);
    jsonObject.put("data", data);
    return jsonObject;
  }
  /**
   * 登录注册
   * 2017-9-11 zxk_senNy
   * @return
   */
  @RequestMapping(value="/login",method=RequestMethod.POST)
  @ResponseBody
  public Object login()
  {
    PageData pd = new PageData();
    PageData userPd = new PageData();
    pd = getPageData();
    String message = "err";
    String code = "200";
    try
    {
      pd.put("PHONE", pd.getString("phone"));
      userPd = this.userService.findByUPhone(pd);
      if (userPd.getString("VERIFICATION_CODE").equals(pd.getString("verificationCode")))
      {
        message = "ok";
        if (userPd.getString("STATUS").equals("0"))
        {
          pd.put("state", "1");
          pd.put("CREATE_DATE", new Date());
          this.userService.updateUser(pd);
        }
      }
    }
    catch (Exception e)
    {
      this.logger.error(e.toString(), e);
      message = "ERROR:When Check VERIFICATION_CODE";
      code = "500";
    }
    JSONObject data = new JSONObject();
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("code", code);
    jsonObject.put("message", message);
    jsonObject.put("data", data);
    return jsonObject;
  }
  /**
   * 更新用户
   * 2017-9-11 zxk_senNy
   * @return
   */
  @RequestMapping(value="/updateUser",method=RequestMethod.PUT)
  public Object updateUser(@RequestBody String req){
	  JSONObject reqJson = JSONObject.fromObject(req);
	  JSONObject respJson = new JSONObject();
	  PageData pd = new PageData();
	  int code = 200;
	  String message = "ok";
	  //后续做30天更改判断
	  Date date = new Date();
	  pd.put("user_id", reqJson.getString("id"));
	  pd.put("nickname", reqJson.getString("nickname"));
	  pd.put("name", reqJson.getString("name"));
	  pd.put("unit", reqJson.getString("unit"));
	  pd.put("email", reqJson.getString("email"));
	  pd.put("update_time", date);
	  try {
		userService.updateUser(pd);
	  } catch (Exception e) {
		 logger.error(e.toString(), e);
	     message = "ERROR:When updateUser";
	     code = 500;
	  }
	  pd.remove("user_id");pd.remove("update_time");
	  pd.put("id", reqJson.getString("id"));
	  pd.put("updateTime", date);
	  respJson.put("code", code);
	  respJson.put("message", message);
	  respJson.put("data", JSONObject.fromObject(pd));
	  return reqJson;
  }
  
}
