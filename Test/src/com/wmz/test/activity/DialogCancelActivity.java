package com.wmz.test.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;

import com.wmz.test.R;

public class DialogCancelActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dialog_cancel);

		showNoProject();

	}

	public void showNoProject() {
//		Builder builder =
		AlertDialog dialog = new AlertDialog.Builder(this)
				.setMessage("����û����Ŀ�����������Ŀ")
				.setPositiveButton("ȥ�����Ŀ", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();

					}
				}).setNegativeButton("�ر�", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();

					}
				}).setCancelable(false).create();
//		builder.setCancelable(false);
//		builder.show();
		dialog.show(); 
	}
}
