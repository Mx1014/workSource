package com.everhomes.sms.plugins;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.sms.SmsBuilder;
import com.everhomes.sms.SmsChannel;
import com.everhomes.sms.SmsHandler;
import com.everhomes.util.Tuple;

@Component("Lsm")
@DependsOn("platformContext")
public class LsmSmsHandler implements SmsHandler {
    protected final static Logger LOGGER = LoggerFactory.getLogger(LsmSmsHandler.class);
    
    private static final String LSM_ACCOUNT_STR = "lsm.account";

    private static final String LSM_HOST = "lsm.address";

    private SmsChannel channel;

    private String hostAddress;

    @Autowired
    private ConfigurationProvider configurationProvider;

    @PostConstruct
    public void init() {
        String account = configurationProvider.getValue(LSM_ACCOUNT_STR, "user:password");
        String[] arr = account.split(":");
        channel = SmsBuilder.create(false).addHeader("Accept-Encoding", "gzip");
        channel.basicAuth(arr[0], arr[1]).setTimeout(30000);
        hostAddress = configurationProvider.getValue(LSM_HOST, "");
    }

    @Override
    public void doSend(String phoneNumber, String text) {
        sendMessage(phoneNumber, text);
    }

    @Override
    public void doSend(String[] phoneNumbers, String text) {
        for (String phoneNumber : phoneNumbers) {
            sendMessage(phoneNumber, text);
        }
    }

    private void sendMessage(String phoneNumber, String text) {
        Map<String, String> body = new HashMap<>();
        body.put("mobile", phoneNumber);
        body.put("message", text);
        String rsp = channel.sendMessage(hostAddress, SmsBuilder.HttpMethod.POST.val(), body, null, null).getMessage();
        LOGGER.info("send message success.Return message msg={}", rsp);
    }

    @Override
    public void doSend(String phoneNumber, String text, String templateId) {
        doSend(phoneNumber,text);
        
    }

    @Override
    public void doSend(String[] phoneNumbers, String text, String templateId) {
        doSend(phoneNumbers,text);
        
    }

    @Override
    public void doSend(Integer namespaceId, String phoneNumber, String templateScope, int templateId,
        String templateLocale, List<Tuple<String, Object>> variables) {
        // Lsm厂商已经不支持，故不实现
    }

    @Override
    public void doSend(Integer namespaceId, String[] phoneNumbers, String templateScope, int templateId,
        String templateLocale, List<Tuple<String, Object>> variables) {
        // Lsm厂商已经不支持，故不实现
    }
}
