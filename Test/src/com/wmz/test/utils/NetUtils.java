package com.wmz.test.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.util.Enumeration;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class NetUtils {
	private static String TAG = "NetUtils";
	public static String getLocalIpAddress()  
    {  
        try  
        {  
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();)  
            {  
               NetworkInterface intf = en.nextElement();  
               for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();)  
               {  
                   InetAddress inetAddress = enumIpAddr.nextElement();  
                   if (!inetAddress.isLoopbackAddress())  
                   {  
                       return inetAddress.getHostAddress().toString();  
                   }  
               }  
           }  
        }  
        catch (SocketException ex)  
        {  
            Log.e("WifiPreference IpAddress", ex.toString());  
        }  
        return null;  
    }  
	public static boolean hasNet(Context context){
		ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE); 
		NetworkInfo info = manager.getActiveNetworkInfo(); 
		if(info!=null && info.isConnected()){
			return true; 
		}else{
			return false; 
		}
	}
	public static int getNetType(Context context){
		ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE); 
		int type = manager.getActiveNetworkInfo().getType(); 
		return type; 
	}
	public static HttpURLConnection getHttpURLConnection(String url){
		HttpURLConnection httpurlconnection = null;

		try {
			httpurlconnection = (HttpURLConnection) new URL(url)
					.openConnection();
			httpurlconnection
			.setRequestProperty(
					"User-Agent",
					"Mozilla/5.0 (Windows NT 6.1) AppleWebKit/535.2 (KHTML, like Gecko) Chrome/15.0.874.106 Safari/535.2");
			return httpurlconnection; 
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null; 
		} catch (IOException e) {
			e.printStackTrace();
			return null; 
		}
	}
	public static InputStream getInputStreamFromHttpURLConnectionByGet(
			String url) {
		try {

			HttpURLConnection httpurlconnection = null;

			httpurlconnection = getHttpURLConnection(url);
			httpurlconnection.setDoInput(true);
			httpurlconnection.setRequestMethod("GET");
			httpurlconnection.setReadTimeout(60 * 1000);
			httpurlconnection.setConnectTimeout(60 * 1000);

			return httpurlconnection.getInputStream();

		} catch (MalformedURLException e) {
			System.out.println("url error!!!");
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	
	public static InputStream getInputStreamFromHttpURLConnectionByPost(
			String url, String params) {
		try {
			Log.d("tag", "wmz:params="+params);
			HttpURLConnection httpurlconnection = null;

			httpurlconnection = getHttpURLConnection(url); 
			httpurlconnection.setDoOutput(true);
			httpurlconnection.setDoInput(true);
			httpurlconnection.setRequestMethod("POST");
			httpurlconnection.getOutputStream().write(params.getBytes());
			httpurlconnection.setReadTimeout(60 * 1000);
			httpurlconnection.setConnectTimeout(60 * 1000);
			
			httpurlconnection.getOutputStream().flush();
			httpurlconnection.getOutputStream().close();
			return httpurlconnection.getInputStream();

		} catch (MalformedURLException e) {
			System.out.println("url error!!!");
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String getStringFromHttpURLConnectionByGet(String url) {
		StringBuffer sb = new StringBuffer();
		try {
			BufferedReader in = null;
			InputStream input = null;

			input = getInputStreamFromHttpURLConnectionByGet(url);
			in = new BufferedReader(new InputStreamReader(input, "utf-8"));
			int len = 0;
			char buf[] = new char[4 * 1024];
			while ((len = in.read(buf)) != -1) {
				sb.append(new String(buf, 0, len));
			}
			in.close();

		} catch (MalformedURLException e) {
			System.out.println("url error!!!");
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return sb.toString();
	}

	public static String getStringFromHttpURLConnectionByPost(String url,
			String params) {
		StringBuffer sb = new StringBuffer();
		try {
			BufferedReader in = null;
			InputStream input = null;

			input = getInputStreamFromHttpURLConnectionByPost(url, params);
			if(input==null){
				return null; 
			}
			in = new BufferedReader(new InputStreamReader(input, "utf-8"));
			int len = 0;
			char buf[] = new char[4 * 1024];
			while ((len = in.read(buf)) != -1) {
				sb.append(new String(buf, 0, len));
			}
			in.close();

		} catch (MalformedURLException e) {
			System.out.println("url error!!!");
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return sb.toString();
	}
}
