package com.chang.news.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.chang.news.bean.NoticeBean;

public class GetNoticeJsonData {
	/**
	 * 从 URL 中获取数据
	 * 
	 * @param url
	 * @return
	 */
	public static List<NoticeBean> getJsonData(String urlPath,String pageNo) {
		String httpUrl = null;
		if (pageNo == null) {
			httpUrl = urlPath;
		} else {
			httpUrl = urlPath + pageNo;
		}
		List<NoticeBean> tempList = null;
		try {
			String jsonString = request(httpUrl);
			
			JSONArray jsonArray;
			JSONObject jsonObject;
			NoticeBean noticeBeam;
			jsonArray = new JSONArray(jsonString);
			tempList = new ArrayList<NoticeBean>();
			for (int i = 0; i < jsonArray.length(); i++) {
				jsonObject = (JSONObject)jsonArray.get(i);
				noticeBeam = new NoticeBean();
				noticeBeam.setTitle(jsonObject.getString("title"));
				noticeBeam.setTime(jsonObject.getString("time"));
				noticeBeam.setUrl(jsonObject.getString("url"));
				try {
					noticeBeam.setImage(jsonObject.getString("image"));
				} catch (Exception e) {
					noticeBeam.setImage("");
				}
				tempList.add(noticeBeam);
			}

		} catch (JSONException e) {
			return null;
		}
		return tempList;
	}


	/**
	 * @param urlAll
	 *            :请求接口
	 * @param httpArg
	 *            :参数
	 * @return 返回结果
	 */
	private static String request(String httpUrl) {
		BufferedReader reader = null;
		String result = null;
		StringBuffer sbf = new StringBuffer();
		try {
			URL url = new URL(httpUrl);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setRequestMethod("GET");
			connection.connect();
			InputStream is = connection.getInputStream();
			reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			String strRead = null;
			while ((strRead = reader.readLine()) != null) {
				sbf.append(strRead);
				sbf.append("\r\n");
			}
			reader.close();
			result = sbf.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
