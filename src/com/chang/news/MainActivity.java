package com.chang.news;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.chang.news.bean.NoticeBean;
import com.chang.news.fragment.FirstFragment;
import com.chang.news.fragment.FiveFragment;
import com.chang.news.fragment.SecondFragment;
import com.chang.news.fragment.ThreeFragment;
import com.chang.news.util.GetNoticeJsonData;
import com.chang.news.util.HttpUtil;
import com.chang.news.util.MyApplication;

public class MainActivity extends FragmentActivity implements OnClickListener {
	private HorizontalScrollView horizontalScrollView;
	private int screenWidth;
	private ViewPager mViewPager;

	private FragmentPagerAdapter mAdapter;
	private List<Fragment> mFragments;

	private LinearLayout mTabInform;
	private LinearLayout mTabPsychology;
	private LinearLayout mTabLecture;
	private LinearLayout mTabAdvance;
	private LinearLayout mTabNews;
	private LinearLayout mTabJob;

	private TextView mTextViewInform;
	private TextView mTextViewPhychology;
	private TextView mTextViewLecture;
	private TextView mTextViewAdvance;
	private TextView mTextViewNews;
	private TextView mTextViewJob;

	private View mViewInform;
	private View mViewPhychology;
	private View mViewLecture;
	private View mViewAdvance;
	private View mViewNews;
	private View mViewJob;

	// ��ͼ�ֲ��Ŀؼ�ʵ��
	private ViewPager viewPagerTop; // android-support-v4�еĻ������
	private List<ImageView> imageViews; // ������ͼƬ����

	private String[] titles; // ͼƬ����
	private String[] titleUrls; // ͼƬ��Ӧ���ݵ�URL
	private List<View> dots; // ͼƬ�������ĵ���Щ��

	private TextView tv_title;
	private int currentItem = 0; // ��ǰͼƬ��������
	private ImageView imageView;

	private boolean isHasUpload = false;

	// �����ֲ�ͼ����Դ��url
	private String urlPath = "http://www.changyaohua.com:8080/NewsService/TopNoticeServlet";
	// ��������ͼƬ������
	private static final int TOPLENGTH = 5;

	// Volley���������
	private RequestQueue mQueue;

	// ���ڼ�¼������ؼ���ʱ��
	private long exitTime;

