package com.vc.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;

public class Activity_Welcome extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		
	        //延迟两秒后执行run方法中的页面跳转  
        new Handler().postDelayed(new Runnable() {  
  
            @Override  
            public void run() {  
                Intent intent = new Intent(Activity_Welcome.this,Main.class);  
                startActivity(intent);  
                Activity_Welcome.this.finish();  
            }  
        }, 3000);  
	      
	}
}
