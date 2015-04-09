//@format
package com.everhomes.sms;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.everhomes.cache.CacheProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.db.DbProvider;
import com.everhomes.util.RuntimeErrorException;

/**
 * TODO To manage throughput and throttling, SMS/email notification service
 * should go through queue service.
 * 
 * For now, it is a fake implementation
 * 
 * 
 * @author Kelven Yang
 *
 */
public class SmsProviderImpl extends AbstractSmsProvider {

	@Autowired(required = true)
	private TaskQueue taskQueue;

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private CacheProvider cacheProvider;

	private final Map<String, SmsProvider> providers;

	@Autowired
	public SmsProviderImpl(DbProvider dbProvider, CacheProvider cacheProvider,
			TaskQueue taskQueue) {
		super(dbProvider, cacheProvider);
		this.cacheProvider = cacheProvider;
		this.dbProvider = dbProvider;
		this.taskQueue = taskQueue;
		providers = new HashMap<String, SmsProvider>();
		scanClass();
	}

	private void scanClass() {
		Map<String, Class<?>> providerClz = SmsHepler.scanSmsProviders();
		providerClz.forEach((val, clz) -> {
			try {
				providers.put(
						val,
						(SmsProvider) clz.getConstructor(DbProvider.class,
								CacheProvider.class).newInstance(dbProvider,
								cacheProvider));
			} catch (Exception e) {
				LOGGER.error("can not create sms provider.annName={},class={}",
						val, clz);
			}
		});
		LOGGER.info("init ok.providers={}.class={}", providers, providerClz);
	}

	private SmsProvider getProvider() {
		// find name from db
		String providerName = "MW";
		SmsProvider provider = providers.get(providerName);
		if (provider == null) {
			LOGGER.error("cannot find relate provider.providerName={}",
					providerName);
			throw RuntimeErrorException
					.errorWith(ErrorCodes.SCOPE_GENERAL,
							ErrorCodes.ERROR_GENERAL_EXCEPTION,
							"can not find relate sms provider.provider="
									+ providerName);
		}
		return provider;
	}

	@Override
	protected void doSend(String phoneNumber, String text) {
		LOGGER.info("Send SMS text:\"{}\" to {}.beginTime={}",
				SmsHepler.getEncodingString(text), phoneNumber,
				System.currentTimeMillis());
		Future<?> f = taskQueue.submit(() -> {
			getProvider().sendSms(
					phoneNumber,
					// replace special character
					SmsHepler.getEncodingString(text).replace(" ", "%20")
							.replace("=", "%3D"));
			LOGGER.info("send sms message ok.endTime={}",
					System.currentTimeMillis());
			return null;
		});
		try {
			f.get();
		} catch (InterruptedException | ExecutionException e) {
			LOGGER.error("send sms message error", e);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
					ErrorCodes.ERROR_GENERAL_EXCEPTION, e.getMessage());
		}
	}

	@Override
	protected void doSend(String[] phoneNumbers, String text) {
		LOGGER.debug("Send SMS text:\"{}\" to {}.beginTime={}",
				SmsHepler.getEncodingString(text),
				StringUtils.join(phoneNumbers, ","), System.currentTimeMillis());
		Future<?> f = taskQueue.submit(() -> {
			getProvider().sendSms(phoneNumbers,
					SmsHepler.getEncodingString(text));
			LOGGER.info("send sms message ok.endTime={}",
					System.currentTimeMillis());
			return null;
		});
		try {
			f.get();
		} catch (InterruptedException | ExecutionException e) {
			LOGGER.error("send sms message error", e);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
					ErrorCodes.ERROR_GENERAL_EXCEPTION, e.getMessage());
		}
	}
}
