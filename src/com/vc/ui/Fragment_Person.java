package com.vc.ui;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.vc.api.AppClientDao;
import com.vc.api.OkHttpConnection;
import com.vc.api.ResultMessage;
import com.vc.entity.User;
import common.Constants;
import common.LocalStorage;
import common.MyApplication;

public class Fragment_Person extends Fragment implements OnClickListener {

	private MyApplication application;

	private ImageView avatarIv;
	private TextView loginTv;
	
	private TextView addFriends;
	private LinearLayout myActivity_LL;
	private LinearLayout strategy_LL;
	private LinearLayout update_LL;
	private LinearLayout set_LL;
	private View personView;
	private int userId = 0;
	private User user = new User();

	private String url = "/user/getUser";
	private JSONObject json;
	private String resultCode;

	private final int SUCCESS = 0;
	private final int INIT_COMPLETE = 1;

	private Dialog pDialog, mDialog;
	private AppClientDao mAppClientDao = new AppClientDao(getActivity());

	DisplayImageOptions options; // 图片锟斤拷示锟斤拷锟斤拷
	protected ImageLoader imageLoader;// 图片锟斤拷锟斤拷

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			// if (mDialog.isShowing())
			// mDialog.dismiss();
			switch (msg.what) {
			case ResultMessage.GETUSERSUCCESS: {
				Object objs = (Object) msg.obj;
				try {
					JSONObject json = new JSONObject(objs.toString())
					.getJSONObject("results");
					if (json != null && !json.equals("")) {
						user.setUserId(userId);
						user.setUserName(json.getString("userName"));
						user.setNickName(json.getString("nickName"));
						user.setUser_address(json.getString("user_address"));
						user.setUser_sex(json.getString("user_sex"));
						user.setUser_autograph(json.getString("user_autograph"));
						user.setUser_birthday(json.getString("user_birthday"));
						user.setUser_college(json.getString("user_college"));
						user.setUser_entrance(json.getString("user_entrance"));
						user.setUser_hobby(json.getString("user_hobby"));
						user.setUser_picPath(json.getString("user_picPath"));
						application.setUser(user);
					}
					if (user != null) {
						loginTv.setText(user.getNickName());
						loadPicture();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
				break;
			case ResultMessage.GETUSERFAILED:
				Toast.makeText(getActivity(), msg.obj.toString(),
						Toast.LENGTH_SHORT).show();
				break;
			case ResultMessage.FAILED:
				Toast.makeText(getActivity(), msg.obj.toString(),
						Toast.LENGTH_SHORT).show();
				break;
			case ResultMessage.TIMEOUT:
				Toast.makeText(getActivity(), msg.obj.toString(),
						Toast.LENGTH_SHORT).show();
				break;
			}
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		personView = inflater.inflate(R.layout.fragment_person, null);
		/* Bundle data = getArguments(); */
		application = (MyApplication) getActivity().getApplication();
		// 图片锟斤拷锟斤拷
		imageLoader = ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));
		initView();
		initData();
		return personView;
	}

	public void initView() {
		avatarIv = (ImageView) personView.findViewById(R.id.imagehead);
		loginTv = (TextView) personView.findViewById(R.id.tvlogin);
		
		addFriends = (TextView) personView.findViewById(R.id.addFriends);
		myActivity_LL = (LinearLayout) personView
				.findViewById(R.id.my_Activity_ll);
		strategy_LL = (LinearLayout) personView.findViewById(R.id.strategy_ll);
		set_LL = (LinearLayout) personView.findViewById(R.id.set_ll);
		update_LL = (LinearLayout) personView.findViewById(R.id.update_ll);
		avatarIv.setOnClickListener(this);
		loginTv.setOnClickListener(this);
		myActivity_LL.setOnClickListener(this);
		update_LL.setOnClickListener(this);
		strategy_LL.setOnClickListener(this);
		set_LL.setOnClickListener(this);
		
		addFriends.setOnClickListener(this);

	}

