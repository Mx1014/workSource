//@format
package com.everhomes.sms;

import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;


import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
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
@Component
public class SmsProviderImpl implements SmsProvider {
    protected final static Logger LOGGER = LoggerFactory.getLogger(SmsProviderImpl.class);

    private static final String VCODE_SEND_TYPE = "sms.handler.type";

    @Autowired
    private TaskQueue taskQueue;

    @Autowired
    private ConfigurationProvider configurationProvider;

    @Autowired
    private Map<String, SmsHandler> handlers;

    private SmsHandler getHandler() {
        // find name from db
        String handlerName = configurationProvider.getValue(VCODE_SEND_TYPE, "WM");
        SmsHandler handler = handlers.get(handlerName);
        if (handler == null) {
            LOGGER.error("cannot find relate handler.handler={}", handlerName);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
                    "can not find relate sms handler.handler=" + handlerName);
        }
        return handler;
    }

    protected void doSend(String phoneNumber, String text) {
        LOGGER.info("Send SMS text:\"{}\" to {}.beginTime={}", SmsHepler.getEncodingString(text), phoneNumber,
                System.currentTimeMillis());
        String escapedText = convert(text);
       
        Future<?> f = taskQueue.submit(() -> {
            getHandler().doSend(phoneNumber, escapedText);
            LOGGER.info("send sms message ok.endTime={}", System.currentTimeMillis());
            return null;
        });
        try {
            f.get();
        } catch (InterruptedException | ExecutionException e) {
            LOGGER.error("send sms message error", e);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
                    e.getMessage());
        }
    }

    protected void doSend(String[] phoneNumbers, String text) {
        LOGGER.info("Send SMS text:\"{}\" to {}.beginTime={}", SmsHepler.getEncodingString(text),
                StringUtils.join(phoneNumbers, ","), System.currentTimeMillis());
        String escapedText = convert(text);
        Future<?> f = taskQueue.submit(() -> {
            getHandler().doSend(phoneNumbers, SmsHepler.getEncodingString(escapedText));
            LOGGER.info("send sms message ok.endTime={}", System.currentTimeMillis());
            return null;
        });
        try {
            f.get();
        } catch (InterruptedException | ExecutionException e) {
            LOGGER.error("send sms message error", e);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
                    e.getMessage());
        }
    }
    
    private static String convert(String text) {
        if (StringUtils.isEmpty(text)) {
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
                    "message is empty");
        }
        return SmsHepler.getEncodingString(text).replace(" ", "%20").replace("=", "%3D");
    }

    @Override
    public void sendSms(String phoneNumber, String text) {
        this.doSend(phoneNumber, text);
    }

    @Override
    public void sendSms(String[] phoneNumbers, String text) throws Exception {
        this.doSend(phoneNumbers, text);
    }
}
