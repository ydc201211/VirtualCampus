package com.vc.ui;

import util.TitleBuilder;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class Activity_NewStrategy extends Activity implements OnClickListener{

	private TextView brief_intro_tv;
	private TextView Registration_process_tv;
	private TextView to_school_route_tv;
	private TextView huaxi_school_tv;
	private TextView liangjiang_school_tv;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_strategy);
		
		initView();
	}
	
	private void initView(){
		initTitle();
		brief_intro_tv = (TextView) this.findViewById(R.id.brief_intro_tv);
		Registration_process_tv = (TextView) this.findViewById(R.id.Registration_process_tv);
		to_school_route_tv = (TextView) this.findViewById(R.id.to_school_route_tv);
		huaxi_school_tv = (TextView) this.findViewById(R.id.huaxi_school_tv);
		liangjiang_school_tv = (TextView) this.findViewById(R.id.liangjiang_school_tv);
		
		brief_intro_tv.setOnClickListener(this);
		Registration_process_tv.setOnClickListener(this);
		to_school_route_tv.setOnClickListener(this);
		huaxi_school_tv.setOnClickListener(this);
		liangjiang_school_tv.setOnClickListener(this);
		
			
	}
	
	private void initTitle() {
		new TitleBuilder(this).setTitleText("新生攻略")
				.setLeftImage(R.drawable.left).setLeftOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent;

		switch (v.getId()) {
		case R.id.titlebar_iv_left:
			intent = new Intent();
			intent.putExtra("back", "from Activity_NewStrategy");
			setResult(RESULT_CANCELED, intent);
			finish();
			break;
		case R.id.brief_intro_tv:
			intent = new Intent(Activity_NewStrategy.this,Activity_CollegeIntro.class);
			startActivityForResult(intent,1);
			
			break;
		case R.id.Registration_process_tv:
			intent = new Intent(Activity_NewStrategy.this,Activity_Reg_Process.class);
			startActivityForResult(intent,2);
			break;
		case R.id.to_school_route_tv:
			intent = new Intent(Activity_NewStrategy.this,Activity_WebView.class);
			intent.putExtra("title", "学校路线");
			intent.putExtra("url", "http://yxxt.i.cqut.edu.cn/web/zzfw_xxfb.do?cdbh=3839D0759B097B82E053069BCACA346F");
			startActivityForResult(intent,3);
			break;
		case R.id.huaxi_school_tv:
			intent = new Intent(Activity_NewStrategy.this,Activity_WebView.class);
			intent.putExtra("title", "花溪校区一览");
			intent.putExtra("url", "http://yxxt.i.cqut.edu.cn/web/zzfw_cdglMain.do?cdbh=li_A4B0FD4A84999AE5E040007F01001ABB&flag=1");
			startActivityForResult(intent,4);
			break;
		case R.id.liangjiang_school_tv:
			intent = new Intent(Activity_NewStrategy.this,Activity_WebView.class);
			intent.putExtra("title", "两江校区一览");
			intent.putExtra("url", "http://yxxt.i.cqut.edu.cn/web/zzfw_cdglMain.do?cdbh=li_A4B0FD4A84999AE5E040007F01001ABB&flag=1");
			startActivityForResult(intent,5);
			break;
		}
		
	
	}

	
}
