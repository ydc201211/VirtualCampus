package com.vc.ui;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import util.TitleBuilder;
import android.app.Activity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.util.Log;

import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;


import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.vc.adapter.AttendanceAdapter;
import com.vc.adapter.CommentAdapter;
import com.vc.adapter.FaceGVAdapter;
import com.vc.adapter.FaceVPAdapter;
import com.vc.adapter.LikeAdapter;

import common.Constants;
import common.LocalStorage;

import com.vc.api.OkHttpConnection;
import dto.ActionSimple;
import dto.AttendanceDetail;
import dto.CommentDetail;
import dto.LikeDetail;

public class Action_Detail_Activity extends Activity implements
		OnClickListener, OnCheckedChangeListener {

	// listView headerView详细信息
	private View action_detail_info;
	private TextView tv_theme;
	private TextView tv_host;
	private TextView tv_time;
	private TextView tv_site;
	private TextView tv_detail;
	private ImageView iv_actionPic;

	// shadow_tab
	private View shadow_action_detail_tab;
	private RadioGroup shadow_rg_action_detail;
	private RadioButton shadow_rb_attendances;
	private RadioButton shadow_rb_comments;
	private RadioButton shadow_rb_likes;

	// listView headerView
	private View action_detail_tab;
	private RadioGroup rg_action_detail;
	private RadioButton rb_attendances;
	private RadioButton rb_comments;
	private RadioButton rb_likes;

	// 
	private EditText write_comment_et;
	private TextView sure_comment_normal_tv;
	private TextView sure_comment_lighlight_tv;
	
	//表情viewPager
	private ViewPager mViewPager;
	//表情
	private LinearLayout chat_face_container;
	private ImageView emotion_iv;
	//静态表情列表
	private List<String> staticFacesList;
	private List<View> views = new ArrayList<View>();
	
	//表情的行列
	private int columns = 6;
	private int rows = 4;
	
	//表情布局中的小圆点
	private LinearLayout mDotsLayout;

	
	private JSONObject json;

	List<NameValuePair> params;
	private String userId;

	// listView -
	private PullToRefreshListView plv_action_detail;
	private ListView listView;
	
	/*private View footView;*/

	
	/*private ActionSimple action;*/
 
	/*private boolean scroll2Comment;*/
	
	
	private ActionSimple acDetail;
	
	
	private long pageNow = 1;
	private long pageCount = 0;
	private String url = "";
	

	public static final int INIT_C_DATA_SUCCESS = 0;
	public static final int INIT_C_DATA_FAIL = 1;
	public static final int REFRESH_C_COMPLETE = 2;
	public static final int REFRESH_C_FAIL = 3;
	public static final int LOADING_C_COMPLETE = 4;
	public static final int LOADING_C_FAIL = 5;

	public static final int ADDCOMMENT_SUCCESS = 6;
	public static final int ADDCOMMENT_FAIL = 7;

	public static final int INIT_A_DATA_SUCCESS = 8;
	public static final int INIT_A_DATA_FAIL = 9;
	public static final int LOADING_A_COMPLETE = 10;
	public static final int LOADING_A_FAIL = 11;
	public static final int REFRESH_A_COMPLETE = 12;
	public static final int REFRESH_A_FAIL = 13;

	public static final int INIT_L_DATA_SUCCESS = 14;
	public static final int INIT_L_DATA_FAIL = 15;
	public static final int REFRESH_L_COMPLETE = 16;
	public static final int REFRESH_L_FAIL = 17;
	public static final int LOADING_L_COMPLETE = 18;
	public static final int LOADING_L_FAIL = 19;
	public static final int GET_USER = 20;

	private LinkedList<CommentDetail> comments = new LinkedList<CommentDetail>();
	private LinkedList<AttendanceDetail> attendance = new LinkedList<AttendanceDetail>();
	private LinkedList<LikeDetail> likes = new LinkedList<LikeDetail>();
	private int type = 0;
	private CommentAdapter adapter;
	private AttendanceAdapter adapter1;
	private LikeAdapter adapter2;

	DisplayImageOptions options; 
	protected ImageLoader imageLoader;
	
	
	private int onLoadFlag = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_action__detail_);

		initView();

		setActionData();
		
		getData(type);

	}

	private void initView() {
		
		//初始化标题栏
		initTitle();
			
		//初始化编辑框
		initEdit();
		
		//初始化imageloader以及配置信息
		imageLoader = ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration
				.createDefault(Action_Detail_Activity.this));
		
		//初始化头部详细信息
		initDetailHead();
		
		//初始化评论、参与、赞tab
		initTab();
		
		//初始化listview
		initListView();
	}

	private void initTitle() {
		new TitleBuilder(this).setTitleText("活动详情")
				.setLeftImage(R.drawable.left).setLeftOnClickListener(this);
	}
	
	/**
	 * 初始化活动详细信息
	 */
	private void initDetailHead() {
		action_detail_info = View.inflate(this, R.layout.action_detail_item,
				null);
		action_detail_info.setBackgroundResource(R.color.white);

		iv_actionPic = (ImageView) action_detail_info
				.findViewById(R.id.ac_detail_pic_Iv);
		tv_theme = (TextView) action_detail_info
				.findViewById(R.id.tv_detail_theme);
		tv_host = (TextView) action_detail_info
				.findViewById(R.id.tv_detail_host);
		tv_detail = (TextView) action_detail_info
				.findViewById(R.id.tv_detail_detail);
		tv_site = (TextView) action_detail_info
				.findViewById(R.id.tv_detail_site);
		tv_time = (TextView) action_detail_info
				.findViewById(R.id.tv_detail_time);
		iv_actionPic.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent  = new Intent(Action_Detail_Activity.this,Activity_BigPicture.class);
				intent.putExtra("picURL",acDetail.getAction_picPath());
				intent.putExtra("picType","action");
				startActivity(intent);
				
			}
		});

	}

	// ����
	private void initTab() {
		// shadow
		shadow_action_detail_tab = findViewById(R.id.action_detail_tab);
		shadow_rg_action_detail = (RadioGroup) shadow_action_detail_tab
				.findViewById(R.id.rg_action_detail);
		shadow_rb_attendances = (RadioButton) shadow_action_detail_tab
				.findViewById(R.id.detail_rb_join);
		shadow_rb_comments = (RadioButton) shadow_action_detail_tab
				.findViewById(R.id.detail_rb_comments);
		shadow_rb_likes = (RadioButton) shadow_action_detail_tab
				.findViewById(R.id.detail_rb_likes);
		shadow_rg_action_detail.setOnCheckedChangeListener(this);

		// header
		action_detail_tab = View
				.inflate(this, R.layout.action_detail_tab, null);
		rg_action_detail = (RadioGroup) action_detail_tab
				.findViewById(R.id.rg_action_detail);
		rb_attendances = (RadioButton) action_detail_tab
				.findViewById(R.id.detail_rb_join);
		rb_comments = (RadioButton) action_detail_tab
				.findViewById(R.id.detail_rb_comments);
		rb_likes = (RadioButton) action_detail_tab
				.findViewById(R.id.detail_rb_likes);
		rg_action_detail.setOnCheckedChangeListener(this);
	}

	
	private void initEdit(){
		write_comment_et =  (EditText) findViewById(R.id.write_comment_et);
		sure_comment_normal_tv = (TextView) findViewById(R.id.sure_comment_normal_tv);
		sure_comment_lighlight_tv = (TextView) findViewById(R.id.sure_comment_highlight_tv);

		

		//表情图标
		emotion_iv = (ImageView) findViewById(R.id.emotion_iv);
		
		//表情底部小圆点
		mDotsLayout = (LinearLayout) findViewById(R.id.face_dots_container);
		
		//装载表情的viewpager
		mViewPager = (ViewPager) findViewById(R.id.face_viewpager);
		
		//初始化表情页、表情图标、评论输入框
		mViewPager.setOnPageChangeListener(new PageChange());
		emotion_iv.setOnClickListener(this);
		write_comment_et.setOnClickListener(this);
		
		//设置发送按钮监听
		sure_comment_normal_tv.setOnClickListener(this);
		sure_comment_lighlight_tv.setOnClickListener(this);
		
		chat_face_container = (LinearLayout) findViewById(R.id.chat_face_container);
		
		//初始化表情列表
		initStaticFaces();
		//初始化表情
		initViewPager();
		
		//初始化评论编辑框
		write_comment_et.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				if (start != 0) {

					sure_comment_lighlight_tv.setVisibility(View.VISIBLE);
					sure_comment_normal_tv.setVisibility(View.GONE);
				} else {
					sure_comment_lighlight_tv.setVisibility(View.GONE);
					sure_comment_normal_tv.setVisibility(View.VISIBLE);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				if(after == 0 && start == 1 ){
					sure_comment_lighlight_tv.setVisibility(View.GONE);
					sure_comment_normal_tv.setVisibility(View.VISIBLE);
				}

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				if(s.length() == 0){
					sure_comment_lighlight_tv.setVisibility(View.GONE);
					sure_comment_normal_tv.setVisibility(View.VISIBLE);
				}else{
					sure_comment_lighlight_tv.setVisibility(View.VISIBLE);
					sure_comment_normal_tv.setVisibility(View.GONE);
				}

			}
		});
	}
	
	private void initListView() {

		// listView
		adapter = new CommentAdapter(this, R.layout.comment_item, comments);
		adapter1 = new AttendanceAdapter(this, R.layout.attendance_item,attendance);
		adapter2 = new LikeAdapter(this, R.layout.like_item, likes);

		plv_action_detail = (PullToRefreshListView) findViewById(R.id.plv_action_detail);

		ILoadingLayout startLabels = plv_action_detail.getLoadingLayoutProxy();
		startLabels.setPullLabel("下拉刷新");
		startLabels.setRefreshingLabel("正在刷新...");
		startLabels.setReleaseLabel("释放");

		plv_action_detail.setMode(Mode.PULL_FROM_START);
		listView = plv_action_detail.getRefreshableView();

		listView.addHeaderView(action_detail_info);
		listView.addHeaderView(action_detail_tab);

		plv_action_detail.setAdapter(adapter);

		//

		plv_action_detail.setOnRefreshListener(new OnRefreshListener<ListView>() {

					@Override
					public void onRefresh(PullToRefreshBase<ListView> refreshView) {
						// TODO Auto-generated method stub
						onLoadFlag = 0;
						if (type == 0) {
							
							pageNow = 1;
							getDataFromServer(REFRESH_C_COMPLETE,
									REFRESH_C_FAIL);
						} else if (type == 1) {
							
							pageNow = 1;
							getDataFromServer(REFRESH_A_COMPLETE,
									REFRESH_A_FAIL);
						} else {
							
							pageNow = 1;
							
							getDataFromServer(REFRESH_L_COMPLETE,
									REFRESH_L_FAIL);
						}

					}

				});

		plv_action_detail.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {

					@Override
					public void onLastItemVisible() {
						// TODO Auto-generated method stub
						if (pageNow < pageCount) {
							pageNow++;
							onLoadFlag = 1;
							if (type == 0) {
								getDataFromServer(LOADING_C_COMPLETE,
										LOADING_C_FAIL);
							} else if (type == 1) {
								getDataFromServer(LOADING_A_COMPLETE,
										LOADING_A_FAIL);
							} else {
								getDataFromServer(LOADING_L_COMPLETE,
										LOADING_L_FAIL);
							}
						} else {
							Toast.makeText(Action_Detail_Activity.this,
									"已经到底啦", Toast.LENGTH_SHORT).show();
						}

					}

				});
		
		// ����״̬����
		plv_action_detail.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// 0-pullHead 1-detailHead 2-tab
				// 
				shadow_action_detail_tab.setVisibility(
						firstVisibleItem >= 2 ? View.VISIBLE: View.GONE);
			}
		});

	}

	/**
	 * 初始化表情列表staticFacesList
	 */
	private void initStaticFaces() {
		try {
			staticFacesList = new ArrayList<String>();
			String[] faces = getAssets().list("face/png");
			//将Assets中的表情名称转为字符串一一添加进staticFacesList
			for (int i = 0; i < faces.length; i++) {
				staticFacesList.add(faces[i]);
			}
			//去掉删除图片
			staticFacesList.remove("emotion_del_normal.png");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/*
	 * 初始表情 
	 */
	private void initViewPager(){
		// 获取页数
		for (int i = 0; i < getPagerCount(); i++) {
			views.add(viewPagerItem(i));
			LayoutParams params = new LayoutParams(16, 16);
			mDotsLayout.addView(dotsItem(i), params);
		}
		FaceVPAdapter mVpAdapter = new FaceVPAdapter(views);
		mViewPager.setAdapter(mVpAdapter);
		mDotsLayout.getChildAt(0).setSelected(true);
	}
	

	/**
	 * 表情页改变时，dots效果也要跟着改变
	 * */
	class PageChange implements OnPageChangeListener {
		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}
		@Override
		public void onPageSelected(int arg0) {
			for (int i = 0; i < mDotsLayout.getChildCount(); i++) {
				mDotsLayout.getChildAt(i).setSelected(false);
			}
			mDotsLayout.getChildAt(arg0).setSelected(true);
		}

	}
	
	private View viewPagerItem(int position) {
		LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.face_gridview, null);//表情布局
		GridView gridview = (GridView) layout.findViewById(R.id.chart_face_gv);
		/**
		 * 注：因为每一页末尾都有一个删除图标，所以每一页的实际表情columns *　rows　－　1; 空出最后一个位置给删除图标
		 * */
		List<String> subList = new ArrayList<String>();
		subList.addAll(staticFacesList
				.subList(position * (columns * rows - 1),
						(columns * rows - 1) * (position + 1) > staticFacesList
								.size() ? staticFacesList.size() : (columns
								* rows - 1)
								* (position + 1)));
		/**
		 * 末尾添加删除图标
		 * */
		subList.add("emotion_del_normal.png");
		FaceGVAdapter mGvAdapter = new FaceGVAdapter(subList, this);
		gridview.setAdapter(mGvAdapter);
		gridview.setNumColumns(columns);
		// 单击表情执行的操作
		gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				try {
					String png = ((TextView) ((LinearLayout) view).getChildAt(1)).getText().toString();
					if (!png.contains("emotion_del_normal")) {// 如果不是删除图标
						insert(getFace(png));
					} else {
						delete();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		return gridview;
	}
	
	
	private SpannableStringBuilder getFace(String png) {
		SpannableStringBuilder sb = new SpannableStringBuilder();
		try {
			/**
			 * 经过测试，虽然这里tempText被替换为png显示，但是但我单击发送按钮时，获取到輸入框的内容是tempText的值而不是png
			 * 所以这里对这个tempText值做特殊处理
			 * 格式：#[face/png/f_static_000.png]#，以方便判斷當前圖片是哪一個
			 * */
			String tempText = "#[" + png + "]#";
			sb.append(tempText);
			sb.setSpan(new ImageSpan(Action_Detail_Activity.this, 
					BitmapFactory.decodeStream(getAssets().open(png))), 
					sb.length() - tempText.length(), sb.length(),
					Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return sb;
	}

	/**
	 * 向输入框里添加表情
	 * */
	private void insert(CharSequence text) {
		int iCursorStart = Selection.getSelectionStart((write_comment_et.getText()));
		int iCursorEnd = Selection.getSelectionEnd((write_comment_et.getText()));
		if (iCursorStart != iCursorEnd) {
			((Editable) write_comment_et.getText()).replace(iCursorStart, iCursorEnd, "");
		}
		int iCursor = Selection.getSelectionEnd((write_comment_et.getText()));
		((Editable) write_comment_et.getText()).insert(iCursor, text);
	}

	/**
	 * 删除图标执行事件
	 * 注：如果删除的是表情，在删除时实际删除的是tempText即图片占位的字符串，所以必需一次性删除掉tempText，才能将图片删除
	 * */
	private void delete() {
		if (write_comment_et.getText().length() != 0) {
			int iCursorEnd = Selection.getSelectionEnd(write_comment_et.getText());
			int iCursorStart = Selection.getSelectionStart(write_comment_et.getText());
			if (iCursorEnd > 0) {
				if (iCursorEnd == iCursorStart) {
					if (isDeletePng(iCursorEnd)) {
						String st = "#[face/png/f_static_000.png]#";
						((Editable) write_comment_et.getText()).delete(
								iCursorEnd - st.length(), iCursorEnd);
					} else {
						((Editable) write_comment_et.getText()).delete(iCursorEnd - 1,
								iCursorEnd);
					}
				} else {
					((Editable) write_comment_et.getText()).delete(iCursorStart,
							iCursorEnd);
				}
			}
		}
	}

	/**
	 * 判断即将删除的字符串是否是图片占位字符串tempText 如果是：则讲删除整个tempText
	 * **/
	private boolean isDeletePng(int cursor) {
		String st = "#[face/png/f_static_000.png]#";
		String content = write_comment_et.getText().toString().substring(0, cursor);
		if (content.length() >= st.length()) {
			String checkStr = content.substring(content.length() - st.length(),
					content.length());
			String regex = "(\\#\\[face/png/f_static_)\\d{3}(.png\\]\\#)";
			Pattern p = Pattern.compile(regex);
			Matcher m = p.matcher(checkStr);
			return m.matches();
		}
		return false;
	}
	
	private ImageView dotsItem(int position) {
		LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.dot_image, null);
		ImageView iv = (ImageView) layout.findViewById(R.id.face_dot);
		iv.setId(position);
		return iv;
	}
	/**
	 * 根据表情数量以及GridView设置的行数和列数计算Pager数量
	 * @return
	 */
	private int getPagerCount() {
		int count = staticFacesList.size();
		return count % (columns * rows - 1) == 0 ? count / (columns * rows - 1)
				: count / (columns * rows - 1) + 1;
	}
	
	//隐藏软键盘
	public void hideSoftInputView() {
		InputMethodManager manager = ((InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE));
		if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
			if (getCurrentFocus() != null)
				manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}
		
	private void setActionData() {
		Intent intent = getIntent();

		
		acDetail = (ActionSimple) intent.getExtras().get("acDetail");

		tv_theme.setText(acDetail.getAction_theme());
		tv_host.setText(acDetail.getAction_host());
		tv_site.setText(acDetail.getAction_site());
		tv_time.setText(acDetail.getAction_time());
		tv_detail.setText(acDetail.getAction_detail());

		
		loadPicture();
	}

	// 
	private void loadPicture() {

		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.icon_onloading)
				.showImageForEmptyUri(R.drawable.ic_launcher) 
				.showImageOnFail(R.drawable.icon_loadfail) 
				.cacheInMemory(true) 
				.cacheOnDisk(true) 
				.displayer(new RoundedBitmapDisplayer(2))
				.build();

		imageLoader.displayImage(Constants.SERVERADDRESS
				+ "/file/image/get?imageUrl=" + acDetail.getAction_picPath(),
				iv_actionPic, options);

	}

	protected Handler pHandler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {

			// ����
			case INIT_C_DATA_SUCCESS:
				if (plv_action_detail != null) {
					jsonToObj(0);
					plv_action_detail.onRefreshComplete();
					adapter.notifyDataSetChanged();
					
				}

				break;
			case INIT_C_DATA_FAIL:
				Toast.makeText(Action_Detail_Activity.this, "请求数据失败",
						Toast.LENGTH_SHORT).show();
				break;

			case REFRESH_C_COMPLETE:
				if (plv_action_detail != null) {
					jsonToObj(0);
					plv_action_detail.onRefreshComplete();
					adapter.notifyDataSetChanged();
				
				}
				break;

			case LOADING_C_COMPLETE:
				if (plv_action_detail != null) {
					jsonToObj(0);
					adapter.notifyDataSetChanged();
				}
				break;
			case LOADING_C_FAIL:
				Toast.makeText(Action_Detail_Activity.this, "请求数据失败",
						Toast.LENGTH_SHORT).show();
				break;

			case REFRESH_C_FAIL:
				Toast.makeText(Action_Detail_Activity.this, "请求数据失败",
						Toast.LENGTH_SHORT).show();
				break;

			// ��������
			case ADDCOMMENT_SUCCESS:
				pageNow = 1;
				comments.clear();
				if (adapter != null) {
					adapter.notifyDataSetChanged();
				}
				getData(0);
				break;
			case ADDCOMMENT_FAIL:
				Toast.makeText(Action_Detail_Activity.this, "添加数据失败",
						Toast.LENGTH_SHORT).show();
				break;

			
				
			case INIT_A_DATA_SUCCESS:
				if (plv_action_detail != null) {
					jsonToObj(1);
					Toast.makeText(Action_Detail_Activity.this, "初始化数据成功",
							Toast.LENGTH_SHORT).show();
					adapter1.notifyDataSetChanged();
					
				}

				break;
			case INIT_A_DATA_FAIL:
				Toast.makeText(Action_Detail_Activity.this, "初始化数据失败",
						Toast.LENGTH_SHORT).show();
				break;
			case REFRESH_A_COMPLETE:
				if (plv_action_detail != null) {
					Toast.makeText(Action_Detail_Activity.this, "刷新成功",
							Toast.LENGTH_SHORT).show();
					jsonToObj(1);
					plv_action_detail.onRefreshComplete();
					adapter1.notifyDataSetChanged();
					
				}
				break;
			case REFRESH_A_FAIL:
				Toast.makeText(Action_Detail_Activity.this, "刷新失败",
						Toast.LENGTH_SHORT).show();
				break;
			case LOADING_A_COMPLETE:
				if (plv_action_detail != null) {
					
					Toast.makeText(Action_Detail_Activity.this, "加载更多成功",
							Toast.LENGTH_SHORT).show();
					jsonToObj(1);
					
					adapter1.notifyDataSetChanged();

				}
				break;
			case LOADING_A_FAIL:
				Toast.makeText(Action_Detail_Activity.this, "加载更多失败",
						Toast.LENGTH_SHORT).show();
				break;

			// ��
			case INIT_L_DATA_SUCCESS:
				if (plv_action_detail != null) {
					
					Toast.makeText(Action_Detail_Activity.this, "初始化成功",
							Toast.LENGTH_SHORT).show();
					jsonToObj(2);

					adapter2.notifyDataSetChanged();

				}

				break;
			case INIT_L_DATA_FAIL:
				Toast.makeText(Action_Detail_Activity.this, "初始化数据失败",
						Toast.LENGTH_SHORT).show();
				break;
			case REFRESH_L_COMPLETE:
				if (plv_action_detail != null) {
					Toast.makeText(Action_Detail_Activity.this, "刷新数据成功",
							Toast.LENGTH_SHORT).show();
					jsonToObj(2);
					plv_action_detail.onRefreshComplete();
					adapter2.notifyDataSetChanged();

				}
				break;
			case REFRESH_L_FAIL:
				Toast.makeText(Action_Detail_Activity.this, "刷新数据失败",
						Toast.LENGTH_SHORT).show();
				break;
			case LOADING_L_COMPLETE:
				if (plv_action_detail != null) {
					Toast.makeText(Action_Detail_Activity.this, "加载更多成功",
							Toast.LENGTH_SHORT).show();
					jsonToObj(2);
					adapter2.notifyDataSetChanged();
				}
				break;
			case LOADING_L_FAIL:
				Toast.makeText(Action_Detail_Activity.this, "加载更多失败",
						Toast.LENGTH_SHORT).show();
				break;
			

			}
		}
	};

	
	
	
	private void getData(int type) {
		if (type == 0) {
			params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("pageNow", String
					.valueOf(pageNow)));
			params.add(new BasicNameValuePair("activityId", String
					.valueOf(acDetail.getAction_id())));
			url = "/comment/getcomment";
			getDataFromServer(INIT_C_DATA_SUCCESS, INIT_C_DATA_FAIL);
		} else if (type == 1) {
			params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("pageNow", String
					.valueOf(pageNow)));
			params.add(new BasicNameValuePair("activityId", String
					.valueOf(acDetail.getAction_id())));
			url = "/attendance/getAttendanceByActivityId";
			getDataFromServer(INIT_A_DATA_SUCCESS, INIT_A_DATA_FAIL);
		} else {
			params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("pageNow", String
					.valueOf(pageNow)));
			params.add(new BasicNameValuePair("activityId", String
					.valueOf(acDetail.getAction_id())));
			url = "/like/getLikeByActivityId";
			getDataFromServer(INIT_L_DATA_SUCCESS, INIT_L_DATA_FAIL);
		}
	}

	private void getDataFromServer(final int a, final int b) {
		new Thread() {
			@Override
			public void run() {
				Looper.prepare();
				json = OkHttpConnection.execute(Action_Detail_Activity.this,
						url, params);
				
				try {
					if (json == null) {
						pHandler.sendMessageDelayed(pHandler.obtainMessage(b),
								3000);
					} else {
						String resultCode = json.getString("resultCode");

						if (resultCode == "000") {
							pHandler.sendMessageDelayed(
									pHandler.obtainMessage(b), 3000);
						} else {
							pHandler.sendMessageDelayed(
									pHandler.obtainMessage(a), 3000);
						}
					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				// ִ�������߳���
				Looper.loop();
			}

		}.start();
	}

	private void jsonToObj(int type) {
		try {
			if (json == null) {
				Toast.makeText(Action_Detail_Activity.this, "网络连接异常",
						Toast.LENGTH_SHORT).show();
			} else {

				pageCount = json.getInt("pageCount");

				JSONArray items = json.getJSONArray("results");
				if (type == 0) {
					if(onLoadFlag == 1){
						for (int i = 0; i < items.length(); i++) {
							CommentDetail ac_comment = new CommentDetail();
							ac_comment.setComment_id(Integer.parseInt(items
									.getJSONObject(i).getString("comment_id")));
							ac_comment.setComment_content(items.getJSONObject(i)
									.getString("comment_content"));
							ac_comment.setComment_time(items.getJSONObject(i)
									.getString("comment_time"));
							ac_comment.setComment_userId(Integer.parseInt(items
									.getJSONObject(i).getString("comment_userId")));
							
							ac_comment.setComment_activityId(Integer.parseInt(items
									.getJSONObject(i).getString(
											"comment_activityId")));
							
							ac_comment.setComment_user_nickName(items
									.getJSONObject(i).getString("comment_user_nickName"));
							
							ac_comment.setComment_user_picPath(items
									.getJSONObject(i).getString("comment_user_picPath"));
							
							addList(ac_comment,0);
						}
					}else{
						for (int i = items.length()- 1; i >= 0; i--) {
							CommentDetail ac_comment = new CommentDetail();
							ac_comment.setComment_id(Integer.parseInt(items
									.getJSONObject(i).getString("comment_id")));
							ac_comment.setComment_content(items.getJSONObject(i)
									.getString("comment_content"));
							ac_comment.setComment_time(items.getJSONObject(i)
									.getString("comment_time"));
							ac_comment.setComment_userId(Integer.parseInt(items
									.getJSONObject(i).getString("comment_userId")));
							ac_comment.setComment_activityId(Integer.parseInt(items
									.getJSONObject(i).getString(
											"comment_activityId")));
							
							ac_comment.setComment_user_nickName(items
									.getJSONObject(i).getString("comment_user_nickName"));
							
							ac_comment.setComment_user_picPath(items
									.getJSONObject(i).getString("comment_user_picPath"));
							
							addList(ac_comment,0);
						}
					}
					
				} else if (type == 1) {
					if(onLoadFlag == 1){
						for (int i = 0; i < items.length(); i++) {
							AttendanceDetail ac_attendance = new AttendanceDetail();
							ac_attendance.setAttendance_id(Integer.parseInt(items
									.getJSONObject(i).getString("attendance_id")));
							ac_attendance.setAttendance_time(items.getJSONObject(i)
									.getString("attendance_time"));
							ac_attendance.setAttendance_userId(Integer
									.parseInt(items.getJSONObject(i).getString(
											"attendance_userId")));
							ac_attendance.setAttendance_activityId(Integer
									.parseInt(items.getJSONObject(i).getString(
											"attendance_activityId")));
							
							ac_attendance.setAttendance_user_nickName(items
									.getJSONObject(i).getString("attendance_user_nickName"));
							
							ac_attendance.setAttendacne_user_picPath(items
									.getJSONObject(i).getString("attendance_user_picPath"));
							Log.i("jdjasjd",ac_attendance.getAttendance_user_nickName());
							addList(ac_attendance,1);
						}
					}else{
						for (int i = items.length()- 1; i >= 0; i--) {
							AttendanceDetail ac_attendance = new AttendanceDetail();
							ac_attendance.setAttendance_id(Integer.parseInt(items
									.getJSONObject(i).getString("attendance_id")));
							ac_attendance.setAttendance_time(items.getJSONObject(i)
									.getString("attendance_time"));
							ac_attendance.setAttendance_userId(Integer
									.parseInt(items.getJSONObject(i).getString(
											"attendance_userId")));
							ac_attendance.setAttendance_activityId(Integer
									.parseInt(items.getJSONObject(i).getString(
											"attendance_activityId")));
							ac_attendance.setAttendance_user_nickName(items
									.getJSONObject(i).getString("attendance_user_nickName"));
							
							ac_attendance.setAttendacne_user_picPath(items
									.getJSONObject(i).getString("attendance_user_picPath"));
							addList(ac_attendance,1);
						}
					}
					
				} else {
					if(onLoadFlag == 1){
						for (int i = 0; i < items.length(); i++) {
							LikeDetail ac_like = new LikeDetail();
							ac_like.setLike_id(Integer.parseInt(items
									.getJSONObject(i).getString("like_id")));
							ac_like.setLike_time(items.getJSONObject(i).getString(
									"like_time"));
							ac_like.setLike_userId(Integer.parseInt(items
									.getJSONObject(i).getString("like_userId")));
							ac_like.setLike_activityId(Integer.parseInt(items
									.getJSONObject(i).getString("like_activityId")));
							
							ac_like.setLike_user_nickName(items
									.getJSONObject(i).getString("like_user_nickName"));
							ac_like.setLike_user_picPath(items
									.getJSONObject(i).getString("like_user_picPath"));
							
							addList(ac_like,2);
						}
					}else{
						for (int i = items.length()- 1; i >= 0; i--) {
							LikeDetail ac_like = new LikeDetail();
							ac_like.setLike_id(Integer.parseInt(items
									.getJSONObject(i).getString("like_id")));
							ac_like.setLike_time(items.getJSONObject(i).getString(
									"like_time"));
							ac_like.setLike_userId(Integer.parseInt(items
									.getJSONObject(i).getString("like_userId")));
							ac_like.setLike_activityId(Integer.parseInt(items
									.getJSONObject(i).getString("like_activityId")));
							ac_like.setLike_user_nickName(items
									.getJSONObject(i).getString("like_user_nickName"));
							ac_like.setLike_user_picPath(items
									.getJSONObject(i).getString("like_user_picPath"));
							
							addList(ac_like,2);
						}
					}
				}
			}
		} catch (JSONException e) {

			e.printStackTrace();
		}

	}
	
	private void addList(Object o,int type){
		if(type == 0){
			CommentDetail c = (CommentDetail)o;
			if(comments.size() == 0){
				comments.add(c);
				
			}else{
				for(int i = 0;i< comments.size();i++){
					if(comments.get(i).getComment_id() == c.getComment_id())
						return;
				}
					
				if(onLoadFlag == 0){
					comments.addFirst(c);
				}else{
					comments.addLast(c);
				}
				
			}
		}else if(type == 1){
			AttendanceDetail a = (AttendanceDetail)o;
			if(attendance.size() == 0){
				attendance.add(a);
				
			}else{
				for(int i = 0;i< attendance.size();i++){
					if(attendance.get(i).getAttendance_id() == a.getAttendance_id())
						return;
				}
					
				if(onLoadFlag == 0){
					attendance.addFirst(a);
				}else{
					attendance.addLast(a);
				}
				
			}
		}else{
			LikeDetail l = (LikeDetail)o;
			if(likes.size() == 0){
				likes.add(l);
				
			}else{
				for(int i = 0;i< likes.size();i++){
					if(likes.get(i).getLike_id() == l.getLike_id())
						return;
				}
					
				if(onLoadFlag == 0){
					likes.addFirst(l);
				}else{
					likes.addLast(l);
				}
				
			}
		}
	}
	
	

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent;
		

		switch (v.getId()) {
		case R.id.titlebar_iv_left:
			intent = new Intent();
			intent.putExtra("back", "return from action_detail_activity");
			setResult(RESULT_CANCELED, intent);

			finish();
			break;
		case R.id.sure_comment_highlight_tv:
			userId = LocalStorage.getString(Action_Detail_Activity.this,
					"userId");
			if (userId == null || userId == "") {
				Toast.makeText(Action_Detail_Activity.this, "请先登录",
						Toast.LENGTH_SHORT).show();
			} else {
				params = new ArrayList<NameValuePair>();
				String commentContent = write_comment_et.getText().toString();

				params.add(new BasicNameValuePair("comment_content",
						commentContent));
				params.add(new BasicNameValuePair("comment_activityId",
						acDetail.getAction_id() + ""));
				params.add(new BasicNameValuePair("comment_userId", userId));
				params.add(new BasicNameValuePair("comment_time",
						"1970.1.1 00:00:00"));
				params.add(new BasicNameValuePair("comment_ip", "0.0.0.0"));
				url = "/comment/addComment";

				write_comment_et.setText("");
				sure_comment_normal_tv.setVisibility(View.VISIBLE);
				sure_comment_lighlight_tv.setVisibility(View.GONE);
				getDataFromServer(ADDCOMMENT_SUCCESS, ADDCOMMENT_FAIL);
			}
			break;
			
		case R.id.emotion_iv:
			hideSoftInputView();//隐藏软键盘
			if(chat_face_container.getVisibility()==View.GONE){
				chat_face_container.setVisibility(View.VISIBLE);
			}else{
				chat_face_container.setVisibility(View.GONE);
			}
			break;
			
		case R.id.write_comment_et:
			if(chat_face_container.getVisibility()==View.VISIBLE){
				chat_face_container.setVisibility(View.GONE);
			}
			break;
		default:
			break;
		}
	}


	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
		switch (checkedId) {
		case R.id.detail_rb_comments:
			pageNow = 1;
		
			shadow_rb_comments.setChecked(true);
			rb_comments.setChecked(true);

			plv_action_detail.setAdapter(adapter);

			getData(0);
			break;
		case R.id.detail_rb_join:
			pageNow = 1;
			attendance.clear();

			shadow_rb_attendances.setChecked(true);
			rb_attendances.setChecked(true);

			plv_action_detail.setAdapter(adapter1);
			getData(1);
			break;
		case R.id.detail_rb_likes:
			pageNow = 1;
			likes.clear();
			

			shadow_rb_likes.setChecked(true);
			rb_likes.setChecked(true);

			plv_action_detail.setAdapter(adapter2);
			getData(2);
			break;
		}
	}
}
