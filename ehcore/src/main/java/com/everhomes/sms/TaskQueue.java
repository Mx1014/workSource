package com.everhomes.sms;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 管理短信发送，如果宕机是不是需要存储信息?
 * 
 * @author elians
 *
 */
public class TaskQueue {
	private ExecutorService service;

	public void init() {
		service = Executors.newFixedThreadPool(2);
	}

	public void destroy() {
		if(service!=null)
		service.shutdown();
	}

	public Future<?> submit(Callable<?> task) {
		//need cache task or not?
		return service.submit(task);
	}
}
