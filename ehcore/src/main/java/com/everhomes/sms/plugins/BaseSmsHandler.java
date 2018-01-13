package com.everhomes.sms.plugins;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.locale.LocaleTemplate;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.namespace.Namespace;
import com.everhomes.rest.sms.SmsTemplateCode;
import com.everhomes.sms.*;
import com.everhomes.util.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by xq.tian on 2018/1/11.
 */
abstract public class BaseSmsHandler implements SmsHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseSmsHandler.class);

    private static final int MAX_LIMIT = 100;

    @Autowired
    protected ConfigurationProvider configurationProvider;

    @Autowired
    protected LocaleTemplateService localeTemplateService;

    @Override
    public SmsLog doSend(Integer namespaceId, String phoneNumber, String templateScope, int templateId,
                         String templateLocale, List<Tuple<String, Object>> variables) {
        List<SmsLog> smsLogs = doSend(namespaceId, new String[]{phoneNumber}, templateScope, templateId, templateLocale, variables);
        if (smsLogs != null && smsLogs.size() > 0) {
            return smsLogs.get(0);
        }
        return null;
    }

    @Override
    public List<SmsLog> doSend(Integer namespaceId, String[] phoneNumbers, String templateScope, int templateId,
                               String templateLocale, List<Tuple<String, Object>> variables) {
        initAccount();

        List<SmsLog> smsLogList = new ArrayList<>();

        Map<String, Object> model = new HashMap<>();
        if (variables != null) {
            model = variables.stream().collect(Collectors.toMap(Tuple::first, Tuple::second));
        }

        String signScope = SmsTemplateCode.SCOPE + ".sign";
        LocaleTemplate sign = localeTemplateService.getLocalizedTemplate(namespaceId, signScope, SmsTemplateCode.SIGN_CODE, templateLocale);
        if (sign == null) {
            LOGGER.error("can not found sign content by "+getHandlerName()+" handler, namespaceId = {}", namespaceId);
            SmsLog log = getSmsErrorLog(namespaceId, phoneNumbers[0], templateScope, templateId, templateLocale, "sms sign is empty");
            smsLogList.add(log);
            return smsLogList;
        }

        templateScope = templateScope + "." + getHandlerName();
        String content = localeTemplateService.getLocaleTemplateString(namespaceId, SmsTemplateCode.SCOPE, templateId, templateLocale, model, "");
        if (content == null || content.isEmpty()) {
            content = localeTemplateService.getLocaleTemplateString(namespaceId, SmsTemplateCode.SCOPE, templateId, templateLocale, model, "");
        }
        if (content == null || content.isEmpty()) {
            content = localeTemplateService.getLocaleTemplateString(Namespace.DEFAULT_NAMESPACE, SmsTemplateCode.SCOPE, templateId, templateLocale, model, "");
        }
        if (content == null || content.isEmpty()) {
            content = localeTemplateService.getLocaleTemplateString(Namespace.DEFAULT_NAMESPACE, SmsTemplateCode.SCOPE, templateId, templateLocale, model, "");
        }

        if (content != null && content.trim().length() > 0) {
            for (int i = 0; i < phoneNumbers.length; i += MAX_LIMIT) {
                int length = MAX_LIMIT;
                if (i + MAX_LIMIT > phoneNumbers.length) {
                    length = phoneNumbers.length - i;
                }
                String[] phonesPart = new String[length];
                System.arraycopy(phoneNumbers, i, phonesPart, 0, length);

                // Map<String, String> message = new HashMap<>();
                // message.put("phones", StringUtils.join(phonesPart, ","));
                // message.put("content", content);
                // message.put("sign", sign.getText());

                RspMessage rspMessage = createAndSend(phonesPart, sign.getText(), content);
                smsLogList.addAll(buildSmsLogs(namespaceId, phonesPart, templateScope, templateId, templateLocale, sign.getText()+content, rspMessage));
            }
        } else {
            SmsLog log = getSmsErrorLog(namespaceId, phoneNumbers[0], templateScope, templateId, templateLocale, "sms content is empty");
            smsLogList.add(log);
            LOGGER.error("can not found sms content by "+getHandlerName()+" namespaceId = {}, templateId = {}", namespaceId, templateId);
        }
        return smsLogList;
    }

    protected SmsLog getSmsErrorLog(Integer namespaceId, String phoneNumber, String templateScope, int templateId, String templateLocale, String error) {
        SmsLog log = new SmsLog();
        log.setCreateTime(new Timestamp(System.currentTimeMillis()));
        log.setNamespaceId(namespaceId);
        log.setScope(templateScope);
        log.setCode(templateId);
        log.setLocale(templateLocale);
        log.setMobile(phoneNumber);
        log.setResult(error);
        log.setHandler(getHandlerName());
        log.setText("");
        log.setStatus(SmsLogStatus.SEND_FAILED.getCode());
        return log;
    }

    abstract String getHandlerName();

    abstract void initAccount();

    abstract RspMessage createAndSend(String[] phones, String sign, String content);

    abstract List<SmsLog> buildSmsLogs(Integer namespaceId, String[] phoneNumbers, String templateScope, int templateId,
                                      String templateLocale, String content, RspMessage rspMessage);
}
