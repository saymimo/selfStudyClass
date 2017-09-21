package com.ssc.controller.system.test;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ssc.controller.base.BaseController;

@Controller
@RequestMapping(value="/api/v1")
public class TestController extends BaseController {
	public Object test(){
		return  null;
	}
}