	private void initData() {
		if (LocalStorage.getString(getActivity(), "userId").toString().trim() != null
				&& !(LocalStorage.getString(getActivity(), "userId").toString()
						.trim()).equals("")) {
			userId = Integer.parseInt(LocalStorage
					.getString(getActivity(), "userId").toString().trim());
			mAppClientDao.Get_User_ById(mHandler, userId);
			// showPDialog();
		}
		if (application.getUserId() != 0) {
			mAppClientDao.Get_User_ById(mHandler, userId);
			// showPDialog();
		}
		// getData();

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent;
		Bundle bundle;
		switch (v.getId()) {
		case R.id.imagehead:
			
			if (userId != 0) {
				intent = new Intent(getActivity(), Activity_Person_Detail.class);
				startActivityForResult(intent, 2);
			} else {
				Toast.makeText(getActivity(), "请先登录", Toast.LENGTH_SHORT)
						.show();
			}
			break;

		case R.id.tvlogin:
			
			if (userId != 0) {
				intent = new Intent(getActivity(), Activity_Person_Detail.class);
				startActivityForResult(intent, 2);
			} else {
				intent = new Intent();
				intent = new Intent(getActivity(), LoginActivity.class);
				startActivityForResult(intent, 1);
			}
			break;
		case R.id.my_Activity_ll:
			
			if (userId != 0) {
				intent = new Intent(getActivity(), Activity_MyActivity.class);
				startActivityForResult(intent, 3);
			} else {
				Toast.makeText(getActivity(), "请先登录", Toast.LENGTH_SHORT)
						.show();
			}
			break;
		case R.id.strategy_ll:
			intent = new Intent(getActivity(), Activity_NewStrategy.class);
			startActivityForResult(intent, 5);
			break;
		case R.id.set_ll:
			intent = new Intent(getActivity(), Activity_Setting.class);
			startActivityForResult(intent, 4);
			break;
		case R.id.update_ll:
			AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
			dialog.setTitle("检查更新");
			dialog.setMessage("没有更新可以下载");
			dialog.setCancelable(false);
			dialog.setPositiveButton("确定",new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					
				}
			});
			dialog.setNegativeButton("取消",new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					
				}
			});
			dialog.show();
			break;
			
		case R.id.addFriends:
			intent = new Intent(getActivity(),Activity_AddFriend.class);
			startActivity(intent);
			
			break;
		default:
			break;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode == 1 && resultCode == 2) {
			if (intent != null) {
				userId = intent.getIntExtra("userId", 0);
				mAppClientDao.Get_User_ById(mHandler, userId);
			}
		}
		if (requestCode == 2 && resultCode == 5) {
			mAppClientDao.Get_User_ById(mHandler, userId);
		}
		if (requestCode == 3) {

		}
		if (requestCode == 4 && resultCode == 1) {
			userId = 0;
			loginTv.setText("未登录");
			avatarIv.setImageResource(R.drawable.nologin);
			LocalStorage.saveString(getActivity(), "userId",
					String.valueOf(userId));
			LocalStorage.saveString(getActivity(), "userName", "");
			LocalStorage.saveString(getActivity(), "password", "");
		}
		if(requestCode == 5 && resultCode == 0){
			
		}
	}

	private void getData() {

		new Thread() {
			@Override
			public void run() {

				Looper.prepare();
				// 使锟斤拷NameValuePair锟斤拷锟斤拷锟斤拷要锟斤拷锟捷碉拷Post锟斤拷锟斤拷
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				// 锟斤拷锟矫达拷锟捷诧拷锟斤拷

				params.add(new BasicNameValuePair("userId", String
						.valueOf(userId)));

				json = OkHttpConnection.execute(getActivity(), url, params);
				JSONObject root;
				if (json != null) {
					try {
						/* root = new JSONObject(json.toString()); */
						resultCode = json.getString("resultCode");

						if (resultCode == "000") {
							Toast.makeText(getActivity(),
									json.getString("results").toString(), 0)
									.show();
						} else {

							pHandler.sendMessageDelayed(
									pHandler.obtainMessage(SUCCESS), 3000);
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				// 执锟斤拷锟斤拷锟斤拷锟竭筹拷锟斤拷
				Looper.loop();
			}
		}.start();

	}

	private User JSONToObj() {
		try {
			/* JSONObject root = new JSONObject(json.toString()); */
			JSONObject items = json.getJSONObject("results");
			User user = new User();
			user.setNickName(items.getString("nickName"));
			user.setUser_address(items.getString("user_address"));
			user.setUser_autograph(items.getString("user_autograph"));
			user.setUser_birthday(items.getString("user_birthday"));
			user.setUser_college(items.getString("user_college"));
			user.setUser_entrance(items.getString("user_entrance"));
			user.setUser_hobby(items.getString("user_hobby"));
			user.setUser_picPath(items.getString("user_picPath"));
			user.setUser_sex(items.getString("user_sex"));
			user.setUserId(Integer.parseInt(items.getString("userId")));
			return user;

		} catch (JSONException e) {

			e.printStackTrace();
		}
		return user;
	}

	protected Handler pHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case SUCCESS: {
				user = JSONToObj();
				application.setUser(user);
				if (user != null) {
					loginTv.setText(user.getNickName());
					loadPicture();
					// mDialog.show();
					// hidePDialog();
				}
			}
				break;
			}
		}
	};

	private void loadPicture() {

		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.icon_onloading)//正在加载
				.showImageForEmptyUri(R.drawable.ic_launcher) // 
				.showImageOnFail(R.drawable.icon_loadfail) // 
				.cacheInMemory(true) // 
				.cacheOnDisk(true) // 
				.imageScaleType(ImageScaleType.IN_SAMPLE_INT) // 
				.build(); //

		imageLoader.displayImage(Constants.SERVERADDRESS
				+ "/file/avatar/get?imageUrl=" + user.getUser_picPath(),
				avatarIv, options);

	}	
}