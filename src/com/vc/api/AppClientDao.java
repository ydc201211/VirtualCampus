package com.vc.api;

import org.apache.http.Header;
import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class AppClientDao {
	private static AsyncHttpClient mClient = new AsyncHttpClient();
	private Context context;

	public AppClientDao(Context context) {
		this.context = context;
	}

	/**
	 * \ 获取所有的标志物
	 */
	public void Get_Mark(final Handler mHandler) {
		RequestParams params = new RequestParams();
		System.out.println("-->>sys" + params); // 锟斤拷锟皆达拷锟斤拷
		mClient.post(ApiUrl.URL_GET_MARK, null, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] responseBody) {
				// 锟斤拷取锟斤拷锟截碉拷锟街凤拷
				String result = new String(responseBody);
				try {
					JSONObject jb = new JSONObject(result);
					int resultCode = jb.getInt("resultCode");
					String resultInfo = jb.getString("resultInfo");
					System.out.print(result);
					if (resultCode == 111)
						mHandler.obtainMessage(ResultMessage.GET_MARK_SUCCESS,
								result).sendToTarget();
					else
						mHandler.obtainMessage(ResultMessage.GET_MARK_FAILED,
								resultInfo).sendToTarget();
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				if (error != null) {
					if (true)
						System.out.println("error.getMessage-->>"
								+ error.toString());
				}
				if (error instanceof ConnectTimeoutException) {
					mHandler.obtainMessage(ResultMessage.TIMEOUT, "请求超时")
							.sendToTarget();
					return;
				}
				mHandler.obtainMessage(ResultMessage.FAILED, "操作失败")
						.sendToTarget();
			}
		});
	}

	/**
	 * \ 通过markId获取标志物
	 */
	public void GetMarkById(final Handler mHandler, int markId) {
		RequestParams params = new RequestParams();
		params.put("markId", markId);
		System.out.println("-->>sys" + params); // 锟斤拷锟皆达拷锟斤拷
		mClient.post(ApiUrl.URL_GET_MARK_BYID, params,
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							byte[] responseBody) {
						// 锟斤拷取锟斤拷锟截碉拷锟街凤拷
						String result = new String(responseBody);
						try {
							JSONObject jb = new JSONObject(result);
							int resultCode = jb.getInt("resultCode");
							String resultinfo = jb.getString("resultInfo");
							System.out.print(result);
							if (resultCode == 111)
								mHandler.obtainMessage(
										ResultMessage.GET_MARK_BYID_SUCCESS,
										result).sendToTarget();
							else
								mHandler.obtainMessage(
										ResultMessage.GET_MARK_BYID_FAILED,
										resultinfo).sendToTarget();
						} catch (JSONException e) {
							e.printStackTrace();
						}

					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							byte[] responseBody, Throwable error) {
						if (error != null) {
							if (true)
								System.out.println("error.getMessage-->>"
										+ error.toString());
						}
						if (error instanceof ConnectTimeoutException) {
							mHandler.obtainMessage(ResultMessage.TIMEOUT,
									"请求超时").sendToTarget();
							return;
						}
						mHandler.obtainMessage(ResultMessage.FAILED, "操作失败")
								.sendToTarget();
					}
				});
	}

	/**
	 * \ 扫一扫通过markId获取标志物
	 */
	public void GetMarkById_Code(final Handler mHandler, int markId) {
		RequestParams params = new RequestParams();
		params.put("markId", markId);
		System.out.println("-->>sys" + params); // 锟斤拷锟皆达拷锟斤拷
		mClient.post(ApiUrl.URL_GET_MARK_BYID, params,
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							byte[] responseBody) {
						// 锟斤拷取锟斤拷锟截碉拷锟街凤拷
						String result = new String(responseBody);
						try {
							JSONObject jb = new JSONObject(result);
							int resultCode = jb.getInt("resultCode");
							String resultinfo = jb.getString("resultInfo");
							System.out.print(result);
							if (resultCode == 111)
								mHandler.obtainMessage(
										ResultMessage.GET_MARK_BYID_CODE_SUCCESS,
										result).sendToTarget();
							else
								mHandler.obtainMessage(
										ResultMessage.GET_MARK_BYID_CODE_FAILED,
										resultinfo).sendToTarget();
						} catch (JSONException e) {
							e.printStackTrace();
						}

					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							byte[] responseBody, Throwable error) {
						if (error != null) {
							if (true)
								System.out.println("error.getMessage-->>"
										+ error.toString());
						}
						if (error instanceof ConnectTimeoutException) {
							mHandler.obtainMessage(ResultMessage.TIMEOUT,
									"请求超时").sendToTarget();
							return;
						}
						mHandler.obtainMessage(ResultMessage.FAILED, "操作失败")
								.sendToTarget();
					}
				});
	}

	/**
	 * \ 锟斤拷取锟斤拷应ParentId锟侥斤拷锟斤拷锟斤拷锟斤拷息
	 */
	public void GetMarkByParentId(final Handler mHandler, int parentId) {
		RequestParams params = new RequestParams();
		params.put("parentId", parentId);
		System.out.println("-->>sys" + params); // 锟斤拷锟皆达拷锟斤拷
		mClient.post(ApiUrl.URL_GET_MARK_BYPARENTID, params,
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							byte[] responseBody) {
						// 锟斤拷取锟斤拷锟截碉拷锟街凤拷
						String result = new String(responseBody);
						try {
							JSONObject jb = new JSONObject(result);
							int resultCode = jb.getInt("resultCode");
							String resultinfo = jb.getString("resultInfo");
							System.out.print("result" + result);
							if (resultCode == 111)
								mHandler.obtainMessage(
										ResultMessage.GET_MARK_BYPARENTID_SUCCESS,
										result).sendToTarget();
							else
								mHandler.obtainMessage(
										ResultMessage.GET_MARK_BYPARENTID_FAILED,
										resultinfo).sendToTarget();
						} catch (JSONException e) {
							e.printStackTrace();
						}

					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							byte[] responseBody, Throwable error) {
						if (error != null) {
							if (true)
								System.out.println("error.getMessage-->>"
										+ error.toString());
						}
						if (error instanceof ConnectTimeoutException) {
							mHandler.obtainMessage(ResultMessage.TIMEOUT,
									"请求超时").sendToTarget();
							return;
						}
						mHandler.obtainMessage(ResultMessage.FAILED, "操作失败")
								.sendToTarget();
					}
				});
	}

	/**
	 * \ 根据关键词搜索
	 */
	public void GetSearchMarks(final Handler mHandler, String keywords) {
		RequestParams params = new RequestParams();
		params.put("keyword", keywords);
		System.out.println("-->>sys" + params); // 锟斤拷锟皆达拷锟斤拷
		mClient.post(ApiUrl.URL_GET_SEARACH_MARKS, params,
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							byte[] responseBody) {
						// 锟斤拷取锟斤拷锟截碉拷锟街凤拷
						String result = new String(responseBody);
						try {
							JSONObject jb = new JSONObject(result);
							int resultCode = jb.getInt("resultCode");
							String resultinfo = jb.getString("resultInfo");
							System.out.print("result" + result);
							if (resultCode == 111)
								mHandler.obtainMessage(
										ResultMessage.GET_SEARACHMARKS_SUCCESS,
										result).sendToTarget();
							else
								mHandler.obtainMessage(
										ResultMessage.GET_SEARACHMARKS_FAILED,
										resultinfo).sendToTarget();
						} catch (JSONException e) {
							e.printStackTrace();
						}

					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							byte[] responseBody, Throwable error) {
						if (error != null) {
							if (true)
								System.out.println("error.getMessage-->>"
										+ error.toString());
						}
						if (error instanceof ConnectTimeoutException) {
							mHandler.obtainMessage(ResultMessage.TIMEOUT,
									"请求超时").sendToTarget();
							return;
						}
						mHandler.obtainMessage(ResultMessage.FAILED, "操作失败")
								.sendToTarget();
					}
				});
	}

	/**
	 * \ 锟斤拷锟斤拷锟斤拷应锟斤拷始锟斤拷木锟轿筹拷锟�
	 */

	public void GetMark_Coord(final Handler mHandler, String start, String end) {
		RequestParams params = new RequestParams();
		params.put("startMark", start);
		params.put("endMark", end);
		System.out.println("-->>sys" + params); // 锟斤拷锟皆达拷锟斤拷
		mClient.post(ApiUrl.URL_GET_CORRD, params,
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							byte[] responseBody) {
						// 锟斤拷取锟斤拷锟截碉拷锟街凤拷
						String result = new String(responseBody);
						try {
							JSONObject jb = new JSONObject(result);
							int resultCode = jb.getInt("resultCode");
							String resultinfo = jb.getString("resultInfo");
							System.out.print(result);
							if (resultCode == 111) {
								mHandler.obtainMessage(
										ResultMessage.GET_MARK_Coord_SUCCESS,
										result).sendToTarget();
							} else
								mHandler.obtainMessage(
										ResultMessage.GET_MARK_Coord_FAILED,
										resultinfo).sendToTarget();
						} catch (JSONException e) {
							e.printStackTrace();
						}

					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							byte[] responseBody, Throwable error) {
						if (error != null) {
							if (true)
								System.out.println("error.getMessage-->>"
										+ error.toString());
						}
						if (error instanceof ConnectTimeoutException) {
							mHandler.obtainMessage(ResultMessage.TIMEOUT,
									"网络连接异常").sendToTarget();
							return;
						}
						mHandler.obtainMessage(ResultMessage.FAILED, "获取数据失败")
								.sendToTarget();
					}
				});
	}

	/**
	 * \ 锟斤拷锟斤拷锟斤拷应id锟斤拷图片锟斤拷址
	 */

	public void GetMark_PicName(final Handler mHandler, int markId) {
		RequestParams params = new RequestParams();
		params.put("markId", markId);
		System.out.println("-->>sys" + params); // 锟斤拷锟皆达拷锟斤拷
		mClient.post(ApiUrl.URL_GET_IMAGE_NAME, params,
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							byte[] responseBody) {
						// 锟斤拷取锟斤拷锟截碉拷锟街凤拷
						String result = new String(responseBody);
						try {
							JSONObject jb = new JSONObject(result);
							int resultCode = jb.getInt("resultCode");
							String resultinfo = jb.getString("resultInfo");
							System.out.print(result);
							if (resultCode == 111) {
								String data = jb.getString("results");
								mHandler.obtainMessage(
										ResultMessage.GET_MARK_PICNAME_SUCCESS,
										data).sendToTarget();
							} else
								mHandler.obtainMessage(
										ResultMessage.GET_MARK_PICNAME_FAILED,
										resultinfo).sendToTarget();
						} catch (JSONException e) {
							e.printStackTrace();
						}

					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							byte[] responseBody, Throwable error) {
						if (error != null) {
							if (true)
								System.out.println("error.getMessage-->>"
										+ error.toString());
						}
						if (error instanceof ConnectTimeoutException) {
							mHandler.obtainMessage(ResultMessage.TIMEOUT,
									"连接超时").sendToTarget();
							return;
						}
						mHandler.obtainMessage(ResultMessage.FAILED, "数据获取失败")
								.sendToTarget();
					}
				});
	}

	/**
	 * \ 锟斤拷锟斤拷锟斤拷应url锟斤拷图片
	 */
	public void GetMark_Pic_Load(final Handler mHandler, String imageUrl) {
		RequestParams params = new RequestParams();
		params.put("imageUrl", imageUrl);
		System.out.println("-->>sys" + params); // 锟斤拷锟皆达拷锟斤拷
		mClient.post(ApiUrl.URL_GET_IMAGE, params,
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							byte[] responseBody) {
						// 锟斤拷取锟斤拷锟截碉拷锟街凤拷
						String result = new String(responseBody);
						try {
							JSONObject jb = new JSONObject(result);
							int resultCode = jb.getInt("resultCode");
							String resultinfo = jb.getString("resultInfo");
							System.out.print("result" + result);
							if (resultCode == 111)
								mHandler.obtainMessage(
										ResultMessage.GET_MARK_PIC_SUCCESS,
										result).sendToTarget();
							else
								mHandler.obtainMessage(
										ResultMessage.GET_MARK_PIC_FAILED,
										resultinfo).sendToTarget();
						} catch (JSONException e) {
							e.printStackTrace();
						}

					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							byte[] responseBody, Throwable error) {
						if (error != null) {
							if (true)
								System.out.println("error.getMessage-->>"
										+ error.toString());
						}
						if (error instanceof ConnectTimeoutException) {
							mHandler.obtainMessage(ResultMessage.TIMEOUT,
									"网络连接超时").sendToTarget();
							return;
						}
						mHandler.obtainMessage(ResultMessage.FAILED, "数据获取失败")
								.sendToTarget();
					}
				});
	}

	/**
	 * 鐭俊鎺ュ彛
	 * 
	 * @param mHandler
	 * @param keywords
	 */
	public void GetSMS_Code(final Handler mHandler, String phoneNo) {
		RequestParams params = new RequestParams();
		params.put("phoneNo", phoneNo);
		System.out.println("-->>sys" + params); // 璋冭瘯浠ｇ爜
		mClient.post(ApiUrl.URL_SEND_SMS, params,
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							byte[] responseBody) {
						// 鑾峰彇杩斿洖鐨勫瓧绗︿覆
						String result = new String(responseBody);
						try {
							JSONObject jb = new JSONObject(result);
							int resultCode = jb.getInt("resultCode");
							String info = jb.getString("resultInfo");
							System.out.print("====0" + result);
							if (resultCode == 111) {
								int ICode = jb.getInt("results");
								mHandler.obtainMessage(
										ResultMessage.SENDSUCCESS, ICode)
										.sendToTarget();
							} else
								mHandler.obtainMessage(
										ResultMessage.SENDFAILED, info)
										.sendToTarget();
						} catch (JSONException e) {
							e.printStackTrace();
						}

					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							byte[] responseBody, Throwable error) {
						if (error != null) {
							if (true)
								System.out.println("error.getMessage-->>"
										+ error.toString());
						}
						if (error instanceof ConnectTimeoutException) {
							mHandler.obtainMessage(ResultMessage.TIMEOUT,
									"网络连接异常").sendToTarget();
							return;
						}
						mHandler.obtainMessage(ResultMessage.FAILED, "数据获取失败")
								.sendToTarget();
					}
				});
	}

	/**
	 * 鐧诲綍
	 * 
	 * @param mHandler
	 * @param keywords
	 */
	public void Get_Login(final Handler mHandler, String userName,
			String password) {
		RequestParams params = new RequestParams();
		params.put("userName", userName);
		params.put("password", password);
		System.out.println("-->>sys" + params); // 璋冭瘯浠ｇ爜
		mClient.post(ApiUrl.URL_LOGIN, params, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] responseBody) {
				// 鑾峰彇杩斿洖鐨勫瓧绗︿覆
				String result = new String(responseBody);
				try {
					JSONObject jb = new JSONObject(result);
					int resultCode = jb.getInt("resultCode");
					String info = jb.getString("resultInfo");
					System.out.print("====0" + result);
					if (resultCode == 111) {
						int results = jb.getInt("results");
						mHandler.obtainMessage(ResultMessage.LOGIN_SUCCESS,
								results).sendToTarget();
					} else {
						mHandler.obtainMessage(ResultMessage.LOGIN_FAILED, info)
								.sendToTarget();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				if (error != null) {
					if (true)
						System.out.println("error.getMessage-->>"
								+ error.toString());
				}
				if (error instanceof ConnectTimeoutException) {
					mHandler.obtainMessage(ResultMessage.TIMEOUT, "网络超时")
							.sendToTarget();
					return;
				}
				mHandler.obtainMessage(ResultMessage.FAILED, "数据获取失败")
						.sendToTarget();
			}
		});
	}

	/**
	 * 娉ㄥ唽
	 * 
	 * @param mHandler
	 * @param keywords
	 */
	public void Get_Registe(final Handler mHandler, String userName,
			String password, String nickName, String user_picPath,
			String user_sex) {
		RequestParams params = new RequestParams();
		params.put("userName", userName);
		params.put("password", password);
		params.put("nickName", nickName);
		params.put("user_picPath", user_picPath);
		params.put("user_sex", user_sex);
		System.out.println("-->>sys" + params); // 璋冭瘯浠ｇ爜
		mClient.post(ApiUrl.URL_REGISTE, params,
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							byte[] responseBody) {
						// 鑾峰彇杩斿洖鐨勫瓧绗︿覆
						String result = new String(responseBody);
						try {
							JSONObject jb = new JSONObject(result);
							int resultCode = jb.getInt("resultCode");
							String info = jb.getString("resultInfo");
							System.out.print("====0" + result);
							if (resultCode == 111) {
								int results = jb.getInt("results");
								mHandler.obtainMessage(
										ResultMessage.REGSITE_SUCCESS, results)
										.sendToTarget();
							} else {
								mHandler.obtainMessage(
										ResultMessage.REGSITE_FAILED, info)
										.sendToTarget();
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}

					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							byte[] responseBody, Throwable error) {
						if (error != null) {
							if (true)
								System.out.println("error.getMessage-->>"
										+ error.toString());
						}
						if (error instanceof ConnectTimeoutException) {
							mHandler.obtainMessage(ResultMessage.TIMEOUT,
									"网络连接超时").sendToTarget();
							return;
						}
						mHandler.obtainMessage(ResultMessage.FAILED, "数据获取失败")
								.sendToTarget();
					}
				});
	}

	/**
	 * 妫�煡璐︽埛
	 * 
	 * @param mHandler
	 * @param keywords
	 */
	public void Get_CheckAccount(final Handler mHandler, String userName) {
		RequestParams params = new RequestParams();
		params.put("userName", userName);
		System.out.println("-->>sys" + params); // 璋冭瘯浠ｇ爜
		mClient.post(ApiUrl.URL_CHECKACCOUNT, params,
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							byte[] responseBody) {
						// 鑾峰彇杩斿洖鐨勫瓧绗︿覆
						String result = new String(responseBody);
						try {
							JSONObject jb = new JSONObject(result);
							int resultCode = jb.getInt("resultCode");
							String info = jb.getString("resultInfo");
							System.out.print("====0" + result);
							if (resultCode != 111) {
								mHandler.obtainMessage(
										ResultMessage.CHECK_SUCCESS, 1)
										.sendToTarget();
							} else {
								mHandler.obtainMessage(
										ResultMessage.CHECK_FAILED, info)
										.sendToTarget();
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}

					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							byte[] responseBody, Throwable error) {
						if (error != null) {
							if (true)
								System.out.println("error.getMessage-->>"
										+ error.toString());
						}
						if (error instanceof ConnectTimeoutException) {
							mHandler.obtainMessage(ResultMessage.TIMEOUT,
									"网络连接超时").sendToTarget();
							return;
						}
						mHandler.obtainMessage(ResultMessage.FAILED, "数据获取失败")
								.sendToTarget();
					}
				});
	}

	/**
	 * 鑾峰緱鐢ㄦ埛淇℃伅
	 * 
	 * @param mHandler
	 * @param keywords
	 */
	public void Get_User_ById(final Handler mHandler, int userId) {
		RequestParams params = new RequestParams();
		params.put("userId", userId);
		System.out.println("-->>sys" + params); // 璋冭瘯浠ｇ爜
		mClient.post(ApiUrl.URL_GET_USER, params,
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							byte[] responseBody) {
						// 鑾峰彇杩斿洖鐨勫瓧绗︿覆
						String result = new String(responseBody);
						try {
							JSONObject jb = new JSONObject(result);
							int resultCode = jb.getInt("resultCode");
							String info = jb.getString("resultInfo");
							if (resultCode == 111) {
								mHandler.obtainMessage(
										ResultMessage.GETUSERSUCCESS, result)
										.sendToTarget();
							} else {
								mHandler.obtainMessage(
										ResultMessage.GETUSERFAILED, info)
										.sendToTarget();
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}

					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							byte[] responseBody, Throwable error) {
						if (error != null) {
							if (true)
								System.out.println("error.getMessage-->>"
										+ error.toString());
						}
						if (error instanceof ConnectTimeoutException) {
							mHandler.obtainMessage(ResultMessage.TIMEOUT,
									"网络连接超时").sendToTarget();
							return;
						}
						mHandler.obtainMessage(ResultMessage.FAILED, "数据获取失败")
								.sendToTarget();
					}
				});
	}

	/**
	 * 鐭俊鎺ュ彛
	 * 
	 * @param mHandler
	 * @param keywords
	 */
	public void Get_ChangePsw_Code(final Handler mHandler, String phoneNo,
			String newpassword) {
		RequestParams params = new RequestParams();
		params.put("userName", phoneNo);
		params.put("password", newpassword);
		System.out.println("-->>sys" + params); // 璋冭瘯浠ｇ爜
		mClient.post(ApiUrl.URL_CHANGEPSW, params,
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							byte[] responseBody) {
						// 鑾峰彇杩斿洖鐨勫瓧绗︿覆
						String result = new String(responseBody);
						try {
							JSONObject jb = new JSONObject(result);
							int resultCode = jb.getInt("resultCode");
							String info = jb.getString("resultInfo");
							System.out.print("====0" + result);
							if (resultCode == 111) {
								int ICode = jb.getInt("results");
								mHandler.obtainMessage(
										ResultMessage.CHANGE_PWD_SUCCESS, ICode)
										.sendToTarget();
							} else
								mHandler.obtainMessage(
										ResultMessage.CHANGE_PWD_FAILED, info)
										.sendToTarget();
						} catch (JSONException e) {
							e.printStackTrace();
						}

					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							byte[] responseBody, Throwable error) {
						if (error != null) {
							if (true)
								System.out.println("error.getMessage-->>"
										+ error.toString());
						}
						if (error instanceof ConnectTimeoutException) {
							mHandler.obtainMessage(ResultMessage.TIMEOUT,
									"网络异常").sendToTarget();
							return;
						}
						mHandler.obtainMessage(ResultMessage.FAILED, "数据获取失败")
								.sendToTarget();
					}
				});
	}

	/**
	 * 鏀瑰ご鍍�
	 * 
	 * @param mHandler
	 * @param keywords
	 */
	public void Get_ChangeImage(final Handler mHandler, int userId,
			String imageName) {
		RequestParams params = new RequestParams();
		params.put("userId", userId);
		params.put("user_picPath", imageName);
		System.out.println("-->>sys" + params); // 璋冭瘯浠ｇ爜
		mClient.post(ApiUrl.URL_CHANGEImage, params,
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							byte[] responseBody) {
						// 鑾峰彇杩斿洖鐨勫瓧绗︿覆
						String result = new String(responseBody);
						try {
							JSONObject jb = new JSONObject(result);
							int resultCode = jb.getInt("resultCode");
							if (resultCode == 111) {
								mHandler.obtainMessage(
										ResultMessage.Change_SUCCESS,
										resultCode).sendToTarget();
							} else {
								mHandler.obtainMessage(
										ResultMessage.Change_FAILED, "修改失败")
										.sendToTarget();
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}

					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							byte[] responseBody, Throwable error) {
						if (error != null) {
							if (true)
								System.out.println("error.getMessage-->>"
										+ error.toString());
						}
						if (error instanceof ConnectTimeoutException) {
							mHandler.obtainMessage(ResultMessage.TIMEOUT,
									"连接超时").sendToTarget();
							return;
						}
						mHandler.obtainMessage(ResultMessage.FAILED, "数据获取失败")
								.sendToTarget();
					}
				});
	}

	public void getPointList(final Handler mHandler, String keyword) {
		RequestParams params = new RequestParams();
		params.put("keyword", keyword);

		mClient.post(ApiUrl.URL_GET_SEARACH_MARKS, params,
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							byte[] responseBody) {
						// 锟斤拷取锟斤拷锟截碉拷锟街凤拷
						String result = new String(responseBody);
						Log.i("okokok", result);
						try {
							JSONObject json = new JSONObject(result);
							int resultCode = json.getInt("resultCode");
							String resultInfo = json.getString("resultInfo");

							if (resultCode == 111)
								mHandler.obtainMessage(
										ResultMessage.GET_MARKS_PROMPT_SUCCESS,
										result).sendToTarget();
							else
								mHandler.obtainMessage(
										ResultMessage.GET_MARKS_PROMPT_FAIL,
										resultInfo).sendToTarget();
						} catch (JSONException e) {
							e.printStackTrace();
						}

					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							byte[] responseBody, Throwable error) {
						if (error != null) {
							if (true)
								System.out.println("error.getMessage-->>"
										+ error.toString());
						}
						if (error instanceof ConnectTimeoutException) {
							mHandler.obtainMessage(ResultMessage.TIMEOUT,
									"连接超时").sendToTarget();
							return;
						}
						mHandler.obtainMessage(ResultMessage.FAILED, "连接失败")
								.sendToTarget();
					}
				});
	}
	
	public void getToken(final Handler mHandler, String userId){
		RequestParams params = new RequestParams();
		params.put("userId", userId);

		mClient.post(ApiUrl.URL_GET_TOKEN, params,
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							byte[] responseBody) {
						// 锟斤拷取锟斤拷锟截碉拷锟街凤拷
						String result = new String(responseBody);
						
						try {
							JSONObject json = new JSONObject(result);
							int resultCode = json.getInt("resultCode");
							String resultInfo = json.getString("resultInfo");
							Log.i("net", result);
							if (resultCode == 111)
								mHandler.obtainMessage(
										ResultMessage.GET_TOKEN_SUCCESS,
										result).sendToTarget();
							else
								mHandler.obtainMessage(
										ResultMessage.GET_TOKEN_FAIL,
										resultInfo).sendToTarget();
						} catch (JSONException e) {
							e.printStackTrace();
						}

					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							byte[] responseBody, Throwable error) {
						if (error != null) {
							if (true)
								System.out.println("error.getMessage-->>"
										+ error.toString());
						}
						if (error instanceof ConnectTimeoutException) {
							mHandler.obtainMessage(ResultMessage.TIMEOUT,
									"连接超时").sendToTarget();
							return;
						}
						mHandler.obtainMessage(ResultMessage.FAILED, "连接失败")
								.sendToTarget();
					}
				});
	}

}
