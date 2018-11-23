package com.everhomes.rentalv2;

import com.everhomes.app.App;
import com.everhomes.app.AppProvider;
import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.messaging.MessagingService;
import com.everhomes.organization.OrganizationMember;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.rentalv2.utils.RentalUtils;
import com.everhomes.rest.activity.ActivityRosterPayVersionFlag;
import com.everhomes.rest.app.AppConstants;
import com.everhomes.rest.common.RentalOrderActionData;
import com.everhomes.rest.common.Router;
import com.everhomes.rest.common.ServiceModuleConstants;
import com.everhomes.rest.messaging.*;
import com.everhomes.rest.order.OrderType;
import com.everhomes.rest.organization.VendorType;
import com.everhomes.rest.pay.controller.CreateOrderRestResponse;
import com.everhomes.rest.rentalv2.*;
import com.everhomes.rest.rentalv2.admin.*;
import com.everhomes.rest.user.IdentifierType;
import com.everhomes.rest.user.MessageChannelType;
import com.everhomes.serviceModuleApp.ServiceModuleApp;
import com.everhomes.serviceModuleApp.ServiceModuleAppService;
import com.everhomes.techpark.onlinePay.OnlinePayService;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserProvider;
import com.everhomes.util.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author sw on 2018/1/9.
 */
@Component
public class RentalCommonServiceImpl {

    private static final Logger LOGGER = LoggerFactory.getLogger(Rentalv2ServiceImpl.class);

    @Autowired
    private MessagingService messagingService;
    @Autowired
    private LocaleTemplateService localeTemplateService;
    @Autowired
    private Rentalv2Provider rentalv2Provider;
    @Autowired
    private AppProvider appProvider;
    @Autowired
    private ConfigurationProvider configurationProvider;
    @Autowired
    private OnlinePayService onlinePayService;
    @Autowired
    private LocaleStringService localeStringService;
    @Autowired
    private UserProvider userProvider;
    @Autowired
    private OrganizationProvider organizationProvider;
    @Autowired
    private Rentalv2PayService  rentalv2PayService;
    @Autowired
    private ServiceModuleAppService serviceModuleAppService;

    public RentalResourceHandler getRentalResourceHandler(String handlerName) {
        RentalResourceHandler handler = null;

        if(handlerName != null && handlerName.length() > 0) {
            String handlerPrefix = RentalResourceHandler.RENTAL_RESOURCE_HANDLER_PREFIX;
            handler = PlatformContext.getComponent(handlerPrefix + handlerName);
        }

        return handler;
    }

    public RentalOrderHandler getRentalOrderHandler(String handlerName) {
        RentalOrderHandler handler = null;

        if(handlerName != null && handlerName.length() > 0) {
            String handlerPrefix = RentalOrderHandler.RENTAL_ORDER_HANDLER_PREFIX;
            handler = PlatformContext.getComponent(handlerPrefix + handlerName);
        }

        return handler;
    }

    public RentalMessageHandler getRentalMessageHandler(String handlerName) {
        RentalMessageHandler handler = null;

        if(handlerName != null && handlerName.length() > 0) {
            String handlerPrefix = RentalMessageHandler.RENTAL_MESSAGE_HANDLER_PREFIX;
            handler = PlatformContext.getComponent(handlerPrefix + handlerName);
        }

        return handler;
    }

    public RentalResource getRentalResource(String resourceType, Long resourceId) {
        String handlerName = RentalResourceHandler.DEFAULT;
        if (StringUtils.isNotBlank(resourceType)) {
            handlerName = resourceType;
        }
        RentalResourceHandler handler = getRentalResourceHandler(handlerName);
        RentalResource rs = handler.getRentalResourceById(resourceId);

        return rs;
    }

    public RentalResourceType createRentalResourceType(Integer namespaceId, String name, Byte pageType,Byte payMode,
                                                       String identify,Byte unauthVisible){
        RentalResourceType rentalResourceType = new RentalResourceType();
        rentalResourceType.setNamespaceId(namespaceId);
        rentalResourceType.setName(name);
        if(null == pageType)
            pageType = 0;
        if (null == payMode)
            payMode = 0;
        if (null == identify)
            identify = RentalV2ResourceType.DEFAULT.getCode();
        if (null == unauthVisible)
            unauthVisible = (byte)0;
        rentalResourceType.setPageType(pageType);
        rentalResourceType.setPayMode(payMode);
        rentalResourceType.setIdentify(identify);
        rentalResourceType.setStatus(ResourceTypeStatus.NORMAL.getCode());
        rentalResourceType.setUnauthVisible(unauthVisible);
        rentalv2Provider.createRentalResourceType(rentalResourceType);
        return rentalResourceType;
    }

