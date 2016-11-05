package com.vc.ui;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import util.TitleBuilder;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.vc.api.AppClientDao;
import com.vc.api.ResultMessage;
import com.vc.entity.User;
import common.Base64;
import common.Constants;

import com.vc.api.OkHttpConnection;
import com.vc.app.App;

public class Activity_Person_Detail extends Activity implements OnClickListener {

	private App application;
	
	//头像ImageView
	private ImageView avatar_iv;
	private TextView nickName_tv, change_p_icon;
	private RelativeLayout editAvatar_ll;

	private RelativeLayout sign_rl;
	private TextView sign_tv;
	private ImageView sign_iv;

	private LinearLayout sign_ll;
	private EditText sign_et;
	private TextView sign_sure_button;
	private TextView sign_cancle_button;

	private LinearLayout detail_ll;
	private LinearLayout edit_detail_ll;

	private TextView sex_tv;
	private Spinner sex_spinner;

	private TextView birthday_tv;
	private EditText birthday_et;

	private TextView address_tv;
	private EditText address_et;

	private TextView college_tv;
	private EditText college_et;

	private TextView entrance_tv;
	private EditText entrance_et;

	private TextView hobby_tv;
	private EditText hobby_et;

	private ImageView editInfo_button;

	private TextView editinfo_cancle_button;
	private TextView editinfo_sure_button;
	private ImageView back_p_info;

	// user
	private User user;
	private int userId;
	private String sex_Item;

	DisplayImageOptions options; 
	protected ImageLoader imageLoader;

	private JSONObject json;
	private String resultCode;
	private static final int SUCCESS = 1;
	

	private final int POST_SUCCESS = 2;
	private final int UPLOADPIC_SUCCESS = 14;
	private final int FAIL = 0;
	private static final String IMAGE_UNSPECIFIED = "image/jpg";
	private static final int CROP_REQUEST_CODE = 4;

	private byte[] imgByte; // 头锟斤拷锟斤拷锟�
	
	private String imgName = Constants.DefaulActivityPhotoName;

	private static final int PHOTO_WITH_DATA = 18; // 锟斤拷SD锟斤拷锟叫得碉拷图片
	private static final int PHOTO_WITH_CAMERA = 37;// 锟斤拷锟斤拷锟斤拷片

	private String url = "";
	AppClientDao mAppClientDao = new AppClientDao(this);
	private List<NameValuePair> params;
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case ResultMessage.Change_SUCCESS: {
				Object objs = (Object) msg.obj;
				Toast.makeText(Activity_Person_Detail.this,"保存头像成功",
						Toast.LENGTH_SHORT).show();
				mAppClientDao.Get_User_ById(mHandler, userId);
			}
				break;
			case ResultMessage.Change_FAILED:
				Toast.makeText(Activity_Person_Detail.this, msg.obj.toString(),
						Toast.LENGTH_SHORT).show();

				break;
			case ResultMessage.GETUSERSUCCESS:
				Object objs = (Object) msg.obj;
				{
					try {
						JSONObject json = new JSONObject(objs.toString())
								.getJSONObject("results");
						if (json != null && !json.equals("")) {
							user.setUserId(userId);
							user.setUserName(json.getString("userName"));
							user.setNickName(json.getString("nickName"));
							user.setUser_address(json.getString("user_address"));
							user.setUser_sex(json.getString("user_sex"));
							user.setUser_autograph(json
									.getString("user_autograph"));
							user.setUser_birthday(json
									.getString("user_birthday"));
							user.setUser_college(json.getString("user_college"));
							user.setUser_entrance(json
									.getString("user_entrance"));
							user.setUser_hobby(json.getString("user_hobby"));
							user.setUser_picPath(json.getString("user_picPath"));
							application.setUser(user);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				initData();
				break;
			case ResultMessage.GETUSERFAILED:
				Toast.makeText(Activity_Person_Detail.this, msg.obj.toString(),
						Toast.LENGTH_SHORT).show();
				break;
			case ResultMessage.FAILED:
				Toast.makeText(Activity_Person_Detail.this, msg.obj.toString(),
						Toast.LENGTH_SHORT).show();
				break;
			case ResultMessage.TIMEOUT:
				Toast.makeText(Activity_Person_Detail.this, msg.obj.toString(),
						Toast.LENGTH_SHORT).show();
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_activity__person__detail);

		application = (App) this.getApplication();

		user = application.getUser();
		userId = user.getUserId();
		
		// 图片锟斤拷锟斤拷
		imageLoader = ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration.createDefault(this));
		initTitle();
		initView();
		registerListener();
		initData();

	}

