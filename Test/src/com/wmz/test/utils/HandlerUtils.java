package com.wmz.test.utils;

import android.os.Handler;
import android.os.Message;

public class HandlerUtils {
	public static void sendHandle(Handler handler, Object response, int what) {
		Message msg = Message.obtain();
		msg.what = what;
		msg.obj = response;
		handler.sendMessage(msg);
	}

	public static void sendHandler(Handler handler, String obj, int arg1,
			int arg2, int what) {
		Message msg = Message.obtain();
		msg.what = what;
		msg.arg1 = arg1;
		msg.arg2 = arg2;
		msg.obj = obj;
		handler.sendMessage(msg);
	}
}
