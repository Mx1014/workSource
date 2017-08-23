/*
package com.everhomes.sms.plugins;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.sms.SmsBuilder;
import com.everhomes.sms.SmsChannel;
import com.everhomes.sms.SmsHandler;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.Tuple;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

*/
/**
 * mw sms provider
 *
 * @author elians
 *
 *//*

@Component("MW")
public class MWSmsHandler implements SmsHandler {
    protected final static Logger LOGGER = LoggerFactory.getLogger(MWSmsHandler.class);

    private static final String MW_PORT = "mw.port";
    private static final String MW_PASSWORD = "mw.password";
    private static final String MW_USER_ID = "mw.user";
    private static final String MW_HOST = "mw.host";
    // max limit size for MW
    private static final int MAX_LIMIT = 100;
    @Autowired
    private ConfigurationProvider configurationProvider;

    @Autowired
    private LocaleTemplateService localeTemplateService;
    private String host;
    private String username;

    private String password;
    private int port;

    @PostConstruct
    public void init() {
        this.host = configurationProvider.getValue(MW_HOST, "");
        this.username = configurationProvider.getValue(MW_USER_ID, "");
        this.password = configurationProvider.getValue(MW_PASSWORD, "");
        this.port = configurationProvider.getIntValue(MW_PORT, 9003);

    }

    */
/*@Override
    public void doSend(String phoneNumber, String text) {
        createAndSend(new String[] { phoneNumber }, text);
    }*//*


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

    */
/*@Override
    public void doSend(String[] phoneNumbers, String text) {
        if (phoneNumbers.length <= MAX_LIMIT) {
            createAndSend((String[]) phoneNumbers, text);
            return;
        }
        int index = MAX_LIMIT;
        if(phoneNumbers.length>MAX_LIMIT)
        for (; index < phoneNumbers.length; index += MAX_LIMIT) {
            createAndSend((String[]) ArrayUtils.subarray(phoneNumbers, index - MAX_LIMIT, index), text);
        }
        // send last one
        createAndSend((String[]) ArrayUtils.subarray(phoneNumbers, index - MAX_LIMIT, phoneNumbers.length), text);
    }*//*


    private void createAndSend(String[] phoneNumbers, String text) {
        SmsChannel channel = SmsBuilder.create(false);
        channel.addHeader("Connection", "Keep-Alive");
        String uri = String.format("http://%s:%d%s", host, port, createUrl((String[]) phoneNumbers, text));
        String result = channel.sendMessage(uri, SmsBuilder.HttpMethod.GET.val(), null, null, null).getMessage();
        parserResult(result);
    }

    private String createUrl(String[] phoneNumbers, String text) {
        return String
                .format("/MWGate/wmgw.asmx/MongateSendSubmit?userId=%s&password=%s&pszMobis=%s&pszMsg=%s&iMobiCount=%d&pszSubPort=*&MsgId=0",
                        username, password, StringUtils.join(phoneNumbers, ","), text, phoneNumbers.length);
    }

    */
/*@Override
    public void doSend(String phoneNumber, String text, String templateId) {
        doSend(phoneNumber, text);

    }

    @Override
    public void doSend(String[] phoneNumbers, String text, String templateId) {
       doSend(phoneNumbers, text);
    }*//*


    @Override
    public void doSend(Integer namespaceId, String phoneNumber, String templateScope, int templateId,
        String templateLocale, List<Tuple<String, Object>> variables) {
        doSend(namespaceId, new String[]{phoneNumber}, templateScope, templateId, templateLocale, variables);
    }

    @Override
    public void doSend(Integer namespaceId, String[] phoneNumbers, String templateScope, int templateId,
        String templateLocale, List<Tuple<String, Object>> variables) {
        Map<String, Object> map = new HashMap<String, Object>();
        if(variables != null) {
            for(Tuple<String, Object> variable : variables) {
                map.put(variable.first(), variable.second());
            }
        }

        String content = localeTemplateService.getLocaleTemplateString(templateScope, templateId,
            templateLocale, map, "");

        if(content != null && content.trim().length() > 0) {
            if (phoneNumbers.length <= MAX_LIMIT) {
                createAndSend((String[]) phoneNumbers, content);
                return;
            }
            int index = MAX_LIMIT;
            if(phoneNumbers.length>MAX_LIMIT)
                for (; index < phoneNumbers.length; index += MAX_LIMIT) {
                    createAndSend((String[]) ArrayUtils.subarray(phoneNumbers, index - MAX_LIMIT, index), content);
                }
            // send last one
            createAndSend((String[]) ArrayUtils.subarray(phoneNumbers, index - MAX_LIMIT, phoneNumbers.length), content);
        } else {
            LOGGER.error("The mw template id is empty, namespaceId=" + namespaceId + ", templateScope=" + templateScope
                + ", templateId=" + templateId + ", templateLocale=" + templateLocale);
        }
    }
}
*/
