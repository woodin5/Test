package com.wmz.test.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;

public class DialogUtils {
	private static AlertDialog dialog = null;

	public static void showDialog(Context context, View view) {
		showDialog(context, view, 0);
	}

	public static void showDialog(Context context, View view, int theme) {
		if (dialog != null) {
			dismissDialog();
			dialog = null;
		}
		dialog = new AlertDialog.Builder(context, theme).setCancelable(false)
				.create();
		dialog.show();

		dialog.getWindow().setGravity(Gravity.CENTER);
		dialog.getWindow().setLayout(
				context.getResources().getDisplayMetrics().widthPixels * 3 / 4,
				android.view.WindowManager.LayoutParams.WRAP_CONTENT);
		dialog.getWindow().setContentView(view);
	}

	public static void dismissDialog() {
		if (dialog != null && dialog.isShowing()) {
			dialog.dismiss();
		}
	}

	public static boolean isShowing() {
		if (dialog != null && dialog.isShowing()) {
			return true;
		}
		return false;
	}
}
