package com.wmz.test.manager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.wmz.test.R;
import com.wmz.test.bean.UpgradBean;
import com.wmz.test.utils.Constants;
import com.wmz.test.utils.FileUtils;

public class UpgradManager {
	private static final int DOWN_NOSDCARD = 0;
	private static final int DOWN_UPDATE = 1;
	private static final int DOWN_OVER = 2;

	private static final int DIALOG_TYPE_LATEST = 0;
	private static final int DIALOG_TYPE_FAIL = 1;

	private static UpgradManager upgradManager;

	private Context mContext;
	// ֪ͨ�Ի���
	private Dialog noticeDialog;
	// ���ضԻ���
	private Dialog downloadDialog;
	// '�Ѿ�������' ���� '�޷���ȡ���°汾' �ĶԻ���
	private Dialog latestOrFailDialog;
	// ������
	private ProgressBar mProgress;
	// ��ʾ������ֵ
	private TextView mProgressText;
	// ��ѯ����
	private ProgressDialog mProDialog;
	// ����ֵ
	private int progress;
	// �����߳�
	private Thread downLoadThread;
	// ��ֹ���
	private boolean interceptFlag;
	// ��ʾ��
	private String updateMsg = "";
	// ���صİ�װ��url
	private String apkUrl = "";
	// ���ذ�����·��
	private String savePath = "";
	// apk��������·��
	private String apkFilePath = "";
	// ��ʱ�����ļ�·��
	private String tmpFilePath = "";
	// �����ļ���С
	private String apkFileSize;
	// �������ļ���С
	private String tmpFileSize;

	private String curVersionName = "";
	private int curVersionCode;
	private UpgradBean mUpgradBean;

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case DOWN_UPDATE:

				if (msg.obj == null)
					return;
				String arr[] = (String[]) (msg.obj);
				float current = Integer.parseInt(arr[1]);
				float total = Integer.parseInt(arr[0]);
				progress = (int) (current / total * 100);
				DecimalFormat df = new DecimalFormat("0.00");
				tmpFileSize = df.format((float) current / 1024 / 1024) + "MB";
				apkFileSize = df.format((float) total / 1024 / 1024) + "MB";
				// ��ǰ����ֵ

