package com.vc.adapter;

import java.util.List;

import com.vc.entity.User;
import com.vc.ui.R;

import android.util.Log;
import android.view.View.OnClickListener;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

@SuppressLint("ResourceAsColor")
public class UserAdapter extends ArrayAdapter<Object>{

	
	private int resourceId;
	private List<User> userList;
	private LayoutInflater inflater;
	

	private int position;
	private Context context;
	
	public UserAdapter(Context context, int resource, List<User> list) {
		super(context, resource);
		// TODO Auto-generated constructor stub
		inflater = LayoutInflater.from(context);
		this.context = context;
		this.userList = list;
		resourceId = resource;
		
	}
	
	@Override
	public int getCount() {
		return userList.size();
	}

	@Override
	public Object getItem(int position){
		return userList.get(position);
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
			viewHolder.textView = (TextView) convertView
					.findViewById(R.id.friend_tv);
			viewHolder.btn = (Button) convertView
					.findViewById(R.id.friend_btn);
			
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		this.position = position;
		
		viewHolder.textView.setText(userList.get(position).getNickName());
		final Button btn = viewHolder.btn;
		viewHolder.btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				btn.setClickable(false);
				btn.setBackgroundColor(R.color.gray);
				btn.setText("ÒÑ·¢ËÍ");
			}
		});
		
		return convertView;
	}
	
	public static class ViewHolder {
		TextView textView;
		Button btn;
	}
}

	
