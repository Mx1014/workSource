package com.everhomes.rentalv2;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.messaging.MessagingService;
import com.everhomes.rest.app.AppConstants;
import com.everhomes.rest.common.RentalOrderActionData;
import com.everhomes.rest.common.Router;
import com.everhomes.rest.messaging.*;
import com.everhomes.rest.rentalv2.RuleSourceType;
import com.everhomes.rest.rentalv2.admin.RentalDurationType;
import com.everhomes.rest.rentalv2.admin.RentalDurationUnit;
import com.everhomes.rest.rentalv2.admin.RentalOrderHandleType;
import com.everhomes.rest.rentalv2.admin.RentalOrderStrategy;
import com.everhomes.rest.user.MessageChannelType;
import com.everhomes.user.User;
import com.everhomes.util.RouterBuilder;
import com.everhomes.util.StringHelper;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
        // 发消息 +推送
        messagingService.routeMessage(User.SYSTEM_USER_LOGIN, AppConstants.APPID_MESSAGING, MessageChannelType.USER.getCode(),
                userId.toString(), messageDto, MessagingConstants.MSG_FLAG_STORED_PUSH.getCode());
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

    public BigDecimal calculateOverTimeFee(RentalOrder order, long now) {

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
            return order.getPaidMoney();
        }
        return order.getPaidMoney();
    }

    public BigDecimal calculateRefundAmount(RentalOrder order, Long now) {

        if (null != order.getRefundStrategy()) {
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
                        .divide(new BigDecimal(100), RoundingMode.HALF_UP);

                processOrderCustomRefundTip(order, outerRules, innerRules, orderRule, amount);

                return amount;
            }else if (order.getRefundStrategy() == RentalOrderStrategy.FULL.getCode()) {
                return order.getPaidMoney();
            }
        }

        processOrderNotRefundTip(order);

        return order.getPaidMoney();
    }

    public void processOrderNotRefundTip(RentalOrder order) {
        StringBuilder sb = new StringBuilder();
        sb.append("亲爱的用户，为保障资源使用效益，现在取消订单，系统将不予退款，恳请您谅解。");
        sb.append("\r\n");
        sb.append("\r\n");
        sb.append("确认要取消订单吗？");
    }

    public void processOrderCustomRefundTip(RentalOrder order, List<RentalOrderRule> outerRules, List<RentalOrderRule> innerRules,
                                        RentalOrderRule orderRule, BigDecimal amount) {

        StringBuilder sb = new StringBuilder();
        sb.append("亲爱的用户，为保障资源使用效益，如在服务开始前取消订单，将扣除您订单金额的一定比例数额，恳请您谅解。具体规则如下：");
        sb.append("\r\n");
        sb.append("\r\n");
        for (int i = 0, size = outerRules.size(); i < size; i++) {
            sb.append(i + 1);
            sb.append("，");
            sb.append("订单开始前");
            sb.append(outerRules.get(i).getDuration());
            sb.append("小时外取消，退还");
            sb.append(outerRules.get(i).getFactor());
            sb.append("%订单金额");
            sb.append("\r\n");
        }

        for (int i = 0, size = innerRules.size(); i < size; i++) {
            sb.append(i + 1);
            sb.append("，");
            sb.append("订单开始前");
            sb.append(outerRules.get(i).getDuration());
            sb.append("小时内取消，退还");
            sb.append(outerRules.get(i).getFactor());
            sb.append("%订单金额");
            sb.append("\r\n");
        }
        sb.append("\r\n");
        sb.append("如果您现在取消订单，将退还");
        sb.append(amount);
        sb.append("元（");
        sb.append(orderRule.getFactor());
        sb.append("%）。");

        order.setTip(sb.toString());
    }
}
