package com.wmz.test.activity;

import com.wmz.test.R;
import com.wmz.test.R.drawable;
import com.wmz.test.R.id;
import com.wmz.test.R.layout;
import com.wmz.test.utils.ImageUtils;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class ImageViewActivity extends Activity implements OnClickListener {

	private String TAG = "ImageViewActivity";
	private Context mContext = ImageViewActivity.this;
	private ImageView mImgShow;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_view);
		initView();
	}

	private void initView() {
		mImgShow = (ImageView) findViewById(R.id.img_imageView_show);
		findViewById(R.id.btn_imageView_).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_imageView_:
			showImage();
			break;

		}
	}

	private void showImage() {
		Drawable drawable = getResources().getDrawable(R.drawable.ic_launcher);
		mImgShow.setImageDrawable(ImageUtils.getUnreadedDrawable(drawable,
				getResources(), 5));
	}
}
