package com.wmz.test.utils;

public class Constants {
	public static String BASE_URL = "http://ceshi.coolshow.cn/";
	public static String URL_AD = "/ad/showAd";
	public static String URL_LOGIN ="/index/terminalLogin";  //"/ShopInspector/login";
	public static String URL_ISONLINE = "/shopInspector/isShopInspectorOnline"; 
	public static String URL_UPLOAD = "/index/uploadPicture"; 
	public static String URL_CHECKING = "/terminal/checkTicket"; // KX86143493
	public static String URL_CHECK_PAS= "/terminal/processingRecord";
	public static String URL_UPGRAD = "/ShopInspector/checkVersion";
	
	
	public static int THEME = 0; 
	public static int HEIGHT = 0; 
	public static int WIDTH = 0; 
	public static boolean ISTEST = false; 
	public static boolean ISGATE = true; 
	
	//test
	public static String TEST_BASE_URL = "http://192.168.18.110:8080/Demo"; 
	public static String TEST_URL_LOGIN ="/Login"; 
	public static String TEST_URL_AD = "/GetAds";
	public static String TEST_URL_UPGRAD = "/CheckVersion";
	public static String TEST_URL_ISONLINE = "/shopInspector/isShopInspectorOnline"; 
	public static String TEST_URL_UPLOAD = "/index/uploadPicture"; 
	public static String TEST_URL_CHECKING = "/ShopInspector/checkCode"; // KX86143493
	public static String TEST_URL_CHECK_PAS= "/ShopInspector/checkCodeRecord";
}
