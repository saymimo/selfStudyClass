package com.ssc.controller.system.test;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ssc.controller.base.BaseController;
import com.ssc.util.GetIpUtil;

@Controller
@RequestMapping(value="/api/v1")
public class TestController extends BaseController {
	
	/**
	 * 网络测试接口
	 * 2017-9-21 zxk_senNy
	 * @return
	 */
	@RequestMapping(value="/test",method=RequestMethod.GET)
	@ResponseBody
	public Object test(HttpServletRequest request){
		JSONObject respJson = new JSONObject();
		JSONObject data = new JSONObject();
		String message = "ok";
		int code = 200;
		String requestHost = GetIpUtil.getIpAddress(request);
		data.put("requestHost", requestHost);
		respJson.put("code", code);
		respJson.put("message", message);
		respJson.put("data", data);
		return  respJson;
	}
}
