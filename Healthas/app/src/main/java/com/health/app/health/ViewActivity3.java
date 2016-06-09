package com.health.app.health;

import android.os.Bundle;

import com.health.app.R;
import com.health.app.widget.FLActivity;


public class ViewActivity3 extends FLActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_health_height);
		navSetContentView(R.layout.activity_health_fat);
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
		setNavbarTitleText("血脂");
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

}
