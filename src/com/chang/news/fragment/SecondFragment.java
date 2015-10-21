package com.chang.news.fragment;



public class SecondFragment  extends FirstFragment 
{
	
	@Override
	protected void initUrlPath() {
		// TODO Auto-generated method stub
//		urlPath = "http://192.168.207.26:8080/NewsService/SecondNoticeServlet?pageNo=";
		urlPath = "http://www.changyaohua.com:8080/NewsService/SecondNoticeServlet?pageNo=";
		type = "¹¤×÷¶¯Ì¬";
		currPage = 2;
	}
	
}
