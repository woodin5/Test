package com.wmz.test.activity;

import java.io.File;
import java.util.HashMap;

import com.wmz.test.R;
import com.wmz.test.R.id;
import com.wmz.test.R.layout;
import com.wmz.test.manager.UploadManager;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

public class UploadFileActivity extends Activity implements OnClickListener {

	private String TAG = "UploadFileActivity";
	private WebView mWebViewUploadResult;
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				if (msg.obj != null) {
					Log.d(TAG, "wmz:msg=" + msg.obj.toString());
					mWebViewUploadResult.loadData(msg.obj.toString(),
							"text/html", "utf-8");
				}
				break;

			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_upload_file);

		Button btnUpload = (Button) findViewById(R.id.btn_upload);
		btnUpload.setOnClickListener(this);

		mWebViewUploadResult = (WebView) findViewById(R.id.webview_upload_result);
	}

	String url = "http://192.168.18.110:8080/Demo/UploadImage";
	String filename = Environment.getExternalStorageDirectory()
			.getAbsolutePath() + File.separator + "1.jpg";

	private void upload() {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("id", "65");
		UploadManager.upload(url, filename, mHandler, params, 0);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_upload:
			upload();
			break;
		}
	}

}
