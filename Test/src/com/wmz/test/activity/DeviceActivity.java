package com.wmz.test.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.wmz.test.R;
import com.wmz.test.helper.DeviceHelper;
import com.wmz.test.utils.SystemUtils;

public class DeviceActivity extends Activity implements OnClickListener {

	private String TAG = "DeviceActivity";
	private Context mContext = DeviceActivity.this;
	private TextView mTextShow;
	private DeviceHelper mDeviceHelper;
	private StringBuffer mStringBuffer = new StringBuffer();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_device);

		initView();
	}

	private void initView() {
		mTextShow = (TextView) findViewById(R.id.text_device_show);
		findViewById(R.id.btn_device_info).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_device_info:
			showDeviceInfo();
			break;

		default:
			break;
		}
	}

	private void showDeviceInfo() {
		mDeviceHelper = DeviceHelper.getInstance(mContext);
		mStringBuffer.append(mDeviceHelper.getNetType()).append("\n")
				.append(mDeviceHelper.getNetwrokIso()).append("\n")
				.append(mDeviceHelper.getDeviceId(mContext)).append("\n")
				.append(mDeviceHelper.getDeviceInfo());
		mTextShow.setText(mStringBuffer.toString());
	}

}
