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
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.Toast;

import common.Base64;
import common.Constants;
import common.LocalStorage;

import com.vc.api.OkHttpConnection;
import com.vc.dialog.DialogUtil;

public class Activity_Post extends Activity implements OnClickListener {

	private static final String TAG = "PostActivity";

	private EditText theme_et;
	private EditText host_et;
	private EditText time_et;
	private EditText site_et;
	private EditText detail_et;
	private RelativeLayout post_select_pic_rl;
	private ImageView post_show_pic_iv;
	
	private TitleBuilder titleBuilder;
	
	
	// 图片选取
	private static final String IMAGE_UNSPECIFIED = "image/jpg";
	private static final int CROP_REQUEST_CODE = 4;

	private byte[] imgByte; // 头锟斤拷锟斤拷锟�
	private String imgPath = "";
	private String imgName = Constants.DefaulActivityPhotoName;

	private static final int PHOTO_WITH_DATA = 18; // 锟斤拷SD锟斤拷锟叫得碉拷图片
	private static final int PHOTO_WITH_CAMERA = 37;// 锟斤拷锟斤拷锟斤拷片

	private Uri imageUri;

	private List<NameValuePair> params;
	private String userId;

	private JSONObject json;
	private String url = "/activity/postActivity";
	private String resultCode;

