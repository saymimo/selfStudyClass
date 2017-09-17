package com.ssc.controller.system.anthology;

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
	public Object findAnthologyList(@RequestBody String req){
		JSONObject respJson = new JSONObject();
		JSONObject data = new JSONObject();
		List<PageData> anthologyList = new ArrayList<PageData>();
		List<PageData> anthologyList2 = new ArrayList<PageData>();
		PageData pd = new PageData();
		pd = getPdFromJson(req);
		String message = "ok";
		int code = 200;
		pd.put("user_id", pd.getString("memberId"));
		try {
			anthologyList = anthologyService.findAnthologyListByUid(pd);
			if (anthologyList!=null&&!anthologyList.isEmpty()) {
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
			}
		} catch (Exception e) {
			logger.error(e.toString(), e);
			code = 500;
			message = "error";
		}
		data.put("anthologies", JSONArray.fromObject(anthologyList2));
		respJson.put("code", code);
		respJson.put("message", message);
		respJson.put("data", data);
		return respJson;
	}
	/**
	 * 查询某文集
	 * 2017-9-16 zxk_senNy
	 * @param req
	 * @return
	 */
	@RequestMapping(value="/findAnthology",method=RequestMethod.GET)
	public Object findAnthology(@RequestBody String req){
		JSONObject respJson = new JSONObject();
		PageData anthology = new PageData();
		List<PageData> articles = new ArrayList<PageData>();
		PageData author = new PageData();
		PageData pd = new PageData();
		pd = getPdFromJson(req);
		String message = "ok";
		int code = 200;
		pd.put("anthology_id", pd.getString("anthologyId"));
		try {
			anthology = anthologyService.findAnthologyAndAuthorById(pd);
			articles = anthologyService.findArticlesByAnthologyId(pd);
			
			author.put("id", anthology.getString("create_by"));
			author.put("name", anthology.getString("name"));
			author.put("nickname", anthology.getString("nickname"));
			anthology.remove("create_by");
			anthology.remove("name");
			anthology.remove("nickname");
			
			anthology.put("author", author);
			anthology.put("article", JSONArray.fromObject(articles));
		} catch (Exception e) {
			logger.error(e.toString(), e);
			code = 500;
			message = "error";
		}
		respJson.put("code", code);
		respJson.put("message", message);
		respJson.put("data", JSONObject.fromObject(anthology));
		return respJson;
	}
	/**
	 * 新增文集
	 * 2017-9-17 zxk_senNy
	 * @param req
	 * @return
	 */
	@RequestMapping(value="/add",method=RequestMethod.POST)
	public Object addAnthology(@RequestBody String req){
		JSONObject respJson = new JSONObject();
		JSONObject data = new JSONObject();
		PageData author = new PageData();
		PageData pd = new PageData();
		pd = getPdFromJson(req);
		String anthology_id = get32UUID();
		Date date = new Date();
		String message = "ok";
		int code = 200;
		pd.put("anthology_id", anthology_id);
		pd.put("create_by", pd.getString("authorId"));
		pd.put("create_time", date);
		pd.put("is_del", 0);
		try {
			anthologyService.saveAnthology(pd);
			pd.put("user_id", pd.getString("authorId"));
			author = userService.findUserByUid(pd);
			data.put("id", anthology_id);
			data.put("title", pd.getString("title"));
			data.put("author", JSONObject.fromObject(author));
		} catch (Exception e) {
			logger.error(e.toString(), e);
			code = 500;
			message = "error";
		}
		respJson.put("code", code);
		respJson.put("message", message);
		respJson.put("data", data);
		return respJson;
	}
	/**
	 * 更新文集名称
	 * 2017-9-17 zxk_senNy
	 * @param req
	 * @return
	 */
	@RequestMapping(value="/update",method=RequestMethod.PUT)
	public Object updateAnthology(@RequestBody String req){
		JSONObject respJson = new JSONObject();
		JSONObject data = new JSONObject();
		PageData author = new PageData();
		PageData pd = new PageData();
		pd = getPdFromJson(req);
		String message = "ok";
		int code = 200;
		pd.put("anthology_id", pd.getString("id"));
		try {
			anthologyService.updateAnthologyById(pd);
			author = anthologyService.findAnthologyAndAuthorById(pd);
			author.put("id", author.getString("create_by"));
			author.remove("id");
			author.remove("title");
			author.remove("create_by");
			data.put("id", pd.getString("id"));
			data.put("title", pd.getString("title"));
			data.put("author", JSONObject.fromObject(author));
		} catch (Exception e) {
			logger.error(e.toString(), e);
			code = 500;
			message = "error";
		}
		respJson.put("code", code);
		respJson.put("message", message);
		respJson.put("data", data);
		return respJson;
	}
	/**
	 * 删除文集
	 * 2017-9-17 zxk_senNy
	 * @param req
	 * @return
	 */
	@RequestMapping(value="/delete",method=RequestMethod.DELETE)
	public Object deleteAnthology(@RequestBody String req){
		JSONObject respJson = new JSONObject();
		PageData pd = new PageData();
		pd = getPdFromJson(req);
		String message = "ok";
		int code = 200;
		pd.put("anthology_id", pd.getString("id"));
		pd.put("is_del", 1);
		try {
			anthologyService.updateAnthologyById(pd);
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
