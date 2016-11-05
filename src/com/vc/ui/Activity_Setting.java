package com.vc.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.vc.app.App;
import com.vc.entity.User;

public class Activity_Setting extends Activity implements OnClickListener {
	private Button btn_quit;
	private ImageView iv_set_left;
	App application;
	private LinearLayout changePsw;
	User user = new User();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settingactivity);
		initview();
		application = (App) getApplication();
	}

	private void initview() {
		// TODO Auto-generated method stub
		btn_quit = (Button) findViewById(R.id.btn_quit);
		iv_set_left = (ImageView) findViewById(R.id.iv_set_left);
		changePsw = (LinearLayout) findViewById(R.id.changePsw);
		changePsw.setOnClickListener(this);
		iv_set_left.setOnClickListener(this);
		btn_quit.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.btn_quit:
			application.setUser(user);
			application.setUserId(0);
			setResult(1);
			this.finish();
			break;
		case R.id.iv_set_left:
			setResult(2);
			this.finish();
			break;
		case R.id.changePsw:
			intent = new Intent(this, Activity_ChangePsw.class);
			startActivity(intent);
			this.finish();
			break;
		}
	}
}
