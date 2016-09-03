package com.vc.adapter;

import java.util.List;

import com.vc.entity.Mark;
import com.vc.ui.R;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class PointAdapter extends ArrayAdapter{

	private int resourceId;
	private List<Mark> markList;
	private LayoutInflater inflater;
	private final int a = 0;

	private int position;
	private Context context;

	
	
	@SuppressWarnings("unchecked")
	public PointAdapter(Context context, int textViewResourceId,
			List<Mark> mark_list) {
		super(context, textViewResourceId,mark_list);
		// TODO Auto-generated constructor stub
		
		inflater = LayoutInflater.from(context);
		this.context = context;
		this.markList = mark_list;
		resourceId = textViewResourceId; // item布局文件id
		

	}
	
	@Override
	public int getCount() {
		return markList.size();
	}

	@Override
	public Object getItem(int position){
		return markList.get(position);
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
					.findViewById(R.id.point_search_item_tv);
			
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		this.position = position;
		Log.i("ydc888", markList.get(position).getMarkName());
		viewHolder.textView.setText(markList.get(position).getMarkName());
		
		
		
		return convertView;
	}
	public static class ViewHolder {
		TextView textView;
	}
}
