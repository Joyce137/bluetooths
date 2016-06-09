package com.mslibs.utils;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout.LayoutParams;

public class CAnimation {

	public static void PanTO(final View moveView, View toView, int duration) {
		
		LayoutParams moveLayPms = (LayoutParams) moveView.getLayoutParams();
		//Log.e("CAnimation", "moveLayPms.left:"+moveLayPms.leftMargin+"  moveLayPms.top:"+moveLayPms.topMargin);
		
		LayoutParams toLayPms = (LayoutParams) toView.getLayoutParams();
		//Log.e("CAnimation", "toLayPms.left:"+toLayPms.leftMargin+"  toLayPms.top:"+toLayPms.topMargin);
		
		int xoffset = (toLayPms.leftMargin +toLayPms.width/2) - (moveLayPms.leftMargin + moveLayPms.width/2);
		int yoffset = (toLayPms.topMargin +toLayPms.height/2) - (moveLayPms.topMargin + moveLayPms.height/2);

		//Log.e("CAnimation", "xoffset:"+xoffset+"  yoffset:"+yoffset);
		
		final LayoutParams newLayPms = new LayoutParams(moveLayPms.width, moveLayPms.height);
		newLayPms.leftMargin = moveLayPms.leftMargin+xoffset;
		newLayPms.topMargin = moveLayPms.topMargin+yoffset;
		//Log.e("CAnimation", "newLayPms.left:"+newLayPms.leftMargin+"  newLayPms.top:"+newLayPms.topMargin);
		
		moveView.clearAnimation();
		
		Animation anim = new TranslateAnimation(0, xoffset, 0, yoffset);
		anim.setDuration(duration);// 设置动画持续时间
		anim.setRepeatCount(0);// 设置重复次数
		
		anim.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				// TranslateAnimation anim = new TranslateAnimation(0,0,0,0);
				// view.setAnimation(anim);
				moveView.clearAnimation();
				moveView.setLayoutParams(newLayPms);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub

			}

		});
		
		moveView.startAnimation(anim);
	}
	
	public static void PanTO(final View moveView, LayoutParams toLayPms, int duration) {
		
		LayoutParams moveLayPms = (LayoutParams) moveView.getLayoutParams();
		//Log.e("CAnimation", "moveLayPms.left:"+moveLayPms.leftMargin+"  moveLayPms.top:"+moveLayPms.topMargin);
		
		
		int xoffset = (toLayPms.leftMargin +toLayPms.width/2) - (moveLayPms.leftMargin + moveLayPms.width/2);
		int yoffset = (toLayPms.topMargin +toLayPms.height/2) - (moveLayPms.topMargin + moveLayPms.height/2);

		//Log.e("CAnimation", "xoffset:"+xoffset+"  yoffset:"+yoffset);
		
		final LayoutParams newLayPms = toLayPms;
		
		moveView.clearAnimation();
		
		Animation anim = new TranslateAnimation(0, xoffset, 0, yoffset);
		anim.setDuration(duration);// 设置动画持续时间
		anim.setRepeatCount(0);// 设置重复次数
		
		anim.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				// TranslateAnimation anim = new TranslateAnimation(0,0,0,0);
				// view.setAnimation(anim);
				moveView.clearAnimation();
				moveView.setLayoutParams(newLayPms);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub

			}

		});
		
		moveView.startAnimation(anim);
	}
}
