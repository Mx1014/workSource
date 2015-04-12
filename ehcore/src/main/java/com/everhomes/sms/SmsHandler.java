package com.everhomes.sms;

public interface SmsHandler {
    void doSend(String phoneNumber, String text);
    void doSend(String[] phoneNumbers, String text);
}
