package com.everhomes.sms.plugins;

import com.everhomes.sms.*;
import org.apache.commons.lang.StringUtils;
import org.apache.http.entity.ContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.nio.charset.Charset;
import java.sql.Timestamp;
import java.util.*;

/**
 * Created by xq.tian on 2018/1/11.
 */
@Component(SmsHandler.JIAN_MI_HANDLER_NAME)
public class JianMiSmsHandler extends BaseSmsHandler {

    protected final static Logger LOGGER = LoggerFactory.getLogger(JianMiSmsHandler.class);

    private static final String ACCOUNT_NAME = "sms.JianMi.accountName";
    private static final String PASSWORD = "sms.JianMi.password";
    private static final String SERVER_NAME = "sms.JianMi.server";

    private final boolean isSecure = false;

    private String accountName;
    private String password;
    private String server;

    @Override
    String getHandlerName() {
        return SmsHandler.JIAN_MI_HANDLER_NAME;
    }

    @Override
    void initAccount() {
        try {
            accountName = configurationProvider.getValue(ACCOUNT_NAME, "4629");
            password = configurationProvider.getValue(PASSWORD, "yjtc7777");
            server = configurationProvider.getValue(SERVER_NAME, "http://sms.53api.com/sdk/SMS");
        } catch (Exception e) {
            //
        }
    }

    @Override
    RspMessage createAndSend(String[] phones, String sign, String content) {
        Map<String, Object> params = new HashMap<>();
        params.put("cmd", "send");
        params.put("msgid", System.nanoTime());
        params.put("uid", accountName);
        params.put("psw", MD5Encode(password));
        params.put("msg", sign + content);
        params.put("mobiles", StringUtils.join(phones, ","));

        RspMessage message = SmsChannelBuilder.create(isSecure)
                .setUrl(server)
                .setMethod(SmsChannelBuilder.HttpMethod.POST)
                .setContentType(ContentType.APPLICATION_FORM_URLENCODED.withCharset(SmsChannelBuilder.GBK))
                .setBodyMap(params)
                .setCharset(Charset.forName("GBK"))
                .send();

        message.setMeta(params);

        return message;
    }

    @Override
    List<SmsLog> buildSmsLogs(Integer namespaceId, String[] phoneNumbers, String templateScope, int templateId, String templateLocale, String content, RspMessage rspMessage) {
        String result = rspMessage.getMessage();
        if (rspMessage.isException()) {
            return error(namespaceId, phoneNumbers, templateScope, templateId, templateLocale, result);
        }

        Map<String, ?> meta = rspMessage.getMeta();
        String mobiles = (String) meta.get("mobiles");
        String msgid = String.valueOf(meta.get("msgid"));

        List<SmsLog> smsLogs = new ArrayList<>();
        for (String mob : mobiles.split(",")) {
            SmsLog log = new SmsLog();
            log.setCreateTime(new Timestamp(System.currentTimeMillis()));
            log.setNamespaceId(namespaceId);
            log.setScope(templateScope);
            log.setCode(templateId);
            log.setLocale(templateLocale);
            log.setMobile(mob);
            log.setResult(result);
            log.setHandler(getHandlerName());
            log.setText(content);
            log.setSmsId(msgid);

            if (Objects.equals("100", result.trim())) {
                log.setStatus(SmsLogStatus.SEND_SUCCESS.getCode());
            } else {
                log.setStatus(SmsLogStatus.SEND_FAILED.getCode());
            }
            smsLogs.add(log);
        }
        return smsLogs;
    }

    private List<SmsLog> error(Integer namespaceId, String[] phoneNumbers, String templateScope, int templateId, String templateLocale, String result) {
        List<SmsLog> smsLogs = new ArrayList<>();
        for (String phoneNumber : phoneNumbers) {
            smsLogs.add(getSmsErrorLog(namespaceId, phoneNumber, templateScope, templateId, templateLocale, result));
        }
        return smsLogs;
    }

    /**
         <?xml version="1.0" encoding="utf-8"?>
         <returnForm>
             <type>1</type>
             <count>2</count>
             <list>
                 <pushStatusForm>
                     <eprId>0</eprId>
                     <mobile>15766208001</mobile>
                     <msgId>1461822936915</msgId>
                     <status>0</status>
                     <statusCode>UNDELIV</statusCode>
                     <userId>pangrihui5</userId>
                 </pushStatusForm>
                 <pushStatusForm>
                     <eprId>0</eprId>
                     <mobile>15766208001</mobile>
                     <msgId>1461822936915</msgId>
                     <status>0</status>
                     <statusCode>UNDELIV</statusCode>
                     <userId>pangrihui5</userId>
                 </pushStatusForm>
             </list>
         </returnForm>
     */
    @XmlRootElement(name = "returnForm")
    public static class ReportResult extends ToString {
        @XmlElement
        public String type;
        @XmlElement
        public String count;
        @XmlElement
        public ReportReturnForm list;

    }

    @XmlRootElement(name = "list")
    public static class ReportReturnForm extends ToString {
        @XmlElement
        public List<ReportPushStatusForm> pushStatusForm;
    }

    @XmlRootElement(name = "list")
    public static class ReportPushStatusForm extends ToString {
        @XmlElement
        public String eprId;
        @XmlElement
        public String mobile;
        @XmlElement
        public String msgId;
        @XmlElement
        public Integer status;
        @XmlElement
        public String statusCode;
    }

    @Override
    public SmsReportResponse report(SmsReportRequest reportRequest) {
        ReportResult reportResult = null;
        try {
            reportResult = xmlToBean(reportRequest.getRequestBody(), ReportResult.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (reportResult == null || reportResult.list == null) {
            return null;
        }

        List<SmsReportDTO> reportDTOS = new ArrayList<>();
        for (ReportPushStatusForm statusForm : reportResult.list.pushStatusForm) {
            SmsReportDTO dto = new SmsReportDTO();
            dto.setSmsId(statusForm.msgId);
            if ("1".equals(String.valueOf(statusForm.status).trim())) {
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
