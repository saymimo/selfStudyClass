package com.ssc.service.system.userAction;

import com.ssc.dao.DaoSupport;
import com.ssc.util.PageData;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service("userActionService")
public class UserActionService{
  @Resource(name="daoSupport")
  private DaoSupport dao;
  
  public void saveUserAction(PageData pd)throws Exception{
    this.dao.save("UserActionMapper.saveUserAction", pd);
  }
}
