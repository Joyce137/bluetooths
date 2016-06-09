package com.health.app.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.health.app.MainActivity;
import com.health.app.R;
import com.health.app.type.UserResponse;
import com.health.app.util.Preferences;
import com.health.app.widget.FLActivity;


public class SuccessActivity extends FLActivity {

	Button btnSignup;
	UserResponse user;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_user_signup_success);
		navSetContentView(R.layout.activity_user_signup_success);
		linkUiVar();
		bindListener();
		ensureUi();

	}

	@Override
	public void linkUiVar() {
		btnSignup = (Button)findViewById(R.id.btnSignup);
	}

	@Override
	public void bindListener() {
		btnSignup.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				mApp.setPreference(Preferences.LOCAL.TOKEN,
						user.token);
				mApp.setPreference(Preferences.LOCAL.PHONE,
						user.account);

				sendBroadcast(Preferences.BROADCAST_ACTION.USERSIGNUP);

				Intent intent = new Intent();
				intent.setClass(mActivity, MainActivity.class);
				startActivity(intent);
				finish();
			}

		});
	}

	@Override
	public void ensureUi() {
		setNavbarTitleText("注册成功");

		user = (UserResponse) getIntent().getSerializableExtra("user");
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

}