    public void rentalOrderSuccess(Long rentalBillId){
        RentalOrder rentalBill = rentalv2Provider.findRentalBillById(rentalBillId);
        rentalBill.setStatus(SiteBillStatus.SUCCESS.getCode());
        rentalv2Provider.updateRentalBill(rentalBill);
    }

    public void rentalOrderCancel(Long rentalBillId){
        RentalOrder rentalBill = rentalv2Provider.findRentalBillById(rentalBillId);
        rentalBill.setStatus(SiteBillStatus.FAIL.getCode());
        rentalv2Provider.updateRentalBill(rentalBill);
    }

    public void sendMessageToUser(Long userId, String content) {
        if(null == userId)
            return;
        MessageDTO messageDto = new MessageDTO();
        messageDto.setAppId(AppConstants.APPID_MESSAGING);
        messageDto.setSenderUid(User.SYSTEM_USER_LOGIN.getUserId());
        messageDto.setChannels(new MessageChannel(MessageChannelType.USER.getCode(), userId.toString()));
        messageDto.setChannels(new MessageChannel(MessageChannelType.USER.getCode(), Long.toString(User.SYSTEM_USER_LOGIN.getUserId())));
        messageDto.setBodyType(MessageBodyType.TEXT.getCode());
        messageDto.setBody(content);
        messageDto.setMetaAppId(AppConstants.APPID_MESSAGING);
        LOGGER.debug("messageDTO : {}", messageDto);
        // 推送
        messagingService.routeMessage(User.SYSTEM_USER_LOGIN, AppConstants.APPID_MESSAGING, MessageChannelType.USER.getCode(),
                userId.toString(), messageDto, MessagingConstants.MSG_FLAG_STORED_PUSH.getCode());
    }

    public void sendMessageToUser(String uids, String content) {
        try {
            String[] userIds = uids.split(",");
            for (String uid : userIds) {
                sendMessageToUser(Long.valueOf(uid), content);
            }
        }catch (Exception e){
            LOGGER.error("send messages error uids = {} exception = {}", uids,e);
        }
    }

    public void sendRouterMessageToUser(Long userId, String content, Long orderId, String resourceType) {
        if(null == userId)
            return;
        MessageDTO messageDto = new MessageDTO();
        messageDto.setAppId(AppConstants.APPID_MESSAGING);
        messageDto.setSenderUid(User.SYSTEM_USER_LOGIN.getUserId());
        messageDto.setChannels(new MessageChannel(MessageChannelType.USER.getCode(), userId.toString()));
        messageDto.setChannels(new MessageChannel(MessageChannelType.USER.getCode(), Long.toString(User.SYSTEM_USER_LOGIN.getUserId())));
        messageDto.setBodyType(MessageBodyType.TEXT.getCode());
        messageDto.setBody(content);
        messageDto.setMetaAppId(AppConstants.APPID_MESSAGING);

        RentalOrderActionData actionData = new RentalOrderActionData();
        actionData.setOrderId(orderId);
        actionData.setResourceType(resourceType);
        actionData.setMetaObject("");

        String routerUri = RouterBuilder.build(Router.RENTAL_ORDER_DETAIL, actionData);

        RouterMetaObject mo = new RouterMetaObject();
        mo.setUrl(routerUri);
        Map<String, String> meta = new HashMap<>();
        meta.put(MessageMetaConstant.META_OBJECT_TYPE, MetaObjectType.MESSAGE_ROUTER.getCode());
        meta.put(MessageMetaConstant.META_OBJECT, StringHelper.toJsonString(mo));
        messageDto.setMeta(meta);

        LOGGER.debug("messageDTO : {}", messageDto);
        // 发消息 +推送
        messagingService.routeMessage(User.SYSTEM_USER_LOGIN, AppConstants.APPID_MESSAGING, MessageChannelType.USER.getCode(),
                userId.toString(), messageDto, MessagingConstants.MSG_FLAG_STORED_PUSH.getCode());
    }

