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

public class ResurePw_Activity extends Activity implements OnClickListener {
	private ImageButton btnleft;
	private Button btn_next;
	private EditText edit_resure_ps1, edit_resure_ps2;
	Dialog mDialog;
	String phoneNo = "";
	AppClientDao mAppClientDao = new AppClientDao(ResurePw_Activity.this);

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (mDialog.isShowing())
				mDialog.dismiss();
			switch (msg.what) {
			case ResultMessage.CHANGE_PWD_FAILED: {
				Toast.makeText(ResurePw_Activity.this, "找回密码失败！", 0).show();
				Intent intent = new Intent(ResurePw_Activity.this,
						LoginActivity.class);
				intent.putExtra("loginId", 3);
				startActivity(intent);
				break;
			}
			case ResultMessage.CHANGE_PWD_SUCCESS: {
				Toast.makeText(ResurePw_Activity.this, "找回密码成功，请返回登录！", 0)
						.show();
				Intent intent = new Intent(ResurePw_Activity.this,
						LoginActivity.class);
				intent.putExtra("loginId", 3);
				startActivity(intent);
				break;
			}
			case ResultMessage.FAILED:
				Toast.makeText(ResurePw_Activity.this, "操作失败", 0).show();
				break;
			case ResultMessage.TIMEOUT:
				Toast.makeText(ResurePw_Activity.this, "连接超时！",
						Toast.LENGTH_SHORT).show();
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO �Զ���ɵķ������
		super.onCreate(savedInstanceState);
		setContentView(R.layout.resurepw_activity);
		Intent intent = this.getIntent();
		// intent.getExtras().getString("phoneNo");
		phoneNo = intent.getStringExtra("phoneNo");
		btnleft = (ImageButton) findViewById(R.id.btn_resure_left);
		btn_next = (Button) findViewById(R.id.btn_resure_next);
		edit_resure_ps1 = (EditText) findViewById(R.id.edit_resure_ps1);
		edit_resure_ps2 = (EditText) findViewById(R.id.edit_resure_ps2);
		btnleft.setOnClickListener(this);
		btn_next.setOnClickListener(this);
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
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_resure_left: {
			finish();
			break;
		}
		case R.id.btn_resure_next: {
			String ps1 = edit_resure_ps1.getText().toString().trim();
			String ps2 = edit_resure_ps2.getText().toString().trim();
			if (ps1.equals(ps2)) {
				if (phoneNo != null && !phoneNo.equals("")) {
					mAppClientDao.Get_ChangePsw_Code(mHandler, phoneNo, ps1);
					mDialog.show();
				}
			} else
				Toast.makeText(this, "两次密码不一致，请重新输入！", 0).show();
			break;
		}
		}
	}

}
