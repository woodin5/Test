package com.wmz.test.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.wmz.test.R;
import com.wmz.test.R.id;
import com.wmz.test.R.layout;
import com.wmz.test.bean.UpgradBean;
import com.wmz.test.manager.UpgradManager;
import com.wmz.test.utils.JsonUtils;

public class DownApkActivity extends Activity implements OnClickListener {

	private String TAG = "DownApkActivity";
	private Context mContext = DownApkActivity.this;

	private UpgradManager mUpgradManager;
	private static final int HANDLER_UPGRAD = 0;
	private Button btnDownloadApk;
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case HANDLER_UPGRAD:
				if (msg.obj != null) {
					UpgradBean bean = JsonUtils
							.getUpgradBeanFromJsonByCheckVersion(msg.obj
									.toString());
					Log.d(TAG, "wmz:upgrad=" + bean);
					if (mUpgradManager.canUpgrad(bean)) {
						mUpgradManager.appUpgrad();
					}
				}
				break;

			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_down_apk);

		initView();
	}

	private void initView() {
		btnDownloadApk = (Button) findViewById(R.id.btn_downloadapk);
		btnDownloadApk.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_downloadapk:
//			downloadApk();
			Intent intent = new Intent(mContext,DownloadService.class);
			startService(intent); 
			break;

		default:
			break;
		}
	}

	private void downloadApk() {
		mUpgradManager = UpgradManager.getUpdateManager();
		mUpgradManager
				.checkAppUpdate(mContext, false, mHandler, HANDLER_UPGRAD);
	}
}
