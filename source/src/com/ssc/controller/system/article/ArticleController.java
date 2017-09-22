package com.ssc.controller.system.article;

import com.ssc.controller.base.BaseController;
import com.ssc.service.system.anthology.AnthologyService;
import com.ssc.service.system.content.ContentService;
import com.ssc.service.system.user.UserService;
import com.ssc.util.PageData;

import java.util.Date;
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
    int code = 200;
    String message = "ok";
    String content_id = get32UUID();
    Date date = new Date();
    pd.put("content_id", content_id);
    pd.put("create_by", pd.getString("authorId"));
    pd.put("create_time", date);
    pd.put("anthology_id", pd.getString("anthologyId"));
    try{
    	contentService.saveContent(pd);
    	pd.put("user_id", pd.getString("authorId"));
    	author = userService.findUserByUid(pd);
    	anthology = anthologyService.findAnthologyById(pd);
    }catch (Exception e){
      logger.error(e.toString(), e);
      code = 500;
      message = "saveArticle error!";
    }
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
    
    respJson.put("code", Integer.valueOf(code));
    respJson.put("message", message);
    respJson.put("data", data);
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
      int code = 200;
      String message = "ok";
      Date date = new Date();
      pd.put("content_id", pd.getString("id"));
      pd.put("is_publish", 1);
      pd.put("publish_time", date);
      pd.put("publish_type", (Integer)pd.get("publishType"));
      try {
    	  contentService.updateByid(pd);
    	  article = contentService.findContent(pd);
      } catch (Exception e) {
		logger.error(e.toString(), e);
		code = 500;
		message = "contentService update error";
      }
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
	  
	  respJson.put("code", code);
	  respJson.put("message", message);
	  respJson.put("data", data);
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
      int code = 200;
      String message = "ok";
      pd.put("content_id", pd.getString("id"));
      pd.put("update_time", date);
      try {
		contentService.updateByid(pd);
		article = contentService.findContent(pd);
      } catch (Exception e) {
		logger.error(e.toString(),e);
		code = 500;
		message = "update error";
      }
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
      
      respJson.put("code", code);
      respJson.put("message", message);
      respJson.put("data", data);
      return respJson;
  }
  
  @RequestMapping(value="/delete",method=RequestMethod.DELETE)
  @ResponseBody
	public Object deleteArticle(@RequestBody String req){
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
