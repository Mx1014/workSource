package com.everhomes.sms;

import java.util.List;

import com.everhomes.util.Tuple;

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
    
    void doSend(Integer namespaceId, String phoneNumber, String templateScope, int templateId, String templateLocale, List<Tuple<String, Object>> variables);
    void doSend(Integer namespaceId, String[] phoneNumbers, String templateScope, int templateId, String templateLocale, List<Tuple<String, Object>> variables);
}
