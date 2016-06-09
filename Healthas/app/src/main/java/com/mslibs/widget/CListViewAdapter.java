package com.mslibs.widget;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

public class CListViewAdapter extends BaseAdapter {

	private final String TAG = "CListViewAdapter";

	private LayoutInflater mInflater;
	private int mItemResource;
	private int mHeaderResource = 0;
	private int mFooterResource = 0;
	private int mSingleResource = 0;
	private int mGetMoreResource = 0;

	private int mSelectedResource = 0;
	private int mSelectedIndex = -1;
	private View mSelectedView = null;

	public void setSelectedResource(int res) {
		this.mSelectedResource = res;
	}

	public void setSelectedIndex(int i) {
		this.mSelectedIndex = i;
	}
	
	public void setSelectedIndex(int i,View v) {
		this.mSelectedIndex = i;
		if(mSelectedView != null){
			mSelectedView.setVisibility(View.GONE);
		}
		mSelectedView = v;
		mSelectedView.setVisibility(View.VISIBLE);
	}
	
	

	public boolean isNotMore = false;

	private Activity mActivity;

	private OnClickListener onItemOnclickLinstener = null;
	private OnClickListener onGetMoreClickListener = null;

	public boolean ItemViewEmptyInvisible = false;
	private ArrayList<ArrayList<CListViewParam>> mDateList;

	// 仅对打开子版块使用
	public int mChildForumResource = 0;
	public int isOpenChildForumTag;

