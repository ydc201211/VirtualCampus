package com.vc.api;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.webkit.WebView;
import android.widget.ImageView;

public class Pic_Down_Thread extends Thread {
	private String url;
	private WebView webview;
	private Handler handler;
	private ImageView imageview;

	public Pic_Down_Thread(ImageView imageview, Handler handler, String PicName) {
		this.url = ApiUrl.URL_GET_IMAGE + "?imageUrl=" + PicName;
		this.imageview = imageview;
		this.handler = handler;
	}

	@Override
	public void run() {
		try {
			URL httpurl = new URL(url);
			try {
				HttpURLConnection conn = (HttpURLConnection) httpurl
						.openConnection();
				conn.setReadTimeout(5000);
				conn.setRequestMethod("GET");
				conn.setDoInput(true);
				InputStream in = conn.getInputStream();
				FileOutputStream out = null;
				File downloadfile = null;
				String filename = String.valueOf(System.currentTimeMillis());
				if (Environment.getExternalStorageState().equals(
						Environment.MEDIA_MOUNTED)) {
					File parent = Environment.getExternalStorageDirectory();
					downloadfile = new File(parent, filename);
					out = new FileOutputStream(downloadfile);
				}
				byte[] b = new byte[2 * 1024];
				int len;
				if (out != null) {
					while ((len = in.read(b)) != -1) {
						out.write(b, 0, len);
					}
				}
				final Bitmap bitmap = BitmapFactory.decodeFile(downloadfile
						.getAbsolutePath());
				if (bitmap != null) {
					handler.post(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							imageview.setImageBitmap(bitmap);
						}
					});
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