	private void initView() {

		//返回按钮
		back_p_info = (ImageView) this.findViewById(R.id.titlebar_iv_left);

		change_p_icon = (TextView) this.findViewById(R.id.change_p_icon);

		avatar_iv = (ImageView) this.findViewById(R.id.p_info_avatar_iv);
		nickName_tv = (TextView) this.findViewById(R.id.p_info_nickname_tv);
		editAvatar_ll = (RelativeLayout) this
				.findViewById(R.id.p_info_edit_avatar_ll);

		sign_rl = (RelativeLayout) this.findViewById(R.id.p_info_sign_rl);
		sign_tv = (TextView) this.findViewById(R.id.p_info_sign_tv);
		sign_iv = (ImageView) this.findViewById(R.id.p_info_sign_iv);

		sign_ll = (LinearLayout) this.findViewById(R.id.p_info_sign_edit_ll);
		sign_et = (EditText) this.findViewById(R.id.p_info_sign_et);
		sign_sure_button = (TextView) this
				.findViewById(R.id.p_info_sign_sure_button);
		sign_cancle_button = (TextView) this
				.findViewById(R.id.p_info_sign_cancle_button);

		detail_ll = (LinearLayout) this.findViewById(R.id.p_info_detail_ll);
		edit_detail_ll = (LinearLayout) this
				.findViewById(R.id.p_info_detail_edit_ll);

		sex_tv = (TextView) this.findViewById(R.id.p_info_sex_tv);
		sex_spinner = (Spinner) this.findViewById(R.id.p_info_sex_spinner);
		birthday_tv = (TextView) this.findViewById(R.id.p_info_birthday_tv);
		birthday_et = (EditText) this.findViewById(R.id.p_info_birthday_et);
		address_tv = (TextView) this.findViewById(R.id.p_info_address_tv);
		address_et = (EditText) this.findViewById(R.id.p_info_address_et);
		college_tv = (TextView) this.findViewById(R.id.p_info_college_tv);
		college_et = (EditText) this.findViewById(R.id.p_info_college_et);
		entrance_tv = (TextView) this.findViewById(R.id.p_info_entrance_tv);
		entrance_et = (EditText) this.findViewById(R.id.p_info_entrance_et);
		hobby_tv = (TextView) this.findViewById(R.id.p_info_hobby_tv);
		hobby_et = (EditText) this.findViewById(R.id.p_info_hobby_et);
		editInfo_button = (ImageView) this
				.findViewById(R.id.p_info_editinfo_button);

		editinfo_cancle_button = (TextView) this
				.findViewById(R.id.p_info_editinfo_cancle_button);
		editinfo_sure_button = (TextView) this
				.findViewById(R.id.p_info_editinfo_sure_button);

	}

	private void setVisibility() {
		sign_rl.setVisibility(View.VISIBLE);
		sign_ll.setVisibility(View.GONE);
		detail_ll.setVisibility(View.VISIBLE);
		editInfo_button.setVisibility(View.VISIBLE);

	}

	private void initData() {
		loadPicture();
		if (user != null) {
			nickName_tv.setText(user.getNickName());
			sign_tv.setText(user.getUser_autograph());
			sex_tv.setText(user.getUser_sex());
			birthday_tv.setText(user.getUser_birthday());
			address_tv.setText(user.getUser_address());
			college_tv.setText(user.getUser_college());
			entrance_tv.setText(user.getUser_entrance());
			hobby_tv.setText(user.getUser_hobby());
		}
	}

	/**
	 * 初始化标题栏
	 */
	private void initTitle() {
		new TitleBuilder(this).setTitleText("个人信息")
				.setLeftImage(R.drawable.left).setLeftOnClickListener(this);
	}

