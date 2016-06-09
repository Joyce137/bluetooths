package com.health.app.widget;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.health.app.R;
import com.mslibs.widget.CActivity;

/**
 * 通用页面模板：标题栏,dialog
 * 
 * @author sherlock
 */
public class NavbarActivity extends CActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.navbar);
		setTopBarAndAction();

	}

	/**
	 * 设置主内容区域的layoutRes
	 * 
	 * @param layoutResId
	 */
	public void navSetContentView(int layoutResId) {
		LinearLayout llayoutContent = (LinearLayout) findViewById(R.id.llayoutContent);
		LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(layoutResId, null);
		llayoutContent.addView(v);
	}

	/**
	 * 隐藏左侧按钮
	 */
	public void hideLeft(boolean bSetHide) {
		ImageButton bt = getLeft();
		if (null != bt) {
			if (bSetHide)
				bt.setVisibility(View.GONE);
			else
				bt.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 隐藏右侧按钮
	 */
	public void hideRight(boolean bSetHide) {
		Button bt = getRight();
		if (null != bt) {
			if (bSetHide)
				bt.setVisibility(View.GONE);
			else
				bt.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 隐藏右侧图片 true 隐藏
	 */
	public void hideRightPic(boolean bSetHide) {
		Button bt = getRightPic();
		if (null != bt) {
			if (bSetHide)
				bt.setVisibility(View.GONE);
			else
				bt.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 得到模板上导航栏的左侧按钮，一般为[返回]
	 * 
	 * @return 成功则返回Button对象，失败则返回null。
	 */
	public ImageButton getLeft() {
		return (ImageButton) findViewById(R.id.btnBack);
	}

	/**
	 * 得到模板上导航栏的右侧按钮，一般为[刷新]
	 * 
	 * @return 成功则返回Button对象，失败则返回null。
	 */
	public Button getRight() {
		return (Button) findViewById(R.id.btnRightWenzi);
	}

	/**
	 * 得到模板上导航栏的右侧按钮，一般为图片
	 * 
	 * @return 成功则返回Button对象，失败则返回null。
	 */
	public Button getRightPic() {
		return (Button) findViewById(R.id.btnRightPic);
	}

	/**
	 * 设置模板上导航栏中间的标题文字
	 * 
	 * @param titleText
	 * @return 修改成功返回true，失败返回false
	 */
	public boolean setNavbarTitleText(String titleText) {
		TextView tv = (TextView) findViewById(R.id.textNavbarTitle);
		if (null != tv) {
			tv.setText(titleText);
			return true;
		}
		return false;
	}

	public String getNavbarTitleText() {
		TextView tv = (TextView) findViewById(R.id.textNavbarTitle);
		String text = null;
		if (null != tv) {
			text = tv.getText().toString().trim();
			return text;
		}

		return null;
	}

	public Button getSure() {
		return (Button) findViewById(R.id.btnNavSure);
	}

	public Button getCancal() {
		return (Button) findViewById(R.id.btnNavCancel);
	}

	public TextView getTip() {
		return (TextView) findViewById(R.id.textNavTip);
	}

	public TextView getDesc() {
		return (TextView) findViewById(R.id.textNavDesc);
	}

	public LinearLayout getDialog() {
		return (LinearLayout) findViewById(R.id.llayoutNavDialog);
	}

	public LinearLayout getLine() {
		return (LinearLayout) findViewById(R.id.llayoutLine);
	}

	public void showDialog(String tip, String desc, String cancal, String sure) {
		if (tip != null && tip.length() > 0) {
			getTip().setText(tip);
		}
		if (desc != null && desc.length() > 0) {
			getDesc().setText(desc);
		}
		if (cancal != null && cancal.length() > 0) {
			getCancal().setText(cancal);
		} else {
			getCancal().setText("取消");
		}
		if (sure != null && sure.length() > 0) {
			getSure().setText(sure);
		} else {
			getSure().setText("确定");
		}
		getDialog().setVisibility(View.VISIBLE);
	}

	public void showDialog(String tip, String desc, String sure) {
		if (tip.length() > 0) {
			getTip().setText(tip);
		}
		if (desc.length() > 0) {
			getDesc().setText(desc);
		}
		if (sure.length() > 0) {
			getSure().setText(sure);
		} else {
			getSure().setText("确定");
		}
		getCancal().setVisibility(View.GONE);
		getLine().setVisibility(View.GONE);
		getDialog().setVisibility(View.VISIBLE);
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

	protected void setTopBarAndAction() {
		setNavbarTitleText(""); // 设置Title标题
		// getLeft().setText("返回"); // 设置左按钮上的文字
		getLeft().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 按钮执行事件
				finish();
			}
		});
		getRight().setText(""); // 设置右按钮上的文字
		getRight().setBackgroundDrawable(null);
		getRight().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 按钮执行事件
				// Intent intent = new Intent(mContext,
				// UserSignupAcivity.class);
				// startActivity(intent);
				// finish();
			}
		});
		hideRightPic(true);
		hideRight(true);
		getDialog().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getDialog().setVisibility(View.GONE);
			}
		});
		getCancal().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getDialog().setVisibility(View.GONE);

			}
		});
		getSure().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getDialog().setVisibility(View.GONE);

			}
		});

	}
}
