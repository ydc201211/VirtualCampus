package com.vc.ui;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.vc.adapter.ComAdapter;
import com.vc.adapter.ViewHolder;
import com.vc.api.AppClientDao;
import com.vc.api.ResultMessage;
import com.vc.entity.Mark;

public class Activity_MapSerach extends Activity implements OnClickListener {
	private ImageButton image_left;
	private Button btn_serach;
	private EditText mapserach_edit;
	private String content = "";
	private Context context;
	ListView suggest_listView;
	private List<Mark> mSerachInfoList = new ArrayList<Mark>();
	private ComAdapter<Mark> sugadapter;
	private LinearLayout view_college, view_schoolroom, view_id_3, view_id_4,
			view_id_5, view_id_6;
	private LinearLayout view_id_7, view_id_8, view_id_9, lin_, suggest_lin;
	private double mLongtitude = 0, mLatitude = 0;

	private AppClientDao mAppClientDao = new AppClientDao(
			Activity_MapSerach.this);
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case ResultMessage.GET_SEARACHMARKS_SUCCESS: {
				Object objs = (Object) msg.obj;
				mSerachInfoList.clear();
				try {
					String arrayString = new JSONObject(objs.toString())
							.getString("results");
					mSerachInfoList = JSON.parseArray(arrayString, Mark.class);
					if (mSerachInfoList.size() > 0) {
						initAdapter();
					} else {
						sugadapter.notifyDataSetChanged();
						lin_.setVisibility(View.VISIBLE);
						suggest_lin.setVisibility(View.GONE);
						btn_serach.setVisibility(View.GONE);
						Toast.makeText(context, "没有相关数据", 0).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
				break;
			case ResultMessage.GET_SEARACHMARKS_FAILED:
				Toast.makeText(Activity_MapSerach.this, msg.obj.toString(),
						Toast.LENGTH_SHORT).show();
				break;
			case ResultMessage.FAILED:
				Toast.makeText(Activity_MapSerach.this, msg.obj.toString(),
						Toast.LENGTH_SHORT).show();
				break;
			case ResultMessage.TIMEOUT:
				Toast.makeText(Activity_MapSerach.this, msg.obj.toString(),
						Toast.LENGTH_SHORT).show();
				break;
			}

		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_mapserach);
		Bundle bundle = getIntent().getExtras();
		mLongtitude = bundle.getDouble("mLongtitude");
		mLatitude = bundle.getDouble("mLatitude");
		context = this.getApplicationContext();
		initview();
	}

