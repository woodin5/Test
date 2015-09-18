package com.wmz.test.activity;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;

import javax.security.auth.login.LoginException;

import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.wmz.test.R;
import com.wmz.test.R.id;
import com.wmz.test.R.layout;
import com.wmz.test.manager.ConnectServerManager;
import com.wmz.test.manager.DownloadManager;
import com.wmz.test.manager.ExecutorSeviceManager;
import com.wmz.test.manager.UploadManager;
import com.wmz.test.utils.FileUtils;
import com.wmz.test.utils.JsonUtils;
import com.wmz.test.utils.NetUtils;

import docom.sdk.client.ClsCamera;
import docom.sdk.client.ClsCamera.TakePictureListener;
import docom.sdk.client.ClsDocomSDK;
import docom.sdk.client.ClsDocomSDK.MsgListener;
import docom.sdk.client.ClsFingerDev;

public class MainActivity extends Activity {

	private static String base_url = "1"; 
	private Context mContext = MainActivity.this;
	private TextView mTv;
	private Button mBtn1, mBtn2,mBtn3;
	private ImageView mImg1,mImg2;
	private List<String> list;
	private final int HANDLER_LOGIN= 3; 
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				if (msg.obj != null) {
					mTv.setText(msg.obj.toString());
					list = JsonUtils.getListFromJsonByGetad(msg.obj.toString());
					Log.d("tag", "wmz:list=" + list);
				}
				break;
			case 1:
				pictureshow();
				if (msg.obj != null) {
					Log.d("tag", "wmz:pic=" + msg.obj);
				}
				break;
			case HANDLER_LOGIN:
				if(msg.obj!=null){
					Log.d("tag", "wmz:login="+msg.obj.toString()); 
				}
				break; 
			default:
				break;
			}
		};
	};

	public void pictureshow() {
		String myJpgPath = mContext.getCacheDir().getAbsolutePath()
				+ File.separator + "ad.jpg";
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 2;
		final Bitmap bm = BitmapFactory.decodeFile(myJpgPath, options);
		Drawable drawable = Drawable.createFromPath(myJpgPath);
		// iv_jpgshow.setImageBitmap(bm);
		mBtn1.setBackgroundDrawable(drawable);
	}

	private ClsDocomSDK sdk = null;
	private ClsDocomSDK.Connection conn = new ClsDocomSDK.Connection() {

		@Override
		public void onDisConnected() {
			// TODO Auto-generated method stub

		}

		@Override
		public void onConnected() {
			// TODO Auto-generated method stub
			Log.d("tag", "wmz:onConnected");
			sdkListener();
//			startTakePic();
			Log.d("tag", "wmz:gateMode="+sdk.GetGateMode()); 
			sdk.SetGateMode(0); 
			sdk.SetMsgListener("MSG,PAS,TCKPAS,FNG,SHW", new MsgListener() {
				
				@Override
				public void onMessage(String type, String message) {
					// TODO Auto-generated method stub
					Log.d("tag", "wmz:type="+type+",message="+message); 
				}
			}); 
		}

	};

	private int count = 0;

	private void startTakePic() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				while (count < 100) {
					String filename = "pic_" + count + ".jpg";
					camera.TakePicture(filename);
					count++;
					Log.d("tag", "wmz:filename=" + filename);
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

	private ClsCamera camera;
	private ClsFingerDev fingerDev; 
	private byte[] tmp2=new byte[1024],tmp1=new byte[1024];
	private int len1,len2,match; 
	
	private void sdkListener() {
		camera = sdk.GetCamera();
		camera.SetTakePictureListener(new TakePictureListener() {

			@Override
			public void OnTakePicture(String filename) {
				UploadManager.upload(url, filename, mHandler, 1);
			}
		});
		
		fingerDev = sdk.GetFinger();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

//		 sdk = new ClsDocomSDK(mContext);
//		 sdk.bindService(conn);

		mTv = (TextView) findViewById(R.id.tv);
		mImg1 = (ImageView) findViewById(R.id.img1); 
		mImg2 = (ImageView) findViewById(R.id.img2); 
		mBtn1 = (Button) findViewById(R.id.btn1);
		mBtn1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			
				
				// upload();
				// getAd();
//				listFile();
				
//				getFinger1(); 
				
//				login(); 
				mTv.setText((NetUtils.getLocalIpAddress())); 
				
			}

			

		

		});

		mBtn2 = (Button) findViewById(R.id.btn2);
		mBtn2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.d("tag", "wmz:uploadPic");
				//�ϴ�ͼƬ����
