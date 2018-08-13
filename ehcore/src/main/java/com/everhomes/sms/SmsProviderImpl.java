// @formatter: off
package com.everhomes.sms;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.locale.LocaleTemplate;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.rest.sms.SmsTemplateCode;
import com.everhomes.util.Tuple;
import com.everhomes.whitelist.WhiteListProvider;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * TODO To manage throughput and throttling, SMS/email notification service should go through queue
 * service.
 *
 * <p>For now, it is a fake implementation
 *
 * @author Kelven Yang
 */
@Component
@DependsOn
public class SmsProviderImpl implements SmsProvider {

    protected static final Logger LOGGER = LoggerFactory.getLogger(SmsProviderImpl.class);

    private static final String SMS_HANDLER_RESOLVER_NAME_KEY = "sms.handlerResolverName";
    private static final String SMS_TEST_PHONE_REGEX_KEY = "sms.testPhoneRegex";
    private static final String SMS_STRICT_PHONE_REGEX_KEY = "sms.strictPhoneRegex";

    // 严格正则
    private static final String SMS_STRICT_PHONE_REGEX =
            "^(?:13[0-9]|14[579]|15[0-3,5-9]|17[0135678]|18[0-9])\\d{8}$";

    // 测试手机号正则
    private static final String SMS_TEST_PHONE_REGEX =
            "^(?:1[0-2])\\d{9}$";

    private Pattern strictPhonePattern = Pattern.compile(SMS_STRICT_PHONE_REGEX);
    private Pattern testPhonePattern = Pattern.compile(SMS_TEST_PHONE_REGEX);

    private Map<String, SmsHandlerResolver> resolvers = new HashMap<>();

    @Autowired
    private ConfigurationProvider configurationProvider;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private SmsLogProvider smsLogProvider;

    @Autowired
    private WhiteListProvider whiteListProvider;

    @Autowired
    protected LocaleTemplateService localeTemplateService;

    @Autowired
    public void setHandlers(Map<String, SmsHandlerResolver> resolverMap) {
        resolverMap.forEach((name, resolver) -> resolvers.put(name, resolver));
    }

    @Override
    public void sendSms(Integer namespaceId, String phoneNumber, String templateScope,
                        int templateId, String templateLocale, List<Tuple<String, Object>> variables) {
        sendSms(null, namespaceId,
                new String[]{phoneNumber}, templateScope, templateId, templateLocale, variables);
    }

    @Override
    public void sendSms(Integer namespaceId, String[] phoneNumbers, String templateScope,
            int templateId, String templateLocale, List<Tuple<String, Object>> variables) {
        sendSms(null, namespaceId,
                phoneNumbers, templateScope, templateId, templateLocale, variables);
    }

    @Override
    public void sendSms(String handlerName, Integer namespaceId, String phoneNumber, String templateScope,
            int templateId, String templateLocale, List<Tuple<String, Object>> variables) {
        sendSms(handlerName, namespaceId,
                new String[]{phoneNumber}, templateScope, templateId, templateLocale, variables);
    }

    // final sendSms
    @Override
    public void sendSms(String handlerName, Integer namespaceId, String[] phoneNumbers, String templateScope,
                        int templateId, String templateLocale, List<Tuple<String, Object>> variables) {
        doSendWhenFailedRetry(handlerName, namespaceId,
                phoneNumbers, templateScope, templateId, templateLocale, variables, 2/*Retry times*/);
    }

    private void doSend(String handlerName, Integer namespaceId, String[] phoneNumbers,
            String templateScope, int templateId, String templateLocale, List<Tuple<String, Object>> variables) {
        doSendWhenFailedRetry(handlerName, namespaceId, phoneNumbers, templateScope, templateId, templateLocale, variables, 0);
    }

