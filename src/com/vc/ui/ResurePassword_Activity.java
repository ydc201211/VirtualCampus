package com.vc.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
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
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

import com.vc.api.AppClientDao;
import com.vc.api.ResultMessage;
import com.vc.dialog.DialogUtil;

public class ResurePassword_Activity extends Activity implements
		OnClickListener {
	private ImageButton btn_left;
	private Button btn_next;
	private EditText et_nickName, et_psw1, et_psw2;
	Dialog mDialog;
	private Context context;
	private String phoneNo;
	private RadioGroup sex_group;
	private String sex = "男";
	AppClientDao mAppClientDao = new AppClientDao(this);
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (mDialog.isShowing())
				mDialog.dismiss();
			switch (msg.what) {
			case ResultMessage.TIMEOUT:
				Toast.makeText(ResurePassword_Activity.this, "连接超时",
						Toast.LENGTH_SHORT).show();
				break;
			case ResultMessage.REGSITE_SUCCESS: {
				Object objs = msg.obj;
				int userId = Integer.parseInt(objs.toString().trim());
				Toast.makeText(ResurePassword_Activity.this, "注册成功", 0).show();
				Intent intent = new Intent(context, LoginActivity.class);
				intent.putExtra("loginId", 3);
				startActivity(intent);
				break;
			}
			case ResultMessage.REGSITE_FAILED:
				Object objs = msg.obj;
				Toast.makeText(ResurePassword_Activity.this, objs.toString(), 0)
						.show();
				break;
			case ResultMessage.FAILED:
				Toast.makeText(ResurePassword_Activity.this, "操作失败", 0).show();
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 锟皆讹拷锟斤拷傻姆锟斤拷锟斤拷锟斤拷
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setpw_activity);
		initView();
		Intent _intent = this.getIntent();
		phoneNo = _intent.getExtras().getString("phoneNo");
		// Toast.makeText(ResurePassword_Activity.this, phoneNo, 0).show();
	}

	@Override
	public void onClick(View v) {
		// TODO 锟皆讹拷锟斤拷傻姆锟斤拷锟斤拷锟斤拷
		switch (v.getId()) {
		case R.id.btn_Ps_left: {
			finish();
		}
			break;
		case R.id.btn_next: {
			String nickName = et_nickName.getText().toString().trim();
			String psw1 = et_psw1.getText().toString().trim();
			String psw2 = et_psw2.getText().toString().trim();
			if (psw1.equals(psw2)) {
				mAppClientDao.Get_Registe(mHandler, phoneNo, psw1, nickName,
						"avatar.jpg", sex);
				mDialog.show();
			} else {
				Toast.makeText(this, "两次密码不一致", 0).show();
			}
		}
			break;

		}
	}

	void initView() {
		mDialog = DialogUtil.getLoadDialog(this, "注册中...");
		mDialog.setCancelable(true);
		mDialog.setCanceledOnTouchOutside(false);
		mDialog.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				mHandler.removeCallbacksAndMessages(null);
				mDialog.dismiss();
			}
		});
		btn_left = (ImageButton) findViewById(R.id.btn_Ps_left);
		btn_left.setOnClickListener(this);
		et_nickName = (EditText) findViewById(R.id.edit_Nick_Name);
		et_psw1 = (EditText) findViewById(R.id.edit_pw_1);
		et_psw2 = (EditText) findViewById(R.id.edit_pw_2);
		btn_next = (Button) findViewById(R.id.btn_next);
		btn_next.setOnClickListener(this);
		context = this;
		sex_group = (RadioGroup) findViewById(R.id.sex_group);
		sex_group.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId == 1) {
					sex = "男";
				} else
					sex = "女";
			}
		});
	}
}
