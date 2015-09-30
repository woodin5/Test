package com.wmz.test.activity;

import com.wmz.test.R;
import com.wmz.test.R.id;
import com.wmz.test.R.layout;
import com.wmz.test.utils.NetUtils;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.os.Build;

public class NetActivity extends Activity implements OnClickListener {

	private String TAG = "NetActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_net);

		initView();

	}

	private void initView() {
		findViewById(R.id.btn_net_get).setOnClickListener(this);
		findViewById(R.id.btn_net_post).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_net_get:

			netGet();
			break;

		case R.id.btn_net_post:
			netPost();

			break;
		}
	}

	private void netGet() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				String url = "http://192.168.18.187:8080/Demo/Login";
				String content = NetUtils
						.getStringFromHttpURLConnectionByGet(url);
				Log.d(TAG, "wmz:" + content);
			}
		}).start();
	}

	private void netPost() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				String url = "http://192.168.18.187:8080/Demo/Login";
				String params = "username=1&password=1";
				String content = NetUtils.getStringFromHttpURLConnectionByPost(
						url, params);
				Log.d(TAG, "wmz:content=" + content);
			}
		}).start();
	}

}
