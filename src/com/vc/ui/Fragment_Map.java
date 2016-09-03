package com.vc.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.utils.DistanceUtil;
import com.baidu.navisdk.adapter.BNOuterTTSPlayerCallback;
import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.baidu.navisdk.adapter.BNRoutePlanNode.CoordinateType;
import com.baidu.navisdk.adapter.BaiduNaviManager;
import com.baidu.navisdk.adapter.BaiduNaviManager.NaviInitListener;
import com.baidu.navisdk.adapter.BaiduNaviManager.RoutePlanListener;
import com.guide.BNDemoGuideActivity;
import com.guide.MyOrientationListener;
import com.guide.MyOrientationListener.OnOrientationListener;
import com.vc.api.AppClientDao;
import com.vc.api.ResultMessage;
import com.vc.code.CaptureActivity;
import com.vc.dialog.DialogUtil;
import com.vc.dialog.KeyBoardPopupWindow;
import com.vc.dialog.KeyBoardPopupWindow.Keyboard1PopListener;
import com.vc.dialog.KeyBoardPopupWindow.Keyboard1isPupListener;
import com.vc.entity.MarkEntity;

public class Fragment_Map extends Fragment implements OnClickListener {
	private MapView mMapView;
	private BaiduMap mBaiduMap;
	private boolean istrue = true;
	private Context context;
	private EditText edit_serach;
	private ImageView Iv_saoyisao;
	// 锟斤拷位锟斤拷锟�
	private LocationClient mLocationClient;
	private MyLocationListener listener;
	private double mLatitude, mLongtitude;
	private double startLatitude = 0, startLongtitude = 0;
	private double endLatitude = 0, endLongtitude = 0;
	// 锟皆讹拷锟藉定位图锟斤拷
	private BitmapDescriptor mIconLocation;
	private MyOrientationListener myOrientationListener;
	MapStatusUpdate msu;// 锟斤拷锟脚硷拷锟斤拷
	private float mCurrentx;
	private PoiSearch mPoiSearch;
	int flag = 1;
	private LocationMode mLocationMode;
	// 锟斤拷锟斤拷锟斤拷锟斤拷锟�
	private BitmapDescriptor mMarker;

	// 锟斤拷锟斤拷
	public static List<Activity> activityList = new LinkedList<Activity>();
	public List<MarkEntity> markList = new ArrayList<MarkEntity>();

	private static final String APP_FOLDER_NAME = "VirtualCampus";

	private ImageView mDb06ll = null;
	private String mSDCardPath = null;
	Dialog mDialog;
	public static final String ROUTE_PLAN_NODE = "routePlanNode";
	public static final String SHOW_CUSTOM_ITEM = "showCustomItem";
	public static final String RESET_END_NODE = "resetEndNode";
	public static final String VOID_MODE = "voidMode";
	private ImageView image_zoomadd, image_zoomlose, image_backlocation;

