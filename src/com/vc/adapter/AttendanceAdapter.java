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

import dto.AttendanceDetail;

public class AttendanceAdapter extends ArrayAdapter {

	private int resourceId;
	private LinkedList<AttendanceDetail> attendanceList;
	private LayoutInflater inflater;
	
	
	private Context context;


	DisplayImageOptions options;
	protected ImageLoader imageLoader;

	

	public AttendanceAdapter(Context context, int textViewResourceId,
			LinkedList<AttendanceDetail> attendance_list) {
		super(context, textViewResourceId);
		// TODO Auto-generated constructor stub
		inflater = LayoutInflater.from(context);
		this.context = context;
		this.attendanceList = attendance_list;
		resourceId = textViewResourceId; 
		imageLoader = ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration.createDefault(context));
	}

	@Override
	public int getCount() {
		return attendanceList.size();
	}

	@Override
	public Object getItem(int position) {
		return attendanceList.get(position);
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
					.findViewById(R.id.iv_a_avatar);
			viewHolder.nickNameText = (TextView) convertView
					.findViewById(R.id.tv_a_subhead);
			viewHolder.attendance_time = (TextView) convertView
					.findViewById(R.id.tv_a_time);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		
		viewHolder.nickNameText.setText(attendanceList.get(position)
				.getAttendance_user_nickName());
		

		viewHolder.attendance_time.setText(attendanceList.get(position)
				.getAttendance_time());
		
		loadPicture(viewHolder,attendanceList.get(position)
				.getAttendacne_user_picPath());

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
		TextView attendance_time;

	}

	

}
