package com.vc.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.vc.api.AppClientDao;
import com.vc.api.ResultMessage;
import com.vc.dialog.DialogUtil;
import common.MyApplication;

public class Activity_ChangePsw extends Activity implements OnClickListener {
	private ImageButton btnleft;
	private Button btn_login;
	private EditText edit_phoneNo, edit_ICode;
	private Button sendnews;
	private Dialog mDialog;
	private MyApplication application;
	int code = 0;
	String phoneNo = "";
	AppClientDao mAppClientDao = new AppClientDao(Activity_ChangePsw.this);
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (mDialog.isShowing())
				mDialog.dismiss();
			switch (msg.what) {

			case ResultMessage.SENDSUCCESS: {
				Object objs = msg.obj;
				code = Integer.parseInt(objs.toString().trim());
				Toast.makeText(Activity_ChangePsw.this, "验证码发送成功", 0).show();
				break;
			}
			case ResultMessage.SENDFAILED:
				Object objs = msg.obj;
				Toast.makeText(Activity_ChangePsw.this, objs.toString(), 0)
						.show();
				break;
			case ResultMessage.FAILED:
				Toast.makeText(Activity_ChangePsw.this, "操作失败", 0).show();
				break;
			case ResultMessage.TIMEOUT:
				Toast.makeText(Activity_ChangePsw.this,"连接超时！",
						Toast.LENGTH_SHORT).show();
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.registeractivity);
		application = (MyApplication) getApplication();
		initview();
	}

	@Override
	public void onClick(View v) {
		String phone = edit_phoneNo.getText().toString().trim();
		String Icode = edit_ICode.getText().toString().trim();
		switch (v.getId()) {
		case R.id.btn_zhuce_left: {
			finish();
			break;
		}
		case R.id.btn_next: {
			if (phone != null && !phone.equals("")) {
				if (Icode.equals("" + code)) {
					Intent intent = new Intent(this,
							Activity_ChangePsw_psw.class);
					intent.putExtra("phoneNo", phoneNo);
					startActivity(intent);
					this.finish();
				} else
					Toast.makeText(this, "验证码错误", 0).show();
			} else
				Toast.makeText(this, "请先输入手机号", 0).show();
			break;
		}
		case R.id.sendnews: {
			if (phone != null && !phone.equals("")) {
				phoneNo = edit_phoneNo.getText().toString().trim();
				mAppClientDao.GetSMS_Code(mHandler, phoneNo);
				Toast.makeText(this, "验证码已发送", 0).show();
				mDialog.show();

			} else
				Toast.makeText(this, "请输入手机号", 0).show();
			break;
		}

		}
	}

	void initview() {
		mDialog = DialogUtil.getLoadDialog(this, "请稍后");
		mDialog.setCancelable(true);
		mDialog.setCanceledOnTouchOutside(false);
		mDialog.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				mHandler.removeCallbacksAndMessages(null);
				mDialog.dismiss();
			}
		});
		btnleft = (ImageButton) findViewById(R.id.btn_zhuce_left);
		btnleft.setOnClickListener(this);
		btn_login = (Button) findViewById(R.id.btn_next);
		btn_login.setOnClickListener(this);
		sendnews = (Button) findViewById(R.id.sendnews);
		sendnews.setOnClickListener(this);
		edit_phoneNo = (EditText) findViewById(R.id.edit_phoneNo);
		edit_ICode = (EditText) findViewById(R.id.edit_ICode);
		if (application.getUser() != null)
			edit_phoneNo.setText(application.getUser().getUserName());

	}
}
