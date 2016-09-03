package com.vc.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.vc.api.AppClientDao;
import com.vc.api.Pic_Down_Thread;
import com.vc.api.ResultMessage;
import com.vc.dialog.DialogUtil;
import com.vc.entity.MarkEntity;

public class InfoDetail_Activity extends Activity implements OnClickListener {
	private ImageView left_back, image_mark, image_1;
	private TextView mark_name, mark_detail;
	private AppClientDao mAppClientDao = new AppClientDao(
			InfoDetail_Activity.this);
	private LinearLayout go_there;
	Dialog mDialog;
	MarkEntity mark;
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (mDialog.isShowing())
				mDialog.dismiss();
			switch (msg.what) {
			case ResultMessage.GET_MARK_PICNAME_SUCCESS: {
				Object objs = (Object) msg.obj;
				String PicName = objs.toString();
				new Pic_Down_Thread(image_mark, mHandler, PicName).start();
			}
				break;
			case ResultMessage.GET_MARK_PICNAME_FAILED:
				Toast.makeText(InfoDetail_Activity.this, msg.obj.toString(),
						Toast.LENGTH_SHORT).show();
				break;
			case ResultMessage.FAILED:
				Toast.makeText(InfoDetail_Activity.this, msg.obj.toString(),
						Toast.LENGTH_SHORT).show();
				break;
			case ResultMessage.TIMEOUT:
				Toast.makeText(InfoDetail_Activity.this, msg.obj.toString(),
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
		setContentView(R.layout.infodetail_activity);
		mDialog = DialogUtil.getLoadDialog(this, "数据加载中...");
		mDialog.setCancelable(true);
		mDialog.setCanceledOnTouchOutside(false);
		mDialog.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				mHandler.removeCallbacksAndMessages(null);
				mDialog.dismiss();
			}
		});
		initview();// ��ʼ���ؼ�
		Bundle bundle = getIntent().getExtras();
		mark = (MarkEntity) bundle.getSerializable("info");
		mAppClientDao.GetMark_PicName(mHandler, mark.getMarkId());
		mDialog.show();
		initdata(mark);
	}

	private void initdata(MarkEntity mark1) {
		// TODO Auto-generated method stub
		mark_name.setText(mark1.getMarkName());
		mark_detail.setText(mark1.getMark_detail());
	}

	private void initview() {
		// TODO Auto-generated method stub
		image_mark = (ImageView) findViewById(R.id.detail_image);
		left_back = (ImageView) findViewById(R.id.left_back);
		mark_name = (TextView) findViewById(R.id.mark_name);
		mark_detail = (TextView) findViewById(R.id.mark_detail);
		go_there = (LinearLayout) findViewById(R.id.go_there);
		go_there.setOnClickListener(this);
		left_back.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		Bundle bundle;
		switch (v.getId()) {
		case R.id.left_back: {
			intent = new Intent();
			setResult(11, this.getIntent());
			this.finish();
			break;
		}
		case R.id.go_there: {
			intent = new Intent();
			setResult(12, this.getIntent());
			this.finish();
			break;
		}
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent();
			setResult(11, this.getIntent());
			this.finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);

	}
}
