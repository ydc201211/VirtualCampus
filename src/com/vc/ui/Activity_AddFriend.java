package com.vc.ui;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vc.adapter.UserAdapter;
import com.vc.api.OkHttpConnection;
import com.vc.entity.User;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;


public class Activity_AddFriend extends Activity implements OnClickListener{
	
	private RelativeLayout cancle_tv;
	private RelativeLayout search_tv;
	private EditText search_et;
	private ListView people_lv;
	private LinearLayout searchFriends_LL;
	
	private JSONObject json;
	
	//查询关键字
	private String keyword;
	
	//查询信息列表
	private List<User> peopleList;
	private UserAdapter userAdapter;
	
	public static final int LOADING_COMPLETE = 1;
	public static final int LOAD_FAIL = 0;
	
	private String resultCode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity__add_friend);
		peopleList = new ArrayList<User>();
		
		initView();
	
		
		
	}
	
	

	private void initView(){
		cancle_tv =  (RelativeLayout) findViewById(R.id.cancle_searchfriends);
		search_tv =  (RelativeLayout) findViewById(R.id.sure_searchfriends);
		search_et = (EditText) findViewById(R.id.friends_search_et);
		searchFriends_LL = (LinearLayout) findViewById(R.id.searchfriens_ll);
		
		cancle_tv.setOnClickListener(this);
		search_tv.setOnClickListener(this);
		
		search_et.addTextChangedListener(textWatcher);
		
		people_lv = (ListView) findViewById(R.id.people_lv);
		userAdapter= new UserAdapter(this, R.layout.item_friends,
				peopleList);
		
		people_lv.setAdapter(userAdapter);
	}
	
	
	
	private TextWatcher textWatcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			peopleList.clear();
			// sugadapter.notifyDataSetChanged();
			if (s.length() != 0) {
				search_tv.setVisibility(View.VISIBLE);
				cancle_tv.setVisibility(View.GONE);
				
				
				
			} else {
				search_tv.setVisibility(View.GONE);
				cancle_tv.setVisibility(View.VISIBLE);
				
			}
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		@Override
		public void afterTextChanged(Editable s) {
		}
	};
	
	
	
	public void getFriendsFromServer(){
		
		new Thread() {
			@Override
			public void run() {
				Looper.prepare();

				List<NameValuePair> params = new ArrayList<NameValuePair>();
				
				params.add(new BasicNameValuePair("keyword",keyword));
				json = OkHttpConnection.execute(Activity_AddFriend.this,
						"/user/getUsersByNickName", params);
				Log.i("ydc", json.toString());
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
	

	protected Handler pHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case LOADING_COMPLETE:
			
				peopleList.clear();
				jsonToObj();
				/*searchFriends_LL.setVisibility(View.VISIBLE);*/
			
				userAdapter.notifyDataSetChanged();
				
				break;
			case LOAD_FAIL:
				
				
				break;
			}
		}

		
	};
	
	
	private void jsonToObj() {
		
		// TODO Auto-generated method stub
		
		Gson gson = new Gson();
		JSONArray item;
		try {
			item = json.getJSONArray("results");
			Type type = new TypeToken<List<User>>(){}.getType();
			List<User> l = gson.fromJson(item.toString(),type); 
	        for(int i = 0; i< l.size();i++){
	        	Log.i("yyy",l.get(i).getNickName());
	        	peopleList.add(l.get(i));
	        }
	        
	        
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		 
	}
	
	

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.cancle_searchfriends:
			finish();
			break;
		case R.id.sure_searchfriends:
			keyword = search_et.getText().toString();
			if(keyword != " "){
				getFriendsFromServer();
				
			}
			
			break;
		default:
			break;
		}
	}
}
