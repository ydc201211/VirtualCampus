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
import android.widget.TextView;
import android.widget.Toast;

import com.vc.api.AppClientDao;
import com.vc.api.ResultMessage;
import com.vc.app.App;
import com.vc.dialog.DialogUtil;
import common.LocalStorage;

public class LoginActivity extends Activity implements OnClickListener {
	private ImageButton btnleft;
	private Button btn_zhuce, login;
	private TextView tv_quick_login, tv_forget_ps;
	private EditText et_username, et_password;
	Dialog mDialog;
	private Context context;
	String username, password;
	private int userId = 0;
	private int loginId = 0;
	private App application;
	AppClientDao mAppClientDao = new AppClientDao(this);
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (mDialog.isShowing())
				mDialog.dismiss();
			switch (msg.what) {
			case ResultMessage.TIMEOUT:
				Toast.makeText(LoginActivity.this, "连接超时！", Toast.LENGTH_SHORT)
						.show();
				break;
			case ResultMessage.LOGIN_SUCCESS: {
				Object objs = msg.obj;
				userId = Integer.parseInt(objs.toString().trim());
				doLogin();
				break;
			}
			case ResultMessage.LOGIN_FAILED:
				Object objs = msg.obj;
				Toast.makeText(LoginActivity.this, objs.toString(), 0).show();
				break;
			case ResultMessage.FAILED:
				Toast.makeText(LoginActivity.this, "操作失败", 0).show();
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 锟皆讹拷锟斤拷傻姆锟斤拷锟斤拷锟斤拷
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loginactivity);
		application = (App) getApplication();
		Intent intent = this.getIntent();
		loginId = intent.getIntExtra("loginId", -1);
		initview();
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		Bundle bundle;
		switch (v.getId()) {
		case R.id.login_left: {
			intent = new Intent();
			setResult(1);
			this.finish();
			break;
		}
		case R.id.btn_zhuce: {
			intent = new Intent(this, RegisterActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.tv_forget_ps: {
			intent = new Intent(this, ForgetPw_Activity.class);
			startActivity(intent);
			break;
		}
		case R.id.login: {
			username = et_username.getText().toString().trim();
			password = et_password.getText().toString().trim();
			mAppClientDao.Get_Login(mHandler, username, password);
			mDialog.show();
			break;
		}
		}
	}

	void doLogin() {
		LocalStorage.saveString(LoginActivity.this, "userId",
				String.valueOf(userId));
		LocalStorage.saveString(LoginActivity.this, "userName", username);
		LocalStorage.saveString(LoginActivity.this, "password", password);
		application.setUserId(userId);
		Toast.makeText(context, "登陆成功！", 0).show();
		Intent intent = new Intent();
		if (loginId == 3) {
			intent = new Intent(LoginActivity.this, Main.class);
			intent.putExtra("fragId", 3);
			intent.putExtra("userId", userId);
			startActivity(intent);
		} else {
			intent.putExtra("userId", userId);
			setResult(2, intent);
		}
		this.finish();
	}

	void initview() {
		mDialog = DialogUtil.getLoadDialog(this, "登录中..");
		mDialog.setCancelable(true);
		mDialog.setCanceledOnTouchOutside(false);
		mDialog.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				mHandler.removeCallbacksAndMessages(null);
				mDialog.dismiss();
			}
		});
		login = (Button) findViewById(R.id.login);
		btnleft = (ImageButton) findViewById(R.id.login_left);
		btn_zhuce = (Button) findViewById(R.id.btn_zhuce);
		tv_forget_ps = (TextView) findViewById(R.id.tv_forget_ps);
		et_username = (EditText) findViewById(R.id.username);
		et_password = (EditText) findViewById(R.id.password);
		btnleft.setOnClickListener(this);
		btn_zhuce.setOnClickListener(this);
		tv_forget_ps.setOnClickListener(this);
		login.setOnClickListener(this);
		context = this;
	}
}
