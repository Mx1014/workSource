// @formatter:off
package com.everhomes.sms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * TODO To manage throughput and throttling, SMS/email notification service should
 * go through queue service.
 * 
 * For now, it is a fake implementation
 *  
 * 
 * @author Kelven Yang
 *
 */
@Component
public class SmsProviderImpl implements SmsProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(SmsProviderImpl.class);

    public void sendSms(String phoneNumber, String text) {
        LOGGER.info("Send SMS text:\" " + text + "\" to " + phoneNumber);
        // TODO
    }
}
