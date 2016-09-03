package com.vc.ui;

import util.TitleBuilder;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

public class Activity_Reg_Process extends Activity implements OnClickListener{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reg_process);
		
		initView();
		
	}
	private void initView(){
		initTitle();
	}
	
	private void initTitle() {
		new TitleBuilder(this).setTitleText("报名流程")
				.setLeftImage(R.drawable.left).setLeftOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent;

		switch (v.getId()) {
		case R.id.titlebar_iv_left:
			intent = new Intent();
			intent.putExtra("back", "from Activity_Reg_Process");
			setResult(RESULT_CANCELED, intent);
			finish();
			break;
		}
	}
}
