package com.wmz.test.manager;

import java.util.HashMap;
import java.util.Map;

import android.os.Handler;

import com.wmz.test.utils.HandlerUtils;
import com.wmz.test.utils.UploadUtils;

public class UploadManager {
	public static void upload(final String uploadUrl, final String filename,final Handler handler,final int what) {
		ExecutorSeviceManager.getExecutorInstance().execute(new Runnable() {

			@Override
			public void run() {
				String response = UploadUtils.uploadFile(uploadUrl, filename); 
				HandlerUtils.sendHandle(handler, response, what);
			}
		});
	}
	public static void upload(final String uploadUrl,final String filename,final Handler handler,final HashMap<String, String> params,final int what){
		ExecutorSeviceManager.getExecutorInstance().execute(new Runnable() {
			
			@Override
			public void run() {
				String response = UploadUtils.uploadFile(uploadUrl, filename,params); 
				HandlerUtils.sendHandle(handler, response, what);
			}
		});
	}
}