	private IntentFilter intentFilter;
	private NetworkChangeReceive networkChangeReceive;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		if (HttpUtil.isNetworkStatusOK(this)) {

			initTopView();

			initView();

			setSelect(0);

			intentFilter = new IntentFilter();
			intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
			networkChangeReceive = new NetworkChangeReceive();
			registerReceiver(networkChangeReceive, intentFilter);

		} else {
			Toast.makeText(this, "��ǰ���粻���ã�", Toast.LENGTH_SHORT).show();
			this.finish();
			startActivity(new Intent(this, NetErrorActivity.class));

		}

	}

	private void initTopView() {
		mQueue = MyApplication.getRequestQueue();

		viewPagerTop = (ViewPager) this.findViewById(R.id.vp);

		titles = new String[TOPLENGTH];
		titles[0] = "��ɫ̫�й����������Ƴ�";

		titleUrls = new String[TOPLENGTH];
		titleUrls[0] = "";

		imageViews = new ArrayList<ImageView>();
		// ��ʼ��ͼƬ��Դ
		for (int i = 0; i < TOPLENGTH; i++) {
			imageView = new ImageView(this);
			imageView.setImageResource(R.drawable.main_title_1);
			imageView.setScaleType(ScaleType.CENTER_CROP);
			imageViews.add(imageView);
		}

		new NoticeAsyncTask().execute();

		dots = new ArrayList<View>();
		dots.add(this.findViewById(R.id.v_dot0));
		dots.add(this.findViewById(R.id.v_dot1));
		dots.add(this.findViewById(R.id.v_dot2));
		dots.add(this.findViewById(R.id.v_dot3));
		dots.add(this.findViewById(R.id.v_dot4));

		tv_title = (TextView) this.findViewById(R.id.tv_title);
		tv_title.setText(titles[0]);//

		viewPagerTop = (ViewPager) this.findViewById(R.id.vp);
		viewPagerTop.setAdapter(new MyAdapter());// �������ViewPagerҳ���������
		// ����һ������������ViewPager�е�ҳ��ı�ʱ����
		viewPagerTop.setOnPageChangeListener(new MyPageChangeListener());
	}

	private void initView() {
		horizontalScrollView = (HorizontalScrollView) findViewById(R.id.hsc_lable);
		// �������
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		screenWidth = dm.widthPixels;
		mViewPager = (ViewPager) findViewById(R.id.id_viewpager);

		mTabInform = (LinearLayout) findViewById(R.id.id_tab_inform);
		mTabPsychology = (LinearLayout) findViewById(R.id.id_psychology);
		mTabLecture = (LinearLayout) findViewById(R.id.id_tab_lecture);
		mTabAdvance = (LinearLayout) findViewById(R.id.id_tab_advance);
		mTabNews = (LinearLayout) findViewById(R.id.id_tab_news);
		mTabJob = (LinearLayout) findViewById(R.id.id_tab_job);

		mTextViewInform = (TextView) findViewById(R.id.id_tab_inform_tv);
		mTextViewPhychology = (TextView) findViewById(R.id.id_tab_psychology_tv);
		mTextViewLecture = (TextView) findViewById(R.id.id_tab_lecture_tv);
		mTextViewAdvance = (TextView) findViewById(R.id.id_tab_advance_tv);
		mTextViewNews = (TextView) findViewById(R.id.id_tab_news_tv);
		mTextViewJob = (TextView) findViewById(R.id.id_tab_job_tv);

		mViewInform = (View) findViewById(R.id.id_tab_inform_view);
		mViewPhychology = (View) findViewById(R.id.id_tab_psychology_view);
		mViewLecture = (View) findViewById(R.id.id_tab_lecture_view);
		mViewAdvance = (View) findViewById(R.id.id_tab_advance_view);
		mViewNews = (View) findViewById(R.id.id_tab_news_view);
		mViewJob = (View) findViewById(R.id.id_tab_job_view);

		mFragments = new ArrayList<Fragment>();
		Fragment mTab01 = new FirstFragment();
		Fragment mTab02 = new SecondFragment();
		Fragment mTab03 = new ThreeFragment();
		Fragment mTab04 = new FiveFragment();
		Fragment mTab05 = new FiveFragment();
		Fragment mTab06 = new FiveFragment();

		mFragments.add(mTab01);
		mFragments.add(mTab02);
		mFragments.add(mTab03);
		mFragments.add(mTab04);
		mFragments.add(mTab05);
		mFragments.add(mTab06);

		mTabInform.setOnClickListener(this);
		mTabPsychology.setOnClickListener(this);
		mTabLecture.setOnClickListener(this);
		mTabNews.setOnClickListener(this);

		// ����viewpagerԤ���ؽ������
		mViewPager.setOffscreenPageLimit(5);

		mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {

			@Override
			public int getCount() {
				return mFragments.size();
			}

			@Override
			public Fragment getItem(int arg0) {
				return mFragments.get(arg0);
			}
		};
		mViewPager.setAdapter(mAdapter);

		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				int currItem = mViewPager.getCurrentItem();
				setTab(currItem);
				if (arg0 >= 4) {
					horizontalScrollView
							.scrollBy((int) (0.25 * screenWidth), 0);
				} else if (arg0 < 3) {
					horizontalScrollView.scrollBy(-(int) (0.25 * screenWidth),
							0);
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.id_tab_inform:
			setSelect(0);
			break;
		case R.id.id_psychology:
			setSelect(1);
			break;
		case R.id.id_tab_lecture:
			setSelect(2);
			break;
		case R.id.id_tab_advance:
			setSelect(3);
			break;
		case R.id.id_tab_news:
			setSelect(4);
			break;
		case R.id.id_tab_job:
			setSelect(5);
			break;

		default:
			break;
		}
	}

	private void setSelect(int i) {
		setTab(i);
		mViewPager.setCurrentItem(i);
	}

	private void setTab(int i) {
		resetView();
		// ���ñ�����»���
		// �л���������
		switch (i) {
		case 0:
			mViewInform.setVisibility(View.VISIBLE);
			mTextViewInform.setTextColor(Color.parseColor("#1E90FF"));
			break;
		case 1:
			mViewPhychology.setVisibility(View.VISIBLE);
			mTextViewPhychology.setTextColor(Color.parseColor("#1E90FF"));
			break;
		case 2:
			mViewLecture.setVisibility(View.VISIBLE);
			mTextViewLecture.setTextColor(Color.parseColor("#1E90FF"));
			break;
		case 3:
			mViewAdvance.setVisibility(View.VISIBLE);
			mTextViewAdvance.setTextColor(Color.parseColor("#1E90FF"));
			break;
		case 4:
			mViewNews.setVisibility(View.VISIBLE);
			mTextViewNews.setTextColor(Color.parseColor("#1E90FF"));
			break;
		case 5:
			mViewJob.setVisibility(View.VISIBLE);
			mTextViewJob.setTextColor(Color.parseColor("#1E90FF"));
			break;
		}
	}

	/**
	 * �������б�����»���
	 */
	private void resetView() {
		mViewInform.setVisibility(View.INVISIBLE);
		mViewPhychology.setVisibility(View.INVISIBLE);
		mViewLecture.setVisibility(View.INVISIBLE);
		mViewAdvance.setVisibility(View.INVISIBLE);
		mViewNews.setVisibility(View.INVISIBLE);
		mViewJob.setVisibility(View.INVISIBLE);

		mTextViewInform.setTextColor(Color.parseColor("#FFFFFF"));
		mTextViewPhychology.setTextColor(Color.parseColor("#FFFFFF"));
		mTextViewLecture.setTextColor(Color.parseColor("#FFFFFF"));
		mTextViewAdvance.setTextColor(Color.parseColor("#FFFFFF"));
		mTextViewNews.setTextColor(Color.parseColor("#FFFFFF"));
		mTextViewJob.setTextColor(Color.parseColor("#FFFFFF"));
	}

	private ScheduledExecutorService scheduledExecutorService;

	// �л���ǰ��ʾ��ͼƬ
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			viewPagerTop.setCurrentItem(currentItem); // �л���ǰ��ʾ��ͼƬ
		};
	};

	@Override
	public void onStart() {
		if (isHasUpload) {
			scheduledExecutorService = Executors
					.newSingleThreadScheduledExecutor();
			// ��Activity��ʾ������ÿ�������л�һ��ͼƬ��ʾ
			scheduledExecutorService.scheduleAtFixedRate(new ScrollTask(), 2,
					8, TimeUnit.SECONDS);
		}
		super.onStart();
	}

	@Override
	public void onStop() {
		// ��Activity���ɼ���ʱ��ֹͣ�л�
		if (scheduledExecutorService != null) {
			scheduledExecutorService.shutdown();
		}

		super.onStop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (networkChangeReceive != null) {
			unregisterReceiver(networkChangeReceive);
		}
	}

	/**
	 * �����л�����
	 * 
	 * @author Administrator
	 * 
	 */
	private class ScrollTask implements Runnable {

		public void run() {
			synchronized (viewPagerTop) {
				currentItem = (currentItem + 1) % imageViews.size();
				handler.obtainMessage().sendToTarget(); // ͨ��Handler�л�ͼƬ
			}
		}

	}

	/**
	 * ���ViewPagerҳ���������
	 * 
	 * @author Administrator
	 * 
	 */
	private class MyAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return TOPLENGTH;
		}

		@Override
		public Object instantiateItem(View arg0, final int arg1) {
			View view = imageViews.get(arg1);
			((ViewPager) arg0).addView(view);
			view.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					String url = titleUrls[arg1];
					if ("".equals(url)
							|| "http://xsc.nuc.edu.cnhttp://xsc.nuc.edu.cn/"
									.equals(url) || url == null) {
						Toast.makeText(MainActivity.this, "���������ͼƬ",
								Toast.LENGTH_SHORT).show();
					} else {
						Intent intent = new Intent(MainActivity.this,
								ClientNewsActivity.class);
						NoticeBean news = new NoticeBean();
						news.setTitle(titles[arg1]);
						news.setTime("time");
						news.setUrl(url);
						intent.putExtra("news", news);
						intent.putExtra("type", "�ֻ�ͼ");
						startActivity(intent);
					}

				}
			});

			return view;
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView((View) arg2);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {

		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {

		}

		@Override
		public void finishUpdate(View arg0) {

		}
	}

	/**
	 * ��ViewPager��ҳ���״̬�����ı�ʱ����
	 * 
	 * @author Administrator
	 * 
	 */
	private class MyPageChangeListener implements OnPageChangeListener {
		private int oldPosition = 0;

		/**
		 * This method will be invoked when a new page becomes selected.
		 * position: Position index of the new selected page.
		 */
		public void onPageSelected(int position) {
			currentItem = position;
			tv_title.setText(titles[position]);
			dots.get(oldPosition).setBackgroundResource(
					R.drawable.main_dot_normal);
			dots.get(position).setBackgroundResource(
					R.drawable.main_dot_focused);
			oldPosition = position;
		}

		public void onPageScrollStateChanged(int arg0) {

		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}
	}

	class NoticeAsyncTask extends AsyncTask<String, Void, List<NoticeBean>> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected List<NoticeBean> doInBackground(String... params) {
			return GetNoticeJsonData.getJsonData(urlPath, null);
		}

		@Override
		protected void onPostExecute(List<NoticeBean> noticeList) {
			super.onPostExecute(noticeList);
			int i = 1;
			for (NoticeBean noticeBean : noticeList) {
				titles[i] = noticeBean.getTitle();
				titleUrls[i] = noticeBean.getUrl();
				String imageUrl = noticeBean.getImage();
				final ImageView imageView = imageViews.get(i);
				ImageRequest imageRequest = new ImageRequest(imageUrl,
						new Response.Listener<Bitmap>() {
							@Override
							public void onResponse(Bitmap response) {
								imageView.setImageBitmap(response);
							}
						}, 0, 0, Config.RGB_565, new Response.ErrorListener() {
							@Override
							public void onErrorResponse(VolleyError error) {
								imageView
										.setImageResource(R.drawable.main_title_1);
							}
						});
				mQueue.add(imageRequest);
				i++;
			}
			isHasUpload = true;
			scheduledExecutorService = Executors
					.newSingleThreadScheduledExecutor();
			// ��Activity��ʾ������ÿ�������л�һ��ͼƬ��ʾ
			scheduledExecutorService.scheduleAtFixedRate(new ScrollTask(), 2,
					8, TimeUnit.SECONDS);
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {

			if ((System.currentTimeMillis() - exitTime) > 2000) // System.currentTimeMillis()���ۺ�ʱ���ã��϶�����2000
			{
				Toast.makeText(getApplicationContext(), "�ٰ�һ���˳�����",
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
