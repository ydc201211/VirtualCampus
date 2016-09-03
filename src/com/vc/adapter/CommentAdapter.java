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

import dto.CommentDetail;

public class CommentAdapter extends ArrayAdapter {

	private int resourceId;
	private LinkedList<CommentDetail> commentList;
	private LayoutInflater inflater;
	

	private Context context;

	// 锟斤拷锟斤拷图片锟斤拷锟斤拷锟斤拷锟斤拷图锟斤拷锟�
	DisplayImageOptions options; // 图片锟斤拷示锟斤拷锟斤拷
	protected ImageLoader imageLoader;// 图片锟斤拷锟斤拷

	

	public CommentAdapter(Context context, int textViewResourceId,
			LinkedList<CommentDetail> comment_list){
		super(context, textViewResourceId);
		inflater = LayoutInflater.from(context);
		this.context = context;
		this.commentList = comment_list;
		resourceId = textViewResourceId; // item锟斤拷源id
		imageLoader = ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration.createDefault(context));

	}

	@Override
	public int getCount() {
		return commentList.size();
	}

	@Override
	public Object getItem(int position) {
		return commentList.get(position);
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
					.findViewById(R.id.iv_comment_avatar);
			
			
			viewHolder.nickNameText = (TextView) convertView
					.findViewById(R.id.tv_comment_subhead);
			viewHolder.comment_time = (TextView) convertView
					.findViewById(R.id.tv_comment_time);
			viewHolder.comment_content = (TextView) convertView
					.findViewById(R.id.tv_comment_caption);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		viewHolder.nickNameText.setText(commentList.get(position)
				.getComment_user_nickName());
		viewHolder.comment_content.setText(commentList.get(position)
				.getComment_content());
		viewHolder.comment_time.setText(commentList.get(position)
				.getComment_time());
		

		loadPicture(viewHolder,commentList.get(position).getComment_user_picPath());
		

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
		TextView comment_content;
		TextView comment_time;

	}

	

}
