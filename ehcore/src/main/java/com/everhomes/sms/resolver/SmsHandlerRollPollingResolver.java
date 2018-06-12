package com.everhomes.sms.resolver;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.server.schema.tables.pojos.EhSmsLogs;
import com.everhomes.sms.SmsHandler;
import com.everhomes.sms.SmsHandlerResolver;
import com.everhomes.sms.SmsLog;
import com.everhomes.sms.SmsLogProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by xq.tian on 2018/1/13.
 */
@Component(SmsHandlerResolver.RESOLVER_NAME_PREFIX + "ROLL_POLLING")
public class SmsHandlerRollPollingResolver extends AbstractSmsHandlerResolver {

    private final ThreadLocalRandom random = ThreadLocalRandom.current();

    @Autowired
    private ConfigurationProvider configurationProvider;

    @Autowired
    private SmsLogProvider smsLogProvider;

    @Override
    public Map<SmsHandler, String[]> resolveHandler(Integer namespaceId, String[] phoneNumbers) {
        String handlerStr = configurationProvider.getValue(namespaceId, SMS_HANDLER_TYPE, "YZX");
        String[] configHandlers = handlerStr.split(",");

        Map<SmsHandler, List<String>> handlerToPhonesMap = new HashMap<>();

        for (String phoneNumber : phoneNumbers) {
            Map<String, SmsLog> smsLogMap = smsLogProvider.findLastLogByMobile(namespaceId, phoneNumber, configHandlers);
            List<String> unknownHandler = getUnknownHandlers(configHandlers, smsLogMap.keySet());
            if (unknownHandler.size() > 0) {
                SmsHandler hd = handlers.get(unknownHandler.get(random.nextInt(0, unknownHandler.size())).toLowerCase());
                putHandlerAndPhone(handlerToPhonesMap, phoneNumber, hd);
            } else {
                smsLogMap.values().stream().min(Comparator.comparing(EhSmsLogs::getCreateTime)).ifPresent(r -> {
                    SmsHandler hd = handlers.get(r.getHandler().toLowerCase());
                    putHandlerAndPhone(handlerToPhonesMap, phoneNumber, hd);
                });
            }
        }

        Map<SmsHandler, String[]> handlerToPhonesArrMap = new HashMap<>();
        handlerToPhonesMap.forEach((k, v) -> handlerToPhonesArrMap.put(k, v.toArray(new String[v.size()])));

        return handlerToPhonesArrMap;
    }

    private void putHandlerAndPhone(Map<SmsHandler, List<String>> handlerToPhonesMap, String phoneNumber, SmsHandler hd) {
        List<String> inMapPhones = handlerToPhonesMap.get(hd);
        if (inMapPhones == null) {
            List<String> tempList = new ArrayList<>();
            tempList.add(phoneNumber);
            handlerToPhonesMap.put(hd, tempList);
        } else {
            inMapPhones.add(phoneNumber);
        }
    }

    private List<String> getUnknownHandlers(String[] configHandlers, Set<String> sent) {
        List<String> unknownHandler = new ArrayList<>();
        for (String handler : configHandlers) {
            if (!sent.contains(handler)) {
                unknownHandler.add(handler);
            }
        }
        return unknownHandler;
    }
}