    private void doSendWhenFailedRetry(String handlerName, Integer namespaceId,
                                       String[] phoneNumbers, String templateScope,
                                       int templateId, String templateLocale,
                                       List<Tuple<String, Object>> variables, int retryTimes) {
        Map<SmsHandler, String[]> handlersMap;

        //白名单过滤 add by yanlong.liang
        phoneNumbers = whiteListFilter(handlerName,namespaceId, phoneNumbers, templateScope, templateId, templateLocale, variables);

        phoneNumbers = normalize(phoneNumbers);
        // validate(phoneNumbers);

        handlersMap = getSmsHandlerMap(handlerName, namespaceId, phoneNumbers);

        handlersMap.forEach((handler, phones) -> {
            publishEvent(() -> {
                List<SmsLog> logs =
                        handler.doSend(namespaceId, phones,
                                templateScope, templateId, templateLocale, variables);
                if (logs != null) {
                    saveSmsLog(logs, logList -> {
                        List<String> failedAndCorrectPhones =
                                logList.stream()
                                        .filter(r ->
                                            SmsLogStatus.fromCode(r.getStatus()) == SmsLogStatus.SEND_FAILED)
                                        // .filter(r ->
                                        //     strictPhonePattern().matcher(r.getMobile()).matches())
                                        .map(SmsLog::getMobile)
                                        .collect(Collectors.toList());

                        if (failedAndCorrectPhones.size() > 0 && retryTimes > 0) {
                            doSendWhenFailedRetry(handlerName,
                                    namespaceId,
                                    failedAndCorrectPhones.toArray(new String[failedAndCorrectPhones.size()]),
                                    templateScope,
                                    templateId,
                                    templateLocale,
                                    variables,
                                    retryTimes - 1);
                        }
                    });
                }
            });
        });

        if (LOGGER.isDebugEnabled()) {
            LOGGER.info(
                    "Send sms message, namespaceId="
                            + namespaceId
                            + ", phoneNumbers=["
                            + StringUtils.join(phoneNumbers, ",")
                            + "], templateScope="
                            + templateScope
                            + ", templateId="
                            + templateId
                            + ", templateLocale="
                            + templateLocale);
        }
    }

    private String[] normalize(String[] phoneNumbers) {
        if (phoneNumbers == null) {
            throw new IllegalArgumentException("Illegal argument phoneNumbers null.");
        }
        List<String> normalizedPhones = new ArrayList<>(phoneNumbers.length);
        Pattern testPattern = testPhonePattern();
        Pattern strictPattern = strictPhonePattern();
        for (String phoneNumber : phoneNumbers) {
            if (phoneNumber == null) {
                continue;
            }
            phoneNumber = phoneNumber.replaceAll("[_,.\\s+]", "");

            // 前缀带00的可能是国际手机号，国际手机号格式不确定，所以不做校验
            if (phoneNumber.startsWith("00")) {
                normalizedPhones.add(phoneNumber);
                continue;
            }
            // 测试手机号，跳过
            if (testPattern.matcher(phoneNumber).matches()) {
                LOGGER.info("Test phone '{}' were found while send sms, SKIPPED.", phoneNumber);
                continue;
            }
            // 空号，跳过
            if (!strictPattern.matcher(phoneNumber).matches()) {
                LOGGER.info("Invalid phone '{}' were found while send sms, SKIPPED.", phoneNumber);
                continue;
            }
            normalizedPhones.add(phoneNumber);
        }
        return normalizedPhones.toArray(new String[normalizedPhones.size()]);
    }

    /*private void validate(String[] phoneNumbers) {
        Pattern pattern = strictPhonePattern();
        for (String phoneNumber : phoneNumbers) {
            if (phoneNumber.startsWith("00")) {
                // 前缀带00的可能是国际手机号，国际手机号格式不确定，所以不做校验
                continue;
            }
            if (!pattern.matcher(phoneNumber).matches()) {
                throw RuntimeErrorException.errorWith(
                        SmsServiceErrorCode.SCOPE,
                        SmsServiceErrorCode.PHONE_VALIDATE_ERROR,
                        "Invalid phone number '%s', this phone number maybe not used.",
                        phoneNumber);
            }
        }
    }*/

    private Pattern testPhonePattern() {
        try {
            String reg = configurationProvider.getValue(SMS_TEST_PHONE_REGEX_KEY, SMS_TEST_PHONE_REGEX);
            this.testPhonePattern = Pattern.compile(reg);
        } catch (Exception e) {
            //
        }
        return testPhonePattern;
    }

