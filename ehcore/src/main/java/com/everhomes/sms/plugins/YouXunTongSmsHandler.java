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
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.security.MessageDigest;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 优讯通
 */
@Component(SmsHandler.YOU_XUN_TONG_HANDLER_NAME)
public class YouXunTongSmsHandler implements SmsHandler {

    protected final static Logger LOGGER = LoggerFactory.getLogger(YouXunTongSmsHandler.class);

    private static final String YXT_USER_NAME = "sms.yxt.accountName";
    private static final String YXT_PASSWORD = "sms.yxt.password";
    private static final String YXT_SERVER = "sms.yxt.server";
    private static final String YXT_TOKEN = "sms.yxt.token";

    private static final int MAX_LIMIT = 100;

    @Autowired
    private ConfigurationProvider configurationProvider;

    @Autowired
    private LocaleTemplateService localeTemplateService;

    private String userName;
    private String password;
    private String token;
    private String server;

    @PostConstruct
    public void init() {
        this.userName = configurationProvider.getValue(YXT_USER_NAME, "");
        this.password = configurationProvider.getValue(YXT_PASSWORD, "");
        this.server = configurationProvider.getValue(YXT_SERVER, "");
        this.token = configurationProvider.getValue(YXT_TOKEN, "");
    }

    /*
      {
          "account":"8373",
          "password":"bb43a2c4081bec02fca7b72f38e63021",
          "phones": "13111111111,13222222222,13333333333",
          "content":"测试短信",
          "sign": "【签名内容】",
          "subcode":"",
          "sendtime":""
      }
     */
    private RspMessage createAndSend(Map<String, String> message) {
        SmsChannel channel = SmsBuilder.create(false);

        Map<String, String> params = new HashMap<>();
        params.put("message", StringHelper.toJsonString(message));
        // params.put("sid", sid);
        params.put("type", "json");

        return channel.sendMessage(server, SmsBuilder.HttpMethod.POST.val(), params, null, null);
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

    @Override
    public List<SmsLog> doSend(Integer namespaceId, String[] phoneNumbers, String templateScope, int templateId,
                               String templateLocale, List<Tuple<String, Object>> variables) {

        Map<String, Object> model = new HashMap<>();
        if (variables != null) {
            model = variables.stream().collect(Collectors.toMap(Tuple::first, Tuple::second));
        }

        String signScope = SmsTemplateCode.SCOPE + ".sign";
        LocaleTemplate sign = localeTemplateService.getLocalizedTemplate(namespaceId, signScope, SmsTemplateCode.SIGN_CODE, templateLocale);

        templateScope = templateScope + "." + SmsTemplateCode.YOU_XUN_TONG_SUFFIX;
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
            List<SmsLog> smsLogList = new ArrayList<>();
            for (int i = 0; i < phoneNumbers.length; i += MAX_LIMIT) {
                int length = MAX_LIMIT;
                if (i + MAX_LIMIT > phoneNumbers.length) {
                    length = phoneNumbers.length - i;
                }
                String[] phonesPart = new String[length];
                System.arraycopy(phoneNumbers, i, phonesPart, 0, length);

                Map<String, String> message = new HashMap<>();
                message.put("account", userName);
                message.put("password", MD5Encode(password));
                message.put("phones", StringUtils.join(phonesPart, ","));
                message.put("content", content);
                message.put("sign", sign.getText());

                RspMessage rspMessage = createAndSend(message);
                smsLogList.addAll(buildSmsLogs(namespaceId, phonesPart, templateScope, templateId, templateLocale, content, rspMessage));
            }
            return smsLogList;
        }
        return null;
    }

    /*
      {
          "msgid ":"123",
          "result ":"0",
          "desc ":"提交成功",
          "blacklist ":"黑名单号码"
      }
    */
    private List<SmsLog> buildSmsLogs(Integer namespaceId, String[] phoneNumbers, String templateScope, int templateId,
                                      String templateLocale, String content, RspMessage rspMessage) {
        List<SmsLog> smsLogs = new ArrayList<>();
        if (rspMessage != null) {
            Result result = (Result) StringHelper.fromJsonString(rspMessage.getMessage(), Result.class);
            for (String phoneNumber : phoneNumbers) {
                SmsLog log = new SmsLog();
                log.setCreateTime(new Timestamp(System.currentTimeMillis()));
                log.setNamespaceId(namespaceId);
                log.setScope(templateScope);
                log.setCode(templateId);
                log.setLocale(templateLocale);
                log.setMobile(phoneNumber);
                log.setResult(rspMessage.getMessage());
                log.setHandler(YOU_XUN_TONG_HANDLER_NAME);
                log.setText(content);
                log.setSmsId(result.msgid);

                if ("0".equals(result.result)) {
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
       [
          {"wgcode":"","time":"201510300954","phone":"13684996628","msgid":9299,"desc":"失败","status":"1"},
          {"wgcode":"","time":"201510300954","phone":"15811835631","msgid":9299,"desc":"失败","status":"1"}
       ]
     */
    @Override
    public List<SmsReportDTO> report(String reportBody) {
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        List<Report> reportList = gson.fromJson(reportBody, new TypeToken<List<Report>>(){}.getType());
        if (reportList == null) {
            return null;
        }

        List<SmsReportDTO> reportDTOS = new ArrayList<>();
        for (Report report : reportList) {
            SmsReportDTO dto = new SmsReportDTO();
            dto.setSmsId(report.msgid);
            if ("0".equals(report.status)) {
                dto.setStatus(SmsLogStatus.REPORT_SUCCESS.getCode());
            } else {
                dto.setStatus(SmsLogStatus.REPORT_FAILED.getCode());
            }
            reportDTOS.add(dto);
        }
        return reportDTOS;
    }

    private static class Report {
        String msgid;
        String status;
    }

    private static class Result {
        String msgid;
        String result;
        // String desc;
        // String blacklist;
    }

    private static String MD5Encode(String sourceString) {
        String resultString = null;
        try {
            resultString = sourceString;
            MessageDigest md = MessageDigest.getInstance("MD5");
            resultString = byte2hexString(md.digest(resultString.getBytes()));
        } catch (Exception ignored) { }
        return resultString;
    }

    private static String byte2hexString(byte[] bytes) {
        StringBuilder bf = new StringBuilder(bytes.length * 2);
        for (int i = 0; i < bytes.length; i++) {
            if ((bytes[i] & 0xff) < 0x10) {
                bf.append("0");
            }
            bf.append(Long.toString(bytes[i] & 0xff, 16));
        }
        return bf.toString();
    }
}
