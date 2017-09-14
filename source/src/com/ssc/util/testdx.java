package com.ssc.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class testdx
{
  public static void main(String[] args) {
	  int limit = 20;
	  int offset = 32;
	  int cur = offset/limit+1;
	  
	  System.out.println(cur);
	  System.out.println((cur-1)*limit);
  }
}
