package com.vc.ui;

import util.TitleBuilder;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;

public class Activity_WebView extends Activity implements OnClickListener{
	
	
	private WebView webView; 

	Intent intent; 
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web_view);
		
		intent = getIntent();
		initView();
		webView.loadUrl(intent.getStringExtra("url").toString()); 
        webView.getSettings().setJavaScriptEnabled(true); 
	}
	
	private void initView(){
		initTitle();
		webView = (WebView) this.findViewById(R.id.webview); 
	}
	
	private void initTitle() {
		new TitleBuilder(this).setTitleText(intent.getStringExtra("title"))
				.setLeftImage(R.drawable.left).setLeftOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent;

		switch (v.getId()) {
		case R.id.titlebar_iv_left:
			intent = new Intent();
			intent.putExtra("back", "from Activity_WebView");
			setResult(RESULT_CANCELED, intent);
			finish();
			break;
		
		}
		
	
	}

}
