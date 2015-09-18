package com.wmz.test.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;


public class SlideTextView extends TextView implements Runnable {
	private int currentScrollX;// ��ǰ������λ��
	private boolean isStop = false;
	private int textWidth;
	private boolean isMeasure = false;

	public SlideTextView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public SlideTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public SlideTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub

		super.onDraw(canvas);
		if (!isMeasure) {// ���ֿ��ֻ���ȡһ�ξͿ�����
			getTextWidth();
			isMeasure = true;
		}
		Log.d("", "wmz:textWidth=" + textWidth);
	}

	/**
	 * ��ȡ���ֿ��
	 */
	private void getTextWidth() {
		Paint paint = this.getPaint();
		String str = this.getText().toString();
		textWidth = (int) paint.measureText(str);
	}

	@Override
	public void run() {
		Log.d("", "wmz:run-currentScrollX="+currentScrollX); 
		currentScrollX -= 1;// �����ٶ�
		scrollTo(currentScrollX, 0);
		if (isStop) {
			return;
		}
		if (getScrollX() <= -(this.getWidth())) {
			scrollTo(textWidth, 0);
			currentScrollX = textWidth;
			// return;	
		}
		postDelayed(this, 50);
	}

	// ��ʼ����
	public void startScroll() {
		isStop = false;
		this.removeCallbacks(this);
		post(this);
	}

	// ֹͣ����
	public void stopScroll() {
		isStop = true;
	}

	// ��ͷ��ʼ����
	public void startFor0() {
		currentScrollX = 0;
		startScroll();
	}
}