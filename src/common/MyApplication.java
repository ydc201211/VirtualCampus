package common;

import android.app.Application;

import com.vc.entity.User;

public class MyApplication extends Application {
	private int userId;
	private User user;

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		setUserId(0);
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
