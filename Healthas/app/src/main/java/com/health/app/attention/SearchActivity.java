package com.health.app.attention;

import android.os.Bundle;

import com.health.app.R;
import com.health.app.widget.FLActivity;


public class SearchActivity extends FLActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_user_attention_search);
		navSetContentView(R.layout.activity_user_attention_search);
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
		setNavbarTitleText("搜索");
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

}
