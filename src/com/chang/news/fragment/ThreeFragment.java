package com.chang.news.fragment;



public class ThreeFragment extends FirstFragment
{
	@Override
	protected void initUrlPath() {
//		urlPath = "http://192.168.207.26:8080/HongTaiNewsService/ThreeNoticeServlet?pageNo=";
		urlPath = "http://www.changyaohua.com:8080/NewsService/ThreeNoticeServlet?pageNo=";
		type = "学院新闻";
		currPage = 3;
	}
}
