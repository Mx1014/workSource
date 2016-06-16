package com.everhomes.payment.taotaogu;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class PaidResultThreadPool {
	static ExecutorService  instance;
	private PaidResultThreadPool(){}
	
	public synchronized static ExecutorService  getInstance(){
		if(instance == null){
			instance = Executors.newCachedThreadPool();
		}
		return instance;
	}
}
