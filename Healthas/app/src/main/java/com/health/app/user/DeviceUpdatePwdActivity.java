package com.health.app.user;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.health.app.R;
import com.health.app.widget.FLActivity;


public class DeviceUpdatePwdActivity extends FLActivity {

	EditText editPwd;
	Button btnSub;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_user_update_pwd);
		navSetContentView(R.layout.activity_user_device_pwd);
		linkUiVar();
		bindListener();
		ensureUi();

	}

	@Override
	public void linkUiVar() {
		btnSub = (Button)findViewById(R.id.btnSub);
		editPwd = (EditText)findViewById(R.id.editPwd);
	}

	@Override
	public void bindListener() {

	}

	@Override
	public void ensureUi() {
		setNavbarTitleText("修改设备验证码");
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

}
