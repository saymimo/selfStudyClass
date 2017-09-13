package com.ssc.util;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.ComparatorUtils;
import org.apache.commons.collections.comparators.ComparableComparator;
import org.apache.commons.collections.comparators.ComparatorChain;

public class SortUtil
{
  public static void main(String[] args)
  {
    System.out.println(testMapSort());
  }
  
  public static List sort(List sortList, String param1, String orderType)
  {
    Comparator mycmp1 = ComparableComparator.getInstance();
    if ("desc".equals(orderType)) {
      mycmp1 = ComparatorUtils.reversedComparator(mycmp1);
    }
    ArrayList<Object> sortFields = new ArrayList();
    sortFields.add(new BeanComparator(param1, mycmp1));
    
    ComparatorChain multiSort = new ComparatorChain(sortFields);
    Collections.sort(sortList, multiSort);
    
    return sortList;
  }
  
  public static List sortParam2(List sortList, String param1, String param2, String orderType)
  {
    Comparator mycmp1 = ComparableComparator.getInstance();
    Comparator mycmp2 = ComparableComparator.getInstance();
    if ("desc".equals(orderType)) {
      mycmp1 = ComparatorUtils.reversedComparator(mycmp1);
    }
    ArrayList<Object> sortFields = new ArrayList();
    sortFields.add(new BeanComparator(param1, mycmp1));
    sortFields.add(new BeanComparator(param2, mycmp2));
    
    ComparatorChain multiSort = new ComparatorChain(sortFields);
    Collections.sort(sortList, multiSort);
    
    return sortList;
  }
  
  public static List testMapSort()
  {
    List sortList = new ArrayList();
    
    Map map = new HashMap();
    map.put("name", "1");
    map.put("age", "1");
    
    Map map2 = new HashMap();
    map2.put("name", "2");
    map2.put("age", "13");
    
    Map map1 = new HashMap();
    map1.put("name", "2");
    map1.put("age", "12");
    
    List list = new ArrayList();
    list.add(map);
    list.add(map1);
    list.add(map2);
    

    return sortParam2(list, "name", "age", "asc");
  }
}
