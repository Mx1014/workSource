// @formatter:off
package com.everhomes.activity;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.coordinator.CoordinationLocks;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.order.OrderEmbeddedHandler;
import com.everhomes.rest.activity.ActivityCancelSignupCommand;
import com.everhomes.rest.activity.ActivityCancelType;
import com.everhomes.rest.activity.ActivityRosterPayFlag;
import com.everhomes.rest.activity.ActivityServiceErrorCode;
import com.everhomes.rest.order.OrderType;
import com.everhomes.rest.order.PayCallbackCommand;
import com.everhomes.util.RuntimeErrorException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Component(OrderEmbeddedHandler.ORDER_EMBEDED_OBJ_RESOLVER_PREFIX + OrderType.ACTIVITY_SIGNUP_ORDER_WECHAT_CODE )
public class ActivitySignupOrderWechatEmbeddedHandler implements OrderEmbeddedHandler{
	
    private static final Logger LOGGER = LoggerFactory.getLogger(ActivitySignupOrderWechatEmbeddedHandler.class);
	
	@Override
	public void paySuccess(PayCallbackCommand cmd) {
		LOGGER.info("ActivitySignupOrderWechatEmbeddedHandler paySuccess cmd = {}, turn to ActivitySignupOrderEmbeddedHandler", cmd);
		//微信公众号支付
		OrderEmbeddedHandler handler = PlatformContext.getComponent(OrderEmbeddedHandler.ORDER_EMBEDED_OBJ_RESOLVER_PREFIX+OrderType.ACTIVITY_SIGNUP_ORDER_CODE);
		handler.paySuccess(cmd);

	}

	@Override
	public void payFail(PayCallbackCommand cmd) {
		LOGGER.info("ActivitySignupOrderWechatEmbeddedHandler payFail cmd = {}, turn to ActivitySignupOrderEmbeddedHandler", cmd);
		OrderEmbeddedHandler handler = PlatformContext.getComponent(OrderEmbeddedHandler.ORDER_EMBEDED_OBJ_RESOLVER_PREFIX+OrderType.ACTIVITY_SIGNUP_ORDER_CODE);
		handler.payFail(cmd);
	}

}
