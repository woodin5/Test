package com.wmz.test.manager;

import android.os.Handler;
import android.util.Log;

import com.wmz.test.utils.HandlerUtils;
import com.wmz.test.utils.NetUtils;

/**
 * 连接服务器管理器
 * 
 * @author wmz
 * 
 */
public class ConnectServerManager {

	/**登录
	 * @param url
	 * @param handler
	 * @param what
	 */
	public static void login(final String url,final Handler handler,final int what){
		ExecutorSeviceManager.getExecutorInstance().execute(new Runnable() {
			
			@Override
			public void run() {
				String response = NetUtils.getStringFromHttpURLConnectionByGet(url); 
				HandlerUtils.sendHandle(handler, response, what);
			}
		});
	}
	
	public static void login(final String url,final String params,final Handler handler,final int what){
		ExecutorSeviceManager.getExecutorInstance().execute(new Runnable() {
			public void run() {
				String response = NetUtils.getStringFromHttpURLConnectionByPost(url, params); 
				HandlerUtils.sendHandle(handler, response, what);
			}
		}); 
	}
	/**�?查版�?
	 * @param url
	 * @param handler
	 * @param what
	 */
	public static void checkVersion(final String url, final Handler handler,
			final int what) {
		Log.d("tag", "wmz:checkVersion");
		ExecutorSeviceManager.getExecutorInstance().execute(new Runnable() {

			@Override
			public void run() {
				String response = NetUtils
						.getStringFromHttpURLConnectionByGet(url);
				HandlerUtils.sendHandle(handler, response, what);
			}
		});
	}

	/**
	 * 获取广告信息
	 * 
	 * @param url
	 * @param handler
	 * @param what
	 */
	public static void getAd(final String url, final Handler handler,
			final int what) {
		ExecutorSeviceManager.getExecutorInstance().execute(new Runnable() {

			@Override
			public void run() {
				String response = NetUtils
						.getStringFromHttpURLConnectionByGet(url);
				HandlerUtils.sendHandle(handler, response, what);
			}
		});
	}

	/**
	 * 获取验证票信�?
	 * 
	 * @param url
	 * @param username
	 * @param ticketNo
	 * @param handler
	 * @param what
	 */
	public static void getTicketMsg(final String url, final String username,
			final String ticketNo, final Handler handler, final int what) {
		ExecutorSeviceManager.getExecutorInstance().execute(new Runnable() {

			@Override
			public void run() {
				String response = NetUtils
						.getStringFromHttpURLConnectionByGet(url);
				HandlerUtils.sendHandle(handler, response, what);
			}
		});
	}

	/**
	 * 发�?�过闸票信息
	 * 
	 * @param url
	 * @param ticketNo
	 * @param handler
	 * @param what
	 */
	public static void sendTicketMsg(final String url, final String ticketNo,
			final Handler handler, final int what) {
		ExecutorSeviceManager.getExecutorInstance().execute(new Runnable() {

			@Override
			public void run() {
				String response = NetUtils
						.getStringFromHttpURLConnectionByGet(url);
				HandlerUtils.sendHandle(handler, response, what);
			}
		});
	}

}
