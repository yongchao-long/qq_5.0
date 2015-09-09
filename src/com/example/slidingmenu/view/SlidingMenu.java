package com.example.slidingmenu.view;

import com.example.slidingmenu.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

public class SlidingMenu extends HorizontalScrollView {
	
	private LinearLayout mWapper;
	private ViewGroup mMenu;
	private ViewGroup mContent;
	//屏幕的宽度
	private int mScreenWidth;
	//菜单的宽度
	private int mMenuWidth ;
	private int mHalfMenuWidth;
	private int mMenuRightPadding;
	private boolean once ;
	private boolean isOpen;
	
	/*
	 * 未使用自定义属性时调用
	 */
	public SlidingMenu(Context context, AttributeSet attrs) {
		this(context, attrs,0);
		
		
		
	}
	/*
	 * 使用自定义属性时调用
	 */
	public SlidingMenu(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		//获取自定义属性
		TypedArray a = context.getTheme().obtainStyledAttributes(attrs,  R.styleable.SlidingMenu, defStyle, 0);
		//attr的数量
		int n = a.getIndexCount();
		for(int i=0;i<n;i++){
			int attr = a.getIndex(i);
			switch (attr) {
			case R.styleable.SlidingMenu_rightPadding:
				mMenuRightPadding = a.getDimensionPixelOffset(attr, (int) TypedValue.
						applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, 
								context.getResources().getDisplayMetrics()));
				break;

			default:
				break;
			}
		}
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics outMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics);
		mScreenWidth = outMetrics.widthPixels;
		
		//dp to px
		//mMenuRightPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, context.getResources().getDisplayMetrics());
	}

	public SlidingMenu(Context context) {
		this(context,null);
		// TODO Auto-generated constructor stub
	}

	/*
	 * 设置子view的宽和高，设置自己的宽和高
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		if(!once){
		mWapper = (LinearLayout) getChildAt(0);
		mMenu = (ViewGroup) mWapper.getChildAt(0);
		mContent = (ViewGroup) mWapper.getChildAt(1);
		
		mMenuWidth = mScreenWidth - mMenuRightPadding;
		mMenu.getLayoutParams().width = mMenuWidth;
		mHalfMenuWidth = mMenuWidth /2;
		mContent.getLayoutParams().width = mScreenWidth;
		
		}
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
  
	/*
	 * 设置子view放置的位置
	 */
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		if(changed){
			this.scrollTo( mMenuWidth, 0);
			once = true;
		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent ev) 
	{
		int action = ev.getAction();
		switch(action){
		case MotionEvent.ACTION_UP:
			//左边隐藏的宽度
			int scrollX = getScrollX();
			if(scrollX >= mHalfMenuWidth){
				this.smoothScrollTo(mMenuWidth, 0);
				isOpen = false;
			}
			else{
				this.smoothScrollTo(0, 0);
				isOpen = true;
			}
			return true;
			
		}
		return super.onTouchEvent(ev); 
    }  
	
	/*
	 * 滚动发生时
	 */
	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		// TODO Auto-generated method stub
		super.onScrollChanged(l, t, oldl, oldt);
		
		//l = getScrollX();
		//设置属性动画
		float scale = l * 1.0f / mMenuWidth;
		float Leftscale = 1.0f - 0.3f * scale;
		float Rightscale = 0.7f + 0.3f * scale;
		float Alpha = 0.6f + 0.4f * (1 - scale);
		
		//菜单缩放设置
		mMenu.setScaleX(Leftscale);
		mMenu.setScaleY(Leftscale);
		//菜单透明度设置
		mMenu.setAlpha(Alpha);
		//菜单偏移量动画
		mMenu.setTranslationX(mMenuWidth*scale*0.6f);
		
		//内容区域缩放中心点设置
		mContent.setPivotX(0);
		mContent.setPivotY(mContent.getHeight() / 2);
		//缩放设置
		mContent.setScaleX(Rightscale);
		mContent.setScaleY(Rightscale);
		
		
	}
	
	
	
	public void openMenu(){
		
		if(isOpen) return;
		this.smoothScrollTo(0, 0);
		isOpen = true;
	}
	
	public void closeMenu(){
		
		if(!isOpen) return;
		this.smoothScrollTo(mMenuWidth, 0);
		isOpen = false;
	}
	
	public void toggle(){
		
		if(isOpen){
			closeMenu();
		}
		else{
			openMenu();
		}
	}
  
}
