package com.mslibs.widget;

import com.health.app.R;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;


public class CSpannedTextView extends LinearLayout {

	private String TAG = "CSpannedTextView";	
	private Activity mActivity;
	private TextView mTextView;
	
	public CSpannedTextView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public CSpannedTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onFinishInflate() {
		// TODO Auto-generated method stub
		super.onFinishInflate();
		View.inflate(this.getContext(), R.layout.widget_spanned_textview, this);
		linkUiVar();
		bindListener();
		ensureUi();
	}
	private void linkUiVar() {
		mTextView = ((TextView) findViewById(R.id.spanned_textview));
	}
	private void bindListener() {

	}
	private void ensureUi() {
		
	}
	
	public void setTextBySpanned(String htmlText, String prefix, Activity act) {
		
		mActivity = act;
		//htmlText = "1<br>2<br>3<br>4<br>5<br>"+htmlText;
		//URLImageParser p = new URLImageParser(this, getContext(), prefix);
		//Spanned htmlSpan = Html.fromHtml(htmlText, p, null);
		//mTextView.setLineSpacing(10,(float)1.2);
		//mTextView.setText(htmlSpan);
		//mTextView.setMovementMethod(LinkMovementMethod.getInstance());
	}
	
}
