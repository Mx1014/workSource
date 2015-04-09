package com.everhomes.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.everhomes.cache.CacheProvider;
import com.everhomes.db.DbProvider;
import com.everhomes.sms.SmsProvider;
import com.everhomes.sms.SmsProviderImpl;
import com.everhomes.sms.TaskQueue;

@Configuration
public class SmsProviderConfig {
	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private CacheProvider cacheProvider;

	@Bean(initMethod = "init", destroyMethod = "destroy")
	TaskQueue taskQueue() {
		return new TaskQueue();
	}

	@Bean
	SmsProvider smsProvider() {
		return new SmsProviderImpl(dbProvider, cacheProvider, taskQueue());
	}
}

