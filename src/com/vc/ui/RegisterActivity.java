package com.vc.ui;

import android.app.Activity;
import android.app.AlertDialog;
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
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

import com.vc.api.AppClientDao;
import com.vc.api.ResultMessage;
import com.vc.dialog.DialogUtil;

public class RegisterActivity extends Activity implements OnClickListener {
	private ImageButton btnleft;
	private Button btn_login;
	private EditText edit_phoneNo, edit_ICode;
	private Button sendnews;
	private Dialog mDialog;
	int code = 0;
	String phoneNo = "";
	private boolean ready = false;
	private static final int CODE_ING = 1; // 已发送，倒计时
	private static final int CODE_REPEAT = 2; // 重新发送
	private static final int SMSDDK_HANDLER = 3; // 短信回调
	private int TIME = 60;// 倒计时60s
	AppClientDao mAppClientDao = new AppClientDao(RegisterActivity.this);
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (mDialog.isShowing())
				mDialog.dismiss();
			switch (msg.what) {
			case ResultMessage.TIMEOUT:
				Toast.makeText(RegisterActivity.this, "连接超时！",
						Toast.LENGTH_SHORT).show();
				break;
			case ResultMessage.SENDSUCCESS: {
				// Object objs = msg.obj;
				// code = Integer.parseInt(objs.toString().trim());
				Toast.makeText(RegisterActivity.this, "验证码发送成功！", 0).show();
				break;
			}
			case ResultMessage.SENDFAILED:
				Object objs = msg.obj;
				Toast.makeText(RegisterActivity.this, objs.toString(), 0)
						.show();
				break;
			case ResultMessage.FAILED:
				Toast.makeText(RegisterActivity.this, "操作失败", 0).show();
				break;

			case ResultMessage.CHECK_SUCCESS: {
				new AlertDialog.Builder(RegisterActivity.this)
						.setTitle("发送短信")
						.setMessage("我们将把验证码发送到以下号码:\n" + "+86:" + phoneNo)
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										cn.smssdk.SMSSDK.getVerificationCode(
												"86", phoneNo);
										sendnews.setClickable(false);
										new Thread(new Runnable() {
											@Override
											public void run() {
												for (int i = 60; i > 0; i--) {
													mHandler.sendEmptyMessage(CODE_ING);
													if (i <= 0) {
														break;
													}
													try {
														Thread.sleep(1000);
													} catch (InterruptedException e) {
														e.printStackTrace();
													}
												}
												mHandler.sendEmptyMessage(CODE_REPEAT);
											}
										}).start();
									}
								}).create().show();

				// mDialog.show();
				break;
			}
			case ResultMessage.CHECK_FAILED:
				Object obj = msg.obj;
				Toast.makeText(RegisterActivity.this, "该账户已被注册，请更换手机号或者找回密码！",
						0).show();
				break;
			case SMSDDK_HANDLER: {
				Toast.makeText(RegisterActivity.this, "验证成功！", 1).show();
				Intent intent = new Intent(RegisterActivity.this,
						ResurePassword_Activity.class);
				intent.putExtra("phoneNo", phoneNo);
				startActivity(intent);
				break;
			}
			case CODE_ING:// 已发送,倒计时
				sendnews.setText("重新发送(" + --TIME + "s)");
				break;
			case CODE_REPEAT:// 重新发送
				sendnews.setText("获取验证码");
				sendnews.setClickable(true);
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.registeractivity);
		initview();
		initSDK();
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
				// if (Icode.equals("" + code)) {
				// Intent intent = new Intent(this,
				// ResurePassword_Activity.class);
				// intent.putExtra("phoneNo", phoneNo);
				// startActivity(intent);
				// } else
				// Toast.makeText(this, "验证码错误！", 0).show();
				System.out.println("--->>>" + phone + "," + Icode);
				cn.smssdk.SMSSDK.submitVerificationCode("86", phone, Icode);
			} else
				Toast.makeText(this, "请先输入手机号！", 0).show();
			break;
		}
		case R.id.sendnews: {
			if (phone != null && !phone.equals("")) {
				phoneNo = edit_phoneNo.getText().toString().trim();
				mAppClientDao.Get_CheckAccount(mHandler, phoneNo);
				mDialog.show();
				break;
			} else
				Toast.makeText(this, "请先输入手机号！", 0).show();
			break;
		}

		}
	}

	private void initSDK() {
		cn.smssdk.SMSSDK.initSDK(this, "15b9c6e107d4b",
				"694ce0d07e23d743ffa9dce378c4fcdd");
		EventHandler eh = new EventHandler() {
			@Override
			public void afterEvent(int event, int result, Object data) {

				if (result == SMSSDK.RESULT_COMPLETE) {
					// 回调完成
					if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
						mHandler.obtainMessage(SMSDDK_HANDLER, "true")
								.sendToTarget();
						System.out.println("--->>>验证验证码成功");
					} else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
						mHandler.obtainMessage(ResultMessage.SENDSUCCESS,
								"true").sendToTarget();
						System.out.println("--->>>获取验证码成功");
						// 获取验证码成功
					} else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
						// 返回支持发送验证码的国家列表
					}
				} else {
					Toast.makeText(RegisterActivity.this, "验证码发送失败！", 1).show();
				}
			}
		};
		SMSSDK.registerEventHandler(eh); // 注册短信回调
		ready = true;
	}

	protected void onDestroy() {
		if (ready) {
			// 销毁回调监听接口
			SMSSDK.unregisterAllEventHandler();
		}
		super.onDestroy();
	}

	void initview() {
		mDialog = DialogUtil.getLoadDialog(this, "请稍后！");
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
	}
}
