package com.everhomes.sms;

/**
 * send msm handler
 * 
 * @author elians
 *
 */
public interface SmsHandler {
    /**
     * single message
     * 
     * @param phoneNumber
     * @param text
     */
    void doSend(String phoneNumber, String text);

    /**
     * mass texting
     * 
     * @param phoneNumbers
     * @param text
     */
    void doSend(String[] phoneNumbers, String text);
}
