package com.wmz.test.activity;

import com.wmz.test.R;
import com.wmz.test.R.layout;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.os.Build;

public class WebViewActivity extends Activity {

	private String TAG = "WebViewActivity";
	private Context mContext = WebViewActivity.this;
	private WebView mWebView;
	private String url = "http://www.qunar.com/";// "http://nedd.me/fr/";

	private Handler mHandler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web_view);

		initView();
	}

	@SuppressLint("JavascriptInterface")
	private void initView() {
		mWebView = (WebView) findViewById(R.id.webview);
		WebSettings wSettings = mWebView.getSettings();
		wSettings.setJavaScriptEnabled(true);
		wSettings.setSupportZoom(true);
		mWebView.setWebChromeClient(new MyWebChromeClient());
		mWebView.setWebViewClient(new WebViewClient());
		mWebView.addJavascriptInterface(new DemoJavaScriptInterface(), "demo");
		mWebView.loadUrl("file:///android_asset/demo.html");
	}

	final class DemoJavaScriptInterface {
		DemoJavaScriptInterface() {
		};

		public void clickOnAndroid() {
			Log.d(TAG, "wmz:clickOnAndroid");
			mHandler.post(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					mWebView.loadUrl("javascript:wave()");
				}
			});
		}
	}

	final class MyWebChromeClient extends WebChromeClient {
		@Override
		public boolean onJsAlert(WebView view, String url, String message,
				JsResult result) {
			// TODO Auto-generated method stub
			Log.d(TAG, "wmz:message=" + message);
			result.confirm();
			return true;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) {
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
