package com.everhomes.sms;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.everhomes.cache.CacheProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.db.DbProvider;
import com.everhomes.util.RuntimeErrorException;

public abstract class AbstractSmsProvider implements SmsProvider {

	protected final static Logger LOGGER = LoggerFactory
			.getLogger(SmsProvider.class);

	public AbstractSmsProvider(DbProvider dbProvider,
			CacheProvider cacheProvider) {

	}

	@Override
	public void sendSms(String phoneNumber, String text) {
		doSend(phoneNumber, convert(text));
	}

	@Override
	public void sendSms(String[] phoneNumbers, String text) throws Exception {
		doSend(phoneNumbers, convert(text));
	}

	private String convert(String text) {
		if (StringUtils.isEmpty(text)) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
					ErrorCodes.ERROR_GENERAL_EXCEPTION, "message is empty");
		}
		return text.replace(" ", "%20").replace("=", "%3D");
	}

	protected abstract void doSend(String phoneNumber, String text);

	protected abstract void doSend(String[] phoneNumbers, String text);

}
