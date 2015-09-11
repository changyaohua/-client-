package com.chang.news.fragment;



public class SecondFragment  extends FirstFragment 
{
	
	@Override
	protected void initUrlPath() {
		// TODO Auto-generated method stub
//		urlPath = "http://192.168.207.26:8080/HongTaiNewsService/SecondNoticeServlet?pageNo=";
		urlPath = "http://115.159.63.146:8080/NewsService/SecondNoticeServlet?pageNo=";
		type = "¹¤×÷¶¯Ì¬";
		currPagePotion = 2;
	}
	
}
