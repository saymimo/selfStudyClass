package com.ssc.dao;

public abstract interface DAO
{
  public abstract Object save(String paramString, Object paramObject)
    throws Exception;
  
  public abstract Object update(String paramString, Object paramObject)
    throws Exception;
  
  public abstract Object delete(String paramString, Object paramObject)
    throws Exception;
  
  public abstract Object findForObject(String paramString, Object paramObject)
    throws Exception;
  
  public abstract Object findForList(String paramString, Object paramObject)
    throws Exception;
  
  public abstract Object findForMap(String paramString1, Object paramObject, String paramString2, String paramString3)
    throws Exception;
  
  public abstract Object findForSize(String paramString)
    throws Exception;
}
