package com.everhomes.rentalv2.message_handler;

import com.alibaba.fastjson.JSONObject;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.parking.ParkingProvider;
import com.everhomes.rentalv2.*;
import com.everhomes.rest.rentalv2.SiteBillStatus;
import com.everhomes.rest.rentalv2.VipParkingUseInfoDTO;
import com.everhomes.rest.sms.SmsTemplateCode;
import com.everhomes.rest.user.IdentifierType;
import com.everhomes.sms.SmsProvider;
import com.everhomes.user.User;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserProvider;
import com.everhomes.util.Tuple;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author sw on 2018/1/5.
 */
@Component(RentalMessageHandler.RENTAL_MESSAGE_HANDLER_PREFIX + "vip_parking")
public class VipParkingRentalMessageHandler implements RentalMessageHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(VipParkingRentalMessageHandler.class);
    @Autowired
    private UserProvider userProvider;
    @Autowired
    private Rentalv2Provider rentalv2Provider;
    @Autowired
    private RentalCommonServiceImpl rentalCommonService;
    @Autowired
    private LocaleTemplateService localeTemplateService;
    @Autowired
    private SmsProvider smsProvider;
    @Autowired
    private ConfigurationProvider configurationProvider;

    @Override
    public void cancelOrderSendMessage(RentalOrder rentalBill) {
        Map<String, String> map = new HashMap<>();
        map.put("resourceTypeName", "VIP车位预约");
        String content = localeTemplateService.getLocaleTemplateString(RentalNotificationTemplateCode.SCOPE,
                RentalNotificationTemplateCode.RENTAL_USER_CANCEL_ORDER, RentalNotificationTemplateCode.locale, map, "");

        //给预约人推送订单取消成功消息
        rentalCommonService.sendRouterMessageToUser(rentalBill.getRentalUid(), content,
                rentalBill.getId(), rentalBill.getResourceType());
        String customJson = rentalBill.getCustomObject();
        VipParkingUseInfoDTO useInfoDTO = JSONObject.parseObject(customJson, VipParkingUseInfoDTO.class);

        String useDetail = getUseDetailStr(rentalBill, useInfoDTO);
        //给车主发短信
        String templateScope = SmsTemplateCode.SCOPE;
        List<Tuple<String, Object>> variables = smsProvider.toTupleList("plateOwnerName", useInfoDTO.getPlateOwnerName());
        smsProvider.addToTupleList(variables, "userName", rentalBill.getUserName());
        smsProvider.addToTupleList(variables, "userPhone", rentalBill.getUserPhone());
        smsProvider.addToTupleList(variables, "useDetail", useDetail);

        int templateId = SmsTemplateCode.RENTAL_USER_CANCEL_ORDER;

        String templateLocale = RentalNotificationTemplateCode.locale;

        try {
            smsProvider.sendSms(rentalBill.getNamespaceId(), useInfoDTO.getPlateOwnerPhone(), templateScope, templateId, templateLocale, variables);
        }catch (RuntimeException e){
            LOGGER.error("VipParkingRentalMessageHandler:Wrong Phone Number:"+useInfoDTO.getPlateOwnerPhone());
        }


    }

    @Override
    public void addOrderSendMessage(RentalOrder rentalBill) {
        //TODO:
    }

    @Override
    public void sendOrderOverTimeMessage(RentalOrder rentalBill) {

        String customJson = rentalBill.getCustomObject();
        if (StringUtils.isNotBlank(customJson)) {
            VipParkingUseInfoDTO useInfoDTO = JSONObject.parseObject(customJson, VipParkingUseInfoDTO.class);

            String useDetail = getUseDetailStr(rentalBill, useInfoDTO);
            Map<String, String> map = new HashMap<>();
            map.put("useDetail", useDetail);

            String content = localeTemplateService.getLocaleTemplateString(RentalNotificationTemplateCode.SCOPE,
                    RentalNotificationTemplateCode.UNPAID_ORDER_OVER_TIME, RentalNotificationTemplateCode.locale, map, "");

            //给预约人推送消息
            rentalCommonService.sendRouterMessageToUser(rentalBill.getRentalUid(), content,
                    rentalBill.getId(), rentalBill.getResourceType());
            //短信
            String templateScope = SmsTemplateCode.SCOPE;
            List<Tuple<String, Object>> variables = smsProvider.toTupleList("useDetail", useDetail);

            int templateId = SmsTemplateCode.UNPAID_ORDER_OVER_TIME;

            String templateLocale = RentalNotificationTemplateCode.locale;

            smsProvider.sendSms(rentalBill.getNamespaceId(), rentalBill.getUserPhone(), templateScope, templateId, templateLocale, variables);
        }
    }

    @Override
    public void sendRentalSuccessSms(RentalOrder order) {
        String customJson = order.getCustomObject();
        if (StringUtils.isNotBlank(customJson)) {
            VipParkingUseInfoDTO useInfoDTO = JSONObject.parseObject(customJson, VipParkingUseInfoDTO.class);

            String useDetail = getUseDetailStr(order, useInfoDTO);
            Map<String, String> map = new HashMap<>();
            map.put("useDetail", useDetail);
            map.put("spaceAddress", useInfoDTO.getSpaceAddress());

            String content = localeTemplateService.getLocaleTemplateString(RentalNotificationTemplateCode.SCOPE,
                    RentalNotificationTemplateCode.PAY_ORDER_SUCCESS, RentalNotificationTemplateCode.locale, map, "");

            //给预约人推送消息
            rentalCommonService.sendRouterMessageToUser(order.getRentalUid(), content,
                    order.getId(), order.getResourceType());

            //给车主发短信
            String templateScope = SmsTemplateCode.SCOPE;
            List<Tuple<String, Object>> variables = smsProvider.toTupleList("plateOwnerName", useInfoDTO.getPlateOwnerName());
            smsProvider.addToTupleList(variables, "userName", order.getUserName());
            smsProvider.addToTupleList(variables, "userPhone", order.getUserPhone());
            smsProvider.addToTupleList(variables, "useDetail", useDetail);
            smsProvider.addToTupleList(variables, "spaceAddress", useInfoDTO.getSpaceAddress());
            String homeUrl = configurationProvider.getValue("home.url","https://core.zuolin.com");
            smsProvider.addToTupleList(variables, "orderDetailUrl", homeUrl+"/vip-parking/build/index.html#/?orderId="+order.getId());

            int templateId = SmsTemplateCode.PAY_ORDER_SUCCESS;

            String templateLocale = RentalNotificationTemplateCode.locale;
            try {
                smsProvider.sendSms(order.getNamespaceId(), useInfoDTO.getPlateOwnerPhone(), templateScope, templateId, templateLocale, variables);
            } catch (RuntimeException e) {
                LOGGER.error("VipParkingRentalMessageHandler:Wrong Phone Number:" + useInfoDTO.getPlateOwnerPhone());
            }
        }
    }

    private String getUseDetailStr(RentalOrder rentalBill, VipParkingUseInfoDTO useInfoDTO) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