				mProgress.setProgress(progress);
				mProgressText.setText(tmpFileSize + "/" + apkFileSize);
				break;
			case DOWN_OVER:
				downloadDialog.dismiss();
				installApk();
				break;
			case DOWN_NOSDCARD:
				downloadDialog.dismiss();
				Toast.makeText(mContext, "�޷����ذ�װ�ļ�������SD���Ƿ����", 3000).show();
				break;
			}
		};
	};

	public static UpgradManager getUpdateManager() {
		if (upgradManager == null) {
			upgradManager = new UpgradManager();
		}
		upgradManager.interceptFlag = false;
		return upgradManager;
	}

	/**
	 * ���App����
	 * 
	 * @param context
	 * @param isShowMsg
	 *            �Ƿ���ʾ��ʾ��Ϣ
	 */
	public void checkAppUpdate(Context context, final boolean isShowMsg,
			Handler hanlder, int what) {
		Log.d("tag", "wmz:checkAppUpdate");
		this.mContext = context;
		getCurrentVersion();
		if (isShowMsg) {
			if (mProDialog == null)
				mProDialog = ProgressDialog.show(mContext, null, "���ڼ�⣬���Ժ�...",
						true, true);
			else if (mProDialog.isShowing()
					|| (latestOrFailDialog != null && latestOrFailDialog
							.isShowing()))
				return;
		}

		final Handler mHandler = new Handler() {
			public void handleMessage(Message msg) {

				// ��ʾ�����
				if (msg.what == 1) {
					Log.d("tag", "wmz:1");
					if (msg.obj == null)
						return;

				} else if (isShowMsg) {
					showLatestOrFailDialog(DIALOG_TYPE_FAIL);
				}
			}
		};

		String url = "";
		if (Constants.ISTEST) {
			url = Constants.TEST_BASE_URL + Constants.TEST_URL_UPGRAD;
		} else {
			url = Constants.BASE_URL + Constants.URL_UPGRAD;
		}
		Log.d("", "wmz:url=" + url);
		ConnectServerManager.checkVersion(url, hanlder, what);

	}

	public boolean canUpgrad(UpgradBean bean) {

		// �رղ��ͷ��ͷŽ������Ի���
		if (mProDialog != null) {
			mProDialog.dismiss();
			mProDialog = null;
		}
		mUpgradBean = bean;
		if (mUpgradBean != null) {
			if (curVersionCode < mUpgradBean.getVersionCode()) {
				return true;
			}
		}
		return false;
	}

	public void appUpgrad() {

		apkUrl = mUpgradBean.getDownloadUrl();
		updateMsg = mUpgradBean.getUpdateLog();
		showNoticeDialog();

	}

	/**
	 * ��ʾ'�Ѿ�������'����'�޷���ȡ�汾��Ϣ'�Ի���
	 */
	private void showLatestOrFailDialog(int dialogType) {
		if (latestOrFailDialog != null) {
			// �رղ��ͷ�֮ǰ�ĶԻ���
			latestOrFailDialog.dismiss();
			latestOrFailDialog = null;
		}
		AlertDialog.Builder builder = new Builder(mContext);
		builder.setTitle("ϵͳ��ʾ");
		if (dialogType == DIALOG_TYPE_LATEST) {
			builder.setMessage("����ǰ�Ѿ������°汾");
		} else if (dialogType == DIALOG_TYPE_FAIL) {
			builder.setMessage("�޷���ȡ�汾������Ϣ");
		}
		builder.setPositiveButton("ȷ��", null);
		latestOrFailDialog = builder.create();
		latestOrFailDialog.show();
	}

	/**
	 * ��ȡ��ǰ�ͻ��˰汾��Ϣ
	 */
	private void getCurrentVersion() {
		try {
			PackageInfo info = mContext.getPackageManager().getPackageInfo(
					mContext.getPackageName(), 0);
			curVersionName = info.versionName;
			curVersionCode = info.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace(System.err);
		}
	}

	/**
	 * ��ʾ�汾����֪ͨ�Ի���
	 */
	private void showNoticeDialog() {
		AlertDialog.Builder builder = new Builder(mContext);
		builder.setTitle("����汾����");
		builder.setMessage(updateMsg);
		builder.setPositiveButton("��������", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				showDownloadDialog();
			}
		});
		builder.setNegativeButton("�Ժ���˵", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		noticeDialog = builder.create();
		noticeDialog.show();
	}

	/**
	 * ��ʾ���ضԻ���
	 */
	private void showDownloadDialog() {
		AlertDialog.Builder builder = new Builder(mContext);
		builder.setTitle("���������°汾");

		final LayoutInflater inflater = LayoutInflater.from(mContext);
		View v = inflater.inflate(R.layout.update_progress, null);
		mProgress = (ProgressBar) v.findViewById(R.id.update_progress);
		mProgressText = (TextView) v.findViewById(R.id.update_progress_text);

		builder.setView(v);
		builder.setNegativeButton("ȡ��", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				interceptFlag = true;
			}
		});
		builder.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				dialog.dismiss();
				interceptFlag = true;
			}
		});
		downloadDialog = builder.create();
		downloadDialog.setCanceledOnTouchOutside(false);
		downloadDialog.show();

		downloadApk();
	}

	private Runnable mdownApkRunnable = new Runnable() {
		@Override
		public void run() {
			try {
				String apkName = "OSChinaApp_" + mUpgradBean.getVersionName()
						+ ".apk";
				String tmpApk = "OSChinaApp_" + mUpgradBean.getVersionName()
						+ ".tmp";
				// �ж��Ƿ������SD��
				String storageState = Environment.getExternalStorageState();
				if (storageState.equals(Environment.MEDIA_MOUNTED)) {
					savePath = Environment.getExternalStorageDirectory()
							.getAbsolutePath() + "/OSChina/Update/";
					File file = new File(savePath);
					if (!file.exists()) {
						file.mkdirs();
					}
					apkFilePath = savePath + apkName;
					tmpFilePath = savePath + tmpApk;
				}

				// û�й���SD�����޷������ļ�
				if (apkFilePath == null || apkFilePath == "") {
					mHandler.sendEmptyMessage(DOWN_NOSDCARD);
					return;
				}

				File ApkFile = new File(apkFilePath);

				// �Ƿ������ظ����ļ�
				if (ApkFile.exists()) {
					downloadDialog.dismiss();
					installApk();
					return;
				}

				// �����ʱ�����ļ�
				File tmpFile = new File(tmpFilePath);
				FileOutputStream fos = new FileOutputStream(tmpFile);

				URL url = new URL(apkUrl);
				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				conn.connect();
				int length = conn.getContentLength();
				InputStream is = conn.getInputStream();

				// ��ʾ�ļ���С��ʽ��2��С������ʾ
				DecimalFormat df = new DecimalFormat("0.00");
				// ������������ʾ�����ļ���С
				apkFileSize = df.format((float) length / 1024 / 1024) + "MB";

				int count = 0;
				byte buf[] = new byte[1024];

				do {
					int numread = is.read(buf);
					count += numread;
					// ������������ʾ�ĵ�ǰ�����ļ���С
					tmpFileSize = df.format((float) count / 1024 / 1024) + "MB";
					// ��ǰ����ֵ
					progress = (int) (((float) count / length) * 100);
					// ���½���
					mHandler.sendEmptyMessage(DOWN_UPDATE);
					if (numread <= 0) {
						// ������� - ����ʱ�����ļ�ת��APK�ļ�
						if (tmpFile.renameTo(ApkFile)) {
							// ֪ͨ��װ
							mHandler.sendEmptyMessage(DOWN_OVER);
						}
						break;
					}
					fos.write(buf, 0, numread);
				} while (!interceptFlag);// ���ȡ����ֹͣ����

				fos.close();
				is.close();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	};

	/**
	 * ����apk
	 * 
	 * @param url
	 */
	private void downloadApk() {

//		File file = new File("/ext_sd");
		File file = Environment.getExternalStorageDirectory().getAbsoluteFile();
		if (FileUtils.existsFile(file)) {
			List<File> list = FileUtils.listPathFiles(file.getAbsolutePath());
			Log.d("tag", "wmz:list=" + list.toString());
			Runtime runtime = Runtime.getRuntime();
			try {
				runtime.exec("chmod 777 " + file.getAbsolutePath());
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			Log.d("tag", "wmz:null");
		}
		String url = "http://192.168.18.116:8080/Demo/DowloadApk";//mUpgradBean.getDownloadUrl();  
//		apkFilePath = "/ext_sd/" + "TicketsChecking" + ".apk";
		apkFilePath = file+File.separator+"Test.apk";
		// apkFilePath = FileUtils.getDownloadPath(mContext) + File.separator
		// + "TicketsChecking" + ".apk";
		Log.d("tag", "wmz:apkFilePath=" + apkFilePath);
		DownloadManager.downloadByxUtils(url, apkFilePath, mHandler, DOWN_OVER, -1,
				DOWN_UPDATE);
		downLoadThread = new Thread(mdownApkRunnable);
		// downLoadThread.start();
	}

	/**
	 * ��װapk
	 * 
	 * @param url
	 */
	private void installApk() {
		Runtime runtime = Runtime.getRuntime();
		try {
			runtime.exec("chmod 777 "
					+ FileUtils.getDownloadPath(mContext).getAbsolutePath());
		} catch (IOException e) {
			e.printStackTrace();
		}
		File apkfile = new File(apkFilePath);
		if (!apkfile.exists()) {
			return;
		}
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setDataAndType(Uri.fromFile(new File(apkFilePath)),
				"application/vnd.android.package-archive");
		// i.setDataAndType(Uri.parse("file://" + apkfile.toString()),
		// "application/vnd.android.package-archive");
		mContext.startActivity(i);
		// FileUtils.deleteFile(new File(apkFilePath));
	}
}
