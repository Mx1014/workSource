package com.everhomes.sms.plugins;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.sms.AbstractSmsProvider;
import com.everhomes.sms.SmsBuilder;
import com.everhomes.sms.SmsChannel;
import com.everhomes.sms.SmsHandler;

@Component
@SmsHandler(value = "LSM")
public class LsmSmsProvider extends AbstractSmsProvider {
    private SmsChannel channel;

    private String hostAddress;

    @Autowired
    private ConfigurationProvider configurationProvider;

    @PostConstruct
    public void init() {
        String username = configurationProvider.getValue("", "");
        String password = configurationProvider.getValue("", "");
        channel = SmsBuilder.create(false).addHeader("Accept-Encoding", "gzip");
        channel.basicAuth(username, password).setTimeout(30000);
        hostAddress = configurationProvider.getValue("", "");// query db
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
        String rsp = channel.sendMessage(hostAddress, SmsBuilder.HttpMethod.POST.val(), body, null).getMessage();
        LOGGER.info("send message success.Return message msg={}", rsp);
    }

}
