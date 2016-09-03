package com.vc.adapter;

import java.util.LinkedList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.vc.ui.R;
import common.Constants;
import dto.LikeDetail;


public class LikeAdapter extends ArrayAdapter {

	private int resourceId;
	private LinkedList<LikeDetail> likeList;
	private LayoutInflater inflater;
	

	
	private Context context;

	
	DisplayImageOptions options; 
	protected ImageLoader imageLoader;

	public LikeAdapter(Context context, int textViewResourceId,
			LinkedList<LikeDetail> like_list) {
		super(context, textViewResourceId);
		inflater = LayoutInflater.from(context);
		this.context = context;
		this.likeList = like_list;
		resourceId = textViewResourceId; // item锟斤拷源id
		imageLoader = ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration.createDefault(context));

	}

	@Override
	public int getCount() {
		return likeList.size();
	}

	@Override
	public Object getItem(int position) {
		return likeList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = inflater.inflate(resourceId, null);
			viewHolder = new ViewHolder();
			viewHolder.imageView = (ImageView) convertView
					.findViewById(R.id.iv_l_avatar);
			viewHolder.nickNameText = (TextView) convertView
					.findViewById(R.id.tv_l_subhead);
			viewHolder.like_time = (TextView) convertView
					.findViewById(R.id.tv_l_time);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		viewHolder.like_time.setText(likeList.get(position).getLike_time());
		viewHolder.nickNameText.setText(likeList.get(position).getLike_user_nickName());
		loadPicture(viewHolder,likeList.get(position).getLike_user_picPath());

		return convertView;
	}

	
	

	private void loadPicture(ViewHolder viewHolder,String picPath) {

		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.icon_onloading)
				.showImageForEmptyUri(R.drawable.ic_launcher) 
				.showImageOnFail(R.drawable.icon_loadfail) 
				.cacheInMemory(true) 
				.cacheOnDisk(true) 
				.build(); 

		imageLoader.displayImage(Constants.SERVERADDRESS
				+ "/file/avatar/get?imageUrl=" + picPath, viewHolder.imageView,
				options);

	}

	public static class ViewHolder {
		ImageView imageView;
		TextView nickNameText;
		TextView like_time;

	}

	
}