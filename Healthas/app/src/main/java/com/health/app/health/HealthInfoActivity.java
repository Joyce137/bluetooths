package com.health.app.health;

import android.os.Bundle;

import com.health.app.R;
import com.health.app.widget.FLActivity;


public class HealthInfoActivity extends FLActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_main);
		navSetContentView(R.layout.activity_health_info);
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
		setNavbarTitleText("个人基本信息");
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

}
