package com.chang.news.adapter;

import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.android.volley.toolbox.NetworkImageView;
import com.chang.news.R;
import com.chang.news.bean.NoticeBean;
import com.chang.news.util.BitmapCache;
import com.chang.news.util.MyApplication;

public class NoticeAdapter extends BaseAdapter {
	private List<NoticeBean> mList;
	private LayoutInflater mInflater;

	RequestQueue mQueue;
	ImageLoader imageLoader;
	ImageListener listener;
	
	public NoticeAdapter(List<NoticeBean> data) {
		mList = data;
		mInflater = LayoutInflater.from(MyApplication.getContext());
		mQueue = MyApplication.getRequestQueue();
		imageLoader = new ImageLoader(mQueue, new BitmapCache());
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;

		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.item_notice, null);
			viewHolder.title = (TextView) convertView
					.findViewById(R.id.notice_title);
			viewHolder.time = (TextView) convertView
					.findViewById(R.id.notice_time);
			viewHolder.image = (NetworkImageView) convertView.findViewById(R.id.notic_image);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		String imageUrl = mList.get(position).getImage();
		if ("".equals(imageUrl) || imageUrl==null) {
			viewHolder.image.setVisibility(View.GONE);
		} else {
			viewHolder.image.setVisibility(View.VISIBLE);
			viewHolder.image.setErrorImageResId(R.drawable.picture_load_failure);
			viewHolder.image.setImageUrl(imageUrl,imageLoader); 
		}

		viewHolder.title.setText(mList.get(position).getTitle());
		viewHolder.time.setText(mList.get(position).getTime());

		return convertView;
	}

	class ViewHolder {
		public TextView title;
		public TextView time;
		public NetworkImageView image;
	}

}
