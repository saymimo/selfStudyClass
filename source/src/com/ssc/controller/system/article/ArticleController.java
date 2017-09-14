package com.ssc.controller.system.article;

import com.ssc.controller.base.BaseController;
import com.ssc.service.system.article.ArticleService;
import com.ssc.service.system.content.ContentService;
import com.ssc.util.Logger;
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
public class ArticleController
  extends BaseController{
  @Resource(name="articleService")
  private ArticleService articleService;
  
  @Resource(name="contentService")
  private ContentService contentService;
  
  /**
   * 新建文章
   * 2017-9-11 zxk_senNy
   * @param req
   * @return
   */
  @RequestMapping(value="/addArticle",method=RequestMethod.POST)
  @ResponseBody
  public Object addArticle(@RequestBody String req){
    JSONObject reqJson = JSONObject.fromObject(req);
    PageData pd = new PageData();
    int code = 200;
    String message = "ok";
    String ARTICLE_ID = get32UUID();
    Date date = new Date();
    pd.put("ARTICLE_ID", ARTICLE_ID);
    pd.put("ANTHOLOGY_ID", reqJson.getString("anthologyId"));
    pd.put("ARTICLE_CONTENT", reqJson.getString("content"));
    pd.put("CREATE_DATE", date);
    pd.put("UPDATE_DATE", date);
    pd.put("CREATE_BY", reqJson.getString("authorId"));
    pd.put("ARTICLE_TITLE", reqJson.getString("title"));
    try{
      articleService.saveArticle(pd);
    }catch (Exception e){
      logger.error(e.toString(), e);
      code = 500;
      message = "saveArticle error!";
    }
    JSONObject respJson = new JSONObject();
    JSONObject data = new JSONObject();
    
    data.put("id", ARTICLE_ID);
    data.put("author", reqJson.getString("authorId"));
    data.put("type", 0);
    data.put("anthology", reqJson.getString("anthologyId"));
    data.put("createTime", date);
    data.put("updateTime", date);
    data.put("title", reqJson.getString("title"));
    data.put("content", reqJson.getString("content"));
    data.put("collectNum", 0);
    data.put("praiseNum", 0);
    
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
  public Object publish(@RequestBody String req){
	  JSONObject respJson = new JSONObject();
	  PageData pd = new PageData();
	  pd = getPdFromJson(req);
      int code = 200;
      String message = "ok";
      Date date = new Date();
      pd.put("content_id", pd.getString("id"));
      pd.put("is_publish", 1);
      pd.put("update_time", date);
      try {
    	  contentService.updateByid(pd);
	} catch (Exception e) {
		logger.error(e.toString(), e);
		code = 500;
		message = "contentService update error";
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
  public Object update(@RequestBody String req){
	  JSONObject reqJson = JSONObject.fromObject(req);
	  JSONObject respJson = new JSONObject();
      PageData pd = new PageData();
      Date date = new Date();
      int code = 200;
      String message = "ok";
      pd.put("ARTICLE_ID", reqJson.getString("id"));
      pd.put("ARTICLE_TITLE", reqJson.getString("title"));
      pd.put("ARTICLE_CONTENT", reqJson.getString("content"));
      pd.put("UPDATE_DATE", date);
      try {
		articleService.update(pd);
	} catch (Exception e) {
		logger.error(e.toString(),e);
		code = 500;
		message = "update error";
	}
	reqJson.remove("title");
	reqJson.remove("content");
	reqJson.put("updateTime", date);
     return reqJson;
  }
  /**
   * 查找文章
   * 2017-9-12 zxk_senNy
   * @return
   */
  @RequestMapping(value="/find",method=RequestMethod.GET)
  public Object find(){
	  PageData pd = new PageData();
	  pd = this.getPageData();
	  JSONObject respJson = new JSONObject();
	  try {
		articleService.findArticle(pd);
	} catch (Exception e) {
		logger.error(e.toString(), e);
	}
	  
	  return respJson;
  }
}
