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

import com.ssc.controller.base.BaseController;
import com.ssc.service.system.comment.CommentService;
import com.ssc.service.system.content.ContentService;
import com.ssc.util.PageData;

@Controller(value="/api/v1/issue")
public class IssueController extends BaseController {
	@Resource(name="contentService")
	private ContentService contentService;
	
	@Resource(name="commentService")
	private CommentService commentService;
	
	/**
	 * 新增问题
	 * 2017-9-17 zxk_senNy
	 * @param req
	 * @return
	 */
	@RequestMapping(value="/addIssue",method=RequestMethod.GET)
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
		String message = "ok";
		int code = 200;
		
		pd.put("content_id", content_id);
		pd.put("parent_id", "");//板块ID 待定
		pd.put("create_by", pd.getString("authorId"));
		pd.put("create_time", date);
		pd.put("type", 2);
		try {
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
			
			
		} catch (Exception e) {
			logger.error(e.toString(), e);
			code = 500;
			message = "error";
		}
		content.put("author", author);
		content.put("publishType", 0);
		content.put("praiseNum", (Integer)content.get("answers_praiseNum"));
		content.remove("answers_praiseNum");
		content.put("comment", JSONArray.fromObject(commentList2));
		respJson.put("code", code);
		respJson.put("message", message);
		respJson.put("data", JSONObject.fromObject(content));
		return respJson;
	}
	/**
	 * 更新问题
	 * 2017-9-17 zxk_senNy
	 * @param req
	 * @return
	 */
	@RequestMapping(value="/updateIssue",method=RequestMethod.PUT)
	public Object updateIssue(@RequestBody String req){
		JSONObject respJson = new JSONObject();
		PageData pd = new PageData();
		PageData content = new PageData();
		PageData author = new PageData();
		List<PageData> commentList = new ArrayList<PageData>();
		List<PageData> commentList2 = new ArrayList<PageData>();
		pd = getPdFromJson(req);
		String message = "ok";
		Date date = new Date();
		int code = 200;
		pd.put("content_id", pd.getString("id"));
		pd.put("update_time", date);
		try {
			contentService.updateByid(pd);
			content = contentService.findContent(pd);
			pd.put("contentId", pd.getString("id"));
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
		} catch (Exception e) {
			logger.error(e.toString(), e);
			code = 500;
			message = "error";
		}
		content.put("author", author);
		content.put("praiseNum", (Integer)content.get("answers_praiseNum"));
		content.remove("answers_praiseNum");
		content.put("comment", JSONArray.fromObject(commentList2));
		respJson.put("code", code);
		respJson.put("message", message);
		respJson.put("data", JSONObject.fromObject(content));
		return respJson;
	}
	/**
	 * 删除问题
	 * 2017-9-17 zxk_senNy
	 * @param req
	 * @return
	 */
	@RequestMapping(value="/deleteIssue",method=RequestMethod.DELETE)
	public Object deleteIssue(@RequestBody String req){
		JSONObject respJson = new JSONObject();
		PageData pd = new PageData();
		pd = getPdFromJson(req);
		String message = "ok";
		int code = 200;
		pd.put("content_id", pd.getString("id"));
		pd.put("is_del", 1);
		try {
			contentService.updateByid(pd);
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
