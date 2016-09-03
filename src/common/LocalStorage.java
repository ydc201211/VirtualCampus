package common;

import android.content.Context;
import android.content.SharedPreferences;

public class LocalStorage {
	private static SharedPreferences getSharedPreferences(Context context) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				"userInfo", Context.MODE_PRIVATE);
		return sharedPreferences;
	}

	public static void saveString(Context context, String key, String value) {
		SharedPreferences sharedPreferences = getSharedPreferences(context);
		sharedPreferences.edit().putString(key, value).commit();

	}

	public static String getString(Context context, String key) {
		return getSharedPreferences(context).getString(key, "");
	}

}
