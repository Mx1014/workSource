package com.everhomes.sms.plugins;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javassist.expr.NewArray;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.sms.DateUtil;
import com.everhomes.sms.EncryptUtil;
import com.everhomes.sms.SmsBuilder;
import com.everhomes.sms.SmsChannel;
import com.everhomes.sms.SmsHandler;
import com.everhomes.sms.TemplateSMS;
import com.everhomes.util.RuntimeErrorException;
import com.google.gson.Gson;

/**
 * yzx sms provider
 * 
 * @author zhouzq
 *
 */
@Component("YZX")
public class YZXSmsHandler implements SmsHandler {
    protected final static Logger LOGGER = LoggerFactory.getLogger(YZXSmsHandler.class);
    
    private static final String YZX_ACCOUNT_SID = "yzx.account.sid";
    private static final String YZX_TOKEN = "yzx.token";
    private static final String YZX_APP_ID = "yzx.appid";
    //private static final String YZX_TEMPLATE_ID = "yzx.template.id";
    private static final String YZX_VERSION = "yzx.version";
    private static final String YZX_SERVER = "yzx.server";
    private static final String YZX_SSL_IP = "yzx.ip";
    private static final String YZX_SSL_PORT = "yzx.port";
    // max limit size for MW
    private static final int MAX_LIMIT = 100;
    @Autowired
    private ConfigurationProvider configurationProvider;
    private String accountSid;
    private String token;
    private String appId;
    //private String templateId;
    private String version;
    private String server;
    private String ip;
    private String port;
    

    @PostConstruct
    public void init() {
        this.accountSid = configurationProvider.getValue(YZX_ACCOUNT_SID, "b0b423e8f630d920aaba8aafe8f37701");
        this.token = configurationProvider.getValue(YZX_TOKEN, "1527ed4b1bb855fab12a5683bb3c7ead");
        this.appId = configurationProvider.getValue(YZX_APP_ID, "89f85f91ed974429992831fb662dbb83");
        //this.templateId = configurationProvider.getValue(YZX_TEMPLATE_ID, "9547");
        this.version = configurationProvider.getValue(YZX_VERSION, "2014-06-30");
        this.server = configurationProvider.getValue(YZX_SERVER, "api.ucpaas.com");
        this.ip = configurationProvider.getValue(YZX_SSL_IP, "0");
        this.port = configurationProvider.getValue(YZX_SSL_PORT, "0");
    }

    @Override
    public void doSend(String phoneNumber, String text) {
        //createAndSend(new String[] { phoneNumber }, text);
    }

    private static void parserResult(String ret) {
        assert (ret != null);
        Document doc;
        try {
            doc = DocumentHelper.parseText(ret);
        } catch (DocumentException e) {
            LOGGER.error("parser msg error,msg={}", ret, e);
            throw errorWrap("can not parser the result");
        }
        Element el = doc.getRootElement();
        String result = el.getText();
        if (StringUtils.isNotEmpty(result) && result.length() > 10) {
            return;
        }
        if (StringUtils.isEmpty(result)) {
            LOGGER.error("error ,parser result is empty.error code is {}", -200);
            throw errorWrap("can not parser the result");
        }
        int code = NumberUtils.toInt(result, -200);
        if (code != 0) {
            LOGGER.error("send message failed.Code={}", code);
            throw errorWrap("error code:" + code);
        }
    }

    private static RuntimeErrorException errorWrap(String reason) {
        return RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, reason);
    }

    @Override
    public void doSend(String[] phoneNumbers, String text) {
//        if (phoneNumbers.length <= MAX_LIMIT) {
//            createAndSend((String[]) phoneNumbers, text);
//            return;
//        }
//        int index = MAX_LIMIT;
//        if(phoneNumbers.length>MAX_LIMIT)
//        for (; index < phoneNumbers.length; index += MAX_LIMIT) {
//            createAndSend((String[]) ArrayUtils.subarray(phoneNumbers, index - MAX_LIMIT, index), text);
//        }
//        // send last one
//        createAndSend((String[]) ArrayUtils.subarray(phoneNumbers, index - MAX_LIMIT, phoneNumbers.length), text);
    }

    private void createAndSend(String[] phoneNumbers, String text, String templateId) {
        SmsChannel channel = SmsBuilder.create(false);
        String timestamp = DateUtil.dateToStr(new Date(),
                DateUtil.DATE_TIME_NO_SLASH);// 时间戳
        String uri = createUrl(accountSid,token,timestamp);
        TemplateSMS temSms = new TemplateSMS();
        temSms.setAppId(appId);
        temSms.setTemplateId(templateId);
        temSms.setTo(StringUtils.join(phoneNumbers, ","));
        temSms.setParam(text);
        Gson gson = new Gson();
        String body = gson.toJson(temSms);
        body="{\"templateSMS\":"+body+"}";
        
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        headers.put("Content-Type", "application/json;charset=utf-8");
        String src = accountSid + ":" + timestamp;
        String auth = null;
        try { 
            auth = EncryptUtil.base64Encoder(src);
        } catch (Exception e) {
            LOGGER.error("Encoder accountsid is error.src={}",src);
        }
        headers.put("Authorization", auth);
        
        String result = channel.sendMessage(uri, SmsBuilder.HttpMethod.POST.val(), null, headers,body).getMessage();
        LOGGER.info("send message result={}",result);
    }

    private String createUrl(String accountSid, String authToken,String timestamp) {
        // 构造请求URL内容
        String url = null;
        try {
            String signature = getSignature(accountSid,authToken,timestamp);
            url = getStringBuffer().append("/").append(version)
            .append("/Accounts/").append(accountSid)
            .append("/Messages/templateSMS")
            .append("?sig=").append(signature).toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
       
        return url;
    }
    
    private String getSignature(String accountSid, String authToken,String timestamp) throws Exception{
        String sig = accountSid + authToken + timestamp;
        String signature =EncryptUtil.md5Digest(sig);
        return signature;
    }
    
    private StringBuffer getStringBuffer() {
        StringBuffer sb = new StringBuffer("https://");
        sb.append(server);
        return sb;
    }

    @Override
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
        
    }

}
