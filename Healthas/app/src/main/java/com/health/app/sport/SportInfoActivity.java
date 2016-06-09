package com.health.app.sport;

import android.os.Bundle;

import com.health.app.R;
import com.health.app.widget.FLActivity;


public class SportInfoActivity extends FLActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_main);
		navSetContentView(R.layout.activity_sport_info);
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
		setNavbarTitleText("运动处方");
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

}
