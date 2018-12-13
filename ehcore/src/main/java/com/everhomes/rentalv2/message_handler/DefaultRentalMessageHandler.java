package com.everhomes.rentalv2.message_handler;

import com.everhomes.aclink.DoorAccessService;
import com.everhomes.contentserver.ContentServerResource;
import com.everhomes.entity.EntityType;
import com.everhomes.rentalv2.*;
import com.everhomes.rest.aclink.GetVisitorCommand;
import com.everhomes.rest.rentalv2.*;
import com.everhomes.rest.rentalv2.admin.AttachmentType;
import com.everhomes.rest.sms.SmsTemplateCode;
import com.everhomes.rest.user.IdentifierType;
import com.everhomes.sms.SmsProvider;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.StringHelper;
import com.everhomes.util.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        managerContent.append(rentalBill.getUserName()).append("（").append(rentalBill.getUserPhone()).append("）");
        managerContent.append("取消预约");
        managerContent.append(rentalBill.getResourceName()).append("（").append(rentalBill.getUseDetail()).append("）");
        //用户取消订单给 资源负责人发送消息
        rentalCommonService.sendMessageToUser(rs.getChargeUid(), managerContent.toString());
    }

    @Override
    public void addOrderSendMessage(RentalOrder rentalBill) {

        RentalResource rs = rentalCommonService.getRentalResource(rentalBill.getResourceType(), rentalBill.getRentalResourceId());

        if(null == rs)
            return;
        //发推送

        Map<String, String> map = new HashMap<>();
        map.put("userName", rentalBill.getUserName());
        map.put("phone", rentalBill.getUserPhone());
        map.put("resourceName", rentalBill.getResourceName());
        map.put("useDetail", rentalBill.getUseDetail());
        map.put("freeGoods", processFreeGoods(rentalBill));
        map.put("paidGoods",  processPaidGoods(rentalBill));
        rentalCommonService.sendMessageCode(rs.getChargeUid(), map,
                    RentalNotificationTemplateCode.RENTAL_CANCEL_NOT_PAY);

        //发短信
        String templateScope = SmsTemplateCode.SCOPE;
        String templateLocale = RentalNotificationTemplateCode.locale;
        int templateId = SmsTemplateCode.RENTAL_ADMIN_NOTIFY;

        List<Tuple<String, Object>> variables = smsProvider.toTupleList("userName", rentalBill.getUserName());
        smsProvider.addToTupleList(variables, "phone", rentalBill.getUserPhone());
        smsProvider.addToTupleList(variables, "resourceName", rentalBill.getResourceName());
        smsProvider.addToTupleList(variables, "useDetail", rentalBill.getUseDetail());
        smsProvider.addToTupleList(variables, "freeGoods", processFreeGoods(rentalBill));
        smsProvider.addToTupleList(variables, "paidGoods", processPaidGoods(rentalBill));
        String[] chargeUids = rs.getChargeUid().split(",");
        for (String uid : chargeUids){
            UserIdentifier userIdentifier = this.userProvider.findClaimedIdentifierByOwnerAndType(Long.valueOf(uid),
                    IdentifierType.MOBILE.getCode());
            if (null == userIdentifier) {
                LOGGER.error("userIdentifier is null...userId = " + uid);
            } else {
                smsProvider.sendSms(rentalBill.getNamespaceId(), userIdentifier.getIdentifierToken(), templateScope,
                        templateId, templateLocale, variables);
            }
        }
    }

    private String processFreeGoods(RentalOrder rentalBill){
        StringBuilder sb = new StringBuilder();
        List<RentalOrderAttachment> attachments = rentalv2Provider.findRentalBillAttachmentByBillId(rentalBill.getId());
            for (RentalOrderAttachment attachment : attachments) {
                if (attachment.getAttachmentType().equals(AttachmentType.GOOD_ITEM.getCode())) {
                    List<RentalConfigAttachment> tempAttachments = rentalv2Provider
                            .queryRentalConfigAttachmentByOwner(rentalBill.getResourceType(), AttachmentType.ORDER_GOOD_ITEM.name(),
                                    rentalBill.getId(), null);
                    if (tempAttachments != null && tempAttachments.size() > 0){
                        sb.append("，需要物资：");
                        for (RentalConfigAttachment attachment1 : tempAttachments){
                            sb.append(attachment1.getItemName()).append("、");
                        }
                        sb.deleteCharAt(sb.length() - 1);
                    }
                    break;
                }
            }
        return sb.toString();
    }

    private String processPaidGoods(RentalOrder rentalBill){
        StringBuilder sb = new StringBuilder();
        List<RentalItemsOrder> ribs = rentalv2Provider.findRentalItemsBillBySiteBillId(rentalBill.getId(), rentalBill.getResourceType());
        if (null != ribs) {
            sb.append("，购买商品：");
            for (RentalItemsOrder item : ribs) {
                sb.append(item.getItemName()).append("*").append(item.getRentalCount()).append("、");
            }
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    @Override
    public void sendOrderOverTimeMessage(RentalOrder rentalBill) {
        //发短信
        String templateScope = SmsTemplateCode.SCOPE;
        String templateLocale = RentalNotificationTemplateCode.locale;
        int templateId = SmsTemplateCode.RENTAL_CANCEL_NOT_PAY;

        List<Tuple<String, Object>> variables = smsProvider.toTupleList("useDetail", rentalBill.getUseDetail());
        smsProvider.addToTupleList(variables, "resourceName", rentalBill.getResourceName());
        smsProvider.addToTupleList(variables, "orderNum", rentalBill.getOrderNo());

        String phone = rentalBill.getUserPhone();
        UserIdentifier userIdentifier = this.userProvider.findClaimedIdentifierByOwnerAndType(rentalBill.getRentalUid(),
                IdentifierType.MOBILE.getCode());
        if (userIdentifier != null)
            phone = userIdentifier.getIdentifierToken();
        if (null == phone) {
            LOGGER.error("phone is null...userId = " + rentalBill.getRentalUid());
        } else {
            smsProvider.sendSms(rentalBill.getNamespaceId(), phone, templateScope,
                    templateId, templateLocale, variables);
        }
    }

    @Override
    public void sendRentalSuccessSms(RentalOrder order){
        //发推送
//        Map<String, String> map = new HashMap<>();
//        map.put("useTime", order.getUseDetail());
//        map.put("resourceName", order.getResourceName());
//        rentalCommonService.sendMessageCode(order.getRentalUid(), map,
//                RentalNotificationTemplateCode.RENTAL_PAY_SUCCESS_CODE);
        //发短信
        String templateScope = SmsTemplateCode.SCOPE;
        String templateLocale = RentalNotificationTemplateCode.locale;
        int templateId = SmsTemplateCode.RENTAL_PAY_SUCCESS_CODE;

        List<Tuple<String, Object>> variables = smsProvider.toTupleList("useTime", order.getUseDetail());
        smsProvider.addToTupleList(variables, "resourceName", order.getResourceName());
        smsProvider.addToTupleList(variables, "orderNum", order.getOrderNo());
        smsProvider.addToTupleList(variables, "aclink", getAclinkInfo(order));

        String phone = order.getUserPhone();
        UserIdentifier userIdentifier = this.userProvider.findClaimedIdentifierByOwnerAndType(order.getRentalUid(),
                IdentifierType.MOBILE.getCode());
        if (userIdentifier != null)
            phone = userIdentifier.getIdentifierToken();
        if (null == phone) {
            LOGGER.error("phone is null...userId = " + order.getRentalUid());
        } else {
            smsProvider.sendSms(order.getNamespaceId(), phone, templateScope,
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
        map.put("resourceName", rentalBill.getResourceName());
        if (!rentalBill.getRentalUid().equals(0L))
            rentalCommonService.sendMessageCode(rentalBill.getRentalUid(), map,
                RentalNotificationTemplateCode.RENTAL_CANCEL_NOT_PAY);
    }

    @Override
    public void cancelOrderWithoutRefund(RentalOrder rentalBill) {
        //发推送
        Map<String, String> map = new HashMap<>();
        map.put("useDetail", rentalBill.getUseDetail());
        map.put("resourceName",rentalBill.getResourceName());
        map.put("totalAmount", rentalBill.getPayTotalMoney().toString());
        if (!rentalBill.getRentalUid().equals(0L))
         rentalCommonService.sendMessageCode(rentalBill.getRentalUid(), map,
                RentalNotificationTemplateCode.RENTAL_CANCEL_ORDER_NO_REFUND);

        //发短信
        String templateScope = SmsTemplateCode.SCOPE;
        List<Tuple<String, Object>> variables = smsProvider.toTupleList("useDetail", rentalBill.getUseDetail());
        smsProvider.addToTupleList(variables,"totalAmount",rentalBill.getPayTotalMoney().toString());
        smsProvider.addToTupleList(variables,"resourceName",rentalBill.getResourceName());
        smsProvider.addToTupleList(variables,"orderNum",rentalBill.getOrderNo());
        int templateId = SmsTemplateCode.RENTAL_CANCEL_ORDER_NO_REFUND;
        String templateLocale = RentalNotificationTemplateCode.locale;
        smsProvider.sendSms(rentalBill.getNamespaceId(), rentalBill.getUserPhone(), templateScope, templateId, templateLocale, variables);

    }

    @Override
    public void cancelOrderNeedRefund(RentalOrder rentalBill) {
        //发推送
        Map<String, String> map = new HashMap<>();
        map.put("useDetail", rentalBill.getUseDetail());
        map.put("refundAmount", rentalBill.getRefundAmount().toString());
        map.put("totalAmount", rentalBill.getPayTotalMoney().toString());
        map.put("resourceName", rentalBill.getResourceName());
        if (!rentalBill.getRentalUid().equals(0L))
             rentalCommonService.sendMessageCode(rentalBill.getRentalUid(), map,
                RentalNotificationTemplateCode.RENTAL_CANCEL_ORDER);

        //发短信
        String templateScope = SmsTemplateCode.SCOPE;
        List<Tuple<String, Object>> variables = smsProvider.toTupleList("useDetail", rentalBill.getUseDetail());
        smsProvider.addToTupleList(variables,"totalAmount",rentalBill.getPayTotalMoney().toString());
        smsProvider.addToTupleList(variables,"refundAmount",rentalBill.getRefundAmount().toString());
        smsProvider.addToTupleList(variables,"orderNum",rentalBill.getOrderNo());
        smsProvider.addToTupleList(variables,"resourceName",rentalBill.getResourceName());
        int templateId = SmsTemplateCode.RENTAL_CANCEL_ORDER;
        String templateLocale = RentalNotificationTemplateCode.locale;
        smsProvider.sendSms(rentalBill.getNamespaceId(), rentalBill.getUserPhone(), templateScope, templateId, templateLocale, variables);
    }

    @Override
    public void orderNearStartSendMessage(RentalOrder rentalBill) {
        //用户发推送
        Map<String, String> map = new HashMap<>();
        map.put("useDetail", rentalBill.getUseDetail());
        map.put("resourceName", rentalBill.getResourceName());
        if (!rentalBill.getRentalUid().equals(0L))
             rentalCommonService.sendMessageCode(rentalBill.getRentalUid(), map,
                RentalNotificationTemplateCode.RENTAL_BEGIN_NOTIFY);

        //用户发短信
        String templateScope = SmsTemplateCode.SCOPE;
        List<Tuple<String, Object>> variables = smsProvider.toTupleList("useDetail", rentalBill.getUseDetail());
        smsProvider.addToTupleList(variables,"resourceName",rentalBill.getResourceName());
        int templateId = SmsTemplateCode.RENTAL_BEGIN_NOTIFY;
        String templateLocale = RentalNotificationTemplateCode.locale;
        smsProvider.sendSms(rentalBill.getNamespaceId(), rentalBill.getUserPhone(), templateScope, templateId, templateLocale, variables);

        //负责人发推送
        RentalResource rs = rentalCommonService.getRentalResource(rentalBill.getResourceType(), rentalBill.getRentalResourceId());
        map = new HashMap<>();
        map.put("useDetail", rentalBill.getUseDetail());
        map.put("resourceName", rentalBill.getResourceName());
        map.put("requestorName", rentalBill.getUserName());
        map.put("requestorPhone", rentalBill.getUserPhone());
        rentalCommonService.sendMessageCode(rs.getChargeUid(), map,
                RentalNotificationTemplateCode.RENTAL_BEGIN_CHARGE_NOTIFY);
    }

    @Override
    public void orderNearEndSendMessage(RentalOrder rentalBill) {
        //用户发推送
        Map<String, String> map = new HashMap<>();
        map.put("useDetail", rentalBill.getUseDetail());
        map.put("resourceName", rentalBill.getResourceName());
        if (!rentalBill.getRentalUid().equals(0L))
             rentalCommonService.sendMessageCode(rentalBill.getRentalUid(), map,
                RentalNotificationTemplateCode.RENTAL_END_NOTIFY_HOUR_USER);

        //用户发短信
        String templateScope = SmsTemplateCode.SCOPE;
        List<Tuple<String, Object>> variables = smsProvider.toTupleList("useDetail", rentalBill.getUseDetail());
        smsProvider.addToTupleList(variables,"resourceName",rentalBill.getResourceName());
        int templateId = SmsTemplateCode.RENTAL_END_NOTIFY_HOUR_USER;
        String templateLocale = RentalNotificationTemplateCode.locale;
        smsProvider.sendSms(rentalBill.getNamespaceId(), rentalBill.getUserPhone(), templateScope, templateId, templateLocale, variables);

        //负责人发推送
        RentalResource rs = rentalCommonService.getRentalResource(rentalBill.getResourceType(), rentalBill.getRentalResourceId());
        map = new HashMap<>();
        map.put("resourceName", rentalBill.getResourceName());
        map.put("requestorName", rentalBill.getUserName());
        map.put("requestorPhone", rentalBill.getUserPhone());
        rentalCommonService.sendMessageCode(rs.getChargeUid(), map,
                RentalNotificationTemplateCode.RENTAL_END_NOTIFY_HOUR);
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
        map.put("resourceName", rentalBill.getResourceName());
        if (!rentalBill.getRentalUid().equals(0L))
            rentalCommonService.sendMessageCode(rentalBill.getRentalUid(), map,
                RentalNotificationTemplateCode.RENTAL_CANCEL_ORDER_REFUND);
        //发短信
        String templateScope = SmsTemplateCode.SCOPE;
        List<Tuple<String, Object>> variables = smsProvider.toTupleList("useDetail", rentalBill.getUseDetail());
        smsProvider.addToTupleList(variables, "totalAmount", rentalBill.getPayTotalMoney().toString());
        smsProvider.addToTupleList(variables, "refundAmount", rentalBill.getRefundAmount().toString());
        smsProvider.addToTupleList(variables, "resourceName", rentalBill.getResourceName());
        smsProvider.addToTupleList(variables, "orderNum", rentalBill.getOrderNo());
        int templateId = SmsTemplateCode.RENTAL_CANCEL_ORDER_REFUND;

        String templateLocale = RentalNotificationTemplateCode.locale;

        smsProvider.sendSms(rentalBill.getNamespaceId(), rentalBill.getUserPhone(), templateScope, templateId, templateLocale, variables);
    }
}
