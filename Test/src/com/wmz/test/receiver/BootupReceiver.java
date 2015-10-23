package com.wmz.test.receiver;

import com.wmz.test.activity.ListActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootupReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent arg1) {
		try {
			 Thread.sleep(2000);
			 } catch (InterruptedException e) {
			 e.printStackTrace();
			 }
//			 Intent i = new Intent(context, ListActivity.class);
//			 i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//			 context.startActivity(i);
	}

}
