package com.everhomes.sms;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SMSBuilder {
	private final static SMSBuilder builder = new SMSBuilder();
	private final Map<Boolean, SMSChannel> cache;

	public enum HttpMethod {
		POST("POST"), PATCH("PATCH"), GET("GET"), DELETE("DELETE");
		private String method;

		HttpMethod(String method) {
			this.method = method;
		}

		public String val() {
			return this.method;
		}
	}

	private SMSBuilder() {
		cache = new ConcurrentHashMap<>();
		cache.put(true, new SMSChannel(true));
		cache.put(false, new SMSChannel(false));
	}

	public SMSChannel getChannel(boolean isSecure) {
		return cache.get(isSecure);
	}

	public static SMSChannel create(boolean isSecure) {
		return builder.getChannel(isSecure);
	}

}
