package com.ssc.service.system.content;

import java.util.List;

import com.ssc.dao.DaoSupport;
import com.ssc.util.PageData;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service("contentService")
public class ContentService{
  @Resource(name="daoSupport")
  private DaoSupport dao;
  
  /**
   * 根据日期批量查询前xx条内容
   * 2017-9-14 zxk_senNy
   * @return
 * @throws Exception 
   */
  public List<PageData> findContentList(PageData pd) throws Exception{
	  return (List<PageData>)dao.findForList("ContentMapper.findContentList", pd);
  } 
  /**
   * 查询某条内容
   * 2017-9-14 zxk_senNy
   * @param pd
   * @return
   * @throws Exception
   */
  public PageData findContent(PageData pd) throws Exception{
	  return (PageData)dao.findForList("ContentMapper.findContent", pd);
  } 
 
}