	public CListViewAdapter(Activity activity, int resouce) {
		mActivity = activity;
		mItemResource = resouce;
		mInflater = (LayoutInflater) activity.getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public CListViewAdapter(Context context, int resouce) {
		mItemResource = resouce;
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public void setData(ArrayList<ArrayList<CListViewParam>> LVPs) {
		mDateList = LVPs;
	}

	public void setItemOnclickLinstener(OnClickListener onClickListener) {
		this.onItemOnclickLinstener = onClickListener;
	}

	public void setGetMoreResource(int resouce) {
		mGetMoreResource = resouce;
	}

	public void setHeaderResource(int resouce) {
		mHeaderResource = resouce;
	}

	public void setFooterResource(int resouce) {
		mFooterResource = resouce;
	}

	public void setSingleResource(int resouce) {
		mSingleResource = resouce;
	}

	public void setGetMoreClickListener(OnClickListener onClickListener) {
		this.onGetMoreClickListener = onClickListener;
	}

	@Override
	public int getCount() {
		return mDateList.size();
	}

	@Override
	public Object getItem(int position) {
		return mDateList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View newView = null;

		// if (convertView != null) {
		// newView = convertView;
		// }

		if (newView == null) {
			if (mDateList.size() == 1) {
				// 单个数据
				if (mSingleResource != 0) {
					newView = mInflater.inflate(mSingleResource, null);

					// Log.e(TAG, "["+position+"]mSingleResource:" +
					// mSingleResource);

					if (onItemOnclickLinstener != null) {
						newView.setOnClickListener(onItemOnclickLinstener);
					}
				}
			} else {
				// 多个数据
				if (position == 0) {
					// 加载头部
					if (mHeaderResource != 0) {
						newView = mInflater.inflate(mHeaderResource, null);

						// Log.e(TAG, "["+position+"]mHeaderResource:" +
						// mHeaderResource);

						if (onItemOnclickLinstener != null) {
							newView.setOnClickListener(onItemOnclickLinstener);
						}
					}
				} else if (position == mDateList.size() - 1) {

					if (isNotMore == true) {
						// 加载尾部
						if (mFooterResource != 0) {
							newView = mInflater.inflate(mFooterResource, null);

							// Log.e(TAG, "["+position+"]mFooterResource:" +
							// mFooterResource);

							if (onItemOnclickLinstener != null) {
								newView.setOnClickListener(onItemOnclickLinstener);
							}
						}
					} else {
						// 加载更多
						if (mGetMoreResource != 0) {
							newView = mInflater.inflate(mGetMoreResource, null);

							// Log.e(TAG, "["+position+"]mGetMoreResource:" +
							// mGetMoreResource);

							if (onGetMoreClickListener != null) {
								newView.setOnClickListener(onGetMoreClickListener);
							}
						}
					}
				}
			}

			// 加载正常行
			if (newView == null && mItemResource != 0) {
				newView = mInflater.inflate(mItemResource, null);

				// Log.e(TAG, "["+position+"]mItemResource:" + mItemResource);

				if (onItemOnclickLinstener != null) {
					newView.setOnClickListener(onItemOnclickLinstener);
				}
			}
		}

		// 绑定编号
		newView.setTag(position);

		bindItemData(newView, mDateList.get(position));

		// 仅对打开子版块处理
		if (mChildForumResource != 0) {
			View childView = newView.findViewById(mChildForumResource);
			if (position != isOpenChildForumTag) {
				childView.setVisibility(View.GONE);
			} else {
				childView.setVisibility(View.VISIBLE);
			}
		}

		// 选择状态
		if (mSelectedResource > 0) {
			View v = newView.findViewById(mSelectedResource);
			if (v != null) {
				if (mSelectedIndex == position) {
					v.setVisibility(View.VISIBLE);
					mSelectedView = v;
				} else {
					v.setVisibility(View.GONE);
				}
			}
		}

		return newView;
	}

	// 绑定行的数据
	private void bindItemData(View view, ArrayList<CListViewParam> LVP) {
		for (int i = 0; i < LVP.size(); i++) {
			View v = view.findViewById(LVP.get(i).getItemRsID());
			bindViewData(v, LVP.get(i));
		}
	}

	// 绑定单个控件的数据
	private void bindViewData(View view, CListViewParam iLVP) {
		if (view != null) {
			// 设置点击事件
			if (iLVP.getOnclickListner() != null) {
				view.setOnClickListener(iLVP.getOnclickListner());
			}
			
			if (iLVP.getOnKeyListener() != null) {
				view.setOnKeyListener(iLVP.getOnKeyListener());
			}

			// 设置是否显示
			if (iLVP.isSetVisibility()) {
				if (iLVP.getVisibility() == true) {
					view.setVisibility(View.VISIBLE);
				} else {
					view.setVisibility(View.GONE);
				}
			} else {
				if (ItemViewEmptyInvisible) {
					if (iLVP.getDate() == null || iLVP.getDate().toString().length() == 0) {
						view.setVisibility(View.GONE);
					}
				}
			}

			// 设置Tag值
			if (iLVP.getItemTag() != null)
				view.setTag(iLVP.getItemTag());

			// 设置Date值
			if (iLVP.getDate() != null && iLVP.getDate().toString().length() > 0) {

				if (view instanceof Button) {
					// button,Date-->setText;
					if (iLVP.getDate() instanceof String) {
						((Button) view).setText(iLVP.getDate().toString());
					} else if (iLVP.getDate() instanceof Integer) {
						((Button) view).setBackgroundResource(Integer.parseInt(iLVP.getDate().toString()));
					}
				} else if (view instanceof ImageButton) {
					// ImageButton,Date-->setImageResource;
					// Log.e(TAG, "ImageButton " + iLVP.getDate().toString());
					((ImageButton) view).setImageResource(Integer.parseInt(iLVP.getDate().toString()));
				} else if (view instanceof TextView) {
					// TextView,Date-->setText;
					if (iLVP.getDate() instanceof Spanned) {
						((TextView) view).setText((Spanned) iLVP.getDate());
						((TextView) view).setMovementMethod(LinkMovementMethod.getInstance());
					} else {
						((TextView) view).setText(iLVP.getDate().toString());
					}
					
					if(iLVP.getTextViewPaintFlags()>-1){
						((TextView) view).getPaint().setFlags(iLVP.getTextViewPaintFlags()|Paint.ANTI_ALIAS_FLAG);
					}
					
				} else if (view instanceof CSpannedTextViewBase) {
					Log.e(TAG, "instanceof CSpannedTextViewBase");
					// TextView,Date-->setText;
					if (iLVP.getDate() != null) {
						// view.setVisibility(View.VISIBLE);
					}
					((CSpannedTextViewBase) view).setData(iLVP.getDate());
				} else if (view instanceof ImageView) {
					// ImageView,Date-->setImageResource;'
					ImageView imgView = (ImageView) view;
					imgView.setImageResource(Integer.parseInt(iLVP.getDate().toString()));

					if (iLVP.getImgAsync()) {
						Log.e(TAG, "getImgAsync:" + iLVP.getItemTag());
						if (iLVP.getItemTag() != null) {
							
							int resource = 0;
							if (iLVP.getDate() != null) {
								resource = (Integer) iLVP.getDate();
							}							
							UrlImageViewHelper.setUrlDrawable((ImageView) view, iLVP.getItemTag().toString(), resource);
						}
					}

					if (iLVP.getImgRoundCorner() > 0) {
						imgView.setDrawingCacheEnabled(true);
						Bitmap bitmap = imgView.getDrawingCache();
						imgView.setDrawingCacheEnabled(false);
						if (bitmap != null) {
							imgView.setImageBitmap(toRoundCorner(bitmap, iLVP.getImgRoundCorner()));
						} else {
							Log.e(TAG, "-----------------------bitmap is null");
						}
					}

				} else if (view instanceof ProgressBar) {
					((ProgressBar) view).setProgress(Integer.parseInt(iLVP.getDate().toString()));
				}
			}
		}
	}

	// 处理图像圆角
	public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = pixels;
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return output;
	}
}
