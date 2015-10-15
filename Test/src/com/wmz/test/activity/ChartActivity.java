package com.wmz.test.activity;

import com.wmz.test.R;
import com.wmz.test.R.layout;
import com.wmz.test.view.ChartPieView;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.os.Build;

public class ChartActivity extends Activity implements OnClickListener {

	private String TAG = "ChartActivity";
	private Context mContext = ChartActivity.this;
	private LinearLayout mLayoutContainer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chart);

		initView();
	}

	private void initView() {
		mLayoutContainer = (LinearLayout) findViewById(R.id.layout_chart_container);
		findViewById(R.id.btn_chart_pie).setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.btn_chart_pie:
			initActivity();
			break;

		}
	}

	private void initActivity() {
		// 图表的使用方法:
		// 使用方式一:
		// 1.新增一个Activity
		// 2.新增一个View,继承Demo中的GraphicalView或DemoView都可，依Demo中View目录下例子绘制图表.
		// 3.将自定义的图表View放置入Activity对应的XML中，将指明其layout_width与layout_height大小.
		// 运行即可看到效果. 可参考非ChartsActivity的那几个图的例子，都是这种方式。

		// 使用方式二:
		// 代码调用 方式有下面二种方法:
		// 方法一:
		// 在xml中的FrameLayout下增加图表和ZoomControls,这是利用了现有的xml文件.
		// 1. 新增一个View，绘制图表.
		// 2. 通过下面的代码得到控件，addview即可
		// LayoutInflater factory = LayoutInflater.from(this);
		// View content = (View) factory.inflate(R.layout.activity_multi_touch,
		// null);

		// 方法二:
		// 完全动态创建,无须XML文件.
		FrameLayout content = new FrameLayout(this);

		// 缩放控件放置在FrameLayout的上层，用于放大缩小图表
		FrameLayout.LayoutParams frameParm = new FrameLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		frameParm.gravity = Gravity.BOTTOM | Gravity.RIGHT;

		/*
		 * //缩放控件放置在FrameLayout的上层，用于放大缩小图表 mZoomControls = new
		 * ZoomControls(this); mZoomControls.setIsZoomInEnabled(true);
		 * mZoomControls.setIsZoomOutEnabled(true);
		 * mZoomControls.setLayoutParams(frameParm);
		 */

		// 图表显示范围在占屏幕大小的90%的区域内
		DisplayMetrics dm = getResources().getDisplayMetrics();
		int scrWidth = (int) (dm.widthPixels * 0.9);
		int scrHeight = (int) (dm.heightPixels * 0.9);
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
				scrWidth, scrHeight);

		// 居中显示
		layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
		// 图表view放入布局中，也可直接将图表view放入Activity对应的xml文件中
		final RelativeLayout chartLayout = new RelativeLayout(this);

		chartLayout.addView(new ChartPieView(mContext), layoutParams);

		// 增加控件
		// ((ViewGroup) content).addView(chartLayout);
		// ((ViewGroup) content).addView(mZoomControls);
		mLayoutContainer.addView(chartLayout);
		// 放大监听
		// mZoomControls.setOnZoomInClickListener(new
		// OnZoomInClickListenerImpl());
		// 缩小监听
		// mZoomControls.setOnZoomOutClickListener(new
		// OnZoomOutClickListenerImpl());
	}
}