	void initview() {
		image_left = (ImageButton) findViewById(R.id.imagebtn_left);
		btn_serach = (Button) findViewById(R.id.btn_search_submit);
		mapserach_edit = (EditText) findViewById(R.id.mapsearch_edit);
		lin_ = (LinearLayout) findViewById(R.id.lin_ID);
		suggest_lin = (LinearLayout) findViewById(R.id.suggest_lin);
		view_schoolroom = (LinearLayout) findViewById(R.id.serach_Id_2);
		view_college = (LinearLayout) findViewById(R.id.serach_Id_1);
		view_id_3 = (LinearLayout) findViewById(R.id.serach_Id_3);
		view_id_4 = (LinearLayout) findViewById(R.id.serach_Id_4);
		view_id_5 = (LinearLayout) findViewById(R.id.serach_Id_5);
		view_id_6 = (LinearLayout) findViewById(R.id.serach_Id_6);
		view_id_7 = (LinearLayout) findViewById(R.id.serach_Id_7);
		view_id_8 = (LinearLayout) findViewById(R.id.serach_Id_8);
		view_id_9 = (LinearLayout) findViewById(R.id.serach_Id_9);
		image_left.setOnClickListener(this);
		btn_serach.setOnClickListener(this);
		mapserach_edit.requestFocus();
		InputMethodManager imm = (InputMethodManager) mapserach_edit
				.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
		mapserach_edit.addTextChangedListener(textWatcher);
		view_college.setOnClickListener(this);
		view_schoolroom.setOnClickListener(this);
		view_id_3.setOnClickListener(this);
		view_id_4.setOnClickListener(this);
		view_id_5.setOnClickListener(this);
		view_id_6.setOnClickListener(this);
		view_id_7.setOnClickListener(this);
		view_id_8.setOnClickListener(this);
		view_id_9.setOnClickListener(this);
		suggest_listView = (ListView) findViewById(R.id.suggest_list);
		suggest_listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				View adapterview = view;
				TextView text1 = (TextView) view
						.findViewById(R.id.suggest_text);
				TextView text2 = (TextView) view.findViewById(R.id.mark_ID);
				sugadapter.notifyDataSetChanged();
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				System.out.println("===>>>" + text1.getText().toString()
						+ text2.getText().toString());
				String context = text1.getText().toString();
				String markID = text2.getText().toString();
				bundle.putString("key", context);
				bundle.putString("address", markID);
				// intent.putExtra("serach", bundle);
				// setResult(8, intent);
				bundle.putString("content", context.trim());
				setResult(2, intent.putExtras(bundle));
				finish();
			}
		});
	}

	private TextWatcher textWatcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			mSerachInfoList.clear();
			// sugadapter.notifyDataSetChanged();
			if (s.length() != 0) {
				btn_serach.setVisibility(View.VISIBLE);
				lin_.setVisibility(View.GONE);
				suggest_lin.setVisibility(View.VISIBLE);
				content = mapserach_edit.getText().toString();
				mAppClientDao.GetSearchMarks(mHandler, content);
				System.out.println("===>>>onTextChanged" + s.length());
			} else {
				System.out.println("===>>>onTextChanged" + s);
				lin_.setVisibility(View.VISIBLE);
				suggest_lin.setVisibility(View.GONE);
				btn_serach.setVisibility(View.GONE);
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

	// 鍒濆鍖栭�傞厤鍣�
	private void initAdapter() {
		sugadapter = new ComAdapter<Mark>(this, mSerachInfoList,
				R.layout.suggestion_item) {
			@SuppressLint("NewApi")
			@Override
			public void convert(ViewHolder helper, Mark item, int position) {
				LatLng p1 = new LatLng(mLatitude, mLongtitude);
				LatLng p2 = new LatLng(item.getMark_latitude(),
						item.getMark_longitude());
				double distance = DistanceUtil.getDistance(p1, p2);
				// DecimalFormat df = new DecimalFormat("######0.00");
				// distance = df.format(distance);
				// distance = Math.floor(distance);
				int dista = (int) distance;
				helper.setText(R.id.suggest_text, item.getMarkName());
				helper.setText(R.id.mark_ID, "" + item.getMarkId());
				helper.setText(R.id.suggest_distance, "" + dista + "米");
			}

		};
		suggest_listView.setAdapter(sugadapter);
		sugadapter.notifyDataSetChanged();
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		Bundle bundle;
		Bundle bundle1 = getIntent().getExtras();
		switch (v.getId()) {
		case R.id.imagebtn_left: {
			intent = new Intent();
			setResult(13);
			this.finish();
		}
			break;
		case R.id.btn_search_submit: {
			intent = new Intent();
			bundle = new Bundle();
			bundle.putString("content", content.trim());
			setResult(2, this.getIntent().putExtras(bundle));
			this.finish();
		}
			break;
		case R.id.serach_Id_1: {
			intent = new Intent(this, Activity_Serach_Detail.class);
			intent.putExtra("MarkFlag", 1);
			intent.putExtras(bundle1);
			startActivityForResult(intent, 1);
		}
			break;
		case R.id.serach_Id_2: {
			intent = new Intent(this, Activity_Serach_Detail.class);
			intent.putExtra("MarkFlag", 2);
			intent.putExtras(bundle1);
			startActivityForResult(intent, 1);
		}
			break;
		case R.id.serach_Id_3: {
			intent = new Intent(this, Activity_Serach_Detail.class);
			intent.putExtra("MarkFlag", 3);
			intent.putExtras(bundle1);
			startActivityForResult(intent, 1);
		}
			break;
		case R.id.serach_Id_4: {
			intent = new Intent(this, Activity_Serach_Detail.class);
			intent.putExtra("MarkFlag", 4);
			intent.putExtras(bundle1);
			startActivityForResult(intent, 1);
		}
			break;
		case R.id.serach_Id_5: {
			intent = new Intent(this, Activity_Serach_Detail.class);
			intent.putExtra("MarkFlag", 5);
			intent.putExtras(bundle1);
			startActivityForResult(intent, 1);
		}
			break;
		case R.id.serach_Id_6: {
			intent = new Intent(this, Activity_Serach_Detail.class);
			intent.putExtra("MarkFlag", 6);
			intent.putExtras(bundle1);
			startActivityForResult(intent, 1);
		}
			break;
		case R.id.serach_Id_7: {
			intent = new Intent(this, Activity_Serach_Detail.class);
			intent.putExtra("MarkFlag", 7);
			intent.putExtras(bundle1);
			startActivityForResult(intent, 1);
		}
			break;
		case R.id.serach_Id_8: {
			intent = new Intent(this, Activity_Serach_Detail.class);
			intent.putExtra("MarkFlag", 8);
			intent.putExtras(bundle1);
			startActivityForResult(intent, 1);
		}
			break;

		case R.id.serach_Id_9: {
			intent = new Intent(this, Activity_Serach_Detail.class);
			intent.putExtra("MarkFlag", 9);
			intent.putExtras(bundle1);
			startActivityForResult(intent, 1);
		}
			break;
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1 && resultCode == 8) {
			Intent intent = new Intent();
			Bundle bundle = data.getExtras();
			int markId = bundle.getInt("markId");
			bundle = new Bundle();
			bundle.putInt("markId", markId);
			System.out.print("mark===>>>" + markId);
			setResult(9, this.getIntent().putExtras(bundle));
			this.finish();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent();
			setResult(3, this.getIntent());
			this.finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}