package com.vc.ui;

import util.TitleBuilder;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

public class Activity_CollegeIntro extends Activity implements OnClickListener{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity__college_intro);
		
		initTitle();
	}
	
	private void initTitle() {
		new TitleBuilder(this).setTitleText("÷ÿ¿Ìπ§ºÚΩÈ")
				.setLeftImage(R.drawable.left).setLeftOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent;

		switch (v.getId()) {
		case R.id.titlebar_iv_left:
			intent = new Intent();
			intent.putExtra("back", "from Activity_CollegeIntro");
			setResult(RESULT_CANCELED, intent);
			finish();
			break;
		}
	}
}
