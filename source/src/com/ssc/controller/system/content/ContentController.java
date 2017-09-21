package com.ssc.controller.system.content;
import java.util.ArrayList;
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
import com.ssc.service.system.anthology.AnthologyService;
import com.ssc.service.system.comment.CommentService;
import com.ssc.service.system.content.ContentService;
import com.ssc.service.system.user.UserService;
import com.ssc.util.PageData;

@Controller
@RequestMapping(value="/api/v1/content")
public class ContentController extends BaseController {
	@Resource(name="contentService")
	private ContentService contentService;
	
	@Resource(name="commentService")
	private CommentService commentService;
	
	@Resource(name="userService")
	private UserService userService;
	
	@Resource(name="anthologyService")
	private AnthologyService anthologyService;
	
	/**
	 * 根据日期批量查询内容
	 * 2017-9-14 zxk_senNy
	 * @param req
	 * @return
	 */
	@RequestMapping(value="/contentList",method=RequestMethod.GET)
	@ResponseBody
	public Object contentList(){
		List<PageData> contentList = new ArrayList<PageData>();
		List<JSONObject> contentList2 = new ArrayList<JSONObject>();
		JSONObject data = new JSONObject();
		JSONObject respJson = new JSONObject();
		PageData pd = new PageData();
		pd = getPageData();
		int code = 200;
		String message = "ok";
		
		int offset = (Integer)pd.get("offset")==null?0:(Integer)pd.get("offset");
		int limit = (Integer)pd.get("limit")==null?10:(Integer)pd.get("limit");
		pd.put("offset", offset);
		pd.put("limit", limit);
		try {
			contentList = contentService.findContentList(pd);
			int num = contentList.size();//23
			int currentPage = offset/limit+1;
			data.put("totalPage", num/limit+1);
			data.put("currentPage",currentPage);
			data.put("pageStartIndex",(currentPage-1)*limit);
			data.put("pageEndIndex", currentPage*limit-1);
			if (contentList!=null&&!contentList.isEmpty()) {
				for (int i = 0; i < num; i++) {
					PageData content = new PageData();
					content = contentList.get(i);
					//===============作者======================
					JSONObject author = new JSONObject();
					if ((Integer)content.get("publishType")==1) {//真名发布
						author.put("id", content.getString("user_id"));
						author.put("avator", content.getString("avator"));
						author.put("name", content.getString("name"));
						author.put("unit", content.getString("unit"));
					}
					if ((Integer)content.get("publishType")==2) {//昵称
						author.put("id", content.getString("user_id"));
						author.put("avator", content.getString("avator"));
						author.put("nickname", content.getString("nickname"));
					}
					if ((Integer)content.get("publishType")==3) {//匿名
						author.put("id", -1);
					}
					content.put("author",author);
					//===============作者======================
					
					//===============文集======================
					JSONObject anthology = new JSONObject();
					if ((Integer)content.get("type")==1) {//若文章，放入文集，去掉关注数
						anthology.put("id", content.getString("anthology_id"));
						anthology.put("title", content.getString("anthologyTitle"));
						content.put("anthology", anthology);
						content.remove("attentionNum");
					}
					//===============文集======================
					
					if ((Integer)content.get("type")==2) {//若问题，去掉收藏数和赞数
						content.remove("collectNum");
						content.remove("praiseNum");
					}
					
					content.remove("user_id");
					content.remove("avator");
					content.remove("name");
					content.remove("nickname");
					content.remove("unit");
					content.remove("anthology_id");
					content.remove("anthologyTitle");
					contentList2.add(JSONObject.fromObject(content));
				}
				data.put("content", JSONArray.fromObject(contentList2));
			}
		} catch (Exception e) {
			logger.error(e.toString(),e);
			message = "error";
			code = 500;
		}
		respJson.put("code", code);
		respJson.put("message", message);
		respJson.put("data", data);
		return respJson;
	}
	
	/**
	 * 查询某条内容
	 * 2017-9-14 zxk_senNy
	 * @return
	 */
	@RequestMapping(value="/findContent",method=RequestMethod.GET)
	@ResponseBody
	public Object findContent(){
		JSONObject respJson = new JSONObject();
		JSONObject author = new JSONObject();
		JSONObject anthology = new JSONObject();
		List<PageData> commentList = new ArrayList<PageData>();
		List<JSONObject> commentList2 = new ArrayList<JSONObject>();
		PageData pd = new PageData();
		PageData content = new PageData();
		pd = getPageData();
		String message = "ok";
		int code = 200;
		try {
			pd.put("content_id", pd.getString("contentId"));
			content = contentService.findContent(pd);
			if ((Integer)content.get("publishType")==1) {//真名发布
				author.put("id", content.getString("user_id"));
				author.put("avator", content.getString("avator"));
				author.put("name", content.getString("name"));
				author.put("unit", content.getString("unit"));
			}
			if ((Integer)content.get("publishType")==2) {//昵称
				author.put("id", content.getString("user_id"));
				author.put("avator", content.getString("avator"));
				author.put("nickname", content.getString("nickname"));
			}
			if ((Integer)content.get("publishType")==3) {//匿名
				author.put("id", -1);
			}
			content.put("author", author);
			//===============文章======================
			if ((Integer)content.get("type")==1) {//若文章，放入文集，去掉关注数
				anthology.put("id", content.getString("anthology_id"));
				anthology.put("title", content.getString("anthologyTitle"));
				content.put("anthology", anthology);
				content.remove("attentionNum");
				content.remove("answers_praiseNum");
				content.remove("answerNum");
			}
			//===============文章======================
			
			//===============问题======================
			if ((Integer)content.get("type")==2) {//若问题，去掉收藏数
				content.remove("collectNum");
				content.put("praiseNum", content.get("answers_praiseNum"));//这里的赞数是指该问题下所有回答赞数的总和
			}
			//===============问题======================

			commentList = commentService.findCommentList(pd);//评论或者回答
			if (commentList!=null&&!commentList.isEmpty()) {
				for (int i = 0; i < commentList.size(); i++) {
					PageData comment = new PageData();
					JSONObject c_author = new JSONObject();//评论回答的作者
					comment = commentList.get(i);
					
					if ((Integer)comment.get("publishType")==1) {
						c_author.put("id", comment.getString("user_id"));
						c_author.put("avatar", comment.getString("avatar"));
						c_author.put("name", comment.getString("name"));
						c_author.put("unit", comment.getString("unit"));
					}
					if ((Integer)comment.get("publishType")==2) {
						c_author.put("id", comment.getString("user_id"));
						c_author.put("avatar", comment.getString("avatar"));
						c_author.put("nickname", comment.getString("nickname"));
					}
					if ((Integer)comment.get("publishType")==3) {
						c_author.put("id",-1);
					}
					comment.put("author", c_author);
					comment.remove("user_id");
					comment.remove("avatar");
					comment.remove("name");
					comment.remove("unit");
					commentList2.add(JSONObject.fromObject(comment));
				}
			}
			content.put("comment", JSONArray.fromObject(commentList2));
			//是否发布 1已发布
			content.put("isPublish", (Integer)content.get("is_publish")==1?true:false);
			content.remove("user_id");
			content.remove("avator");
			content.remove("name");
			content.remove("nickname");
			content.remove("unit");
			content.remove("anthology_id");
			content.remove("anthologyTitle");
			content.remove("is_publish");
		} catch (Exception e) {
			logger.error(e.toString(), e);
			code = 500;
			message = "error";
		}
		respJson.put("code", code);
		respJson.put("message", message);
		respJson.put("data", JSONObject.fromObject(content));
		return respJson;
	}
	
	
	  
	  
}
