package com.wmz.test.activity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.wmz.test.FileActivity;
import com.wmz.test.R;
import com.wmz.test.SQLiteActivity;
import com.wmz.test.R.layout;
import com.wmz.test.utils.FileUtils;
import com.wmz.test.utils.SystemUtils;

public class ListActivity extends android.app.ListActivity {

	private String[] strs = { "sqlite", "webview", "file", "upload", "slide",
			"dialog", "TestGate" };
	private Class<?>[] classs = { SQLiteActivity.class, WebViewActivity.class,
			FileActivity.class, UploadFileActivity.class,
			SlideTextViewActivity.class, DialogCancelActivity.class,
			GateActivity.class };

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
