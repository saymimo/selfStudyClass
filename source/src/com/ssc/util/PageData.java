package com.ssc.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

public class PageData extends HashMap implements Map{
  private static final long serialVersionUID = 1L;
  Map map = null;
  HttpServletRequest request;
  
  public PageData(HttpServletRequest request){
    this.request = request;
    Map properties = request.getParameterMap();
    Map returnMap = new HashMap();
    Iterator entries = properties.entrySet().iterator();
    
    String name = "";
    String value = "";
    while (entries.hasNext()){
      Map.Entry entry = (Map.Entry)entries.next();
      name = (String)entry.getKey();
      Object valueObj = entry.getValue();
      if (valueObj == null){
        value = "";
      }else if ((valueObj instanceof String[])){
        String[] values = (String[])valueObj;
        value = "";
        for (int i = 0; i < values.length; i++) {
          value = value + values[i] + ",";
        }
        value = value.substring(0, value.length() - 1);
      }else{
        value = valueObj.toString();
      }
      returnMap.put(name, value);
    }
    this.map = returnMap;
  }
  
  public PageData(String req){
	  Map<String, Object> map = JSONObject.fromObject(req);
	  this.map = map;
  }
  
  public PageData(){
    this.map = new HashMap();
  }
  
  public Object get(Object key){
    Object obj = null;
    if ((this.map.get(key) instanceof Object[])){
      Object[] arr = (Object[])this.map.get(key);
      obj = this.request.getParameter((String)key) == null ? arr : this.request == null ? arr : arr[0];
    }else{
      obj = this.map.get(key);
    }
    return obj;
  }
  
  public String getString(Object key)
  {
    return (String)get(key);
  }
  
  public float getFloat(Object key)
  {
    try
    {
      String str = get(key).toString();
      return Float.parseFloat(str);
    }
    catch (Exception e) {}
    return 0.0F;
  }
  
  public Object put(Object key, Object value)
  {
    return this.map.put(key, value);
  }
  
  public Object remove(Object key)
  {
    return this.map.remove(key);
  }
  
  public void clear()
  {
    this.map.clear();
  }
  
  public boolean containsKey(Object key)
  {
    return this.map.containsKey(key);
  }
  
  public boolean containsValue(Object value)
  {
    return this.map.containsValue(value);
  }
  
  public Set entrySet()
  {
    return this.map.entrySet();
  }
  
  public boolean isEmpty()
  {
    return this.map.isEmpty();
  }
  
  public Set keySet()
  {
    return this.map.keySet();
  }
  
  public void putAll(Map t)
  {
    this.map.putAll(t);
  }
  
  public int size()
  {
    return this.map.size();
  }
  
  public Collection values()
  {
    return this.map.values();
  }
}
