package com.everhomes.sms.plugins;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.rest.sms.SmsTemplateCode;
import com.everhomes.sms.*;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.StringHelper;
import com.everhomes.util.Tuple;
import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.StringReader;
import java.sql.Timestamp;
import java.util.*;

/**
 * YunZhiXun sms provider
 *
 * @author zhouzq
 *
 */
@Component(SmsHandler.YUN_ZHI_XUN_HANDLER_NAME)
public class YunZhiXunSmsHandler implements SmsHandler {

    protected final static Logger LOGGER = LoggerFactory.getLogger(YunZhiXunSmsHandler.class);

    private static final String YZX_ACCOUNT_SID = "yzx.account.sid";
    private static final String YZX_TOKEN = "yzx.token";
    private static final String YZX_APP_ID = "yzx.appid";
    private static final String YZX_VERSION = "yzx.version";
    private static final String YZX_SERVER = "yzx.server";
    private static final String YZX_SSL_IP = "yzx.ip";
    private static final String YZX_SSL_PORT = "yzx.port";

    private static final int MAX_LIMIT = 100;

    @Autowired
    private ConfigurationProvider configurationProvider;

    @Autowired
    private LocaleTemplateService localeTemplateService;

    private String accountSid;
    private String token;
    private String appId;
    private String version;
    private String server;
    private String ip;
    private String port;

    @PostConstruct
    public void init() {
        this.accountSid = configurationProvider.getValue(YZX_ACCOUNT_SID, "");
        this.token = configurationProvider.getValue(YZX_TOKEN, "");
        this.appId = configurationProvider.getValue(YZX_APP_ID, "");
        //this.templateId = configurationProvider.getValue(YZX_TEMPLATE_ID, "9547");
        this.version = configurationProvider.getValue(YZX_VERSION, "2014-06-30");
        this.server = configurationProvider.getValue(YZX_SERVER, "api.ucpaas.com");
        // this.ip = configurationProvider.getValue(YZX_SSL_IP, "0");
        // this.port = configurationProvider.getValue(YZX_SSL_PORT, "0");
    }

