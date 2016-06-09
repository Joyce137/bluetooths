package com.health.app.user;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.health.app.MainActivity;
import com.health.app.R;
import com.health.app.api.Api;
import com.health.app.device.BindActivity;
import com.health.app.type.UserResponse;
import com.health.app.util.Preferences;
import com.health.app.widget.FLActivity;
import com.mslibs.api.CallBack;


public class UserSignActivity extends FLActivity {

	EditText  editName,editPWD;
	Button btnSignin,btnSignup,btnFotget;

	BroadcastReceiver broadcastReceiver;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_signin);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		linkUiVar();
		bindListener();
		ensureUi();

		broadcastReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				if(intent.getAction().equals(Preferences.BROADCAST_ACTION.USERSIGNUP)){
					finish();
				}
			}
		};
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(Preferences.BROADCAST_ACTION.USERSIGNUP);
		registerReceiver(broadcastReceiver,intentFilter);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(broadcastReceiver);
	}

	@Override
	public void linkUiVar() {
		editName  = (EditText)findViewById(R.id.editName);
		editPWD  = (EditText)findViewById(R.id.editPWD);
		btnSignin  = (Button)findViewById(R.id.btnSignin);
		btnSignup  = (Button)findViewById(R.id.btnSignup);
		btnFotget  = (Button)findViewById(R.id.btnFotget);

	}

	@Override
	public void bindListener() {
		btnSignup.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent7 = new Intent();
				intent7.setClass(mContext, UserSignupActivity.class);
				startActivity(intent7);
			}
		});
		btnFotget.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent7 = new Intent();
				intent7.setClass(mContext, PwdForgetActivity.class);
				startActivity(intent7);
			}
		});
		btnSignin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				String  tel = editName.getText().toString().trim();
				String  pwd = editPWD.getText().toString().trim();

				if(TextUtils.isEmpty(tel)){
					showMessage("请输入手机号");
					return;
				}
				if(TextUtils.isEmpty(pwd)){
					showMessage("请输入密码");
					return;
				}
				hideSoftInput(editName);
				showLoadingLayout("正在登录，请稍后...");
				new Api(callback,mApp).signIn(tel,pwd);
			}
		});
	}

	@Override
	public void ensureUi() {
		if(mApp.getPreference(Preferences.LOCAL.PHONE) != null && mApp.getPreference(Preferences.LOCAL.PHONE).length()>0){
			editName.setText(mApp.getPreference(Preferences.LOCAL.PHONE));
		}
	}
	CallBack callback = new CallBack() {
		@Override
		public void onSuccess(String response) {
			dismissLoadingLayout();
			Gson gson = new Gson();
			try {
				UserResponse user = gson.fromJson(response, UserResponse.class);
				mApp.setPreference(Preferences.LOCAL.TOKEN, user.token);
				mApp.setPreference(Preferences.LOCAL.PHONE, user.account);

				sendBroadcast(Preferences.BROADCAST_ACTION.USERSIGNIN);

				Intent intent = new Intent();
				intent.setClass(mActivity, MainActivity.class);
				startActivity(intent);
				finish();

			} catch (JsonSyntaxException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onFailure(String message) {
			super.onFailure(message);
			dismissLoadingLayout();
			showMessage(message);

//			Intent intent = new Intent();
//			intent.setClass(mActivity, MainActivity.class);
//			startActivity(intent);
//			finish();

		}
	};
	@Override
	protected void onResume() {
		super.onResume();
	}

}
