package com.wmz.test.activity;

import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.wmz.test.R;
import com.wmz.test.utils.EnvironmentUtils;
import com.wmz.test.utils.FileUtils;

public class FileActivity extends Activity implements OnClickListener {

	private String TAG = "FileActivity";
	private Context mContext = FileActivity.this;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_file);

		initView();
	}

	private void initView() {
		findViewById(R.id.btn_file_write).setOnClickListener(this);
		findViewById(R.id.btn_file_read).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_file_write:
			// FileUtils.write(mContext, "wmz_1", "wmz_1");
			FileUtils.writeFile(mContext,
					EnvironmentUtils.getExternalStoragePath() + File.separator
							+ "wmz_2.txt", "wmz_2\n".getBytes());
			break;

		case R.id.btn_file_read:

			break;
		}
	}

}
