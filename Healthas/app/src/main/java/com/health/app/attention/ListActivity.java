package com.health.app.attention;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.health.app.R;
import com.health.app.widget.FLActivity;


public class ListActivity extends FLActivity {

	LinearLayout llayoutAdd,llayoutList;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_main);
		navSetContentView(R.layout.activity_user_attention_list);
		linkUiVar();
		bindListener();
		ensureUi();

	}

	@Override
	public void linkUiVar() {
		llayoutList = (LinearLayout)findViewById(R.id.llayoutList);
		llayoutAdd = (LinearLayout)findViewById(R.id.llayoutAdd);
	}

	@Override
	public void bindListener() {
		llayoutAdd.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent();
				intent.setClass(mContext,SearchActivity.class);
				startActivity(intent);

			}
		});
	}

	@Override
	public void ensureUi() {
		setNavbarTitleText("关注");
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

}
