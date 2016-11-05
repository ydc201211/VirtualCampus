package com.vc.ui;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import util.TitleBuilder;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.vc.adapter.ActivityAdapter;
import com.vc.dialog.DialogUtil_1;
import com.vc.entity.User;
import common.LocalStorage;

import com.vc.api.OkHttpConnection;
import dto.ActionSimple;
//修改
public class Activity_ActivityList extends Activity implements OnClickListener {

	private RelativeLayout acList_back;// 返回
	private ImageView post_button;

	// listView
	private LinkedList<ActionSimple> ac_list;
	private ActivityAdapter activityAdapter;
	private PullToRefreshListView mPullListView;
	private ImageView imageView;

	// ��ҳ����
	private int pageNow = 1; // 当前页
	private int pageCount = 0; // 页数
	private String resultCode;

	public static final int LOADING_COMPLETE = 1;
	public static final int LOAD_FAIL = 0;

	private JSONObject json;
	private User user;

	private String url = "/activity/getActivity";

	
	
	
	private int onloadFlag = 0; 
	

	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_activity_list);

		initView(); //初始化标题栏和发布按钮
		
		ac_list = new LinkedList<ActionSimple>();

		
		initListView();

		initData();
		
	}

	private void initView() {
		// 初始化标题栏
		initTitle();
		/*imageView = (ImageView) findViewById(R.id.ac_pic_Iv);*/
		post_button = (ImageView) findViewById(R.id.post_button);
		post_button.setOnClickListener(this);
	}

	private void initTitle() {
		new TitleBuilder(this).setTitleText("活动列表")
				.setLeftImage(R.drawable.left).setLeftOnClickListener(this);
	}

	public void initListView() {

		mPullListView = (PullToRefreshListView) findViewById(R.id.activity_list);
		
		mPullListView.setEmptyView(findViewById(R.id.empty_lsitview));
		
		ILoadingLayout startLabels = mPullListView.getLoadingLayoutProxy();
		startLabels.setPullLabel("下拉刷新");
		startLabels.setRefreshingLabel("正在刷新...");
		startLabels.setReleaseLabel("释放刷新");
		mPullListView.setMode(Mode.PULL_FROM_START);
		activityAdapter = new ActivityAdapter(this, R.layout.activity_item,
				ac_list);
		mPullListView.setAdapter(activityAdapter);
		mPullListView.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				pageNow = 1;
				onloadFlag = 0;
				/*if (activityAdapter != null) {
					activityAdapter.notifyDataSetChanged();
				}*/
				initData();
			}

		});

		mPullListView.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {

					@Override
					public void onLastItemVisible() {
						// TODO Auto-generated method stub
						if (pageNow < pageCount) {
							pageNow++;
							onloadFlag = 1;
							initData();
							
						} else {
							Toast.makeText(Activity_ActivityList.this, "已经到底了",
									Toast.LENGTH_SHORT).show();
						}

					}

				});

		mPullListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub

				ActionSimple acDetail = ac_list.get(position - 1);

				Intent intent = new Intent(Activity_ActivityList.this,
						Action_Detail_Activity.class);
				intent.putExtra("acDetail", acDetail);

				
				startActivityForResult(intent, 2);

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

	// ���fragment����
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		/* pDialog.dismiss(); */

	}

	/**
	 * ����������д
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
					
				}
				break;
			case LOAD_FAIL:
				
				
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
			
				params.add(new BasicNameValuePair("pageNow", String
						.valueOf(pageNow)));
				json = OkHttpConnection.execute(Activity_ActivityList.this,
						url, params);

				try {
					if (json == null) {
						pHandler.sendMessageDelayed(
								pHandler.obtainMessage(LOAD_FAIL), 3000);
					} else {

						resultCode = json.getString("resultCode");
						if (resultCode == "000") {
							pHandler.sendMessageDelayed(
									pHandler.obtainMessage(LOAD_FAIL), 3000);
						} else {
							pHandler.sendMessageDelayed(
									pHandler.obtainMessage(LOADING_COMPLETE),
									3000);
						}
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				
				Looper.loop();
			}

		}.start();
	}

	private void jsonToObj() {
		try {

			pageCount = json.getInt("pageCount");

			JSONArray items = json.getJSONArray("results");
			if(onloadFlag == 0){
				for (int i = items.length()-1; i >= 0; i--) {
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
					ac.setAction_detail(items.getJSONObject(i).getString("action_detail"));
					ac.setAttendance_nums(Integer.parseInt(items.getJSONObject(i)
							.getString("attendance_nums")));
					ac.setLike_nums(Integer.parseInt(items.getJSONObject(i)
							.getString("like_nums")));
					
					
					addActionSimple(ac);
				}
				
			}else{
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
					ac.setAction_detail(items.getJSONObject(i).getString("action_detail"));
					ac.setAttendance_nums(Integer.parseInt(items.getJSONObject(i)
							.getString("attendance_nums")));
					ac.setLike_nums(Integer.parseInt(items.getJSONObject(i)
							.getString("like_nums")));
					
					
					addActionSimple(ac);
				}
			}
				
				

			
		} catch (JSONException e) {

			e.printStackTrace();
		}
	}

	private void addActionSimple(ActionSimple ac){
		
		if(ac_list.size() == 0){
			ac_list.add(ac);
			
		}else{
			for(int i = 0;i< ac_list.size();i++){
				if(ac_list.get(i).getAction_id() == ac.getAction_id())
					return;
			}
				
			if(onloadFlag == 0){
				ac_list.addFirst(ac);
			}else{
				ac_list.addLast(ac);
			}
			
		}
		
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent;
		Bundle bundle;
		switch (v.getId()) {
		case R.id.titlebar_iv_left:
			intent = new Intent();
			setResult(RESULT_OK);
			finish();
			break;
		case R.id.post_button:

			String userId = LocalStorage.getString(Activity_ActivityList.this,
					"userId");
			if (userId == null || userId == "") {
				Toast.makeText(Activity_ActivityList.this, "请先登录",
						Toast.LENGTH_SHORT).show();
			} else {
				intent = new Intent(Activity_ActivityList.this,
						Activity_Post.class);
				startActivityForResult(intent, 1);

			}
			break;
		default:
			break;

		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		switch (requestCode) {
		case 1:
			if (intent != null) {
				if (resultCode == -1) {
					pageNow = 1;
					initData();
					
				} else {
					
				}
			}
			break;
		case 2:
			if (intent != null) {
				String a = intent.getStringExtra("back");
				Log.i("fragment_person", a);
			}
			break;
		default:
			break;
		}

	}
}