//        StringBuilder sb = new StringBuilder("VIP车位（");
//        sb.append(useInfoDTO.getParkingLotName());
//        sb.append(useInfoDTO.getSpaceNo());
//        sb.append("车位：");
//        sb.append(sdf.format(rentalBill.getStartTime()));
//        sb.append(" - ");
//        sb.append(sdf.format(rentalBill.getEndTime()));
//        sb.append("）");

        Map<String, String> map = new HashMap<>();
        map.put("parkingLotName", useInfoDTO.getParkingLotName());
        map.put("spaceNo", useInfoDTO.getSpaceNo());
        map.put("startTime", sdf.format(rentalBill.getStartTime()));
        map.put("endTime", sdf.format(rentalBill.getEndTime()));
        String content = localeTemplateService.getLocaleTemplateString(RentalNotificationTemplateCode.SCOPE,
                RentalNotificationTemplateCode.RENTAL_ORDER_USE_DETAIL, RentalNotificationTemplateCode.locale, map, "");

        return content;
    }

    private String getUseDetailStrByOldEndTime(RentalOrder rentalBill, VipParkingUseInfoDTO useInfoDTO) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

//        StringBuilder sb = new StringBuilder("VIP车位（");
//        sb.append(useInfoDTO.getParkingLotName());
//        sb.append(useInfoDTO.getSpaceNo());
//        sb.append("车位：");
//        sb.append(sdf.format(rentalBill.getStartTime()));
//        sb.append(" - ");
//        sb.append(sdf.format(rentalBill.getOldEndTime()));
//        sb.append("）");

        Map<String, String> map = new HashMap<>();
        map.put("parkingLotName", useInfoDTO.getParkingLotName());
        map.put("spaceNo", useInfoDTO.getSpaceNo());
        map.put("startTime", sdf.format(rentalBill.getStartTime()));
        map.put("endTime", sdf.format(rentalBill.getOldEndTime()));
        String content = localeTemplateService.getLocaleTemplateString(RentalNotificationTemplateCode.SCOPE,
                RentalNotificationTemplateCode.RENTAL_ORDER_USE_DETAIL, RentalNotificationTemplateCode.locale, map, "");

        return content;
    }

    @Override
    public void renewRentalOrderSendMessage(RentalOrder rentalBill) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        String customJson = rentalBill.getCustomObject();
        VipParkingUseInfoDTO useInfoDTO = JSONObject.parseObject(customJson, VipParkingUseInfoDTO.class);

        String useDetail = getUseDetailStrByOldEndTime(rentalBill, useInfoDTO);
        Map<String, String> map = new HashMap<>();
        map.put("useDetail", useDetail);
        map.put("newEndTime", sdf.format(rentalBill.getEndTime()));
        String refundContent = localeTemplateService.getLocaleTemplateString(RentalNotificationTemplateCode.SCOPE,
                RentalNotificationTemplateCode.RENEW_RENTAL_ORDER_SUCCESS, RentalNotificationTemplateCode.locale, map, "");
        //给预约人推送订单退款成功消息
        rentalCommonService.sendRouterMessageToUser(rentalBill.getRentalUid(), refundContent,
                rentalBill.getId(), rentalBill.getResourceType());

        //给车主发短信
        String templateScope = SmsTemplateCode.SCOPE;
        List<Tuple<String, Object>> variables = smsProvider.toTupleList("plateOwnerName", useInfoDTO.getPlateOwnerName());
        smsProvider.addToTupleList(variables, "userName", rentalBill.getUserName());
        smsProvider.addToTupleList(variables, "userPhone", rentalBill.getUserPhone());
        smsProvider.addToTupleList(variables, "useDetail", useDetail);
        smsProvider.addToTupleList(variables, "newEndTime", sdf.format(rentalBill.getEndTime()));
        String homeUrl = configurationProvider.getValue("home.url","https://core.zuolin.com");
        smsProvider.addToTupleList(variables, "orderDetailUrl", homeUrl+"/vip-parking/build/index.html#/?orderId="+rentalBill.getId());

        int templateId = SmsTemplateCode.RENEW_RENTAL_ORDER_SUCCESS;

        String templateLocale = RentalNotificationTemplateCode.locale;
        try {
            smsProvider.sendSms(rentalBill.getNamespaceId(), useInfoDTO.getPlateOwnerPhone(), templateScope, templateId, templateLocale, variables);
        } catch (RuntimeException e) {
            LOGGER.error("VipParkingRentalMessageHandler:Wrong Phone Number:" + useInfoDTO.getPlateOwnerPhone());
        }
    }

    @Override
    public void endReminderSendMessage(RentalOrder rentalBill) {


        String customJson = rentalBill.getCustomObject();
        VipParkingUseInfoDTO useInfoDTO = JSONObject.parseObject(customJson, VipParkingUseInfoDTO.class);
        String useDetail = getUseDetailStr(rentalBill, useInfoDTO);
        //消息推送
        Map<String, String> map = new HashMap<>();
        map.put("useDetail", useDetail);
        String timeupContent = localeTemplateService.getLocaleTemplateString(RentalNotificationTemplateCode.SCOPE,
                RentalNotificationTemplateCode.RENTAL_ORDER_TIME_UP, RentalNotificationTemplateCode.locale, map, "");
        rentalCommonService.sendRouterMessageToUser(rentalBill.getRentalUid(), timeupContent,
                rentalBill.getId(), rentalBill.getResourceType());
        //给车主发短信
        String templateScope = SmsTemplateCode.SCOPE;
        List<Tuple<String, Object>> variables = smsProvider.toTupleList("useDetail", useDetail);

        int templateId = SmsTemplateCode.RENTAL_ORDER_WILL_END;

        String templateLocale = RentalNotificationTemplateCode.locale;
        try {
            smsProvider.sendSms(rentalBill.getNamespaceId(), useInfoDTO.getPlateOwnerPhone(), templateScope, templateId, templateLocale, variables);
        } catch (RuntimeException e) {
            LOGGER.error("VipParkingRentalMessageHandler:Wrong Phone Number:" + useInfoDTO.getPlateOwnerPhone());
        }
    }

    @Override
    public void overTimeSendMessage(RentalOrder rentalBill) {

        String customJson = rentalBill.getCustomObject();
        VipParkingUseInfoDTO useInfoDTO = JSONObject.parseObject(customJson, VipParkingUseInfoDTO.class);

        String useDetail = getUseDetailStr(rentalBill, useInfoDTO);
        Map<String, String> map = new HashMap<>();
        map.put("useDetail", useDetail);
        map.put("unPaidAmount", String.valueOf(rentalBill.getPayTotalMoney().subtract(rentalBill.getPaidMoney())));
        String refundContent = localeTemplateService.getLocaleTemplateString(RentalNotificationTemplateCode.SCOPE,
                RentalNotificationTemplateCode.RENTAL_ORDER_OWING_FEE, RentalNotificationTemplateCode.locale, map, "");
        //给预约人推送订单超时消息
        rentalCommonService.sendRouterMessageToUser(rentalBill.getRentalUid(), refundContent,
                rentalBill.getId(), rentalBill.getResourceType());

    }

    @Override
    public void completeOrderSendMessage(RentalOrder rentalBill) {
        String customJson = rentalBill.getCustomObject();
        VipParkingUseInfoDTO useInfoDTO = JSONObject.parseObject(customJson, VipParkingUseInfoDTO.class);

        String useDetail = getUseDetailStr(rentalBill, useInfoDTO);
        Map<String, String> map = new HashMap<>();
        map.put("useDetail", useDetail);
        map.put("totalAmount", String.valueOf(rentalBill.getPayTotalMoney()));
        String refundContent = localeTemplateService.getLocaleTemplateString(RentalNotificationTemplateCode.SCOPE,
                RentalNotificationTemplateCode.COMPLETE_RENTAL_ORDER, RentalNotificationTemplateCode.locale, map, "");
        //给预约人推送订单完成消息
        rentalCommonService.sendRouterMessageToUser(rentalBill.getRentalUid(), refundContent,
                rentalBill.getId(), rentalBill.getResourceType());
    }

    @Override
    public void autoCancelOrderSendMessage(RentalOrder rentalBill) {
        String customJson = rentalBill.getCustomObject();
        VipParkingUseInfoDTO useInfoDTO = JSONObject.parseObject(customJson, VipParkingUseInfoDTO.class);

        String useDetail = getUseDetailStr(rentalBill, useInfoDTO);
        Map<String, String> map = new HashMap<>();
        map.put("useDetail", useDetail);
        String refundContent = localeTemplateService.getLocaleTemplateString(RentalNotificationTemplateCode.SCOPE,
                RentalNotificationTemplateCode.SYSTEM_AUTO_CANCEL_ORDER, RentalNotificationTemplateCode.locale, map, "");
        //给预约人推送订单自动取消消息
        rentalCommonService.sendRouterMessageToUser(rentalBill.getRentalUid(), refundContent,
                rentalBill.getId(), rentalBill.getResourceType());

        String templateScope = SmsTemplateCode.SCOPE;
        List<Tuple<String, Object>> variables = smsProvider.toTupleList("useDetail", useDetail);

        int templateId = SmsTemplateCode.SYSTEM_AUTO_CANCEL_ORDER;

        String templateLocale = RentalNotificationTemplateCode.locale;
        try {
            smsProvider.sendSms(rentalBill.getNamespaceId(), useInfoDTO.getPlateOwnerPhone(), templateScope, templateId, templateLocale, variables);
            smsProvider.sendSms(rentalBill.getNamespaceId(), rentalBill.getUserPhone(), templateScope, templateId, templateLocale, variables);
        } catch (RuntimeException e) {
            LOGGER.error("VipParkingRentalMessageHandler:Wrong Phone Number:" + useInfoDTO.getPlateOwnerPhone());
        }
    }

    @Override
    public void autoUpdateOrderSpaceSendMessage(RentalOrder rentalBill) {
        String customJson = rentalBill.getCustomObject();
        VipParkingUseInfoDTO useInfoDTO = JSONObject.parseObject(customJson, VipParkingUseInfoDTO.class);
        VipParkingUseInfoDTO oldUseInfoDTO = JSONObject.parseObject(rentalBill.getOldCustomObject(), VipParkingUseInfoDTO.class);


        String useDetail = getUseDetailStr(rentalBill, useInfoDTO);
        Map<String, String> map = new HashMap<>();
        map.put("useDetail", useDetail);
        map.put("spaceNo", oldUseInfoDTO.getSpaceNo());
        String refundContent = localeTemplateService.getLocaleTemplateString(RentalNotificationTemplateCode.SCOPE,
                RentalNotificationTemplateCode.SYSTEM_AUTO_UPDATE_SPACE, RentalNotificationTemplateCode.locale, map, "");
        //给预约人推送订单自动换车位消息
        rentalCommonService.sendRouterMessageToUser(rentalBill.getRentalUid(), refundContent,
                rentalBill.getId(), rentalBill.getResourceType());

        String templateScope = SmsTemplateCode.SCOPE;
        List<Tuple<String, Object>> variables = smsProvider.toTupleList("useDetail", useDetail);
        smsProvider.addToTupleList(variables, "spaceNo", oldUseInfoDTO.getSpaceNo());

        int templateId = SmsTemplateCode.SYSTEM_AUTO_UPDATE_SPACE_RESERVER;

        String templateLocale = RentalNotificationTemplateCode.locale;

        smsProvider.sendSms(rentalBill.getNamespaceId(), rentalBill.getUserPhone(), templateScope, templateId, templateLocale, variables);


        List<Tuple<String, Object>> variables2 = smsProvider.toTupleList("plateOwnerName", useInfoDTO.getPlateOwnerName());
        smsProvider.addToTupleList(variables2, "userName", rentalBill.getUserName());
        smsProvider.addToTupleList(variables2, "userPhone", rentalBill.getUserPhone());
        smsProvider.addToTupleList(variables2, "useDetail", useDetail);
        smsProvider.addToTupleList(variables2, "spaceNo", oldUseInfoDTO.getSpaceNo());
        String homeUrl = configurationProvider.getValue("home.url","https://core.zuolin.com");
        smsProvider.addToTupleList(variables, "orderDetailUrl", homeUrl+"/vip-parking/build/index.html#/?orderId="+rentalBill.getId());
        int templateId2 = SmsTemplateCode.SYSTEM_AUTO_UPDATE_SPACE_PLATE_OWNER;
        try {
            smsProvider.sendSms(rentalBill.getNamespaceId(), useInfoDTO.getPlateOwnerPhone(), templateScope, templateId2, templateLocale, variables2);
        } catch (RuntimeException e) {
            LOGGER.error("VipParkingRentalMessageHandler:Wrong Phone Number:" + useInfoDTO.getPlateOwnerPhone());
        }
    }

    @Override
    public void refundOrderSuccessSendMessage(RentalOrder rentalBill) {
        Map<String, String> map = new HashMap<>();
        String customJson = rentalBill.getCustomObject();
        VipParkingUseInfoDTO useInfoDTO = JSONObject.parseObject(customJson, VipParkingUseInfoDTO.class);
        String useDetail = getUseDetailStr(rentalBill, useInfoDTO);
        map.put("useDetail", useDetail);
        map.put("totalAmount", String.valueOf(rentalBill.getPayTotalMoney()));
        map.put("refundAmount", String.valueOf(rentalBill.getRefundAmount()));
        String refundContent = localeTemplateService.getLocaleTemplateString(RentalNotificationTemplateCode.SCOPE,
                RentalNotificationTemplateCode.RENTAL_USER_CANCEL_ORDER_REFUND, RentalNotificationTemplateCode.locale, map, "");
        //给预约人推送订单退款成功消息
        rentalCommonService.sendRouterMessageToUser(rentalBill.getRentalUid(), refundContent,
                rentalBill.getId(), rentalBill.getResourceType());
    }
}
