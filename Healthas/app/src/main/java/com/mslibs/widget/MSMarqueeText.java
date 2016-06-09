package com.mslibs.widget;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.TextView;

public class MSMarqueeText extends TextView {
	public MSMarqueeText(Context con) {
		super(con);
	}

	public MSMarqueeText(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	public MSMarqueeText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	@Override
	public boolean isFocused() {
		return true;
	}
	@Override
	protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
	}
}
