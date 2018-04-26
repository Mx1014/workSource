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
    private static final String INTERNATIONAL_SERVER_NAME = "sms.YunPian.internationalServer";

    private String apiKey;
    private String server;
    private String internationalServer;

    @Override
    String getHandlerName() {
        return SmsHandler.YUN_PIAN_HANDLER_NAME;
    }

    @Override
    protected int internationalSmsMaxLimit() {
        return 1;
    }

    @Override
    void initAccount() {
        try {
            apiKey = configurationProvider.getValue(API_KEY_NAME, "a010af7f5e94f889c010ed38a7fc3a05");
            server = configurationProvider.getValue(SERVER_NAME, "https://sms.yunpian.com/v2/sms/batch_send.json");
            internationalServer = configurationProvider.getValue(INTERNATIONAL_SERVER_NAME, "https://sms.yunpian.com/v2/sms/single_send.json");
        } catch (Exception e) {
            //
        }
    }

    @Override
    protected RspMessage createAndSend(String[] phones, String sign, String content) {
        Map<String, Object> params = new HashMap<>();
        params.put("apikey", apiKey);
        params.put("text", sign + content);
        params.put("mobile", StringUtils.join(phones, ","));

        return SmsChannelBuilder.create(true)
                .setUrl(server)
                .setBodyMap(params)
                .send();
    }

    @Override
    protected RspMessage createAndSendInternationalPhones(String[] phones, String sign, String content) {
        // 国际短信一次只能发一个手机号
        if (phones.length > 0) {
            Map<String, Object> params = new HashMap<>();
            params.put("apikey", apiKey);
            params.put("text", sign + content);

            String phone = phones[0];

            if (phone.startsWith("00")) {
                phone = phone.substring(2, phone.length());
            }
            params.put("mobile", "+" + phone);
            return SmsChannelBuilder.create(true)
                    .setUrl(internationalServer)
                    .setBodyMap(params)
                    .send();
        }
        return null;
    }

    /**
     * {"http_status_code":400,"code":3,"msg":"账户余额不足","detail":"请充值后重试"}
     *
     * {"http_status_code":400,"code":22,"msg":"验证码类短信1小时内同一手机号发送次数不能超过3次","detail":"验证码类短信1小时内同一手机号发送次数不能超过3次"}
     *
     * // 国际短信返回值
     * {"code":0,"msg":"发送成功","count":1,"fee":0.05,"unit":"RMB","mobile":"13246687272","sid":21364791658}
     */
    @Override
    List<SmsLog> buildSmsLogs(
            Integer namespaceId, String[] phoneNumbers, String templateScope,
                              int templateId,
                              String templateLocale, String content, RspMessage rspMessage) {
        List<SmsLog> smsLogs = new ArrayList<>(phoneNumbers.length);
        SendResult res = new SendResult();
        String result = "failed";

        if (rspMessage != null) {
            try {
                result = rspMessage.getMessage();
                SendData sendData = (SendData) StringHelper.fromJsonString(result, SendData.class);
                if (sendData != null && sendData.code != null && sendData.code > 0) {
                    return error(namespaceId, phoneNumbers, templateScope, templateId, templateLocale, content, result);
                }
                res = (SendResult) StringHelper.fromJsonString(result, SendResult.class);
            } catch (Exception e) {
                return error(namespaceId, phoneNumbers, templateScope, templateId, templateLocale, content, result);
            }
        }

        if (res.data == null) {
            SendData sendData = (SendData) StringHelper.fromJsonString(result, SendData.class);
            if (sendData != null) {
                res.data = new ArrayList<>();
                res.data.add(sendData);
            } else {
                return error(namespaceId, phoneNumbers, templateScope, templateId, templateLocale, content, result);
            }
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

    private List<SmsLog> error(Integer namespaceId, String[] phoneNumbers, String templateScope,
                               int templateId,
                               String templateLocale, String content, String result) {
        List<SmsLog> smsLogs = new ArrayList<>(phoneNumbers.length);
        for (String phoneNumber : phoneNumbers) {
            smsLogs.add(getSmsErrorLog(namespaceId, phoneNumber, templateScope, templateId, templateLocale, content, result));
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
     * @param reportRequest
     */
    @Override
    public SmsReportResponse report(SmsReportRequest reportRequest) {
        String[] smsStatuses = reportRequest.getParameter("sms_status");

        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        List<ReportResult> reportList = null;
        try {
            reportList = gson.fromJson(
                    URLDecoder.decode(smsStatuses[0], "UTF-8"), new TypeToken<List<ReportResult>>(){}.getType());
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
        response.setResponseBody("SUCCESS");
        return response;
    }
}
