package com.everhomes.util;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池工厂类
 * @author sfyan
 * @date 2016年3月25日 下午6:44:23
 * @version 
 * @since
 */
public final class ThreadUtil {
	
	/**
	 * 根据参数创建执行者服务
	 * @param coreSize -- 线程池核心线程数
	 * @param maxSize -- 线程池最大线程数
	 * @param queueSize -- 线程池等待队列长度
	 * @param keepAlive -- 线程最大空闲时间(单位:秒)
	 * @param nameTemplate -- 线程名称模板
	 * @return -- 执行者服务
	 */
	public static ExecutorService newExecutorService(int coreSize, 
													 int maxSize,
													 int queueSize,
													 int keepAlive,
													final String nameTemplate) {
		
		BlockingQueue<Runnable> queue =  new ArrayBlockingQueue<Runnable>(queueSize);
		final ThreadGroup tg = new ThreadGroup(nameTemplate);
		tg.setDaemon(true);
		ThreadFactory fac = new ThreadFactory() { 
			
			private int index = 0;
			
			// 创建一个新的线程, 同时设置它的名称和daemon模式
			public Thread newThread(Runnable r) { 
				
				long stackSize = 256 * 1024;
				String tn = nameTemplate + "_" + index++; 
				Thread t = new Thread(tg, r, tn, stackSize);
				t.setDaemon(true);
				return t;
			} 
		}; 
		ThreadPoolExecutor tp = new ThreadPoolExecutor(coreSize, maxSize, keepAlive, TimeUnit.SECONDS, queue, fac);
		tp.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy()); // 当达到阀值后使用当前调用线程执行任务
		return tp;
	}
	
	/**
	 * 根据参数创建调度执行者服务
	 * @param coreSize -- 线程池核心线程数
	 * @param nameTemplate -- 线程名称模板
	 * @return -- 调度执行者服务
	 */
	public static ScheduledExecutorService newScheduledExecutorService(int coreSize, final String nameTemplate) {

		final ThreadGroup tg = new ThreadGroup(nameTemplate);
		tg.setDaemon(true);
		
		ThreadFactory fac = new ThreadFactory() {
			
			private int index = 0;
			
			// 创建一个新的线程, 同时设置它的名称和daemon模式
			public Thread newThread(Runnable r) { 
				
				long stackSize = 256 * 1024;
				String tn = nameTemplate + "_" + index++;
				Thread t = new Thread(tg, r, tn, stackSize);
				t.setDaemon(true);
				return t;
			} 
		}; 
		ScheduledThreadPoolExecutor te = new ScheduledThreadPoolExecutor(coreSize, fac);
		te.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy()); // 当达到阀值后使用当前调用线程执行任务
		return te;
	}
	
	/**
	 * 睡眠当前线程
	 * @param millis - 睡眠毫秒数
	 */
	@SuppressWarnings("static-access")
	public static void sleep(long millis) {
		try {
			Thread.currentThread().sleep(millis);
		} catch (Exception ex) { }
	}
			
	/**
	 * 防止非法实例化
	 */
	private ThreadUtil() { }
	
}
