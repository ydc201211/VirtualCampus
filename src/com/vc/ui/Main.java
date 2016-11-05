package com.vc.ui;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;


import com.vc.api.OkHttpConnection;
import com.vc.api.ResultMessage;
import com.vc.app.App;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

import common.LocalStorage;

public class Main extends FragmentActivity implements OnCheckedChangeListener,
		OnClickListener {

	private App application = (App) this.getApplication();

	private Fragment map_Fragment, find_Fragment, person_Fragment,
			currentFragment;

	private RadioGroup rg_tab;
	private RadioButton rb_mine;
	private String token;
	private JSONObject json;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_main);
		
		
		if(LocalStorage.getString(this,"userId") != ""){
			Log.i("jdjsdjs", "lllllll");
			getToken(LocalStorage.getString(this,"userId"));
			
		}
		
		int fragId = this.getIntent().getIntExtra("fragId", -1);
		init();
		System.out.println("===>>>fragId=" + fragId);
		if (fragId == 3)
			rb_mine.setChecked(true);
		
	}

	
	
	private void getToken(final String userId) {
		// TODO Auto-generated method stub
		new Thread() {
			@Override
			public void run() {
				Looper.prepare();

				List<NameValuePair> params = new ArrayList<NameValuePair>();
			
				params.add(new BasicNameValuePair("userId",userId));
				json = OkHttpConnection.execute(Main.this,
						"/user/getToken", params);

				try {
					if (json == null) {
						pHandler.sendMessageDelayed(
								pHandler.obtainMessage(ResultMessage.GET_TOKEN_FAIL), 3000);
					} else {

						String resultCode = json.getString("resultCode");
						if (resultCode == "000") {
							pHandler.sendMessageDelayed(
									pHandler.obtainMessage(ResultMessage.GET_TOKEN_FAIL), 3000);
						} else {
							pHandler.sendMessageDelayed(
									pHandler.obtainMessage(ResultMessage.GET_TOKEN_SUCCESS),
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



	private void init() {

		if (map_Fragment == null) {
			map_Fragment = new Fragment_Map();
		}

		if (!map_Fragment.isAdded()) {
			// 锟结交锟斤拷锟斤拷
			getSupportFragmentManager().beginTransaction()
					.add(R.id.line_fragment, map_Fragment).commit();

			// 锟斤拷录锟斤拷前Fragment
			currentFragment = map_Fragment;
		}
		rg_tab = (RadioGroup) findViewById(R.id.rg_tab);
		rb_mine = (RadioButton) findViewById(R.id.rb_mine);
		rg_tab.setOnCheckedChangeListener(this);

	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
		switch (checkedId) {
		case R.id.rb_map:
			if (map_Fragment == null) {
				map_Fragment = new Fragment_Map();
			}

			addOrShowFragment(getSupportFragmentManager().beginTransaction(),
					map_Fragment);
			break;
		case R.id.rb_find:
			if (find_Fragment == null) {
				find_Fragment = new Fragment_Find();
			}

			addOrShowFragment(getSupportFragmentManager().beginTransaction(),
					find_Fragment);
			break;
		case R.id.rb_mine:
			if (person_Fragment == null) {
				person_Fragment = new Fragment_Person();
			}

			addOrShowFragment(getSupportFragmentManager().beginTransaction(),
					person_Fragment);
			break;

		default:
			break;
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

	}

	private void addOrShowFragment(FragmentTransaction transaction,
			Fragment fragment) {
		if (currentFragment == fragment)
			return;

		if (!fragment.isAdded()) {
			// 锟斤拷锟角癴ragment未锟斤拷锟斤拷樱锟斤拷锟斤拷锟接碉拷Fragment锟斤拷锟斤拷锟斤拷锟斤拷
			transaction.hide(currentFragment).add(R.id.line_fragment, fragment)
					.commit();
		} else {
			transaction.hide(currentFragment).show(fragment).commit();
		}

		currentFragment = fragment;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			AlertDialog.Builder dialog = new AlertDialog.Builder(this);
			// dialog.setIcon(android.R.drawable.zoom_plate);
			dialog.setTitle("警告");
			dialog.setMessage("你确定要退出当前程序？");
			dialog.setPositiveButton("确定",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							finish();
						}
					});
			dialog.setNegativeButton("取消",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {

						}
					});
			dialog.show();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	
	private void connect() {

	    if (getApplicationInfo().packageName.equals(App.getCurProcessName(getApplicationContext()))) {

	        /**
	         * IMKit SDK调用第二步,建立与服务器的连接
	         */
	        RongIM.connect(token, new RongIMClient.ConnectCallback() {

	            /**
	             * Token 错误，在线上环境下主要是因为 Token 已经过期，您需要向 App Server 重新请求一个新的 Token
	             */
	            @Override
	            public void onTokenIncorrect() {

	                Log.i("LoginActivity", "--onTokenIncorrect");
	            }

	            /**
	             * 连接融云成功
	             * @param userid 当前 token
	             */
	            @Override
	            public void onSuccess(String userid) {
	            	
	                Log.i("Main", "--onSuccess" + userid);
	                
	                
	            }

	            /**
	             * 连接融云失败
	             * @param errorCode 错误码，可到官网 查看错误码对应的注释
	             */
	            @Override
	            public void onError(RongIMClient.ErrorCode errorCode) {

	                Log.i("LoginActivity", "--onError" + errorCode);
	            }
	        });
	    }
	}
	
	private Handler pHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			
			switch (msg.what) {
				case ResultMessage.GET_TOKEN_SUCCESS: 
					
					
//					try {
//					//	JSONObject result = (JSONObject) json.get("results");	
//					//	token = result.getString("token");
//						
//						
//				//		connect();
//					} catch (JSONException e) {
//						e.printStackTrace();
//					}
				
					break;
				case ResultMessage.GET_TOKEN_FAIL:
					Toast.makeText(Main.this,"失败", Toast.LENGTH_SHORT).show();
					break;
			
			}
		}
	};
}
