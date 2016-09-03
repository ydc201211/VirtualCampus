package com.vc.ui;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import util.TitleBuilder;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.vc.adapter.ActivityAdapter;
import com.vc.dialog.DialogUtil_1;

import common.LocalStorage;
import common.MyApplication;

import com.vc.api.OkHttpConnection;
import dto.ActionSimple;

public class Activity_MyActivity extends Activity implements OnClickListener {

	// listView
	private List<ActionSimple> ac_list;
	private ActivityAdapter activityAdapter;
	private PullToRefreshListView mPullListView;
	private ImageView imageView;
	private ImageView login_back_iv;// 锟斤拷锟截帮拷钮
	private Dialog mDialog;
	private String userId;
	int user_id = 0;
	// 锟斤拷页锟斤拷锟斤拷
	private int pageNow = 1; // 锟斤拷前页
	private int pageCount = 0; // 页锟斤拷
	private String resultCode;
	MyApplication application;

	public static final int LOADING_COMPLETE = 1;
	public static final int LOAD_FAIL = 0;

	public static final int CONN_FAIL = 2;
	private JSONObject json;

	private String url = "/activity/getActivityByUserId";

	// 锟斤拷锟斤拷锟�
	private DialogUtil_1 pDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_myactivity);

		userId = LocalStorage.getString(Activity_MyActivity.this, "userId");
		application = (MyApplication) getApplication();
		if (application.getUserId() != 0)
			user_id = application.getUserId();
		initView(); // 锟斤拷始锟斤拷锟斤拷锟截帮拷钮
		pDialog = new DialogUtil_1(this);
		ac_list = new ArrayList<ActionSimple>();
		initListView();
		pDialog.showPDialog();
		initData();
	}

	private void initView() {
		// 锟斤拷始锟斤拷锟斤拷锟斤拷锟斤拷
		initTitle();
		imageView = (ImageView) findViewById(R.id.ac_pic_Iv);
		login_back_iv = (ImageView) findViewById(R.id.titlebar_iv_left);
		login_back_iv.setOnClickListener(this);
	}

	private void initTitle() {
		new TitleBuilder(this).setTitleText("我的活动")
				.setLeftImage(R.drawable.left).setLeftOnClickListener(this);
	}

	public void initListView() {

		mPullListView = (PullToRefreshListView) findViewById(R.id.myactivity_list);
		ILoadingLayout startLabels = mPullListView.getLoadingLayoutProxy();
		startLabels.setPullLabel("下拉刷新");// 锟斤拷锟斤拷锟斤拷时锟斤拷锟斤拷示锟斤拷锟斤拷示
		startLabels.setRefreshingLabel("正在刷新...");// 刷锟斤拷时
		startLabels.setReleaseLabel("释放刷新");// 锟斤拷锟斤拷锟斤到一锟斤拷锟斤拷锟斤拷时锟斤拷锟斤拷示锟斤拷锟斤拷示
		mPullListView.setMode(Mode.PULL_FROM_START);
		activityAdapter = new ActivityAdapter(this, R.layout.activity_item,
				ac_list);
		mPullListView.setAdapter(activityAdapter);
		mPullListView.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				pageNow = 1;
				ac_list.clear();
				initData();
			}

		});

		mPullListView
				.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {

					@Override
					public void onLastItemVisible() {
						// TODO Auto-generated method stub
						pageNow++;
						initData();

					}

				});

		mPullListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub

				ActionSimple acDetail = ac_list.get(position - 1);

				Intent intent = new Intent(Activity_MyActivity.this,
						Action_Detail_Activity.class);
				intent.putExtra("acDetail", acDetail);

				// 锟斤拷锟斤拷锟斤拷一锟斤拷Activity锟斤拷
				startActivity(intent);

			}

		});

	}

	@Override
	public void onResume() {
		super.onResume();

	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();

	}

	// 锟斤拷锟絝ragment锟斤拷锟斤拷
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		/* pDialog.dismiss(); */

	}

	/**
	 * 锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷写
	 */
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	protected Handler pHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case LOADING_COMPLETE:
				if (mPullListView != null) {
					jsonToObj();
					mPullListView.onRefreshComplete();
					activityAdapter.notifyDataSetChanged();
					pDialog.hidePDialog();
				}
				break;
			case LOAD_FAIL:
				Toast.makeText(Activity_MyActivity.this,"对不起，没有搜索到数据",
						Toast.LENGTH_SHORT).show();
				pDialog.hidePDialog();
				break;
			case CONN_FAIL:
				Toast.makeText(Activity_MyActivity.this, "网络连接异常",
						Toast.LENGTH_SHORT).show();
				pDialog.hidePDialog();
				break;
			}
		}
	};

	private void initData() {
		new Thread() {
			@Override
			public void run() {
				Looper.prepare();
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				// 锟斤拷锟矫达拷锟捷诧拷锟斤拷
				params.add(new BasicNameValuePair("pageNow", String
						.valueOf(pageNow)));
				params.add(new BasicNameValuePair("userId", userId));
				json = OkHttpConnection.execute(Activity_MyActivity.this, url,
						params);
				JSONObject root;
				try {
					if (json == null) {
						pHandler.sendMessageDelayed(
								pHandler.obtainMessage(CONN_FAIL), 3000);
					} else {
						resultCode = json.getString("resultCode");
						pageCount = json.getInt("pageCount");
						if (resultCode.equals("000")) {
							pHandler.sendMessageDelayed(
									pHandler.obtainMessage(LOAD_FAIL), 3000);
						} else {
							pHandler.sendMessageDelayed(
									pHandler.obtainMessage(LOADING_COMPLETE),
									3000);
						}
						System.out.println("============" + json.toString());
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				// 执锟斤拷锟斤拷锟斤拷锟竭筹拷锟斤拷
				Looper.loop();
			}

		}.start();
	}

	private void jsonToObj() {
		try {

			JSONObject root = new JSONObject(json.toString());
			pageCount = root.getInt("pageCount");

			JSONArray items = root.getJSONArray("results");
			for (int i = 0; i < items.length(); i++) {
				ActionSimple ac = new ActionSimple();
				ac.setAction_id(Integer.parseInt(items.getJSONObject(i)
						.getString("action_id")));

				ac.setAction_host(items.getJSONObject(i).getString(
						"action_host"));

				ac.setAction_picPath(items.getJSONObject(i).getString(
						"action_picPath"));
				ac.setAction_site(items.getJSONObject(i).getString(
						"action_site"));
				ac.setAction_time(items.getJSONObject(i).getString(
						"action_time"));
				ac.setAction_theme(items.getJSONObject(i).getString(
						"action_theme"));
				ac.setAttendance_nums(Integer.parseInt(items.getJSONObject(i)
						.getString("attendance_nums")));
				ac.setLike_nums(Integer.parseInt(items.getJSONObject(i)
						.getString("like_nums")));
				ac_list.add(ac);

			}
		} catch (JSONException e) {

			e.printStackTrace();
		}
	}

	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent;

		switch (v.getId()) {
		case R.id.titlebar_iv_left:
			intent = new Intent();
			intent.putExtra("back", "from myActivity");
			setResult(RESULT_CANCELED, intent);
			finish();
			break;
		default:
			break;
		}
	}
}