    public void sendMessageCode(Long uid, Map<String, String> map, int code) {
        String scope = RentalNotificationTemplateCode.SCOPE;
        String locale = RentalNotificationTemplateCode.locale;

        String notifyText = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
        sendMessageToUser(uid, notifyText);
    }

    public void sendMessageCode(String uids, Map<String, String> map, int code) {
        try {
            String[] userIds = uids.split(",");
            for (String uid : userIds) {
                sendMessageCode(Long.valueOf(uid), map, code);
            }
        }catch (Exception e){
            LOGGER.error("send messages error uids = {} exception = {}", uids,e);
        }
    }

    public BigDecimal calculateOverTimeFee(RentalOrder order,BigDecimal amount, long now) {

        if (order.getRefundStrategy() == RentalOrderStrategy.CUSTOM.getCode()) {
            RentalDefaultRule rule = this.rentalv2Provider.getRentalDefaultRule(null, null,
                    order.getResourceType(), order.getResourceTypeId(), RuleSourceType.RESOURCE.getCode(), order.getRentalResourceId());


            List<RentalOrderRule> refundRules = rentalv2Provider.listRentalOrderRules(order.getResourceType(), rule.getSourceType(),
                    rule.getId(), RentalOrderHandleType.OVERTIME.getCode());

            List<RentalOrderRule> outerRules = refundRules.stream().filter(r -> r.getDurationType() == RentalDurationType.OUTER.getCode())
                    .collect(Collectors.toList());
            List<RentalOrderRule> innerRules = refundRules.stream().filter(r -> r.getDurationType() == RentalDurationType.INNER.getCode())
                    .collect(Collectors.toList());

            RentalOrderRule orderRule = null;
            Long endUseTime = order.getEndTime().getTime();

            long intervalTime = now - endUseTime;

            //处于时间内，查找最小的时间
            for (RentalOrderRule r : innerRules) {
                long duration = 0;

                if (r.getDurationUnit() == RentalDurationUnit.HOUR.getCode()) {
                    duration = (long) (r.getDuration() * 60 * 60 * 1000);
                }
                if (intervalTime < duration) {
                    if (null == orderRule || r.getDuration() < orderRule.getDuration()) {
                        orderRule = r;
                    }
                }
            }

            if (orderRule == null) {
                //处于时间外，查找最大的时间
                for (RentalOrderRule r : outerRules) {
                    long duration = 0;

                    if (r.getDurationUnit() == RentalDurationUnit.HOUR.getCode()) {
                        duration = (long) (r.getDuration() * 60 * 60 * 1000);
                    }
                    if (intervalTime > duration) {
                        if (null == orderRule || r.getDuration() > orderRule.getDuration()) {
                            orderRule = r;
                        }
                    }
                }
            }

            amount = amount.multiply(new BigDecimal(orderRule.getFactor()));
            BigDecimal totalAmount = order.getPayTotalMoney().add(amount);//计算价格
            order.setResourceTotalMoney(totalAmount);
            order.setPayTotalMoney(totalAmount);
            return amount;
        }else if (order.getRefundStrategy() == RentalOrderStrategy.FULL.getCode()){
            BigDecimal totalAmount = order.getPayTotalMoney().add(amount);//计算价格
            order.setResourceTotalMoney(totalAmount);
            order.setPayTotalMoney(totalAmount);
        }

        return new BigDecimal(0);
    }

