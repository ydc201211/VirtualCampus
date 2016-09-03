package com.vc.ui;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.vc.adapter.ComAdapter;
import com.vc.adapter.ViewHolder;
import com.vc.api.AppClientDao;
import com.vc.api.ResultMessage;
import com.vc.dialog.DialogUtil;
import com.vc.entity.MarkEntity;

public class Activity_Serach_Detail extends Activity implements OnClickListener {
	private ComAdapter<MarkEntity> mAdapter;
	private ListView listinfo;
	private ImageView image_left;
	private int MarkFlag = 1;
	TextView title;
	private static final double EARTH_RADIUS = 6378137.0;
	Dialog mDialog;
	private double myLatitude, myLongtitude;
	private double endLatitude = 0, endLongtitude = 0;
	private double distance = 0;
	private AppClientDao mAppClientDao = new AppClientDao(this);
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (mDialog.isShowing())
				mDialog.dismiss();
			switch (msg.what) {
			case ResultMessage.GET_MARK_BYPARENTID_SUCCESS: {
				Object objs = (Object) msg.obj;
				try {
					String arrayString = new JSONObject(objs.toString())
							.getString("results");
					List<MarkEntity> resList = JSON.parseArray(arrayString,
							MarkEntity.class);
					UpdateView(resList);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
				break;
			case ResultMessage.GET_MARK_BYPARENTID_FAILED:
				Toast.makeText(Activity_Serach_Detail.this, msg.obj.toString(),
						Toast.LENGTH_SHORT).show();
				break;
			case ResultMessage.FAILED:
				Toast.makeText(Activity_Serach_Detail.this, msg.obj.toString(),
						Toast.LENGTH_SHORT).show();
				break;
			case ResultMessage.TIMEOUT:
				Toast.makeText(Activity_Serach_Detail.this, msg.obj.toString(),
						Toast.LENGTH_SHORT).show();
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_serach_detail);
		mDialog = DialogUtil.getLoadDialog(this, "加载中..");
		mDialog.setCancelable(true);
		mDialog.setCanceledOnTouchOutside(false);
		mDialog.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				mHandler.removeCallbacksAndMessages(null);
				mDialog.dismiss();
			}
		});
		Intent _getIntent = this.getIntent();
		MarkFlag = _getIntent.getExtras().getInt("MarkFlag");

		myLatitude = _getIntent.getExtras().getDouble("mLatitude");
		myLongtitude = _getIntent.getExtras().getDouble("mLongtitude");

		mAppClientDao.GetMarkByParentId(mHandler, MarkFlag);
		mDialog.show();
		listinfo = (ListView) findViewById(R.id.listinfos);
		image_left = (ImageView) findViewById(R.id.image_left);
		title = (TextView) findViewById(R.id.title);
		image_left.setOnClickListener(this);
		changeTitle(MarkFlag);
		listinfo.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				ViewHolder holder = (ViewHolder) view.getTag();
				TextView tv = holder.getView(R.id.MarkId);
				int markId = Integer.parseInt(tv.getText().toString().trim());
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putInt("markId", markId);
				setResult(
						8,
						Activity_Serach_Detail.this.getIntent().putExtras(
								bundle));
				Activity_Serach_Detail.this.finish();
			}

		});
	}

	private void changeTitle(int markFlag2) {
		// TODO Auto-generated method stub
		switch (markFlag2) {
		case 1:
			title.setText("学院");
			break;
		case 2:
			title.setText("教学楼");
			break;
		case 3:
			title.setText("学生宿舍");
			break;
		case 4:
			title.setText("学校景观");
			break;
		case 5:
			title.setText("饮食");
			break;
		case 6:
			title.setText("商店");
			break;
		case 7:
			title.setText("生活服务");
			break;
		case 8:
			title.setText("教室公寓");
			break;
		case 9:
			title.setText("其他");
			break;
		default:
			break;
		}
	}

	private void UpdateView(List<MarkEntity> resList) {
		mAdapter = new ComAdapter<MarkEntity>(this, resList,
				R.layout.item_detail) {
			@SuppressLint("NewApi")
			@Override
			public void convert(ViewHolder helper, MarkEntity markEntity,
					int position) {
				distance = getDistance(myLongtitude, myLatitude,
						markEntity.mark_longitude, markEntity.mark_latitude);
				helper.setText(R.id.name, markEntity.markName);
				helper.setText(R.id.address, markEntity.mark_detail);
				helper.setText(R.id.MarkId, "" + markEntity.markId);
				helper.setText(R.id.distance, "" + distance + "米");
			}
		};
		listinfo.setAdapter(mAdapter);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.image_left:
			this.finish();
			break;

		default:
			break;
		}
	}

	public static double getDistance(double longitude1, double latitude1,
			double longitude2, double latitude2) {
		double Lat1 = rad(latitude1);
		double Lat2 = rad(latitude2);
		double a = Lat1 - Lat2;
		double b = rad(longitude1) - rad(longitude2);
		double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
				+ Math.cos(Lat1) * Math.cos(Lat2)
				* Math.pow(Math.sin(b / 2), 2)));
		s = s * EARTH_RADIUS;
		s = Math.round(s * 10000) / 10000;
		return s;
	}

	private static double rad(double d) {
		return d * Math.PI / 180.0;
	}
}
