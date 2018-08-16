package com.everhomes.decoration;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.rentalv2.RentalNotificationTemplateCode;
import com.everhomes.rentalv2.message_handler.VipParkingRentalMessageHandler;
import com.everhomes.rest.sms.SmsTemplateCode;
import com.everhomes.sms.SmsProvider;
import com.everhomes.util.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DecorationSMSProcessor {
    private static final Logger LOGGER = LoggerFactory.getLogger(DecorationSMSProcessor.class);
    @Autowired
    private LocaleTemplateService localeTemplateService;
    @Autowired
    private SmsProvider smsProvider;
    @Autowired
    private ConfigurationProvider configurationProvider;

    public void applySuccess(DecorationRequest request){
        //发短信给负责人
        String templateScope = SmsTemplateCode.SCOPE;
        List<Tuple<String, Object>> variables = smsProvider.toTupleList("decoratorName", request.getDecoratorName());
        smsProvider.addToTupleList(variables, "applyName", request.getApplyName());
        smsProvider.addToTupleList(variables, "applyPhone", request.getApplyPhone());
        smsProvider.addToTupleList(variables, "applyCompamy", request.getApplyCompany());
        String homeUrl = configurationProvider.getValue("home.url","https://core.zuolin.com");
        smsProvider.addToTupleList(variables, "url", homeUrl+"/decoration-management/build/download.html?ns="+request.getNamespaceId());

        int templateId = SmsTemplateCode.DECORATION_APPLY_SUCCESS;

        String templateLocale = RentalNotificationTemplateCode.locale;

        try {
            smsProvider.sendSms(request.getNamespaceId(), request.getDecoratorPhone(), templateScope, templateId, templateLocale, variables);
        }catch (RuntimeException e){
            LOGGER.error("DecorationSMSProcessor:Wrong Phone Number:"+request.getDecoratorPhone());
        }
    }

    public void fileApprovalSuccess(DecorationRequest request){
        //负责人
        String templateScope = SmsTemplateCode.SCOPE;
        List<Tuple<String, Object>> variables = smsProvider.toTupleList("decoratorName", request.getDecoratorName());
        smsProvider.addToTupleList(variables, "applyCompamy", request.getApplyCompany());
        int templateId = SmsTemplateCode.DECORATION_APPROVAL_SUCCESS;
        String templateLocale = RentalNotificationTemplateCode.locale;

        try {
            smsProvider.sendSms(request.getNamespaceId(), request.getDecoratorPhone(), templateScope, templateId, templateLocale, variables);
        }catch (RuntimeException e){
            LOGGER.error("DecorationSMSProcessor:Wrong Phone Number:"+request.getDecoratorPhone());
        }

    }

    public void fileApprovalFail(DecorationRequest request){
        //负责人
        String templateScope = SmsTemplateCode.SCOPE;
        List<Tuple<String, Object>> variables = smsProvider.toTupleList("decoratorName", request.getDecoratorName());
        smsProvider.addToTupleList(variables, "applyCompamy", request.getApplyCompany());
        int templateId = SmsTemplateCode.DECORATION_APPROVAL_FAIL;
        String templateLocale = RentalNotificationTemplateCode.locale;

        try {
            smsProvider.sendSms(request.getNamespaceId(), request.getDecoratorPhone(), templateScope, templateId, templateLocale, variables);
        }catch (RuntimeException e){
            LOGGER.error("DecorationSMSProcessor:Wrong Phone Number:"+request.getDecoratorPhone());
        }
    }

    public void feeListGenerate(DecorationRequest request){
        //负责人
        String templateScope = SmsTemplateCode.SCOPE;
        List<Tuple<String, Object>> variables = smsProvider.toTupleList("name", request.getDecoratorName());
        smsProvider.addToTupleList(variables, "applyCompamy", request.getApplyCompany());
        int templateId = SmsTemplateCode.DECORATION_FEE_GENERATE;
        String templateLocale = RentalNotificationTemplateCode.locale;

        try {
            smsProvider.sendSms(request.getNamespaceId(), request.getDecoratorPhone(), templateScope, templateId, templateLocale, variables);
        }catch (RuntimeException e){
            LOGGER.error("DecorationSMSProcessor:Wrong Phone Number:"+request.getDecoratorPhone());
        }
        //租户
        variables = smsProvider.toTupleList("name", request.getApplyName());
        smsProvider.addToTupleList(variables, "applyCompamy", request.getApplyCompany());
        try {
            smsProvider.sendSms(request.getNamespaceId(), request.getApplyPhone(), templateScope, templateId, templateLocale, variables);
        }catch (RuntimeException e){
            LOGGER.error("DecorationSMSProcessor:Wrong Phone Number:"+request.getDecoratorPhone());
        }

    }

    public void feeConfirm(DecorationRequest request){
        //负责人
        String templateScope = SmsTemplateCode.SCOPE;
        List<Tuple<String, Object>> variables = smsProvider.toTupleList("name", request.getDecoratorName());
        smsProvider.addToTupleList(variables, "applyCompamy", request.getApplyCompany());
        int templateId = SmsTemplateCode.DECORATION_FEE_CONFIRM;
        String templateLocale = RentalNotificationTemplateCode.locale;

        try {
            smsProvider.sendSms(request.getNamespaceId(), request.getDecoratorPhone(), templateScope, templateId, templateLocale, variables);
        }catch (RuntimeException e){
            LOGGER.error("DecorationSMSProcessor:Wrong Phone Number:"+request.getDecoratorPhone());
        }
        //租户
        variables = smsProvider.toTupleList("name", request.getApplyName());
        smsProvider.addToTupleList(variables, "applyCompamy", request.getApplyCompany());
        try {
            smsProvider.sendSms(request.getNamespaceId(), request.getApplyPhone(), templateScope, templateId, templateLocale, variables);
        }catch (RuntimeException e){
            LOGGER.error("DecorationSMSProcessor:Wrong Phone Number:"+request.getDecoratorPhone());
        }
    }

    public void checkSuccess(DecorationRequest request){
        //负责人
        String templateScope = SmsTemplateCode.SCOPE;
        List<Tuple<String, Object>> variables = smsProvider.toTupleList("decoratorName", request.getDecoratorName());
        smsProvider.addToTupleList(variables, "applyCompamy", request.getApplyCompany());
        int templateId = SmsTemplateCode.DECORATION_CHECK_SUCCESS;
        String templateLocale = RentalNotificationTemplateCode.locale;

        try {
            smsProvider.sendSms(request.getNamespaceId(), request.getDecoratorPhone(), templateScope, templateId, templateLocale, variables);
        }catch (RuntimeException e){
            LOGGER.error("DecorationSMSProcessor:Wrong Phone Number:"+request.getDecoratorPhone());
        }
    }

    public void checkfail(DecorationRequest request){
        //负责人
        String templateScope = SmsTemplateCode.SCOPE;
        List<Tuple<String, Object>> variables = smsProvider.toTupleList("decoratorName", request.getDecoratorName());
        smsProvider.addToTupleList(variables, "applyCompamy", request.getApplyCompany());
        int templateId = SmsTemplateCode.DECORATION_CHECK_FAIL;
        String templateLocale = RentalNotificationTemplateCode.locale;

        try {
            smsProvider.sendSms(request.getNamespaceId(), request.getDecoratorPhone(), templateScope, templateId, templateLocale, variables);
        }catch (RuntimeException e){
            LOGGER.error("DecorationSMSProcessor:Wrong Phone Number:"+request.getDecoratorPhone());
        }
    }

    public void refoundGenerate(DecorationRequest request){
        //负责人
        String templateScope = SmsTemplateCode.SCOPE;
        List<Tuple<String, Object>> variables = smsProvider.toTupleList("name", request.getDecoratorName());
        smsProvider.addToTupleList(variables, "applyCompamy", request.getApplyCompany());
        int templateId = SmsTemplateCode.DECORATION_REFOUND_GENERATE;
        String templateLocale = RentalNotificationTemplateCode.locale;

        try {
            smsProvider.sendSms(request.getNamespaceId(), request.getDecoratorPhone(), templateScope, templateId, templateLocale, variables);
        }catch (RuntimeException e){
            LOGGER.error("DecorationSMSProcessor:Wrong Phone Number:"+request.getDecoratorPhone());
        }
        //租户
        variables = smsProvider.toTupleList("name", request.getApplyName());
        smsProvider.addToTupleList(variables, "applyCompamy", request.getApplyCompany());
        try {
            smsProvider.sendSms(request.getNamespaceId(), request.getApplyPhone(), templateScope, templateId, templateLocale, variables);
        }catch (RuntimeException e){
            LOGGER.error("DecorationSMSProcessor:Wrong Phone Number:"+request.getDecoratorPhone());
        }
    }

    public void refoundConfirm(DecorationRequest request){
        //负责人
        String templateScope = SmsTemplateCode.SCOPE;
        List<Tuple<String, Object>> variables = smsProvider.toTupleList("name", request.getDecoratorName());
        smsProvider.addToTupleList(variables, "applyCompamy", request.getApplyCompany());
        int templateId = SmsTemplateCode.DECORATION_REFOUND_CONFIRM;
        String templateLocale = RentalNotificationTemplateCode.locale;

        try {
            smsProvider.sendSms(request.getNamespaceId(), request.getDecoratorPhone(), templateScope, templateId, templateLocale, variables);
        }catch (RuntimeException e){
            LOGGER.error("DecorationSMSProcessor:Wrong Phone Number:"+request.getDecoratorPhone());
        }
        //租户
        variables = smsProvider.toTupleList("name", request.getApplyName());
        smsProvider.addToTupleList(variables, "applyCompamy", request.getApplyCompany());
        try {
            smsProvider.sendSms(request.getNamespaceId(), request.getApplyPhone(), templateScope, templateId, templateLocale, variables);
        }catch (RuntimeException e){
            LOGGER.error("DecorationSMSProcessor:Wrong Phone Number:"+request.getDecoratorPhone());
        }
    }

    public void createWorker(DecorationRequest request,DecorationWorker worker){
        //通知员工
        String templateScope = SmsTemplateCode.SCOPE;
        List<Tuple<String, Object>> variables = smsProvider.toTupleList("workerName", worker.getName());
        smsProvider.addToTupleList(variables, "decoratorName", request.getDecoratorName());
        smsProvider.addToTupleList(variables, "decoratorPhone", request.getDecoratorPhone());
        smsProvider.addToTupleList(variables, "applyCompamy", request.getApplyCompany());
        String homeUrl = configurationProvider.getValue("home.url","https://core.zuolin.com");
        smsProvider.addToTupleList(variables, "url", homeUrl+"/decoration-management/build/download.html?ns="+request.getNamespaceId());
        int templateId = SmsTemplateCode.DECORATION_CREATE_WORKER;
        String templateLocale = RentalNotificationTemplateCode.locale;

        try {
            smsProvider.sendSms(request.getNamespaceId(), worker.getPhone(), templateScope, templateId, templateLocale, variables);
        }catch (RuntimeException e){
            LOGGER.error("DecorationSMSProcessor:Wrong Phone Number:"+request.getDecoratorPhone());
        }
    }

    public void decorationCancel(DecorationRequest request,String operatorName,String operatorPhone){
        //负责人
        String templateScope = SmsTemplateCode.SCOPE;
        List<Tuple<String, Object>> variables = smsProvider.toTupleList("name", request.getDecoratorName());
        smsProvider.addToTupleList(variables, "applyCompamy", request.getApplyCompany());
        smsProvider.addToTupleList(variables, "operatorName", operatorName);
        smsProvider.addToTupleList(variables, "operatorPhone", operatorPhone);
        int templateId = SmsTemplateCode.DECORATION_CANCEL;
        String templateLocale = RentalNotificationTemplateCode.locale;

        try {
            smsProvider.sendSms(request.getNamespaceId(), request.getDecoratorPhone(), templateScope, templateId, templateLocale, variables);
        }catch (RuntimeException e){
            LOGGER.error("DecorationSMSProcessor:Wrong Phone Number:"+request.getDecoratorPhone());
        }
        //租户
        variables = smsProvider.toTupleList("name", request.getApplyName());
        smsProvider.addToTupleList(variables, "applyCompamy", request.getApplyCompany());
        smsProvider.addToTupleList(variables, "operatorName", operatorName);
        smsProvider.addToTupleList(variables, "operatorPhone", operatorPhone);
        try {
            smsProvider.sendSms(request.getNamespaceId(), request.getApplyPhone(), templateScope, templateId, templateLocale, variables);
        }catch (RuntimeException e){
            LOGGER.error("DecorationSMSProcessor:Wrong Phone Number:"+request.getDecoratorPhone());
        }
    }

    public void modifyFee(DecorationRequest request){
        //负责人
        String templateScope = SmsTemplateCode.SCOPE;
        List<Tuple<String, Object>> variables = smsProvider.toTupleList("name", request.getDecoratorName());
        smsProvider.addToTupleList(variables, "applyCompamy", request.getApplyCompany());
        smsProvider.addToTupleList(variables, "name", request.getDecoratorName());
        int templateId = SmsTemplateCode.DECORATION_MOTIFY_FEE;
        String templateLocale = RentalNotificationTemplateCode.locale;
        try {
            smsProvider.sendSms(request.getNamespaceId(), request.getDecoratorPhone(), templateScope, templateId, templateLocale, variables);
        }catch (RuntimeException e){
            LOGGER.error("DecorationSMSProcessor:Wrong Phone Number:"+request.getDecoratorPhone());
        }

        //租户
        variables = smsProvider.toTupleList("name", request.getApplyName());
        smsProvider.addToTupleList(variables, "applyCompamy", request.getApplyCompany());
        smsProvider.addToTupleList(variables, "name", request.getApplyName());
        try {
            smsProvider.sendSms(request.getNamespaceId(), request.getApplyPhone(), templateScope, templateId, templateLocale, variables);
        }catch (RuntimeException e){
            LOGGER.error("DecorationSMSProcessor:Wrong Phone Number:"+request.getDecoratorPhone());
        }
    }

    public void modifyRefundFee(DecorationRequest request){
        //负责人
        String templateScope = SmsTemplateCode.SCOPE;
        List<Tuple<String, Object>> variables = smsProvider.toTupleList("name", request.getDecoratorName());
        smsProvider.addToTupleList(variables, "applyCompamy", request.getApplyCompany());
        smsProvider.addToTupleList(variables, "name", request.getDecoratorName());
        int templateId = SmsTemplateCode.DECORATION_MOTIFY_REFUND;
        String templateLocale = RentalNotificationTemplateCode.locale;
        try {
            smsProvider.sendSms(request.getNamespaceId(), request.getDecoratorPhone(), templateScope, templateId, templateLocale, variables);
        }catch (RuntimeException e){
            LOGGER.error("DecorationSMSProcessor:Wrong Phone Number:"+request.getDecoratorPhone());
        }

        //租户
        variables = smsProvider.toTupleList("name", request.getApplyName());
        smsProvider.addToTupleList(variables, "applyCompamy", request.getApplyCompany());
        smsProvider.addToTupleList(variables, "name", request.getApplyName());
        try {
            smsProvider.sendSms(request.getNamespaceId(), request.getApplyPhone(), templateScope, templateId, templateLocale, variables);
        }catch (RuntimeException e){
            LOGGER.error("DecorationSMSProcessor:Wrong Phone Number:"+request.getDecoratorPhone());
        }
    }
}