    public void refundOrder(RentalOrder order, long timestamp, BigDecimal orderAmount) {

        Long refundOrderNo = this.onlinePayService.createBillId(timestamp);

        if (order.getPaidVersion() == ActivityRosterPayVersionFlag.V2.getCode() ||
                order.getPaidVersion() == ActivityRosterPayVersionFlag.V3.getCode() ) {
            //新支付退款
            Long amount = changePayAmount(orderAmount);
            rentalv2PayService.refundOrder(order,amount);
        } else {
            refundParkingOrderV1(order, timestamp, refundOrderNo, orderAmount);
        }

        RentalRefundOrder rentalRefundOrder = new RentalRefundOrder();
        rentalRefundOrder.setOrderId(order.getId());
        rentalRefundOrder.setOrderNo(Long.valueOf(order.getOrderNo()));
        rentalRefundOrder.setRefundOrderNo(refundOrderNo);
        rentalRefundOrder.setResourceTypeId(order.getResourceTypeId());
        rentalRefundOrder.setOnlinePayStyleNo(VendorType.fromCode(order.getVendorType()).getStyleNo());
        rentalRefundOrder.setResourceType(order.getResourceType());

        rentalRefundOrder.setAmount(orderAmount);
        rentalRefundOrder.setStatus(SiteBillStatus.REFUNDING.getCode());
        rentalRefundOrder.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        if (UserContext.current().getUser()!=null)
             rentalRefundOrder.setCreatorUid(UserContext.current().getUser().getId());
        rentalRefundOrder.setOperateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        if (UserContext.current().getUser()!=null)
            rentalRefundOrder.setOperatorUid(UserContext.current().getUser().getId());

        this.rentalv2Provider.createRentalRefundOrder(rentalRefundOrder);
    }

    public Long changePayAmount(BigDecimal amount){

        if(amount == null){
            return 0L;
        }
        return  amount.multiply(new BigDecimal(100)).longValue();
    }

    private void refundParkingOrderV1(RentalOrder order, Long timestamp, Long refundOrderNo, BigDecimal amount) {
        PayZuolinRefundCommand refundCmd = new PayZuolinRefundCommand();
        String refundApi = this.configurationProvider.getValue(UserContext.getCurrentNamespaceId(), "pay.zuolin.refound", "POST /EDS_PAY/rest/pay_common/refund/save_refundInfo_record");
        String appKey = configurationProvider.getValue(UserContext.getCurrentNamespaceId(), "pay.appKey", "");

        Integer randomNum = (int) (Math.random() * 1000);

        refundCmd.setAppKey(appKey);
        refundCmd.setTimestamp(timestamp);
        refundCmd.setNonce(randomNum);
        refundCmd.setRefundOrderNo(String.valueOf(refundOrderNo));
        refundCmd.setOrderNo(String.valueOf(order.getOrderNo()));
        refundCmd.setOnlinePayStyleNo(VendorType.fromCode(order.getVendorType()).getStyleNo());
        refundCmd.setOrderType(OrderType.OrderTypeEnum.RENTALORDER.getPycode());
        //已付金额乘以退款比例除以100
        refundCmd.setRefundAmount(amount);
        refundCmd.setRefundMsg("预订单取消退款");
        this.setSignatureParam(refundCmd);

        PayZuolinRefundResponse refundResponse = (PayZuolinRefundResponse) this.restCall(refundApi, refundCmd, PayZuolinRefundResponse.class);
        if (refundResponse != null && refundResponse.getErrorCode().equals(HttpStatus.OK.value())) {
            //退款成功保存退款单信息，修改bill状态
        } else {
            LOGGER.error("Refund failed from vendor, refundCmd={}, response={}", refundCmd, refundResponse);
            throw RuntimeErrorException.errorWith(RentalServiceErrorCode.SCOPE,
                    RentalServiceErrorCode.ERROR_REFUND_ERROR,
                    "bill refund error");
        }
    }

    public Object restCall(String api, Object command, Class<?> responseType) {
        String host = this.configurationProvider.getValue(UserContext.getCurrentNamespaceId(),"pay.zuolin.host", "https://pay.zuolin.com");
        return RentalUtils.restCall(api, command, responseType, host);
    }

