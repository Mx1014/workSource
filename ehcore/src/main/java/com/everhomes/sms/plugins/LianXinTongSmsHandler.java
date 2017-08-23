package com.everhomes.sms.plugins;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.locale.LocaleTemplate;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.namespace.Namespace;
import com.everhomes.rest.sms.SmsTemplateCode;
import com.everhomes.sms.*;
import com.everhomes.util.StringHelper;
import com.everhomes.util.Tuple;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 连信通
 */
@Component(SmsHandler.LIAN_XIN_TONG_HANDLER_NAME)
public class LianXinTongSmsHandler implements SmsHandler {

    protected final static Logger LOGGER = LoggerFactory.getLogger(LianXinTongSmsHandler.class);

    private static final String LXT_SERVER = "sms.lxt.server";
    private static final String LXT_SP_ID = "sms.lxt.spId";
    private static final String LXT_AUTH_CODE = "sms.lxt.authCode";
    private static final String LXT_SRC_ID = "sms.lxt.srcId";

    private String spId;
    private String authCode;
    private String srcId;
    private String server;

    private static final int MAX_LIMIT = 10;

    @Autowired
    private ConfigurationProvider configurationProvider;

    @Autowired
    private LocaleTemplateService localeTemplateService;

    @PostConstruct
    public void init() {
        this.spId = configurationProvider.getValue(LXT_SP_ID, "");
        this.authCode = configurationProvider.getValue(LXT_AUTH_CODE, "");
        this.srcId = configurationProvider.getValue(LXT_SRC_ID, "");
        this.server = configurationProvider.getValue(LXT_SERVER, "");
    }

    private RspMessage createAndSend(Map<String, String> message) {
        SmsChannel channel = SmsBuilder.create(false);
        message.put("authCode", authCode);
        message.put("spId", spId);
        message.put("srcId", srcId);
        return channel.sendMessage(server, SmsBuilder.HttpMethod.POST.val(), null, null, StringHelper.toJsonString(message));
    }

    @Override
    public SmsLog doSend(Integer namespaceId, String phoneNumber, String templateScope, int templateId,
                         String templateLocale, List<Tuple<String, Object>> variables) {
        List<SmsLog> smsLogs = doSend(namespaceId, new String[]{phoneNumber}, templateScope, templateId, templateLocale, variables);
        if (smsLogs != null && smsLogs.size() > 0) {
            return smsLogs.get(0);
        }
        return null;
    }

    /*
      {
          "authCode": "cuihq01",
          "spId": " cuihq01",
          "srcId": "01",
          "reqId": "123456789",
          "serviceId": "95588",
          "content": "test message",
          "mobiles": ["18211111111", "13411111111"],
      }
     */
    @Override
    public List<SmsLog> doSend(Integer namespaceId, String[] phoneNumbers, String templateScope, int templateId,
                               String templateLocale, List<Tuple<String, Object>> variables) {

        Map<String, Object> model = new HashMap<>();
        if (variables != null) {
            model = variables.stream().collect(Collectors.toMap(Tuple::first, Tuple::second));
        }

        LocaleTemplate sign = localeTemplateService.getLocalizedTemplate(namespaceId, SmsTemplateCode.SCOPE, SmsTemplateCode.SIGN_CODE, templateLocale);

        templateScope = templateScope + "." + SmsTemplateCode.LIAN_XIN_TONG_SUFFIX;
        String content = localeTemplateService.getLocaleTemplateString(namespaceId, templateScope, templateId, templateLocale, model, "");
        if (content == null || content.isEmpty()) {
            content = localeTemplateService.getLocaleTemplateString(namespaceId, SmsTemplateCode.SCOPE, templateId, templateLocale, model, "");
        }
        if (content == null || content.isEmpty()) {
            content = localeTemplateService.getLocaleTemplateString(Namespace.DEFAULT_NAMESPACE, templateScope, templateId, templateLocale, model, "");
        }
        if (content == null || content.isEmpty()) {
            content = localeTemplateService.getLocaleTemplateString(Namespace.DEFAULT_NAMESPACE, SmsTemplateCode.SCOPE, templateId, templateLocale, model, "");
        }

        if (content != null && content.trim().length() > 0) {
            List<SmsLog> smsLogList = new ArrayList<>();
            for (int i = 0; i < phoneNumbers.length + MAX_LIMIT; i += MAX_LIMIT) {
                int length = MAX_LIMIT;
                if (i + MAX_LIMIT > phoneNumbers.length) {
                    length = phoneNumbers.length - i;
                }
                String[] phonesPart = new String[length];
                System.arraycopy(phoneNumbers, i, phonesPart, 0, length);

                Map<String, String> message = new HashMap<>();
                message.put("content", content);
                message.put("mobiles", StringHelper.toJsonString(phonesPart));
                // message.put("content", sign.getText() + content);
                // message.put("reqId", "123456");

                RspMessage rspMessage = createAndSend(message);
                smsLogList.addAll(buildSmsLogs(namespaceId, phonesPart, templateScope, templateId, templateLocale, content, rspMessage));
            }
            return smsLogList;
        }
        return null;
    }

