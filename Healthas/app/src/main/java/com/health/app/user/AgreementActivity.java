package com.health.app.user;

import android.os.Bundle;

import com.health.app.R;
import com.health.app.widget.FLActivity;


public class AgreementActivity extends FLActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_user_signup_agreement);
		navSetContentView(R.layout.activity_user_signup_agreement);
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
		setNavbarTitleText("注册协议");
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

}
