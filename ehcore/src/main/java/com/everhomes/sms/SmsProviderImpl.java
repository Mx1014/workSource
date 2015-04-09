//@format
package com.everhomes.sms;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
public class SmsProviderImpl extends AbstractSmsProvider {

    @Autowired(required = true)
    private TaskQueue taskQueue;

    @Autowired
    private List<SmsProvider> providers;

    private Map<String, SmsProvider> smsProviderCache;

    @PostConstruct
    public void init() {
        smsProviderCache = new HashMap<>();
        providers.forEach(provider -> {
            smsProviderCache.put(provider.getClass().getAnnotation(SmsHandler.class).value(), provider);
        });

    }

    private SmsProvider getProvider() {
        // find name from db
        String providerName = "MW";
        SmsProvider provider = smsProviderCache.get(providerName);
        if (provider == null) {
            LOGGER.error("cannot find relate provider.providerName={}", providerName);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
                    "can not find relate sms provider.provider=" + providerName);
        }
        return provider;
    }

    @Override
    protected void doSend(String phoneNumber, String text) {
        LOGGER.info("Send SMS text:\"{}\" to {}.beginTime={}", SmsHepler.getEncodingString(text), phoneNumber,
                System.currentTimeMillis());
        Future<?> f = taskQueue.submit(() -> {
            getProvider().sendSms(phoneNumber, text);
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

    @Override
    protected void doSend(String[] phoneNumbers, String text) {
        LOGGER.info("Send SMS text:\"{}\" to {}.beginTime={}", SmsHepler.getEncodingString(text),
                StringUtils.join(phoneNumbers, ","), System.currentTimeMillis());
        Future<?> f = taskQueue.submit(() -> {
            getProvider().sendSms(phoneNumbers, SmsHepler.getEncodingString(text));
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
}
