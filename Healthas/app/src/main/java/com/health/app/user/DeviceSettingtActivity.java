package com.health.app.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.health.app.R;
import com.health.app.widget.FLActivity;


public class DeviceSettingtActivity extends FLActivity {

	TextView textPwd;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_user_update_pwd);
		navSetContentView(R.layout.activity_user_device_setting);
		linkUiVar();
		bindListener();
		ensureUi();

	}

	@Override
	public void linkUiVar() {
		textPwd = (TextView) findViewById(R.id.textPwd);
	}

	@Override
	public void bindListener() {
		textPwd.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent6 = new Intent();
				intent6.setClass(mContext, DeviceUpdatePwdActivity.class);
				startActivity(intent6);
			}
		});
	}

	@Override
	public void ensureUi() {
		setNavbarTitleText("设备设置");
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

}
