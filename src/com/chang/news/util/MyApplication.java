package com.chang.news.util;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.chang.news.NetErrorActivity;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;
/**
 * 用于获取应用级别的变量
 * @author CHANG
 *
 */
public class MyApplication extends Application {
	private static Context context;
	private static RequestQueue requestQueue;
	
	@Override
	public void onCreate() {
		super.onCreate();
		context = getApplicationContext();
		requestQueue = Volley.newRequestQueue(context);
		
	}
	
	public static Context getContext(){
		return context;
	}
	
	public static RequestQueue getRequestQueue(){
		return requestQueue;
	}
}
