package com.wmz.test.manager;

import android.os.Handler;
import android.util.Log;

import com.wmz.test.utils.DownloadUtils;

public class DownloadManager {

	/**
	 * 下载
	 * 
	 * @param url
	 * @param target
	 * @param handler
	 * @param success
	 * @param failure
	 */
	public static void downloadByxUtils(final String url, final String target,
			final Handler handler, final int success, final int failure) {
		ExecutorSeviceManager.getExecutorInstance().execute(new Runnable() {

			@Override
			public void run() {
				DownloadUtils.downloadByxUtils(url, target, handler, success,
						failure);
			}
		});
	}

	/**
	 * 下载
	 * 
	 * @param url
	 * @param target
	 * @param handler
	 * @param success
	 * @param failure
	 * @param update
	 */
	public static void downloadByxUtils(final String url, final String target,
			final Handler handler, final int success, final int failure,
			final int update) {
		ExecutorSeviceManager.getExecutorInstance().execute(new Runnable() {

			@Override
			public void run() {
				DownloadUtils.downloadByxUtils(url, target, handler, success,
						failure, update);
			}
		});
	}
}
