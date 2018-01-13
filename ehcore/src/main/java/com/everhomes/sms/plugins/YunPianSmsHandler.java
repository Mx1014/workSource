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

import java.net.URLDecoder;
import java.sql.Timestamp;
import java.util.*;

/**
 * Created by xq.tian on 2018/1/11.
 */
@Component(SmsHandler.YUN_PIAN_HANDLER_NAME)
public class YunPianSmsHandler extends BaseSmsHandler {

    protected final static Logger LOGGER = LoggerFactory.getLogger(YunPianSmsHandler.class);

    private static final String API_KEY_NAME = "sms.YunPian.apiKey";
    private static final String SERVER_NAME = "sms.YunPian.server";

    private String apiKey;
    private String server;

    @Override
    String getHandlerName() {
        return SmsHandler.YUN_PIAN_HANDLER_NAME;
    }

    @Override
    void initAccount() {
        try {
            apiKey = configurationProvider.getValue(API_KEY_NAME, "a010af7f5e94f889c010ed38a7fc3a05");
            server = configurationProvider.getValue(SERVER_NAME, "https://sms.yunpian.com/v2/sms/batch_send.json");
        } catch (Exception e) {
            //
        }
    }

    @Override
    RspMessage createAndSend(String[] phones, String sign, String content) {
        Map<String, String> params = new HashMap<>();
        params.put("apikey", apiKey);
        params.put("text", sign + content);
        params.put("mobile", StringUtils.join(phones, ","));

        SmsChannel channel = SmsBuilder.create(false);
        return channel.sendMessage(server, SmsBuilder.HttpMethod.POST.val(), params, null, null);
    }

    /**
     * {"http_status_code":400,"code":3,"msg":"账户余额不足","detail":"请充值后重试"}
     */
    @Override
    List<SmsLog> buildSmsLogs(Integer namespaceId, String[] phoneNumbers, String templateScope, int templateId, String templateLocale, String content, RspMessage rspMessage) {
        List<SmsLog> smsLogs = new ArrayList<>();
        SendResult res = new SendResult();
        String result = "failed";

        if (rspMessage != null) {
            try {
                result = rspMessage.getMessage();
                res = (SendResult) StringHelper.fromJsonString(rspMessage.getMessage(), SendResult.class);
            } catch (Exception e) {
                return error(namespaceId, phoneNumbers, templateScope, templateId, templateLocale, smsLogs, result);
            }
        }

        if (res.data == null) {
            return error(namespaceId, phoneNumbers, templateScope, templateId, templateLocale, smsLogs, result);
        }

        for (SendData data : res.data) {
            SmsLog log = new SmsLog();
            log.setCreateTime(new Timestamp(System.currentTimeMillis()));
            log.setNamespaceId(namespaceId);
            log.setScope(templateScope);
            log.setCode(templateId);
            log.setLocale(templateLocale);
            log.setMobile(data.mobile);
            log.setResult(result);
            log.setHandler(getHandlerName());
            log.setText(content);
            log.setSmsId(data.sid);

            if (Objects.equals(0, data.code)) {
                log.setStatus(SmsLogStatus.SEND_SUCCESS.getCode());
            } else {
                log.setStatus(SmsLogStatus.SEND_FAILED.getCode());
            }
            smsLogs.add(log);
        }
        return smsLogs;
    }

    private List<SmsLog> error(Integer namespaceId, String[] phoneNumbers, String templateScope, int templateId, String templateLocale, List<SmsLog> smsLogs, String result) {
        for (String phoneNumber : phoneNumbers) {
            smsLogs.add(getSmsErrorLog(namespaceId, phoneNumber, templateScope, templateId, templateLocale, result));
        }
        return smsLogs;
    }

    /**
     * {"total_count":2,"total_fee":"0.1000","unit":"RMB","data":[{"code":0,"count":2,"fee":0.1,"mobile":"13246687272","msg":"发送成功","sid":20937316970,"unit":"RMB"}]}
     */
    public static class SendResult {
        public List<SendData> data;
    }

    public static class SendData {
        public Integer code;
        public String mobile;
        public String msg;
        public String sid;
    }

    public static class ReportResult {
        public String mobile;
        public String report_status;
        public String sid;
    }

    /**
     * [
         {
             "sid": 9527,
             "uid": null,
             "user_receive_time": "2014-03-17 22:55:21",
             "error_msg": "DELIVRD",
             "mobile": "15205201314",
             "report_status": "SUCCESS"
         },
         {
             "sid": 9528,
             "uid": null,
             "user_receive_time": "2014-03-17 22:55:23",
             "error_msg": "DELIVRD",
             "mobile": "15212341234",
             "report_status": "SUCCESS"
         }
     ]
     */
    @Override
    public SmsReportResponse report(String reportBody) {
        String[] split = reportBody.split("=");

        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        List<ReportResult> reportList = null;
        try {
            reportList = gson.fromJson(URLDecoder.decode(split[1], "UTF-8"), new TypeToken<List<ReportResult>>(){}.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (reportList == null) {
            return null;
        }

        List<SmsReportDTO> reportDTOS = new ArrayList<>();
        for (ReportResult report : reportList) {
            SmsReportDTO dto = new SmsReportDTO();
            dto.setSmsId(report.sid);
            if ("SUCCESS".equals(report.report_status)) {
                dto.setStatus(SmsLogStatus.REPORT_SUCCESS.getCode());
            } else {
                dto.setStatus(SmsLogStatus.REPORT_FAILED.getCode());
            }
            reportDTOS.add(dto);
        }

        SmsReportResponse response = new SmsReportResponse(reportDTOS);
        response.setResponseContentType("SUCCESS");
        return response;
    }
}
