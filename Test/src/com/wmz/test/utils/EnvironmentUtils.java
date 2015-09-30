package com.wmz.test.utils;

import android.os.Environment;

public class EnvironmentUtils {
	public static boolean hasExternalStorage() {
		return Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
	}

	public static String getExternalStoragePath() {
		if (hasExternalStorage()) {
			return Environment.getExternalStorageDirectory().getAbsolutePath();
		}
		return null;
	}

	public static String getDataPath() {
		return Environment.getDataDirectory().getAbsolutePath();
	}

}
