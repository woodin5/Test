package com.wmz.test.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.wmz.test.R;
import com.wmz.test.db.SqliteDatabaseUtils;
import com.wmz.test.manager.ExecutorSeviceManager;
import com.wmz.test.utils.CursorUtils;
import com.wmz.test.utils.EnvironmentUtils;

import docom.sdk.client.ClsCamera;
import docom.sdk.client.ClsDocomSDK;
import docom.sdk.client.ClsDocomSDK.Connection;
import docom.sdk.client.ClsDocomSDK.MsgListener;
import docom.sdk.client.ClsFingerDev;

public class GateActivity extends Activity implements OnClickListener {

	private String TAG = "GateActivity";
	private Context mContext = GateActivity.this;
	private ClsDocomSDK mClsDocomSDK;
	private ClsCamera mClsCamera;
	private ClsFingerDev mClsFingerDev;
	private byte[] tmp2 = new byte[1024], tmp1 = new byte[1024];
	private int len1, len2, mMatchSource;
	private List<String> mFingerprintList;

	private TextView mText_show;
	private boolean isMatch = false;
	private long startTime;
	private ImageView mImageView;
	private boolean isGate = true;

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:

				Log.d(TAG, "wmz:0:time="
						+ (System.currentTimeMillis() - startTime));
				break;

			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gate);

		isGate = true;
		initSdk();
		initView();
	}

	private void initView() {
		findViewById(R.id.btn_openA).setOnClickListener(this);
		findViewById(R.id.btn_openB).setOnClickListener(this);
		findViewById(R.id.btn_getFinger1).setOnClickListener(this);
		findViewById(R.id.btn_getFinger2).setOnClickListener(this);
		findViewById(R.id.btn_match_finger12).setOnClickListener(this);
		findViewById(R.id.btn_match_finger).setOnClickListener(this);

		mImageView = (ImageView) findViewById(R.id.img_finger);

		mText_show = (TextView) findViewById(R.id.text_show);
	}

	private void initSdk() {
		if (isGate) {
			mClsDocomSDK = new ClsDocomSDK(mContext);

			mClsDocomSDK.bindService(new Connection() {

				@Override
				public void onDisConnected() {

				}

				@Override
				public void onConnected() {

					mClsDocomSDK.SetMsgListener("MSG,PAS,TCKPAS,FNG,SHW",
							new MsgListener() {

								@Override
								public void onMessage(String type,
										String message) {
									Log.d(TAG, "wmz:type=" + type + ",message="
											+ message);
									mText_show.setText("type=" + type
											+ ",message=" + message);
								}
							});
				}
			});
			setSdkListener();
		} else {
			createFingerPrintDB1();
		}
	}

	private void setSdkListener() {
		mClsFingerDev = mClsDocomSDK.GetFinger();
		Log.d(TAG, "wmz:ClsFingerDev-type=" + mClsFingerDev.GetDevType());
		createFingerPrintDB();
	}

	private void createFingerPrintDB() {
		String path = "/ext_sd/fingerprint.db3";
		String sql = "create table table_fingerprint(_id INTEGER PRIMARY KEY AUTOINCREMENT, fingerprint_length integer,fingerprint varchar)";
		boolean isCreateTable = new File(path).exists() ? false : true;
		SqliteDatabaseUtils.openOrCreateDatabase(path);
		if (isCreateTable) {
			SqliteDatabaseUtils.execSQL(sql);
		}

	}

	private void createFingerPrintDB1() {
		String path = EnvironmentUtils.getExternalStoragePath()
				+ File.separator + "fingerprint.db3";
		String sql = "create table table_fingerprint(_id INTEGER PRIMARY KEY AUTOINCREMENT, fingerprint_length integer,fingerprint varchar)";
		boolean isCreateTable = new File(path).exists() ? false : true;
		SqliteDatabaseUtils.openOrCreateDatabase(path);
		if (isCreateTable) {
			SqliteDatabaseUtils.execSQL(sql);
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_openA:
			mClsDocomSDK.OpenGate("A", 1);
			break;
		case R.id.btn_openB:
			mClsDocomSDK.OpenGate("B", 1);
			break;
		case R.id.btn_getFinger1:
			len1 = mClsFingerDev.GetFingerTmp(tmp1, 1000);
			// Bitmap bm = BitmapFactory.decodeByteArray(tmp1, 0, len1);
			// mImageView.setImageBitmap(bm);

			// new Thread(new Runnable() {
			//
			// @Override
			// public void run() {
			// String url = "http://192.168.18.187:8080/Demo/ReceiveFinger";
			// String params = "finger="
			// + Base64.encodeToString(tmp1, Base64.DEFAULT);
			//
			// String content = NetUtils
			// .getStringFromHttpURLConnectionByPost(url, params);
			// Log.d(TAG, "wmz:content=" + content);
			// }
			// }).start();

			SqliteDatabaseUtils.execSQL(
					"insert into table_fingerprint values(null,?,?)",
					new Object[] { len1,
							Base64.encodeToString(tmp1, Base64.DEFAULT) });

			new Thread(new Runnable() {

				@Override
				public void run() {
					startTime = System.currentTimeMillis();
					for (int i = 0; i < 199; i++) {
						tmp1[len1] = (byte) i;
						SqliteDatabaseUtils
								.execSQL(
										"insert into table_fingerprint values(null,?,?)",
										new Object[] {
												len1 + 1,
												Base64.encodeToString(tmp1,
														Base64.DEFAULT) });
					}

					Log.d(TAG, "wmz:1-time="
							+ (System.currentTimeMillis() - startTime));

				}
			});

			break;
		case R.id.btn_getFinger2:
			len2 = mClsFingerDev.GetFingerTmp(tmp2, 1000);

			ContentValues values = new ContentValues();
			values.put("fingerprint_length", len2);
			values.put("fingerprint",
					Base64.encodeToString(tmp2, Base64.DEFAULT));
			SqliteDatabaseUtils.insert("table_fingerprint", values);
			new Thread(new Runnable() {

				@Override
				public void run() {
					startTime = System.currentTimeMillis();
					SqliteDatabaseUtils.db.beginTransaction();
					for (int i = 0; i < 1999; i++) {
						tmp1[len2] = (byte) i;
						ContentValues values = new ContentValues();
						values.put("fingerprint_length", len2 + 1);
						values.put("fingerprint",
								Base64.encodeToString(tmp2, Base64.DEFAULT));
						SqliteDatabaseUtils.insert("table_fingerprint", values);
					}
					SqliteDatabaseUtils.db.setTransactionSuccessful();
					SqliteDatabaseUtils.db.endTransaction();
					Log.d(TAG, "wmz:2-time="
							+ (System.currentTimeMillis() - startTime));

				}
			});

			// SqliteDatabaseUtils.execSQL(
			// "insert into table_fingerprint values(null,?,?)",
			// new Object[] { len2,
			// Base64.encodeToString(tmp2, Base64.DEFAULT) });

			break;
		case R.id.btn_match_finger12:
			startTime = System.currentTimeMillis();
			mMatchSource = mClsFingerDev.Match(tmp1, len1, tmp2, len2);
			// int count = SqliteDatabaseUtils.getCount();
			// Log.d(TAG, "wmz:count=" + count);
			Log.d(TAG, "wmz:12-mMatchSouce=" + mMatchSource);
			Log.d(TAG, "wmz:time=" + (System.currentTimeMillis() - startTime));
			mText_show.setText("对比分数="+mMatchSource+",花费时间="+(System.currentTimeMillis() - startTime));
			break;
		case R.id.btn_match_finger:
			isMatch = false; 
			startTime = System.currentTimeMillis();
			len2 = mClsFingerDev.GetFingerTmp(tmp2, 1000);

			ArrayList<ReadRunnable> list = new ArrayList<GateActivity.ReadRunnable>();
			for (int i = 0; i < 1; i++) {
				list.add(new ReadRunnable(i, SqliteDatabaseUtils.getCount()));
			}

			startThreads(list);

			break;

		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	class ReadRunnable implements Runnable {
		AtomicInteger count = new AtomicInteger(0);
		private int readCount;
		private int index;

		ReadRunnable(int index, int readCount) {
			this.index = index;
			this.readCount = readCount;
		}

		@Override
		public void run() {

			Cursor cursor = SqliteDatabaseUtils.rawQuery(
					"select * from table_fingerprint limit ?,?", new String[] {
							index + "", readCount + "" });
			List<String> list = CursorUtils.getList(cursor, "fingerprint");
			startTime = System.currentTimeMillis();
			if (list != null && list.size() > 0) {
				Log.d(TAG, "wmz:index=" + index + ",list.size=" + list.size());
				int i = 0;
				while (i < list.size() && !isMatch) {

					byte[] data = Base64.decode(list.get(i), Base64.DEFAULT);

					mMatchSource = mClsFingerDev.Match(data, data.length, tmp2,
							len2);
					if (mMatchSource >= 20000) {
						Log.d(TAG, "wzm:match");
						mHandler.sendEmptyMessage(0);
						isMatch = true;
						break;
					} else {
						Log.d(TAG, "wmz:time="
								+ (System.currentTimeMillis() - startTime));
					}
					Log.d(TAG, "wmz:index=" + index + ",i=" + ++i);
				}
			}
			// Log.d(TAG, "wmz:time=" + (System.currentTimeMillis() -
			// startTime));

		}
	}

	void startThreads(Collection<? extends Runnable> rs) {
		for (Runnable r : rs) {
			ExecutorSeviceManager.getExecutorInstance().execute(r);
		}
	}

}
