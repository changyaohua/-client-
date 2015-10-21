package com.chang.news;

import com.chang.news.util.HttpUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.Toast;

public class NetErrorActivity extends Activity {

	// 用于记录点击返回键的时间
	private long exitTime;
	private LinearLayout layout_errorpage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activty_net_error);
		
		layout_errorpage = (LinearLayout) findViewById(R.id.layout_errorpage);
		layout_errorpage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (HttpUtil.isNetworkStatusOK(NetErrorActivity.this)) {
					NetErrorActivity.this.finish();
					startActivity(new Intent(NetErrorActivity.this,MainActivity.class));
				} else {
					Toast.makeText(NetErrorActivity.this, "当前网络不可用！",
							Toast.LENGTH_SHORT).show();
					
				}
				
			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {

			if ((System.currentTimeMillis() - exitTime) > 2000) // System.currentTimeMillis()无论何时调用，肯定大于2000
			{
				Toast.makeText(getApplicationContext(), "再按一次退出程序",
						Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
			} else {
				finish();
				System.exit(0);
			}

			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
