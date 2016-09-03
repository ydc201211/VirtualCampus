package com.vc.ui;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import common.Base64.InputStream;
import common.Constants;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;


public class Activity_BigPicture extends Activity {

	private String picURL;
	private ImageView imageView;
	private ImageView leftImg;
	private String picType;
	
	DisplayImageOptions options; 
	protected ImageLoader imageLoader;
	@Override  
    protected void onCreate(Bundle savedInstanceState) {  
        // TODO Auto-generated method stub  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.activity_activity__big_picture);
        imageLoader = ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration.createDefault(this));
        initView();  
        
        picURL  = getIntent().getStringExtra("picURL");
        picType = getIntent().getStringExtra("picType");
        
        initPic();
        
        
    }  
	
	
	 
    private void initView() {  
    	imageView = (ImageView) findViewById(R.id.common_imageview_imageControl1);  
    	leftImg = (ImageView) findViewById(R.id.icon_left);
    	
    	
    	leftImg.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
    }  
	
    private void initPic(){
    	
    	options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.icon_onloading)
		.showImageForEmptyUri(R.drawable.ic_launcher) 
		.showImageOnFail(R.drawable.icon_loadfail) 
		.cacheInMemory(true) 
		.cacheOnDisk(true) 
		.displayer(new RoundedBitmapDisplayer(2)) 
		.build();
    		
    	if(picType.equals("action")){
    		Log.i("kkk",picType);
	    	imageLoader.displayImage(Constants.SERVERADDRESS
				+ "/file/image/get?imageUrl="
				+ picURL,imageView,options);
    	}else if(picType.equals("avatar")){
    		imageLoader.displayImage(Constants.SERVERADDRESS
    				+ "/file/avatar/get?imageUrl="
    				+ picURL,imageView,options);
    	}
    	
   
    }
     
}
