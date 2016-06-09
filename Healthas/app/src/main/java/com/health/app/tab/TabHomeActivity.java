package com.health.app.tab;

import com.health.app.R;
import com.health.app.widget.FLActivity;

import android.os.Bundle;


public class TabHomeActivity extends FLActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

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

	}

	@Override
	protected void onResume() {
		super.onResume();
	}

}