	private final int POST_SUCCESS = 2;
	private final int UPLOAD_SUCCESS = 1;
	private final int FAIL = 0;
	private final int POST_FAIL = 0;
	private final int UPLOAD_FAIL = 3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_activity__post);
		
		
		
		userId = LocalStorage.getString(Activity_Post.this, "userId");
		initView();
		initTitle();
		registerListener();

	}

	private void initView() {
		theme_et = (EditText) this.findViewById(R.id.post_theme_et);
		host_et = (EditText) this.findViewById(R.id.post_host_et);
		time_et = (EditText) this.findViewById(R.id.post_time_et);
		site_et = (EditText) this.findViewById(R.id.post_site_et);
		detail_et = (EditText) this.findViewById(R.id.post_detail_et);
		post_select_pic_rl = (RelativeLayout) this
				.findViewById(R.id.post_select_pic_rl);
		post_show_pic_iv = (ImageView) this.findViewById(R.id.post_show_pic_iv);

	}

	private void initTitle() {
		titleBuilder = new TitleBuilder(this);
		titleBuilder.setTitleText("活动发布").setLeftText("取消")
				.setLeftOnClickListener(this).setRightText("发布")
				.setRightOnClickListener(this);

	}

	private void registerListener() {

		time_et.setOnClickListener(this);
		post_select_pic_rl.setOnClickListener(this);

	}

	// 锟斤拷锟斤拷锟斤拷锟�
	public void saveActivity() {
		new Thread() {
			@Override
			public void run() {
				Looper.prepare();

				json = OkHttpConnection
						.execute(Activity_Post.this, url, params);
				JSONObject root;
				try {

					if (json == null) {
						pHandler.sendMessageDelayed(
								pHandler.obtainMessage(POST_FAIL), 3000);
					} else {
						resultCode = json.getString("resultCode");
						if (resultCode == "000") {
							pHandler.sendMessageDelayed(
									pHandler.obtainMessage(POST_FAIL), 3000);
						} else {
							pHandler.sendMessageDelayed(
									pHandler.obtainMessage(POST_SUCCESS), 3000);
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
				json = OkHttpConnection.execute(Activity_Post.this,
						"/file/upload", params);
				JSONObject root;

				try {
					if (json == null) {
						pHandler.sendMessageDelayed(
								pHandler.obtainMessage(UPLOAD_FAIL), 3000);
					} else {
						resultCode = json.getString("resultCode");
						if (resultCode == "000") {
							pHandler.sendMessageDelayed(
									pHandler.obtainMessage(UPLOAD_FAIL), 3000);
						} else {
							pHandler.sendMessageDelayed(
									pHandler.obtainMessage(UPLOAD_SUCCESS),
									3000);
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
			case POST_SUCCESS:
				break;
			case UPLOAD_SUCCESS:

				Toast.makeText(Activity_Post.this,"图片上传成功", Toast.LENGTH_SHORT)
						.show();
				Intent intent = new Intent();
				intent.putExtra("success", "from post");
				setResult(RESULT_OK, intent);
				finish();

				break;
			case UPLOAD_FAIL:
				Toast.makeText(Activity_Post.this, "图片上传失败", Toast.LENGTH_SHORT)
						.show();

				break;
			case FAIL:

				Toast.makeText(Activity_Post.this,"信息发布失败", Toast.LENGTH_SHORT)
						.show();
				break;
			default:
				break;
			}
		}
	};

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent;
		Bundle bundle;
		switch (v.getId()) {
		case R.id.titlebar_tv_left:
			intent = new Intent();
			intent.putExtra("back", "from post");
			setResult(RESULT_CANCELED, intent);
			finish();
			break;
		case R.id.titlebar_tv_right:
			titleBuilder.setRight_TvEnable(false);
			getEditData();
			saveActivity();
			if (imgName != null && imgByte != null)
				uploadPic();
			
			break;
		case R.id.post_select_pic_rl:
			Log.i(TAG, "click");
			openPictureSelectDialog();

			break;
		}
	}

	//编辑图片

	private void getEditData() {
		
		String theme = theme_et.getText().toString();
		Log.i(TAG, theme_et.getText().toString());
		String host = host_et.getText().toString();
		String time = time_et.getText().toString();
		String site = site_et.getText().toString();
		String detail = detail_et.getText().toString();
		if (theme != null) {
			if (host != null) {
				if (time != null) {
					if (site != null) {
						params = new ArrayList<NameValuePair>();
						
						
						params.add(new BasicNameValuePair("action_userId",
								userId));
						params.add(new BasicNameValuePair("action_theme", theme));
						params.add(new BasicNameValuePair("action_host", host));
						params.add(new BasicNameValuePair("action_time", time));
						params.add(new BasicNameValuePair("action_site", theme));
						params.add(new BasicNameValuePair("action_detail",
								theme));
						params.add(new BasicNameValuePair("action_picPath",
								imgName));
					} else {
						Toast.makeText(Activity_Post.this,"地点不能为空",
								Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(Activity_Post.this,"时间不能为空",
							Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(Activity_Post.this, "主持者不能为空", Toast.LENGTH_SHORT)
						.show();
			}
		} else {
			Toast.makeText(Activity_Post.this,"主题不能为空", Toast.LENGTH_SHORT)
					.show();
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode == RESULT_OK) { 
			switch (requestCode) {
			case PHOTO_WITH_CAMERA: {
				String status = Environment.getExternalStorageState();
				if (status.equals(Environment.MEDIA_MOUNTED)) { 

					startCrop(Uri.fromFile(new File(Environment
							.getExternalStorageDirectory(), "image.jpg")));
				
				} else {
					Toast.makeText(Activity_Post.this,"无SD卡",
							Toast.LENGTH_LONG).show();
				}
				break;
			}
			case PHOTO_WITH_DATA: {

				Uri originalUri = data.getData();
				startCrop(originalUri);
			
				break;

			}
			case CROP_REQUEST_CODE:
				if (data == null) {
					
					return;
				}
				Bundle extras = data.getExtras();
				if (extras != null) {
					Bitmap bitmap = extras.getParcelable("data");
					imgByte = getBitmapByte(bitmap);
					Log.i(TAG, imgByte[0] + "ydc");
					imgName = createPhotoFileName();
					Log.i(TAG, imgName);
					savePicture(imgName, bitmap);
					ByteArrayOutputStream stream = new ByteArrayOutputStream();
					bitmap.compress(Bitmap.CompressFormat.JPEG, 75, stream);
					
					post_show_pic_iv.setImageBitmap(bitmap);
				}

			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	
	private void openPictureSelectDialog() {
		
		Context dialogContext = new ContextThemeWrapper(Activity_Post.this,
				android.R.style.Theme_Light);
		String[] choiceItems = new String[2];
		choiceItems[0] = "相机拍摄"; 
		choiceItems[1] = "本地相册"; 
		ListAdapter adapter = new ArrayAdapter<String>(dialogContext,
				android.R.layout.simple_list_item_1, choiceItems);
		
		AlertDialog.Builder builder = new AlertDialog.Builder(dialogContext);
		builder.setTitle("添加图片");
		builder.setSingleChoiceItems(adapter, -1,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case 0: 
							doTakePhoto();
							break;
						case 1: 
							doPickPhotoFromGallery();
							break;
						}
						dialog.dismiss();
					}
				});
		builder.create().show();
	}

	
	private void doTakePhoto() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); 

		
		Uri imageUri = Uri.fromFile(new File(Environment
				.getExternalStorageDirectory(), "image.jpg"));
		
		intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

		startActivityForResult(intent, PHOTO_WITH_CAMERA); 
	}

	
	private void doPickPhotoFromGallery() {
		Intent intent = new Intent(Intent.ACTION_PICK, null);
		intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
				IMAGE_UNSPECIFIED);
		
		 startActivityForResult(intent, PHOTO_WITH_DATA); 
	}

	
	private String createPhotoFileName() {
		String fileName = "";
		Date date = new Date(System.currentTimeMillis()); 
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"'IMG'_yyyyMMdd_HHmmss");
		fileName = dateFormat.format(date) + ".jpg";
		return fileName;
	}

	

	
	private void startCrop(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, IMAGE_UNSPECIFIED);
		intent.putExtra("crop", "true");
		
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		
		intent.putExtra("outputX", 400);
		intent.putExtra("outputY", 600);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, CROP_REQUEST_CODE);
	}

	
	private void savePicture(String fileName, Bitmap bitmap) {

		FileOutputStream fos = null;
		try {
			fos = Activity_Post.this.openFileOutput(fileName,
					Context.MODE_PRIVATE);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);

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

	
	public byte[] getBitmapByte(Bitmap bitmap) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
		return out.toByteArray();
	}

	
	public Bitmap getBitmapFromByte(byte[] temp) {
		if (temp != null) {
			Bitmap bitmap = BitmapFactory.decodeByteArray(temp, 0, temp.length);
			return bitmap;
		} else {
			return null;
		}
	}
}
