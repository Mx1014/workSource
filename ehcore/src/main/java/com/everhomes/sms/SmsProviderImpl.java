//@formatter: off
package com.everhomes.sms;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.namespace.Namespace;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.Tuple;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO To manage throughput and throttling, SMS/email notification service
 * should go through queue service.
 *
 * For now, it is a fake implementation
 *
 *
 * @author Kelven Yang
 *
 */
@Component
public class SmsProviderImpl implements SmsProvider {
    protected final static Logger LOGGER = LoggerFactory.getLogger(SmsProviderImpl.class);

    private static final String VCODE_SEND_TYPE = "sms.handler.type";

    // @Autowired
    // private TaskQueue taskQueue;

    @Autowired
    private ConfigurationProvider configurationProvider;

    private Map<String, SmsHandler> handlers = new HashMap<>();

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    public void setHandlers(Map<String, SmsHandler> prop) {
        prop.forEach((name, handler) -> handlers.put(name.toLowerCase(), handler));
    }

    private SmsHandler getHandler(Integer namespaceId) {
        // find name from db
        String handlerName = configurationProvider.getValue(namespaceId, VCODE_SEND_TYPE, "MW");
        SmsHandler handler = handlers.get(handlerName.toLowerCase());
        if (handler == null) {
            LOGGER.error("cannot find relate handler.handler={}", handlerName);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
                    "can not find relate sms handler.handler=" + handlerName);
        }
        return handler;
    }

    private void doSend(String phoneNumber, String text, String templateId) {
        LOGGER.info("Send SMS text:\"{}\" to {}.beginTime={}", SmsHepler.getEncodingString(text), phoneNumber,
                System.currentTimeMillis());
        String escapedText = SmsHepler.convert(text);

        publishEvent(() -> getHandler(Namespace.DEFAULT_NAMESPACE).doSend(phoneNumber, escapedText, templateId));

        /*Future<?> f = taskQueue.submit(() -> {
            getHandler(Namespace.DEFAULT_NAMESPACE).doSend(phoneNumber, escapedText, templateId);
            LOGGER.info("send sms message ok.endTime={}", System.currentTimeMillis());
            return null;
        });*/

       /*try {
           f.get();
       } catch (InterruptedException | ExecutionException e) {
           LOGGER.error("send sms message error", e);
           throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
                   e.getMessage());
       }*/
    }

    private void doSend(String[] phoneNumbers, String text, String templateId) {
        LOGGER.info("Send SMS text:\"{}\" to {}.beginTime={}", SmsHepler.getEncodingString(text),
                StringUtils.join(phoneNumbers, ","), System.currentTimeMillis());
        String escapedText = SmsHepler.convert(text);

        publishEvent(() -> {
            getHandler(Namespace.DEFAULT_NAMESPACE).doSend(phoneNumbers, SmsHepler.getEncodingString(escapedText), templateId);
            LOGGER.info("send sms message ok.endTime={}", System.currentTimeMillis());
        });

        /*Future<?> f = taskQueue.submit(() -> {
            getHandler(Namespace.DEFAULT_NAMESPACE).doSend(phoneNumbers, SmsHepler.getEncodingString(escapedText),templateId);
            LOGGER.info("send sms message ok.endTime={}", System.currentTimeMillis());
            return null;
        });*/
       /*try {
           f.get();
       } catch (InterruptedException | ExecutionException e) {
           LOGGER.error("send sms message error", e);
           throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
                   e.getMessage());
       }*/
    }

    private void publishEvent(SmsCallback callback) {
        applicationEventPublisher.publishEvent(new SendSmsEvent(callback));
        // if (LOGGER.isDebugEnabled()) {
        //     LOGGER.debug("sms event publish success...");
        // }
    }

    @Override
    public void sendSms(String phoneNumber, String text) {
        this.doSend(phoneNumber, text, null);
    }

    @Override
    public void sendSms(String[] phoneNumbers, String text) throws Exception {
        this.doSend(phoneNumbers, text, null);
    }

    @Override
    public void sendSms(String phoneNumber, String text, String templateId) {
        this.doSend(phoneNumber, text, templateId);
    }

    @Override
    public void sendSms(String[] phoneNumbers, String text, String templateId) throws Exception {
        this.doSend(phoneNumbers, text, templateId);
    }

    @Override
    public void sendSms(Integer namespaceId, String phoneNumber, String templateScope, int templateId, String templateLocale, List<Tuple<String, Object>> variables) {
        sendSms(namespaceId, new String[]{phoneNumber}, templateScope, templateId, templateLocale, variables);
    }

    @Override
    public void sendSms(Integer namespaceId, String[] phoneNumbers, String templateScope, int templateId, String templateLocale, List<Tuple<String, Object>> variables) {
        publishEvent(() -> {
            getHandler(namespaceId).doSend(namespaceId, phoneNumbers, templateScope, templateId, templateLocale, variables);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.info("Send sms message, namespaceId=" + namespaceId + ", phoneNumbers=[" + StringUtils.join(phoneNumbers, ",")
                        + "], templateScope=" + templateScope + ", templateId=" + templateId + ", templateLocale=" + templateLocale);
            }
        });

        /*taskQueue.submit(() -> {
            try{
        		getHandler(namespaceId).doSend(namespaceId, phoneNumbers, templateScope, templateId, templateLocale, variables);
        		  if(LOGGER.isDebugEnabled()) {
                      LOGGER.info("Send sms message, namespaceId=" + namespaceId + ", phoneNumbers=[" + StringUtils.join(phoneNumbers, ",")
                          + "], templateScope=" + templateScope + ", templateId=" + templateId + ", templateLocale=" + templateLocale);
                  }
        	} catch(Exception ex) {
        		LOGGER.error("send sms failed ex=", ex);
        	}
            
            return null;
        });*/
    }

    public List<Tuple<String, Object>> toTupleList(String key, Object value) {
        List<Tuple<String, Object>> list = new ArrayList<Tuple<String, Object>>();
        Tuple<String, Object> variable = new Tuple<String, Object>(key, value);
        list.add(variable);
        return list;
    }

    public void addToTupleList(List<Tuple<String, Object>> list, String key, Object value) {
        Tuple<String, Object> variable = new Tuple<String, Object>(key, value);
        list.add(variable);
    }
}
