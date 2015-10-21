package com.chang.news.fragment;



public class FiveFragment extends FirstFragment
{
	@Override
	protected void initUrlPath() {
//		urlPath = "http://192.168.207.26:8080/HongTaiNewsService/FourNoticeServlet?pageNo=";
		urlPath = "http://www.changyaohua.com:8080/NewsService/FourNoticeServlet?pageNo=";
		type = "½²×ùÔ¤¸æ";
		currPage = 4;
	}
}
