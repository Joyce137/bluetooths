package com.health.app.user;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.health.app.R;
import com.health.app.api.Api;
import com.health.app.type.MsgType;
import com.health.app.type.UserResponse;
import com.health.app.util.Preferences;
import com.health.app.widget.FLActivity;
import com.health.app.widget.RoundAngleImageView;
import com.koushikdutta.ion.builder.Builders;
import com.mslibs.api.CallBack;


public class UserInfoActivity extends FLActivity {

	LinearLayout llayoutPwd;
	RoundAngleImageView imageUser;
	ScrollView mScrollView;
	EditText editSex,editAge,editReal,editTel,editNick;
	UserResponse userResponse;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_main);
		navSetContentView(R.layout.activity_user_info);
		linkUiVar();
		bindListener();
		ensureUi();

	}

	@Override
	public void linkUiVar() {
		llayoutPwd = (LinearLayout)findViewById(R.id.llayoutPwd);
		imageUser = (RoundAngleImageView)findViewById(R.id.imageUser);
		editSex = (EditText)findViewById(R.id.editSex);
		editAge = (EditText)findViewById(R.id.editAge);
		editReal = (EditText)findViewById(R.id.editReal);
		editTel = (EditText)findViewById(R.id.editTel);
		editNick = (EditText)findViewById(R.id.editNick);
		mScrollView = (ScrollView)findViewById(R.id.mScrollView);
	}

	@Override
	public void bindListener() {
		llayoutPwd.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent4 = new Intent();
				intent4.setClass(mContext, UserPwdUpateActivity.class);
				startActivity(intent4);
			}
		});
		imageUser.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				BuildImageDialog(mContext, imageDialogCallback);
			}
		});
	}

	@Override
	public void ensureUi() {

		setNavbarTitleText("个人信息");
		mScrollView.setVisibility(View.GONE);
		showLoadingLayout("努力加载中...");
		new Api(callback2,mApp).getInfo();
//		hideRight(false);
//		getRight().setText("保存");
		getRight().setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				//editSex,editAge,editReal,editTel,editNick;
				String sex = editSex.getText().toString().trim();
				String age = editAge.getText().toString().trim();
				String real = editReal.getText().toString().trim();
				String tel = editTel.getText().toString().trim();
				String nick = editNick.getText().toString().trim();


				if(TextUtils.isEmpty(nick)){
					showMessage("请输入昵称");
					return;
				}
				if(TextUtils.isEmpty(sex)){
					showMessage("请输入性别");
					return;
				}else{
					if(!sex.equals("男") && !sex.equals("女")){
						showMessage("性别输入男或女");
						return;
					}
				}
				if(TextUtils.isEmpty(age)){
					showMessage("请输入年龄");
					return;
				}
				if(TextUtils.isEmpty(real)){
					showMessage("请输入真实姓名");
					return;
				}
				if(TextUtils.isEmpty(tel)){
					showMessage("请输入联系电话");
					return;
				}

				String gender = "";
				if(sex.equals("男")){
					gender = "1";
				}else{
					gender = "2";
				}

				hideSoftInput(editAge);
				showLoadingLayout("正在提交，请稍后...");
				new Api(callback,mApp).updateInfo(real,age,gender,tel,nick);
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	CallBack callback2 = new CallBack() {
		@Override
		public void onSuccess(String response) {
			Gson gson = new Gson();
			try {
				userResponse = gson.fromJson(response, UserResponse.class);
				if(userResponse != null){
					//editSex,editAge,editReal,editTel,editNick;
				}
			} catch (JsonSyntaxException e) {
				e.printStackTrace();
			}
			hideRight(false);
			getRight().setText("保存");

			mScrollView.setVisibility(View.VISIBLE);
			dismissLoadingLayout();
		}
		@Override
		public void onFailure(String message) {
			super.onFailure(message);
			dismissLoadingLayout();

			showTipsLayout("连接失败", "请检查您的网络是否可用",
					"重试", new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							showLoadingLayout("努力加载中...");
							new Api(callback2,mApp).getInfo();

						}
					});
		}
	};
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
					showMessage("信息修改成功");
				}

				sendBroadcast(Preferences.BROADCAST_ACTION.USER_UPDATE);


				Intent intent = new Intent(Preferences.BROADCAST_ACTION.USER_UPDATE);
				LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
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
	CallBack imageDialogCallback = new CallBack() {

		@Override
		public void onSuccess(String response) {
			// showMessage("图片己选中！");
			Bitmap bmp = (Bitmap) getExtra();

			final float mDensity = getMetricsDensity();
			imageUser.setxRadius(30 * mDensity);
			imageUser.setyRadius(30 * mDensity);
			imageUser.setImageBitmap(bmp);

			new Api(avatarcallback, mApp).updateAvatar(response);

		}
	};
	CallBack avatarcallback = new CallBack() {

		@Override
		public void onSuccess(String response) {
			dismissProgress();
			showMessage("头像保存成功！");

			Intent intent = new Intent();
			intent.setAction(Preferences.BROADCAST_ACTION.USER_UPDATE);
			sendBroadcast(intent);

			Intent intent2 = new Intent(Preferences.BROADCAST_ACTION.USER_UPDATE);
			LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent2);
		}

		@Override
		public void onFailure(String message) {
			super.onFailure(message);
			dismissProgress();
			showMessage(message);
		}
	};
}
