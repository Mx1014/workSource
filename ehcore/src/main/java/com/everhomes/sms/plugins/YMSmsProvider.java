//package com.everhomes.sms.plugins;
//
//import cn.emay.sdk.client.api.Client;
//
//import com.everhomes.cache.CacheProvider;
//import com.everhomes.constants.ErrorCodes;
//import com.everhomes.db.DbProvider;
//import com.everhomes.sms.AbstractSmsProvider;
//import com.everhomes.sms.SmsHandler;
//import com.everhomes.util.RuntimeErrorException;
//
//@SmsHandler(value = "YM")
//public class YMSmsProvider extends AbstractSmsProvider {
//	private final Client client;
//
//	private final int smsPriority;
//
//	public YMSmsProvider(DbProvider dbProvider, CacheProvider cacheProvider)
//			throws Exception {
//		super(dbProvider, cacheProvider);
//		// get config item from db or cache
//		client = new Client("", "");
//		smsPriority = 1;// get from db
//	}
//
//	@Override
//	public void sendSms(String phoneNumber, String text) {
//		int ret = client.sendSMS(new String[] { phoneNumber }, text, "",
//				smsPriority);
//		validateRsp(ret);
//	}
//
//	@Override
//	public void sendSms(String[] phoneNumbers, String text) throws Exception {
//		int ret = client.sendSMS(phoneNumbers, text, "", smsPriority);
//		validateRsp(ret);
//
//	}
//
//	private void validateRsp(int ret) {
//		if (ret != 0) {
//			logger.error("send sms message failed.return code={}", ret);
//			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
//					ErrorCodes.ERROR_GENERAL_EXCEPTION,
//					"Send sms message error");
//		}
//		logger.info("send message ok");
//	}
//
//}