//				uploadPic();
				
				getFinger2(); 
			}

		
		});
		
		mBtn3 = (Button) findViewById(R.id.btn3);
		mBtn3.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startMatchFinger(); 
			}

			
		});
	}
	
	private void login() {
		String url= "http://ceshi.coolshow.cn/terminal/terminallogin"; 
		String params = "username=shop&password=shop"; 
		ConnectServerManager.login(url, params, mHandler, HANDLER_LOGIN);
	}
	private void getFinger1() {
		len1 = fingerDev.GetFingerTmp(tmp1, 1000); 
		Log.d("tag", "wmz:len1="+len1);     
		ByteArrayInputStream is = new ByteArrayInputStream(tmp1, 0, len1);
		mImg1.setImageBitmap(BitmapFactory.decodeStream(is));
	}
	private void getFinger2() {
	
		// TODO Auto-generated method stub
		len2 = fingerDev.GetFingerTmp(tmp2, 1000); 
		Log.d("tag","wmz:len2="+len2+",tmp2="+tmp2[len2-1]);
		mImg2.setImageBitmap(BitmapFactory.decodeByteArray(tmp2, 0, len2));
		
	}
	private void startMatchFinger() {
		match = fingerDev.Match(tmp1, len1, tmp2, len2); 
		Log.d("tag", "wmz:match="+match);
		
		String s1 = Base64.encodeToString(tmp1, 0, len1, Base64.DEFAULT); 
		String s2 = Base64.encodeToString(tmp2, 0,len2,Base64.DEFAULT); 
		byte[] b = Base64.decode(s1, Base64.DEFAULT); 
	
	}
	
	
	private void listFile() {
//		String file = mContext.getCacheDir().getAbsolutePath();
		String file = FileUtils.getAdPath(mContext).getAbsolutePath(); 
		Log.d("tag", "wmz:file=" + file);
//		List list = FileUtils.listPathFiles(file);
		List list = FileUtils.listPath("/mnt"); 
		Log.d("tag", "wmz:list=" + list.toString());
	}

	private void getAd() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				String url = "http://ceshi.coolshow.cn/ad/showAd";
				String response = NetUtils
						.getStringFromHttpURLConnectionByGet(url);
				Message msg = Message.obtain();
				msg.what = 0;
				msg.obj = response;
				mHandler.sendMessage(msg);
			}
		}).start();
	}

	private void upload() {
		String urlImg = "http://www.androidcentral.com/sites/androidcentral.com/files/postimages/684/podcast_featured_3.jpg";
		// String target = Environment.getExternalStorageDirectory()
		// .getAbsolutePath() + File.separator + "ad.jpg";
		String target = mContext.getCacheDir().getAbsolutePath()
				+ File.separator + "ad.jpg";
		DownloadManager.downloadByxUtils(urlImg, target, mHandler, 1, 0);

		// new Thread(new Runnable() {
		//
		// @Override
		// public void run() {
		//
		// HttpUtils httpUtils = new HttpUtils();
		// httpUtils.download(urlImg, target, true,
		// new RequestCallBack<File>() {
		// @Override
		// public void onStart() {
		// // TODO Auto-generated method stub
		// Log.d("tag", "wmz:onStart");
		// super.onStart();
		// }
		//
		// @Override
		// public void onLoading(long total, long current,
		// boolean isUploading) {
		// // TODO Auto-generated method stub
		// Log.d("tag", "wmz:total=" + total + ",current="
		// + current + ",isUploading="
		// + isUploading);
		// super.onLoading(total, current, isUploading);
		// }
		//
		// @Override
		// public void onSuccess(ResponseInfo<File> info) {
		// Log.d("tag", "wmz:result=" + info.result);
		// }
		//
		// @Override
		// public void onFailure(HttpException arg0,
		// String arg1) {
		//
		// }
		// });
		// }
		// });
	}

	String url = "http://ceshi.coolshow.cn/index/uploadPicture";
	String filename = Environment.getExternalStorageDirectory()
			.getAbsolutePath() + File.separator + "3.jpg";

	// String filename = "/ext_sd/camera/1.jpg";

	private void uploadPic() {
		HashMap<String, String> params = new HashMap<String, String>(); 
		params.put("id", "65"); 
		UploadManager.upload(url, filename, mHandler,params, 1);

		// new Thread(new Runnable() {
		// @Override
		// public void run() {
		//
		// String content = UploadUtils.uploadFile(url, filename);
		// Message msg = Message.obtain();
		// msg.obj = content;
		// msg.what = 1;
		// mHandler.sendMessage(msg);
		// }
		// });

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (sdk != null) {
			sdk.Destroy();
		}
	}

}
