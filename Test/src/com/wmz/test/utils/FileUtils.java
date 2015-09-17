package com.wmz.test.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;

/**
 * �ļ�������
 * 
 * @author wmz
 * 
 */
public class FileUtils {

	public static File getDownloadPath(Context context) {
		File file = new File(context.getCacheDir().getAbsolutePath()
				+ File.separator + "download");
		if (!existsFile(file)) {
			file.mkdir();
		}
		return file;
	}

	/**
	 * �ļ��Ƿ����
	 * 
	 * @param file
	 * @return
	 */
	public static boolean existsFile(File file) {
		return file.exists();
	}

	/**
	 * �ļ��Ƿ����
	 * 
	 * @param filePath
	 * @return
	 */
	public static boolean existsFile(String filePath) {
		File file = new File(filePath);
		return existsFile(file);
	}

	/**
	 * ��ȡ����ļ���Ŀ¼
	 * 
	 * @param context
	 * @return
	 */
	public static File getAdPath(Context context) {
		File file = new File(context.getCacheDir().getAbsoluteFile()
				+ File.separator + "ad");
		if (!existsFile(file)) {
			file.mkdir();
		}
		return file;
	}

	/**
	 * �г�rootĿ¼��������Ŀ¼
	 * 
	 * @param path
	 * @return ����·��
	 */
	public static List<String> listPath(String root) {
		List<String> allDir = new ArrayList<String>();
		SecurityManager checker = new SecurityManager();
		File path = new File(root);
		checker.checkRead(root);
		// ���˵���.��ʼ���ļ���
		if (path.isDirectory()) {
			for (File f : path.listFiles()) {
				if (f.isDirectory() && !f.getName().startsWith(".")) {
					allDir.add(f.getAbsolutePath());
				}
			}
		}
		return allDir;
	}

	/**
	 * ��ȡһ���ļ����µ������ļ�
	 * 
	 * @param root
	 * @return
	 */
	public static List<File> listPathFiles(String root) {
		List<File> allDir = new ArrayList<File>();
		SecurityManager checker = new SecurityManager();
		File path = new File(root);
		checker.checkRead(root);
		File[] files = path.listFiles();
		for (File f : files) {
			if (f.isFile())
				allDir.add(f);
			else
				listPath(f.getAbsolutePath());
		}
		return allDir;
	}

	/**
	 * ɾ��֮ǰ�Ĺ��
	 */
	private static void deleteOldAd(Context context) {
		String path = FileUtils.getAdPath(context).getAbsolutePath();
		List<File> list = FileUtils.listPathFiles(path);
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).exists()) {
				list.get(i).delete();
			}
		}
	}

	public static void write(Context context, String fileName, String content) {
		if (content == null)
			content = "";
		Log.d("", "wmz:fileName=" + fileName);
		try {
			FileOutputStream fos = context.openFileOutput(fileName,
					Context.MODE_PRIVATE);
			fos.write(content.getBytes());
			Log.d("", "wmz:file-ok");
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void writeFile(Context context, String filePath, byte[] data) {
		if (data == null)
			data = "".getBytes();
		try {
			Long startTime = System.currentTimeMillis();
			Log.d("", "wmz:startTime=" + startTime);
			FileOutputStream fileOutputStream = new FileOutputStream(filePath);
			fileOutputStream.write(data);
			Log.d("", "wmz:time=" + (System.currentTimeMillis() - startTime));
			fileOutputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
