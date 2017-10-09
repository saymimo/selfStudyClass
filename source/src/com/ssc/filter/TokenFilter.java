package com.ssc.filter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.ssc.controller.base.BaseController;
import com.ssc.util.PageData;

public class TokenFilter extends BaseController implements Filter{

	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
//		BufferedReader br = request.getReader();
//		String str, reqStr = "";
//		while((str = br.readLine()) != null){
//			reqStr += str;
//		}
//		PageData pd = new PageData();
//		pd = getPdFromJson(reqStr);
//		String token = pd.getString("token");
		chain.doFilter(request, response);
	}

	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
		
	}

}
