package com.health.app.user;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.byl.datepicker.wheelview.WheelView;
import com.health.app.R;
import com.health.app.widget.FLActivity;


public class TargetActivity extends FLActivity {

	TextView textOne,textWeek,textMonth;
	LinearLayout llayoutOne,llayoutWeek,llayoutMonth;
	LinearLayout includeTarget;
	TextView textTip;
	WheelView Step;
	Button btnSave;
	int type = 0;//1=one,2=week,3=month
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_user_target);
		navSetContentView(R.layout.activity_user_target);
		linkUiVar();
		bindListener();
		ensureUi();

	}

	@Override
	public void linkUiVar() {
		textOne = (TextView)findViewById(R.id.textOne);
		textWeek = (TextView)findViewById(R.id.textWeek);
		textMonth = (TextView)findViewById(R.id.textMonth);
		llayoutOne = (LinearLayout)findViewById(R.id.llayoutOne);
		llayoutWeek = (LinearLayout)findViewById(R.id.llayoutWeek);
		llayoutMonth = (LinearLayout)findViewById(R.id.llayoutMonth);
		includeTarget = (LinearLayout)findViewById(R.id.includeTarget);
		textTip = (TextView)findViewById(R.id.textTip);
		Step = (WheelView)findViewById(R.id.Step);
		btnSave = (Button)findViewById(R.id.btnSave);
	}

	@Override
	public void bindListener() {
		btnSave.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				includeTarget.setVisibility(View.GONE);
			}
		});
		llayoutOne.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				type = 1;
				includeTarget.setVisibility(View.VISIBLE);
			}
		});
	}

	@Override
	public void ensureUi() {
		setNavbarTitleText("目标设置");
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

}
