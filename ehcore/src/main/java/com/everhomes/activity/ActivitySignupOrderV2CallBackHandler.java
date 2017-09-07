// @formatter:off
package com.everhomes.activity;

import com.everhomes.coordinator.CoordinationLocks;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.order.OrderEmbeddedHandler;
import com.everhomes.order.PaymentCallBackHandler;
import com.everhomes.rest.activity.ActivityCancelSignupCommand;
import com.everhomes.rest.activity.ActivityCancelType;
import com.everhomes.rest.activity.ActivityRosterPayFlag;
import com.everhomes.rest.activity.ActivityServiceErrorCode;
import com.everhomes.rest.order.OrderType;
import com.everhomes.rest.order.PayCallbackCommand;
import com.everhomes.rest.order.PaymentCallBackCommand;
import com.everhomes.util.RuntimeErrorException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Component(PaymentCallBackHandler.ORDER_PAYMENT_BACK_HANDLER_PREFIX + OrderType.ACTIVITY_SIGNUP_ORDER_CODE )
public class ActivitySignupOrderV2CallBackHandler implements PaymentCallBackHandler {
	
    private static final Logger LOGGER = LoggerFactory.getLogger(ActivitySignupOrderV2CallBackHandler.class);

    @Autowired
    private ActivityProivider activityProvider;
    
    @Autowired
    private ActivityService activityService;
    
	@Autowired
	private CoordinationProvider coordinationProvider;

	@Override
	public void paySuccess(PaymentCallBackCommand cmd) {
		LOGGER.info("ActivitySignupOrderV2CallBackHandler paySuccess start cmd = {}", cmd);

		ActivityRoster roster = activityProvider.findRosterByOrderNo(cmd.getOrderId());
		if(roster == null){
			throw RuntimeErrorException.errorWith(ActivityServiceErrorCode.SCOPE, ActivityServiceErrorCode.ERROR_NO_ROSTER,
					"no roster.");
		}
		Activity activity = activityProvider.findActivityById(roster.getActivityId());
		if(activity == null){
			throw RuntimeErrorException.errorWith(ActivityServiceErrorCode.SCOPE, ActivityServiceErrorCode.ERROR_INVALID_ACTIVITY_ID,
					"no activity.");
		}
		//检验支付结果和应价格是否相等
		checkPayAmount(cmd.getAmount(), activity.getChargePrice());
		//支付宝回调时，可能会同时回调多次，
		this.coordinationProvider.getNamedLock(CoordinationLocks.UPDATE_ACTIVITY_ROSTER.getCode() + roster.getId()).enter(()-> {
			roster.setPayFlag(ActivityRosterPayFlag.PAY.getCode());
			roster.setPayTime(new Timestamp(Long.valueOf(cmd.getPayDateTime())));
			//TODO check Long to BigDecimal
			roster.setPayAmount(new BigDecimal(cmd.getAmount().doubleValue()/100));
			roster.setVendorType(String.valueOf(cmd.getPaymentType()));
			roster.setOrderType(cmd.getOrderType());
			activityProvider.updateRoster(roster);
			return null;
		});
		LOGGER.info("ActivitySignupOrderV2CallBackHandler paySuccess end");
	}

	@Override
	public void payFail(PaymentCallBackCommand cmd) {
		LOGGER.info("ActivitySignupOrderV2CallBackHandler payFail cmd = {}", cmd);

		if(LOGGER.isDebugEnabled())
			LOGGER.error("onlinePayBillFail");
		ActivityRoster roster = activityProvider.findRosterByOrderNo(cmd.getOrderId());
		if(roster == null){
			throw RuntimeErrorException.errorWith(ActivityServiceErrorCode.SCOPE, ActivityServiceErrorCode.ERROR_NO_ROSTER,
					"no roster.");
		}
		ActivityCancelSignupCommand cancelCmd = new ActivityCancelSignupCommand();
		cancelCmd.setActivityId(roster.getActivityId());
		cancelCmd.setUserId(roster.getUid());
		cancelCmd.setCancelType(ActivityCancelType.PAY_FAIL.getCode());
		activityService.cancelSignup(cancelCmd);
		LOGGER.info("ActivitySignupOrderV2CallBackHandler payFail end");
	}

	
	
	private void checkPayAmount(Long payAmount, BigDecimal chargePrice) {
		if(payAmount == null || chargePrice == null || payAmount.longValue() != chargePrice.longValue() * 100){
			LOGGER.error("payAmount and chargePrice is not equal.");
			throw RuntimeErrorException.errorWith(ActivityServiceErrorCode.SCOPE, ActivityServiceErrorCode.ERROR_PAYAMOUNT_ERROR,
					"payAmount and chargePrice is not equal.");
		}
	}
}
