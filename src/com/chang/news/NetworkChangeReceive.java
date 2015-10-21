package com.chang.news;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class NetworkChangeReceive extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		ConnectivityManager connectManager = (ConnectivityManager) context.getSystemService(android.content.Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectManager.getActiveNetworkInfo();
		if (activeNetworkInfo != null) {
			if (!activeNetworkInfo.isAvailable()) {
				Toast.makeText(context, "网络已断开，请连接", Toast.LENGTH_LONG).show();
			}
		}
	}

}
