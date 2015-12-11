package com.wmz.test.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.wmz.test.R;
import com.wmz.test.fileselect.FileSelectDialog;

import docom.sdk.client.ClsSerialPort;

public class ListActivity extends android.app.ListActivity {

	private String[] strs = { "TestGate", "face", "chart", "volley",
			"fileSelect", "player", "recyclerView", "imageView", "device",
			"net", "download", "sqlite", "webview", "file", "upload", "slide",
			"dialog" };
	private Class<?>[] classs = { GateActivity.class, FaceActivity.class,
			ChartActivity.class, VolleyActivity.class,
			FileSelectActivity.class, PlayerActivity.class,
			RecyclerViewActivity.class, ImageViewActivity.class,
			DeviceActivity.class, NetActivity.class, DownloadActivity.class,
			SQLiteActivity.class, WebViewActivity.class, FileActivity.class,
			UploadFileActivity.class, SlideTextViewActivity.class,
			DialogCancelActivity.class };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list);
		setListAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, strs));
		getListView().setTextFilterEnabled(true);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Intent intent = new Intent(this, classs[position]);
		startActivity(intent);
	}

}