    public BigDecimal calculateRefundAmount(RentalOrder order, Long now) {
        if (null != order.getRefundStrategy()) {
            List<RentalRefundTip> tips = rentalv2Provider.listRefundTips(order.getResourceType(), RuleSourceType.RESOURCE.getCode(), order.getRentalResourceId(),
                    order.getRefundStrategy());
            String refundTip = null;
            if (tips != null && tips.size()>0)
                refundTip = tips.get(0).getTips();
            if (order.getRefundStrategy() == RentalOrderStrategy.CUSTOM.getCode()) {
                RentalDefaultRule rule = this.rentalv2Provider.getRentalDefaultRule(null, null,
                        order.getResourceType(), order.getResourceTypeId(), RuleSourceType.RESOURCE.getCode(), order.getRentalResourceId());

                List<RentalOrderRule> refundRules = rentalv2Provider.listRentalOrderRules(order.getResourceType(), rule.getSourceType(),
                        rule.getId(), RentalOrderHandleType.REFUND.getCode());

                List<RentalOrderRule> outerRules = refundRules.stream().filter(r -> r.getDurationType() == RentalDurationType.OUTER.getCode())
                        .collect(Collectors.toList());
                List<RentalOrderRule> innerRules = refundRules.stream().filter(r -> r.getDurationType() == RentalDurationType.INNER.getCode())
                        .collect(Collectors.toList());

                RentalOrderRule orderRule = null;

                Long startUseTime = order.getStartTime().getTime();

                long intervalTime = startUseTime - now;

                //处于时间外，查找最大的时间
                for (RentalOrderRule r: outerRules) {
                    long duration = 0;

                    if (r.getDurationUnit() == RentalDurationUnit.HOUR.getCode()) {
                        duration = (long)(r.getDuration() * 60 * 60 * 1000);
                    }
                    if (intervalTime > duration) {
                        if (null == orderRule || r.getDuration() > orderRule.getDuration()) {
                            orderRule = r;
                        }
                    }
                }
                if (orderRule == null) {
                    //处于时间内，查找最小的时间
                    for (RentalOrderRule r: innerRules) {
                        long duration = 0;

                        if (r.getDurationUnit() == RentalDurationUnit.HOUR.getCode()) {
                            duration = (long)(r.getDuration() * 60 * 60 * 1000);
                        }
                        if (intervalTime < duration) {
                            if (null == orderRule || r.getDuration() < orderRule.getDuration()) {
                                orderRule = r;
                            }
                        }
                    }
                }

                BigDecimal amount = order.getPaidMoney().multiply(new BigDecimal(orderRule.getFactor()))
                        .divide(new BigDecimal(100),2, RoundingMode.HALF_UP);

//                if (refundTip == null)
//                     processOrderCustomRefundTip(order, outerRules, innerRules, orderRule, amount);
//                else
                    order.setTip(refundTip);

                return amount;
            }else if (order.getRefundStrategy() == RentalOrderStrategy.FULL.getCode()) {
                order.setTip(refundTip);
                return order.getPaidMoney();
            }else if (order.getRefundStrategy() == RentalOrderStrategy.NONE.getCode()){
//                if (refundTip == null)
//                    order.setTip(processOrderNotRefundTip());
//                else
                    order.setTip(refundTip);
            }
        }

        order.setTip(processOrderNotRefundTip());

        return order.getPaidMoney();
    }


    public String processOrderNotRefundTip() {

        String locale = UserContext.current().getUser().getLocale();

        String content = localeStringService.getLocalizedString(RentalNotificationTemplateCode.SCOPE,
                String.valueOf(RentalNotificationTemplateCode.RENTAL_ORDER_NOT_REFUND_TIP), locale, "");

        return content;
    }

    public ServiceModuleApp getServiceMoudleAppByResourceTypeId(Long resourceTypeId){
        if (resourceTypeId == null)
            return null;
        List<ServiceModuleApp> apps = serviceModuleAppService.listReleaseServiceModuleApp(
                UserContext.getCurrentNamespaceId(),
                ServiceModuleConstants.RENTAL_MODULE,
                null, resourceTypeId.toString(), null);
        if (apps != null && apps.size() > 0)
            return apps.get(0);
        return null;
    }

    public String processResourceCustomRefundTip(RentalDefaultRule rule){
        List<RentalOrderRule> refundRules = rentalv2Provider.listRentalOrderRules(rule.getResourceType(), rule.getSourceType(),
                rule.getId(), RentalOrderHandleType.REFUND.getCode());

        List<RentalOrderRule> outerRules = refundRules.stream().filter(r -> r.getDurationType() == RentalDurationType.OUTER.getCode())
                .collect(Collectors.toList());
        List<RentalOrderRule> innerRules = refundRules.stream().filter(r -> r.getDurationType() == RentalDurationType.INNER.getCode())
                .collect(Collectors.toList());
        StringBuilder sb = new StringBuilder();
        sb.append("亲爱的用户，为保障资源使用效益，如在服务开始前取消订单，将扣除您订单金额的一定比例数额，恳请您谅解。具体规则如下：");
        sb.append("\r\n");
        int i = 0;
        for (int  size = outerRules.size(); i < size; i++) {
            sb.append(i+1);
            sb.append("，");
            sb.append("订单开始前");
            sb.append(outerRules.get(i).getDuration());
            sb.append("小时外取消，退还");
            sb.append(outerRules.get(i).getFactor().intValue());
            sb.append("%订单金额;");
            sb.append("\r\n");
        }

        for (int j=0, size = innerRules.size(); j < size; j++) {
            sb.append(i+j+1);
            sb.append("，");
            sb.append("订单开始前");
            sb.append(innerRules.get(j).getDuration());
            sb.append("小时内取消，退还");
            sb.append(innerRules.get(j).getFactor().intValue());
            sb.append("%订单金额。");
        }
        return sb.toString();
    }

