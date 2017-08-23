package com.everhomes.sms;

import com.everhomes.util.Tuple;

import java.util.List;

/**
 * send msm handler
 * 
 * @author elians
 *
 */
public interface SmsHandler {

    // 云之讯
    String YUN_ZHI_XUN_HANDLER_NAME = "YZX";
    // 优讯通
    String YOU_XUN_TONG_HANDLER_NAME = "YouXunTong";
    // 连信通
    String LIAN_XIN_TONG_HANDLER_NAME = "LianXinTong";

    /**
     * single message
     * 
     * @param phoneNumber
     * @param text
     */
    // void doSend(String phoneNumber, String text);

    /**
     * mass texting
     * 
     * @param phoneNumbers
     * @param text
     */
    // void doSend(String[] phoneNumbers, String text);
    
    /**
     * 
     * @param phoneNumber
     * @param text
     * @param templateId, yzx sms need templateId
     */
    // void doSend(String phoneNumber, String text, String templateId);
    
    /**
     * @param templateId,yzx sms need templateId
     */
    // void doSend(String[] phoneNumbers, String text, String templateId);

    /**
     * 状态报告
     * @param reportBody    报告结果
     * @return  返回响应内容
     */
    List<SmsReportDTO> report(String reportBody);
    
    SmsLog doSend(Integer namespaceId, String phoneNumber, String templateScope, int templateId, String templateLocale, List<Tuple<String, Object>> variables);

    List<SmsLog> doSend(Integer namespaceId, String[] phoneNumbers, String templateScope, int templateId, String templateLocale, List<Tuple<String, Object>> variables);
}
