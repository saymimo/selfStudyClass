package com.ssc.service.system.article;

import com.ssc.dao.DaoSupport;
import com.ssc.util.PageData;

import javax.annotation.Resource;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;

@Service("articleService")
public class ArticleService
{
  @Resource(name="daoSupport")
  private DaoSupport dao;
  
  public void saveArticle(PageData pd)throws Exception{
    this.dao.save("ArticleMapper.saveArticle", pd);
  }
  public void update(PageData pd)throws Exception{
	  this.dao.save("ArticleMapper.saveArticle", pd);
  }
}
