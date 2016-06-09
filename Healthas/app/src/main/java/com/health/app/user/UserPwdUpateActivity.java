package com.health.app.user;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.health.app.R;
import com.health.app.api.Api;
import com.health.app.type.MsgType;
import com.health.app.type.UserResponse;
import com.health.app.widget.FLActivity;
import com.mslibs.api.CallBack;


public class UserPwdUpateActivity extends FLActivity {

	EditText editOld,editNew,editAgain;
	Button btnSub;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_user_update_pwd);
		navSetContentView(R.layout.activity_user_update_pwd);
		linkUiVar();
		bindListener();
		ensureUi();

	}

	@Override
	public void linkUiVar() {
		editOld = (EditText)findViewById(R.id.editOld);
		editNew = (EditText)findViewById(R.id.editNew);
		editAgain = (EditText)findViewById(R.id.editAgain);
		btnSub = (Button)findViewById(R.id.btnSub);
	}

	@Override
	public void bindListener() {
		btnSub.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				String pwdold = editOld.getText().toString().trim();
				String pwdnew = editNew.getText().toString().trim();
				String again = editAgain.getText().toString().trim();

				if(TextUtils.isEmpty(pwdold)){
					showMessage("请输入旧密码");
					return;
				}
				if(pwdnew.length() < 8){
					showMessage("密码最少8位");
					return;
				}
				if(TextUtils.isEmpty(again)){
					showMessage("请输入再次密码");
					return;
				}
				if(!pwdnew.equals(again)){
					showMessage("两次密码输入不一致");
					return;
				}

				hideSoftInput(editAgain);
				showLoadingLayout("正在提交，请稍后...");
				new Api(callback,mApp).updatePwd(pwdold,pwdnew,again);
			}
		});
	}

	@Override
	public void ensureUi() {
		setNavbarTitleText("修改密码");
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	CallBack callback = new CallBack() {

		@Override
		public void onSuccess(String response) {
			dismissLoadingLayout();
			Gson gson = new Gson();
			try {
				MsgType msgType = gson.fromJson(response, MsgType.class);
				if(msgType != null && msgType.message != null){
					showMessage(msgType.message);
				}else{
					showMessage("修改成功");
				}

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