	private Boolean isShowPup = false;// ��ʾ�Ƿ����˼���
	KeyBoardPopupWindow kbdpw;
	private AppClientDao mAppClientDao = new AppClientDao(getActivity());
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (mDialog.isShowing())
				mDialog.dismiss();
			switch (msg.what) {
			case ResultMessage.GET_MARK_SUCCESS: {
				Object objs = (Object) msg.obj;
				try {
					String arrayString = new JSONObject(objs.toString())
							.getString("results");
					List<MarkEntity> resList = JSON.parseArray(arrayString,
							MarkEntity.class);
					addMarkInfoOverLays(resList);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
				break;
			case ResultMessage.GET_MARK_FAILED:
				Toast.makeText(getActivity(), msg.obj.toString(),
						Toast.LENGTH_SHORT).show();
				break;
			case ResultMessage.FAILED:
				Toast.makeText(getActivity(), msg.obj.toString(),
						Toast.LENGTH_SHORT).show();
				break;
			case ResultMessage.TIMEOUT:
				Toast.makeText(getActivity(), msg.obj.toString(),
						Toast.LENGTH_SHORT).show();
				break;
			case ResultMessage.GET_MARK_BYID_SUCCESS: {
				Object objs = (Object) msg.obj;
				try {
					JSONObject jsonObject = new JSONObject(objs.toString());
					// 锟斤拷锟斤拷json锟斤拷锟斤拷锟斤拷
					JSONObject jsonObject2 = jsonObject
							.getJSONObject("results");
					MarkEntity mark = new MarkEntity();
					mark.setMarkId(jsonObject2.getInt("markId"));
					mark.setMarkName(jsonObject2.getString("markName"));
					mark.setMark_latitude(jsonObject2
							.getDouble("mark_latitude"));
					mark.setMark_longitude(jsonObject2
							.getDouble("mark_longitude"));
					mark.setMark_detail(jsonObject2.getString("mark_detail"));
					mark.setMark_like(jsonObject2.getInt("mark_like"));
					mark.setMark_parentId(jsonObject2.getInt("mark_parentId"));
					mark.setMark_isOnMap(jsonObject2.getInt("mark_isOnMap"));
					addSerachOverLayPUP(mark);// ////////
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
				break;
			case ResultMessage.GET_MARK_BYID_FAILED:
				Toast.makeText(getActivity(), msg.obj.toString(),
						Toast.LENGTH_SHORT).show();
				break;
			/**
			 * 扫一扫返回结果
			 */
			case ResultMessage.GET_MARK_BYID_CODE_SUCCESS: {
				Object objs = (Object) msg.obj;
				try {
					JSONObject jsonObject = new JSONObject(objs.toString());
					// 锟斤拷锟斤拷json锟斤拷锟斤拷锟斤拷
					JSONObject jsonObject2 = jsonObject
							.getJSONObject("results");
					MarkEntity mark = new MarkEntity();
					mark.setMarkId(jsonObject2.getInt("markId"));
					mark.setMarkName(jsonObject2.getString("markName"));
					mark.setMark_latitude(jsonObject2
							.getDouble("mark_latitude"));
					mark.setMark_longitude(jsonObject2
							.getDouble("mark_longitude"));
					mark.setMark_detail(jsonObject2.getString("mark_detail"));
					mark.setMark_like(jsonObject2.getInt("mark_like"));
					mark.setMark_parentId(jsonObject2.getInt("mark_parentId"));
					mark.setMark_isOnMap(jsonObject2.getInt("mark_isOnMap"));
					addSerachOverLayPUP(mark);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
				break;
			case ResultMessage.GET_MARK_BYID_CODE_FAILED:
				Toast.makeText(getActivity(), msg.obj.toString(),
						Toast.LENGTH_SHORT).show();
				break;
			case ResultMessage.GET_SEARACHMARKS_SUCCESS: {
				Object objs = (Object) msg.obj;
				try {
					String arrayString = new JSONObject(objs.toString())
							.getString("results");
					List<MarkEntity> resList = JSON.parseArray(arrayString,
							MarkEntity.class);
					if (resList.size() == 1)
						addSerachMarkOverLays(resList);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
				break;
			case ResultMessage.GET_SEARACHMARKS_FAILED:
				Toast.makeText(getActivity(), msg.obj.toString(),
						Toast.LENGTH_SHORT).show();
				break;

			case ResultMessage.GET_MARK_Coord_SUCCESS: {
				Object objs = (Object) msg.obj;
				try {
					String arrayString = new JSONObject(objs.toString())
							.getString("results");
					List<MarkEntity> resList = JSON.parseArray(arrayString,
							MarkEntity.class);
					if (resList.size() == 2) {
						Lines_Choose(resList);
					} else
						Toast.makeText(context, "起点无效", 0).show();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
				break;
			case ResultMessage.GET_MARK_Coord_FAILED:
				Toast.makeText(getActivity(), msg.obj.toString(),
						Toast.LENGTH_SHORT).show();
				break;

			}

		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		SDKInitializer.initialize(getActivity().getApplicationContext());
		View view = inflater.inflate(R.layout.fragment_map, null);
		mDialog = DialogUtil.getLoadDialog(getActivity(), "数据加载中...");
		mDialog.setCancelable(true);
		mDialog.setCanceledOnTouchOutside(false);
		mDialog.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				mHandler.removeCallbacksAndMessages(null);
				mDialog.dismiss();
			}
		});
		context = getActivity();
		activityList.add(getActivity());
		init(view);
		mBaiduMap = mMapView.getMap();
		initMapControls(mBaiduMap); // 锟斤拷锟斤拷锟皆达拷图锟斤拷
		initLocation();// 锟斤拷始锟斤拷锟斤拷位
		initMarkers();// 锟斤拷始锟斤拷锟斤拷锟斤拷锟斤拷
		mBaiduMap.setTrafficEnabled(true);
		initMap();
		mAppClientDao.Get_Mark(mHandler);
		mDialog.show();
		return view;
	}

	/**
	 * 锟斤拷锟斤拷logo锟斤拷锟斤拷锟斤拷锟斤拷
	 * 
	 * @param mBaiduMap2
	 */
	private void initMapControls(BaiduMap mBaiduMap2) {
		msu = MapStatusUpdateFactory.zoomTo(19.0f);
		mBaiduMap.setMapStatus(msu);
		mMapView.showZoomControls(false);// 锟截憋拷锟皆讹拷锟斤拷锟斤拷锟脚帮拷钮
		mMapView.showScaleControl(false);
		int count = mMapView.getChildCount();
		for (int i = 0; i < count; i++) {
			View child = mMapView.getChildAt(i);
			if (child instanceof ImageView) {
				// 锟斤拷锟截百讹拷logo
				child.setVisibility(View.INVISIBLE);
			}
		}
	}

	private void init(View view) {
		// TODO Auto-generated method stub
		mDb06ll = (ImageView) view.findViewById(R.id.button1);
		mMapView = (MapView) view.findViewById(R.id.bmapView);
		edit_serach = (EditText) view.findViewById(R.id.search_edit);
		edit_serach.setFocusable(false);
		image_zoomadd = (ImageView) view.findViewById(R.id.image_zoom_add);
		image_zoomlose = (ImageView) view.findViewById(R.id.image_zoom_lose);
		image_backlocation = (ImageView) view
				.findViewById(R.id.image_backlocation);
		mDb06ll.setOnClickListener(this);
		image_zoomadd.setOnClickListener(this);
		image_zoomlose.setOnClickListener(this);
		image_backlocation.setOnClickListener(this);
		Iv_saoyisao = (ImageView) view.findViewById(R.id.image_saoyisao);
		edit_serach.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					Intent intent = new Intent(context,
							Activity_MapSerach.class);
					Bundle bundle = new Bundle();
					bundle.putDouble("mLatitude", mLatitude);
					bundle.putDouble("mLongtitude", mLongtitude);
					intent.putExtras(bundle);
					startActivityForResult(intent, 1);
					return true;
				} else
					return false;
			}
		});
		Iv_saoyisao.setOnClickListener(this);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1 && resultCode == 2) {
			if (data != null) {
				Bundle bundle = data.getExtras();
				String content = bundle.getString("content").trim();
				if (content != null || (!content.equals(""))) {
					// edit_serach.setText(content);
					flag = 0;
					mAppClientDao.GetSearchMarks(mHandler, content);
					mDialog.show();
				}
			}
		}
		if (requestCode == 1 && resultCode == 9) {
			if (data != null) {
				Bundle bundle = data.getExtras();
				int markId = 1;
				markId = bundle.getInt("markId");
				flag = 0;
				mAppClientDao.GetMarkById(mHandler, markId);
				mDialog.show();
			}
		}
		if (requestCode == 2 && resultCode == 12) {
			if (BaiduNaviManager.isNaviInited()) {
				routeplanToNavi(CoordinateType.BD09LL);
			}
		}
		if (requestCode == 6 && resultCode == 6) {
			Bundle bundle = data.getExtras();
			String start = bundle.getString("start");
			String end = bundle.getString("end");
			mAppClientDao.GetMark_Coord(mHandler, start.trim(), end.trim());
			mDialog.show();
		}
		if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
			Bundle bundle = data.getExtras();
			String result = bundle.getString("result");
			flag = 1;
			int a = Integer.parseInt(result);
			// Toast.makeText(context, result, 1).show();
			mAppClientDao.GetMarkById_Code(mHandler, a);
			mDialog.show();
		}
	}

