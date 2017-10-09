package com.ssc.controller.system.anthology;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.shiro.session.Session;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ssc.controller.base.BaseController;
import com.ssc.service.system.anthology.AnthologyService;
import com.ssc.service.system.user.UserService;
import com.ssc.util.PageData;

@Controller
@RequestMapping("/api/v1/anthology")
public class AnthologyController extends BaseController {
	@Resource(name="anthologyService")
	private AnthologyService anthologyService;
	
	@Resource(name="userService")
	private UserService userService;
	
	/**
	 * 批量查询文集
	 * 2017-9-16 zxk_senNy
	 * @param req
	 * @return
	 */
	@RequestMapping(value="/List",method=RequestMethod.GET)
	@ResponseBody
	public Object findAnthologyList(@RequestBody String req){
		JSONObject respJson = new JSONObject();
		JSONObject data = new JSONObject();
		List<PageData> anthologyList = new ArrayList<PageData>();
		List<PageData> anthologyList2 = new ArrayList<PageData>();
		PageData pd = new PageData();
		pd = getPageData();
		pd.put("user_id", pd.getString("memberId"));
		String message = "token为空";
		int code = 401;
		
		String token = pd.getString("token");//客户端发来的token
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
					//===================业务逻辑==================================
					code = 205;message = "用户没有文集";
					anthologyList = anthologyService.findAnthologyListByUid(pd);
					if (anthologyList!=null&&!anthologyList.isEmpty()) {//文集存在
						for (int i = 0; i < anthologyList.size(); i++) {
							PageData anthology = new PageData();
							JSONObject author = new JSONObject();
							anthology = anthologyList.get(i);
							author.put("id", anthology.getString("user_id"));
							author.put("name", anthology.getString("name"));
							author.put("nickname", anthology.getString("nickname"));
							anthology.remove("user_id");
							anthology.remove("name");
							anthology.remove("nickname");
							anthology.put("author", author);
							anthologyList2.add(anthology);
						}
						code = 200;message = "ok";
						data.put("anthologies", JSONArray.fromObject(anthologyList2));
						respJson.put("data", data);
					}
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
	 * 查询某文集
	 * 2017-9-16 zxk_senNy
	 * @param req
	 * @return
	 */
	@RequestMapping(value="/findAnthology",method=RequestMethod.GET)
	@ResponseBody
	public Object findAnthology(){
		JSONObject respJson = new JSONObject();
		PageData anthology = new PageData();
		List<PageData> articles = new ArrayList<PageData>();
		List<PageData> articles2 = new ArrayList<PageData>();
		PageData author = new PageData();
		PageData pd = new PageData();
		pd = getPageData();
		pd.put("anthology_id", pd.getString("anthologyId"));
		
		
		
		String message = "token为空";
		int code = 401;
		String token = pd.getString("token");//客户端发来的token
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
					//===================业务逻辑==================================
					anthology = anthologyService.findAnthologyAndAuthorById(pd);
					articles = anthologyService.findArticlesByAnthologyId(pd);
					
					author.put("id", anthology.getString("create_by"));
					author.put("name", anthology.getString("name"));
					author.put("nickname", anthology.getString("nickname"));
					anthology.remove("create_by");
					anthology.remove("name");
					anthology.remove("nickname");
					
					anthology.put("author", author);
					if (articles!=null&&!articles.isEmpty()) {
						for (int i = 0; i < articles.size(); i++) {
							PageData article = new PageData();
							article = articles.get(i);
							article.put("isPublish", (Integer)article.get("is_publish")==1?true:false);
							article.remove("is_publish");
							articles2.add(article);
						}
					}
					code = 200;message="ok";
					anthology.put("article", JSONArray.fromObject(articles2));
					respJson.put("data", JSONObject.fromObject(anthology));
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
	 * 新增文集
	 * 2017-9-17 zxk_senNy
	 * @param req
	 * @return
	 */
	@RequestMapping(value="/add",method=RequestMethod.POST)
	@ResponseBody
	public Object addAnthology(@RequestBody String req){
		JSONObject respJson = new JSONObject();
		JSONObject data = new JSONObject();
		PageData author = new PageData();
		PageData pd = new PageData();
		pd = getPdFromJson(req);
		String anthology_id = get32UUID();
		Date date = new Date();
		pd.put("anthology_id", anthology_id);
		pd.put("create_by", pd.getString("authorId"));
		pd.put("create_time", date);
		pd.put("is_del", 0);
		
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
					anthologyService.saveAnthology(pd);
					pd.put("user_id", pd.getString("authorId"));
					author = userService.findUserByUid(pd);
					data.put("id", anthology_id);
					data.put("title", pd.getString("title"));
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
	 * 更新文集名称
	 * 2017-9-17 zxk_senNy
	 * @param req
	 * @return
	 */
	@RequestMapping(value="/update",method=RequestMethod.PUT)
	@ResponseBody
	public Object updateAnthology(@RequestBody String req){
		JSONObject respJson = new JSONObject();
		JSONObject data = new JSONObject();
		PageData author = new PageData();
		PageData pd = new PageData();
		pd = getPdFromJson(req);
		pd.put("anthology_id", pd.getString("id"));
		
		String message = "token为空";
		int code = 401;
		String token = pd.getString("token");//客户端发来的token
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
					//===================业务逻辑==================================
					anthologyService.updateAnthologyById(pd);
					author = anthologyService.findAnthologyAndAuthorById(pd);
					author.put("id", author.getString("create_by"));
					author.remove("id");
					author.remove("title");
					author.remove("create_by");
					data.put("id", pd.getString("id"));
					data.put("title", pd.getString("title"));
					data.put("author", JSONObject.fromObject(author));
					respJson.put("data", data);
					code = 200;message="ok";
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
	 * 删除文集
	 * 2017-9-17 zxk_senNy
	 * @param req
	 * @return
	 */
	@RequestMapping(value="/delete",method=RequestMethod.DELETE)
	@ResponseBody
	public Object deleteAnthology(@RequestBody String req){
		JSONObject respJson = new JSONObject();
		PageData pd = new PageData();
		pd = getPdFromJson(req);
		pd.put("anthology_id", pd.getString("id"));
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
					anthologyService.deleteAnthology(pd);// 删除文集和文集下的文章 （非物理删除，实为更新）
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
