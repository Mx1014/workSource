package com.everhomes.sms.plugins;

import java.util.HashMap;
import java.util.Map;

import com.everhomes.cache.CacheProvider;
import com.everhomes.db.DbProvider;
import com.everhomes.sms.AbstractSmsProvider;
import com.everhomes.sms.SmsBuilder;
import com.everhomes.sms.SmsChannel;
import com.everhomes.sms.SmsHandler;

@SmsHandler(value = "LSM")
public class LsmSmsProvider extends AbstractSmsProvider {
	private final SmsChannel channel;

	private final String hostAddress;

	public LsmSmsProvider(DbProvider dbProvider, CacheProvider cacheProvider) {
		super(dbProvider, cacheProvider);
		String username = "";// query from db or cache
		String password = "";// query from db or cache
		channel = SmsBuilder.create(false).addHeader("Accept-Encoding", "gzip");
		channel.basicAuth(username, password).setTimeout(30000);
		hostAddress = "";// query db
	}

	@Override
	public void doSend(String phoneNumber, String text) {
		sendMessage(phoneNumber, text);
	}

	@Override
	public void doSend(String[] phoneNumbers, String text) {
		for (String phoneNumber : phoneNumbers) {
			sendMessage(phoneNumber, text);
		}
	}

	private void sendMessage(String phoneNumber, String text) {
		Map<String, String> body = new HashMap<>();
		body.put("mobile", phoneNumber);
		body.put("message", text);
		String rsp = channel.sendMessage(hostAddress, SmsBuilder.HttpMethod.POST.val(), body, null).getMessage();
		LOGGER.info("send message success.Return message msg={}", rsp);
	}

}
