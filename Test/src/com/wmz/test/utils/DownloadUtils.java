package com.wmz.test.utils;

import java.io.File;

import android.os.Handler;
import android.util.Log;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

/**
 * 下载工具类
 * 
 * @author wmz
 * 
 */
public class DownloadUtils {
	/**
	 * 下载
	 * 
	 * @param url
	 * @param target
	 * @param handler
	 * @param success
	 * @param failure
	 */
	public static void downloadByxUtils(String url, String target,
			final Handler handler, final int success, final int failure) {
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.download(url, target, true, false,
				new RequestCallBack<File>() {

					@Override
					public void onSuccess(ResponseInfo<File> info) {
						HandlerUtils.sendHandle(handler,
								info.result.getAbsolutePath(), success);
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						HandlerUtils.sendHandle(handler, arg1, failure);
					}
				});
	}

	public static void downloadByxUtils(String url, String target,
			final Handler handler, final int success, final int failure,
			final int update) {
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.download(url, target, true, true,
				new RequestCallBack<File>() {

					@Override
					public void onLoading(long total, long current,
							boolean isUploading) {
						Log.d("", "wmz:current=" + current);
						// TODO Auto-generated method stub
						String arr[] = { total + "", current + "" };
						HandlerUtils.sendHandle(handler, arr, update);
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						HandlerUtils.sendHandle(handler, arg1, failure);
					}

					@Override
					public void onSuccess(ResponseInfo<File> info) {
						HandlerUtils.sendHandle(handler,
								info.result.getAbsolutePath(), success);
					}
				});
	}
}
