package com.vc.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import common.LocalStorage;
import common.MyApplication;

public class Main extends FragmentActivity implements OnCheckedChangeListener,
		OnClickListener {

	private MyApplication application = (MyApplication) this.getApplication();

	private Fragment map_Fragment, find_Fragment, person_Fragment,
			currentFragment;

	private RadioGroup rg_tab;
	private RadioButton rb_mine;
	private String token;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_main);
		
		/*if(LocalStorage.getString(this,"userId") != ""){
			
		}*/
		
		int fragId = this.getIntent().getIntExtra("fragId", -1);
		init();
		System.out.println("===>>>fragId=" + fragId);
		if (fragId == 3)
			rb_mine.setChecked(true);
		
	}

	 
	private void init() {

		if (map_Fragment == null) {
			map_Fragment = new Fragment_Map();
		}

		if (!map_Fragment.isAdded()) {
			// 锟结交锟斤拷锟斤拷
			getSupportFragmentManager().beginTransaction()
					.add(R.id.line_fragment, map_Fragment).commit();

			// 锟斤拷录锟斤拷前Fragment
			currentFragment = map_Fragment;
		}
		rg_tab = (RadioGroup) findViewById(R.id.rg_tab);
		rb_mine = (RadioButton) findViewById(R.id.rb_mine);
		rg_tab.setOnCheckedChangeListener(this);

	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
		switch (checkedId) {
		case R.id.rb_map:
			if (map_Fragment == null) {
				map_Fragment = new Fragment_Map();
			}

			addOrShowFragment(getSupportFragmentManager().beginTransaction(),
					map_Fragment);
			break;
		case R.id.rb_find:
			if (find_Fragment == null) {
				find_Fragment = new Fragment_Find();
			}

			addOrShowFragment(getSupportFragmentManager().beginTransaction(),
					find_Fragment);
			break;
		case R.id.rb_mine:
			if (person_Fragment == null) {
				person_Fragment = new Fragment_Person();
			}

			addOrShowFragment(getSupportFragmentManager().beginTransaction(),
					person_Fragment);
			break;

		default:
			break;
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

	}

	private void addOrShowFragment(FragmentTransaction transaction,
			Fragment fragment) {
		if (currentFragment == fragment)
			return;

		if (!fragment.isAdded()) {
			// 锟斤拷锟角癴ragment未锟斤拷锟斤拷樱锟斤拷锟斤拷锟接碉拷Fragment锟斤拷锟斤拷锟斤拷锟斤拷
			transaction.hide(currentFragment).add(R.id.line_fragment, fragment)
					.commit();
		} else {
			transaction.hide(currentFragment).show(fragment).commit();
		}

		currentFragment = fragment;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			AlertDialog.Builder dialog = new AlertDialog.Builder(this);
			// dialog.setIcon(android.R.drawable.zoom_plate);
			dialog.setTitle("警告");
			dialog.setMessage("你确定要退出当前程序？");
			dialog.setPositiveButton("确定",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							finish();
						}
					});
			dialog.setNegativeButton("取消",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {

						}
					});
			dialog.show();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
