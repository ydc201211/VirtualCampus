package com.vc.api;

import java.io.IOException;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import common.Constants;

public class OkHttpConnection {

	private final static String TAG = "OkHttp";

	private final static OkHttpClient client = new OkHttpClient();

	public static JSONObject execute(Context context, String url,
			List<NameValuePair> params) {
		JSONObject object = null;
		FormEncodingBuilder builder = new FormEncodingBuilder();
		for (int i = 0; i < params.size(); i++) {
			builder.add(params.get(i).getName(), params.get(i).getValue());
		}

		Request request = new Request.Builder()
				.url(Constants.SERVERADDRESS + url)
				.header("User-Agent", "OkHttp Headers.java")
				.post(builder.build()).build();

		Response response;
		try {
			response = client.newCall(request).execute();
			if (response.isSuccessful()) {
				Log.i("TAG", "" + response.code());
				String result = response.body().string();
				try {
					object = new JSONObject(result);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				Log.i(TAG, "OKhttp连接成功");

				return object;

			}
			/* return null; */
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}
}
