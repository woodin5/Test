package com.wmz.test.manager;

import android.util.Log;

import com.wmz.test.utils.NetUtils;

public class NetWorkManager {
	public static String TAG = "NetWorManager";
	
	public static void Post(String url){
		String response = NetUtils.getStringFromHttpURLConnectionByGet(url);  
		Log.d(TAG, "wmz:response="); 
	}
	public static void Post(String url,String params){
		String response = NetUtils.getStringFromHttpURLConnectionByPost(url, params); 
		Log.d(TAG, "wmz:response="); 
	}
	
}
