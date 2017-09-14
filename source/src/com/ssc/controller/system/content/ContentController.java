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

import com.ssc.controller.base.BaseController;
import com.ssc.entity.Page;
import com.ssc.service.system.content.ContentService;
import com.ssc.util.PageData;

@Controller
@RequestMapping(value="/api/v1/content")
public class ContentController extends BaseController {
	@Resource(name="contentService")
	private ContentService contentService;
	
	/**
	 * 根据日期批量查询内容
	 * 2017-9-14 zxk_senNy
	 * @param req
	 * @return
	 */
	@RequestMapping(value="/contentList",method=RequestMethod.GET)
	public Object contentList(@RequestBody String req){
		List<PageData> contentList = new ArrayList<PageData>();
		List<JSONObject> contentList2 = new ArrayList<JSONObject>();
		JSONObject data = new JSONObject();
		JSONObject respJson = new JSONObject();
		PageData pd = new PageData();
		pd = getPdFromJson(req);
		int code = 200;
		String message = "ok";
		
		int offset = (Integer)pd.get("offset")==null?0:(Integer)pd.get("offset");
		int limit = (Integer)pd.get("limit")==null?10:(Integer)pd.get("limit");
		pd.put("offset", offset);
		pd.put("limit", limit);
		try {
			contentList = contentService.findContentList(pd);
			if (contentList!=null&&!contentList.isEmpty()) {
				int num = contentList.size();//23
				int currentPage = offset/limit+1;
				data.put("totalPage", num/limit+1);
				data.put("currentPage",currentPage);
				data.put("pageStartIndex",(currentPage-1)*limit);
				data.put("pageEndIndex", currentPage*limit-1);
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
	public Object findContent(@RequestBody  String req){
		JSONObject respJson = new JSONObject();
		PageData pd = new PageData();
		pd = getPdFromJson(req);
		
		return null;
	}
}
