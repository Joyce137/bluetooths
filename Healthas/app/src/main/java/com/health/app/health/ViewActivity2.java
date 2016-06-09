package com.health.app.health;

import android.os.Bundle;
import android.widget.TextView;

import com.health.app.R;
import com.health.app.widget.FLActivity;


public class ViewActivity2 extends FLActivity {

	int type = 0;
	TextView textType;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_health_height);
		navSetContentView(R.layout.activity_health_kgbmi4);
		linkUiVar();
		bindListener();
		ensureUi();

	}

	@Override
	public void linkUiVar() {
		textType = (TextView)findViewById(R.id.textType);
	}

	@Override
	public void bindListener() {

	}

	@Override
	public void ensureUi() {
		setNavbarTitleText("");

		type = getIntent().getIntExtra("type",0);
		switch (type){
			case 2:
				setNavbarTitleText("体重");
				textType.setText("体重");
				break;
			case 3:
				setNavbarTitleText("BMI");
				textType.setText("BMI");
				break;
			case 4:
				setNavbarTitleText("血糖");
				textType.setText("空腹血糖");
				break;
			case 5:
				setNavbarTitleText("血压");
				textType.setText("血压");
				break;
			case 6:
				setNavbarTitleText("糖化血红蛋白");
				textType.setText("糖化血红蛋白");
				break;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

}
