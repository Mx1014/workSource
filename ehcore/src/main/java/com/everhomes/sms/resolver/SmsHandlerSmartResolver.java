package com.everhomes.sms.resolver;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.sms.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.*;

/**
 * Created by xq.tian on 2018/1/13.
 */
@Component(SmsHandlerResolver.RESOLVER_NAME_PREFIX + "SMART")
public class SmsHandlerSmartResolver extends AbstractSmsHandlerResolver {

    @Autowired
    private ConfigurationProvider configurationProvider;

    @Autowired
    private SmsLogProvider smsLogProvider;

    @Override
    public Map<SmsHandler, String[]> resolveHandler(Integer namespaceId, String[] phoneNumbers) {
        Map<SmsHandler, List<String>> handlerToPhonesMap = new HashMap<>();
        for (String phoneNumber : phoneNumbers) {
            SmsHandler handler = getHandler(namespaceId, phoneNumber);
            List<String> inMapPhones = handlerToPhonesMap.get(handler);
            if (inMapPhones == null) {
                List<String> tempList = new ArrayList<>();
                tempList.add(phoneNumber);
                handlerToPhonesMap.put(handler, tempList);
            } else {
                inMapPhones.add(phoneNumber);
            }
        }

        Map<SmsHandler, String[]> handlerToPhonesArrMap = new HashMap<>();
        handlerToPhonesMap.forEach((k, v) -> handlerToPhonesArrMap.put(k, v.toArray(new String[v.size()])));

        return handlerToPhonesArrMap;
    }

    private SmsHandler getHandler(Integer namespaceId, String phoneNumber) {
        String handlerStr = configurationProvider.getValue(namespaceId, SMS_HANDLER_TYPE, "YZX");
        String[] configHandlers = handlerStr.split(",");

        String selectedHandlerName = null;

        Map<String, SmsLog> handlerToSmsLogMap = smsLogProvider.findLastLogByMobile(namespaceId, phoneNumber, configHandlers);
        if (handlerToSmsLogMap != null) {
            if (handlerToSmsLogMap.size() < configHandlers.length) {
                Set<String> sentHandlers = handlerToSmsLogMap.keySet();
                for (String handler : configHandlers) {
                    if (!sentHandlers.contains(handler)) {
                        SmsLog smsLog = new SmsLog();
                        smsLog.setStatus(SmsLogStatus.UNKNOWN.getCode());
                        smsLog.setHandler(handler);
                        smsLog.setCreateTime(new Timestamp(System.currentTimeMillis() - 100 * 60 * 1000));
                        handlerToSmsLogMap.put(handler, smsLog);
                    }
                }
            }
            selectedHandlerName = selectHandler(handlerToSmsLogMap.entrySet(), SmsLogStatus.REPORT_SUCCESS);
        }
        if (selectedHandlerName == null || SmsLogStatus.fromCode(handlerToSmsLogMap.get(selectedHandlerName).getStatus()) == SmsLogStatus.REPORT_FAILED) {
            selectedHandlerName = configHandlers[0];
        }
        return handlers.get(selectedHandlerName.toLowerCase());
    }

    private String selectHandler(Set<Map.Entry<String, SmsLog>> entries, SmsLogStatus expectStatus) {
        String selectedHandlerName = null;
        for (Map.Entry<String, SmsLog> entry : entries) {
            SmsLog smsLog = entry.getValue();

            long interval = System.currentTimeMillis() - (smsLog.getCreateTime() != null ? smsLog.getCreateTime().getTime() : 0);

            SmsLogStatus status = SmsLogStatus.fromCode(smsLog.getStatus());
            if (status == expectStatus && interval > 3 * 60 * 1000) {
                selectedHandlerName = smsLog.getHandler();
                break;
            }
        }
        if (selectedHandlerName == null && expectStatus.ordinal() < SmsLogStatus.values().length - 1) {
            return selectHandler(entries, SmsLogStatus.values()[expectStatus.ordinal() + 1]);
        }
        return selectedHandlerName;
    }
}
