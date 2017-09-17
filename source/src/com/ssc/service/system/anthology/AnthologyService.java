package com.ssc.service.system.anthology;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ssc.dao.DaoSupport;
import com.ssc.util.PageData;

@Service("anthologyService")
public class AnthologyService {
	@Resource(name="daoSupport")
	  private DaoSupport dao;
	
	/**
	 * 根据id查文集
	 * 2017-9-15 zxk_senNy
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public PageData findAnthologyById(PageData pd) throws Exception{
		return (PageData)dao.findForObject("AnthologyMapper.findAnthologyById", pd);
	}
	/**
	 * 查文集和作者
	 * 2017-9-16 zxk_senNy
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public PageData findAnthologyAndAuthorById(PageData pd) throws Exception{
		return (PageData)dao.findForObject("AnthologyMapper.findAnthologyAndAuthorById", pd);
	}
	/**
	 * 查找文集和文章
	 * 2017-9-16 zxk_senNy
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public List<PageData> findArticlesByAnthologyId(PageData pd) throws Exception{
		return (List<PageData>)dao.findForList("AnthologyMapper.findArticlesByAnthologyId", pd);
	}
	/**
	 * 根据用户id查其文集list
	 * 2017-9-16 zxk_senNy
	 * @param pd
	 * @return
	 * @throws Exception 
	 */
	public List<PageData> findAnthologyListByUid(PageData pd) throws Exception{
		return (List<PageData>)dao.findForList("AnthologyMapper.findAnthologyListByUid", pd);
	}
	/**
	 * 保存文集
	 * 2017-9-17 zxk_senNy
	 * @param pd
	 * @throws Exception
	 */
	public void saveAnthology(PageData pd) throws Exception{
		dao.save("AnthologyMapper.saveAnthology", pd);
	}
	/**
	 * 根据id更新文集
	 * 2017-9-17 zxk_senNy
	 * @param pd
	 * @throws Exception 
	 */
	public void updateAnthologyById(PageData pd) throws Exception{
		dao.update("AnthologyMapper.updateAnthologyById", pd);
	}
}
