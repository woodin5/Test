package com.wmz.test.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.wmz.test.R;

import docom.sdk.client.ClsSerialPort;

public class ListActivity extends android.app.ListActivity {

	private String[] strs = { "download", "sqlite", "webview", "file",
			"upload", "slide", "dialog", "TestGate" };
	private Class<?>[] classs = { DownloadActivity.class, SQLiteActivity.class,
			WebViewActivity.class, FileActivity.class,
			UploadFileActivity.class, SlideTextViewActivity.class,
			DialogCancelActivity.class, GateActivity.class };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list);
		setListAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, strs));

	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		Intent intent = new Intent(this, classs[position]);
		startActivity(intent);
	}

}
