package com.everhomes.sms;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.util.RuntimeErrorException;

public class SmsHepler {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(SmsHepler.class);
	private static final String[] encodings = { "GB2312", "ISO-8859-1",
			"UTF-8", "GBK" };

	public  static Map<String, Class<?>> scanSmsProviders() {
		Reflections reflections = new Reflections("com.everhomes.sms.plugins");
		Set<Class<?>> clz = reflections.getTypesAnnotatedWith(SmsHandler.class);
		LOGGER.info("find class in package,classes={}", clz);
		Map<String, Class<?>> ret = new HashMap<String, Class<?>>();
		clz.forEach(klass -> {
			SmsHandler smsInvok = klass.getAnnotation(SmsHandler.class);
			ret.put(smsInvok.value(), klass);
		});
		return ret;
	}

	public static String getEncodingString(String text) {
		for (String encode : encodings) {
			try {
				if (text.equals(new String(text.getBytes(encode), encode))) {
					LOGGER.info("current text encoding is {}",encode);
					return new String(text.getBytes(encode), "UTF-8");
				}
			} catch (Exception e) {
				// TODO
			}
		}
		LOGGER.error("cannot find encode type.text={}", text);
		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
				ErrorCodes.ERROR_INVALID_PARAMETER, "can not known encode");
	}

}
