package com.ssc.service.system.user;

import com.ssc.dao.DaoSupport;
import com.ssc.util.PageData;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service("userService")
public class UserService{
  @Resource(name="daoSupport")
  private DaoSupport dao;
  
  public PageData findUserByUid(PageData pd)throws Exception{
	  return (PageData)this.dao.findForObject("UserMapper.findUserByUid", pd);
  }
  public PageData findByUPhone(PageData pd)throws Exception{
    return (PageData)this.dao.findForObject("UserMapper.findByUPhone", pd);
  }
  
  public void updateUserByUid(PageData pd)throws Exception{
    this.dao.findForObject("UserMapper.updateUserByUid", pd);
  }
  
  public void saveU(PageData pd)throws Exception{
    this.dao.save("UserMapper.saveU", pd);
  }
}
