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
		 * ��SDK����
		 * 
		 * ������ connection - ���ӳɹ���Ļص��ӿ�
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
				 * ������Ϣ�����ص�
				 * 
				 * ������ type - ������Ϣ���͵����,����','�ָ�. . MSG - Τ��,���ڵ�������Ϣ . PAS - *
				 * ��բ�س��ź���Ϣ . TCKPAS - ˢƱ��բ�س��ź���Ϣ . FNG - ָ�Ʋɼ���Ϣ . SHW - ��ʾ��Ϣ
				 * listener - ��Ϣ�ص��ӿ�
				 */
				mClsDocomSDK.SetMsgListener("MSG,PAS,TCKPAS,FNG,SHW",
						new MsgListener() {
							/*
							 * ������ type - ��Ϣ����. . MSG - Τ��,���ڵ�������Ϣ . PAS -
							 * ��բ�س��ź���Ϣ . * TCKPAS - ˢƱ��բ�س��ź���Ϣ . FNG - ָ�Ʋɼ���Ϣ .
							 * SHW - ��ʾ��Ϣ message - * ��Ϣ����,������type��Ϣ������� .
							 * type="MSG" - * ����+�˿ں�,��Ϣ����,��:"IW1,12345678" . ����:
							 * I-���, O-���� . �˿ںţ� * C1-n:����,U1-n:USB,K1-n:�������,
							 * W1-n: ά�� . type="PAS" - * "A":A��س��ź�,"B":B��س��ź� .
							 * type="TCKPAS" - * "Ʊ��,ʱ��,��բ����,��բ��Ϣ". .
							 * ʱ��:yyyy-MM-dd HH:mm:ss��ʽ . ��բ����: A��B .
							 * ��բ��Ϣ:0-��֤��ʱ,1-��֤ʧ��,2-��բ��ʱ,3-�ѹ�բ . type="FNG" - *
							 * "ָ�����ͺ�,ָ����Ϣ" . ָ�����ͺ�: 82:����ָ����, 84:����ָ���� . ָ����Ϣ:
							 * * X16^nnnn,
							 * nnnnʮ�����������ַ�����ʽ.��0x12,0x34��ʾΪ"X16^1234" . *
							 * type="SHW" - "��Ʊ��Դ,��Ʊ����,����԰����,����԰����,��Ч����,��֤����".
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
						mText_show.setText("��Է���=" + mMatchSource + "��" + i
								+ "����Գɹ�������ʱ��="
								+ (System.currentTimeMillis() - startTime));
						break;
					} else {
						mText_show.setText("��Է���=" + mMatchSource + "��" + i
								+ "�����ʧ�ܣ�����ʱ��="
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
