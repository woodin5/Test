package com.wmz.test.manager;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * �̳߳ع�����
 * 
 * @author wmz
 * 
 */
public class ExecutorSeviceManager {
	private static ExecutorService executorService = Executors
			.newCachedThreadPool();

	/**
	 * ��ȡ�̳߳�ʵ��
	 * 
	 * @return
	 */
	public  static ExecutorService getExecutorInstance() {
		return executorService;
	}
}
