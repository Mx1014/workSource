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
    
    /**
     * 
     * @param phoneNumber
     * @param text
     * @param templateId, yzx sms need templateId
     */
    void doSend(String phoneNumber, String text, String templateId);
    
    /**
     * 
     * @param phoneNumbers
     * @param text
     * @param templateId,yzx sms need templateId
     */
    void doSend(String[] phoneNumbers, String text, String templateId);
}
