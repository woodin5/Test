package com.wmz.test.utils;

import android.content.Context;
import android.telephony.TelephonyManager;

public class SystemUtils {
	public static String getDeviceId(Context context) {
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);

		return tm.getDeviceId();
	}
}
