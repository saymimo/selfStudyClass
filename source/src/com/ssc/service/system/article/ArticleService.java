package com.ssc.service.system.article;

import java.util.List;

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
  //保存文章
  public void saveArticle(PageData pd)throws Exception{
    this.dao.save("ArticleMapper.saveArticle", pd);
  }
  //更新文章
  public void update(PageData pd)throws Exception{
	  this.dao.save("ArticleMapper.update", pd);
  }
  //查找文章
  public List<PageData> findArticle(PageData pd) throws Exception{
	  return (List<PageData>)dao.findForObject("ArticleMapper.findArticle", pd);
  }
}
