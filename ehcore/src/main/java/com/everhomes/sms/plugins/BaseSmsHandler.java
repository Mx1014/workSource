package com.everhomes.sms.plugins;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.locale.LocaleTemplate;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.namespace.Namespace;
import com.everhomes.rest.sms.SmsTemplateCode;
import com.everhomes.sms.RspMessage;
import com.everhomes.sms.SmsHandler;
import com.everhomes.sms.SmsLog;
import com.everhomes.sms.SmsLogStatus;
import com.everhomes.util.StringHelper;
import com.everhomes.util.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Created by xq.tian on 2018/1/11.
 */
abstract public class BaseSmsHandler implements SmsHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseSmsHandler.class);

    protected static final int HUNDRED = 100;

    @Autowired
    protected ConfigurationProvider configurationProvider;

    @Autowired
    protected LocaleTemplateService localeTemplateService;

    @Override
    public SmsLog doSend(Integer namespaceId, String phoneNumber, String templateScope,
                         int templateId, String templateLocale, List<Tuple<String, Object>> variables) {
        List<SmsLog> smsLogs = doSend(namespaceId, new String[]{phoneNumber},
                templateScope, templateId, templateLocale, variables);
        if (smsLogs != null && smsLogs.size() > 0) {
            return smsLogs.get(0);
        }
        return null;
    }

    @Override
    public List<SmsLog> doSend(Integer namespaceId, String[] phoneNumbers, String templateScope,
                               int templateId, String templateLocale, List<Tuple<String, Object>> variables) {
        initAccount();

        List<SmsLog> smsLogList = new ArrayList<>(phoneNumbers.length);

        Map<String, Object> model = new HashMap<>();
        if (variables != null) {
            model = variables.stream().collect(Collectors.toMap(Tuple::first, Tuple::second));
        }

        String signScope = SmsTemplateCode.SCOPE + ".sign";
        LocaleTemplate sign = localeTemplateService.getLocalizedTemplate(
                namespaceId, signScope, SmsTemplateCode.SIGN_CODE, templateLocale);
        if (sign == null) {
            LOGGER.error("can not found sign content by " +
                    getHandlerName() + " handler, namespaceId = {}", namespaceId);
            SmsLog log = getSmsErrorLog(namespaceId, phoneNumbers[0],
                    templateScope, templateId, templateLocale, "", "sms sign is empty");
            smsLogList.add(log);
            return smsLogList;
        }

        templateScope = templateScope + "." + getHandlerName();
        String content = getSmsContent(namespaceId, templateId, templateLocale, model);

        if (content != null && content.trim().length() > 0) {
            Tuple<String[], String[]> tuple = regionPartition(phoneNumbers);
            // 国内手机号
            String[] chinaPhonesArr = tuple.first();
            // 国际手机号
            String[] internationalPhonesArr = tuple.second();

            final String finalTplScope = templateScope;
            final String finalContent = content;

            splitSend(chinaPhonesArr, chinaSmsMaxLimit(), (splitPhones) -> {
                RspMessage rspMessage = createAndSend(splitPhones, sign.getText(), finalContent);
                smsLogList.addAll(buildSmsLogs(namespaceId, splitPhones, finalTplScope,
                        templateId, templateLocale, sign.getText() + finalContent, rspMessage));
            });

            splitSend(internationalPhonesArr, internationalSmsMaxLimit(), (splitPhones) -> {
                RspMessage rspMessage = createAndSendInternationalPhones(splitPhones, sign.getText(), finalContent);
                smsLogList.addAll(buildSmsLogs(namespaceId, splitPhones, finalTplScope,
                        templateId, templateLocale, sign.getText() + finalContent, rspMessage));
            });
        } else {
            SmsLog log = getSmsErrorLog(namespaceId, phoneNumbers[0],
                    templateScope, templateId, templateLocale, content, "sms content is empty");
            smsLogList.add(log);
            LOGGER.error("can not found sms content by " +
                    getHandlerName() + " namespaceId = {}, templateId = {}", namespaceId, templateId);
        }
        return smsLogList;
    }

    private Tuple<String[], String[]> regionPartition(String[] phoneNumbers) {
        List<String> chinaPhones = new ArrayList<>(phoneNumbers.length);
        List<String> internationalPhones = new ArrayList<>(phoneNumbers.length);

        for (String phoneNumber : phoneNumbers) {
            // 国际手机号必须加区号
            if (phoneNumber.startsWith("00")) {
                internationalPhones.add(phoneNumber);
            } else {
                chinaPhones.add(phoneNumber);
            }
        }
        // 国内手机号
        String[] chinaPhonesArr = chinaPhones.toArray(new String[chinaPhones.size()]);
        // 国际手机号
        String[] internationalPhonesArr = internationalPhones.toArray(new String[internationalPhones.size()]);
        return new Tuple<>(chinaPhonesArr, internationalPhonesArr);
    }

    private String getSmsContent(Integer namespaceId, int templateId, String templateLocale, Map<String, Object> model) {
        String content = localeTemplateService.getLocaleTemplateString(namespaceId,
                SmsTemplateCode.SCOPE, templateId, templateLocale, model, "");
        if (content == null || content.isEmpty()) {
            content = localeTemplateService.getLocaleTemplateString(namespaceId,
                    SmsTemplateCode.SCOPE, templateId, templateLocale, model, "");
        }
        if (content == null || content.isEmpty()) {
            content = localeTemplateService.getLocaleTemplateString(Namespace.DEFAULT_NAMESPACE,
                    SmsTemplateCode.SCOPE, templateId, templateLocale, model, "");
        }
        if (content == null || content.isEmpty()) {
            content = localeTemplateService.getLocaleTemplateString(Namespace.DEFAULT_NAMESPACE,
                    SmsTemplateCode.SCOPE, templateId, templateLocale, model, "");
        }
        return content;
    }

    protected void splitSend(String[] phoneNumbers, int maxLimit, Consumer<String[]> c) {
        for (int i = 0; i < phoneNumbers.length; i += maxLimit) {
            int length = maxLimit;
            if (i + maxLimit > phoneNumbers.length) {
                length = phoneNumbers.length - i;
            }
            String[] phonesPart = new String[length];
            System.arraycopy(phoneNumbers, i, phonesPart, 0, length);
            c.accept(phonesPart);
        }
    }

    protected SmsLog getSmsErrorLog(Integer namespaceId, String phoneNumber,
                                    String templateScope, int templateId, String templateLocale, String content, String error) {
        SmsLog log = new SmsLog();
        log.setCreateTime(new Timestamp(System.currentTimeMillis()));
        log.setNamespaceId(namespaceId);
        log.setScope(templateScope);
        log.setCode(templateId);
        log.setLocale(templateLocale);
        log.setMobile(phoneNumber);
        log.setResult(error);
        log.setHandler(getHandlerName());
        log.setText(content);
        log.setStatus(SmsLogStatus.SEND_FAILED.getCode());
        return log;
    }

    protected static String MD5Encode(String sourceString) {
        String resultString = null;
        try {
            resultString = sourceString;
            MessageDigest md = MessageDigest.getInstance("MD5");
            resultString = byte2hexString(md.digest(resultString.getBytes()));
        } catch (Exception ignored) {
            //
        }
        return resultString;
    }

    protected static String SHA1Encode(String sourceString) {
        String resultString = null;
        try {
            resultString = sourceString;
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            resultString = byte2hexString(md.digest(resultString.getBytes("utf-8")));
        } catch (Exception ignored) {
            //
        }
        return resultString.toUpperCase();
    }

    protected static String byte2hexString(byte[] bytes) {
        StringBuilder bf = new StringBuilder(bytes.length * 2);
        for (byte aByte : bytes) {
            if ((aByte & 0xff) < 0x10) {
                bf.append("0");
            }
            bf.append(Long.toString(aByte & 0xff, 16));
        }
        return bf.toString();
    }

    protected String URLEncode(String plainText) {
        try {
            return URLEncoder.encode(plainText, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    protected static <T> T xmlToBean(String xml, Class<T> c) {
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

    protected static class ToString {
        public String toString() {
            return StringHelper.toJsonString(this);
        }
    }

    abstract String getHandlerName();

    abstract void initAccount();

    protected int internationalSmsMaxLimit() {
        return HUNDRED;
    }

    protected int chinaSmsMaxLimit() {
        return HUNDRED;
    }

    protected abstract RspMessage createAndSend(String[] phones, String sign, String content);

    protected abstract RspMessage createAndSendInternationalPhones(String[] phones, String sign, String content);

    abstract List<SmsLog> buildSmsLogs(Integer namespaceId, String[] phoneNumbers, String templateScope,
                                       int templateId, String templateLocale, String content, RspMessage rspMessage);
}
