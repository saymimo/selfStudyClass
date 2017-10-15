package com.ssc.controller.system.article;

import com.ssc.controller.base.BaseController;
import com.ssc.service.system.anthology.AnthologyService;
import com.ssc.service.system.content.ContentService;
import com.ssc.service.system.user.UserService;
import com.ssc.service.system.userAction.UserActionService;
import com.ssc.util.PageData;
import com.sun.org.apache.bcel.internal.generic.NEW;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping({"/api/v1/article"})
public class ArticleController extends BaseController{
  
  @Resource(name="contentService")
  private ContentService contentService;
  
  @Resource(name="userService")
  private UserService userService;
	
  @Resource(name="anthologyService")
  private AnthologyService anthologyService;
  
  /**
   * 新建文章
   * 2017-9-11 zxk_senNy
   * @param req
   * @return
   */
  @RequestMapping(value="/add",method=RequestMethod.POST)
  @ResponseBody
  public Object addArticle(@RequestBody String req){
	JSONObject respJson = new JSONObject();
	JSONObject data = new JSONObject();
	PageData author = new PageData();
	PageData anthology = new PageData();
    PageData pd = new PageData();
    pd = getPdFromJson(req);
    
    String content_id = get32UUID();
    Date date = new Date();
    pd.put("content_id", content_id);
    pd.put("create_by", pd.getString("authorId"));
    pd.put("create_time", date);
    pd.put("anthology_id", pd.getString("anthologyId"));
    
    
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
				contentService.saveContent(pd);
		    	pd.put("user_id", pd.getString("authorId"));
		    	author = userService.findUserByUid(pd);
		    	anthology = anthologyService.findAnthologyById(pd);
		    	
		    	data.put("id", content_id);
		        data.put("isPublish", false);
		        data.put("publishType", 0);//还没发布，没有发布类型
		        data.put("type", 1);
		        data.put("createTime", Integer.parseInt(String.valueOf(date.getTime())));
		        data.put("updateTime", 0);
		        data.put("status", 1);
		        data.put("title", pd.getString("title"));
		        data.put("content", pd.getString("content"));
		        data.put("collectNum", 0);
		        data.put("praiseNum", 0);
		        data.put("anthology", JSONObject.fromObject(anthology));
		        data.put("author", JSONObject.fromObject(author));
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
  /**
   * 发布文章
   * 2017-9-11 zxk_senNy
   * @param req
   * @return
   */
  @RequestMapping(value="/publish",method=RequestMethod.POST)
  @ResponseBody
  public Object publish(@RequestBody String req){
	  JSONObject respJson = new JSONObject();
	  JSONObject data = new JSONObject();
	  JSONObject author = new JSONObject();
	  JSONObject anthology = new JSONObject();
	  PageData pd = new PageData();
	  PageData article = new PageData();
	  pd = getPdFromJson(req);
	  
	  Date date = new Date();
	  pd.put("content_id", pd.getString("id"));
	  pd.put("parent_id", "");//话题ID 待传
	  pd.put("is_publish", 1);
	  pd.put("publish_time", date);
	  pd.put("publish_type", (Integer)pd.get("publishType"));
      
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
			  contentService.updateByid(pd);
	    	  article = contentService.findContent(pd);
	    	  
	    	  author.put("id", article.getString("user_id"));
	          author.put("avatar", article.getString("avatar"));
	          author.put("name", article.getString("name"));
	          author.put("nickname", article.getString("nickname"));
	          author.put("unit", article.getString("unit"));
	          
	          anthology.put("id", article.getString("anthology_id"));
	          anthology.put("title", article.getString("anthologyTitle"));
	          
	    	  data.put("id", pd.getString("id"));
	    	  data.put("isPublish", true);
	    	  data.put("publishType", (Integer)pd.get("publishType"));
	    	  data.put("type",1);
	    	  data.put("createTime",date.getTime());
	    	  data.put("updateTime",article.get("updateTime"));
	    	  data.put("status",1);
	    	  data.put("title",article.getString("title"));
	    	  data.put("content",article.getString("content"));
	    	  data.put("collectNum",0);
	    	  data.put("praiseNum",0);
	    	  data.put("author", author);
	    	  data.put("anthology", anthology);
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
  /**
   * 更新文章
   * 2017-9-11 zxk_senNy
   * @return
   */
  @RequestMapping(value="/update",method=RequestMethod.PUT)
  @ResponseBody
  public Object update(@RequestBody String req){
	  JSONObject respJson = new JSONObject();
	  JSONObject data = new JSONObject();
	  JSONObject author = new JSONObject();
	  JSONObject anthology = new JSONObject();
	  PageData article = new PageData();
      PageData pd = new PageData();
      pd = getPdFromJson(req);
      
      Date date = new Date();
      pd.put("content_id", pd.getString("id"));
      pd.put("anthology_id", pd.getString("anthologyId"));
      pd.put("update_time", date);
      
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
      			contentService.updateByid(pd);
      			article = contentService.findContent(pd);
      			
      			author.put("id", article.getString("user_id"));
      			author.put("avatar", article.getString("avatar"));
      			author.put("name", article.getString("name"));
      			author.put("nickname", article.getString("nickname"));
      			author.put("unit", article.getString("unit"));
      			
      			anthology.put("id", article.getString("anthology_id"));
      			anthology.put("title", article.getString("anthologyTitle"));
      			
      			data.put("id", pd.getString("id"));
      			data.put("isPublish", (Integer)article.get("is_publish")==1?true:false);
      			data.put("publishType", (Integer)article.get("publishType"));
      			data.put("type", (Integer)article.get("type"));
      			data.put("createTime",article.getString("createTime"));
      			data.put("updateTime",article.getString("updateTime"));
      			data.put("status",(Integer)article.get("status"));
      			data.put("title", article.getString("title"));
      			data.put("content",article.getString("content"));
      			data.put("collectNum",article.get("collectNum"));
      			data.put("praiseNum", article.get("praiseNum"));
      			data.put("author", author);
      			data.put("anthology", anthology);
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
	  /**
	   * 删除文章
	   * 2017-9-30 zxk_senNy
	   * @param req
	   * @return
	   */
  	@RequestMapping(value="/delete",method=RequestMethod.DELETE)
  	@ResponseBody
	public Object deleteArticle(@RequestBody String req){
		JSONObject respJson = new JSONObject();
		PageData pd = new PageData();
		pd = getPdFromJson(req);
		
		pd.put("content_id", pd.getString("id"));
		pd.put("is_del", 1);
		
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
					contentService.updateByid(pd);
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
