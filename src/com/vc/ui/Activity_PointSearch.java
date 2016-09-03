package com.vc.ui;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.vc.adapter.PointAdapter;
import com.vc.api.AppClientDao;
import com.vc.api.ResultMessage;
import com.vc.entity.Mark;

import dto.ActionSimple;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;


public class Activity_PointSearch extends Activity implements OnClickListener{
	
	private RelativeLayout point_search_back;
	private EditText point_search_actv;
	private ArrayAdapter pointAdapter;
	private LinearLayout point_search_ll;
	private ListView point_lv;
	private List<Mark> pointList =new ArrayList<Mark>();
	private String keyword;
	private AppClientDao mAppClientDao = new AppClientDao(this);
	private JSONObject json;
	private int count_1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_point_search);
		
		initView();
		
	}
	
	
	protected Handler pHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case ResultMessage.GET_MARKS_PROMPT_SUCCESS:
				Object objs = (Object) msg.obj;
				try {
					pointList.clear();
					json = new JSONObject(objs.toString());
					jsonToObj();
					point_search_ll.setVisibility(View.VISIBLE);
					Log.i("pppllll",pointList.get(0).getMarkName());
					pointAdapter.notifyDataSetChanged();
					
				} catch (JSONException e) {
					e.printStackTrace();
				}
				break;
				
			case ResultMessage.GET_MARKS_PROMPT_FAIL:
				
				break;
			}
		}
	};
	
	private void initView(){
		point_search_back = (RelativeLayout) this.findViewById(R.id.point_search_back);
		point_search_actv = (EditText) this.findViewById(R.id.point_search_actv);
		point_lv = (ListView) this.findViewById(R.id.point_lv);
		point_search_ll = (LinearLayout) this.findViewById(R.id.point_search_ll);
		InputMethodManager imm = (InputMethodManager)point_search_actv
				.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
		point_search_back.setOnClickListener(this);
		point_search_actv.requestFocus();
		
		point_search_actv.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				Log.i("s-", s+"");
				Log.i("start", start+"");
				Log.i("before", before+"");
				Log.i("count", count+"");
				if(s != null && (start==0 && before == 0 && count == 1) 
						|| s != null && (start==1 && before == 1 && count == 0)
						|| s != null && start!=0){
					keyword = s.toString();
					
					mAppClientDao.getPointList(pHandler,keyword);
				}else{
					pointList.clear();
					point_search_ll.setVisibility(View.GONE);
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
		
		point_lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Mark point = pointList.get(position);
				Log.i("ydc",point.getMarkName());

				Intent intent = new Intent();
				
				intent.putExtra("point",point);
				
				setResult(-1,intent);
				finish();
			}
		});
		
		pointAdapter = new PointAdapter(this,R.layout.point_search_item,pointList);
		point_lv.setAdapter(pointAdapter);
		
	}
	
	
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent;

		switch (v.getId()) {
		case R.id.point_search_back:
			intent = new Intent();
			intent.putExtra("back", "from Activity_PointSearch");
			setResult(RESULT_CANCELED, intent);
			finish();
			break;
		}
	}
	
	private void jsonToObj() {
		try {

		
			JSONArray items = json.getJSONArray("results");
			for (int i = 0; i < items.length(); i++) {
				Mark mark = new Mark();
				mark.setMarkId((Integer.parseInt(items.getJSONObject(i)
						.getString("markId"))));

				mark.setMarkName(items.getJSONObject(i).getString(
						"markName"));

				mark.setMark_longitude(Double.parseDouble((items.getJSONObject(i).getString(
						"mark_longitude"))));
				mark.setMark_latitude(Double.parseDouble((items.getJSONObject(i).getString(
						"mark_latitude"))));
				mark.setMark_detail(items.getJSONObject(i).getString(
						"mark_detail"));
				mark.setMark_isOnMap(Integer.parseInt((items.getJSONObject(i).getString(
						"mark_isOnMap"))));
				
				mark.setMark_like(Integer.parseInt((items.getJSONObject(i).getString(
						"mark_like"))));
				
				mark.setMark_parentId((Integer.parseInt(items.getJSONObject(i)
						.getString("mark_parentId"))));
				pointList.add(mark);

			}
		} catch (JSONException e) {

			e.printStackTrace();
		}
	}
	
	
}
