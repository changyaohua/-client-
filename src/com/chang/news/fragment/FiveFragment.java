package com.chang.news.fragment;



public class FiveFragment extends FirstFragment
{
	@Override
	protected void initUrlPath() {
		// TODO Auto-generated method stub
//		urlPath = "http://192.168.207.26:8080/HongTaiNewsService/FourNoticeServlet?pageNo=";
		urlPath = "http://115.159.63.146:8080/NewsService/FourNoticeServlet?pageNo=";
		type = "½²×ùÔ¤¸æ";
		currPagePotion = 4;
	}
}
