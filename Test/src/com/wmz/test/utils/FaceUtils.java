package com.wmz.test.utils;

import java.io.ByteArrayOutputStream;

import org.json.JSONObject;

import android.graphics.Bitmap;
import android.util.Log;

import com.android.volley.toolbox.JsonObjectRequest;
import com.facepp.error.FaceppParseException;
import com.facepp.http.HttpRequests;
import com.facepp.http.PostParameters;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;
import com.wmz.test.manager.ExecutorSeviceManager;

public class FaceUtils {
	public interface CallBack {
		void success(JSONObject jsonObject);

		void error(FaceppParseException exception);
	}

	public static void detect(final Bitmap bm, final CallBack callBack) {
		ExecutorSeviceManager.getExecutorInstance().execute(new Runnable() {

			@Override
			public void run() {

				try {
					HttpRequests requests = new HttpRequests(
							Constants.FACE_KEY, Constants.FACE_SECRET, true,
							true);
					Bitmap bitmap = Bitmap.createBitmap(bm, 0, 0,
							bm.getWidth(), bm.getHeight());
					ByteArrayOutputStream stream = new ByteArrayOutputStream();
					bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
					byte[] array = stream.toByteArray();
					PostParameters parameters = new PostParameters();
					parameters.setImg(array);
					JSONObject jsonObject = requests
							.detectionDetect(parameters);
					Log.d("", "wmz:jsonObject=" + jsonObject.toString());
					if (callBack != null) {
						callBack.success(jsonObject);
					}
				} catch (FaceppParseException e) {
					e.printStackTrace();
					if (callBack != null) {
						callBack.error(e);
					}
				}

			}
		});
	}
}
