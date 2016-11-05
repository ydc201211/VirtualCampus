package com.vc.app;

import com.vc.entity.User;

import io.rong.imkit.RongIM;


import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;


public class App extends Application {
	
	private int userId;
	private User user;

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	private final static String TAG = "app";
	
	@Override
    public void onCreate() {

		super.onCreate();
		setUserId(0);
	    /**
	     * OnCreate �ᱻ����������룬��α������룬ȷ��ֻ������Ҫʹ�� RongIM �Ľ��̺� Push ����ִ���� init��
	     * io.rong.push Ϊ���� push �������ƣ������޸ġ�
	     */
	    if (getApplicationInfo().packageName.equals(getCurProcessName(getApplicationContext())) ||
	            "io.rong.push".equals(getCurProcessName(getApplicationContext()))) {
	
	        /**
	         * IMKit SDK���õ�һ�� ��ʼ��
	         */
	        RongIM.init(this);
	    }
	}
	
	

	/**
	 * ��õ�ǰ���̵�����
	 *
	 * @param context
	 * @return ���̺�
	 */
	public static String getCurProcessName(Context context) {
	
	    int pid = android.os.Process.myPid();
	
	    ActivityManager activityManager = (ActivityManager) context
	            .getSystemService(Context.ACTIVITY_SERVICE);
	
	    for (ActivityManager.RunningAppProcessInfo appProcess : activityManager
	            .getRunningAppProcesses()) {
	
	        if (appProcess.pid == pid) {
	            return appProcess.processName;
	        }
	    }
	    return null;
	}
	

	@Override
	public void onTerminate() {
		super.onTerminate();
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	   
}
