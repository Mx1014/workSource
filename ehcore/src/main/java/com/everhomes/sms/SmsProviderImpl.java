//@formatter: off
package com.everhomes.sms;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.util.Tuple;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private static final String SMS_HANDLER_RESOLVER_NAME = "sms.handlerResolverName";

    private Map<String, SmsHandlerResolver> resolvers = new HashMap<>();

    @Autowired
    private ConfigurationProvider configurationProvider;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private SmsLogProvider smsLogProvider;

    @Autowired
    public void setHandlers(Map<String, SmsHandlerResolver> resolverMap) {
        resolverMap.forEach((name, resolver) -> resolvers.put(name, resolver));
    }

    /*private void doSend(String phoneNumber, String text, String templateId) {
        LOGGER.info("Send SMS text:\"{}\" to {}.beginTime={}", SmsHepler.getEncodingString(text), phoneNumber,
                System.currentTimeMillis());
        String escapedText = SmsHepler.convert(text);

        publishEvent(() -> getHandler(Namespace.DEFAULT_NAMESPACE, phoneNumber).doSend(phoneNumber, escapedText, templateId));
    }

    private void doSend(String[] phoneNumbers, String text, String templateId) {
        LOGGER.info("Send SMS text:\"{}\" to {}.beginTime={}", SmsHepler.getEncodingString(text),
                StringUtils.join(phoneNumbers, ","), System.currentTimeMillis());
        String escapedText = SmsHepler.convert(text);

        publishEvent(() -> {
            Map<SmsHandler, String[]> handler = getHandler(Namespace.DEFAULT_NAMESPACE, phoneNumbers);
            handler.forEach((k, v) -> {
                k.doSend(v, SmsHepler.getEncodingString(escapedText), templateId);
                LOGGER.info("send sms message ok.endTime={}", System.currentTimeMillis());
            });
        });
    }*/

    @Override
    public void sendSms(Integer namespaceId, String phoneNumber, String templateScope, int templateId, String templateLocale, List<Tuple<String, Object>> variables) {
        sendSms(null, namespaceId, new String[]{phoneNumber}, templateScope, templateId, templateLocale, variables);
    }

    @Override
    public void sendSms(Integer namespaceId, String[] phoneNumbers, String templateScope, int templateId, String templateLocale, List<Tuple<String, Object>> variables) {
        sendSms(null, namespaceId, phoneNumbers, templateScope, templateId, templateLocale, variables);
    }

    @Override
    public void sendSms(String handlerName, Integer namespaceId, String phoneNumber, String templateScope, int templateId, String templateLocale, List<Tuple<String, Object>> variables) {
        sendSms(handlerName, namespaceId, new String[]{phoneNumber}, templateScope, templateId, templateLocale, variables);
    }

    @Override
    public void sendSms(String handlerName, Integer namespaceId, String[] phoneNumbers, String templateScope, int templateId, String templateLocale, List<Tuple<String, Object>> variables) {
        doSend(handlerName, namespaceId, phoneNumbers, templateScope, templateId, templateLocale, variables);
    }

    private void doSend(String handlerName, Integer namespaceId, String[] phoneNumbers, String templateScope, int templateId, String templateLocale, List<Tuple<String, Object>> variables) {
        Map<SmsHandler, String[]> handlersMap;

        handlersMap = getSmsHandlerMap(handlerName, namespaceId, phoneNumbers);

        handlersMap.forEach((handler, phones) -> {
            publishEvent(() -> {
                List<SmsLog> logs = handler.doSend(namespaceId, phones, templateScope, templateId, templateLocale, variables);
                if (logs != null) {
                    saveSmsLog(logs);
                }
            });
        });

        if (LOGGER.isDebugEnabled()) {
            LOGGER.info("Send sms message, namespaceId=" + namespaceId + ", phoneNumbers=[" + StringUtils.join(phoneNumbers, ",")
                    + "], templateScope=" + templateScope + ", templateId=" + templateId + ", templateLocale=" + templateLocale);
        }

    }

    private Map<SmsHandler, String[]> getSmsHandlerMap(String handlerName, Integer namespaceId, String[] phoneNumbers) {
        String val = configurationProvider.getValue(SMS_HANDLER_RESOLVER_NAME, "SMART");

        SmsHandlerResolver resolver = resolvers.get(SmsHandlerResolver.RESOLVER_NAME_PREFIX + val);

        Map<SmsHandler, String[]> handlersMap;
        if (handlerName != null && handlerName.length() > 0) {
            handlersMap = new HashMap<>();
            handlersMap.put(resolver.getHandlerByName(handlerName), phoneNumbers);
        } else {
            handlersMap = resolver.resolveHandler(namespaceId, phoneNumbers);
        }
        return handlersMap;
    }

    private void saveSmsLog(List<SmsLog> smsLogList) {
        for (SmsLog smsLog : smsLogList) {
            smsLogProvider.createSmsLog(smsLog);
        }
    }

    public List<Tuple<String, Object>> toTupleList(String key, Object value) {
        List<Tuple<String, Object>> list = new ArrayList<>();
        Tuple<String, Object> variable = new Tuple<>(key, value);
        list.add(variable);
        return list;
    }

    public void addToTupleList(List<Tuple<String, Object>> list, String key, Object value) {
        Tuple<String, Object> variable = new Tuple<>(key, value);
        list.add(variable);
    }

    private void publishEvent(SmsCallback callback) {
        applicationEventPublisher.publishEvent(new SendSmsEvent(callback));
    }
}
