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

public class Activity_ChangePsw_psw extends Activity {
	private EditText edit_resure_ps1, edit_resure_ps2;
	private Button btn_resure_next;
	private ImageButton btn_resure_left;
	Dialog mDialog;
	private String phoneNo;
	AppClientDao mAppClientDao = new AppClientDao(Activity_ChangePsw_psw.this);
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (mDialog.isShowing())
				mDialog.dismiss();
			switch (msg.what) {
			case ResultMessage.CHANGE_PWD_SUCCESS: {
				Object objs = msg.obj;
				Toast.makeText(Activity_ChangePsw_psw.this,"修改成功", 0).show();
				Intent intent = new Intent(Activity_ChangePsw_psw.this,
						Main.class);
				startActivity(intent);
				Activity_ChangePsw_psw.this.finish();
				break;
			}
			case ResultMessage.CHANGE_PWD_FAILED:
				Object objs = msg.obj;
				Toast.makeText(Activity_ChangePsw_psw.this, objs.toString(), 0)
						.show();
				break;
			case ResultMessage.FAILED:
				Toast.makeText(Activity_ChangePsw_psw.this, "操作失败", 0).show();
				break;
			case ResultMessage.TIMEOUT:
				Toast.makeText(Activity_ChangePsw_psw.this,"连接超时",
						Toast.LENGTH_SHORT).show();
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.resurepw_activity);
		initview();
		Intent intent = this.getIntent();
		phoneNo = intent.getStringExtra("phoneNo");
	}

	private void initview() {
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
		edit_resure_ps1 = (EditText) findViewById(R.id.edit_resure_ps1);
		edit_resure_ps2 = (EditText) findViewById(R.id.edit_resure_ps2);
		btn_resure_next = (Button) findViewById(R.id.btn_resure_next);
		btn_resure_next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String psw1 = edit_resure_ps1.getText().toString().trim();
				String psw2 = edit_resure_ps2.getText().toString().trim();
				if(psw1 == null || psw1 == "" || psw2 == null ||psw2 == ""){
					Toast.makeText(Activity_ChangePsw_psw.this,"请先输入",Toast.LENGTH_SHORT).show();
				}else if (psw1.equals(psw2)) {
					mAppClientDao.Get_ChangePsw_Code(mHandler, phoneNo, psw1);
					mDialog.show();
				}
			}
		});
		btn_resure_left = (ImageButton) findViewById(R.id.btn_resure_left);
		btn_resure_left.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Activity_ChangePsw_psw.this.finish();
			}
		});
	}
}
