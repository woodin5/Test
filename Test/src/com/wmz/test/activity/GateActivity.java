package com.wmz.test.activity;

import java.io.File;
import java.util.List;

import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.wmz.test.R;
import com.wmz.test.R.id;
import com.wmz.test.R.layout;
import com.wmz.test.db.SqliteDatabaseUtils;
import com.wmz.test.utils.CursorUtils;
import com.wmz.test.utils.FileUtils;

import docom.sdk.client.ClsCamera;
import docom.sdk.client.ClsDocomSDK;
import docom.sdk.client.ClsFingerDev;
import docom.sdk.client.ClsDocomSDK.Connection;
import docom.sdk.client.ClsDocomSDK.MsgListener;

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gate);

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

		mText_show = (TextView) findViewById(R.id.text_show);
	}

	private void initSdk() {
		mClsDocomSDK = new ClsDocomSDK(mContext);
		/*
		 * 绑定SDK服务
		 * 
		 * 参数： connection - 连接成功后的回调接口
		 */
		mClsDocomSDK.bindService(new Connection() {

			@Override
			public void onDisConnected() {

			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see docom.sdk.client.ClsDocomSDK.Connection#onConnected()
			 */

			@Override
			public void onConnected() {
				/*
				 * 设置消息监听回调
				 * 
				 * 参数： type - 以下消息类型的组合,逗号','分隔. . MSG - 韦根,串口等输入消息 . PAS - *
				 * 过闸回程信号消息 . TCKPAS - 刷票过闸回程信号消息 . FNG - 指纹采集消息 . SHW - 显示消息
				 * listener - 消息回调接口
				 */
				mClsDocomSDK.SetMsgListener("MSG,PAS,TCKPAS,FNG,SHW",
						new MsgListener() {
							/*
							 * 参数： type - 消息类型. . MSG - 韦根,串口等输入消息 . PAS -
							 * 过闸回程信号消息 . * TCKPAS - 刷票过闸回程信号消息 . FNG - 指纹采集消息 .
							 * SHW - 显示消息 message - * 消息内容,内容与type消息类型相关 .
							 * type="MSG" - * 方向+端口号,消息内容,如:"IW1,12345678" . 方向:
							 * I-入口, O-出口 . 端口号： * C1-n:串口,U1-n:USB,K1-n:矩阵键盘,
							 * W1-n: 维根 . type="PAS" - * "A":A向回程信号,"B":B向回程信号 .
							 * type="TCKPAS" - * "票号,时间,过闸方向,过闸消息". .
							 * 时间:yyyy-MM-dd HH:mm:ss格式 . 过闸方向: A或B .
							 * 过闸消息:0-验证超时,1-验证失败,2-过闸超时,3-已过闸 . type="FNG" - *
							 * "指纹仪型号,指纹信息" . 指纹仪型号: 82:美国指纹仪, 84:国产指纹仪 . 指纹信息:
							 * * X16^nnnn,
							 * nnnn十六进制数的字符串格式.如0x12,0x34表示为"X16^1234" . *
							 * type="SHW" - "门票来源,门票类型,可入园人数,已入园人数,有效日期,验证过程".
							 * (non-Javadoc)
							 * 
							 * @see
							 * docom.sdk.client.ClsDocomSDK.MsgListener#onMessage
							 * (java .lang.String, java.lang.String)
							 */
							@Override
							public void onMessage(String type, String message) {
								Log.d(TAG, "wmz:type=" + type + ",message="
										+ message);
								mText_show.setText("type=" + type + ",message="
										+ message);
							}
						});
			}
		});
		setSdkListener();
	}

	private void setSdkListener() {
		mClsFingerDev = mClsDocomSDK.GetFinger();
		createFingerPrintDB();
		Log.d(TAG, "wmz:ClsFingerDev-type=" + mClsFingerDev.GetDevType());
	}

	private void createFingerPrintDB() {
		String path = "/ext_sd/fingerprint.db3";
		String sql = "create table table_fingerprint(_id INTEGER PRIMARY KEY AUTOINCREMENT, fingerprint_length integer,fingerprint varchar)";
		boolean isCreateTable = new File(path).exists() ? false : true;
		SqliteDatabaseUtils.openOrCreateDatabase(path);
		if (isCreateTable) {
			SqliteDatabaseUtils.execSQL(sql);
		}
		mFingerprintList = CursorUtils.getList(SqliteDatabaseUtils
				.rawQuery("select fingerprint from table_fingerprint"),
				"fingerprint");

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
			SqliteDatabaseUtils.execSQL(
					"insert into table_fingerprint values(null,?,?)",
					new Object[] { len1,
							Base64.encodeToString(tmp1, Base64.DEFAULT) });
			break;
		case R.id.btn_getFinger2:
			len2 = mClsFingerDev.GetFingerTmp(tmp2, 1000);
			SqliteDatabaseUtils
					.execSQL("insert into table_fingerprint values(null,"
							+ len2 + ","
							+ Base64.encodeToString(tmp2, Base64.DEFAULT) + ")");

			break;
		case R.id.btn_match_finger12:
			mMatchSource = mClsFingerDev.Match(tmp1, len1, tmp2, len2);
			mText_show.setText(mMatchSource);
			Log.d(TAG, "wmz:12-mMatchSouce=" + mMatchSource);
			break;
		case R.id.btn_match_finger:

			len2 = mClsFingerDev.GetFingerTmp(tmp2, 1000);
			Long startTime = System.currentTimeMillis();
			if (mFingerprintList != null && mFingerprintList.size() > 0) {
				Log.d(TAG, "wmz:mFingerprintList=" + mFingerprintList.size());
				for (int i = 0; i < mFingerprintList.size(); i++) {
					byte[] data = Base64.decode(mFingerprintList.get(i),
							Base64.DEFAULT);
					mMatchSource = mClsFingerDev.Match(data, data.length, tmp2,
							len2);
					if (mMatchSource >= 20000) {
						mText_show.setText("配对分数=" + mMatchSource + "第" + i
								+ "次配对成功，花费时间="
								+ (System.currentTimeMillis() - startTime));
						break;
					} else {
						mText_show.setText("配对分数=" + mMatchSource + "第" + i
								+ "次配对失败，花费时间="
								+ (System.currentTimeMillis() - startTime));
					}
				}
			}

			break;
		default:
			break;
		}
	}
}
