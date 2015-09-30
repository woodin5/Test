package com.wmz.test.activity;

import java.io.File;
import java.io.FileFilter;
import java.util.HashMap;

import com.wmz.test.R;
import com.wmz.test.R.id;
import com.wmz.test.R.layout;
import com.wmz.test.fileselect.FileSelectDialog;
import com.wmz.test.fileselect.FileSelectedCallBackBundle;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.os.Build;

public class FileSelectActivity extends Activity implements OnClickListener {

	private String TAG = "FileSelectActivity";
	private Context mContext = FileSelectActivity.this;
	private Dialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_file_select);

		initView();
	}

	private void initView() {
		findViewById(R.id.btn_fileSelect).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_fileSelect:
			if (dialog == null) {
				FileFilter fileFilter = createFileFilter();
				dialog = FileSelectDialog.createDialog(mContext, "Select File",
						fileFilter, new FileSelectedCallBackBundle() {

							@Override
							public void onFileSelected(Bundle bundle) {
								String uri = bundle.getString("uri");
								Log.d(TAG, "wmz:uri=" + uri);
								dialog.dismiss();
							}
						});
			}
			dialog.show();
			break;

		default:
			break;
		}
	}

	private FileFilter createFileFilter() {
		final HashMap<String, String> supportTypeMap = new HashMap<String, String>();
		supportTypeMap.put("rmvb", "rmvb");
		supportTypeMap.put("mp4", "mp4");
		supportTypeMap.put("3gp", "3gp");
		supportTypeMap.put("mov", "mov");
		supportTypeMap.put("mkv", "mkv");
		supportTypeMap.put("ts", "ts");
		supportTypeMap.put("flv", "flv");
		supportTypeMap.put("asf", "asf");
		supportTypeMap.put("wmv", "wmv");
		supportTypeMap.put("rm", "rm");
		supportTypeMap.put("avi", "avi");
		supportTypeMap.put("f4v", "f4v");
		supportTypeMap.put("m3u8", "m3u8");
		supportTypeMap.put("ac3", "ac3");
		supportTypeMap.put("mpg", "mpg");
		supportTypeMap.put("vob", "vob");
		supportTypeMap.put("swf", "swf");

		FileFilter filter = new FileFilter() {

			@Override
			public boolean accept(File pathname) {
				if (pathname.isDirectory()) {
					return true;
				}

				String path = pathname.getName().toLowerCase();
				if (path != null) {
					int index = path.lastIndexOf('.');
					if (index != -1) {
						String suffix = path.substring(index + 1);
						if (supportTypeMap.get(suffix) != null) {
							return true;
						}
					}
				}
				return false;
			}
		};
		return filter;
	}
}
