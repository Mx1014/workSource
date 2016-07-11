package com.everhomes.util; 

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * 线程池,任务调度工具类
 * @author sfyan
 * @date 2016年3月25日 上午2:06:27
 *
 */
public final class ExecutorUtil {

	private static ScheduledExecutorService schExecutor = ThreadUtil.newScheduledExecutorService(2, "content-cloud-task");
	
	/**
	 * 线程池
	 */
	private static ExecutorService threadpool = ThreadUtil.newExecutorService(5, 150, 150, 1800, "content-cloud-exe");
	
	/**
	 * 按延迟时长调度一次任务
	 * @param task -  任务
	 * @param delay -  延迟时长,单位:毫秒
	 * @return - 执行期望值
	 */
	public static ScheduledFuture<?> schedule(Runnable task, long delay) {
		return schExecutor.schedule(task, delay, TimeUnit.MILLISECONDS);
	}
	
	/**
	 * 执行任务
	 * @param task - 任务
	 * @return - 执行期望值
	 */
	public static Future<?> submit(Runnable task) {
		return threadpool.submit(task); 
	}
}

