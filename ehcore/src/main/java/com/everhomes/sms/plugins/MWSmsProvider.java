package com.everhomes.sms.plugins;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.cache.CacheProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.db.DbProvider;
import com.everhomes.sms.AbstractSmsProvider;
import com.everhomes.sms.SmsBuilder;
import com.everhomes.sms.SmsChannel;
import com.everhomes.sms.SmsHandler;
import com.everhomes.util.RuntimeErrorException;

/**
 * mw sms provider
 * 
 * @author elians
 *
 */
@Component
@SmsHandler(value = "MW")
public class MWSmsProvider extends AbstractSmsProvider {

    // max limit size for MW
    private static final int MAX_LIMIT = 100;
    @Autowired
    private ConfigurationProvider configurationProvider;
    private String host;
    private String username;

    private String password;
    private int port;

    public void init() {
        // get from cache or db,current for test
        this.host = configurationProvider.getValue("", "");
        this.username = configurationProvider.getValue("", "");
        this.password = configurationProvider.getValue("", "");
        this.port = configurationProvider.getIntValue("", 9003);

    }

    @Override
    public void doSend(String phoneNumber, String text) {
        createAndSend(new String[] { phoneNumber }, text);
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
        if (phoneNumbers.length <= MAX_LIMIT) {
            createAndSend((String[]) phoneNumbers, text);
            return;
        }
        int index = MAX_LIMIT;
        for (; index < phoneNumbers.length; index += MAX_LIMIT) {
            createAndSend((String[]) ArrayUtils.subarray(phoneNumbers, index - MAX_LIMIT, index), text);
        }
        // send last one
        createAndSend((String[]) ArrayUtils.subarray(phoneNumbers, index - MAX_LIMIT, phoneNumbers.length), text);
    }

    private void createAndSend(String[] phoneNumbers, String text) {
        SmsChannel channel = SmsBuilder.create(false);
        String uri = String.format("http://%s:%d%s", host, port, createUrl((String[]) phoneNumbers, text));
        String result = channel.sendMessage(uri, SmsBuilder.HttpMethod.GET.val(), null, null).getMessage();
        parserResult(result);
    }

    private String createUrl(String[] phoneNumbers, String text) {
        return String
                .format("/MWGate/wmgw.asmx/MongateSendSubmit?userId=%s&password=%s&pszMobis=%s&pszMsg=%s&iMobiCount=%d&pszSubPort=*&MsgId=0",
                        username, password, StringUtils.join(phoneNumbers, ","), text, phoneNumbers.length);
    }

}
