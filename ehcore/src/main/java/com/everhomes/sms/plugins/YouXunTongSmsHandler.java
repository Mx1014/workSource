package com.everhomes.sms.plugins;

import com.everhomes.sms.*;
import com.everhomes.util.StringHelper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 优讯通
 */
@Component(SmsHandler.YOU_XUN_TONG_HANDLER_NAME)
public class YouXunTongSmsHandler extends BaseSmsHandler {

    protected final static Logger LOGGER = LoggerFactory.getLogger(YouXunTongSmsHandler.class);

    private static final String YXT_USER_NAME = "sms.YouXunTong.accountName";
    private static final String YXT_PASSWORD = "sms.YouXunTong.password";
    private static final String YXT_SERVER = "sms.YouXunTong.server";
    private static final String YXT_TOKEN = "sms.YouXunTong.token";

    private String userName;
    private String password;
    private String token;
    private String server;

    public void initAccount() {
        try {
            this.userName = configurationProvider.getValue(YXT_USER_NAME, "");
            this.password = configurationProvider.getValue(YXT_PASSWORD, "");
            this.server = configurationProvider.getValue(YXT_SERVER, "");
            this.token = configurationProvider.getValue(YXT_TOKEN, "");
        } catch (Exception e) {
            //
        }
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
    @Override
    RspMessage createAndSend(String[] phones, String sign, String content) {
        Map<String, String> message = new HashMap<>();
        message.put("phones", StringUtils.join(phones, ","));
        message.put("content", content);
        message.put("sign", sign);
        message.put("account", userName);
        message.put("password", MD5Encode(password));

        Map<String, Object> params = new HashMap<>();
        String jsonMsg = StringHelper.toJsonString(message);
        params.put("message", jsonMsg);
        params.put("type", "json");

        String sid= SHA1Encode((token+"&"+ jsonMsg));
        params.put("sid", sid);
        // return channel.sendMessage(server, SmsChannelBuilder.HttpMethod.POST.val(), params, null, null);
        return SmsChannelBuilder.create(false)
                .setUrl(server)
                .setBodyMap(params)
                .setMethod(SmsChannel.HttpMethod.POST)
                .send();
    }

    @Override
    String getHandlerName() {
        return YOU_XUN_TONG_HANDLER_NAME;
    }

    /*
      {
          "msgid ":"123",
          "result ":"0",
          "desc ":"提交成功",
          "blacklist ":"黑名单号码"
      }
    */
    public List<SmsLog> buildSmsLogs(Integer namespaceId, String[] phoneNumbers, String templateScope, int templateId,
                                      String templateLocale, String content, RspMessage rspMessage) {
        List<SmsLog> smsLogs = new ArrayList<>();
        Result res = new Result();
        String result = "failed";

        if (rspMessage != null) {
            try {
                result = rspMessage.getMessage();
                res = (Result) StringHelper.fromJsonString(rspMessage.getMessage(), Result.class);
            } catch (Exception e) {
                for (String phoneNumber : phoneNumbers) {
                    smsLogs.add(getSmsErrorLog(namespaceId, phoneNumber, templateScope, templateId, templateLocale, "Exception:"+result));
                }
                return smsLogs;
            }
        }

        for (String phoneNumber : phoneNumbers) {
            SmsLog log = new SmsLog();
            log.setCreateTime(new Timestamp(System.currentTimeMillis()));
            log.setNamespaceId(namespaceId);
            log.setScope(templateScope);
            log.setCode(templateId);
            log.setLocale(templateLocale);
            log.setMobile(phoneNumber);
            log.setResult(result);
            log.setHandler(YOU_XUN_TONG_HANDLER_NAME);
            log.setText(content);
            log.setSmsId(res.msgid);

            if ("0".equals(res.result)) {
                log.setStatus(SmsLogStatus.SEND_SUCCESS.getCode());
            } else {
                log.setStatus(SmsLogStatus.SEND_FAILED.getCode());
            }
            smsLogs.add(log);
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
    public SmsReportResponse report(String reportBody) {
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
        return new SmsReportResponse(reportDTOS);
    }

    private static class Report {
        String msgid = "";
        String status = "";
    }

    private static class Result {
        String msgid = "";
        String result = "";
        // String desc;
        // String blacklist;
    }
}
