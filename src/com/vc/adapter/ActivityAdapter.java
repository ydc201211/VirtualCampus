package com.vc.adapter;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;


import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.vc.ui.Activity_BigPicture;
import com.vc.ui.R;
import common.Constants;
import common.LocalStorage;
import com.vc.api.OkHttpConnection;
import com.vc.db.ActionDB;

import dto.ActionSimple;
import dto.AttendanceStatus;
import dto.LikeStatus;

public class ActivityAdapter extends ArrayAdapter {

	private int resourceId;
	private List<ActionSimple> activityList;
	private LayoutInflater inflater;
	private final int a = 0;

	private int position;
	private Context context;

	DisplayImageOptions options; 
	protected ImageLoader imageLoader;

	public ActivityAdapter(Context context, int textViewResourceId,
			List<ActionSimple> ac_list) {
		super(context, textViewResourceId);
		inflater = LayoutInflater.from(context);
		this.context = context;
		this.activityList = ac_list;
		resourceId = textViewResourceId; // item璧勬簮id
		imageLoader = ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration.createDefault(context));

	}

	@Override
	public int getCount() {
		return activityList.size();
	}

	@Override
	public Object getItem(int position) {
		return activityList.get(position);
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
					.findViewById(R.id.ac_pic_Iv);
			viewHolder.themeText = (TextView) convertView
					.findViewById(R.id.theme_tv);
			viewHolder.timeText = (TextView) convertView
					.findViewById(R.id.time_Tv);
			viewHolder.hostText = (TextView) convertView
					.findViewById(R.id.host_Tv);
			viewHolder.activitySiteText = (TextView) convertView
					.findViewById(R.id.site_Tv);
			viewHolder.attendanceText = (TextView) convertView
					.findViewById(R.id.rb_join);
			viewHolder.likeText = (TextView) convertView
					.findViewById(R.id.rb_like);
			
			viewHolder.a_ll = (RelativeLayout) convertView
					.findViewById(R.id.a_ll);
			viewHolder.l_ll = (RelativeLayout) convertView
					.findViewById(R.id.l_ll);
			
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		this.position = position;
		loadPicture(viewHolder);

		viewHolder.themeText.setText(activityList.get(position)
				.getAction_theme());
		viewHolder.hostText
				.setText(activityList.get(position).getAction_host());
		viewHolder.timeText
				.setText(activityList.get(position).getAction_time());
		viewHolder.activitySiteText.setText(activityList.get(position)
				.getAction_site());
		
		
		viewHolder.attendanceText.setText(activityList.get(position)
				.getAttendance_nums() == 0 ? "参与" : activityList.get(position)
				.getAttendance_nums() + "");
		viewHolder.likeText
				.setText(activityList.get(position).getLike_nums() == 0 ? "赞"
						: activityList.get(position).getLike_nums() + "");
		
		final TextView a_tv = viewHolder.attendanceText;
		final TextView l_tv = viewHolder.likeText;
		final ImageView big_imageview = viewHolder.imageView;
		final int position_1 = position;
		
		
		viewHolder.a_ll.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				
				
				if(LocalStorage.getString(context,"userId") == ""){
					Toast.makeText(context, "请先登录",Toast.LENGTH_SHORT).show();
					
				}else{
					
					ActionDB actionDB =ActionDB.getInstance(context);
					List<AttendanceStatus> list = actionDB.getAttendance();
					int isAttendance = 0;
					int s = 0;
					
					if(a_tv.getText().toString() == "参与"){
						s = 0;
					}else{
						s = Integer.parseInt(a_tv.getText().toString());
					}
					
					
					for(int i = 0; i < list.size();i++){
						
						if(list.get(i).getIsAttendance() == 3){
							isAttendance = 1;
							break;
						}
					}
					
					if(isAttendance != 1){
						
						a_tv.setText(String.valueOf(s + 1));
						activityList.get(position_1).setAttendance_nums(s + 1);
						
						//存储到数据库
						AttendanceStatus as = new AttendanceStatus();
						
						as.setActionId(activityList.get(position_1).getAction_id());
						
						as.setIsAttendance(1);
						int userId = Integer.parseInt(LocalStorage.getString(context,"userId"));
						as.setUserId(userId);
						actionDB.saveAttendanceStatus(as);
						postMessageToServer("addAttendance",
									String.valueOf(activityList.get(position_1).getAction_id()),
									LocalStorage.getString(context,"userId"));
						
					}else{
						if(s == 1){
							a_tv.setText("参与");
							activityList.get(position_1).setAttendance_nums(0);
							actionDB.deleteAttendance(String.valueOf(activityList.get(position_1).getAction_id()));
							postMessageToServer("deleteAttendance",
										String.valueOf(activityList.get(position_1).getAction_id()),
										LocalStorage.getString(context,"userId"));
							
							
							
						}else{
							a_tv.setText(String.valueOf(s-1));
							activityList.get(position_1).setAttendance_nums(s-1);
							actionDB.deleteAttendance(String.valueOf(activityList.get(position_1).getAction_id()));
							postMessageToServer("deleteAttendance",
										String.valueOf(activityList.get(position_1).getAction_id()),
										LocalStorage.getString(context,"userId"));
							
							
						}
					}
				}	
			}
		});
		
		
		viewHolder.l_ll.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(LocalStorage.getString(context,"userId") == ""){
					Toast.makeText(context,"请先登录",Toast.LENGTH_SHORT).show();
				}else{
					ActionDB actionDB =ActionDB.getInstance(context);
					List<LikeStatus> list = actionDB.getLike();
					int isLike = 0;
					int s = 0;
					if(l_tv.getText().toString() == "赞"){
						s = 0;
					}else{
						s = Integer.parseInt(l_tv.getText().toString());
					}
					
					
					for(int i = 0; i < list.size();i++){
						if(list.get(i).getIsLike() == 3){
							isLike = 1;
							break;
						}
					}
					
					if(isLike != 1){
						l_tv.setText(String.valueOf(s + 1));
						activityList.get(position_1).setLike_nums(s + 1);
						//存储到数据库
						LikeStatus ls = new LikeStatus();
						ls.setActionId(activityList.get(position_1).getAction_id());
						ls.setIsLike(1);
						ls.setUserId(Integer.parseInt(LocalStorage.getString(context,"userId")));
						actionDB.saveLikeStatus(ls);
						postMessageToServer("addLike",
								String.valueOf(activityList.get(position_1).getAction_id()),
								LocalStorage.getString(context,"userId"));
					}else{
						if(s == 1){
							l_tv.setText("赞");
							activityList.get(position_1).setLike_nums(0);
							actionDB.deleteLike(String.valueOf(activityList.get(position_1).getAction_id()));
							postMessageToServer("deleteLike",
									String.valueOf(activityList.get(position_1).getAction_id()),
									LocalStorage.getString(context,"userId"));
						}else{
							l_tv.setText(String.valueOf(s-1));
							activityList.get(position_1).setLike_nums(s-1);
							actionDB.deleteLike(String.valueOf(activityList.get(position_1).getAction_id()));
							postMessageToServer("deleteLike",
									String.valueOf(activityList.get(position_1).getAction_id()),
									LocalStorage.getString(context,"userId"));
						}
						
					}
				}
			}
		});
		
		viewHolder.imageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(context,Activity_BigPicture.class);
				intent.putExtra("picURL",activityList.get(position_1).getAction_picPath());
				intent.putExtra("picType","action");
				context.startActivity(intent);
			}
		});

		return convertView;
	}

	private void loadPicture(ViewHolder viewHolder) {

		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.icon_onloading)
				.showImageForEmptyUri(R.drawable.ic_launcher) 
				.showImageOnFail(R.drawable.icon_loadfail) 
				.cacheInMemory(true) 
				.cacheOnDisk(true) 
				.displayer(new RoundedBitmapDisplayer(2)) 
				.build();

		imageLoader.displayImage(Constants.SERVERADDRESS
				+ "/file/image/get?imageUrl="
				+ activityList.get(position).getAction_picPath(),
				viewHolder.imageView, options);

	}
	
	
	private void postMessageToServer(final String type,final String activityId,final String userId){
		new Thread(new Runnable() {
            @Override
            public void run() {
            	List<NameValuePair> params;
            	JSONObject json = new JSONObject();
            	if(type == "addAttendance"){
            		params = new ArrayList<NameValuePair>();
            		String dateTime = MessageFormat.format("{0,date,yyyy-MM-dd-HH-mm:ss}" ,
                            new Object[]{
                                new java.sql.Date(System.currentTimeMillis())
                            });
            		params.add(new BasicNameValuePair("attendance_userId",userId));
            		params.add(new BasicNameValuePair("attendance_activityId",activityId));
            		params.add(new BasicNameValuePair("attendance_time",dateTime));
            		json = OkHttpConnection.execute(context,
        					"/attendance/addAttendance", params);
            	}else if(type == "deleteAttendance"){
            		params = new ArrayList<NameValuePair>();
            		
            		params.add(new BasicNameValuePair("attendance_userId",userId));
            		params.add(new BasicNameValuePair("attendance_activityId",activityId));
            		json = OkHttpConnection.execute(context,
        					"/attendance/deleteAttendance", params);
            		
            	}else if(type == "addLike"){
            		params = new ArrayList<NameValuePair>();
            		String dateTime = MessageFormat.format("{0,date,yyyy-MM-dd-HH-mm:ss}" ,
                            new Object[]{
                                new java.sql.Date(System.currentTimeMillis())
                            });
            		params.add(new BasicNameValuePair("like_userId",userId));
            		params.add(new BasicNameValuePair("like_activityId",activityId));
            		params.add(new BasicNameValuePair("like_time",dateTime));
            		json = OkHttpConnection.execute(context,
        					"/like/addLike", params);
            		
            	}else{
            		params = new ArrayList<NameValuePair>();
            		
            		params.add(new BasicNameValuePair("like_userId",userId));
            		params.add(new BasicNameValuePair("like_activityId",activityId));
            		
            		json = OkHttpConnection.execute(context,
        					"/like/deleteLike", params);
            	}
            	
            }
        }).start();
	}

	public static class ViewHolder {
		ImageView imageView;
		TextView themeText;
		TextView hostText;
		TextView timeText;
		TextView activitySiteText;
		TextView attendanceText;
		TextView likeText;
		RelativeLayout a_ll;
		RelativeLayout l_ll;

	}

}
