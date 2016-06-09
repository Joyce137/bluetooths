package com.mslibs.widget;

import android.content.Context;
import android.graphics.Paint;
import android.os.CountDownTimer;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.koushikdutta.ion.Ion;
import com.mslibs.utils.CountDownType;
import com.mslibs.utils.MsStringUtils;
import com.mslibs.utils.RoundedTransformation;
import com.squareup.picasso.Picasso;

public class MSListViewParam {

	private final String TAG = "MSListViewParam";

	// private int mViewRsID = 0;
	public int mItemRsID = 0; // 单个view的id
	public Object mData = null; // 付值数据
	public boolean mVisibility = true; // 单个可见设置参数

	public OnClickListener onClickListener = null; // 单个view点击事件
	public OnKeyListener onKeyListener = null; // 键盘输入事件
	public OnLongClickListener onLongClickListener = null; //  长按点击事件

	public Object mTag = null;// 付给view.tag
	
	public boolean isImgAsync = false;// 是否异步读取图片	
	
	private Context mContext = null;// 是否异步读取图片
	
	public boolean isCountDown = false;
	public CountDownType countDownCallback = null;

	private int textViewPaintFlags = -1; // 抗锯齿

	private String hint = null;

	private int width = 0;
	private int height = 0;
	private int radius = 0;
	
	public MSListViewParam(int ItemRsID, Object data, Boolean Visibility) {
		mItemRsID = ItemRsID;
		mData = data;
		mVisibility = Visibility;
	}

	public void setItemTag(Object tag) {
		mTag = tag;		
	}
	
	public void setItemResizeTag(Object tag, int width, int height) {
		mTag = tag;
		this.width = width;
		this.height = height;
	}
	
	public void setItemTag(Object tag, int width, int radius) {
		mTag = tag;
		this.width = width;
		this.height = width;
		this.radius = radius;
	}
	
	public void setImgAsync(boolean b, Context c) {
		isImgAsync = b;
		mContext = c;
	}

	public void setCountDown(boolean b, CountDownType countDownCallback) {
		isCountDown = b;
		this.countDownCallback = countDownCallback;
	}
	
	public void setOnclickLinstener(OnClickListener l) {
		this.onClickListener = l;
	}
	
	public void setOnClickListener(OnClickListener l) {
		this.onClickListener = l;
	}

	public void setOnKeyListener(OnKeyListener l) {
		this.onKeyListener = l;
	}
	
	public void setOnLongClickListener(OnLongClickListener onLongClickListener) {
		this.onLongClickListener = onLongClickListener;
	}
	
	public void setTextViewPaintFlags(int textViewPaintFlags) {
		this.textViewPaintFlags = textViewPaintFlags;
	}

	public void bindItemData(View listItemView) {

		View itemView = listItemView.findViewById(mItemRsID);

		if (itemView != null) {
			// 设置点击事件
			if (onClickListener != null) {
				itemView.setOnClickListener(onClickListener);
			}

			if (onKeyListener != null) {
				itemView.setOnKeyListener(onKeyListener);
			}
			
			if (onLongClickListener != null){
				itemView.setOnLongClickListener(onLongClickListener);
			}

			// 设置是否显示
			if (mVisibility) {
				itemView.setVisibility(View.VISIBLE);
			} else {
				itemView.setVisibility(View.GONE);
			}

			// 设置Tag值
			if (mTag != null)
				itemView.setTag(mTag);
			
			// 设置hint
			if(hint != null && itemView instanceof EditText){
				((EditText) itemView).setHint(hint);
			}

			// 设置Data值
			if (mData != null && mData.toString().length() > 0) {
				if (itemView instanceof Button) {
					// button,Date-->setText;
					if (mData instanceof String) {
						((Button) itemView).setText(mData.toString());
					} else if (mData instanceof Integer) {
						((Button) itemView).setBackgroundResource(Integer.parseInt(mData.toString()));
					}
				} else if (itemView instanceof ImageButton) {
					// ImageButton,Date-->setImageResource;
					// Log.e(TAG, "ImageButton " + mData.toString());
					((ImageButton) itemView).setImageResource(Integer.parseInt(mData.toString()));
				} else if (itemView instanceof TextView) {
					// TextView,Date-->setText;
					if(isCountDown) {
						String data = mData.toString();						
						final TextView view = (TextView) itemView;
						long left = MsStringUtils.str2long(data);
						if(left>0 && countDownCallback!=null) {
							new CountDownTimer(left * 1000, countDownCallback.countDownInterval*1000) {
								public void onTick(long millisUntilFinished) {
									String value = countDownCallback.onTickString(millisUntilFinished);
									view.setText(value);
								}
								public void onFinish() {
									view.setText(countDownCallback.countDownFinishString);
								}
							}.start();
							
						} else if(countDownCallback!=null) {
							view.setText(countDownCallback.countDownFinishString);
						}
					} else {
						if (mData instanceof Spanned) {
							((TextView) itemView).setText((Spanned) mData);
							((TextView) itemView).setMovementMethod(LinkMovementMethod.getInstance());
						} else {
							((TextView) itemView).setText(mData.toString());
						}

						if (textViewPaintFlags > -1) {
							((TextView) itemView).getPaint().setFlags(textViewPaintFlags | Paint.ANTI_ALIAS_FLAG);
						}
					}

				} else if (itemView instanceof CSpannedTextViewBase) {
					// Log.e(TAG, "instanceof CSpannedTextViewBase");
					// TextView,Date-->setText;
					if (mData != null) {
						// view.setVisibility(View.VISIBLE);
					}
					((CSpannedTextViewBase) itemView).setData(mData);
				} else if (itemView instanceof ImageView) {
					// ImageView,Date-->setImageResource;'
					ImageView imgView = (ImageView) itemView;
					
					int resource = 0;
					if (mData != null) {
						resource = MsStringUtils.str2int(mData.toString());
					}
					
					if (isImgAsync) {
						// Log.e(TAG, "getImgAsync:" + mTag);
						if (mTag != null) {
							if(mContext!=null && !TextUtils.isEmpty(mTag.toString())) {								
								if(width>0 && height>0 && radius>0) {
									Picasso.with(mContext).load(mTag.toString()).placeholder(resource)
									.resize(width,height).transform(new RoundedTransformation(radius, 0)).into(imgView);
								} else if(width>0 && height>0) {
									Log.e(TAG, "resize width:"+width+ " height:"+height);
									Picasso.with(mContext).load(mTag.toString()).placeholder(resource)
									.resize(width,height).centerCrop().into(imgView);
								} else {
									//Log.e(TAG, "using Picasso");
									Picasso.with(mContext).load(mTag.toString()).placeholder(resource)
									.fit().into(imgView);
								}
							} else {
								
								//Log.e(TAG, "using ion");
								Ion.with((ImageView) itemView)
								.placeholder(resource)
								.error(resource)
								.load(mTag.toString());
							}
						}
					} else {
						imgView.setImageResource(resource);
					}

				} else if (itemView instanceof ProgressBar) {
					((ProgressBar) itemView).setProgress(Integer.parseInt(mData.toString()));
				} else if (itemView instanceof EditText) {
					((EditText) itemView).setText(mData.toString());
					
				}
			}
		}
	}

	public String getHint() {
		return hint;
	}

	public void setHint(String hint) {
		this.hint = hint;
	}
	
	
	
}
