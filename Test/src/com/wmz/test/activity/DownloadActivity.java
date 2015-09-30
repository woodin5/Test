package com.wmz.test.activity;

import java.io.File;
import java.io.InputStream;

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
import com.wmz.test.manager.DownloadManager;
import com.wmz.test.manager.UpgradManager;
import com.wmz.test.utils.EnvironmentUtils;
import com.wmz.test.utils.FileUtils;
import com.wmz.test.utils.JsonUtils;
import com.wmz.test.utils.NetUtils;

public class DownloadActivity extends Activity implements OnClickListener {

	private String TAG = "DownApkActivity";
	private Context mContext = DownloadActivity.this;

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
			case 1:
				Log.d(TAG, "wmz:1" + msg.obj);
				break;
			case 2:
				Log.d(TAG, "wmz:2" + msg.obj);
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
		btnDownloadApk = (Button) findViewById(R.id.btn_download_apk);
		btnDownloadApk.setOnClickListener(this);

		findViewById(R.id.btn_download_file).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_download_apk:
			// downloadApk();
			Intent intent = new Intent(mContext, DownloadService.class);
			startService(intent);
			break;

		case R.id.btn_download_file:
			downloadFile();
			break;
		default:
			break;
		}
	}

	private void downloadFile() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				String url = "http://192.168.18.200:8080/Demo/ShowImage";
				String filePath = EnvironmentUtils.getExternalStoragePath()
						+ File.separator + "wmz.jpg";

				InputStream is = NetUtils
						.getInputStreamFromHttpURLConnectionByGet(url);
				boolean isOk = FileUtils.writeFile(filePath, is);
				Log.d(TAG, "wmz:isOk=" + isOk);
			}
		}).start();

	}

	private void downloadApk() {
		String url = "http://192.168.18.200:8080/Demo/DownloadApk";
		String filePath = EnvironmentUtils.getExternalStoragePath()
				+ File.separator + "wmz.apk";
		DownloadManager.downloadByxUtils(url, filePath, mHandler, 1, 2);
	}
}
