package com.health.app.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.health.app.R;
import com.health.app.api.Api;
import com.health.app.type.MsgType;
import com.health.app.type.UserResponse;
import com.health.app.widget.FLActivity;
import com.mslibs.api.CallBack;


public class UserSignupActivity extends FLActivity {

	TextView textAgree,textCode;
	Button btnSignup;
	EditText editReal,editPhone,editCode,editPwd,editPwdAgain,editNo,editTel;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_user_signup);
		navSetContentView(R.layout.activity_user_signup);
		linkUiVar();
		bindListener();
		ensureUi();

	}

	@Override
	public void linkUiVar() {
		textCode= (TextView)findViewById(R.id.textCode);
		textAgree = (TextView)findViewById(R.id.textAgree);
		btnSignup = (Button)findViewById(R.id.btnSignup);
		editReal = (EditText)findViewById(R.id.editReal);
		editPhone = (EditText)findViewById(R.id.editPhone);
		editCode = (EditText)findViewById(R.id.editCode);
		editPwd = (EditText)findViewById(R.id.editPwd);
		editPwdAgain = (EditText)findViewById(R.id.editPwdAgain);
		editNo = (EditText)findViewById(R.id.editNo);
		editTel = (EditText)findViewById(R.id.editTel);


	}

	@Override
	public void bindListener() {
		textAgree.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent7 = new Intent();
				intent7.setClass(mContext, AgreementActivity.class);
				startActivity(intent7);
			}
		});
		btnSignup.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				String real = editReal.getText().toString().trim();
				String phone = editPhone.getText().toString().trim();
				String code = editCode.getText().toString().trim();
				String pwd = editPwd.getText().toString().trim();
				String pwda = editPwdAgain.getText().toString().trim();
				String no = editNo.getText().toString().trim();
				String tel = editTel.getText().toString().trim();

				if(TextUtils.isEmpty(real)){
					showMessage("请输入真实姓名");
					return;
				}
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
				if(TextUtils.isEmpty(no)){
					showMessage("请输入身份证号码");
					return;
				}
				if(TextUtils.isEmpty(tel)){
					showMessage("请输入紧急联系电话");
					return;
				}

				hideSoftInput(editCode);
				showLoadingLayout("正在提交，请稍后...");
				new Api(callback,mApp).signUp(phone,pwd,pwda,code,real,no,tel);
			}
		});
		textCode.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				String phone = editPhone.getText().toString();
				if (phone.length() == 0) {
					showMessage("请输入手机号！");
					return;
				}
				if (phone.length() != 11) {
					showMessage("请输入正确的手机号");
					return;
				}
				hideSoftInput(editPhone);
				new Api(callbackForcode, mApp).getCode(phone, 2);
				textCode.setEnabled(false);
			}
		});
	}

	@Override
	public void ensureUi() {
		setNavbarTitleText("填写注册信息");
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
					showMessage(msgType.message);

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
				UserResponse userResponse = gson.fromJson(response,UserResponse.class);


				Intent intent = new Intent();
				intent.setClass(mContext,SuccessActivity.class);
				intent.putExtra("user",userResponse);
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
		}
	};

}
