// @formatter:off
package com.everhomes.sms;

import com.everhomes.util.Tuple;

import java.util.List;

public interface SmsProvider {
    /**
     * 不存在短信模板的调用
     * @param phoneNumber
     * @param text
     */
    // void sendSms(String phoneNumber, String text);
    /**
     * 不存在短信模板的调用
     * @param phoneNumbers
     * @param text
     * @throws Exception
     */
    // void sendSms(String[] phoneNumbers, String text)throws Exception;
    
    /**
     * @param phoneNumber
     * @param text
     * @param templateId 没有模板设为null
     */
    // void sendSms(String phoneNumber, String text, String templateId);
    /**
     * @param phoneNumbers
     * @param text
     * @param templateId 没有模板设为null
     * @throws Exception
     */
    // void sendSms(String[] phoneNumbers, String text, String templateId)throws Exception;
    
    /**
     * 根据指定的模板发送短信
     * @param namespaceId 命名空间ID
     * @param phoneNumber 收短信的手机号
     * @param templateScope 模板类型，对应eh_locale_templates表
     * @param templateId 模板ID，对应eh_locale_templates表
     * @param templateLocale 模板语言，对应eh_locale_templates表
     * @param variables 翻译模板的参数（由于有些厂家自己定义模板，参数需要有顺序故使用List
     */
    void sendSms(Integer namespaceId, String phoneNumber, String templateScope, int templateId, String templateLocale, List<Tuple<String, Object>> variables);

    void sendSms(Integer namespaceId, String[] phoneNumbers, String templateScope, int templateId, String templateLocale, List<Tuple<String, Object>> variables);

    void sendSms(String handlerName, Integer namespaceId, String phoneNumber, String templateScope, int templateId, String templateLocale, List<Tuple<String, Object>> variables);

    void sendSms(String handlerName, Integer namespaceId, String[] phoneNumbers, String templateScope, int templateId, String templateLocale, List<Tuple<String, Object>> variables);

    List<Tuple<String, Object>> toTupleList(String key, Object value);

    void addToTupleList(List<Tuple<String, Object>> list, String key, Object value);
}