    private Pattern strictPhonePattern() {
        try {
            String reg = configurationProvider.getValue(SMS_STRICT_PHONE_REGEX_KEY, SMS_STRICT_PHONE_REGEX);
            this.strictPhonePattern = Pattern.compile(reg);
        } catch (Exception e) {
            //
        }
        return strictPhonePattern;
    }

    private Map<SmsHandler, String[]> getSmsHandlerMap(
            String handlerName, Integer namespaceId, String[] phoneNumbers) {
        String val = configurationProvider.getValue(SMS_HANDLER_RESOLVER_NAME_KEY, "SMART");

        SmsHandlerResolver resolver = resolvers.get(SmsHandlerResolver.RESOLVER_NAME_PREFIX + val);

        Map<SmsHandler, String[]> handlersMap;
        if (handlerName != null && handlerName.length() > 0) {
            handlersMap = new HashMap<>();
            handlersMap.put(resolver.getHandlerByName(handlerName), phoneNumbers);
        } else {
            handlersMap = resolver.resolveHandler(namespaceId, phoneNumbers);
        }
        return handlersMap;
    }

    private void saveSmsLog(List<SmsLog> smsLogList, Consumer<List<SmsLog>> c) {
        for (SmsLog smsLog : smsLogList) {
            smsLogProvider.createSmsLog(smsLog);
        }
        if (c != null) {
            c.accept(smsLogList);
        }
    }

    public List<Tuple<String, Object>> toTupleList(String key, Object value) {
        List<Tuple<String, Object>> list = new ArrayList<>();
        Tuple<String, Object> variable = new Tuple<>(key, value);
        list.add(variable);
        return list;
    }

    public void addToTupleList(List<Tuple<String, Object>> list, String key, Object value) {
        Tuple<String, Object> variable = new Tuple<>(key, value);
        list.add(variable);
    }

    private void publishEvent(SmsCallback callback) {
        applicationEventPublisher.publishEvent(new SendSmsEvent(callback));
    }

    private String[] whiteListFilter(String handleName, Integer namespaceId,
                                     String[] phoneNumbers, String templateScope,
                                     int templateId, String templateLocale, List<Tuple<String, Object>> variables) {
        if (phoneNumbers == null) {
            throw new IllegalArgumentException("Illegal argument phoneNumbers null.");
        }
        //只有在测试环境中才启用短信白名单
        if ("true".equals(configurationProvider.getValue("sms.whitelist.test",""))) {
            List<String> whiteListPhones = new ArrayList<>();
            List<String> allPhoneNumbers = whiteListProvider.listAllWhiteList(namespaceId);
            for (String phoneNumber : phoneNumbers) {
                if (allPhoneNumbers.contains(phoneNumber)) {
                    whiteListPhones.add(phoneNumber);
                }else {
                    String signScope = SmsTemplateCode.SCOPE + ".sign";
                    LocaleTemplate sign = localeTemplateService.getLocalizedTemplate(
                            namespaceId, signScope, SmsTemplateCode.SIGN_CODE, templateLocale);
                    SmsLog smsLog = new SmsLog();
                    smsLog.setHandler("not_in_whitelist");
                    smsLog.setMobile(phoneNumber);
                    smsLog.setNamespaceId(namespaceId);
                    smsLog.setScope(templateScope);
                    smsLog.setCode(templateId);
                    smsLog.setLocale(templateLocale);
                    smsLog.setStatus(SmsLogStatus.SEND_FAILED.getCode());
                    smsLog.setResult("手机号码不在白名单中");

                    String content = "";
                    if (variables != null) {
                        Map<String, Object> varsMap = variables.stream().collect(Collectors.toMap(Tuple::first, Tuple::second));
                        content = varsMap.toString();
                    }
                    if (sign != null) {
                        smsLog.setText(sign.getText() + content);
                    }
                    smsLogProvider.createSmsLog(smsLog);
                }
            }
            return whiteListPhones.toArray(new String[whiteListPhones.size()]);
        }
        return phoneNumbers;
    }
}
