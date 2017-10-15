package com.ssc.controller.system.interaction;

import java.util.Date;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.apache.shiro.authc.UsernamePasswordToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ssc.controller.base.BaseController;
import com.ssc.service.system.user.UserService;
import com.ssc.service.system.userAction.UserActionService;
import com.ssc.util.PageData;

@Controller
@RequestMapping(value="/api/v1/interaction")
public class InteractionController extends BaseController {

	  @Resource(name="userActionService")
	  private UserActionService userActionService;
	  
	  @Resource(name="userService")
	  private UserService userService;
	  
	  /**
	  	 * 用户操作：收藏，赞，关注等
	  	 * 2017-9-30 zxk_senNy
	  	 * @param req
	  	 * @return
	  	 */
	  	@RequestMapping(value="/interact",method=RequestMethod.POST)
	  	@ResponseBody
		public Object interact(@RequestBody String req){
			JSONObject respJson = new JSONObject();
			JSONObject data = new JSONObject();
			PageData pd = new PageData();
			pd = getPdFromJson(req);
			
			String user_action_id = get32UUID();
			Date date = new Date();
			pd.put("user_action_id", user_action_id);
			pd.put("obj_id", pd.getString("id"));
			pd.put("user_id", pd.getString("memberId"));
			//pd.put("action_type", 2);//操作类型 前台传
			//pd.put("obj_parentid", "");//作用对象的上级id 前台传
			//pd.put("obj_belong", "");//作用对象的拥有人 前台传 
			pd.put("creat_time", date);
			
			String token = pd.getString("token");//客户端发来的token
			String message = "token为空";
			int code = 401;
			long time = 0;
			if (token!=null) {//token存在，开始校验token是否过期
				try {
					message = "token已过期";//假设已过期  
					pd.put("phone", token.split("-")[0]);
					PageData user = userService.findUserByUPhone(pd);//根据token中的手机信息识别出用户
					String userToken = user.getString("token");//用户的实际token
					time = Long.valueOf(token.split("-")[1]);//获取时间戳
					long now = new Date().getTime();
					long hours = (now-time)/1000/3600;
					
					if (hours<(24*30)&&token.equals(userToken)) {//不超过30天且客户端和服务端token一致，未过期
						code = 200;message="ok";
						//===================业务逻辑==================================
						userActionService.saveUserAction(pd);
						data.put("id", pd.getString("id"));
						respJson.put("data", data);
						//===================业务逻辑==================================
					}
				} catch (Exception e) {
					logger.error(e.toString(), e);
					code = 500;
					message = "程序异常";
				}
			}
			respJson.put("code", code);
			respJson.put("message", message);
			return respJson;
		}
}
