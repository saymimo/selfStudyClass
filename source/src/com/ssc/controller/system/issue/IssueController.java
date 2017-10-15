package com.ssc.controller.system.issue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ssc.controller.base.BaseController;
import com.ssc.service.system.comment.CommentService;
import com.ssc.service.system.content.ContentService;
import com.ssc.service.system.user.UserService;
import com.ssc.util.PageData;

@Controller
@RequestMapping(value="/api/v1/issue")
public class IssueController extends BaseController {
	@Resource(name="contentService")
	private ContentService contentService;
	
	@Resource(name="commentService")
	private CommentService commentService;
	
	@Resource(name="userService")
	private UserService userService;
	
	/**
	 * 新增问题
	 * 2017-9-17 zxk_senNy
	 * @param req
	 * @return
	 */
	@RequestMapping(value="/add",method=RequestMethod.POST)
	@ResponseBody
	public Object addIssue(@RequestBody String req){
		JSONObject respJson = new JSONObject();
		JSONObject author = new JSONObject();
		PageData content = new PageData();
		List<PageData> commentList = new ArrayList<PageData>();
		List<PageData> commentList2 = new ArrayList<PageData>();
		PageData pd = new PageData();
		pd = getPdFromJson(req);
		
		String content_id = get32UUID();
		Date date = new Date();
		pd.put("content_id", content_id);
		pd.put("parent_id", "");//话题ID 待传
		pd.put("create_by", pd.getString("authorId"));
		pd.put("create_time", date);
		pd.put("publish_time", date);
		pd.put("is_publish", 1);
		pd.put("type", 2);
		
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
					content = contentService.findContent(pd);
					pd.put("contentId", content_id);
					commentList = commentService.findCommentList(pd);
					if (commentList!=null&&!commentList.isEmpty()) {
						for (int i = 0; i < commentList.size(); i++) {
							PageData comment = new PageData();
							PageData comment_author = new PageData();
							comment = commentList.get(i);
							comment_author.put("id", comment.getString("user_id"));
							comment_author.put("avatar", comment.getString("avatar"));
							comment_author.put("name", comment.getString("name"));
							comment_author.put("nickname", comment.getString("nickname"));
							comment_author.put("unit", comment.getString("unit"));
							comment.put("author", JSONObject.fromObject(comment_author));
							comment.remove("user_id");
							comment.remove("avatar");
							comment.remove("name");
							comment.remove("nickname");
							comment.remove("unit");
							commentList2.add(comment);
						}
					}
					
					author.put("id", content.getString("user_id"));
					author.put("avatar", content.getString("avatar"));
					author.put("name", content.getString("name"));
					author.put("nickname", content.getString("nickname"));
					author.put("unit", content.getString("unit"));
					
					content.remove("user_id");
					content.remove("avatar");
					content.remove("name");
					content.remove("nickname");
					content.remove("unit");
					content.remove("anthology_id");
					content.remove("anthologyTitle");
					content.remove("collectNum");
					
					content.put("author", author);
					content.put("publishType", (Integer)pd.get("publishType"));
					content.put("praiseNum", content.get("answers_praiseNum"));
					content.remove("answers_praiseNum");
					content.put("comment", JSONArray.fromObject(commentList2));
					respJson.put("data", JSONObject.fromObject(content));
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
	 * 更新问题
	 * 2017-9-17 zxk_senNy
	 * @param req
	 * @return
	 */
	@RequestMapping(value="/update",method=RequestMethod.PUT)
	@ResponseBody
	public Object updateIssue(@RequestBody String req){
		JSONObject respJson = new JSONObject();
		PageData pd = new PageData();
		PageData content = new PageData();
		JSONObject author = new JSONObject();
		List<PageData> commentList = new ArrayList<PageData>();
		List<JSONObject> commentList2 = new ArrayList<JSONObject>();
		pd = getPdFromJson(req);
		
		Date date = new Date();
		pd.put("content_id", pd.getString("id"));
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
					content = contentService.findContent(pd);
					pd.put("contentId", pd.getString("id"));
					commentList = commentService.findCommentList(pd);
					if (commentList!=null&&!commentList.isEmpty()) {
						for (int i = 0; i < commentList.size(); i++) {
							PageData comment = new PageData();
							JSONObject comment_author = new JSONObject();
							comment = commentList.get(i);
							comment_author.put("id", comment.getString("user_id"));
							comment_author.put("avatar", comment.getString("avatar"));
							comment_author.put("name", comment.getString("name"));
							comment_author.put("nickname", comment.getString("nickname"));
							comment_author.put("unit", comment.getString("unit"));
							comment.put("author", comment_author);
							comment.remove("user_id");
							comment.remove("avatar");
							comment.remove("name");
							comment.remove("nickname");
							comment.remove("unit");
							commentList2.add(JSONObject.fromObject(comment));
						}
					}
					author.put("id", content.getString("user_id"));
					author.put("avatar", content.getString("avatar"));
					author.put("name", content.getString("name"));
					author.put("nickname", content.getString("nickname"));
					author.put("unit", content.getString("unit"));
					content.remove("user_id");
					content.remove("avatar");
					content.remove("name");
					content.remove("nickname");
					content.remove("unit");
					content.remove("anthology_id");
					content.remove("anthologyTitle");
					content.remove("collectNum");
					content.put("author", author);
					content.put("praiseNum", content.get("answers_praiseNum"));
					content.remove("answers_praiseNum");
					content.put("comment", JSONArray.fromObject(commentList2));
					
					respJson.put("data", JSONObject.fromObject(content));
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
	 * 删除问题
	 * 2017-9-17 zxk_senNy
	 * @param req
	 * @return
	 */
	@RequestMapping(value="/delete",method=RequestMethod.DELETE)
	@ResponseBody
	public Object deleteIssue(@RequestBody String req){
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