    /*
      {
	    "rets": [{
			"mobile": "13411111111",
			"msgId": "1234567890001",
			"rspcod": 0
         },{
            "mobile": "18211111111",
            "msgId": "1234567890000",
            "rspcod": 0
         }]
      }
    */
    private List<SmsLog> buildSmsLogs(Integer namespaceId, String[] phoneNumbers, String templateScope, int templateId,
                                      String templateLocale, String content, RspMessage rspMessage) {
        List<SmsLog> smsLogs = new ArrayList<>();
        if (rspMessage != null) {
            Gson gson = new GsonBuilder().disableHtmlEscaping().create();
            List<Result> results = gson.fromJson(rspMessage.getMessage(), new TypeToken<List<Result>>(){}.getType());
            for (Result result : results) {
                SmsLog log = new SmsLog();
                log.setCreateTime(new Timestamp(System.currentTimeMillis()));
                log.setNamespaceId(namespaceId);
                log.setScope(templateScope);
                log.setCode(templateId);
                log.setLocale(templateLocale);
                log.setMobile(result.mobile);
                log.setResult(rspMessage.getMessage());
                log.setHandler(LIAN_XIN_TONG_HANDLER_NAME);
                log.setVariables(content);
                log.setSmsId(result.msgId);
                log.setHttpStatusCode(rspMessage.getCode());

                if ("0".equals(result.rspcod)) {
                    log.setStatus(SmsLogStatus.SEND_SUCCESS.getCode());
                } else {
                    log.setStatus(SmsLogStatus.SEND_FAILED.getCode());
                }
                smsLogs.add(log);
            }
        }
        return smsLogs;
    }

    /*
        {"destId":"12306123","mobile":"18211111111","msgId":"1234567890001","reqId":"","status":"DELIVERD"}
    */
    @Override
    public List<SmsReportDTO> report(String reportBody) {
        Report report = (Report) StringHelper.fromJsonString(reportBody, Report.class);
        if (report == null) {
            return null;
        }
        SmsReportDTO dto = new SmsReportDTO();
        dto.setSmsId(report.msgId);
        dto.setMobile(report.mobile);
        if (report.status.toLowerCase().startsWith("deliverd")) {
            dto.setStatus(SmsLogStatus.REPORT_SUCCESS.getCode());
        } else {
            dto.setStatus(SmsLogStatus.REPORT_FAILED.getCode());
        }
        return Collections.singletonList(dto);
    }

    private static class Result {
        String mobile;
        String msgId;
        String rspcod;
    }

    private static class Report {
        // String destId;
        String mobile;
        String msgId;
        String status;
    }
}
