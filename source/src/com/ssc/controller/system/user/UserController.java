package com.ssc.controller.system.user;

import com.ssc.controller.base.BaseController;
import com.ssc.entity.Page;
import com.ssc.service.system.user.UserService;
import com.ssc.util.HttpUtils;
import com.ssc.util.PageData;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping({"/api/v1/member"})
public class UserController extends BaseController{
  @Resource(name="userService")
  private UserService userService;
  
  @RequestMapping({"/test"})
  public ModelAndView test(){
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
  @RequestMapping(value="/verificationCode",method=RequestMethod.GET)
  @ResponseBody
  public Object verificationCode(){
    PageData pd = new PageData();
    pd = getPageData();
    PageData userPd = new PageData();
    String message = "ok";
    int code = 200;
    String phone = pd.getString("mobile");
    pd.put("phone", phone);
    try{
      userPd = userService.findByUPhone(pd);
    }catch (Exception e1){
      logger.error(e1.toString(), e1);
      message = "userService.findByUPhone(pd) ERROR!";
      code = 500;
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
    try{
      HttpResponse response = HttpUtils.doGet(host, path, method, headers, querys);
      System.out.println(response.toString());
      
      System.out.println(EntityUtils.toString(response.getEntity()));
    }catch (Exception e){
      logger.error(e.toString(), e);
      message = "ERROR:When Send Msg To User";
      code = 500;
    }
    String user_id = get32UUID();
    pd.put("verification_code", verCode);
    if ((userPd != null) && (userPd.size() > 0)){//如果用户已经存在，则更新其最新验证码
      try{
    	 pd.put("user_id", userPd.getString("id"));
    	 userService.updateUserByUid(pd);
      }catch (Exception e){
        logger.error(e.toString(), e);
        message = "ERROR:When Update UserVerCode";
        code = 500;
      }
    }else{
      pd.put("user_id", user_id);
      pd.put("verification_code", verCode);
      try{
        userService.saveU(pd);
      }catch (Exception e){
        this.logger.error(e.toString(), e);
        message = "ERROR:When Save User";
        code = 500;
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
  public Object login(@RequestBody String req,
		  HttpServletRequest request){
	JSONObject data = new JSONObject();
	JSONObject respJson = new JSONObject();
	HttpSession session = request.getSession();
    PageData pd = new PageData();
    pd = getPdFromJson(req);
    String message = "验证不通过";
    Date date = new Date();
    int code = 403;
    PageData userPd = new PageData();
    try{
      userPd = userService.findByUPhone(pd);
      userPd.put("isNew", (Integer)userPd.get("state")==0?true:false);//默认是已注册的老用户
      if (userPd.getString("verification_code").equals(pd.getString("verificationCode"))){//验证通过
        message = "验证通过";
        code = 200;
        if ((Integer)userPd.get("state")==0){//注册
          pd.put("state", 1);
          pd.put("join_time", date);
          pd.put("user_id", userPd.getString("id"));
          userService.updateUserByUid(pd);
          userPd = userService.findByUPhone(pd);
          userPd.put("isNew", true);
        }
        //如果登录成功，存下用户的token和session
        session.setAttribute("user", userService.findUserByUPhone(pd));
        PageData pd2 = new PageData();
        pd2.put("token", date.getTime());
        pd2.put("user_id", userPd.getString("id"));
        userService.updateUserByUid(pd2);
        
        userPd.remove("verification_code");
        userPd.remove("state");
        data = JSONObject.fromObject(userPd);
        respJson.put("data", data);
      }
      
    }catch (Exception e){
      this.logger.error(e.toString(), e);
      message = "ERROR:When Check VERIFICATION_CODE";
      code = 500;
    }
    respJson.put("code", code);
    respJson.put("message", message);
    return respJson;
  }
  /**
   * 更新用户
   * 2017-9-11 zxk_senNy
   * @return
 * @throws Exception 
   */
  @RequestMapping(value="/updateUser",method=RequestMethod.PUT)
  @ResponseBody
  public Object updateUser(@RequestBody String req){
	  JSONObject reqJson = JSONObject.fromObject(req);
	  JSONObject respJson = new JSONObject();
	  PageData pd = new PageData();
	  int code = 200;
	  String message = "ok";
	  //后续做30天更改判断
	  
	  String token = pd.getString("token");//客户端发来的token
	  long time = 0;
	  if (token==null) {
		code = 401;
		message = "token为空";
	  }else {
		  try {
			pd.put("phone", token.split("-")[0]);
			PageData user = userService.findUserByUPhone(pd);//根据token中的手机信息识别出用户
			String userToken = user.getString("token");//用户的实际token
			time = Long.valueOf(token.split("-")[1]);//获取时间戳
			long now = new Date().getTime();
			long hours = (now-time)/1000/3600;
			code = 401;
			message = "token已过期";
			if (hours<(24*30)||!token.equals(userToken)) {//不超过30天
			  code = 200;
			  message = "ok";
			  Date date = new Date();
			  pd.put("user_id", reqJson.getString("id"));
			  pd.put("nickname", reqJson.getString("nickname"));
			  pd.put("name", reqJson.getString("name"));
			  pd.put("unit", reqJson.getString("unit"));
			  pd.put("email", reqJson.getString("email"));
			  pd.put("update_time", date);
			 
			  userService.updateUserByUid(pd);
			  pd.remove("user_id");pd.remove("update_time");
			  pd.put("id", reqJson.getString("id"));
			  pd.put("updateTime",date.getTime());
			  respJson.put("data", JSONObject.fromObject(pd));
			}
		  } catch (Exception e) {
			 logger.error(e.toString(), e);
		     message = "ERROR:When updateUser";
		     code = 500;
		  }
	  }
	
	  respJson.put("code", code);
	  respJson.put("message", message);
	  return respJson;
  }
  /**
   * 进入app时的免登陆判断
   * 2017-10-4 zxk_senNy
   * @return
 * @throws Exception 
   */
  @RequestMapping(value="/intoApp",method=RequestMethod.POST)
  @ResponseBody
  public Object intoApp(@RequestBody String req) throws Exception{
	  JSONObject respJson = new JSONObject();
	  PageData pd = new PageData();
	  pd = getPdFromJson(req);
	  String message = "ok";
	  int code = 200;
	  String token = pd.getString("token");//客户端发来的token
	  long time = 0;
	  if (token==null) {
		code = 401;
		message = "token为空";
	  }else {
		pd.put("phone", token.split("-")[0]);
		PageData user = userService.findUserByUPhone(pd);//根据token中的手机信息识别出用户
		String userToken = user.getString("token");//用户的实际token
		time = Long.valueOf(token.split("-")[1]);//获取时间戳
		long now = new Date().getTime();
		long hours = (now-time)/1000/3600;
		if (hours>=(24*30)||!token.equals(userToken)) {//超过30天
			code = 401;
			message = "token已过期";
			}
	  }
	  respJson.put("code", code);
	  respJson.put("message", message);
	  return respJson;
  }
}
