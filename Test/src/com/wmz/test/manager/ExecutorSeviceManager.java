package com.wmz.test.manager;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.text.GetChars;

/**
 * �̳߳ع�����
 * 
 * @author wmz
 * 
 */
public class ExecutorSeviceManager {
	private static ExecutorService executorService = Executors
			.newFixedThreadPool(3);

	/**
	 * ��ȡ�̳߳�ʵ��
	 * 
	 * @return
	 */
	public static ExecutorService getExecutorInstance() {
		return executorService;
	}

	public static void execute(Runnable command) {
		if (executorService != null && !executorService.isShutdown()) {
			executorService.execute(command);
		}
	}

	public static void shutdown() {
		if (executorService != null) {
			executorService.shutdown();
		}
	}

	public static void shutdownNow() {
		if (executorService != null) {
			executorService.shutdownNow();
		}
	}
}
