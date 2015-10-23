package com.wmz.test.activity;

import java.nio.channels.SelectableChannel;
import java.util.concurrent.CountDownLatch;

import org.json.JSONArray;
import org.json.JSONObject;

import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.baidu.cyberplayer.utils.w;
import com.facepp.error.FaceppParseException;
import com.wmz.test.R;
import com.wmz.test.R.id;
import com.wmz.test.R.layout;
import com.wmz.test.utils.FaceUtils;
import com.wmz.test.utils.FaceUtils.CallBack;

import android.R.integer;
import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.os.Build;
import android.provider.MediaStore;

public class FaceActivity extends Activity implements OnClickListener {

	private String TAG = "FaceActivity";
	private Context mContext = FaceActivity.this;
	private static final int PIC_CODE = 0;
	private ImageView mImgFace;
	private Bitmap mBitmapPhoto;
	private final static int HANDLER_SUCCESS = 0;
	private final static int HANDLER_ERROR = 1;
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case HANDLER_SUCCESS:
				JSONObject jsonObject = (JSONObject) msg.obj;
				jsonRsBitmap(jsonObject);
				mImgFace.setImageBitmap(mBitmapPhoto);
				break;

			case HANDLER_ERROR:
				break;
			}
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_face);

		initView();

	}

	private void initView() {
		mImgFace = (ImageView) findViewById(R.id.img_face);
		findViewById(R.id.btn_face_find).setOnClickListener(this);
		findViewById(R.id.btn_face_select).setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.btn_face_find:
			if (mBitmapPhoto != null) {
				FaceUtils.detect(mBitmapPhoto, new CallBack() {

					@Override
					public void success(JSONObject jsonObject) {
						Message msg = Message.obtain();
						msg.what = HANDLER_SUCCESS;
						msg.obj = jsonObject;
						mHandler.sendMessage(msg);
					}

					@Override
					public void error(FaceppParseException exception) {
						Message msg = Message.obtain();
						msg.what = HANDLER_ERROR;
						msg.obj = exception.getErrorMessage();
						mHandler.sendMessage(msg);
					}
				});
			}
			break;

		case R.id.btn_face_select:
			selectPic();
			break;
		}
	}

	private void selectPic() {
		Intent intent = new Intent(Intent.ACTION_PICK);
		intent.setType("image/*");
		startActivityForResult(intent, PIC_CODE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == PIC_CODE) {
			if (data != null) {
				Uri uri = data.getData();
				Cursor cursor = getContentResolver().query(uri, null, null,
						null, null);
				int count = cursor.getColumnCount();
				cursor.moveToFirst();

				int idx = cursor
						.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
				String photoPath = cursor.getString(idx);
				resizePhoto(photoPath);
				cursor.close();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void resizePhoto(String photoPath) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(photoPath, options);
		double ratio = Math.max(options.outWidth * 1.0d / 1024f,
				options.outHeight * 1.0d / 1024f);
		options.inSampleSize = (int) Math.ceil(ratio);
		options.inJustDecodeBounds = false;
		mBitmapPhoto = BitmapFactory.decodeFile(photoPath, options);
		mImgFace.setImageBitmap(mBitmapPhoto);
	}

	private void jsonRsBitmap(JSONObject jsonObject) {
		Bitmap bitmap = Bitmap.createBitmap(mBitmapPhoto.getWidth(),
				mBitmapPhoto.getHeight(), mBitmapPhoto.getConfig());
		Canvas canvas = new Canvas(bitmap);
		canvas.drawBitmap(mBitmapPhoto, 0, 0, null);
		try {
			JSONArray faces = jsonObject.getJSONArray("face");
			int faceCount = faces.length();
			for (int i = 0; i < faceCount; i++) {
				JSONObject face = faces.getJSONObject(i);
				JSONObject position = face.getJSONObject("position");
				float x = (float) position.getJSONObject("center").getDouble(
						"x");
				float y = (float) position.getJSONObject("center").getDouble(
						"y");

				float w = (float) position.getDouble("width");
				float h = (float) position.getDouble("height");

				x = x / 100 * bitmap.getWidth();
				y = y / 100 * bitmap.getHeight();

				w = w / 100 * bitmap.getWidth();
				h = h / 100 * bitmap.getHeight();

				Paint mPaint = new Paint();
				mPaint.setColor(0xffffffff);
				mPaint.setStrokeWidth(3);

				canvas.drawLine(x - w / 2, y - h / 2, x - w / 2, y + h / 2,
						mPaint);
				canvas.drawLine(x - w / 2, h - h / 2, x + w / 2, y - h / 2,
						mPaint);
				canvas.drawLine(x - w / 2, y + h / 2, x + w / 2, y + h / 2,
						mPaint);
				canvas.drawLine(x + w / 2, y - h / 2, x + w / 2, y + h / 2,
						mPaint);

				int age = face.getJSONObject("attribute").getJSONObject("age")
						.getInt("value");
				String gender = face.getJSONObject("attribute")
						.getJSONObject("gender").getString("value");
				Bitmap ageBitmap = buildBitmap(age, "Male".equals(gender));
				int ageWidth = ageBitmap.getWidth();
				int ageHeight = ageBitmap.getHeight();
				if (bitmap.getWidth() < mImgFace.getWidth()
						&& bitmap.getHeight() < mImgFace.getHeight()) {
					float ratio = Math.max(
							bitmap.getWidth() * 1.0f / mImgFace.getWidth(),
							bitmap.getHeight() * 1.0f / mImgFace.getHeight());
					ageBitmap = Bitmap.createScaledBitmap(ageBitmap,
							(int) (ageBitmap.getWidth() * ratio),
							(int) (ageBitmap.getHeight() * ratio), false);
				}
				canvas.drawBitmap(ageBitmap, x - ageBitmap.getWidth(), y - h
						/ 2 - ageBitmap.getHeight(), null);
				mBitmapPhoto = bitmap;
			}
		} catch (Exception e) {
		}
	}

	private Bitmap buildBitmap(int age, boolean isMale) {
		TextView age_gender = (TextView) findViewById(R.id.age_and_gender);
		age_gender.setText(age + "");
		if (isMale) {
			age_gender.setCompoundDrawablesWithIntrinsicBounds(getResources()
					.getDrawable(R.drawable.male), null, null, null);
		} else {
			age_gender.setCompoundDrawablesWithIntrinsicBounds(getResources()
					.getDrawable(R.drawable.female), null, null, null);
		}

		age_gender.setDrawingCacheEnabled(true);
		Bitmap bitmap = Bitmap.createBitmap(age_gender.getDrawingCache());
		age_gender.destroyDrawingCache();
		return bitmap;
	};
}
