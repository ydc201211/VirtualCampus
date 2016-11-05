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
	     * OnCreate 会被多个进程重入，这段保护代码，确保只有您需要使用 RongIM 的进程和 Push 进程执行了 init。
	     * io.rong.push 为融云 push 进程名称，不可修改。
	     */
	    if (getApplicationInfo().packageName.equals(getCurProcessName(getApplicationContext())) ||
	            "io.rong.push".equals(getCurProcessName(getApplicationContext()))) {
	
	        /**
	         * IMKit SDK调用第一步 初始化
	         */
	        RongIM.init(this);
	    }
	}
	
	

	/**
	 * 获得当前进程的名字
	 *
	 * @param context
	 * @return 进程号
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
