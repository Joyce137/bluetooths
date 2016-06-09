package com.health.app.user;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.health.app.R;
import com.health.app.api.Api;
import com.health.app.type.MsgType;
import com.health.app.widget.FLActivity;
import com.mslibs.api.CallBack;


public class PwdForgetActivity extends FLActivity {

	EditText editName,editCode,editNew,editAgain;
	TextView textCode;
	Button btnSub;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_user_forget);
		navSetContentView(R.layout.activity_user_forget);
		linkUiVar();
		bindListener();
		ensureUi();

	}

	@Override
	public void linkUiVar() {
		editName = (EditText)findViewById(R.id.editName);
		editCode = (EditText)findViewById(R.id.editCode);
		editNew = (EditText)findViewById(R.id.editNew);
		editAgain = (EditText)findViewById(R.id.editAgain);
		textCode = (TextView)findViewById(R.id.textCode);
		btnSub = (Button)findViewById(R.id.btnSub);


	}

	@Override
	public void bindListener() {
		textCode.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				String phone = editName.getText().toString();
				if (phone.length() == 0) {
					showMessage("请输入手机号！");
					return;
				}
				if (phone.length() != 11) {
					showMessage("请输入正确的手机号");
					return;
				}
				hideSoftInput(editName);
				new Api(callbackForcode, mApp).getCode(phone, 1);
				textCode.setEnabled(false);
			}
		});
		btnSub.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				String phone = editName.getText().toString();
				String code = editCode.getText().toString();
				String pwd = editNew.getText().toString().trim();
				String pwda = editAgain.getText().toString().trim();
				if (phone.length() == 0) {
					showMessage("请输入手机号！");
					return;
				}
				if (phone.length() != 11) {
					showMessage("请输入正确的手机号");
					return;
				}
				if(TextUtils.isEmpty(code)){
					showMessage("请输入验证码");
					return;
				}
				if(TextUtils.isEmpty(pwd)){
					showMessage("请输入密码");
					return;
				}
				if(pwd.length() < 8){
					showMessage("密码最少8位");
					return;
				}
				if(TextUtils.isEmpty(pwda)){
					showMessage("请输入再次密码");
					return;
				}
				if(!pwd.equals(pwda)){
					showMessage("两次密码输入不一致");
					return;
				}
				hideSoftInput(editCode);
				showLoadingLayout("正在提交，请稍后...");
				new Api(callback,mApp).resetPwd(phone,pwd,pwda,code);
			}
		});
	}

	@Override
	public void ensureUi() {
		setNavbarTitleText("忘记密码");
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	CallBack callbackForcode = new CallBack() {

		@Override
		public void onSuccess(String response) {

			Gson gson = new Gson();
			try {
				MsgType msgType = gson.fromJson(response, MsgType.class);
				if(msgType != null && msgType.message != null){
					showMessage(msgType.message);
				}else{
					showMessage("短信发送成功，请注意查收");
				}


				msgSent();

			} catch (JsonSyntaxException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onFailure(String message) {
			super.onFailure(message);
			showMessage(message);
			textCode.setEnabled(true);
		}
	};

	public void msgSent() {
		Log.e(TAG, "msgSent");

		new CountDownTimer(60 * 1000, 1000) {
			public void onTick(long millisUntilFinished) {
				textCode
						.setText("获取验证码\n (" + millisUntilFinished / 1000 + ")");
			}

			public void onFinish() {
				textCode.setText("获取验证码");
				textCode.setEnabled(true);
			}
		}.start();
	}
	CallBack callback = new CallBack() {
		@Override
		public void onSuccess(String response) {
			Gson gson = new Gson();
			try {
				MsgType msgType = gson.fromJson(response, MsgType.class);
				if (msgType != null && msgType.message != null) {
					showMessage(msgType.message);
				} else {
					showMessage("重置密码成功");
				}

			} catch (JsonSyntaxException e) {
				e.printStackTrace();
			}

			mActivity.finish();
			dismissLoadingLayout();
		}

		@Override
		public void onFailure(String message) {
			super.onFailure(message);
			showMessage(message);
			dismissLoadingLayout();
		}
	};
}
