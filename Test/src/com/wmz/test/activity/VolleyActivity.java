package com.wmz.test.activity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.android.volley.Request.Method;
import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.android.volley.toolbox.StringRequest;
import com.wmz.test.R;
import com.wmz.test.R.id;
import com.wmz.test.R.layout;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class VolleyActivity extends Activity implements OnClickListener {

	private String TAG = "VolleyActivity";
	private Context mContext = VolleyActivity.this;
	private TextView mTextViewShow;
	private RequestQueue mRequestQueue;
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:

				break;

			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_volley);
		mRequestQueue = Volley.newRequestQueue(mContext);
		initView();
	}

	private void initView() {
		mTextViewShow = (TextView) findViewById(R.id.text_volley_show);
		findViewById(R.id.btn_volley_StringRequest_get)
				.setOnClickListener(this);
		findViewById(R.id.btn_volley_StringRequest_post).setOnClickListener(
				this);
		findViewById(R.id.btn_volley_JsonObjectRequest_get).setOnClickListener(
				this);
		findViewById(R.id.btn_volley_JsonObjectRequest_post)
				.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.btn_volley_StringRequest_get:
			StringRequest(Method.GET);
			break;
		case R.id.btn_volley_StringRequest_post:
			StringRequest(Method.POST);
			break;
		case R.id.btn_volley_JsonObjectRequest_get:
			JsonObjectRequest(Method.GET);
			break;
		case R.id.btn_volley_JsonObjectRequest_post:
			JsonObjectRequest(Method.POST);
			break;
		case R.id.btn_volley_ImageRequest:
			ImageRequest(); 
			break; 
		}
	}

	private void ImageRequest() {
		String url = ""; 
//		ImageRequest mImageRequest = new ImageRequest(url, listener, maxWidth, maxHeight, decodeConfig, errorListener); 
//		mRequestQueue.add(mImageRequest); 
	}

	private void JsonObjectRequest(int method) {
		String url = "http://192.168.18.136:8080/Demo/Login";
		Map<String, String> map = new HashMap<>();
		map.put("username", "1");
		map.put("password", "1");
		JsonObjectRequest mJsonObjectRequest = new JsonObjectRequest(method,
				url, new JSONObject(map), new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						mTextViewShow.setText(response.toString());
					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						mTextViewShow.setText(error.getMessage());
					}
				});
		mRequestQueue.add(mJsonObjectRequest);
	}

	private void StringRequest(int method) {
		String url = "http://192.168.18.136:8080/Demo/Login";
		StringRequest mStringRequest = new StringRequest(method, url,
				new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						mTextViewShow.setText(response);
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						mTextViewShow.setText(error.getMessage());
					}
				}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> map = new HashMap<>();
				map.put("username", "1");
				map.put("password", "1");
				return map;
			}
		};
		mRequestQueue.add(mStringRequest);
	}
}
