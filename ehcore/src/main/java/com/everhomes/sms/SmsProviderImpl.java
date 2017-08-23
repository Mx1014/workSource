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

import java.util.*;

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
    private ConfigurationProvider configurationProvider;

    private Map<String, SmsHandler> handlers = new HashMap<>();

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private SmsLogProvider smsLogProvider;

    @Autowired
    public void setHandlers(Map<String, SmsHandler> prop) {
        prop.forEach((name, handler) -> handlers.put(name.toLowerCase(), handler));
    }

    private Map<SmsHandler, String[]> getHandler(Integer namespaceId, String[] phoneNumbers) {
        Map<SmsHandler, List<String>> handlerToPhonesMap = new HashMap<>();
        for (String phoneNumber : phoneNumbers) {
            SmsHandler handler = getHandler(namespaceId, phoneNumber);
            List<String> inMapPhones = handlerToPhonesMap.get(handler);
            if (inMapPhones == null) {
                handlerToPhonesMap.put(handler, Collections.singletonList(phoneNumber));
            } else {
                inMapPhones.add(phoneNumber);
            }
        }

        Map<SmsHandler, String[]> handlerToPhonesArrMap = new HashMap<>();
        handlerToPhonesMap.forEach((k, v) -> handlerToPhonesArrMap.put(k, v.toArray(new String[v.size()])));

        return handlerToPhonesArrMap;
    }

    private SmsHandler getHandler(Integer namespaceId, String phoneNumber) {
        String handlerStr = configurationProvider.getValue(namespaceId, VCODE_SEND_TYPE, "MW");
        String[] handlerNames = handlerStr.split(",");

        String selectedHandlerName = null;
        SmsLogStatus selectedStatus = null;

        Map<String, SmsLog> handlerToSmsLogMap = smsLogProvider.findLastLogByMobile(namespaceId, phoneNumber, handlerNames);
        if (handlerToSmsLogMap != null) {
            if (handlerToSmsLogMap.size() < handlerNames.length) {
                Set<String> sendHandlers = handlerToSmsLogMap.keySet();
                for (String handlerName : handlerNames) {
                    if (!sendHandlers.contains(handlerName)) {
                        SmsLog smsLog = new SmsLog();
                        smsLog.setStatus(SmsLogStatus.UNKNOWN.getCode());
                        handlerToSmsLogMap.put(handlerName, smsLog);
                    }
                }
            }
            selectedHandlerName = selectHandler(handlerToSmsLogMap.entrySet(), SmsLogStatus.REPORT_SUCCESS);
            selectedStatus = SmsLogStatus.fromCode(handlerToSmsLogMap.get(selectedHandlerName).getStatus());
        }

        if (selectedHandlerName == null || selectedStatus == SmsLogStatus.REPORT_FAILED) {
            selectedHandlerName = handlerNames[0];
        }
        return handlers.get(selectedHandlerName.toLowerCase());
    }

    private String selectHandler(Set<Map.Entry<String, SmsLog>> entries, SmsLogStatus expectStatus) {
        String selectedHandlerName = null;
        for (Map.Entry<String, SmsLog> entry : entries) {
            SmsLog smsLog = entry.getValue();

            long interval = System.currentTimeMillis() - smsLog.getCreateTime().getTime();

            SmsLogStatus status = SmsLogStatus.fromCode(smsLog.getStatus());
            if (status == expectStatus && interval > 3 * 60 * 1000) {
                selectedHandlerName = smsLog.getHandler();
                break;
            }
        }
        if (selectedHandlerName == null && expectStatus.ordinal() < SmsLogStatus.values().length) {
            return selectHandler(entries, SmsLogStatus.values()[expectStatus.ordinal() + 1]);
        }
        return selectedHandlerName;
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

        if (handlerName != null) {
            handlersMap = new HashMap<>();
            handlersMap.put(handlers.get(handlerName.toLowerCase()), phoneNumbers);
        } else {
            handlersMap = this.getHandler(namespaceId, phoneNumbers);
        }

        List<SmsLog> smsLogList = new ArrayList<>();

        handlersMap.forEach((handler, phones) -> {
            publishEvent(() -> {
                List<SmsLog> logs = handler.doSend(namespaceId, phones, templateScope, templateId, templateLocale, variables);
                if (logs != null) {
                    smsLogList.addAll(logs);
                }
            });
        });

        if (LOGGER.isDebugEnabled()) {
            LOGGER.info("Send sms message, namespaceId=" + namespaceId + ", phoneNumbers=[" + StringUtils.join(phoneNumbers, ",")
                    + "], templateScope=" + templateScope + ", templateId=" + templateId + ", templateLocale=" + templateLocale);
        }

        saveSmsLog(smsLogList);
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
