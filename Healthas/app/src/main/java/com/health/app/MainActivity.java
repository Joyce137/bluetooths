package com.health.app;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.health.app.device.BindActivity;
import com.health.app.fragment.HealthFragment;
import com.health.app.fragment.LeftFragment;
import com.health.app.fragment.MonitorFragment;
import com.health.app.fragment.MsgFragment;
import com.health.app.fragment.SportFragment;
import com.health.app.sport.SportInfoActivity;
import com.health.app.statics.Users;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

public class MainActivity extends SlidingFragmentActivity {
	public Activity mActivity;
	MainApplication mainApp;
	ImageButton btnMore,btnSport;
	TextView textNavbarTitle;
	Button btnAttention ;
	FrameLayout frame_content;
	LinearLayout ly_monitor,ly_sport,ly_health,ly_msg;
	Button btnMonitorTab,btnSportTab,btnHealthTab,btnMsgTab;
	TextView tv_monitor,tv_sport,tv_health,tv_mag;

	private Fragment mContent;

	LinearLayout select_ll;
	Button select_btn;
	TextView select_text;

	int flag = 0;
	Fragment health,msg,sport,monitor;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mainApp = MainApplication.getInstance();
		initView();
		initSlidingMenu(savedInstanceState);

		setSelect(btnMonitorTab,tv_monitor);
		initFragment(0);
	}

	private void initView() {
		btnMore = (ImageButton) findViewById(R.id.btnMore);
		btnSport = (ImageButton) findViewById(R.id.btnSport);
		textNavbarTitle = (TextView) findViewById(R.id.textNavbarTitle);
//		textNavbarTitle.setText(Users.sLoginUsername);
		btnAttention = (Button) findViewById(R.id.btnAttention);
		frame_content = (FrameLayout) findViewById(R.id.frame_content);
//		include_bottom = (LinearLayout) findViewById(R.id.include_bottom);
		ly_monitor = (LinearLayout) findViewById(R.id.ly_monitor);
		ly_sport = (LinearLayout) findViewById(R.id.ly_sport);
		ly_health = (LinearLayout) findViewById(R.id.ly_health);
		ly_msg = (LinearLayout) findViewById(R.id.ly_msg);
		btnMonitorTab = (Button) findViewById(R.id.btnMonitorTab);
		btnSportTab = (Button) findViewById(R.id.btnSportTab);
		btnHealthTab = (Button) findViewById(R.id.btnHealthTab);
		btnMsgTab = (Button) findViewById(R.id.btnMsgTab);
		tv_monitor = (TextView) findViewById(R.id.tv_monitor);
		tv_sport = (TextView) findViewById(R.id.tv_sport);
		tv_health = (TextView) findViewById(R.id.tv_health);
		tv_mag = (TextView) findViewById(R.id.tv_mag);

		btnMore.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Log.e("btnMore", "onClick:toggle " );
				toggle();
			}
		});
		btnAttention.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
