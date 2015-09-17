package com.wmz.test.activity;

import android.app.Activity;
import android.os.Bundle;

import com.wmz.test.R;
import com.wmz.test.view.SlideTextView;

public class SlideTextViewActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_slide_text_view);

		SlideTextView textView = (SlideTextView) findViewById(R.id.text_slide);
		textView.setText("123432432");
		// textView.startScroll();


	}

}