	/**
	 * 路线规划
	 * 
	 * @param mark
	 */
	void Lines_Choose(List<MarkEntity> resList) {
		MarkEntity markinfo1 = resList.get(0);
		startLatitude = markinfo1.getMark_latitude();
		startLongtitude = markinfo1.getMark_longitude();
		MarkEntity markinfo2 = resList.get(1);
		endLatitude = markinfo2.getMark_latitude();
		endLongtitude = markinfo2.getMark_longitude();
		if (startLatitude == endLatitude || endLatitude == endLongtitude) {
			Toast.makeText(context, "起点不能相同！", 0).show();
		} else {
			if (BaiduNaviManager.isNaviInited()) {
				routeplanToNavi(CoordinateType.BD09LL);
			}
		}
	}

	private void addSerachOverLay(MarkEntity mark) {
		mBaiduMap.clear();
		LatLng latlng = null;
		Marker marker = null;
		OverlayOptions options;
		double mark_latitude = mark.mark_latitude;
		double mark_longitude = mark.mark_longitude;
		latlng = new LatLng(mark_latitude, mark_longitude);
		// 图锟斤拷
		options = new MarkerOptions().position(latlng).icon(mMarker).zIndex(5);
		marker = (Marker) mBaiduMap.addOverlay(options);
		Bundle arg0 = new Bundle();
		arg0.putSerializable("info", mark);
		marker.setExtraInfo(arg0);
		OverlayOptions textOption = new TextOptions().bgColor(0xAAFFFF00)
				.fontSize(24).fontColor(0xFFFF00FF).text(mark.getMarkName())
				.rotate(0).position(latlng);
		// 锟节碉拷图锟斤拷锟斤拷痈锟斤拷锟斤拷侄锟斤拷锟斤拷锟绞�
		mBaiduMap.addOverlay(textOption);
		MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latlng);
		mBaiduMap.setMapStatus(msu);
	}

	/**
	 * 扫一扫返回结果
	 * 
	 * @param mark
	 */
	private void addSerachOverLayPUP(MarkEntity mark) {
		mBaiduMap.clear();
		LatLng latlng = null;
		Marker marker = null;
		OverlayOptions options;
		double mark_latitude = mark.mark_latitude;
		double mark_longitude = mark.mark_longitude;
		endLatitude = mark_latitude;
		endLongtitude = mark_longitude;
		latlng = new LatLng(mark_latitude, mark_longitude);
		// 图锟斤拷
		options = new MarkerOptions().position(latlng).icon(mMarker).zIndex(5);
		marker = (Marker) mBaiduMap.addOverlay(options);
		Bundle arg0 = new Bundle();
		arg0.putSerializable("info", mark);
		marker.setExtraInfo(arg0);
		OverlayOptions textOption = new TextOptions().bgColor(0xAAFFFF00)
				.fontSize(24).fontColor(0xFFFF00FF).text(mark.getMarkName())
				.rotate(0).position(latlng);
		// 锟节碉拷图锟斤拷锟斤拷痈锟斤拷锟斤拷侄锟斤拷锟斤拷锟绞�
		mBaiduMap.addOverlay(textOption);
		MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latlng);
		mBaiduMap.setMapStatus(msu);
		LatLng p1 = new LatLng(mLatitude, mLongtitude);
		int distance = (int) DistanceUtil.getDistance(p1, latlng);// 计算距离
		pupwindow(mark, distance + "米", flag);
	}

	/**
	 * pupWindow弹框
	 * 
	 * @param pointmarker
	 * @param distance1
	 */
	void pupwindow(MarkEntity pointmarker, String distance1, int flag) {
		if (!isShowPup) {
			if (flag == 1)
				kbdpw = new KeyBoardPopupWindow(getActivity(), pointmarker,
						distance1, 1);
			else
				kbdpw = new KeyBoardPopupWindow(getActivity(), pointmarker,
						distance1, 0);
			kbdpw.setFocusable(false);
			kbdpw.setBackgroundDrawable(new BitmapDrawable());
			kbdpw.setOutsideTouchable(true);
			if (flag == 1)
				kbdpw.showAtLocation(
						getActivity().findViewById(R.id.fragment_map),
						Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0,
						0); // 设置layout在PopupWindow中显示的位置
			else
				kbdpw.showAtLocation(
						getActivity().findViewById(R.id.fragment_map),
						Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); // 设
			isShowPup = false;
			kbdpw.setOnKeyboard1PopListener(new Keyboard1PopListener() {
				@Override
				public void onTimeSelect(String calendar) {
					// TODO Auto-generated method stub
					if (calendar != null) {
						System.out.println("===>>>导航+++");
						if (BaiduNaviManager.isNaviInited()) {
							routeplanToNavi(CoordinateType.BD09LL);
						}
						if (BaiduNaviManager.isNaviInited()) {
							routeplanToNavi(CoordinateType.BD09LL);
						}
						if (kbdpw != null) {
							if (kbdpw.isShowing()) {
								kbdpw.dismiss();
								isShowPup = false;
							}
						}
					}
				}
			});
			kbdpw.setOnKeyboard1IsPupListener(new Keyboard1isPupListener() {

				@Override
				public void onIsPup(Boolean isPup) {
					// TODO Auto-generated method stub
					isShowPup = !isPup;
				}
			});

		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.image_saoyisao: {
			Intent intent = new Intent(context, CaptureActivity.class);
			startActivityForResult(intent, 0);
		}
			break;
		case R.id.image_zoom_add: {
			float zoomLevel = mBaiduMap.getMapStatus().zoom;
			if (zoomLevel <= 21) {
				mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomIn());
			} else
				Toast.makeText(getActivity(), "已经放大到最大啦!", 1).show();
		}
			break;
		case R.id.image_zoom_lose: {
			float zoomLevel = mBaiduMap.getMapStatus().zoom;
			if (zoomLevel > 4) {
				mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomOut());
			} else {
				Toast.makeText(getActivity(), "已经缩小到最小啦！", Toast.LENGTH_SHORT)
						.show();
			}
		}
			break;
		case R.id.image_backlocation: {
			centerToMyLocation();// 锟截碉拷锟斤拷前锟斤拷位
		}
			break;
		case R.id.button1: {
			Intent intent = new Intent(context, Lines_Choose_Activity.class);
			startActivityForResult(intent, 6);
		}
			break;
		}
	}

	// 锟斤拷位锟斤拷锟揭的碉拷址
	private void centerToMyLocation() {
		// 锟截碉拷锟揭碉拷位锟斤拷
		LatLng latLng = new LatLng(mLatitude, mLongtitude);
		MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
		mBaiduMap.animateMapStatus(msu);
	}

	/**
	 * 初始添加覆盖物
	 * 
	 */
	private void addMarkInfoOverLays(List<MarkEntity> mlist) {
		mBaiduMap.clear();
		LatLng latlng = null;
		OverlayOptions options;
		MarkEntity info;
		double latitude = 0;
		double longgitude = 0;
		if (mlist.size() != 0) {
			info = (MarkEntity) mlist.get(0);
			for (int i = 0; i < mlist.size(); i++) {
				info = (MarkEntity) mlist.get(i);
				if (((info.mark_latitude != 0) && (info.mark_longitude != 0))) {
					latitude = info.getMark_latitude();
					longgitude = info.getMark_longitude();
					// 锟斤拷纬锟斤拷
					latlng = new LatLng(latitude, longgitude);
					// 图锟斤拷
					options = new MarkerOptions().position(latlng)
							.icon(mMarker).zIndex(5);
					Marker marker = null;
					marker = (Marker) mBaiduMap.addOverlay(options);
					Bundle arg0 = new Bundle();
					arg0.putSerializable("info", info);
					marker.setExtraInfo(arg0);
					mLatitude = info.getMark_latitude();
					mLongtitude = info.getMark_longitude();
					// 锟斤拷锟斤拷锟斤拷锟斤拷Option锟斤拷锟斤拷锟斤拷锟斤拷锟节碉拷图锟斤拷锟斤拷锟斤拷锟斤拷锟�
					OverlayOptions textOption = new TextOptions()
							.bgColor(0xAAFFFF00).fontSize(24)
							.fontColor(0xFFFF00FF).text(info.getMarkName())
							.rotate(0).position(latlng);
					// 锟节碉拷图锟斤拷锟斤拷痈锟斤拷锟斤拷侄锟斤拷锟斤拷锟绞�
					mBaiduMap.addOverlay(textOption);
				}
				LatLng mlatlng = new LatLng(latitude, longgitude);
				MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latlng);
				mBaiduMap.setMapStatus(msu);
			}
		}

	}

	/**
	 * 添加覆盖物
	 * 
	 */
	private void addSerachMarkOverLays(List<MarkEntity> mlist) {
		mBaiduMap.clear();
		LatLng latlng = null;
		OverlayOptions options;
		MarkEntity info;
		double latitude = 0;
		double longgitude = 0;
		if (mlist.size() != 0) {
			info = (MarkEntity) mlist.get(0);
			for (int i = 0; i < mlist.size(); i++) {
				info = (MarkEntity) mlist.get(i);
				if (((info.mark_latitude != 0) && (info.mark_longitude != 0))) {
					latitude = info.getMark_latitude();
					longgitude = info.getMark_longitude();
					// 锟斤拷纬锟斤拷
					latlng = new LatLng(latitude, longgitude);
					// 图锟斤拷
					options = new MarkerOptions().position(latlng)
							.icon(mMarker).zIndex(5);
					Marker marker = null;
					marker = (Marker) mBaiduMap.addOverlay(options);
					Bundle arg0 = new Bundle();
					arg0.putSerializable("info", info);
					marker.setExtraInfo(arg0);
					mLatitude = info.getMark_latitude();
					mLongtitude = info.getMark_longitude();
					// 锟斤拷锟斤拷锟斤拷锟斤拷Option锟斤拷锟斤拷锟斤拷锟斤拷锟节碉拷图锟斤拷锟斤拷锟斤拷锟斤拷锟�
					OverlayOptions textOption = new TextOptions()
							.bgColor(0xAAFFFF00).fontSize(24)
							.fontColor(0xFFFF00FF).text(info.getMarkName())
							.rotate(0).position(latlng);
					// 锟节碉拷图锟斤拷锟斤拷痈锟斤拷锟斤拷侄锟斤拷锟斤拷锟绞�
					mBaiduMap.addOverlay(textOption);
					LatLng p1 = new LatLng(mLatitude, mLongtitude);
					int distance = (int) DistanceUtil.getDistance(p1, latlng);// 计算距离
					pupwindow(info, distance + "米", flag);
				}
				LatLng mlatlng = new LatLng(latitude, longgitude);
				MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latlng);
				mBaiduMap.setMapStatus(msu);
			}
		}

	}

	void initMap() {
		mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {
			@Override
			public boolean onMarkerClick(Marker marker) {
				Bundle extraInfo = marker.getExtraInfo();
				MarkEntity info = (MarkEntity) extraInfo
						.getSerializable("info");
				endLatitude = info.getMark_latitude();
				endLongtitude = info.getMark_longitude();
				LatLng p1 = new LatLng(mLatitude, mLongtitude);
				LatLng p2 = new LatLng(endLatitude, endLongtitude);
				int distance = (int) DistanceUtil.getDistance(p1, p2);// 计算距离
				pupwindow(info, distance + "米", 0);
				return true;

			}
		});
		mBaiduMap.setOnMapClickListener(new OnMapClickListener() {

			@Override
			public boolean onMapPoiClick(MapPoi arg0) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public void onMapClick(LatLng arg0) {
				// TODO Auto-generated method stub
				mBaiduMap.hideInfoWindow();
			}
		});
	}

	private void initMarkers() {
		mMarker = BitmapDescriptorFactory.fromResource(R.drawable.maker);
	}

	private void initLocation() {
		mLocationMode = LocationMode.Hight_Accuracy;
		mLocationClient = new LocationClient(getActivity()
				.getApplicationContext());
		listener = new MyLocationListener();
		mLocationClient.registerLocationListener(listener);
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(mLocationMode);
		option.setAddrType("all");
		option.setCoorType("bd09ll");
		option.setIsNeedAddress(true);
		option.setOpenGps(true);
		option.setIsNeedLocationDescribe(true);
		option.setIsNeedLocationPoiList(true);
		option.setScanSpan(3000);
		mLocationClient.setLocOption(option);
		mIconLocation = BitmapDescriptorFactory
				.fromResource(R.drawable.navi_map_gps_locked);
		myOrientationListener = new MyOrientationListener(context);
		myOrientationListener
				.setOnOrientationListener(new OnOrientationListener() {

					@Override
					public void onOrientationChanged(float x) {
						// TODO Auto-generated method stub
						mCurrentx = x;
					}
				});
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// 锟斤拷activity执锟斤拷onDestroy时执锟斤拷mMapView.onDestroy()锟斤拷实锟街碉拷图锟斤拷锟斤拷锟斤拷锟节癸拷锟斤拷
		mMapView.onDestroy();
	}

	@Override
	public void onResume() {
		super.onResume();
		// 锟斤拷activity执锟斤拷onResume时执锟斤拷mMapView. onResume
		// ()锟斤拷实锟街碉拷图锟斤拷锟斤拷锟斤拷锟节癸拷锟斤拷
		mMapView.onResume();
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		// 锟斤拷锟斤拷锟斤拷位
		mBaiduMap.setMyLocationEnabled(true);
		if (!mLocationClient.isStarted())
			mLocationClient.start();
		// 锟斤拷锟斤拷锟斤拷锟津传革拷锟斤拷
		myOrientationListener.start();
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		// 停止锟斤拷位
		mBaiduMap.setMyLocationEnabled(false);
		mLocationClient.stop();
		// 停止锟斤拷锟津传革拷锟斤拷
		myOrientationListener.stop();
	}

	@Override
	public void onPause() {
		super.onPause();
		// 锟斤拷activity执锟斤拷onPause时执锟斤拷mMapView. onPause
		// ()锟斤拷实锟街碉拷图锟斤拷锟斤拷锟斤拷锟节癸拷锟斤拷
		mMapView.onPause();
	}

	private class MyLocationListener implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			// 锟斤拷一锟轿讹拷位锟斤拷锟斤拷锟侥碉拷锟斤拷锟斤拷

			MyLocationData data = new MyLocationData.Builder()//
					.direction(mCurrentx).accuracy(location.getRadius())//
					.latitude(location.getLatitude())//
					.longitude(location.getLongitude())//
					.build();
			mBaiduMap.setMyLocationData(data);
			// 锟斤拷锟斤拷锟皆讹拷锟斤拷图锟斤拷
			MyLocationConfiguration config = new MyLocationConfiguration(null,
					true, mIconLocation);
			mBaiduMap.setMyLocationConfigeration(config);
			// 锟斤拷锟铰撅拷纬锟斤拷
			mLatitude = location.getLatitude();
			mLongtitude = location.getLongitude();
			startLatitude = location.getLatitude();
			startLongtitude = location.getLongitude();
			if (istrue) {
				LatLng latLng = new LatLng(location.getLatitude(),
						location.getLongitude());
				MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
				mBaiduMap.animateMapStatus(msu);
				istrue = false;
			}
			// initListener();
			if (initDirs()) {
				initNavi();
			}

		}
	}

	private boolean initDirs() {
		mSDCardPath = getSdcardDir();
		if (mSDCardPath == null) {
			return false;
		}
		File f = new File(mSDCardPath, APP_FOLDER_NAME);
		if (!f.exists()) {
			try {
				f.mkdir();
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}

	String authinfo = null;

	private void initNavi() {
		BaiduNaviManager.getInstance().init(getActivity(), mSDCardPath,
				APP_FOLDER_NAME, new NaviInitListener() {
					@Override
					public void onAuthResult(int status, String msg) {
						if (0 == status) {
							authinfo = "key验证成功";
						} else {
							authinfo = "key验证失败, " + msg;
						}
						getActivity().runOnUiThread(new Runnable() {
							@Override
							public void run() {
								// Toast.makeText(getActivity(), authinfo,
								// Toast.LENGTH_LONG).show();
							}
						});
					}

					public void initSuccess() {
					}

					public void initStart() {
					}

					public void initFailed() {
					}

				}, null/* null mTTSCallback */);
	}

	private String getSdcardDir() {
		if (Environment.getExternalStorageState().equalsIgnoreCase(
				Environment.MEDIA_MOUNTED)) {
			return Environment.getExternalStorageDirectory().toString();
		}
		return null;
	}

	private void routeplanToNavi(CoordinateType coType) {
		BNRoutePlanNode sNode = null;
		BNRoutePlanNode eNode = null;
		switch (coType) {
		case BD09LL: {
			if (endLatitude == 0 || endLongtitude == 0) {
				Toast.makeText(getActivity(), "请选择目的地！", Toast.LENGTH_SHORT)
						.show();
				return;
			} else {
				Toast.makeText(getActivity(), "请稍等,导航加载中..", Toast.LENGTH_SHORT)
						.show();
				sNode = new BNRoutePlanNode(startLongtitude, startLatitude,
						"起始点", null, coType);
				eNode = new BNRoutePlanNode(endLongtitude, endLatitude, "目的地",
						null, coType);
			}
			break;
		}
		default:
			;
		}
		if (sNode != null && eNode != null) {
			List<BNRoutePlanNode> list = new ArrayList<BNRoutePlanNode>();
			list.add(sNode);
			list.add(eNode);
			BaiduNaviManager.getInstance().launchNavigator(getActivity(), list,
					1, true, new DemoRoutePlanListener(sNode));
		}
	}

	public class DemoRoutePlanListener implements RoutePlanListener {

		private BNRoutePlanNode mBNRoutePlanNode = null;

		public DemoRoutePlanListener(BNRoutePlanNode node) {
			mBNRoutePlanNode = node;
		}

		@Override
		public void onJumpToNavigator() {
			for (Activity ac : activityList) {
				if (ac.getClass().getName().endsWith("BNDemoGuideActivity")) {
					return;
				}
			}
			Intent intent = new Intent(getActivity(), BNDemoGuideActivity.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable(ROUTE_PLAN_NODE,
					(BNRoutePlanNode) mBNRoutePlanNode);
			intent.putExtras(bundle);
			startActivity(intent);
		}

		@Override
		public void onRoutePlanFailed() {
			// TODO Auto-generated method stub
			Toast.makeText(getActivity(), "算路失败！", Toast.LENGTH_SHORT).show();
		}
	}

	private BNOuterTTSPlayerCallback mTTSCallback = new BNOuterTTSPlayerCallback() {

		@Override
		public void stopTTS() {
			// TODO Auto-generated method stub
			Log.e("test_TTS", "stopTTS");
		}

		@Override
		public void resumeTTS() {
			// TODO Auto-generated method stub
			Log.e("test_TTS", "resumeTTS");
		}

		@Override
		public void releaseTTSPlayer() {
			// TODO Auto-generated method stub
			Log.e("test_TTS", "releaseTTSPlayer");
		}

		@Override
		public int playTTSText(String speech, int bPreempt) {
			// TODO Auto-generated method stub
			Log.e("test_TTS", "playTTSText" + "_" + speech + "_" + bPreempt);

			return 1;
		}

		@Override
		public void phoneHangUp() {
			// TODO Auto-generated method stub
			Log.e("test_TTS", "phoneHangUp");
		}

		@Override
		public void phoneCalling() {
			// TODO Auto-generated method stub
			Log.e("test_TTS", "phoneCalling");
		}

		@Override
		public void pauseTTS() {
			// TODO Auto-generated method stub
			Log.e("test_TTS", "pauseTTS");
		}

		@Override
		public void initTTSPlayer() {
			// TODO Auto-generated method stub
			Log.e("test_TTS", "initTTSPlayer");
		}

		@Override
		public int getTTSState() {
			// TODO Auto-generated method stub
			Log.e("test_TTS", "getTTSState");
			return 1;
		}
	};

}