	/**
	 * 注册监听
	 */
	private void registerListener() {
		editAvatar_ll.setOnClickListener(this);
		change_p_icon.setOnClickListener(this);
		avatar_iv.setOnClickListener(this);
		sign_iv.setOnClickListener(this);
		
		//个性签名编辑添加监听
		sign_et.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

				if (start != 0) {
					sign_sure_button.setVisibility(View.VISIBLE);
					sign_cancle_button.setVisibility(View.GONE);
				} else {
					sign_cancle_button.setVisibility(View.VISIBLE);
					sign_sure_button.setVisibility(View.GONE);
				}

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});

		sign_sure_button.setOnClickListener(this);
		sign_cancle_button.setOnClickListener(this);

		/* 锟斤拷锟斤拷锟斤拷锟斤拷锟叫憋拷募锟斤拷锟斤拷录锟�*/
		sex_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				sex_Item = (String) sex_spinner.getSelectedItem();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});

		birthday_et.setOnClickListener(this);
		address_et.setOnClickListener(this);
		college_et.setOnClickListener(this);
		entrance_et.setOnClickListener(this);
		hobby_et.setOnClickListener(this);
		editInfo_button.setOnClickListener(this);

		editinfo_cancle_button.setOnClickListener(this);
		editinfo_sure_button.setOnClickListener(this);
	}

	private void loadPicture() {

		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.icon_onloading)
				.showImageForEmptyUri(R.drawable.ic_launcher) 
				.showImageOnFail(R.drawable.icon_loadfail) 
				.cacheInMemory(true) 
				.cacheOnDisk(true) 
				.imageScaleType(ImageScaleType.IN_SAMPLE_INT) 
				.build(); 
		if (user != null) {
			imageLoader.displayImage(Constants.SERVERADDRESS
					+ "/file/avatar/get?imageUrl=" + user.getUser_picPath(),
					avatar_iv, options);
		}

	}

	/**
	 * 保存user信息到服务器
	 */
	public void saveUser() {
		new Thread() {
			@Override
			public void run() {
				Looper.prepare();
				json = OkHttpConnection.execute(Activity_Person_Detail.this,
						url, params);
				JSONObject root;
				try {

					if (json == null) {
						pHandler.sendMessageDelayed(
								pHandler.obtainMessage(FAIL), 3000);
					} else {
						resultCode = json.getString("resultCode");
						if (resultCode == "000") {
							pHandler.sendMessageDelayed(
									pHandler.obtainMessage(FAIL), 3000);
						} else {
							pHandler.sendMessageDelayed(
									pHandler.obtainMessage(SUCCESS), 3000);
						}
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

	protected Handler pHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case SUCCESS:
				Toast.makeText(Activity_Person_Detail.this,"保存成功!",
						Toast.LENGTH_SHORT).show();
				initData();
				break;
			case POST_SUCCESS:
				break;
			case UPLOADPIC_SUCCESS:
				System.out.println("--->>>" + userId + "," + imgName);
				mAppClientDao.Get_ChangeImage(mHandler, userId, imgName);
				break;
			case FAIL:
				Toast.makeText(Activity_Person_Detail.this,"上传失败！",
						Toast.LENGTH_SHORT).show();
				break;
			}
		}
	};

	
	private void JSONToObj() {
		try {

			/* JSONObject root = new JSONObject(json.toString()); */
			JSONObject items = json.getJSONObject("results");
			user = new User();
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
			setResult(5);
			finish();
			break;
		case R.id.p_info_avatar_iv:
			intent  = new Intent(Activity_Person_Detail.this,Activity_BigPicture.class);
			intent.putExtra("picURL",user.getUser_picPath());
			intent.putExtra("picType","avatar");
			startActivity(intent);
			
			break;
		case R.id.change_p_icon:
			openPictureSelectDialog();
			break;
		case R.id.p_info_sign_iv:

			sign_rl.setVisibility(View.GONE);
			sign_ll.setVisibility(View.VISIBLE);
			break;
		case R.id.p_info_sign_cancle_button:
			sign_rl.setVisibility(View.VISIBLE);
			sign_ll.setVisibility(View.GONE);
			break;
		case R.id.p_info_sign_sure_button:
			url = "/user/saveAutograph";
			user.setUser_autograph(String.valueOf(sign_et.getText()));
			params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("userId", String.valueOf(user
					.getUserId())));
			params.add(new BasicNameValuePair("autograph", user
					.getUser_autograph()));
			saveUser();
			sign_ll.setVisibility(View.GONE);
			sign_sure_button.setVisibility(View.GONE);
			sign_rl.setVisibility(View.VISIBLE);
			sign_cancle_button.setVisibility(View.VISIBLE);
			break;
		case R.id.p_info_editinfo_button:
			detail_ll.setVisibility(View.GONE);
			edit_detail_ll.setVisibility(View.VISIBLE);
			break;
		case R.id.p_info_editinfo_cancle_button:
			detail_ll.setVisibility(View.VISIBLE);
			edit_detail_ll.setVisibility(View.GONE);
			break;
		case R.id.p_info_editinfo_sure_button:
			url = "/user/editInfo";
			detail_ll.setVisibility(View.VISIBLE);
			edit_detail_ll.setVisibility(View.GONE);
			user.setUser_sex(sex_Item);
			user.setUser_birthday(birthday_et.getText().toString());
			user.setUser_address(address_et.getText().toString());
			user.setUser_college(college_et.getText().toString());
			user.setUser_entrance(entrance_et.getText().toString());
			user.setUser_hobby(hobby_et.getText().toString());

			params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("userId", String.valueOf(user
					.getUserId())));
			params.add(new BasicNameValuePair("user_sex", user.getUser_sex()));

			params.add(new BasicNameValuePair("user_birthday", user
					.getUser_birthday()));
			params.add(new BasicNameValuePair("user_address", user
					.getUser_address()));
			params.add(new BasicNameValuePair("user_college", user
					.getUser_college()));
			params.add(new BasicNameValuePair("user_entrance", user
					.getUser_entrance()));
			params.add(new BasicNameValuePair("user_hobby", user
					.getUser_hobby()));

			saveUser();

		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == RESULT_OK) { // 锟斤拷锟截成癸拷
			switch (requestCode) {
			case PHOTO_WITH_CAMERA: {// 锟斤拷锟秸伙拷取图片
				String status = Environment.getExternalStorageState();
				if (status.equals(Environment.MEDIA_MOUNTED)) { // 锟角凤拷锟斤拷SD锟斤拷

					startCrop(Uri.fromFile(new File(Environment
							.getExternalStorageDirectory(), "image.jpg")));
				} else {
					Toast.makeText(Activity_Person_Detail.this,"没有sd卡",
							Toast.LENGTH_LONG).show();
				}
				break;
			}
			case PHOTO_WITH_DATA: {// 锟斤拷图锟斤拷锟斤拷选锟斤拷图片

				Uri originalUri = data.getData();
				startCrop(originalUri);
				break;

			}
			case CROP_REQUEST_CODE:
				if (data == null) {
					// TODO 锟斤拷锟街帮拷院锟斤拷锟斤拷锟斤拷霉锟斤拷锟绞局帮拷锟斤拷玫锟酵计�锟斤拷锟斤拷锟斤拷示默锟较碉拷图片
					return;
				}
				Bundle extras = data.getExtras();
				if (extras != null) {
					Bitmap bitmap = extras.getParcelable("data");
					imgByte = getBitmapByte(bitmap);
					imgName = createPhotoFileName();
					savePicture(imgName, bitmap);
					ByteArrayOutputStream stream = new ByteArrayOutputStream();
					bitmap.compress(Bitmap.CompressFormat.JPEG, 75, stream);// (0-100)压锟斤拷锟侥硷拷
					avatar_iv.setImageBitmap(bitmap); // 锟斤拷图片锟斤拷示锟斤拷ImageView锟截硷拷锟斤拷
					uploadPic();
				}

			}
		}
	}

	// 头像图片选择
	private void openPictureSelectDialog() {
		// 
		Context dialogContext = new ContextThemeWrapper(
				Activity_Person_Detail.this, android.R.style.Theme_DeviceDefault_Light_DarkActionBar);
		String[] choiceItems = new String[2];
		choiceItems[0] = "想机拍摄"; 
		choiceItems[1] = "本地相册"; 
		ListAdapter adapter = new ArrayAdapter<String>(dialogContext,
				android.R.layout.simple_list_item_1, choiceItems);
		//对话框弹出选择对话框
		AlertDialog.Builder builder = new AlertDialog.Builder(dialogContext);
		builder.setTitle("添加图片");
		builder.setSingleChoiceItems(adapter, -1,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case 0: //拍照
							doTakePhoto();
							break;
						case 1: //从相册中选择
							doPickPhotoFromGallery();
							break;
						}
						dialog.dismiss();
					}
				});
		builder.create().show();
	}

	// 锟斤拷锟斤拷
	private void doTakePhoto() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); // 锟斤拷锟斤拷系统锟斤拷锟�

		// 指锟斤拷锟斤拷片锟斤拷锟斤拷路锟斤拷锟斤拷SD锟斤拷锟斤拷锟斤拷image.jpg为一锟斤拷锟斤拷时锟侥硷拷锟斤拷每锟斤拷锟斤拷锟秸猴拷锟斤拷锟酵计拷锟斤拷岜伙拷婊�
		Uri imageUri = Uri.fromFile(new File(Environment
				.getExternalStorageDirectory(), "image.jpg"));
		// 直锟斤拷使锟矫ｏ拷没锟斤拷锟斤拷小
		intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

		startActivityForResult(intent, PHOTO_WITH_CAMERA); // 锟矫伙拷锟斤拷锟斤拷舜锟斤拷锟斤拷锟饺�
	}

	/** 锟斤拷锟斤拷锟斤拷取图片 **/
	private void doPickPhotoFromGallery() {
		Intent intent = new Intent(Intent.ACTION_PICK, null);
		intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
				IMAGE_UNSPECIFIED);
		startActivityForResult(intent, PHOTO_WITH_DATA); // 取锟斤拷锟斤拷片锟襟返回碉拷锟斤拷锟斤拷锟斤拷
	}

	// 锟斤拷锟斤拷锟斤拷片路锟斤拷
	private String createPhotoFileName() {
		String fileName = "";
		Date date = new Date(System.currentTimeMillis()); // 系统锟斤拷前时锟斤拷
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"'IMG'_yyyyMMdd_HHmmss");
		fileName = dateFormat.format(date) + ".jpg";
		return fileName;
	}

	/**
	 * 锟斤拷始锟矫硷拷
	 * 
	 * @param uri
	 */
	private void startCrop(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");// 锟斤拷锟斤拷Android系统锟皆达拷锟揭伙拷锟酵计拷锟斤拷锟揭筹拷锟�
		intent.setDataAndType(uri, IMAGE_UNSPECIFIED);
		intent.putExtra("crop", "true");// 锟斤拷锟斤拷锟睫硷拷
		// aspectX aspectY 锟角匡拷叩谋锟斤拷锟�
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 锟角裁硷拷图片锟斤拷锟�
		intent.putExtra("outputX", 500);
		intent.putExtra("outputY", 500);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, CROP_REQUEST_CODE);
	}

	/** 锟斤拷锟斤拷图片锟斤拷锟斤拷应锟斤拷锟斤拷 **/
	private void savePicture(String fileName, Bitmap bitmap) {

		FileOutputStream fos = null;
		try {// 直锟斤拷写锟斤拷锟斤拷萍锟斤拷桑锟矫伙拷谢岜伙拷远锟斤拷锟斤拷锟斤拷锟剿斤拷校锟街伙拷斜锟接︼拷貌锟斤拷芊锟斤拷剩锟斤拷锟斤拷锟斤拷锟斤拷锟叫达拷锟结被锟斤拷锟斤拷
			fos = Activity_Person_Detail.this.openFileOutput(fileName,
					Context.MODE_PRIVATE);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);// 锟斤拷图片写锟斤拷指锟斤拷锟侥硷拷锟斤拷锟斤拷

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != fos) {
					fos.close();
					fos = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/* 锟斤拷bitmap转锟斤拷为锟街凤拷锟斤拷锟斤拷 */
	public byte[] getBitmapByte(Bitmap bitmap) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
		return out.toByteArray();
	}

	// 锟斤拷锟街斤拷锟斤拷锟斤拷转锟斤拷为bitmap
	public Bitmap getBitmapFromByte(byte[] temp) {
		if (temp != null) {
			Bitmap bitmap = BitmapFactory.decodeByteArray(temp, 0, temp.length);
			return bitmap;
		} else {
			return null;
		}
	}

	public void uploadPic() {
		new Thread() {
			@Override
			public void run() {
				Looper.prepare();
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("fileName", imgName));
				params.add(new BasicNameValuePair("fileBody", Base64
						.encodeBytes(imgByte)));
				params.add(new BasicNameValuePair("type", "activity"));
				json = OkHttpConnection.execute(Activity_Person_Detail.this,
						"/file/upload/avatar", params);
				JSONObject root;
				try {
					resultCode = json.getString("resultCode");
					System.out.println("------------" + json.toString());
					if (resultCode == "000") {
						pHandler.sendMessageDelayed(
								pHandler.obtainMessage(FAIL), 3000);
					} else {
						pHandler.sendMessageDelayed(
								pHandler.obtainMessage(UPLOADPIC_SUCCESS), 3000);
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
}
