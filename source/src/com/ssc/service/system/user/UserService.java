package com.ssc.service.system.user;

import com.ssc.dao.DaoSupport;
import com.ssc.util.PageData;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service("userService")
public class UserService{
  @Resource(name="daoSupport")
  private DaoSupport dao;
  
  public PageData findByUPhone(PageData pd)throws Exception{
    return (PageData)this.dao.findForObject("UserXMapper.findByUPhone", pd);
  }
  
  public void updateUser(PageData pd)throws Exception{
    this.dao.findForObject("UserXMapper.updateUser", pd);
  }
  
  public void saveU(PageData pd)throws Exception{
    this.dao.save("UserXMapper.saveU", pd);
  }
}
