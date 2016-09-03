package com.vc.dialog;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.vc.api.AppClientDao;
import com.vc.api.Pic_Down_Thread;
import com.vc.api.ResultMessage;
import com.vc.entity.MarkEntity;
import com.vc.ui.R;

public class KeyBoardPopupWindow extends PopupWindow implements OnClickListener {

	public StringBuffer address = new StringBuffer(); // ��ַ����
	private String hHint; // ��ʾ
	private Context context; // Ҫ���õ�Acti
	private Keyboard1PopListener listener; // �ӿڼ�����
	private int screen_width; // ��Ļ��
	private int screen_height;// ��Ļ��
	View lLayout; //
	LinearLayout btnSubmit; // ȷ����
	TextView text_key, roadName, text_address, text_distance;
	private ImageView image_mark;
	private Keyboard1isPupListener isPupListener; // ��ֹ������
	private int load_Index = 0;
	private MarkEntity info;
	String distance = "";
	int flag = 0;
	private AppClientDao mAppClientDao = new AppClientDao(context);
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case ResultMessage.GET_MARK_PICNAME_SUCCESS: {
				Object objs = (Object) msg.obj;
				String PicName = objs.toString();
				new Pic_Down_Thread(image_mark, mHandler, PicName).start();
			}
				break;
			case ResultMessage.GET_MARK_PICNAME_FAILED:
				Toast.makeText(context, msg.obj.toString(), Toast.LENGTH_SHORT)
						.show();
				break;
			case ResultMessage.FAILED:
				Toast.makeText(context, msg.obj.toString(), Toast.LENGTH_SHORT)
						.show();
				break;
			case ResultMessage.TIMEOUT:
				Toast.makeText(context, msg.obj.toString(), Toast.LENGTH_SHORT)
						.show();
				break;
			}

		}
	};

	public KeyBoardPopupWindow(Context context, MarkEntity info,
			String distance, int flag) {
		this.context = context;
		this.info = info;
		this.flag = flag;
		this.distance = distance;
		DisplayMetrics dm = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay()
				.getMetrics(dm); // ��ȡ�ֻ���Ļ�Ĵ�С
		screen_width = dm.widthPixels;
		screen_height = dm.heightPixels;
		init();
		showKeyboard();
	}

	public void showKeyboard() {
		this.setContentView(lLayout);
		// ����SelectPicPopupWindow��������Ŀ�
		if (address.length() > 0) {
		}
		// this.setWidth(LayoutParams.FILL_PARENT);
		this.setWidth(LayoutParams.WRAP_CONTENT);
		// ����SelectPicPopupWindow��������ĸ�
		this.setHeight(LayoutParams.WRAP_CONTENT);
		// ����SelectPicPopupWindow��������ɵ��
		this.setFocusable(true);
		// ����SelectPicPopupWindow�������嶯��Ч��
		// this.setAnimationStyle(R.style.mystyle);
		lLayout.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				int height = lLayout.findViewById(R.id.keyboard1_layout)
						.getTop();
				int y = (int) event.getY();
				if (event.getAction() == MotionEvent.ACTION_UP) {
					if (y < height) {
						dismiss();
						isPupListener.onIsPup(true);
					}
				}
				return true;
			}
		});
	}

	public void init() {
		lLayout = ((Activity) context).getLayoutInflater().inflate(
				R.layout.keyboard, null);
		image_mark = (ImageView) lLayout.findViewById(R.id.pic_mark);
		btnSubmit = (LinearLayout) lLayout.findViewById(R.id.submit_lay);
		text_key = (TextView) lLayout.findViewById(R.id.name);
		text_address = (TextView) lLayout.findViewById(R.id.m_detail);
		text_distance = (TextView) lLayout.findViewById(R.id.distance);
		btnSubmit.setOnClickListener(this);
		if (flag == 1) {
			mAppClientDao.GetMark_PicName(mHandler, info.getMarkId());
			image_mark.setVisibility(View.VISIBLE);
		}
		text_key.setText(info.getMarkName());
		text_address.setText(info.getMark_detail());
		text_distance.setText(distance);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.submit_lay:
			if (listener != null && isPupListener != null) {
				System.out.println("===>>>导航");
				isPupListener.onIsPup(true);
				listener.onTimeSelect(info.getMarkName());
			}
			this.dismiss();
			break;
		}
	}

	public interface Keyboard1isPupListener {
		public void onIsPup(Boolean isPup);
	}

	public void setOnKeyboard1IsPupListener(
			Keyboard1isPupListener keyboard1isPupListener) {
		this.isPupListener = keyboard1isPupListener;
	}

	public void setOnKeyboard1PopListener(
			Keyboard1PopListener keyboard1PopListener) {
		this.listener = keyboard1PopListener;
	}

	public interface Keyboard1PopListener {
		public void onTimeSelect(String string);
	}
}
