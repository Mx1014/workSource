package com.everhomes.rentalv2.message_handler;

import com.everhomes.aclink.DoorAccessService;
import com.everhomes.rentalv2.*;
import com.everhomes.rest.aclink.GetVisitorCommand;
import com.everhomes.rest.rentalv2.RentalV2ResourceType;
import com.everhomes.rest.sms.SmsTemplateCode;
import com.everhomes.rest.user.IdentifierType;
import com.everhomes.sms.SmsProvider;
import com.everhomes.user.User;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserProvider;
import com.everhomes.util.StringHelper;
import com.everhomes.util.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author sw on 2018/1/5.
 */
@Component(RentalMessageHandler.RENTAL_MESSAGE_HANDLER_PREFIX + "default")
public class DefaultRentalMessageHandler implements RentalMessageHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultRentalMessageHandler.class);

    @Autowired
    private Rentalv2Provider rentalv2Provider;
    @Autowired
    private UserProvider userProvider;
    @Autowired
    private RentalCommonServiceImpl rentalCommonService;
    @Autowired
    private SmsProvider smsProvider;
    @Autowired
    private DoorAccessService doorAccessService;

    @Override
    public void cancelOrderSendMessage(RentalOrder rentalBill) {
        RentalResource rs = rentalCommonService.getRentalResource(rentalBill.getResourceType(), rentalBill.getRentalResourceId());

//			RentalResource rs = this.rentalv2Provider.getRentalSiteById(rentalBill.getRentalResourceId());
        if(null == rs)
            return;
        //给负责人推送
        StringBuilder managerContent = new StringBuilder();
        User user = userProvider.findUserById(rentalBill.getRentalUid()) ;
        if (null == user )
            return;
        managerContent.append(user.getNickName());
        managerContent.append("取消预约");
        managerContent.append(rentalBill.getResourceName());
        managerContent.append("\n使用详情：");
        managerContent.append(rentalBill.getUseDetail());
        if(null != rentalBill.getRentalCount() ){
            managerContent.append("\n预约数：");
            managerContent.append(rentalBill.getRentalCount());
        }
        //用户取消订单给 资源负责人发送消息
        rentalCommonService.sendMessageToUser(rs.getChargeUid(), managerContent.toString());
    }

    @Override
    public void addOrderSendMessage(RentalOrder rentalBill) {
        User user = this.userProvider.findUserById(rentalBill.getRentalUid()) ;
        if (null == user )
            return;

        RentalResource rs = rentalCommonService.getRentalResource(rentalBill.getResourceType(), rentalBill.getRentalResourceId());

        if(null == rs)
            return;

        try{
            Map<String, String>map = new HashMap<>();
            map.put("userName", user.getNickName());
            map.put("resourceName", rentalBill.getResourceName());
            map.put("useDetail", rentalBill.getUseDetail());
            map.put("rentalCount", rentalBill.getRentalCount() ==null ? "1" : String.valueOf(rentalBill.getRentalCount()));
            rentalCommonService.sendMessageCode(rs.getChargeUid(), map, RentalNotificationTemplateCode.RENTAL_ADMIN_NOTIFY);
        }catch(Exception e){
            LOGGER.error("SEND MESSAGE FAILED", e);
        }
    }

    @Override
    public void sendOrderOverTimeMessage(RentalOrder rentalBill) {
        String useDetail = rentalBill.getUseDetail();
        Map<String, String> map = new HashMap<>();
        map.put("useDetail", useDetail);
        //给预约人推送消息
        rentalCommonService.sendMessageCode(rentalBill.getRentalUid(), map, RentalNotificationTemplateCode.UNPAID_ORDER_OVER_TIME);
    }

    @Override
    public void sendRentalSuccessSms(RentalOrder order){
        //发推送
        Map<String, String> map = new HashMap<>();
        map.put("useTime", order.getUseDetail());
        map.put("resourceName", order.getResourceName());
        rentalCommonService.sendMessageCode(order.getRentalUid(), map,
                RentalNotificationTemplateCode.RENTAL_PAY_SUCCESS_CODE);
        //发短信
        String templateScope = SmsTemplateCode.SCOPE;
        String templateLocale = RentalNotificationTemplateCode.locale;
        int templateId = SmsTemplateCode.RENTAL_PAY_SUCCESS_CODE;

        List<Tuple<String, Object>> variables = smsProvider.toTupleList("useTime", order.getUseDetail());
        smsProvider.addToTupleList(variables, "resourceName", order.getResourceName());
        smsProvider.addToTupleList(variables, "orderNum", order.getOrderNo());
        smsProvider.addToTupleList(variables, "aclink", getAclinkInfo(order));

        UserIdentifier userIdentifier = this.userProvider.findClaimedIdentifierByOwnerAndType(order.getCreatorUid(),
                IdentifierType.MOBILE.getCode());
        if (null == userIdentifier) {
            LOGGER.error("userIdentifier is null...userId = " + order.getCreatorUid());
        } else {
            smsProvider.sendSms(order.getNamespaceId(), userIdentifier.getIdentifierToken(), templateScope,
                    templateId, templateLocale, variables);
        }
    }

    private String  getAclinkInfo(RentalOrder order){
        if (StringHelper.hasContent(order.getDoorAuthId())){
            StringBuilder sb = new StringBuilder().append("请点击以下链接使用门禁二维码：");
            String [] authes = order.getDoorAuthId().split(",");
            for (String aclinkId : authes){
                GetVisitorCommand cmd = new GetVisitorCommand();
                cmd.setId(aclinkId);
                String url = doorAccessService.getVisitorUrlById(cmd);
                if (StringHelper.hasContent(url)){
                    sb.append(url).append("\n");
                }
            }
            return sb.deleteCharAt(sb.length()-1).toString();
        }
        return "";
    }

    @Override
    public void cancelOrderWithoutPaySendMessage(RentalOrder rentalBill) {
        //发推送
        Map<String, String> map = new HashMap<>();
        map.put("useDetail", rentalBill.getUseDetail());
        rentalCommonService.sendMessageCode(rentalBill.getRentalUid(), map,
                RentalNotificationTemplateCode.RENTAL_CANCEL_NOT_PAY);
        //发短信
        String templateScope = SmsTemplateCode.SCOPE;
        List<Tuple<String, Object>> variables = smsProvider.toTupleList("useDetail", rentalBill.getUseDetail());

        int templateId = SmsTemplateCode.UNPAID_ORDER_OVER_TIME;

        String templateLocale = RentalNotificationTemplateCode.locale;

        smsProvider.sendSms(rentalBill.getNamespaceId(), rentalBill.getUserPhone(), templateScope, templateId, templateLocale, variables);
    }

    @Override
    public void cancelOrderWithoutRefund(RentalOrder rentalBill) {
        //发推送
        Map<String, String> map = new HashMap<>();
        map.put("useDetail", rentalBill.getUseDetail());
        map.put("totalAmount", rentalBill.getPayTotalMoney().toString());
        rentalCommonService.sendMessageCode(rentalBill.getRentalUid(), map,
                RentalNotificationTemplateCode.RENTAL_CANCEL_ORDER_NO_REFUND);

        //发短信
        String templateScope = SmsTemplateCode.SCOPE;
        List<Tuple<String, Object>> variables = smsProvider.toTupleList("useDetail", rentalBill.getUseDetail());
        smsProvider.addToTupleList(variables,"totalAmount",rentalBill.getPayTotalMoney().toString());
        int templateId = SmsTemplateCode.RENTAL_CANCEL_ORDER_NO_REFUND;
        String templateLocale = RentalNotificationTemplateCode.locale;
        smsProvider.sendSms(rentalBill.getNamespaceId(), rentalBill.getUserPhone(), templateScope, templateId, templateLocale, variables);

    }

    @Override
    public void cancelOrderNeedRefund(RentalOrder rentalBill) {
        //发推送
        Map<String, String> map = new HashMap<>();
        map.put("useDetail", rentalBill.getUseDetail());
        map.put("orderNum", rentalBill.getOrderNo());
        map.put("refundAmount", rentalBill.getRefundAmount().toString());
        map.put("totalAmount", rentalBill.getPayTotalMoney().toString());
        rentalCommonService.sendMessageCode(rentalBill.getRentalUid(), map,
                RentalNotificationTemplateCode.RENTAL_CANCEL_ORDER);

        //发短信
        String templateScope = SmsTemplateCode.SCOPE;
        List<Tuple<String, Object>> variables = smsProvider.toTupleList("useDetail", rentalBill.getUseDetail());
        smsProvider.addToTupleList(variables,"totalAmount",rentalBill.getPayTotalMoney().toString());
        smsProvider.addToTupleList(variables,"refundAmount",rentalBill.getRefundAmount().toString());
        smsProvider.addToTupleList(variables,"orderNum",rentalBill.getOrderNo());
        int templateId = SmsTemplateCode.RENTAL_CANCEL_ORDER;
        String templateLocale = RentalNotificationTemplateCode.locale;
        smsProvider.sendSms(rentalBill.getNamespaceId(), rentalBill.getUserPhone(), templateScope, templateId, templateLocale, variables);
    }

    @Override
    public void renewRentalOrderSendMessage(RentalOrder rentalBill) {

    }

    @Override
    public void endReminderSendMessage(RentalOrder rentalBill) {

    }

    @Override
    public void overTimeSendMessage(RentalOrder rentalBill) {

    }

    @Override
    public void completeOrderSendMessage(RentalOrder rentalBill) {

    }

    @Override
    public void autoCancelOrderSendMessage(RentalOrder rentalBill) {

    }

    @Override
    public void autoUpdateOrderSpaceSendMessage(RentalOrder rentalBill) {

    }

    @Override
    public void refundOrderSuccessSendMessage(RentalOrder rentalBill) {
        //发推送
        Map<String, String> map = new HashMap<>();
        map.put("useDetail", rentalBill.getUseDetail());
        map.put("totalAmount", rentalBill.getPayTotalMoney().toString());
        map.put("refundAmount", rentalBill.getRefundAmount().toString());
        rentalCommonService.sendMessageCode(rentalBill.getRentalUid(), map,
                RentalNotificationTemplateCode.RENTAL_CANCEL_ORDER_REFUND);
        //发短信
        String templateScope = SmsTemplateCode.SCOPE;
        List<Tuple<String, Object>> variables = smsProvider.toTupleList("useDetail", rentalBill.getUseDetail());
        smsProvider.addToTupleList(variables, "totalAmount", rentalBill.getPayTotalMoney().toString());
        smsProvider.addToTupleList(variables, "refundAmount", rentalBill.getRefundAmount().toString());
        int templateId = SmsTemplateCode.RENTAL_CANCEL_ORDER_REFUND;

        String templateLocale = RentalNotificationTemplateCode.locale;

        smsProvider.sendSms(rentalBill.getNamespaceId(), rentalBill.getUserPhone(), templateScope, templateId, templateLocale, variables);
    }
}