    public void processOrderCustomRefundTip(RentalOrder order, List<RentalOrderRule> outerRules, List<RentalOrderRule> innerRules,
                                        RentalOrderRule orderRule, BigDecimal amount) {

        StringBuilder sb = new StringBuilder();
        sb.append("亲爱的用户，为保障资源使用效益，如在服务开始前取消订单，将扣除您订单金额的一定比例数额，恳请您谅解。具体规则如下：");
        //sb.append("\r\n");
        //sb.append("\r\n");
        int i = 0;
        for (int  size = outerRules.size(); i < size; i++) {
            sb.append(i+1);
            sb.append("，");
            sb.append("订单开始前");
            sb.append(outerRules.get(i).getDuration());
            sb.append("小时外取消，退还");
            sb.append(outerRules.get(i).getFactor().intValue());
            sb.append("%订单金额;");
          //  sb.append("\r\n");
        }

        for (int j=0, size = innerRules.size(); j < size; j++) {
            sb.append(i+j+1);
            sb.append("，");
            sb.append("订单开始前");
            sb.append(innerRules.get(j).getDuration());
            sb.append("小时内取消，退还");
            sb.append(innerRules.get(j).getFactor().intValue());
            sb.append("%订单金额;");
         //   sb.append("\r\n");
        }
       // sb.append("\r\n");
        sb.append("如果您现在取消订单，将退还");
        sb.append(amount);
        sb.append("元（");
        sb.append(orderRule.getFactor().intValue());
        sb.append("%）。");

        if (order.getPayMode() == PayMode.OFFLINE_PAY.getCode()){
           // sb.append("\r\n");
            sb.append("请在确认后联系客服线下退款:");
            RentalResource rs = getRentalResource(order.getResourceType(),order.getRentalResourceId());
            if (rs.getOfflinePayeeUid()!=null){
                OrganizationMember member = organizationProvider.findOrganizationMemberByUIdAndOrgId(rs.getOfflinePayeeUid(), rs.getOrganizationId());
                if(null!=member){
                    sb.append(member.getContactName());
                    UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByOwnerAndType(member.getTargetId(), IdentifierType.MOBILE.getCode());
                    if (userIdentifier!=null)
                        sb.append("("+userIdentifier.getIdentifierToken()+")");
                }
            }
        }

        order.setTip(sb.toString());
    }

    /***给支付相关的参数签名*/
    public void setSignatureParam(PayZuolinRefundCommand cmd) {
        App app = appProvider.findAppByKey(cmd.getAppKey());

        Map<String,String> map = new HashMap<>();
        map.put("appKey",cmd.getAppKey());
        map.put("timestamp",cmd.getTimestamp()+"");
        map.put("nonce",cmd.getNonce()+"");
        map.put("refundOrderNo",cmd.getRefundOrderNo());
        map.put("orderNo", cmd.getOrderNo());
        map.put("onlinePayStyleNo",cmd.getOnlinePayStyleNo() );
        map.put("orderType",cmd.getOrderType() );
        //modify by wh 2016-10-24 退款使用toString,下订单的时候使用doubleValue,两边用的不一样,为了和电商保持一致,要修改成toString
//		map.put("refundAmount", cmd.getRefundAmount().doubleValue()+"");
        map.put("refundAmount", cmd.getRefundAmount().toString());
        map.put("refundMsg", cmd.getRefundMsg());
        String signature = SignatureHelper.computeSignature(map, app.getSecretKey());
        cmd.setSignature(signature);
    }
}
