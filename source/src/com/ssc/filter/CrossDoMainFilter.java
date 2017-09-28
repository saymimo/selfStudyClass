package com.ssc.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

public class CrossDoMainFilter
  implements Filter
{
  public void init(FilterConfig fc)
    throws ServletException
  {}
  
  public void destroy() {}
  
  public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
    throws IOException, ServletException
  {
    HttpServletResponse response = (HttpServletResponse)res;
    res.setContentType("application/json; charset=utf-8");
    response.setHeader("Access-Control-Allow-Origin", "*");
    response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE,PUT");
    response.setHeader("Access-Control-Max-Age", "0");
    response.setHeader("Access-Control-Allow-Headers", "Origin, No-Cache, X-Requested-With, If-Modified-Since, Pragma, Last-Modified, Cache-Control, Expires, Content-Type, X-E4M-With,userId,token");
    response.setHeader("Access-Control-Allow-Credentials", "true");
    response.setHeader("XDomainRequestAllowed", "1");
    chain.doFilter(req, response);
  }
}
