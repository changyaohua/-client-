package com.chang.news.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.chang.news.model.NoticeBeam;
import com.chang.news.R;

public class NoticeAdapter extends BaseAdapter
{
	private List<NoticeBeam> mList;
	private LayoutInflater mInflater;
	private int currentType;
	private int CurrPage;

	public NoticeAdapter(Context context, List<NoticeBeam> data, int CurrPage)
	{
		mList = data;
		mInflater = LayoutInflater.from(context);
		this.CurrPage = CurrPage;
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
	public int getItemViewType(int position) {
		// TODO Auto-generated method stub
		if (position == 0) {
			return 0;
		} else {
			return 1;
		}
	}

	@Override
	public int getViewTypeCount() {
		// TODO Auto-generated method stub
		return 2;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		ViewHolder viewHolder;
		TopHolder topHolder;
		currentType = getItemViewType(position);
		if (currentType == 0) {
			if(convertView == null)
			{
				topHolder = new TopHolder();
				convertView = mInflater.inflate(R.layout.item_notice_top, null);
				topHolder.title = (TextView) convertView.findViewById(R.id.notice_title);
				topHolder.top_img = (ImageView) convertView.findViewById(R.id.top_img);
				convertView.setTag(topHolder);
			}
			else
			{
				topHolder = (TopHolder) convertView.getTag();
			}
			topHolder.title.setText(mList.get(position).getTitle());
			switch (CurrPage) {
			case 1:
				topHolder.top_img.setImageResource(R.drawable.notice_top1);
				break;

			case 2:
				topHolder.top_img.setImageResource(R.drawable.notify_top2);
				break;
				
			case 3:
				topHolder.top_img.setImageResource(R.drawable.notify_top3);
				break;
			}
		} else {
			if(convertView == null)
			{
				viewHolder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.item_notice, null);
				viewHolder.title = (TextView) convertView.findViewById(R.id.notice_title);
				viewHolder.time = (TextView) convertView.findViewById(R.id.notice_time);
				convertView.setTag(viewHolder);
			}
			else
			{
				viewHolder = (ViewHolder) convertView.getTag();
			}
			
			viewHolder.title.setText(mList.get(position).getTitle());
			viewHolder.time.setText(mList.get(position).getTime());
		}
		
		return convertView;
	}

	class ViewHolder
	{
		public TextView title;
		public TextView time;
	}
	class TopHolder
	{
		public ImageView top_img;
		public TextView title;
	}

}
