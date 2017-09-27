package com.ssc.controller.system.comment;

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
import com.ssc.service.system.user.UserService;
import com.ssc.util.PageData;

@Controller
@RequestMapping(value="/api/v1/comment")
public class CommentController extends BaseController {
	@Resource(name="commentService")
	private CommentService commentService;
	
	@Resource(name="userService")
	private UserService userService;
	
	/**
	 * 查找某条评论
	 * 2017-9-17 zxk_senNy
	 * @param req
	 * @return
	 */
	@RequestMapping(value="/findComment",method=RequestMethod.GET)
	@ResponseBody
	public Object findComment(){
		JSONObject respJson = new JSONObject();
		List<PageData> discussList = new ArrayList<PageData>();
		List<JSONObject> discussList2 = new ArrayList<JSONObject>();
		PageData pd = new PageData();
		PageData comment = new PageData();
		JSONObject author = new JSONObject();
		pd = getPageData();
		String message = "ok";
		int code = 200;
		try {
			comment = commentService.findComment(pd);
			pd.put("contentId", pd.getString("comment_id"));
			discussList = commentService.findCommentList(pd);
			if (discussList!=null&&!discussList.isEmpty()) {
				for (int i = 0; i < discussList.size(); i++) {
					PageData discuss = new PageData();
					JSONObject discuss_author = new JSONObject();
					discuss = discussList.get(i);
					discuss_author.put("id", discuss.getString("user_id"));
					discuss_author.put("avatar", discuss.getString("avatar"));
					discuss_author.put("name", discuss.getString("name"));
					discuss_author.put("nickname", discuss.getString("nickname"));
					discuss_author.put("unit", discuss.getString("unit"));
					discuss.remove("user_id");
					discuss.remove("avatar");
					discuss.remove("name");
					discuss.remove("nickname");
					discuss.remove("unit");
					discuss.remove("discussNum");
					discuss.put("author", discuss_author);
					discussList2.add(JSONObject.fromObject(discuss));
				}
			}
			author.put("id", comment.getString("user_id"));
			author.put("avatar", comment.getString("avatar"));
			author.put("name", comment.getString("name"));
			author.put("nickname", comment.getString("nickname"));
			author.put("unit", comment.getString("unit"));
			comment.put("author", author);
			comment.put("discuss", JSONArray.fromObject(discussList2));
			comment.remove("user_id");
			comment.remove("avatar");
			comment.remove("name");
			comment.remove("nickname");
			comment.remove("unit");
			respJson.put("data", JSONObject.fromObject(comment));
		} catch (Exception e) {
			logger.error(e.toString(), e);
			code = 500;
			message = "error";
		}
		
		respJson.put("code", code);
		respJson.put("message", message);
		return respJson;
	}
	/**
	 * 发布评论
	 * 2017-9-17 zxk_senNy
	 * @param req
	 * @return
	 */
	@RequestMapping(value="/publish",method=RequestMethod.POST)
	@ResponseBody
	public Object publishComment(@RequestBody String req){
		JSONObject respJson = new JSONObject();
		JSONObject data = new JSONObject();
		PageData author = new PageData();
		PageData pd = new PageData();
		pd = getPdFromJson(req);
		String message = "ok";
		String comment_id = get32UUID();
		Date date = new Date();
		int code = 200;
		pd.put("comment_id", comment_id);
		pd.put("parent_id", pd.getString("target"));
		pd.put("create_by", pd.getString("authorId"));
		pd.put("create_time", date);
		pd.put("comment_type", (Integer)pd.get("type"));
		pd.put("publish_type", pd.get("publishType"));
		pd.put("publish_time", date);
		pd.put("is_publish", 1);
		try {
			commentService.saveComment(pd);
			pd.put("user_id", pd.getString("authorId"));
			author = userService.findUserByUid(pd);
			data.put("id", comment_id);
			data.put("author", JSONObject.fromObject(author));
			data.put("target", pd.getString("target"));
			data.put("type", (Integer)pd.get("type"));
			data.put("createTime", date.getTime()/1000);
			data.put("content", pd.getString("content"));
			data.put("praiseNum", 0);
			data.put("discussNum", 0);
			data.put("publishType", (Integer)pd.get("publishType"));
			respJson.put("data", data);
		} catch (Exception e) {
			logger.error(e.toString(), e);
			code = 500;
			message = "error";
		}
		
		respJson.put("code", code);
		respJson.put("message", message);
		return respJson;
	}
	/**
	 * 更新评论
	 * 2017-9-17 zxk_senNy
	 * @param req
	 * @return
	 */
	@RequestMapping(value="/update",method=RequestMethod.PUT)
	@ResponseBody
	public Object update(@RequestBody String req){
		JSONObject respJson = new JSONObject();
		JSONObject author = new JSONObject();
		PageData comment = new PageData();
		PageData pd = new PageData();
		pd = getPdFromJson(req);
		String message = "ok";
		int code = 200;
		pd.put("comment_id", pd.getString("id"));
		try {
			commentService.updateByid(pd);
			comment = commentService.findComment(pd);
			author.put("id", comment.getString("user_id"));
			author.put("avatar", comment.getString("avatar"));
			author.put("name", comment.getString("name"));
			author.put("nickname", comment.getString("nickname"));
			author.put("unit", comment.getString("unit"));
			comment.put("author", author);
			comment.remove("user_id");
			comment.remove("avatar");
			comment.remove("name");
			comment.remove("nickname");
			comment.remove("unit");
			respJson.put("data", JSONObject.fromObject(comment));
		} catch (Exception e) {
			logger.error(e.toString(), e);
			code = 500;
			message = "error";
		}
		respJson.put("code", code);
		respJson.put("message", message);
		return respJson;
	}
	/**
	 * 删除评论
	 * 2017-9-17 zxk_senNy
	 * @param req
	 * @return
	 */
	@RequestMapping(value="/delete",method=RequestMethod.DELETE)
	@ResponseBody
	public Object deleteComment(@RequestBody String req){
		JSONObject respJson = new JSONObject();
		PageData pd = new PageData();
		pd = getPdFromJson(req);
		String message = "ok";
		int code = 200;
		pd.put("comment_id", pd.getString("id"));
		pd.put("is_del", 1);
		try {
			commentService.updateByid(pd);
		} catch (Exception e) {
			logger.error(e.toString(), e);
			code = 500;
			message = "error";
		}
		respJson.put("code", code);
		respJson.put("message", message);
		return respJson;
	}
}
