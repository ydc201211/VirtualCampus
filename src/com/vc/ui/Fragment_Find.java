package com.vc.ui;

import util.TitleBuilder;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import common.Constants;

//修改
public class Fragment_Find extends Fragment implements OnClickListener {
	private String url = "/file/image/get";
	DisplayImageOptions options; // 图片加载设置
	protected ImageLoader imageLoader;
	private ImageView activityIv;  
	private TextView activityTv;
	private View ac_view; // 娲诲姩View
	private Bitmap bitmap;
	
	private LinearLayout jjjjj;

	// 
	private ProgressDialog pDialog;

	public static final int COMPLETE = 1;
	public static final int DATA = 2;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ac_view = inflater.inflate(R.layout.fragment_find, null);
		initView();
		
		// pDialog.show();
		loadPicture();
		return ac_view;
	}

	private void initView() {
		
		initTitle();
		
		initContent();
	}

	// 鍒濆鍖栨爣棰�
	private void initTitle() {
		new TitleBuilder(ac_view).setTitleText("发现");
	}

	private void initContent() {
		activityIv = (ImageView) ac_view.findViewById(R.id.activity_Iv);
		activityTv = (TextView) ac_view.findViewById(R.id.activity_Tv);
		jjjjj = (LinearLayout) ac_view.findViewById(R.id.jjjj);
		activityIv.setOnClickListener(this);
		jjjjj.setOnClickListener(this);
	}

	private void loadPicture() {
		imageLoader = ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));
		options = new DisplayImageOptions.Builder()
				
				.showImageForEmptyUri(R.drawable.ic_launcher) // 璁剧疆鍥剧墖Uri涓虹┖鎴栨槸閿欒鐨勬椂鍊欐樉绀虹殑鍥剧墖
				.showImageOnFail(R.drawable.icon_loadfail) // 璁剧疆鍥剧墖鍔犺浇鎴栬В鐮佽繃绋嬩腑鍙戠敓閿欒鏄剧ず鐨勫浘鐗�
				.cacheInMemory(true) // 璁剧疆涓嬭浇鐨勫浘鐗囨槸鍚︾紦瀛樺湪鍐呭瓨涓�
				.cacheOnDisk(true) // 璁剧疆涓嬭浇鐨勫浘鐗囨槸鍚︾紦瀛樺湪SD鍗′腑
				.displayer(new RoundedBitmapDisplayer(2)) // 璁剧疆鎴愬渾瑙掑浘鐗�
				.build(); // 鍒涘缓閰嶇疆杩囧緱DisplayImageOption瀵硅薄

		imageLoader.displayImage(Constants.SERVERADDRESS + url
				+ "?imageUrl=a.jpg", activityIv, options);

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		

	}

	@Override
	public void onResume() {
		super.onResume();
		/* initData(); */
	}

	

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent;
		Bundle bundle;
		switch (v.getId()) {
		case R.id.activity_Iv:
			
			intent = new Intent(getActivity(), Activity_ActivityList.class);
			startActivity(intent);
			break;
		case R.id.jjjj:
			
			intent = new Intent(getActivity(), Activity_PointSearch.class);
			startActivityForResult(intent, 1);
		default:
			break;
		}
		
	}
}
