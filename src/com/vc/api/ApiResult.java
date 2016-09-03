package com.vc.api;

import org.json.JSONException;
import org.json.JSONObject;

public class ApiResult {

	public boolean isSuccess;
	public String resCode;
	public Object data;
	public String resInfo;
	public static final String SUCESS = "111";
	public static final String AUTHORITY_ERROR = "001";
	public static final String SERVER_ERROR = "002";

	public static ApiResult getApiResult(String jsonString) {
		ApiResult result = new ApiResult();
		JSONObject object;
		if (jsonString != null && !jsonString.equals("连接超时"))
			try {
				System.out.println(jsonString);
				object = new JSONObject(jsonString);
				result.resCode = object.getString("resultCode");
				result.isSuccess = result.resCode.equals(SUCESS);
				result.data = object.getString("data");
				try {
					JSONObject jsonData = object.getJSONObject("data");
					result.resInfo = jsonData.getString("resultinfo");
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		else {
			result.isSuccess = false;
			if (jsonString != null && jsonString.equals("连接超时")) {
				result.resInfo = "连接超时";
			}
		}
		return result;
	}

	@Override
	public String toString() {
		return "ApiResult [isSuccess=" + isSuccess + ", resCode=" + resCode
				+ ", data=" + data + ", resInfo=" + resInfo + "]";
	}

}
