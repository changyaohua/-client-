package com.chang.news.fragment;



public class ThreeFragment extends FirstFragment
{
	@Override
	protected void initUrlPath() {
		// TODO Auto-generated method stub
		urlPath = "http://115.159.63.146:8080/HongTaiNewsService/ThreeNoticeServlet?pageNo=";
		type = "学院新闻";
		currPagePotion = 3;
	}
}
