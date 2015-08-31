package com.chang.news.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.chang.news.model.NewsBeam;
import com.chang.news.util.BitmapCache;
import com.imooc.tab03.R;

public class NewsAdapter extends BaseAdapter
{
	private List<NewsBeam> mList;
	private LayoutInflater mInflater;

	public static String[] URLS;

	ListView listView;
	
	RequestQueue mQueue;
	ImageLoader imageLoader;
	ImageListener listener;

	public NewsAdapter(Context context, List<NewsBeam> data, ListView listView)
	{
		mList = data;
		this.listView = listView;
		mInflater = LayoutInflater.from(context);
		mQueue = Volley.newRequestQueue(context);
		imageLoader = new ImageLoader(mQueue, new BitmapCache());
	}

	@Override
	public int getCount()
	{
		return mList.size();
	}

	@Override
	public Object getItem(int position)
	{
		return mList.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		ViewHolder viewHolder;
		if(convertView == null)
		{
			viewHolder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.item_layout, null);
			viewHolder.icon = (NetworkImageView) convertView.findViewById(R.id.id_icon);
			viewHolder.title = (TextView) convertView.findViewById(R.id.id_title);
			viewHolder.content = (TextView) convertView.findViewById(R.id.id_content);
			viewHolder.time = (TextView) convertView.findViewById(R.id.id_time);
			convertView.setTag(viewHolder);
			
		}
		else
		{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		String url = mList.get(position).newsIconUrl;
		if ("".equals(url) || url==null) {
			viewHolder.icon.setVisibility(View.GONE);
		} else {
			viewHolder.icon.setVisibility(View.VISIBLE);
			((NetworkImageView) viewHolder.icon).setImageUrl(url,imageLoader); 
		}
		
		viewHolder.title.setText(mList.get(position).newsTitle);
		viewHolder.content.setText(mList.get(position).newsContent);
		viewHolder.time.setText(mList.get(position).newsTime);
		return convertView;
	}

	class ViewHolder
	{
		public TextView title;
		public TextView content;
		public TextView time;
		public ImageView icon;
	}

}
