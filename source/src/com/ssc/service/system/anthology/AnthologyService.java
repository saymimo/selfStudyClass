package com.ssc.service.system.anthology;

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
}
