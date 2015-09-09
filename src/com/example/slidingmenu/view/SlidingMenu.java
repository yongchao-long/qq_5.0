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
	//��Ļ�Ŀ��
	private int mScreenWidth;
	//�˵��Ŀ��
	private int mMenuWidth ;
	private int mHalfMenuWidth;
	private int mMenuRightPadding;
	private boolean once ;
	private boolean isOpen;
	
	/*
	 * δʹ���Զ�������ʱ����
	 */
	public SlidingMenu(Context context, AttributeSet attrs) {
		this(context, attrs,0);
		
		
		
	}
	/*
	 * ʹ���Զ�������ʱ����
	 */
	public SlidingMenu(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		//��ȡ�Զ�������
		TypedArray a = context.getTheme().obtainStyledAttributes(attrs,  R.styleable.SlidingMenu, defStyle, 0);
		//attr������
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
	 * ������view�Ŀ�͸ߣ������Լ��Ŀ�͸�
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
	 * ������view���õ�λ��
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
			//������صĿ��
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
	 * ��������ʱ
	 */
	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		// TODO Auto-generated method stub
		super.onScrollChanged(l, t, oldl, oldt);
		
		//l = getScrollX();
		//�������Զ���
		float scale = l * 1.0f / mMenuWidth;
		float Leftscale = 1.0f - 0.3f * scale;
		float Rightscale = 0.7f + 0.3f * scale;
		float Alpha = 0.6f + 0.4f * (1 - scale);
		
		//�˵���������
		mMenu.setScaleX(Leftscale);
		mMenu.setScaleY(Leftscale);
		//�˵�͸��������
		mMenu.setAlpha(Alpha);
		//�˵�ƫ��������
		mMenu.setTranslationX(mMenuWidth*scale*0.6f);
		
		//���������������ĵ�����
		mContent.setPivotX(0);
		mContent.setPivotY(mContent.getHeight() / 2);
		//��������
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
