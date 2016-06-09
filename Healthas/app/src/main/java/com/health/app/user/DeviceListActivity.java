package com.health.app.user;

import android.os.Bundle;

import com.health.app.R;
import com.health.app.widget.FLActivity;


public class DeviceListActivity extends FLActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_user_update_pwd);
		navSetContentView(R.layout.activity_user_device_list);
		linkUiVar();
		bindListener();
		ensureUi();

	}

	@Override
	public void linkUiVar() {

	}

	@Override
	public void bindListener() {

	}

	@Override
	public void ensureUi() {
		setNavbarTitleText("我的设备");
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

}
