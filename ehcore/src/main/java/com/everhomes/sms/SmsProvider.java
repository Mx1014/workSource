// @formatter:off
package com.everhomes.sms;

public interface SmsProvider {
    void sendSms(String phoneNumber, String text);
}
