package com.vc.ui;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.vc.adapter.ComAdapter;
import com.vc.adapter.ViewHolder;
import com.vc.api.AppClientDao;
import com.vc.api.ResultMessage;
import com.vc.entity.Mark;

public class Lines_Choose_Activity extends Activity implements OnClickListener {
	private EditText et_start, et_end;
	private Button btn_guide;
	private ImageView left_back;
	ListView msListView;
	String content;
	private List<Mark> mSerachInfoList = new ArrayList<Mark>();
	private ComAdapter<Mark> sugadapter;
	private AppClientDao mAppClientDao = new AppClientDao(
			Lines_Choose_Activity.this);
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
						msListView.setVisibility(View.VISIBLE);
						initAdapter();
					} else {
						sugadapter.notifyDataSetChanged();
						Toast.makeText(Lines_Choose_Activity.this, "没有相关数据", 0)
								.show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
				break;
			case ResultMessage.GET_SEARACHMARKS_FAILED:
				Toast.makeText(Lines_Choose_Activity.this, msg.obj.toString(),
						Toast.LENGTH_SHORT).show();
				break;
			case ResultMessage.FAILED:
				Toast.makeText(Lines_Choose_Activity.this, msg.obj.toString(),
						Toast.LENGTH_SHORT).show();
				break;
			case ResultMessage.TIMEOUT:
				Toast.makeText(Lines_Choose_Activity.this, msg.obj.toString(),
						Toast.LENGTH_SHORT).show();
				break;
			}

		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.lines_choose_activity);
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		et_end = (EditText) findViewById(R.id.et_end);
		et_start = (EditText) findViewById(R.id.et_start);
		btn_guide = (Button) findViewById(R.id.btn_guide);
		left_back = (ImageView) findViewById(R.id.left_back);
		btn_guide.setOnClickListener(this);
		left_back.setOnClickListener(this);
		et_start.addTextChangedListener(textWatcher);
		et_end.addTextChangedListener(textWatcher);
		et_start.setFocusable(true);
		msListView = (ListView) findViewById(R.id.suggest_list2);
		msListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				View adapterview = view;
				TextView text1 = (TextView) view
						.findViewById(R.id.suggest_text);
				String context = text1.getText().toString();
				msListView.setVisibility(View.GONE);
				if (et_start.hasFocus()) {
					et_start.setText(context);
				} else if (et_end.hasFocus()) {
					et_end.setText(context);
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		Bundle bundle;
		switch (v.getId()) {
		case R.id.btn_guide: {
			intent = new Intent();
			bundle = new Bundle();
			String start = et_start.getText().toString().trim();
			String end = et_end.getText().toString().trim();
			if (start.length() > 0 && end.length() > 0) {
				bundle.putString("start", start);
				bundle.putString("end", end);
				setResult(6, this.getIntent().putExtras(bundle));
				this.finish();
			} else
				Toast.makeText(Lines_Choose_Activity.this, "起始点不能为空！", 0)
						.show();
			break;
		}

		case R.id.left_back: {
			intent = new Intent();
			setResult(5, this.getIntent());
			this.finish();
		}
			break;
		}
	}

	private TextWatcher textWatcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			content = s.toString();
			if (s.length() != 0) {
				msListView.setVisibility(View.VISIBLE);
				mAppClientDao.GetSearchMarks(mHandler, content);
				System.out.println("===>>>onTextChanged" + content);
			} else {
				System.out.println("===>>>onTextChanged" + s);
				mSerachInfoList.clear();
				sugadapter.notifyDataSetChanged();
				msListView.setVisibility(View.GONE);
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

	// 初始化适配器
	private void initAdapter() {
		sugadapter = new ComAdapter<Mark>(this, mSerachInfoList,
				R.layout.suggestion_item) {
			@SuppressLint("NewApi")
			@Override
			public void convert(ViewHolder helper, Mark item, int position) {
				// LatLng p1 = new LatLng(mLatitude, mLongtitude);
				// LatLng p2 = new LatLng(item.getMark_latitude(),
				// item.getMark_longitude());
				// double distance = DistanceUtil.getDistance(p1, p2);// 计算距离
				// // DecimalFormat df = new DecimalFormat("######0.00");
				// // distance = df.format(distance);
				// // distance = Math.floor(distance);
				// int dista = (int) distance;
				helper.setText(R.id.suggest_text, item.getMarkName());
				helper.setText(R.id.mark_ID, "" + item.getMarkId());
				// helper.setText(R.id.suggest_distance, "" + dista + "米");
			}

		};
		msListView.setAdapter(sugadapter);
		sugadapter.notifyDataSetChanged();
	}
}