//				Intent intent = new Intent();
//				intent.setClass(getBaseContext(),ListActivity.class);
//				startActivity(intent);
				Intent intent = new Intent();
				intent.setClass(getBaseContext(),BindActivity.class);
				startActivity(intent);
			}
		});
		btnSport.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent();
				intent.setClass(getBaseContext(),SportInfoActivity.class);
				startActivity(intent);

			}
		});
		ly_monitor.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				setSelect(btnMonitorTab,tv_monitor);
				initFragment(0);
			}
		});
		btnMonitorTab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				setSelect(btnMonitorTab,tv_monitor);
				initFragment(0);
			}
		});
		ly_sport.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				setSelect(btnSportTab,tv_sport);
				initFragment(1);
			}
		});
		btnSportTab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				setSelect(btnSportTab,tv_sport);
				initFragment(1);
			}
		});
		ly_health.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				setSelect(btnHealthTab,tv_health);
				initFragment(2);
			}
		});
		btnHealthTab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				setSelect(btnHealthTab,tv_health);
				initFragment(2);
			}
		});
		ly_msg.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				setSelect(btnMsgTab,tv_mag);
				initFragment(3);
			}
		});
		btnMsgTab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				setSelect(btnMsgTab,tv_mag);
				initFragment(3);
			}
		});
	}
	/**
	 * 初始化侧边栏
	 */
	private void initSlidingMenu(Bundle savedInstanceState) {
		// 如果保存的状态不为空则得到之前保存的Fragment，否则实例化MyFragment
		if (savedInstanceState != null) {
//			mContent = getSupportFragmentManager().getFragment(
//					savedInstanceState, "mContent");
		}

		// 设置左侧滑动菜单
		setBehindContentView(R.layout.menu_frame_left);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.menu_frame, new LeftFragment()).commitAllowingStateLoss();

		// 实例化滑动菜单对象
		SlidingMenu sm = getSlidingMenu();
		// 设置可以左右滑动的菜单
		sm.setMode(SlidingMenu.LEFT);
		// 设置滑动阴影的宽度
		sm.setShadowWidthRes(R.dimen.shadow_width);
		// 设置滑动菜单阴影的图像资源
		sm.setShadowDrawable(null);
		// 设置滑动菜单视图的宽度
		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		// 设置渐入渐出效果的值
		sm.setFadeDegree(0.35f);
		// 设置触摸屏幕的模式,这里设置为全屏
		sm.setTouchModeAbove(SlidingMenu.LEFT);
		// 设置下方视图的在滚动时的缩放比例
		sm.setBehindScrollScale(0.0f);

	}
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		//	getSupportFragmentManager().putFragment(outState, "mContent", mContent);
	}

	public void setSelect(Button button,TextView textView){
		if (select_btn != null){
			select_btn.setSelected(false);
			select_text.setTextColor(getResources().getColor(R.color.tab_n));
		}
		select_btn  = button;
		select_text  = textView;
		select_btn.setSelected(true);
		select_text.setTextColor(getResources().getColor(R.color.tab_o));
	}
	private void initFragment(int index) {
		// 由于是引用了V4包下的Fragment，所以这里的管理器要用getSupportFragmentManager获取
		FragmentManager fragmentManager = getSupportFragmentManager();
		// 开启事务
		FragmentTransaction transaction = fragmentManager.beginTransaction();
//        // 隐藏所有Fragment
//        hideFragment(transaction);
//        if (fragmentManager.getFragments() != null && fragmentManager.getFragments().size() > 0) {
//            for (Fragment cf : fragmentManager.getFragments()) {
////                if(cf instanceof FileDealActivity
////                        || cf instanceof MyhealthActivity
////                        || cf instanceof SearchDoctor
////                        || cf instanceof SearchMedicine
////                        || cf instanceof MedicineList)
//                    transaction.hide(cf);
//            }
//        }

		switch (index) {
			//实时监测
			case 0:
				btnSport.setVisibility(View.GONE);
				monitor = new MonitorFragment();
				transaction.replace(R.id.frame_content, monitor);
				break;
			//运动
			case 1:
				btnSport.setVisibility(View.VISIBLE);
				sport = new SportFragment();
				transaction.replace(R.id.frame_content, sport);
				break;
			//健康
			case 2:
				btnSport.setVisibility(View.GONE);
				health = new HealthFragment();
				transaction.replace(R.id.frame_content, health);
				break;
			//消息
			case 3:
				btnSport.setVisibility(View.GONE);
				msg = new MsgFragment();
				transaction.replace(R.id.frame_content, msg);
				break;
			default:
				break;
		}

		// 提交事务
		transaction.commit();

	}
	public boolean onKeyDown(int keyCode, KeyEvent event) {// 捕捉返回键

		if (keyCode == KeyEvent.KEYCODE_BACK) {


			if (flag == 0) {
//				showMessage("再按一次离开乐活旅行");
				Toast.makeText(getBaseContext(),"再按一次退出应用",Toast.LENGTH_SHORT).show();
				flag = 2;
			} else if (flag == 2) {
				// sendBroadcast(Preferences.BROADCAST_ACTION.FINISH);
				finish();
			}

			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