    private static RuntimeErrorException errorWrap(String reason) {
        return RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, reason);
    }

    private RspMessage createAndSend(String[] phoneNumbers, String text, String templateId) {
        SmsChannel channel = SmsBuilder.create(false);
        String timestamp = DateUtil.dateToStr(new Date(),
                DateUtil.DATE_TIME_NO_SLASH);// 时间戳
        String uri = createUrl(accountSid, token, timestamp);
        TemplateSMS temSms = new TemplateSMS();
        temSms.setAppId(appId);
        temSms.setTemplateId(templateId);
        temSms.setTo(StringUtils.join(phoneNumbers, ","));
        temSms.setParam(text);
        Gson gson = new Gson();
        String entityJsonStr = gson.toJson(temSms);
        entityJsonStr = "{\"templateSMS\":" + entityJsonStr + "}";

        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        headers.put("Content-Type", "application/json;charset=utf-8");
        String src = accountSid + ":" + timestamp;
        String auth = null;
        try {
            auth = EncryptUtil.base64Encoder(src);
        } catch (Exception e) {
            LOGGER.error("Send sms, encoder accountsid is error, src=" + src, e);
        }
        headers.put("Authorization", auth);

        RspMessage result = channel.sendMessage(uri, SmsBuilder.HttpMethod.POST.val(), null, headers, entityJsonStr);
        LOGGER.info("Send sms, result={}, uri={}, headers={}, phone={}, text={}", result, uri, headers, StringUtils.join(phoneNumbers, ","), text);
        return result;
    }

    private String createUrl(String accountSid, String authToken, String timestamp) {
        // 构造请求URL内容
        String url = null;
        try {
            String signature = getSignature(accountSid, authToken, timestamp);
            url = getStringBuffer().append("/").append(version)
                    .append("/Accounts/").append(accountSid)
                    .append("/Messages/templateSMS")
                    .append("?sig=").append(signature).toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return url;
    }

    private String getSignature(String accountSid, String authToken, String timestamp) throws Exception {
        String sig = accountSid + authToken + timestamp;
        return EncryptUtil.md5Digest(sig);
    }

    private StringBuffer getStringBuffer() {
        StringBuffer sb = new StringBuffer("https://");
        sb.append(server);
        return sb;
    }

    /*@Override
    public void doSend(String phoneNumber, String text, String templateId) {
        createAndSend(new String[] { phoneNumber }, text,templateId);
    }

    @Override
    public void doSend(String[] phoneNumbers, String text, String templateId) {
        if (phoneNumbers.length <= MAX_LIMIT) {
            createAndSend((String[]) phoneNumbers, text,templateId);
            return;
        }
        int index = MAX_LIMIT;
        if(phoneNumbers.length>MAX_LIMIT)
        for (; index < phoneNumbers.length; index += MAX_LIMIT) {
            createAndSend((String[]) ArrayUtils.subarray(phoneNumbers, index - MAX_LIMIT, index), text,templateId);
        }
        // send last one
        createAndSend((String[]) ArrayUtils.subarray(phoneNumbers, index - MAX_LIMIT, phoneNumbers.length), text,templateId);
        
    }*/

    @Override
    public List<SmsReportDTO> report(String reportBody) {
        YzxSmsReport report = xmlToBean(reportBody, YzxSmsReport.class);
        String smsId = report.smsId;
        if (smsId == null) {
            LOGGER.error("YZX sms report smsId is null, reportBody = {}", reportBody);
            return null;
        }
        SmsReportDTO dto = new SmsReportDTO();
        dto.setSmsId(smsId);
        if ("0".equals(report.status)) {
            dto.setStatus(SmsLogStatus.REPORT_SUCCESS.getCode());
        } else {
            dto.setStatus(SmsLogStatus.REPORT_FAILED.getCode());
        }

        dto.setResponseContentType("text/xml;charset=utf-8");
        dto.setResponseBody("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<response>\n<retcode>0</retcode>\n</response>");
        return Collections.singletonList(dto);
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
        // 对于云之讯短信厂商，由于模板是在云之讯定义，在此只能使用模板ID作为参数传给云之讯API，
        // 为了使得短信厂商这些特殊要求从业务层剥离出来、业务层只决定要发短信的内容及收短信的人，
        // 故业务层只给短信模块传phoneNumber（要收短信的人）、templateScope/templateId/templateLocale（决定短信内容），
        // 由于有些短信厂商是直接发内容的，而有些厂商则要使用模板的，为了兼容这两种情况，
        // 需要在template表既定义一种有模板内容的，同时再加一种templateLocale+厂商后缀的模板（模板值为厂商要求的模板ID）
        StringBuilder strBuilder = new StringBuilder();
        templateScope = strBuilder.append(templateScope).append(".").append(SmsTemplateCode.YZX_SUFFIX).toString();

        String yzxTemplateId = localeTemplateService.getLocaleTemplateString(namespaceId, templateScope, templateId,
                templateLocale, new HashMap<String, Object>(), "");

        if (yzxTemplateId != null && yzxTemplateId.trim().length() > 0) {
            String content = "";
            if (variables != null) {
                strBuilder.setLength(0);
                for (Tuple<String, Object> variable : variables) {
                    if (strBuilder.length() > 0) {
                        strBuilder.append(",");
                    }
                    if (variable.second() == null) {
                        strBuilder.append("");
                    } else {
                        strBuilder.append(variable.second().toString());
                    }
                }
                content = strBuilder.toString();
            }
            RspMessage rspMessage = createAndSend(phoneNumbers, content, yzxTemplateId);

            return buildSmsLogs(namespaceId, phoneNumbers, templateScope, templateId, templateLocale, content, rspMessage);
        } else {
            String log = "The yzx template id is empty, namespaceId=" + namespaceId + ", templateScope=" + templateScope
                    + ", templateId=" + templateId + ", templateLocale=" + templateLocale;
            LOGGER.error(log);
            return null;
        }
    }

    private List<SmsLog> buildSmsLogs(Integer namespaceId, String[] phoneNumbers, String templateScope, int templateId,
                                      String templateLocale, String variables, RspMessage rspMessage) {
        List<SmsLog> smsLogs = new ArrayList<>();
        if (rspMessage != null) {
            YzxSmsResult res = (YzxSmsResult) StringHelper.fromJsonString(rspMessage.getMessage(), YzxSmsResult.class);
            for (String phoneNumber : phoneNumbers) {
                SmsLog log = new SmsLog();
                log.setCreateTime(new Timestamp(System.currentTimeMillis()));
                log.setNamespaceId(namespaceId);
                log.setScope(templateScope);
                log.setCode(templateId);
                log.setLocale(templateLocale);
                log.setMobile(phoneNumber);
                log.setResult(rspMessage.getMessage());
                log.setHandler(YUN_ZHI_XUN_HANDLER_NAME);
                log.setVariables(variables);
                log.setSmsId(res.resp.templateSMS.smsId);
                log.setHttpStatusCode(rspMessage.getCode());

                if ("000000".equals(res.resp.respCode)) {
                    log.setStatus(SmsLogStatus.SEND_SUCCESS.getCode());
                } else {
                    log.setStatus(SmsLogStatus.SEND_FAILED.getCode());
                }
                smsLogs.add(log);
            }
        }
        return smsLogs;
    }

    private static <T> T xmlToBean(String xml, Class<T> c) {
        T t = null;
        try {
            JAXBContext context = JAXBContext.newInstance(c);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            t = (T) unmarshaller.unmarshal(new StringReader(xml));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }

    /**
     *  云之讯发送短信的响应结果
     * {
     *    "resp": {
     *        "respCode": "000000",
     *        "failure": 1,
     *        "templateSMS": {
     *            "createDate": 20140623185016,
     *            "smsId": "f96f79240e372587e9284cd580d8f953"
     *        }
     *     }
     * }
     */
    public static class YzxSmsResult {
        Resp resp;
        public static class Resp {
            String respCode;
            // Byte failure;
            Sms templateSMS;
        }

        public static class Sms {
            // Long createDate;
            String smsId;
        }
    }

    /**
     *  云之讯短信报告
     * <request>
     *     <type>1</type>
     *     <smsid>******</smsid>
     *     <status>******</status>
     *     <reportTime>******</reportTime>
     *     <desc>******</desc>
     * </request>
     */
    @XmlAccessorType(XmlAccessType.PROPERTY)
    @XmlRootElement(name = "request")
    public static class YzxSmsReport {
        // Byte type;
        @XmlElement(name = "smsid")
        String smsId;
        String status;
        // String desc;
    }
}
