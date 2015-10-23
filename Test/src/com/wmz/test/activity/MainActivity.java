package com.wmz.test.activity;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo.DisplayNameComparator;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class MainActivity extends ListActivity {

	private String TAG = "MainActivity";
	private Context mContext = MainActivity.this;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent intent = getIntent();
		String path = intent.getStringExtra("com.wmz.test.list.path");
		if (path == null) {
			path = "";
		}

		setListAdapter(new SimpleAdapter(mContext, getData(path),
				android.R.layout.simple_list_item_1, new String[] { "title" },
				new int[] { android.R.id.text1 }));
		getListView().setTextFilterEnabled(true);
	}

	private List<? extends Map<String, ?>> getData(String prefix) {
		List<Map<String, Object>> mData = new ArrayList<>();
		Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
		mainIntent.addCategory("com.wmz.test.list");

		PackageManager pManager = getPackageManager();
		List<ResolveInfo> infos = pManager.queryIntentActivities(mainIntent, 0);
		if (infos == null) {
			return mData;
		}
		String[] prefixPath;
		String prefixWithSlash = prefix;
		if (prefix.equals("")) {
			prefixPath = null;
		} else {
			prefixPath = prefix.split("/");
			prefixWithSlash = prefix + "/";
		}
		int len = infos.size();
		Map<String, Boolean> entries = new HashMap<>();
		for (int i = 0; i < len; i++) {
			ResolveInfo info = infos.get(i);
			CharSequence labelSeq = info.loadLabel(pManager);
			Log.d(TAG, "wmz:labelSeq=" + labelSeq.toString() + ",name="
					+ info.activityInfo.name);
			String label = labelSeq != null ? labelSeq.toString()
					: info.activityInfo.name;
			if (prefixWithSlash.length() == 0
					|| label.endsWith(prefixWithSlash)) {
				String[] labelPath = label.split("/");
				String nextLabel = prefixPath == null ? labelPath[0]
						: labelPath[prefixPath.length];
				if ((prefixPath != null ? labelPath.length : 0) == labelPath.length - 1) {
					addItem(mData,
							nextLabel,
							activityIntent(
									info.activityInfo.applicationInfo.packageName,
									info.activityInfo.name));
				} else {
					if (entries.get(nextLabel) != null) {
						addItem(mData, nextLabel,
								browseIntent(prefix.equals("") ? nextLabel
										: prefix + "/" + nextLabel));
						entries.put(nextLabel, true);
					}
				}
			}
		}
		Collections.sort(mData, mDisplayNameComparator);
		return mData;
	}

	private final static Comparator<Map<String, Object>> mDisplayNameComparator = new Comparator<Map<String, Object>>() {
		final Collator collator = Collator.getInstance();

		@Override
		public int compare(Map<String, Object> map1, Map<String, Object> map2) {
			return collator.compare(map1.get("title"), map2.get("title"));
		}
	};

	private Intent browseIntent(String path) {
		Intent intent = new Intent();
		intent.setClass(mContext, MainActivity.class);
		intent.putExtra("com.wmz.test.list.path", path);
		return intent;
	}

	private void addItem(List<Map<String, Object>> mData, String nextLabel,
			Intent activityIntent) {
		Map<String, Object> temp = new HashMap<>();
		temp.put("title", nextLabel);
		temp.put("intent", activityIntent);
		mData.add(temp);
	}

	private Intent activityIntent(String packageName, String name) {
		Intent intent = new Intent();
		intent.setClassName(packageName, name);
		return intent;
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Map<String, Object> map = (Map<String, Object>) l
				.getItemAtPosition(position);
		Intent intent = (Intent) map.get("intent");
		startActivity(intent);
	}
}
