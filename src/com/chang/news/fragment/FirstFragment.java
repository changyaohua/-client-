package com.chang.news.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.chang.news.ClientNewsActivity;
import com.chang.news.NetErrorActivity;
import com.chang.news.R;
import com.chang.news.adapter.NoticeAdapter;
import com.chang.news.bean.NoticeBean;
import com.chang.news.util.GetNoticeJsonData;
import com.chang.news.util.HttpUtil;
import com.chang.news.util.RefreshableView;
import com.chang.news.util.RefreshableView.PullToRefreshListener;

public class FirstFragment extends Fragment implements OnItemClickListener {
	String urlPath;
	String type;
	int currPage;

	ProgressBar progressBar;
	RefreshableView refreshableView;
	ListView listView;

	NoticeAdapter adapter;
	View view;

	boolean mHasLoadedOnce = false;

	int currPageNo = 1;

	NewAsyncTask asyncTask;

	List<NoticeBean> noticeList;

	Handler handler = new Handler();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initUrlPath();
	}

	protected void initUrlPath() {
		// urlPath =
		// "http://192.168.207.26:8080/HongTaiNewsService/HongTaiNoticeServlet?pageNo=";
		urlPath = "http://www.changyaohua.com:8080/NewsService/HongTaiNoticeServlet?pageNo=";
		type = "重要通知";
		currPage = 1;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.layout_first, container, false);
		progressBar = (ProgressBar) view.findViewById(R.id.load_bar);
		refreshableView = (RefreshableView) view
				.findViewById(R.id.refreshable_view);
		listView = (ListView) view.findViewById(R.id.list_view);
		noticeList = new ArrayList<NoticeBean>();

		this.listView.setOnItemClickListener(this);

		asyncTask = new NewAsyncTask();
		asyncTask.execute("" + currPageNo);

		refreshableView.setOnRefreshListener(new PullToRefreshListener() {
			@Override
			public void onRefresh() {
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				List<NoticeBean> tempList = GetNoticeJsonData.getJsonData(
						urlPath, "-1");

				if (tempList == null || tempList.size() == 0) {
					handler.post(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(getActivity(), "暂无新消息",
									Toast.LENGTH_SHORT).show();
						}
					});
				} else {
					String currDateUrl = noticeList.get(0).getUrl();
					List<NoticeBean> mList = new ArrayList<NoticeBean>();
					for (NoticeBean newsBeam : tempList) {
						if (currDateUrl.equals(newsBeam.getUrl())) {
							break;
						} else {
							mList.add(newsBeam);
						}
					}
					if (mList.size() == 0) {
						handler.post(new Runnable() {
							@Override
							public void run() {
								Toast.makeText(getActivity(), "暂无新消息",
										Toast.LENGTH_SHORT).show();
							}
						});
					} else {
						noticeList.addAll(0, mList);
						final int updateNum = mList.size();
						handler.post(new Runnable() {
							@Override
							public void run() {
								Toast.makeText(getActivity(),
										"新更新" + updateNum + "条消息",
										Toast.LENGTH_SHORT).show();
								adapter.notifyDataSetChanged();
							}
						});
					}
				}

				refreshableView.finishRefreshing();
			}

			@Override
			public void onLoad() {
				if (!HttpUtil.isNetworkStatusOK(getActivity())) {

					Toast.makeText(getActivity(), "当前网络不可用！",
							Toast.LENGTH_SHORT).show();
					getActivity().finish();
					startActivity(new Intent(getActivity(),
							NetErrorActivity.class));
				}
				new Thread(new Runnable() {

					@Override
					public void run() {
						try {
							Thread.sleep(1500);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						currPageNo++;
						Log.d("currPageNo", "" + currPageNo);
						List<NoticeBean> mlist = GetNoticeJsonData.getJsonData(
								urlPath, "" + currPageNo);
						if (mlist == null) {
							handler.post(new Runnable() {
								@Override
								public void run() {
									Toast.makeText(getActivity(), "已无更多消息",
											Toast.LENGTH_SHORT).show();
								}

							});
						} else {
							noticeList.addAll(mlist);
						}
						handler.post(new Runnable() {
							@Override
							public void run() {
								adapter.notifyDataSetChanged();
								refreshableView.loadComplete();
							}

						});
					}
				}).start();
			}
		}, currPage);

		return view;
	}

	class NewAsyncTask extends AsyncTask<String, Void, List<NoticeBean>> {
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progressBar.setVisibility(View.VISIBLE);

		}

		@Override
		protected List<NoticeBean> doInBackground(String... params) {
			return GetNoticeJsonData.getJsonData(urlPath, params[0]);
		}

		@Override
		protected void onPostExecute(List<NoticeBean> newsBeams) {
			super.onPostExecute(newsBeams);
			noticeList = newsBeams;
			adapter = new NoticeAdapter(noticeList);
			listView.setAdapter(adapter);
			progressBar.setVisibility(View.GONE);
		}

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {

		if (HttpUtil.isNetworkStatusOK(getActivity())) {

			NoticeBean temp = noticeList.get(position);

			Intent intent = new Intent(getActivity(), ClientNewsActivity.class);
			NoticeBean news = new NoticeBean();
			news.setTitle(temp.getTitle());
			news.setTime("time");
			news.setUrl(temp.getUrl());
			intent.putExtra("news", news);
			intent.putExtra("type", type);
			startActivity(intent);

		} else {
			Toast.makeText(getActivity(), "当前网络不可用！", Toast.LENGTH_SHORT)
					.show();
			getActivity().finish();
			startActivity(new Intent(getActivity(), NetErrorActivity.class));

		}

	}

}
