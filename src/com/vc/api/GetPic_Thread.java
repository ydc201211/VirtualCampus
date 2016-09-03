package com.vc.api;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class GetPic_Thread {
	

	public static InputStream getPicIs(String url){
	
		try {
			URL httpurl = new URL(url);
			try {
				HttpURLConnection conn = (HttpURLConnection) httpurl
						.openConnection();
				conn.setReadTimeout(5000);
				conn.setRequestMethod("GET");
				conn.setDoInput(true);
				InputStream in = conn.getInputStream();
				return in;
			}catch (MalformedURLException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
